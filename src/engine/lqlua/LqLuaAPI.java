/*
 * ========= (C) Copyright 2014 Liquare, L.L.C. All rights reserved. ===========
 * 
 * The copyright to the contents herein is the property of Liquare, L.L.C.
 * The contents may be used and/or copied only with the written permission of
 * Liquare, L.L.C., or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the contents have been supplied.
 * 
 * $Header: $
 * $NoKeywords: $
 * =============================================================================
 */
package engine.lqlua;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.swing.JOptionPane;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import project.LqAsset;
import project.CacheSprites;
import project.LqProject;
import project.LqSprite;
import ui.LuaConsole;

/**
 * The Liquare's LUA compiler API for ShadowFusion Engine
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqLuaAPI {
    
    public static void implementLqLuaAPI(ScriptEngine engine) {
        engine.put("sleep", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                try {
                    /*System.out.println("arg "+arg.tojstring());
                     return LuaValue.valueOf(123);*/

                    Thread.sleep(Integer.parseInt(arg.tojstring()));
                } catch (InterruptedException ex) {
                    Logger.getLogger(LuaConsole.class.getName()).log(Level.SEVERE, null, ex);
                }

                return null;
            }
        });
        
        engine.put("showMessage", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                JOptionPane.showMessageDialog(null, arg.tojstring(), "LqLUA Api", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }
        });
        
        engine.put("getStageComponent", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue arg, LuaValue arg2) {
                int idxMap = Integer.parseInt(arg.tojstring());
                int idxObj = Integer.parseInt(arg2.tojstring());
                
               // LqSprite sp = LqProject.maps.getMap(idxMap).layers.getLayer(0).sprites.get(idxObj);
                
                //LuaValue x = CoerceJavaToLua.coerce(sp);
                        
                LuaValue x = null;
                
                return x;
            }
        });
        
        
        engine.put("switchSpriteDisk", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue arg, LuaValue arg2, LuaValue arg3) {
                String path = arg.tojstring();
                int idxMap = Integer.parseInt(arg2.tojstring());
                int idxObj = Integer.parseInt(arg3.tojstring());
                
                LqAsset newAsset = new LqAsset();
                //newAsset.name = name;
                newAsset.path = path;
                
                //LqProject.maps.getMap(idxMap).layers.getLayer(0).sprites.get(idxObj).asset = newAsset;
                
              //  LuaValue x = CoerceJavaToLua.coerce(CacheSprites.addCacheSprite(newAsset));
                        
                return null;
            }
        });
         
        engine.put("convertLuaToJava", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue arg, LuaValue arg2) {
                String path = arg.tojstring();
                String name = arg2.tojstring();
                
                LqAsset newAsset = new LqAsset();
                newAsset.name = name;
                newAsset.path = path;
                
                
                
                //REVER ISSo
                LuaValue x = null; // CoerceJavaToLua.coerce(CacheSprites.addCacheSprite(newAsset));
                        
                return x;
            }
        });
    }
}
