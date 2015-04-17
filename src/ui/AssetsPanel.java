package ui;

import engine.Alika2D;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import project.CacheSprites;
import project.LqAsset;
import project.LqCacheSprite;
import project.LqMapObject;
import project.LqProject;

public class AssetsPanel extends JPanel {

    //Screen Objects
    private JComboBox categoriesList;
    private JScrollPane panelListAssets;
    private JPanel subContainer;
    private ArrayList<JButton> assetButtons;
    private int countAssets = 0;

    //Cache
    private int uiCache = 0;
    private int newComboIndex;

    //Fills
    private final Dimension assetButtonSize = new Dimension(90, 90);

    public AssetsPanel() {
        subContainer = new JPanel();
        categoriesList = new JComboBox();
        panelListAssets = new JScrollPane(subContainer);
        assetButtons = new ArrayList<>();

        //categoriesList.setModel(new DefaultComboBoxModel(new String[] {"Default"}));
        subContainer.setLayout(new WrapLayout());
        panelListAssets.setBorder(null);

        
        //add
        GroupLayout jPanel1Layout = new GroupLayout(this);

        this.setLayout(jPanel1Layout);

        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(categoriesList, 0, 215, Short.MAX_VALUE)
                .addComponent(panelListAssets)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(categoriesList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelListAssets, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
        );

        categoriesList.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                uiCache = 0;
                clearButtonsAssets();

               /* new Thread() {
                    @Override
                    public void run() {
                        updateUIZ();
                    }
                }.start(); */
                
                updateUIZ();
            }
        });
        
        //zz();

        refreshCombo();

        //updateUIZ();
    }
    
    public void addNewAsset(final LqAsset asset) {
        JButton newAsset = new JButton();
        
        //Creating the cache...
        /*LqCacheSprite cacheSpriteObj = CacheSprites.getSearchCacheSprite(asset) == null ? 
                CacheSprites.addCacheSprite(asset) : CacheSprites.getSearchCacheSprite(asset) ;
        */
        
        //lambda
        /*newAsset.addActionListener((ActionEvent e) -> {
            RunTimeConfig.assetSelected = asset;
        });*/
        
        
        newAsset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                  RunTimeConfig.assetSelected = (LqMapObject) asset;
            }
        });
        
        
        
        
        newAsset.setPreferredSize(assetButtonSize);
        newAsset.setIcon(new ImageIcon(
                Alika2D.loadIcon(asset.path).getScaledInstance(assetButtonSize.height, assetButtonSize.width, Image.SCALE_DEFAULT))
        );

        assetButtons.add(newAsset);
        subContainer.add(assetButtons.get(countAssets));

        countAssets++;

        subContainer.updateUI();
    }

    public void refreshCombo() {
        int antOption = categoriesList.getSelectedIndex();
        
        categoriesList.setModel(new DefaultComboBoxModel(LqProject.assets.getCategoryList()));
        
        if (antOption != -1 && categoriesList.getItemCount() < 0)
            categoriesList.setSelectedIndex(antOption);
    }
    
    public void clearButtonsAssets() {
        Component[] components = subContainer.getComponents();

        for (Component component : components)
            if (component.getClass().getName().toString().equals("javax.swing.JButton")) 
                subContainer.remove(component);
        
       //subContainer.removeNotify();
       //System.gc();
    }

    public void updateUIZ() {
        /*if (uiCache == 0) {
            clearButtonsAssets();
        } */

        int i = categoriesList.getSelectedIndex(), j;

        for (j = uiCache; j < LqProject.assets.getAssets().get(i).getCountAssets(); j++) {
            addNewAsset(LqProject.assets.getAssets().get(i).assets.get(j));
        }

        uiCache = j;
        
        this.updateUI();
    }
}
