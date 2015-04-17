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

import foundation.LqSystem;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Alika2DG2D {
    private JPanel window;
    private Graphics2D g2d;
    private Graphics g;
    
    private long deltaS = 0;
    private boolean showFPS;
    
    public Alika2DG2D(Graphics g, JPanel window) {
        g2d = (Graphics2D) g.create();
        this.g = g;
        this.window = window;
        
        
        this.deltaS = System.currentTimeMillis();
    }
    
    //Ativa o antialiasing
    public void activateAntialiasing() {
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    }
    
    //Finaliza o fluxo
    public void dispose() {
        //Gambiarra para Linux e Mac renderizar corretamente
        Toolkit.getDefaultToolkit().sync();

        g.dispose();
    }
    
    //Show FPS
    
    public void showFPS() {
       // if(this.showFPS) {
            long deltaT = System.currentTimeMillis();
            long calc   = deltaT - this.deltaS;
            long fps    = (calc == 0 ? 1  : calc) * 1000;
            long frames = LqSystem.Monitor.getHertz();
            
            //If is high of monitor hz, consider monitor
            if (fps < frames)
                frames = fps;
            
            g2d.setColor(Color.yellow);
            g2d.drawString(frames + " fps", window.getWidth() - (String.valueOf(frames).length() + 4 * 10), 20); 
       // }
    }
    
    public Graphics2D getGraphicsObj() {
        return g2d;
    }
    
    public void draw(Shape shape) {
        g2d.draw(shape);
    }
    
    public void fill(Shape shape) {
        g2d.fill(shape);
    }
    
    public void setColor(Color c) {
        g2d.setColor(c);
    }
    
    public void displayFPS(boolean s) {
        this.showFPS = s;
    }
    
    public void light(Point mousepointer) {
        //java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float(200, 200);
        
        Point2D center = new Point2D.Float(mousepointer.x, mousepointer.y);
        float radius = 92;
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.BLACK};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(p);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .60f));
        g2d.fillRect(0, 0, window.getWidth(), window.getHeight()); 
    }
    
    public static Image loadSprite(String URL) {
        LqLog.log("Grab from disk: " + URL);
        
        ImageIcon newIcon = new ImageIcon(URL);
        
	return newIcon.getImage(); 
    }
}
