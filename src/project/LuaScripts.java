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

package project;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LuaScripts implements Serializable {
    private ArrayList<LqLuaScript> scripts = new ArrayList<>();
    
    public void addScript(String name, String content) {
        LqLuaScript newScript = new LqLuaScript();
        
        newScript.name = name;
        newScript.script = content;
        
        scripts.add(newScript);
    }
    
    public LqLuaScript getScript(int index) {
        return scripts.get(index);
    }
    
    public int getCount() {
        return scripts.size();
    }
}
