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

import com.badlogic.gdx.graphics.Color;
import java.io.Serializable;

/**
 * Liquare's color
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqColor implements Serializable {
    
    private float r, g, b, a;
    
    public LqColor() {}

    public LqColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public LqColor(Color color) {
        this.setGDXColor(color);
    }

    public LqColor(java.awt.Color color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
        this.a = color.getAlpha();
    }
    
    public void setGDXColor(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }
    
    public Color getGDXColor() {
        return new Color(Color.rgb888(r, g, b));
    }
    
    public java.awt.Color getJColor() {
        return new java.awt.Color(r, g, b, a);
    }
    
    //g/s
    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }
    
}
