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
 public class LqLayer implements Serializable {
    public int order;
    public String name;
    public ArrayList<LqMapObject> sprites = new ArrayList<>();
    
    
    public void flipSpritePosition(int idxOrigin, int idxDest) {
        Collections.swap(sprites, idxOrigin, idxDest);
    }
}