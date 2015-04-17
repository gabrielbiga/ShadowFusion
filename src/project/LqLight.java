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

import box2dLight.PointLight;
import box2dLight.RayHandler;
import java.io.Serializable;

/**
 * A ShadowFusion Light
 *
 * @author GabrielBiga 
 */
public class LqLight extends LqMapObject implements Serializable {
    
    public String name = "Light";
    
    private transient PointLight light;
    
    private LqColor color;
    private int rays;
    private int distance;
    
    public void alloc(RayHandler ray, int rays) {
        this.rays = rays;
       //light = new PointLight(ray, 10000, Color.CYAN, 500, (Gdx.graphics.getWidth()/2) - 50, (Gdx.graphics.getHeight() /2));
        light = new PointLight(ray, rays);
        
        updateLight();
    }
    
    public void destroy() {
        light.remove();
    }

    public LqColor getColor() {
        return color;
    }

    public void setColor(LqColor color) {
        this.color = color;
        light.setColor(color.getGDXColor());
    }

    public int getRays() {
        return rays;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
        light.setDistance(distance);
    }
    
     @Override
    public void setX(int x) {
        this.x = x;
        updateLight();
    }
    
    @Override
    public void setY(int y) {
        this.y = y;
        updateLight();
    }
    
    private void updateLight() {
        light.setPosition(this.x, this.y);
        if (color != null)
            light.setColor(color.getGDXColor());
        light.setDistance(distance);
    }
}
