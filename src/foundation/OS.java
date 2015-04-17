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

//Especificos de sistema operacional
package foundation;

import java.awt.Color;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OS {

    public static class MacOS {
        public static final Color OS_X_UNIFIED_TOOLBAR_FOCUSED_BOTTOM_COLOR   =  new Color(64, 64, 64);
        public static final Color OS_X_UNIFIED_TOOLBAR_UNFOCUSED_BORDER_COLOR =  new Color(135, 135, 135);       

        public static boolean isMacOSX() {
            return System.getProperty("os.name").indexOf("Mac OS X") >= 0;
        }

        public static void enableFullScreenMode(Window window) {
            String className = "com.apple.eawt.FullScreenUtilities";
            String methodName = "setWindowCanFullScreen";

            try {
                Class<?> clazz = Class.forName(className);
                Method method = clazz.getMethod(methodName, new Class<?>[]{
                    Window.class, boolean.class});
                method.invoke(null, window, true);
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException t) {
                System.err.println("Full screen mode is not supported");
            }
        }
    }
    
    public static class Windows {
        public static boolean isWindows() {
            return System.getProperty("os.name").indexOf("Windows") >= 0;
        }
    }

}
