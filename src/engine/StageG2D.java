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

package engine;

import javax.swing.JPanel;


public class StageG2D extends JPanel {
//
//    protected Alika2DG2D alika; //Graphics!
//
//    private final LqMap map;
//    private Point mousePointer = new Point();
//
//    int preX, preY;
//    boolean pressOut = false;
//    boolean isFirstTime = true;
//
//    public static CrasseLua retangulo; //TODO: Remove that!!!!!!!
//
//    public StageG2D(LqMap map) {
//        this.map = map;
//
//        retangulo = new CrasseLua();
//
//        setDoubleBuffered(true);
//
//        //this.setBackground(new Color(237, 237, 237));
//        this.setBackground(new Color(0, 153, 255));
//        //---
//
//        //Timer to render
//        JPanel xz = this;
//        Action x = new AbstractAction() {
//
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                xz.repaint();
//            }
//        };
//
//        new Timer(10, x).start(); // ~60 FPS 
//
//        this.addMouseMotionListener(mouseMotionListner());
//        this.addMouseListener(mouseListner());
//        this.addMouseWheelListener(mouseWheelListner());
//
//        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
//        manager.addKeyEventDispatcher(keyboardListner());
//
//    }
//
//    private KeyEventDispatcher keyboardListner() {
//        return new KeyEventDispatcher() {
//
//            @Override
//            public boolean dispatchKeyEvent(KeyEvent ke) {
//                
//                if (ke.getKeyCode() == KeyEvent.VK_UP) {
//                    map.mainCamera.setY(map.mainCamera.getY() - 10);
//                    
//                    requestFocusInWindow();
//                }
//
//                if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
//                    map.mainCamera.setY(map.mainCamera.getY() + 10);
//                    
//                    requestFocusInWindow();
//                }
//
//                if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
//                    map.mainCamera.setX(map.mainCamera.getX() + 10);
//                }
//
//                if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
//                    map.mainCamera.setX(map.mainCamera.getX() - 10);
//                }
//
//                return false;
//            }
//        };
//    }
//    
//    private int lastOffsetX;
//    private int lastOffsetY;
//
//    private MouseAdapter mouseListner() {
//        return new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent me) {
//                if (RunTimeConfig.assetSelected != null) {
//                    LqSprite newSprite = new LqSprite();
//                    newSprite.asset = RunTimeConfig.assetSelected.getAsset();
//                    
//                    if (RunTimeConfig.isWarpContent) {
//                        int x = map.SizeBlock.width;
//                        int y = map.SizeBlock.height;
//
//                        int i, j;
//
//                        int h = map.yBlocks == 0 ? getHeight() : map.yBlocks * y;
//                        int w = map.xBlocks == 0 ? getWidth()  : map.xBlocks * x;
//                        
//                        int fX = 0, fY = 0;
//
//                        if (mousePointer != null) {
//                            Rectangle rect = null;
//
//                            for (i = 0; i <= w; i += x) {
//                                for (j = 0; j <= h; j += y) {
//                                    if (mousePointer.x >= i && mousePointer.x <= (i != 0 ? 2 * i : x)) {
//                                        if (mousePointer.y >= j && mousePointer.y <= (j != 0 ? 2 * j : y)) {
//                                            fX = i;
//                                            fY = j;
//                                        }
//                                    }
//                                }
//                            }
//                            
//                            newSprite.x = fX;
//                            newSprite.y = fY;
//                        }
//                    } else {
//                        newSprite.x = mousePointer.x;
//                        newSprite.y = mousePointer.y;
//                    }
//                    
//                    map.layers.addSprite(0, newSprite);
//                }
//            }
//            
//            @Override
//            public void mousePressed(MouseEvent e) {
//                    // capture starting point
//                    lastOffsetX = e.getX();
//                    lastOffsetY = e.getY();
//            }
//
//            //---
//        };
//    }
//    
//    public void updateLocation(MouseEvent e) {
//        retangulo.rect.setLocation(preX + e.getX(), preY + e.getY());
//
//        if (checkRect()) {
//          LqLog.log(retangulo.rect.getX() + ", " + retangulo.rect.getY());
//        } else {
//         // LqLog.log("drag inside the area.");
//        }
//
//        repaint();
//    }
//     
//    Rectangle area;
//    
//    boolean checkRect() {
//        if (area == null) {
//          return false;
//        }
//
//        if (area.contains(retangulo.rect.x, retangulo.rect.y, 100, 50)) {
//          return true;
//        }
//        int new_x = retangulo.rect.x;
//        int new_y = retangulo.rect.y;
//
//        if ((retangulo.rect.x + 100) > area.getWidth()) {
//          new_x = (int) area.getWidth() - 99;
//        }
//        if (retangulo.rect.x < 0) {
//          new_x = -1;
//        }
//        if ((retangulo.rect.y + 50) > area.getHeight()) {
//          new_y = (int) area.getHeight() - 49;
//        }
//        if (retangulo.rect.y < 0) {
//          new_y = -1;
//        }
//        retangulo.rect.setLocation(new_x, new_y);
//        
//        return false;
//      
//    } 
//
//    private MouseAdapter mouseMotionListner() {
//        return new MouseAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent me) {
//                //Get position of mouse on plane
//                //Division for scale
//                //Substraction for camera position
//                mousePointer.x = (int) ((me.getX() - map.mainCamera.getX()) /  (map.scale.getX()));
//                mousePointer.y = (int) ((me.getY() - map.mainCamera.getY()) /  (map.scale.getY()));
//            }
//
//            public void mouseDragged(MouseEvent me) {
//                mousePointer.x = (int) ((me.getX() - map.mainCamera.getX()) /  (map.scale.getX()));
//                mousePointer.y = (int) ((me.getY() - map.mainCamera.getY()) /  (map.scale.getY()));
//               //Mouse move
//                //System.out.println("moveuzz");
//
//               //Middle Button
//               if ((me.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
//                   int newX = me.getX() - lastOffsetX;
//                   int newY = me.getY() - lastOffsetY;
//                   
//                   lastOffsetX += newX;
//                   lastOffsetY += newY;
//                   
//                   map.mainCamera.setX(newX + map.mainCamera.getX());
//                   map.mainCamera.setY(newY + map.mainCamera.getY());
//                }
//            }
//            
//            //---
//            @Override
//            public void mousePressed(MouseEvent e) {
//                /*preX = retangulo.x - e.getX();
//                preY = retangulo.y - e.getY();
//
//                if (retangulo.rect.contains(e.getX(), e.getY())) {
//                    //updateLocation(e);
//                } else {
//                    LqLog.log("Drag it.");
//                    pressOut = true;
//                }*/
//            }
//
//            /*public void mouseDragged(MouseEvent e) {
//                if (!pressOut) {
//                    updateLocation(e);
//                } else {
//                    LqLog.log("Drag it.");
//                }
//            }*/
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                /*if (retangulo.rect.contains(e.getX(), e.getY())) {
//                    updateLocation(e);
//                } else {
//                    LqLog.log("Drag it.");
//                    pressOut = false;
//                }*/
//            }
//        };
//    }
//
//    private void workingMousePointer(Alika2DG2D ali2d) {
//        int x = map.SizeBlock.width ;
//        int y = map.SizeBlock.height ;
//        
//        int i, j;
//        /*int h = this.getHeight();
//        int w = this.getWidth();*/
//        
//        int h = map.yBlocks == 0 ? this.getHeight() : map.yBlocks * y;
//        int w = map.xBlocks == 0 ? this.getWidth()  : map.xBlocks * x;
//
//        if (mousePointer != null) {
//            Rectangle rect = null;
//            Image img = null;
//            int fX = 0, fY = 0;
//
//            if (RunTimeConfig.assetSelected != null && !RunTimeConfig.isWarpContent) {
//                //img = RunTimeConfig.assetSelected.getSprite();
//                fX = mousePointer.x;
//                fY = mousePointer.y;
//            } else {
//                for (i = 0; i <= w; i += x) {
//                    for (j = 0; j <= h; j += y) {
//                        if (mousePointer.x >= i && mousePointer.x <= (i != 0 ? 2 * i : x)) {
//                            if (mousePointer.y >= j && mousePointer.y <= (j != 0 ? 2 * j : y)) {
//                                if (RunTimeConfig.assetSelected != null) {
//                                    //img = RunTimeConfig.assetSelected.getSprite();
//                                    fX = i;
//                                    fY = j;
//                                } else {
//                                    rect = new Rectangle(i, j, x, y);
//                                }
//                            }
//                        }
//                    }
//                } 
//            }
//             
//            
//            if (rect != null) {
//                ali2d.setColor(Color.GREEN);
//                ali2d.draw(rect);
//                ali2d.fill(rect);
//            } else if (img != null) {
//                ali2d.getGraphicsObj().drawImage(img, fX, fY, this);
//            }
//        }
//    }
//
//    private void drawGridForm(Alika2DG2D ali2d) {
//        int x = map.SizeBlock.width;
//        int y = map.SizeBlock.height;
//        
//        int i, j;
//        
//        /*int h = this.getHeight();
//        int w = this.getWidth(); */
//        
//        int h = map.yBlocks == 0 ? this.getHeight() : map.yBlocks * y;
//        int w = map.xBlocks == 0 ? this.getWidth()  : map.xBlocks * x;
//        
//        JPanel window = this;
//
//        for (i = 0; i <= w; i += x) {
//            for (j = 0; j <= h; j += y) {
//                Rectangle rect = new Rectangle(i, j, x, y);
//
//                ali2d.setColor(Color.DARK_GRAY);
//                ali2d.draw(rect);
//            }
//        }
//    }
//
//    private void drawMap(Alika2DG2D ali2d) {
//        int i, j, x, y;
//        Layers layers = map.layers;
//        Layer layer;
//        LqSprite sprite;
//        Image img;
//        int countLayers = layers.getCount();
//
//        for (i = 0; i < countLayers; i++) {
//            layer = layers.getLayer(i);
//
//            for (j = 0; j < layer.sprites.size(); j++) {
//                sprite = layer.sprites.get(j);
//
//                //CacheSprite cacheSpriteObj = CacheSprites.getSearchCacheSprite(sprite.asset);
//      /*          LqCacheSprite cacheSpriteObj = CacheSprites.getSearchCacheSprite(sprite.asset) == null
//                        ? CacheSprites.addCacheSprite(sprite.asset) : CacheSprites.getSearchCacheSprite(sprite.asset);
//*/
//                //img = cacheSpriteObj.getSprite();
//                x = sprite.x;
//                y = sprite.y;
//                
//               // ali2d.getGraphicsObj().drawImage(img, x, y, null);
//            }
//        }
//    }
//    
//    private void drawSelectedObj() {
//        if (RunTimeConfig.objectSelected != null) {
//            //ali2d.getGraphicsObj().drawString("Selected", x, y);
//
//            LqCacheSprite cacheSpriteObj = map.cacheSprites.getSearchCacheSprite(RunTimeConfig.objectSelected.asset);
//
//            alika.getGraphicsObj().setColor(Color.BLUE);
//
//           // Rectangle rect = new Rectangle(new Point(RunTimeConfig.objectSelected.x, RunTimeConfig.objectSelected.y), new Dimension(cacheSpriteObj.getWidth(), cacheSpriteObj.getHeight()));
//
////            alika.draw(rect);
//        }
//    }
//
//    private AffineTransform coordTransform = new AffineTransform();
//    
//    @Override
//    public void paint(Graphics g) {
//        super.paint(g);
//        
//        alika = new Alika2DG2D(g, this); //Push matrix
//        {
//            if (RunTimeConfig.showFPS)
//                alika.showFPS();
//        }
//        alika.dispose(); //Pop matrix
//        Dimension dim = getSize();
//        int w = (int) dim.getWidth();
//        int h = (int) dim.getHeight();
//        
//        if (isFirstTime) {
//            //area = new Rectangle(getWidth(), getHeight());
//            
//            retangulo.rect.setLocation(w / 2 - 50, h / 2 - 25);
//            isFirstTime = false;
//          }
//
//        try {
//            alika.activateAntialiasing();
//
//            //Camera
//            if (map.mainCamera != null) {
//                alika.getGraphicsObj().translate(map.mainCamera.getX(), map.mainCamera.getY());
//            } else {
//                map.mainCamera = new LqCamera();
//            }
//            
//            if (map.scale != null) {
//                alika.getGraphicsObj().scale(map.scale.getX(), map.scale.getY());
//            } else {
//                map.scale = new Point2D.Double(1, 1);
//            }
//            //---
//
//            drawMap(alika);
//            
//            if (RunTimeConfig.showGrid) {
//                drawGridForm(alika);
//            }
//
//            drawSelectedObj();
//            
//            workingMousePointer(alika);
//
//            /*alika.getGraphicsObj().setColor(Color.yellow);
//             alika.getGraphicsObj().fillRect(retangulo.x, retangulo.y, 200, 200);*/
//            
//            //alika.getGraphicsObj().draw(retangulo.rect);
//            
//            if (RunTimeConfig.showLight) {
//                alika.light(mousePointer);
//            }
//
//        } finally {
//            Thread.yield();
//
//            alika.dispose();
//        }
//    }
//    
//    private final float zoomAmount = .1f;
//    
//    private MouseWheelListener mouseWheelListner() {
//        return new MouseWheelListener() {
//
//            @Override
//            public void mouseWheelMoved(MouseWheelEvent e) {
//                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
//                    double antX = map.scale.getX();
//                    double antY = map.scale.getY();
//                    
//                    int wheelRotation = e.getWheelRotation();
//
//                    if (wheelRotation < 0) {
//                        map.scale.setLocation(antX + zoomAmount, antY + zoomAmount);
//                    } else {
//                        if (antY >= 0.1 && antX >= 0.1) {
//                           map.scale.setLocation(antX - zoomAmount, antY - zoomAmount);
//                        }
//                    }
//                }
//            }
//        };
//    }
//
//    private class ScaleHandler implements MouseWheelListener {
//
//        public void mouseWheelMoved(MouseWheelEvent e) {
//
//            int x = e.getX();
//            int y = e.getY();
//
//            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
//
//                /*if (myRect.getBounds2D().contains(x, y)) {
//                 float amount = e.getWheelRotation() * 5f;
//                 myRect.width += amount;
//                 myRect.height += amount;
//                 repaint();
//                 }*/
//                //amount_translate += e.getWheelRotation() * 5f;
//
//              //alika.getGraphicsObj().scale(0, amount);
//                //alika.getGraphicsObj().translate(0, amount);
//                //System.out.println(amount);
//            }
//        }
//    }
}
