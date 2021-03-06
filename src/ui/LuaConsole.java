package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Reader;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import jsyntaxpane.DefaultSyntaxKit;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import ui.CrasseLua;
import engine.Stage;

/**
 *
 * @author GabrielBiga
 */
public class LuaConsole extends javax.swing.JFrame {

    /**
     * Creates new form LuaConsole
     */
    public LuaConsole() {
        initComponents();
        
        DefaultSyntaxKit.initKit();
         
        final JEditorPane codeEditor = jEditorPane1;
        //JScrollPane scrPane = new JScrollPane(codeEditor);
        //final Container c = this.getContentPane();
        //c.setLayout(new BorderLayout());
        //c.add(scrPane, BorderLayout.CENTER);
        //c.doLayout();
        codeEditor.setContentType("text/lua");
        //codeEditor.setText("public static void main(String[] args) {\n}");
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToggleButton1.setText("Compila essa bagaca!");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jScrollPane3.setViewportView(jEditorPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane2))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(224, 224, 224)
                .addComponent(jToggleButton1)
                .addContainerGap(239, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addLog(String message) {
        jTextArea2.setText(jTextArea2.getText() + message + "\n");
    }
    
   
    
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        jTextArea2.setText("");
        
        
        
        
        new Thread() {

            @Override
            public void run() {
                ScriptEngineManager sem = new ScriptEngineManager();
                ScriptEngine e = sem.getEngineByName("luaj");
                ScriptEngineFactory f = e.getFactory();

                addLog( "Engine name: " +f.getEngineName() );
                addLog( "Engine Version: " +f.getEngineVersion() );
                addLog( "LanguageName: " +f.getLanguageName() );
                addLog( "Language Version: " +f.getLanguageVersion() );
                //String statement = f.getOutputStatement("\"hello, world\"");
                //addLog(statement);

                addLog("---");
                Reader input = new CharArrayReader("abcdefg\nhijk".toCharArray());
                CharArrayWriter output = new CharArrayWriter();
                //e.put("x", CrasseLua.x);
                //e.put("y", CrasseLua.y);
             //   e.put("obj", Stage.retangulo);
                //e.put("mulilo.sleep", sleep());
                e.put("sleep", new OneArgFunction() {
                    public LuaValue call(LuaValue arg) {
                        try {
                            /*System.out.println("arg "+arg.tojstring());
                            return LuaValue.valueOf(123);*/

                            Thread.sleep(Integer.parseInt(arg.tojstring()));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(LuaConsole.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        return null;
                    }
                });

                String errors = "";
                String script = jEditorPane1.getText(); 

                addLog("Compilando...");
                e.getContext().setReader(input);
                e.getContext().setWriter(output);
                
                try {
                    e.eval(script);
                } catch (ScriptException ex) {
                    errors += ex.getMessage();
                }
                
                addLog(output.toString());
                addLog("---\n");
                addLog("errors::>"+errors+"<::errors");  
            }
        }.start();
        
        //e.getContext().setErrorWriter(errors);
        /*try {
            e.eval(script);
        } catch (ScriptException ex) {
            errors += ex.getMessage();
        }*/
       
        
    }//GEN-LAST:event_jToggleButton1ActionPerformed
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
