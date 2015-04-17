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
public class Maps implements Serializable{

    private ArrayList<LqMap> maps = new ArrayList<>();
    
    public void addMap(String name) {
        LqMap newMap = new LqMap();
        newMap.name = name;
        maps.add(newMap);
    }
    
    public LqMap getMap(int index) {
        return maps.get(index);
    }
    
    public int getCount() {
        return maps.size();
    }
}
