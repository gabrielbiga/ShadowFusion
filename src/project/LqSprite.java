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

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqSprite extends LqMapObject implements Serializable {
    
    private static final long serialVersionUID = 3475863746316745585l;

    public LqAsset asset;
   
    public double random;
    
    public String name = "Sprite";
    
    public LqSprite() {
        random = Math.random();
    }
    
}
