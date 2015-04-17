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

package ui;

import engine.LqLog;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import project.LqLuaScript;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class LuaScriptUI extends RTextScrollPane {
    
    private static class LuaDash extends RSyntaxTextArea {
        private LqLuaScript script;
        
        public LuaDash() {
            setStyle();
        }
        
        public LuaDash(String content) {
            setStyle();
            
            setText(content);
        }
        
        public LuaDash(LqLuaScript content) {
            setStyle();
            
            this.script = content;
            
            setText(this.script.script);
        }
        
        private void setStyle() {
            setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
            setAntiAliasingEnabled(true);
            setAutoIndentEnabled(true);
            setCodeFoldingEnabled(true);
            
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent ke) {
                    if (script != null) 
                        script.script = getText();
                }
            });
        }
    }
    
    public LuaScriptUI() {
        super(new LuaDash());
    }
    
    public LuaScriptUI(String content) {
        super(new LuaDash(content));
    }
    
    public LuaScriptUI(LqLuaScript content) {
        super(new LuaDash(content));
    }
}
