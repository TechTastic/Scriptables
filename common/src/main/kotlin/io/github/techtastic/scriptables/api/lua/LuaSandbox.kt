package io.github.techtastic.scriptables.api.lua

import org.luaj.vm2.*
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseMathLib
import org.luaj.vm2.lib.jse.JseStringLib

object LuaSandbox {
    fun runScript(script: String, limit: Int = 20, libraries: List<TwoArgFunction> = listOf()): LuaThread {
        // Each script will have its own set of globals, which should
        // prevent leakage between scripts running on the same server.
        val userGlobals = Globals()
        userGlobals.load(JseBaseLib())
        userGlobals.load(PackageLib())
        userGlobals.load(Bit32Lib())
        userGlobals.load(TableLib())
        userGlobals.load(JseStringLib())
        userGlobals.load(JseMathLib())

        // Replacing `print()` so it uses the custom logger
        userGlobals.load(object: TwoArgFunction() {
            override fun call(modname: LuaValue, env: LuaValue): LuaValue {
                env.set("print", object: VarArgFunction() {
                    override fun invoke(args: Varargs): Varargs {
                        val tostring: LuaValue = serverGlobals.get("tostring")
                        var i = 1
                        val n = args.narg()
                        while (i <= n) {
                            val s = tostring.call(args.arg(i)).strvalue()
                            println(s.tojstring())
                            i++
                        }
                        return NONE
                    }
                })
                return env
            }
        })

        // Add Custom Libraries
        libraries.forEach(userGlobals::load)

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

        sethook.invoke(
            LuaValue.varargsOf(
                arrayOf<LuaValue>(
                    thread,
                    hookfunc,
                    LuaValue.EMPTYSTRING,
                    LuaValue.valueOf(limit)
                )
            )
        )

        // When we resume the thread, it will run up to 'instruction_count' instructions
        // then call the hook function which will error out and stop the script.
        val result: Varargs = thread.resume(LuaValue.NIL)
        println("[[$script]] -> $result")

        return thread
    }

    val serverGlobals: Globals
        get() {
            val g = Globals()
            g.load(JseBaseLib())
            g.load(PackageLib())
            g.load(JseStringLib())

            g.load(JseMathLib())
            LoadState.install(g)
            LuaC.install(g)

            LuaString.s_metatable = ReadOnlyLuaTable(LuaString.s_metatable)

            return g
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