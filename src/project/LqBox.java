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
import java.io.Serializable;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqBox extends LqMapObject implements Serializable {
    
    public String name = "Box";
    
    private int width;
    private int height;
    
    private LqColor color;
    
    public LqBox() {}
    
    public LqBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public LqBox(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public LqBox(Point coords, Dimension size) {
        this.x = coords.x;
        this.y = coords.y;
        this.width = size.width;
        this.height = size.height;
    }
    
    public LqBox(LqBox clone) {
        this.x = clone.getX();
        this.y = clone.getY();
        this.width = clone.getWidth();
        this.height = clone.getHeight();
        this.color = clone.getColor();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public LqColor getColor() {
        return color;
    }
    
    public void setColor(LqColor color) {
        //LqColor c = new LqColor();
        //c.setGDXColor(color);
        
        this.color = color;
    }
    
    
}
