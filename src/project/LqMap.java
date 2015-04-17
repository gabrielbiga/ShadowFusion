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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqMap implements Serializable {

    private static final long serialVersionUID = 4323570619442916641l;

    //Fields
    public String name;
    public Layers layers;
    
   
    
    //Blocks
    public int xBlocks;
    public int yBlocks;
    public Dimension SizeBlock = new Dimension(30, 30); //30x30 Default
    
    //Camera and Scale
    public LqCamera mainCamera = new LqCamera();
    public Point2D scale = new Point2D.Double(1, 1); //*Long* Up to 4K textures

    public LqMap() {
        layers = new Layers();

        layers.addLayer(0, "Default");
    }
}
