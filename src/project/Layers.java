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
import java.util.Collections;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class Layers implements Serializable {

    private ArrayList<LqLayer> layers = new ArrayList<>();

    public void addLayer(int order, String name) {
        LqLayer newLayer = new LqLayer();
        newLayer.order = order;
        newLayer.name = name;

        layers.add(newLayer);
    }

    public LqLayer getLayer(int index) {
        return layers.get(index);
    }

    public int getCount() {
        return layers.size();
    }

    public void addObject(int indexLayer, LqMapObject sprite) {
        layers.get(indexLayer).sprites.add(sprite);
    }

    public int getSpriteCount(int indexLayer) {
        return layers.get(indexLayer).sprites.size();
    }
    
    public int searchIdxLayer(LqLayer layer) {
        if (getCount() == 0)
            return -1;
        
        for(int i = 0; i < getCount(); i++) {
            if(getLayer(i).equals(layer)) {
                return i;
            }
        }
        
        return -1;
    }
}
