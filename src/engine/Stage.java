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

package engine;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import project.LqMap;
import ui.LqWindow;


public class Stage extends JPanel {

    public Stage(LqMap map, LqWindow app) {
        LwjglAWTCanvas canvas = null;
        
        this.setLayout(new BorderLayout());
        
        //Optimize
        try {
            canvas = new LwjglAWTCanvas(new StageGDX(map, app));
        } finally {
            this.add(canvas.getCanvas(), BorderLayout.CENTER);
        }
    }
}
