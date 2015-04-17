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

package ui;

import project.LqAsset;
import project.LqCacheSprite;
import project.LqMapObject;
import project.LqSprite;

public class RunTimeConfig {
    
    //Style
    public volatile static boolean blackStyle = true;
    
    //Configs
    public volatile static boolean showGrid     = true;
    public volatile static boolean onlyViewport = false;
    public volatile static boolean showFPS      = true;
    public volatile static boolean showLight    = false;
    public volatile static boolean isWarpContent;
    
    //UI ObjectCache
    public /*volatile*/ static ObjectCache treeProjectCache = new ObjectCache();
    public /*volatile*/ static ObjectCache treeAssetCache  = new ObjectCache();
    public /*volatile*/ static ObjectCache mapCacheUI  = new ObjectCache();
    
    //UI Selected
    //public volatile static LqCacheSprite assetSelected;  //Asset selected to put on stage
    public static LqMapObject assetSelected;
    public volatile static LqMapObject objectSelected; //Asset seletected on the stage to realtime edit in editor
    
    //I/O
    public volatile static String projectOpened;
    
    //Don't instance that!        
    public RunTimeConfig() {} 
    
    public static void destroy() {
        treeProjectCache = new ObjectCache();
        treeAssetCache  = new ObjectCache();
        mapCacheUI  = new ObjectCache();
        
        assetSelected = null;
        objectSelected = null;
        
        projectOpened = null;
        
        System.gc();
    }
}
