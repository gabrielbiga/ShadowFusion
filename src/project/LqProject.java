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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import ui.RunTimeConfig;

/**
 * The opened project
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqProject implements Serializable {

    public static String name = "Untitled";
    public static LqAssets assets = new LqAssets();
    public static Maps maps = new Maps();
    public static LuaScripts scripts = new LuaScripts();
    public static LqCamera mainCamera = new LqCamera();
    
    public static void saveProject(String dest) {
        try {
            FileOutputStream fout = new FileOutputStream(dest);

            ObjectOutputStream oos = new ObjectOutputStream(fout);

            //Object[] avoidMap = new Object[]{name, assets, maps};
            oos.writeObject(name);
            oos.writeObject(assets);
            oos.writeObject(scripts);
            oos.writeObject(mainCamera);
            oos.writeObject(maps);

            oos.close();
            
            RunTimeConfig.projectOpened = dest;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadProject(String path) {
        Object obj = null;

        try {
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);

            while ((obj = ois.readObject()) != null) {
                if (obj instanceof String) {
                    String name = (String) obj;
                    LqProject.name = name;
                } else if (obj instanceof LqAssets) {
                    LqAssets assets = (LqAssets) obj;
                    LqProject.assets = assets;
                } else if (obj instanceof LuaScripts) {
                    LuaScripts luaScripts = (LuaScripts) obj;
                    LqProject.scripts = luaScripts;
                } else if (obj instanceof LqCamera) {
                    LqCamera camera = (LqCamera) obj;
                    LqProject.mainCamera = camera;
                } else if (obj instanceof Maps) {
                    Maps maps = (Maps) obj;
                    LqProject.maps = maps;

                    break; //EOF
                }
            }

            ois.close();
            
            RunTimeConfig.projectOpened = path;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void destroy() {
        name = "Untitled";
        assets = new LqAssets();
        maps = new Maps();
        scripts = new LuaScripts();
        mainCamera = new LqCamera();
        
        System.gc(); //Destroooooy alll!!
    }
}
