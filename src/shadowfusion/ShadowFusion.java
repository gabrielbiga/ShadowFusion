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

package shadowfusion;

import engine.Controller;
import engine.LqLog;
import foundation.OS;
import testchamber.TestUnit;
import ui.LqWindow;

/**
 * Main class
 *
 * @author GabrielBiga
 */
public class ShadowFusion {
    
    //-----------------------------------------------------------------------------
    // Warming UP of Editor
    //-----------------------------------------------------------------------------
    public ShadowFusion() {
        new TestUnit();
        
        hello();
        
        //---
        
        /* Set the look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel of editor ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                //System.out.println(info.getName());
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                
                //javax.swing.UIManager.setLookAndFeel("org.jb2011.lnf.beautyeye.BeautyEyeLookAndFeelCross");
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LqWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LqWindow().setVisible(true);
            }
        });
    }
    
    private void hello() {
        LqLog.log("ShadowFusion Engine 0.1 Alpha.");
        LqLog.log("The copyright to the contents herein the property of Liquare, L.L.C.");
        LqLog.log("---");
        LqLog.log("SO: "  + System.getProperty("os.name"));
        LqLog.log("Current JVM: "  + System.getProperty("sun.arch.data.model") + " bit");
        LqLog.log("");
        
        if (OS.Windows.isWindows()) {
            LqLog.log("Trying to connect with XBox360Controller...");
            try {
                Controller.initXBox360();
            } catch (Exception e) {
                LqLog.log("Problems with XBox360 controller. \n>>> " + e.getMessage());
            }
        }
        
        LqLog.log("Warming UP...");
    }
    
    //-----------------------------------------------------------------------------
    // Bootstrap
    //-----------------------------------------------------------------------------
    public static void main(String[] args) {
        new ShadowFusion();
    }
}
