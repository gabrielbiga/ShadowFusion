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

import ch.aplu.xboxcontroller.XboxController;
import ch.aplu.xboxcontroller.XboxControllerAdapter;
import javax.swing.JTextArea;
import ui.CrasseLua;


public class Controller {

    static private XboxController xc;
    static private int leftVibrate = 0;
    static private int rightVibrate = 0;
    
    static public JTextArea logDash;
    
    static private void addLog(String message) {
        logDash.setText(logDash.getText() + "Alika::XBox360: " + message + "\n");
    }
    

    public static void initXBox360() {
        xc = new XboxController();

        if (!xc.isConnected()) {
            System.out.println("Error with XBox Controller");
            xc.release();
            return;
        }

        xc.addXboxControllerListener(new XboxControllerAdapter() {
            public void leftTrigger(double value) {
                leftVibrate = (int) (65535 * value * value);
                xc.vibrate(leftVibrate, rightVibrate);
            }

            public void rightTrigger(double value) {
                rightVibrate = (int) (65535 * value * value);
                xc.vibrate(leftVibrate, rightVibrate);
            }

            public void buttonA(boolean pressed) {
                addLog("A, " + String.valueOf(pressed));
            }
            
            public void leftThumbDirection(double direction) {
                //addLog(String.valueOf(direction));
                
                if (direction > 10 && direction < 150) {
                    addLog(">>");
                   // Stage.retangulo.x += 10;
                }
                
                if (direction > 240 && direction < 300) {
                    addLog("<<");
                    //Stage.retangulo.x -= 10;
                }
            }

        });

        System.out.println(
                "Xbox controller connected.\n");

    //xc.release();
        //System.exit(0);
    }

}
