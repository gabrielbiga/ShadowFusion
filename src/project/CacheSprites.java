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

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class CacheSprites {

    private ArrayList<LqCacheSprite> cache = new ArrayList<>();
    
    public LqCacheSprite addCacheSprite(LqAsset asset, Sprite oi) {
        LqCacheSprite newCacheSprite = new LqCacheSprite();
        newCacheSprite.setAsset(asset);
        newCacheSprite.setSprite(oi);
        
        cache.add(newCacheSprite);
        
        return newCacheSprite;
    }
    
    public LqCacheSprite getCacheSprite(int index) {
        return cache.get(index);
    }
    
    //TODO: Implement BinarySearch
    //@Deprecated
    public LqCacheSprite getSearchCacheSprite(LqAsset asset) {
        /*int count = getCount() ;
        
        if (count == 0)
            return null;
        
        int i;
        boolean flag = false;
        for(i = 0; i < count; i++) {
            if (cache.get(i).getAsset().equals(asset)) {
                flag = true;
                break;
            }
        }
        
        return flag ? cache.get(i) : null; //WTF????*/
        
        int count = getCount() ;
        
        if (count == 0)
            return null;
        
        for(int i = 0; i < count; i++) {
            if (cache.get(i).getAsset().equals(asset)) {
                return cache.get(i);
            }
        }
        
        return null;
    }
    
    //TODO: Implement BinarySearch
    public LqCacheSprite getSearchCacheSprite(String path) {
        int count = getCount() ;
        
        if (count == 0)
            return null;
        
        int i;
        for(i = 0; i < count; i++) {
            if (cache.get(i).getAsset().path.equals(path)) {
                return cache.get(i);
            }
        }
        
        return null;
    }
    
    public int getCount() {
        return cache.size();
    }
}
