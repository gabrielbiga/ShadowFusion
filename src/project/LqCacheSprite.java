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
import java.awt.Image;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqCacheSprite {
    private LqAsset asset;
    private Sprite sprite;
    
    private float width;
    private float height;

    public LqAsset getAsset() {
        return asset;
    }

    public void setAsset(LqAsset asset) {
        this.asset = asset;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        
        this.height = sprite.getHeight();
        this.width  = sprite.getWidth();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
