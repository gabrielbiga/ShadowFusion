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

import engine.Alika2D;
import engine.Controller;
import engine.Stage;
import engine.lqlua.LqLua;
import engine.lqlua.LqLuaVisualReturn;
import foundation.LqSystem;
import foundation.OS;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import project.LqAssets;
import project.LqBox;
import project.LqCacheSprite;
import project.LqColor;
import project.LqLayer;
import project.LqLight;
import project.LqLuaScript;
import project.LqMap;
import project.LqMapObject;
import project.LqPhysicsBox;
import project.LqPhysicsCircle;
import project.LqProject;
import project.LqSprite;
import project.Maps;
import ui.objatrib.AtribLqBox;
import ui.objatrib.AtribLqPhysicsBox;

/**
 *
 * @author GabrielBiga
 */
public class LqWindow extends javax.swing.JFrame {

    private AssetsPanel ap;
    private LqStatusBar statusBar;

    /**
     * Creates new form LqWindow
     */
    public LqWindow() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //Enable JMenu up to GL context
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        
        //InitObjects
        applyTheme();
        
        if (OS.Windows.isWindows()) {
            Controller.logDash = jTextArea1;
        }

        //Assets Bar
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);

        ap = new AssetsPanel();

        jPanel1.setLayout(jPanel1Layout);

        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        
        addEditorMap("New Map 1");

        //Window title
        updateEditorTitle();
    }
    
    public final void updateEditorTitle() {
        this.setTitle("Liquare ShadowFusion Engine - " + LqProject.name);
        
        //Update Tree too :)
        updateTree(); 
    }
    
    private void applyTheme() {
        final Color cian   = new Color(44, 44, 44);
        final Color blcian = new Color(75, 75, 75);
        
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(ui.assets.AssetLoader.class.getResource("icon.png")));

        if (RunTimeConfig.blackStyle) {
            UIManager.put("Panel.background", cian);
            UIManager.put("TabbedPane.background", cian);
            UIManager.put("TabbedPane.foreground", Color.WHITE);
            UIManager.put("SplitPane.background",blcian);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("OptionPane.background", cian);
            UIManager.put("ScrollPane.background", blcian);
            UIManager.put("List.background", blcian);
            UIManager.put("List.foreground", Color.YELLOW);
            UIManager.put("Tree.background", cian);
            UIManager.put("Tree.foreground", cian);
            UIManager.put("ToggleButton.background", Color.YELLOW);
            UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0)); //No select border in tabs
            UIManager.put("Label.foreground", Color.WHITE);
            UIManager.put("CheckBox.background", cian);
            UIManager.put("CheckBox.foreground", Color.WHITE);
            UIManager.put("Button.background", cian);

            this.getContentPane().setBackground(blcian); 
        }
        
        initComponents();
        
        this.setJMenuBar(jMenuBar1);
        
        if (RunTimeConfig.blackStyle) {
            JTree[] trees = new JTree[]{treeProject, treeLayers};
            for(JTree tree : trees) {
                if (tree.getCellRenderer() instanceof DefaultTreeCellRenderer) {
                    final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) (tree.getCellRenderer());
                    renderer.setBackgroundNonSelectionColor(new Color(44, 44, 44));
                    renderer.setBackgroundSelectionColor(new Color(75, 75, 75));
                    renderer.setTextNonSelectionColor(Color.WHITE);
                    renderer.setTextSelectionColor(Color.WHITE);
                } else {
                    System.out.println("Error on apply themes.");
                }
            }

            JTabbedPane[] tabbeds = new JTabbedPane[]{jTabbedPane1, jTabbedPane2, jTabbedPane11, jTabbedPane4, jTabbedPane10};
            for (JTabbedPane tabbed : tabbeds) {
                tabbed.setUI(new BasicTabbedPaneUI() {
                    public void paint(Graphics g, Component c) {
                        g.fillRect(0, 0, c.getSize().width, c.getSize().height);
                    }
                });
            }

            tabbeds = null;
            System.gc();
        }
        
        //StatusBar
        //jPanel8 = new LqStatusBar();
        //jPanel8.add(statusBar);
    }
    
    //Add a brand new map to workspace
    private void addEditorMap(String name) {
        //Creating a default Map
        LqProject.maps.addMap(name);
        
        //Creating up stage!
        LqMap map = LqProject.maps.getMap(LqProject.maps.getCount() -1);
        
        Stage newWindow = new Stage(map, this);
        
        newWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                updateAssetTree();
            }
        });
        
        jTabbedPane2.add(name, newWindow);
        jTabbedPane2.setSelectedIndex(jTabbedPane2.getTabCount() -1);
        
        RunTimeConfig.mapCacheUI.addNode(jTabbedPane2.getTabCount() -1, map);
        
         this.setJMenuBar(jMenuBar1);
    }
    
    //Add a existent map to workspace
    private synchronized void addExEditorMap(LqMap map) {
        //Creating up stage!
        Stage newWindow = new Stage(map, this);
        
        newWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                updateAssetTree();
            }
        });
        
        jTabbedPane2.add(map.name, newWindow);
        jTabbedPane2.setSelectedIndex(jTabbedPane2.getTabCount() -1);
        
        RunTimeConfig.mapCacheUI.addNode(jTabbedPane2.getTabCount() -1, map);
        
         this.setJMenuBar(jMenuBar1);
    }
    
    private void newProject() {
        LqProject.destroy();
        RunTimeConfig.destroy();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jToggleButton9 = new javax.swing.JToggleButton();
        jPanel12 = new javax.swing.JPanel();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton10 = new javax.swing.JToggleButton();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        treeProject = new javax.swing.JTree();
        jSplitPane3 = new javax.swing.JSplitPane();
        jSplitPane10 = new javax.swing.JSplitPane();
        jTabbedPane11 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        treeLayers = new javax.swing.JTree();
        jPanel7 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane10 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Liquare ShadowFusion Engine");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jSplitPane2.setBorder(null);
        jSplitPane2.setDividerLocation(220);

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setDoubleBuffered(true);

        jTabbedPane1.setDoubleBuffered(true);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(38, 70));

        jPanel2.setAutoscrolls(true);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/pointer.png"))); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/brush.png"))); // NOI18N
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/play.png"))); // NOI18N
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/hand.png"))); // NOI18N
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Warp Content");
        jCheckBox1.setToolTipText("");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jCheckBox1))
        );

        jTabbedPane1.addTab("Toolbar", jPanel2);

        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/pointer.png"))); // NOI18N
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        jToggleButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/circle.png"))); // NOI18N
        jToggleButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton7ActionPerformed(evt);
            }
        });

        jToggleButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/box.png"))); // NOI18N
        jToggleButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton8ActionPerformed(evt);
            }
        });

        jToggleButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/light.png"))); // NOI18N
        jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Physics", jPanel9);

        jToggleButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/pointer.png"))); // NOI18N
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        jToggleButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/box.png"))); // NOI18N
        jToggleButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jToggleButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(110, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Solid", jPanel12);

        jSplitPane1.setTopComponent(jTabbedPane1);

        jPanel1.setAutoscrolls(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Assets", jPanel1);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Project");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Maps");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("MAP 1");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Scripts");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("LUA Script 1");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeProject.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeProjectMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(treeProject);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Project", jPanel6);

        jSplitPane1.setRightComponent(jTabbedPane4);

        jSplitPane2.setLeftComponent(jSplitPane1);

        jSplitPane3.setBorder(null);
        jSplitPane3.setDividerLocation(210);
        jSplitPane3.setContinuousLayout(true);
        jSplitPane3.setDoubleBuffered(true);

        jSplitPane10.setDividerLocation(200);
        jSplitPane10.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTabbedPane11.setDoubleBuffered(true);
        jTabbedPane11.setMinimumSize(new java.awt.Dimension(38, 70));

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Layers");
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Layer1");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Asset 1");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Layer2");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Asset 3");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeLayers.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeLayers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeLayersMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(treeLayers);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/remove.png"))); // NOI18N
        jButton2.setToolTipText("Remove Object");
        jButton2.setAlignmentY(0.9F);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/transfer.png"))); // NOI18N
        jButton3.setToolTipText("Transfer Into Layer");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/up.png"))); // NOI18N
        jButton4.setToolTipText("Up Object");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/assets/menu/down.png"))); // NOI18N
        jButton5.setToolTipText("Down Object");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addComponent(jButton4)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane11.addTab("Layers", jPanel11);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 171, Short.MAX_VALUE)
        );

        jTabbedPane11.addTab("Lights", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 171, Short.MAX_VALUE)
        );

        jTabbedPane11.addTab("Threads", jPanel4);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 171, Short.MAX_VALUE)
        );

        jTabbedPane11.addTab("Cameras", jPanel8);

        jSplitPane10.setTopComponent(jTabbedPane11);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane10.addTab("Inspector", jPanel10);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTabbedPane10.addTab("Messages", jScrollPane1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 248, Short.MAX_VALUE)
        );

        jTabbedPane10.addTab("Physics", jPanel5);

        jSplitPane10.setRightComponent(jTabbedPane10);

        jSplitPane3.setRightComponent(jSplitPane10);

        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });
        jSplitPane3.setLeftComponent(jTabbedPane2);

        jSplitPane2.setRightComponent(jSplitPane3);

        jMenu1.setText("File");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("New Project");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Open Project");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Save Project");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Save As...");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);
        jMenu1.add(jSeparator2);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Close Project");
        jMenu1.add(jMenuItem9);
        jMenu1.add(jSeparator3);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Exit Engine");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem23.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem23.setText("Cut");
        jMenu2.add(jMenuItem23);

        jMenuItem22.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem22.setText("Copy");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem22);

        jMenuItem24.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem24.setText("Paste");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem24);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("View");

        jMenu4.setText("Stage");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Show Grid");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Only Viewport");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem12.setText("Show FPS");
        jMenuItem12.setToolTipText("");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem12);

        jMenuItem14.setText("Show Light");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem14);

        jMenu3.add(jMenu4);

        jMenuItem21.setText("Reset Camera");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem21);

        jMenuItem10.setText("Lua Console");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

        jMenuBar1.add(jMenu3);

        jMenu8.setText("Project");

        jMenuItem18.setText("Preferences");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem18);

        jMenuBar1.add(jMenu8);

        jMenu5.setText("Add");

        jMenuItem11.setText("New Lua Script");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem11);

        jMenuItem19.setText("New Layer");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem19);

        jMenuItem20.setText("New Map");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem20);

        jMenu7.setText("Asset");

        jMenuItem15.setText("New Category");
        jMenuItem15.setToolTipText("");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem15);

        jMenuItem16.setText("New Asset");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem16);

        jMenu5.add(jMenu7);

        jMenuBar1.add(jMenu5);

        jMenu9.setText("Export");

        jMenuItem27.setText("XBox 360");
        jMenu9.add(jMenuItem27);

        jMenuItem26.setText("Android");
        jMenu9.add(jMenuItem26);

        jMenuBar1.add(jMenu9);

        jMenu6.setText("Help");

        jMenuItem13.setText("About");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem13);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        //Ajust the division of screen
        jSplitPane3.setDividerLocation(getWidth() - 460);
    }//GEN-LAST:event_formComponentResized

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        RunTimeConfig.showGrid = !RunTimeConfig.showGrid;
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (!RunTimeConfig.onlyViewport) {
            jSplitPane1.setVisible(false);
            jSplitPane10.setVisible(false);

            jSplitPane3.setDividerLocation(0);
            jSplitPane2.setDividerLocation(0);

            RunTimeConfig.onlyViewport = true;
        } else {
            jSplitPane1.setVisible(true);
            jSplitPane10.setVisible(true);

            jSplitPane3.setDividerLocation(getWidth() - 460);
            jSplitPane2.setDividerLocation(220);

            RunTimeConfig.onlyViewport = false;
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        newProject();
        
        jTabbedPane2.removeAll();
        
        addEditorMap("New Map 1");
        
        //Refresh workspace
        updateEditorTitle();
        
        //Refresh Assets Panel (ap)
        ap.refreshCombo();
        ap.clearButtonsAssets();
        ap.updateUIZ();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        JFrame consoleLua = new LuaConsole();
        consoleLua.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        String ret = JOptionPane.showInputDialog(null, "Type the name of new script", "New script", JOptionPane.QUESTION_MESSAGE);

        if (ret != null && !ret.isEmpty()) {
            LqProject.scripts.addScript(ret, null);

            jTabbedPane2.addTab(ret, null, new LuaScriptUI(LqProject.scripts.getScript(LqProject.scripts.getCount() -1)), "LUA Script");
            jTabbedPane2.setSelectedIndex(jTabbedPane2.getTabCount() -1);
            
            updateTree();
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void addLog(String message) {
        jTextArea1.setText(jTextArea1.getText() + message + "\n");
    }


    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        jTextArea1.setText("");

        //List all lua scripts
        for(int i = 0; i < LqProject.scripts.getCount(); i++) {
            LqLuaScript script = LqProject.scripts.getScript(i);
            
            if (script.script != null) {
                LqLua.execute(script.name, script.script, new LqLuaVisualReturn() {

                    @Override
                    public void echo(String message) {
                        jTextArea1.setText(jTextArea1.getText() + message);
                    }
                });
            }
        
            //addLog("Joined on ThreadList!");
        }
        
        
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        RunTimeConfig.showFPS = !RunTimeConfig.showFPS;
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        RunTimeConfig.showLight = !RunTimeConfig.showLight;
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // New asset category
        String ret = JOptionPane.showInputDialog(null, "Type the name", "New asset category", JOptionPane.QUESTION_MESSAGE);

        if (ret != null && !ret.isEmpty()) {
            LqProject.assets.addCategory(ret);
            JOptionPane.showMessageDialog(null, ret + " added!", "New asset category", JOptionPane.INFORMATION_MESSAGE);

            ap.refreshCombo();
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        //LqSystem.OpenDialog dialog = (new LqSystem()).new OpenDialog(this);
        LqSystem.FileDialog dialog = new LqSystem.FileDialog(this, LqSystem.FileDialog.Types.open, true);
        String[] ret = dialog.show();
        int id;

        if (ret != null) {
            String[] categories = LqProject.assets.getCategoryList();
            
            String x = (String) JOptionPane.showInputDialog(null, "Choose the category to put the asset.", "Select category", JOptionPane.INFORMATION_MESSAGE, null, categories, categories[0]);
            
            for(id = 0; !categories[id].equals(x); id++); //Search the category
            
            for(int i = 0; i < ret.length; i++) {
                LqProject.assets.addAsset(id, "", ret[i]);
            }

            ap.updateUIZ();
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        String ret = RunTimeConfig.projectOpened;
        
        if (ret == null) { 
            LqSystem.FileDialog dialog = new LqSystem.FileDialog(this, LqSystem.FileDialog.Types.save, false);

            ret = dialog.show()[0];
        } 

        if (ret != null) {
            LqProject.saveProject(ret);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        //LqSystem.OpenDialog dialog = (new LqSystem()).new OpenDialog(this);
        LqSystem.FileDialog dialog = new LqSystem.FileDialog(this, LqSystem.FileDialog.Types.open, false);

        String[] ret = dialog.show();

        if (ret != null) {
            newProject(); //Destroy all instances
            
            jTabbedPane2.removeAll();
            
            LqProject.loadProject(ret[0]);

            addExEditorMap(LqProject.maps.getMap(0));
            
            //Refresh workspace
            updateEditorTitle();
            
            //Refresh Assets Panel (ap)
            ap.refreshCombo();
            ap.clearButtonsAssets();
            ap.updateUIZ();
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        // TODO add your handling code here:
        Preferences prefScreen = new Preferences(this, true);
        prefScreen.setVisible(true);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        clearSelection();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void clearSelection() {
        RunTimeConfig.assetSelected  = null;
        RunTimeConfig.objectSelected = null;
    }
    
    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void treeProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeProjectMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2 && treeProject.getSelectionCount() > 0) {
            //Search if opened...
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeProject.getLastSelectedPathComponent();
            
            Object nodeInfo = node.getUserObject();
            
            for(int i = 0; i < jTabbedPane2.getTabCount(); i++) {
                if (jTabbedPane2.getTitleAt(i).equals(nodeInfo)) {
                    jTabbedPane2.setSelectedIndex(i);
                    return;
                }
            }
            
            //If not opened...
            for (int i = 0; i < treeProject.getRowCount(); i++) {
                treeProject.expandRow(i);
            }
            int selected = treeProject.getSelectionRows()[0];
            Object obj = RunTimeConfig.treeProjectCache.getNodeObj(selected);
        
            if (obj != null) {
                if (obj instanceof LqLuaScript) {
                    /// wrap = new JPanel();
                    LqLuaScript script = (LqLuaScript) obj;

                    //wrap.add(new LuaScriptUI(script));

                    jTabbedPane2.addTab(script.name, null, new LuaScriptUI(script), null);
                }

                if (obj instanceof LqMap) {
                    LqMap map = (LqMap) obj;

                    addExEditorMap(map);
                }

                jTabbedPane2.setSelectedIndex(jTabbedPane2.getTabCount() -1);
            }
        }
    }//GEN-LAST:event_treeProjectMouseClicked

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        RunTimeConfig.isWarpContent = jCheckBox1.isSelected();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        LqSystem.FileDialog dialog = new LqSystem.FileDialog(this, LqSystem.FileDialog.Types.save, false);

        String[] ret;
        
        if ((ret = dialog.show()) != null) {
            LqProject.saveProject(ret[0]);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void treeLayersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeLayersMouseClicked
        //if (evt.getClickCount() == 2) {
        if (treeLayers.getSelectionCount() > 0) {
            Object obj = RunTimeConfig.treeAssetCache.getNodeObj(treeLayers.getSelectionRows()[0]);
        
            if (obj instanceof LqSprite) {
                LqSprite sprite = (LqSprite) obj;

                RunTimeConfig.objectSelected = sprite;
            } else if (obj instanceof LqPhysicsBox) {
                LqPhysicsBox box = (LqPhysicsBox) obj;
            
                RunTimeConfig.objectSelected = box;
                
                switchAtribPane(new AtribLqPhysicsBox(box, this));
            } else if (obj instanceof LqPhysicsCircle) {
                LqPhysicsCircle circle = (LqPhysicsCircle) obj;

                RunTimeConfig.objectSelected = circle;
            } else if (obj instanceof LqBox) {
                LqBox box = (LqBox) obj;
                
                switchAtribPane(new AtribLqBox(box, this));
            }
        }
    }//GEN-LAST:event_treeLayersMouseClicked

    private void switchAtribPane(JPanel obj) {
        jPanel10.removeAll();
        
        GroupLayout jPanel10Layout = new GroupLayout(jPanel10);

        jPanel10.setLayout(jPanel10Layout);

        jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(obj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(obj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (treeLayers.getSelectionCount() > 0) {
            LqMap mapSelected = getUIMap();
            
            if (mapSelected == null) {
                JOptionPane.showMessageDialog(null, "No Map in Screen.", "Alert", JOptionPane.INFORMATION_MESSAGE);
                
                return;
            }
            
            int idxTree = treeLayers.getSelectionRows()[0];
            
            Object obj = RunTimeConfig.treeAssetCache.getNodeObj(idxTree);
            
             if (obj instanceof LqSprite) {
                LqSprite sprite = (LqSprite) obj;
                
                found: {
                    for (int i = 0; i < mapSelected.layers.getCount(); i++) {
                        for (int j = 0; j < mapSelected.layers.getLayer(i).sprites.size(); j++) {
                            if (mapSelected.layers.getLayer(i).sprites.get(j).equals(sprite)) {
                                mapSelected.layers.getLayer(i).sprites.remove(j);
                                
                                break found; //jump outside O^2
                            }
                        }
                    }
                }
                
                RunTimeConfig.treeAssetCache.removeNode(idxTree);
                RunTimeConfig.objectSelected = null;
                updateAssetTree();
             }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked
        // TODO add your handling code here:
        if ((evt.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
            LqMap atualMap = (LqMap) RunTimeConfig.mapCacheUI.getNodeObj(jTabbedPane2.getSelectedIndex());
        
            if (atualMap != null) {
                RunTimeConfig.mapCacheUI.removeNode(jTabbedPane2.getSelectedIndex());
            }
            
            jTabbedPane2.remove(jTabbedPane2.getSelectedIndex());
        }
        
        updateTree();
    }//GEN-LAST:event_jTabbedPane2MouseClicked

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        About aboutScreen = new About(this, true);
        aboutScreen.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        LqMap atualMap = (LqMap) RunTimeConfig.mapCacheUI.getNodeObj(jTabbedPane2.getSelectedIndex());
        
        if (atualMap == null) {
            JOptionPane.showMessageDialog(null, "No Map in Screen.", "Alert", JOptionPane.INFORMATION_MESSAGE);

            return;
        }
        
        String ret = JOptionPane.showInputDialog(null, "Type the name of new layer", "New layer", JOptionPane.QUESTION_MESSAGE);

        if (ret != null && !ret.isEmpty()) {
            atualMap.layers.addLayer(atualMap.layers.getCount() -1, ret);
            
            updateAssetTree();
        }
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        String ret = JOptionPane.showInputDialog(null, "Type the name of new map", "New map", JOptionPane.QUESTION_MESSAGE);

        if (ret != null && !ret.isEmpty()) {
            addEditorMap(ret);
            
            updateTree();
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        LqMap atualMap = (LqMap) RunTimeConfig.mapCacheUI.getNodeObj(jTabbedPane2.getSelectedIndex());
        
        atualMap.mainCamera.setX(0);
        atualMap.mainCamera.setY(0);
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        LqMap mapSelected = getUIMap();
        
        if (mapSelected == null) {
            JOptionPane.showMessageDialog(null, "No Map in Screen.", "Alert", JOptionPane.INFORMATION_MESSAGE);

            return;
        }
        
        RunTimeConfig.assetSelected = null;
        RunTimeConfig.objectSelected = null;

        int idxTree = treeLayers.getSelectionRows()[0];

        Object obj = RunTimeConfig.treeAssetCache.getNodeObj(idxTree);

        if (obj instanceof LqSprite) {
           LqSprite sprite = (LqSprite) obj;

           found: {
               for (int i = 0; i < mapSelected.layers.getCount(); i++) {
                   for (int j = 0; j < mapSelected.layers.getLayer(i).sprites.size(); j++) {
                       if (mapSelected.layers.getLayer(i).sprites.get(j).equals(sprite)) {

                           //mapSelected.layers.getLayer(i).sprites.remove(j);
                           mapSelected.layers.getLayer(i).flipSpritePosition(j, j -1);

                           break found; //jump outside O^2
                       }
                   }
               }
           }

          //RunTimeConfig.treeAssetCache.removeNode(idxTree);
          // RunTimeConfig.objectSelected = null;
           updateAssetTree();
           
           treeLayers.setSelectionRow(idxTree -1);
        }
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        LqMap mapSelected = getUIMap();
        
        if (mapSelected == null) {
            JOptionPane.showMessageDialog(null, "No Map in Screen.", "Alert", JOptionPane.INFORMATION_MESSAGE);

            return;
        }
        
        RunTimeConfig.assetSelected = null;
        RunTimeConfig.objectSelected = null;
        
        int idxTree = treeLayers.getSelectionRows()[0];

        Object obj = RunTimeConfig.treeAssetCache.getNodeObj(idxTree);

        if (obj instanceof LqSprite) {
           LqSprite sprite = (LqSprite) obj;

           found: {
               for (int i = 0; i < mapSelected.layers.getCount(); i++) {
                   for (int j = 0; j < mapSelected.layers.getLayer(i).sprites.size(); j++) {
                       if (mapSelected.layers.getLayer(i).sprites.get(j).equals(sprite)) {

                           //mapSelected.layers.getLayer(i).sprites.remove(j);
                           mapSelected.layers.getLayer(i).flipSpritePosition(j, j +1);

                           break found; //jump outside O^2
                       }
                   }
               }
           }
           
          // RunTimeConfig.treeAssetCache.removeNode(idxTree);
          // RunTimeConfig.objectSelected = null;
           updateAssetTree();
           
           treeLayers.setSelectionRow(idxTree +1);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed

    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        clearSelection();
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        clearSelection();
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
        LqPhysicsCircle circle = new LqPhysicsCircle(10f);       
        
        RunTimeConfig.assetSelected = (LqMapObject) circle;        
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void jToggleButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton8ActionPerformed
        LqPhysicsBox  box = new LqPhysicsBox();
        box.setWidth(200);
        box.setHeight(100);
        
        
        RunTimeConfig.assetSelected = (LqMapObject) box;    
    }//GEN-LAST:event_jToggleButton8ActionPerformed

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        LqLight light = new LqLight();
        
        RunTimeConfig.assetSelected = (LqMapObject) light;    
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton10ActionPerformed
        LqBox box = new LqBox(400, 400);
        //box.setColor(new LqColor(234, 235, 246, 1f));
        box.setColor(new LqColor(com.badlogic.gdx.graphics.Color.GRAY));
        
        RunTimeConfig.assetSelected = (LqMapObject) box;
    }//GEN-LAST:event_jToggleButton10ActionPerformed
    
    public void updateTree() {
        updateProjectTree();
        updateAssetTree();
    }
    
    private void updateProjectTree() {
        int i;
        RunTimeConfig.treeProjectCache.clear();
        
        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode(LqProject.name);
        DefaultMutableTreeNode treeNode2 = new DefaultMutableTreeNode("Scripts");
        
        //Get the Script names...
        for(i = 0; i < LqProject.scripts.getCount(); i++) {
            LqLuaScript obj = LqProject.scripts.getScript(i);
            
            treeNode2.add(new DefaultMutableTreeNode(obj.name));
            
            RunTimeConfig.treeProjectCache.addNode(2 + i, obj);
        }
        
        treeNode1.add(treeNode2);
        treeNode2 = new DefaultMutableTreeNode("Maps");
        
        //Get the Map names...
        for(i = 0; i < LqProject.maps.getCount(); i++) {
            LqMap obj = LqProject.maps.getMap(i);
            
            treeNode2.add(new DefaultMutableTreeNode(obj.name));
            
            RunTimeConfig.treeProjectCache.addNode(2 + LqProject.scripts.getCount() + 1 + i, obj);
        }
        
        treeNode1.add(treeNode2);
        
        treeProject.setModel(new DefaultTreeModel(treeNode1));
    }
    
    private LqMap getUIMap() {
        return (LqMap) RunTimeConfig.mapCacheUI.getNodeObj(jTabbedPane2.getSelectedIndex());
    }
    
    private void updateAssetTree() {
        int index = 1;
        LqMap atualMap = getUIMap();
        
        if (atualMap == null)
            return;
        
        RunTimeConfig.treeAssetCache.clear();
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Layers");
        
        for(int i = 0; i < atualMap.layers.getCount(); i++) {
            LqLayer atualLayer = atualMap.layers.getLayer(i);
            
            DefaultMutableTreeNode nodeAtualLayer = new DefaultMutableTreeNode(atualLayer.name);
            
            for(int j = 0; j < atualLayer.sprites.size(); j++) {
                if (atualLayer.sprites.get(j) instanceof LqSprite) {
                    LqSprite atualSprite = (LqSprite) atualLayer.sprites.get(j);

                    nodeAtualLayer.add(new DefaultMutableTreeNode(String.valueOf(j) + " - " + atualSprite.name));
                    index++;

                    RunTimeConfig.treeAssetCache.addNode(index, atualSprite);
                } else if (atualLayer.sprites.get(j) instanceof LqPhysicsBox) {
                    LqPhysicsBox atualBox = (LqPhysicsBox) atualLayer.sprites.get(j);

                    nodeAtualLayer.add(new DefaultMutableTreeNode(String.valueOf(j) + " - " + atualBox.name));
                    index++;

                    RunTimeConfig.treeAssetCache.addNode(index, atualBox);
                } else if (atualLayer.sprites.get(j) instanceof LqPhysicsCircle) {
                    LqPhysicsCircle atualCircle = (LqPhysicsCircle) atualLayer.sprites.get(j);

                    nodeAtualLayer.add(new DefaultMutableTreeNode(String.valueOf(j) + " - " + atualCircle.name));
                    index++;

                    RunTimeConfig.treeAssetCache.addNode(index, atualCircle);
                } else if (atualLayer.sprites.get(j) instanceof LqLight) {
                    LqLight atualLight = (LqLight) atualLayer.sprites.get(j);

                    nodeAtualLayer.add(new DefaultMutableTreeNode(String.valueOf(j) + " - " + atualLight.name));
                    index++;

                    RunTimeConfig.treeAssetCache.addNode(index, atualLight);
                } else if (atualLayer.sprites.get(j) instanceof LqBox) {
                    LqBox atualBox = (LqBox) atualLayer.sprites.get(j);

                    nodeAtualLayer.add(new DefaultMutableTreeNode(String.valueOf(j) + " - " + atualBox.name));
                    index++;

                    RunTimeConfig.treeAssetCache.addNode(index, atualBox);
                }
            }
            
            root.add(nodeAtualLayer);
            index++;
        }
        
        treeLayers.setModel(new DefaultTreeModel(root)); 
        
        for (int i = 0; i < treeLayers.getRowCount(); i++) {
            treeLayers.expandRow(i);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane10;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane10;
    private javax.swing.JTabbedPane jTabbedPane11;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    private javax.swing.JTree treeLayers;
    private javax.swing.JTree treeProject;
    // End of variables declaration//GEN-END:variables
}
