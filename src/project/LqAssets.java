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

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LqAssets implements Serializable {
    //Classes
    public class Category implements Serializable {
        public String name;
        
        public ArrayList<LqAsset> assets = new ArrayList<>();
        private int countAssets = 0;
        
        public int getCountAssets() {
            return countAssets;
        }
    }
    
    public LqAssets() {
        addCategory("Default"); //The Default Category!
    }
    
    //Field
    private ArrayList<Category> categories = new ArrayList<>();
    
    //Count
    private int countCategories = 0;
    
    //Add & Get
    public void addCategory(String name) {
        Category newCat = new Category();
        
        newCat.name = name;
        
        categories.add(newCat);
        countCategories++;
    }
    
    public void addAsset(int idCat, String name, String path) {
        LqAsset newAsset = new LqAsset();
        
        newAsset.name   = name;
        newAsset.path   = path;
        //newAsset.sprite = sprite;
        
        categories.get(idCat).assets.add(newAsset);
        categories.get(idCat).countAssets++;
    }
    
    public ArrayList<Category> getAssets() {
        return categories;
    }

    public int getCountCategories() {
        return countCategories;
    }
    
    public String[] getCategoryList() {
        int countCat = this.getCountCategories();
        int i;
        String[] forkCat = new String[countCat];

        //forkCat[0] = "Default";
        for (i = 0; i < countCat; i++) {
            forkCat[i] = this.getAssets().get(i).name;
        }

        return forkCat;
    }

}
