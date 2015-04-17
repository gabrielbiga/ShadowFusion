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

package testchamber;

//import com.google.gson.Gson;
import engine.LqLog;
import java.util.ArrayList;
import project.LqProject;

/**
 * Class only for random tests
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class TestUnit {
    private class Struct {
        public String bigao = "chupa";
        public int idade = 20;
        
    }
    
    private class Struct2 {
        private ArrayList<Struct> zx = new ArrayList<>();
    }
    
    public TestUnit() {
        //bootStrap();
    }
    
    private void bootStrap() {
        Struct2 x = new Struct2();
        
        Struct p1 = new Struct();
        Struct p2 = new Struct();
        
        x.zx.add(p1);
        x.zx.add(p2);
        
        
       // Gson gson = new Gson();
        //String json = gson.toJson(Project.assets);
        
        //LqLog.log(json);
        
        System.exit(0);
    }
}
