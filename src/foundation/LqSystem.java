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
package foundation;

import java.awt.Color;
import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import javax.accessibility.AccessibleContext;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import ui.RunTimeConfig;

/**
 * Functions of System
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqSystem {

    public static class Monitor {

        public static int getHertz() {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();

            for (int i = 0; i < gs.length; i++) {
                DisplayMode dm = gs[i].getDisplayMode();

                int refreshRate = dm.getRefreshRate();
                if (refreshRate != DisplayMode.REFRESH_RATE_UNKNOWN) {
                    return refreshRate;
                } else {
                    break;
                }
            }

            return 0;
        }
    }
    
    public static class ColorPicker {
        private JColorChooser colorChooser;
        private Component window;
        private String title;
        
        
        public ColorPicker(Component window, String title) {
            this.window = window;
            this.title = title;
        }
        
        
        public Color show() {
            return JColorChooser.showDialog(window, title, Color.yellow);
        }
    }

    public static class FileDialog {
        private JFileChooser fileChooser;
        private Object window;
        private Types dialogType;

        //Types
        public enum Types {
            open,
            save
        };
        
        public FileDialog(Object window, Types dialogType, boolean multipleSelection) {
            this.dialogType = dialogType;
            this.window = window;
            
            fileChooser = new JFileChooser();
            if (RunTimeConfig.blackStyle)
                setColors(fileChooser, Color.WHITE, new Color(44, 44, 44));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setMultiSelectionEnabled(multipleSelection);
        }
        
        public String[] show() {
            int result;
            
            //fileChooser.set
            
            if (dialogType == Types.open) {
                //fileChooser.setDialogTitle("Open");
                
                result = fileChooser.showOpenDialog((Component) window);
            } else {
                result = fileChooser.showSaveDialog((Component) window);
            }
            
            //int result = fileChooser.showOpenDialog((Component) window);

            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                String[] seletedFilesStr = null;
                         
                if (selectedFiles.length == 0) {
                    seletedFilesStr = new String[1];
                    
                    seletedFilesStr[0] = fileChooser.getSelectedFile().getAbsolutePath();
                } else {
                
                    seletedFilesStr = new String[selectedFiles.length];

                    //Build String array
                    for(int i = 0; i < selectedFiles.length; i++) {
                        seletedFilesStr[i] = selectedFiles[i].getAbsolutePath();
                    }
                }
                
                return seletedFilesStr;
            }
            
            return null;
        }

        private void setColors(Component c, Color fg, Color bg) {
            setColors0(c.getAccessibleContext(), fg, bg);
        }

        private void setColors0(AccessibleContext ac, Color fg, Color bg) {
            ac.getAccessibleComponent().setForeground(fg);
            ac.getAccessibleComponent().setBackground(bg);

            int n = ac.getAccessibleChildrenCount();

            for (int i = 0; i < n; i++) {
                String componentName = ac.getAccessibleChild(i).getAccessibleContext().getClass().getName();

                if (componentName.contains("JButton") || componentName.contains("JComboBox") || componentName.contains("JToggleButton") /*|| componentName.contains("JToolBar")*/) {
                    setColors0(ac.getAccessibleChild(i).getAccessibleContext(), Color.BLACK, bg);
                } else {
                    setColors0(ac.getAccessibleChild(i).getAccessibleContext(), fg, bg);
                }
            }
        }
    }
}
