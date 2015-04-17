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

import box2dLight.RayHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.awt.Dimension;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.CacheSprites;
import project.Layers;
import project.LqAsset;
import project.LqBox;
import project.LqCacheSprite;
import project.LqColor;
import project.LqLayer;
import project.LqLight;
import project.LqMap;
import project.LqMapObject;
import project.LqPhysicsBox;
import project.LqPhysicsCircle;
import project.LqSprite;
import ui.LqWindow;
import ui.RunTimeConfig;

/**
 *
 * @author Gabriel Marinho <gabrielbiga@me.com>
 */
public class StageGDX extends Game implements InputProcessor {
    
    private final LqMap map;
    private final LqWindow window;
    
    private Alika2D alika2d;
    
    private Viewport viewport;
    public  OrthographicCamera camera;
    
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    
    private Box2DDebugRenderer renderer;
    private World world;
    private RayHandler  handler;
    private LqLight luz;
    
    private Point mousePointer = new Point(0, 0);
    private final float zoomAmount = .2f;
    
    private final int PIXELS_PER_METER = 1;
    private final Vector3 _lastMouseWorldMovePos = new Vector3();
    private final Vector3 _lastMouseWorldDragPos = new Vector3();
    private final Vector3 _lastMouseWorldPressPos = new Vector3();
    private final Vector3 _lastMouseWorldReleasePos = new Vector3();
    private final Vector3 _lastMouseScreenPos = new Vector3();
   
    private int _mouseButtonDown = -1;
    
    private final boolean invertViewPort = false; //Origin in the top of corner
    
     //THE FUCKING CACHE
    public transient CacheSprites cacheSprites = new CacheSprites();
    
    public StageGDX(LqMap map, LqWindow window) {
       this.map = map;
       this.window = window;
    }
    
    
    private void workingMousePointer() {
        int x = map.SizeBlock.width;
        int y = map.SizeBlock.height;
        
        int i, j;
        
        int h = map.yBlocks == 0 ? Gdx.graphics.getHeight() : map.yBlocks * y;
        int w = map.xBlocks == 0 ? Gdx.graphics.getWidth()  : map.xBlocks * x;

        if (mousePointer != null) {
            LqBox rect = null;
            //Sprite img = null;
            boolean isPreview = false;
            int fX = 0, fY = 0;

            if (RunTimeConfig.assetSelected != null && !RunTimeConfig.isWarpContent) {
                //img = allocSearchLqAsset(RunTimeConfig.assetSelected);
                isPreview = true;
                fX = mousePointer.x;
                fY = mousePointer.y;
            } else {
                for (i = 0; i <= w; i += x) {
                    for (j = 0; j <= h; j += y) {
                        if (mousePointer.x >= i && mousePointer.x <= (i != 0 ? 2 * i : x)) {
                            if (mousePointer.y >= j && mousePointer.y <= (j != 0 ? 2 * j : y)) {
                                if (RunTimeConfig.assetSelected != null) {
                                    //img = allocSearchLqAsset(RunTimeConfig.assetSelected);
                                    isPreview = true;
                                    fX = i;
                                    fY = j;
                                } else {
                                    rect = new LqBox(i, j, x, y);
                                }
                            }
                        }
                    }
                } 
            }
            
            if (rect != null) {
                rect.setColor(new LqColor(Color.GREEN));
                alika2d.drawRect(rect, ShapeRenderer.ShapeType.Filled);
            } 
            
            if (isPreview) {
                if (RunTimeConfig.assetSelected instanceof LqAsset) {
                    Sprite img = allocSearchLqAsset((LqAsset) RunTimeConfig.assetSelected, false);
                    alika2d.drawImage(img, new Point(fX, fY));
                    alika2d.drawImage(img, new Point(fX, fY)); //Gambi to overlap others sprites
                }
                
                if (RunTimeConfig.assetSelected instanceof LqPhysicsBox) {
                    if (tmpLqForm == null || !tmpLqForm.equals(RunTimeConfig.assetSelected)) {
                        //RunTimeConfig.assetSelected = null;
                        updateTmpLqForm();
                        LqPhysicsBox b = (LqPhysicsBox) RunTimeConfig.assetSelected;
                       
                        b.setBody(world.createBody(b.getBoxBodyDef()));
                        b.getBody().createFixture(b.getBox(), 0.0f);
                        
                        tmpLqForm = b;
                    }
                    
                    tmpLqForm.setX(fX);
                    tmpLqForm.setY(fY);
                    
                }
                
                if (RunTimeConfig.assetSelected instanceof LqPhysicsCircle) {
                    if (tmpLqForm == null || !tmpLqForm.equals(RunTimeConfig.assetSelected)) {
                        //RunTimeConfig.assetSelected = null;
                        updateTmpLqForm();
                        LqPhysicsCircle c = (LqPhysicsCircle) RunTimeConfig.assetSelected;
                       
                        c.setBody(world.createBody(c.getCircleDef()));
                        c.getBody().createFixture(c.getCircleFixture());
                        
                        tmpLqForm = c;
                    }
                    
                    tmpLqForm.setX(fX);
                    tmpLqForm.setY(fY);
                    
                }
                
                if (RunTimeConfig.assetSelected instanceof LqLight) {
                    if (tmpLqForm == null || !tmpLqForm.equals(RunTimeConfig.assetSelected)) {
                        //RunTimeConfig.assetSelected = null;
                        updateTmpLqForm();
                        LqLight c = (LqLight) RunTimeConfig.assetSelected;
                        
                        c.alloc(handler, 1000);
                        c.setColor(new LqColor(Color.OLIVE));
                        c.setDistance(1000);
                        
                        tmpLqForm = c;
                    }
                    
                    tmpLqForm.setX(fX);
                    tmpLqForm.setY(fY);
                    
                }
                
                if (RunTimeConfig.assetSelected instanceof LqBox) {
                    /*if (tmpLqForm == null || !tmpLqForm.equals(RunTimeConfig.assetSelected)) {
                        //RunTimeConfig.assetSelected = null;
                        updateTmpLqForm();
                        LqBox c = (LqBox) RunTimeConfig.assetSelected;
                        
                        tmpLqForm = c;
                    }*/
                    
                    LqBox c = (LqBox) RunTimeConfig.assetSelected;
                    
                    c.setX(fX);
                    c.setY(fY);
                    
                    alika2d.drawRect(c, ShapeRenderer.ShapeType.Filled);
                    
                   
                    
                }
            }
                
        }
    }
    
    private LqMapObject tmpLqForm;
    
    private LqMapObject getLqTmpForm() {
        if (tmpLqForm == null) {
            
        }
        
        return null;
    }
    
    private Sprite allocSearchLqAsset(LqAsset asset, boolean unique) {
        LqCacheSprite c;
        
        /*if (unique) {
            if ((c = cacheSprites.getSearchCacheSprite(asset.path)) == null) {
                c = cacheSprites.addCacheSprite(asset, alika2d.loadSprite(asset.path));
            }
        } else {
            if ((c = cacheSprites.getSearchCacheSprite(asset)) == null) {
                c = cacheSprites.addCacheSprite(asset, alika2d.loadSprite(asset.path));
            }
        }*/
        
        
        if ((c = cacheSprites.getSearchCacheSprite(asset)) == null) {
                c = cacheSprites.addCacheSprite(asset, alika2d.loadSprite(asset.path));
            }
        
        return c.getSprite();
    }
    
    
    private void drawGridForm() {
        int x = map.SizeBlock.width;
        int y = map.SizeBlock.height;
        
        int h = map.yBlocks == 0 ? Gdx.graphics.getHeight() : map.yBlocks * y;
        int w = map.xBlocks == 0 ? Gdx.graphics.getWidth()  : map.xBlocks * x;
        
        //JPanel window = this;

        for (int i = 0; i <= w; i += x) {
            for (int j = 0; j <= h; j += y) {
                LqBox rect = new LqBox(i, j, x, y);

                //ali2d.setColor(java.awt.Color.DARK_GRAY);
                rect.setColor(new LqColor(Color.DARK_GRAY));
                alika2d.drawRect(rect, ShapeRenderer.ShapeType.Line);
            }
        }
    }
    
    private void drawMap() {
        int i, j, x=0, y=0;
        Layers layers = map.layers;
        LqLayer layer;
        LqSprite sprite;
        Sprite img = null;
        int countLayers = layers.getCount();

        for (i = 0; i < countLayers; i++) {
            layer = layers.getLayer(i);

            for (j = 0; j < layer.sprites.size(); j++) {
                //sprite = (LqSprite) layer.sprites.get(j);
                
                if (layer.sprites.get(j) instanceof LqSprite) {
                    sprite = (LqSprite) layer.sprites.get(j);
                    img = allocSearchLqAsset(sprite.asset, true);
                    x = sprite.getX();
                    y = sprite.getY();

                    alika2d.drawImage(img, new Point(x, y));
                } else if (layer.sprites.get(j) instanceof LqBox) {
                    LqBox box = (LqBox) layer.sprites.get(j);
                    
                    alika2d.drawRect(box, ShapeRenderer.ShapeType.Filled);
                }
            }
        }
        
        //Gambi para renderizar o ultimo na ordem correta
        if (img != null)
            alika2d.drawImage(img, new Point(x, y));
    }
    
    private void drawSelectedObj() {
        if (RunTimeConfig.objectSelected != null) {
            LqBox rect = null;
            
            if (RunTimeConfig.objectSelected instanceof LqSprite) {
                LqCacheSprite cacheSpriteObj = cacheSprites.getSearchCacheSprite(((LqSprite) RunTimeConfig.objectSelected).asset.path);

                rect = new LqBox(new Point(RunTimeConfig.objectSelected.getX(), RunTimeConfig.objectSelected.getY()), new Dimension((int) cacheSpriteObj.getWidth(), (int) cacheSpriteObj.getHeight()));
            } else if (RunTimeConfig.objectSelected instanceof LqPhysicsBox) {
                rect = new LqBox(new Point(RunTimeConfig.objectSelected.getX() - (((LqPhysicsBox) RunTimeConfig.objectSelected).getHeight() *2), RunTimeConfig.objectSelected.getY() - ((LqPhysicsBox) RunTimeConfig.objectSelected).getHeight() ), new Dimension((int) ((LqPhysicsBox) RunTimeConfig.objectSelected).getWidth() *2 , (int) ((LqPhysicsBox) RunTimeConfig.objectSelected).getHeight() *2));
            } else if (RunTimeConfig.objectSelected instanceof LqPhysicsCircle) {
                int r = (int) ((LqPhysicsCircle) RunTimeConfig.objectSelected).getRadius();
                double areaF = (Math.PI * r *2) /2;
                int area = (int) areaF;
                
                rect = new LqBox(new Point((int) ((LqPhysicsCircle) RunTimeConfig.objectSelected).getBody().getPosition().x - r, (int) ((LqPhysicsCircle) RunTimeConfig.objectSelected).getBody().getPosition().y -r),
                                     new Dimension(area, area));
            }
            
            rect.setColor(new LqColor(Color.BLACK));
            alika2d.drawRect(rect, ShapeRenderer.ShapeType.Line);
        }
    }
    
    //Add all objects to physics
    private void physicsObjectsKickoff() {
        LqMap atualMap = map;
        
        for(int i = 0; i < atualMap.layers.getCount(); i++) {
            LqLayer atualLayer = atualMap.layers.getLayer(i);
            for(int j = 0; j < atualLayer.sprites.size(); j++) {
                LqMapObject obj = atualLayer.sprites.get(j);
                
                if (obj instanceof LqPhysicsBox) {
                    LqPhysicsBox atualBox = (LqPhysicsBox) atualLayer.sprites.get(j);

                    atualBox.reAlloc(); //Re alloc objects
                    atualBox.updateBox(); //Update screen coords
                    atualBox.setBody(world.createBody(atualBox.getBoxBodyDef()));
                    atualBox.getBody().createFixture(atualBox.getBox(), 0.0f);
                    
                } else if (obj instanceof LqPhysicsCircle) {
                    LqPhysicsCircle atualCircle = (LqPhysicsCircle) atualLayer.sprites.get(j);

                    atualCircle.reAlloc(); //Re alloc objects
                    atualCircle.updateCirclePos();//Update screen coords
                    atualCircle.setBody(world.createBody(atualCircle.getCircleDef()));
                    atualCircle.getBody().createFixture(atualCircle.getCircleFixture());
                    
                } else if (obj instanceof LqLight) {
                    ((LqLight) obj).alloc(handler, 1000);
                }
            }
        }
    }
    
    @Override
    public void create() {     
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();  
        
        alika2d = new Alika2D(batch, shapeRenderer, invertViewPort);
        world = new World(new Vector2(0, -9.8f), false);
        renderer = new Box2DDebugRenderer();
        handler = new RayHandler(world);
        
        camera= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(invertViewPort, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new ScreenViewport(camera);        
        
        
        physicsObjectsKickoff(); //Start physics objects on load project moment
        
        Gdx.input.setInputProcessor(this);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void render() {  
        Gdx.gl.glClearColor(1, 1, 1, 1);       
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL30.GL_COVERAGE_BUFFER_BIT_NV:0));
        
        camera.update();
        if (RunTimeConfig.assetSelected == null) 
            updateTmpLqForm();
        
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        handler.setCombinedMatrix(camera.combined);
        
        renderer.render(world, camera.combined);
       
         
        world.step(1/50f, 6, 2);
        
        if (RunTimeConfig.showGrid)
            drawGridForm();
        
        
        drawMap();

        workingMousePointer();

        drawSelectedObj();
        
         
        if (RunTimeConfig.showLight)
            handler.updateAndRender();
    }
    
    
    private void updateTmpLqForm() {
         {
            if (tmpLqForm != null) {
                if (tmpLqForm instanceof LqPhysicsBox) {
                    world.destroyBody(((LqPhysicsBox) tmpLqForm).getBody());
                } else if (tmpLqForm instanceof LqPhysicsCircle) {
                    world.destroyBody(((LqPhysicsCircle) tmpLqForm).getBody());
                } else if (tmpLqForm instanceof LqLight) {
                    ((LqLight) tmpLqForm).destroy();
                }
                
                tmpLqForm = null;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        
        camera.viewportHeight = height; //set the viewport
        camera.viewportWidth = width; 
        
        camera.update();
    }
    
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    
    //InputProcessor
    @Override
    public boolean touchDown(int x, int y, int i2, int button) {
        _mouseButtonDown = button;
        _lastMouseWorldPressPos.set(x, y, 0);
        camera.unproject(_lastMouseWorldPressPos);
        
        if (RunTimeConfig.assetSelected != null && button == 0) {
            LqMapObject newSprite = null;
            
            if (RunTimeConfig.assetSelected instanceof LqAsset) {
                
                LqSprite s = new LqSprite();
                s.asset = new LqAsset((LqAsset) RunTimeConfig.assetSelected);
                
                newSprite = (LqMapObject) s;
            }
            
           if (tmpLqForm != null) {
                try {
                    newSprite = (LqMapObject) tmpLqForm.clone();
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(StageGDX.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
            
           if (RunTimeConfig.assetSelected instanceof LqBox) {
                
                LqBox s = new LqBox((LqBox) RunTimeConfig.assetSelected);
                
                newSprite = (LqMapObject) s;
            }
            
            if (RunTimeConfig.isWarpContent) {
                int w = map.SizeBlock.width;
                int h = map.SizeBlock.height;

                int i, j;

                int hW = map.yBlocks == 0 ? Gdx.graphics.getHeight() : map.yBlocks * y;
                int wW = map.xBlocks == 0 ? Gdx.graphics.getWidth()  : map.xBlocks * x;

                int fX = 0, fY = 0;

                if (mousePointer != null) {
                    LqBox rect = null;

                    for (i = 0; i <= wW; i += w) {
                        for (j = 0; j <= hW; j += h) {
                            if (mousePointer.x >= i && mousePointer.x <= (i != 0 ? 2 * i : w)) {
                                if (mousePointer.y >= j && mousePointer.y <= (j != 0 ? 2 * j : h)) {
                                    fX = i;
                                    fY = j;
                                }
                            }
                        }
                    }
                    
                    newSprite.setX(fX);
                    newSprite.setY(fY);
                    
                }
            } else {
                newSprite.setX(mousePointer.x);
                newSprite.setY(mousePointer.y);
            }

           
            map.layers.addObject(0, newSprite);
            
            
            
            if (RunTimeConfig.assetSelected instanceof LqPhysicsBox) {
                ((LqPhysicsBox) newSprite).setBody(world.createBody(((LqPhysicsBox) newSprite).getBoxBodyDef()));
                 ((LqPhysicsBox) newSprite).getBody().createFixture(((LqPhysicsBox) newSprite).getBox(), 0.0f);
            } else if (RunTimeConfig.assetSelected instanceof LqPhysicsCircle) {
                ((LqPhysicsCircle) newSprite).setBody(world.createBody(((LqPhysicsCircle) newSprite).getCircleDef()));
                ((LqPhysicsCircle) newSprite).getBody().createFixture(((LqPhysicsCircle) newSprite).getCircleFixture());
            } else if (RunTimeConfig.assetSelected instanceof LqLight) {
                ((LqLight) newSprite).alloc(handler, 1000);
            }
            
            window.updateTree();
        }
        
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int i2, int button) {
        _mouseButtonDown = -1;
        _lastMouseWorldReleasePos.set(x, y, 0);
        camera.unproject(_lastMouseWorldReleasePos);
        
        //Mouse
        if (button == 0) {
            
        }
        
        //Wheel
        if (button == 2) {
            
        }
        
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int i2) {
        if (_mouseButtonDown == Input.Buttons.MIDDLE) {
            float newX = (x - _lastMouseScreenPos.x) / (invertViewPort ? -PIXELS_PER_METER : -PIXELS_PER_METER);
            float newY = (y - _lastMouseScreenPos.y) / (invertViewPort ? -PIXELS_PER_METER : PIXELS_PER_METER);
            
            camera.translate(newX, newY);
           
            map.mainCamera.setX((int) newX);
            map.mainCamera.setY((int) newY);
        }
        
        _lastMouseWorldDragPos.set(x, y, 0);
        camera.unproject(_lastMouseWorldDragPos);
        _lastMouseWorldMovePos.set(x, y, 0);
        camera.unproject(_lastMouseWorldMovePos);
        _lastMouseScreenPos.set(x, y, 0);
        
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        _lastMouseWorldMovePos.set(x, y, 0);
        camera.unproject(_lastMouseWorldMovePos);
        _lastMouseScreenPos.set(x, y, 0);
        
        Vector3 realCoords = new Vector3(x, y, 0);
        
        camera.unproject(realCoords);
        
        mousePointer.x = (int) realCoords.x - map.mainCamera.getX();
        mousePointer.y = (int) realCoords.y - map.mainCamera.getY();
        
        
        /*if (luz != null) {
            luz.setX((int) realCoords.x);
            luz.setY((int) realCoords.y);
        }*/
        
        
        /*if (caixa != null) {
            //box.setTransform((int) realCoords.x, (int) realCoords.y, 0);
           caixa.setX((int) realCoords.x);
           caixa.setY((int) realCoords.y);
        }*/
        
        
       // luz.setPosition(realCoords.x, realCoords.y);
        
        return false;
    }

    @Override
    public boolean scrolled(int wheelRotation) {
        double antX = map.scale.getX();
        double antY = map.scale.getY();

        if (wheelRotation < 0) {
            map.scale.setLocation(antX + zoomAmount, antY + zoomAmount);
            camera.zoom += zoomAmount;
            map.scale.setLocation(antX + zoomAmount, antY + zoomAmount);
        } else {
            if (antY >= 0.1 && antX >= 0.1) {
               map.scale.setLocation(antX - zoomAmount, antY - zoomAmount);
               camera.zoom -= zoomAmount;
                map.scale.setLocation(antX - zoomAmount, antY - zoomAmount);
            }
        }
        
        camera.update();
        
        return true;
    }

    @Override
    public boolean keyDown(int i) {
        return true;
    }

    @Override
    public boolean keyUp(int i) {
         return true;
    }

    @Override
    public boolean keyTyped(char c) {
         return true;
    }
}
