/*
 * ========= (C) Copyright 2014 Liquare, L.L.C. All rights reserved. ===========
 * 
 * The copyright to the contents herein is the property of Liquare, L.L.C.
 * The contents may be used and/or copied only with the written permission of
 * Liquare, L.L.C., or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the contents have been supplied.
 * 
 * The Liquare's Lua pre-compiler main class.
 * 
 * $Header: $
 * $NoKeywords: $
 * =============================================================================
 */

package engine.lqlua;

import engine.LqLog;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import ui.LuaConsole;
import engine.Stage;

/**
 * Liquare's LUA compiler main class
 * 
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqLua extends LqLuaThreads {
    
    public static void execute(String name, final String externalScript, final LqLuaVisualReturn echo) {
        Thread newWorker;
        newWorker = new Thread() {
            
            @Override
            public void run() {
                ScriptEngineManager sem = new ScriptEngineManager();
                ScriptEngine e = sem.getEngineByName("luaj");
                ScriptEngineFactory f = e.getFactory();

                Reader input = new CharArrayReader("abcdefg\nhijk".toCharArray());
                CharArrayWriter output = new CharArrayWriter();

                //Rectangle Test
                //e.put("rect", Stage.retangulo);
                
                //Liquare LUA API
                LqLuaAPI.implementLqLuaAPI(e);
                
                //String errors = "";
                String script = externalScript;
                
                e.getContext().setReader(input);
                e.getContext().setWriter(output);
                
                try {
                    e.eval(script);
                } catch (ScriptException ex) {
                   new LqLuaException(ex.getMessage());
                }
                
                if (echo != null) {
                    echo.echo(output.toString());
                } else {
                    LqLog.log(output.toString());
                }
            }
        };
        
        LqLuaThreads.addThread(name, newWorker, true);
    }
    
    //-----------------------------------------------------------------------------
    // Don't allow instance this class!
    //-----------------------------------------------------------------------------
    public LqLua() {}
}
