package io.github.techtastic.scriptables.api.lua

import org.luaj.vm2.*
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseMathLib
import org.luaj.vm2.lib.jse.JseStringLib

class LuaSandbox {
    // These globals are used by the server to compile scripts.
    val serverGlobals = Globals()

    init {
        // Create server globals with just enough library support to compile user scripts.
        serverGlobals.load(JseBaseLib())
        serverGlobals.load(PackageLib())
        serverGlobals.load(JseStringLib())

        // To load scripts, we occasionally need a math library in addition to compiler support.
        // To limit scripts using the debug library, they must be closures, so we only install LuaC.
        serverGlobals.load(JseMathLib())
        LoadState.install(serverGlobals)
        LuaC.install(serverGlobals)

        // Set up the LuaString metatable to be read-only since it is shared across all scripts.
        LuaString.s_metatable = ReadOnlyLuaTable(LuaString.s_metatable)
    }

    fun main(args: Array<String>) {
        // Example normal scripts that behave as expected.
        runScriptInSandbox("return 'foo'")
        runScriptInSandbox("return ('abc'):len()")
        runScriptInSandbox("return getmetatable('abc')")
        runScriptInSandbox("return getmetatable('abc').len")
        runScriptInSandbox("return getmetatable('abc').__index")

        // Example user scripts that attempt rogue operations, and will fail.
        runScriptInSandbox("return setmetatable('abc', {})")
        runScriptInSandbox("getmetatable('abc').len = function() end")
        runScriptInSandbox("getmetatable('abc').__index = {}")
        runScriptInSandbox("getmetatable('abc').__index.x = 1")
        runScriptInSandbox("while true do print('loop') end")

        // Example use of other shared metatables, which should also be made read-only.
        // This toy example allows booleans to be added to numbers.
        runScriptInSandbox("return 5 + 6, 5 + true, false + 6")
        LuaBoolean.s_metatable = ReadOnlyLuaTable(
            LuaValue.tableOf(
                arrayOf<LuaValue>(
                    LuaValue.ADD, object : TwoArgFunction() {
                        override fun call(x: LuaValue, y: LuaValue): LuaValue? {
                            return valueOf(
                                (if (x === TRUE) 1.0 else x.todouble()) +
                                        if (y === TRUE) 1.0 else y.todouble()
                            )
                        }
                    })
            )
        )
        runScriptInSandbox("return 5 + 6, 5 + true, false + 6")
    }

    fun runScriptInSandbox(script: String) = this.runScriptInSandbox(script, mutableListOf())

    fun runScriptInSandbox(script: String, logger: MutableList<Pair<Boolean, String>>) = this.runScriptInSandbox(script, listOf(), logger)

    // Run a script in a lua thread and limit it to a certain number
    // of instructions by setting a hook function.
    // Give each script its own copy of globals, but leave out libraries
    // that contain functions that can be abused.
    fun runScriptInSandbox(script: String, customLibraries: List<TwoArgFunction>, logger: MutableList<Pair<Boolean, String>>) {

        // Each script will have its own set of globals, which should
        // prevent leakage between scripts running on the same server.
        val userGlobals = Globals()
        userGlobals.load(JseBaseLib())
        userGlobals.load(PackageLib())
        userGlobals.load(Bit32Lib())
        userGlobals.load(TableLib())
        userGlobals.load(JseStringLib())
        userGlobals.load(JseMathLib())
        customLibraries?.forEach(userGlobals::load)

        //userGlobals.load(ScriptablesAPI.parseAPI(TestLib()))

        // This library is dangerous as it gives unfettered access to the
        // entire Java VM, so it's not suitable within this lightweight sandbox.
        // user_globals.load(new LuajavaLib());

        // Starting coroutines in scripts will result in threads that are
        // not under the server control, so this library should probably remain out.
        // user_globals.load(new CoroutineLib());

        // These are probably unwise and unnecessary for scripts on servers,
        // although some date and time functions may be useful.
        // user_globals.load(new JseIoLib());
        // user_globals.load(new JseOsLib());

        // Loading and compiling scripts from within scripts may also be
        // prohibited, though in theory it should be fairly safe.
        // LoadState.install(user_globals);
        // LuaC.install(user_globals);

        // The debug library must be loaded for hook functions to work, which
        // allow us to limit scripts to run a certain number of instructions at a time.
        // However, we don't wish to expose the library in the user globals,
        // so it is immediately removed from the user globals once created.
        userGlobals.load(DebugLib())
        val sethook: LuaValue = userGlobals.get("debug").get("sethook")
        userGlobals.set("debug", LuaValue.NIL)

        // Set up the script to run in its own lua thread, which allows us
        // to set a hook function that limits the script to a specific number of cycles.
        // Note that the environment is set to the user globals, even though the
        // compiling is done with the server globals.
        val chunk: LuaValue = serverGlobals.load(script, "main", userGlobals)
        val thread = LuaThread(userGlobals, chunk)

        // Set the hook function to immediately throw an Error, which will not be
        // handled by any Lua code other than the coroutine.
        val hookfunc: LuaValue = object : ZeroArgFunction() {
            override fun call(): LuaValue? {
                // A simple lua error may be caught by the script, but a
                // Java Error will pass through to top and stop the script.
                throw Error("Script overran resource limits.")
            }
        }
        val instructionCount = 20
        sethook.invoke(
            LuaValue.varargsOf(
                arrayOf<LuaValue>(
                    thread, hookfunc,
                    LuaValue.EMPTYSTRING, LuaValue.valueOf(instructionCount)
                )
            )
        )

        // When we resume the thread, it will run up to 'instruction_count' instructions
        // then call the hook function which will error out and stop the script.
        val result: Varargs = thread.resume(LuaValue.NIL)
        println("[[$script]] -> $result")
        logger.add(Pair(result.toboolean(1), result.optjstring(2, "")))
    }

    // Simple read-only table whose contents are initialized from another table.
    internal class ReadOnlyLuaTable(table: LuaValue) : LuaTable() {
        init {
            presize(table.length(), 0)
            var n: Varargs = table.next(LuaValue.NIL)
            while (!n.arg1().isnil()) {
                val key: LuaValue = n.arg1()
                val value: LuaValue = n.arg(2)
                super.rawset(key, if (value.istable()) ReadOnlyLuaTable(value) else value)
                n = table
                    .next(n.arg1())
            }
        }

        override fun setmetatable(metatable: LuaValue?): LuaValue {
            return error("table is read-only")
        }

        override fun set(key: Int, value: LuaValue?) {
            error("table is read-only")
        }

        override fun rawset(key: Int, value: LuaValue?) {
            error("table is read-only")
        }

        override fun rawset(key: LuaValue?, value: LuaValue?) {
            error("table is read-only")
        }

        override fun remove(pos: Int): LuaValue {
            return error("table is read-only")
        }
    }
}