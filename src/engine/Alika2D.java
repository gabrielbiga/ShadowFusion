//========== (C) Copyright 2014 Liquare, L.L.C. All rights reserved. ===========
//
// The copyright to the contents herein is the property of Liquare, L.L.C.
// The contents may be used and/or copied only with the written permission of
// Liquare, L.L.C., or in accordance with the terms and conditions stipulated in
// the agreement/contract under which the contents have been supplied.
//
// $Header: $
// $NoKeywords: $
//=============================================================================

package engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import project.LqBox;

public class Alika2D  {
    
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private boolean flipViewport;
    
    public Alika2D(SpriteBatch batch, ShapeRenderer shapeRenderer, boolean flipViewport) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.flipViewport = flipViewport;
        
    }
    
    //Ativa o antialiasing
    public void activateAntialiasing() {
        /*g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);*/
    }
    
    //Finaliza o fluxo
    public void dispose() {
        batch.dispose();
    }
    
    public void drawRect(LqBox shape, ShapeType type) {
        shapeRenderer.begin(type);
            if (shape.getColor() != null)
                shapeRenderer.setColor(shape.getColor().getGDXColor());
            else
                shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
        shapeRenderer.end();
    }
    
    public void drawImage(Sprite sprite, Point position) {
        batch.begin();
            sprite.draw(batch);
            sprite.setPosition(position.x, position.y);
            if (flipViewport)
                sprite.flip(false, true);
        batch.end();
    }
    
    public Sprite loadSprite(String URL) {
        LqLog.log("Grab from disk: " + URL);
        
        Texture tex = new Texture(URL);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        
        return new Sprite(tex);
    }
    
    public static Image loadIcon(String URL) {
        LqLog.log("Grab from disk: " + URL);
        
        ImageIcon newIcon = new ImageIcon(URL);
        
	return newIcon.getImage();
        
    }
}
