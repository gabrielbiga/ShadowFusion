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

import java.util.ArrayList;

/**
 * Liquare's LUA PreCompiler
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqLuaPreCompiler {
    private class IncludeLib {
        String name;
        String path;
    }
    
    private class SymbolicVar {
        String name;
        String content;
    }
    
    private String script;
    
    private ArrayList<IncludeLib>  includeLibs;
    private ArrayList<SymbolicVar> symbolicVars;
    
    public LqLuaPreCompiler(String script) {
        symbolicVars = new ArrayList<>();
        
        this.script = script;
    }
    
    public String preCompile() {
        int posI;
        
        while ((posI = script.indexOf("#define")) != -1) {
            String directive = "";
        
            for(int i = posI; script.charAt(i) != ';'; i++) {
                directive += script.charAt(i);
            }

            String explode[] = directive.split(" ");

            SymbolicVar newVar = new SymbolicVar();

            newVar.name    = explode[1];
            newVar.content = explode[2];

            symbolicVars.add(newVar);
            
            //clear
            script = script.replaceAll(directive + ";", "");
        }
        
        return symbolicVars.get(0).name;
    }
}
