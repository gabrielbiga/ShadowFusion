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
import java.util.List;

/**
 * LUA Compiler script threads
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqLuaThreads {
    //Thread Class
    private static class LqLuaThread {
        protected String name;
        protected Thread memoryScript;
    }
    
    //Memory Array
    private static ArrayList<LqLuaThread> memoryScripts;
    private static int count = 0;
    //---
            
    protected static void addThread(String name, Thread object, boolean start) {
        if (memoryScripts == null) 
            memoryScripts = new ArrayList<>();
        
        LqLuaThread newThread = new LqLuaThread();
        newThread.name = name;
        newThread.memoryScript = object;
        
        memoryScripts.add(newThread);
        
        if (start)
            memoryScripts.get(count).memoryScript.start();
        
        count++;
    }
    
    protected static int count() {
        return count;
    }
}
