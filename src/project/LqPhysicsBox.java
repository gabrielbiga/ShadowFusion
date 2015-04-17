/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.io.Serializable;

/**
 *
 * @author GabrielBiga
 */
public class LqPhysicsBox extends LqMapObject implements Serializable {
    private static final long serialVersionUID = -623206548866771896L;

    public String name = "PhyBox";
    
    private int width;
    private int height;
    
    private transient BodyDef boxBodyDef;
    private transient PolygonShape box;
    
    private transient Body body;
    
    public LqPhysicsBox() {
        reAlloc();
    }
    
    public void destroyEx() {
        World parentWorld = body.getWorld();
        destroy();
        
        reAlloc();
        updateBox();
        parentWorld.createBody(boxBodyDef).createFixture(box, 0.0f);
    }
    
    public void destroy() {
        World parentWorld = body.getWorld();
        parentWorld.destroyBody(getBody());
    }
    
    public void reAlloc() {
        boxBodyDef = new BodyDef();        
        box = new PolygonShape();
    }
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
         updateBox();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
         updateBox();
    }
    
     @Override
    public void setX(int x) {
        this.x = x;
        updateBox();
    }
    
    @Override
    public void setY(int y) {
        this.y = y;
        updateBox();
    }
    
    public void updateBox() {
        box.setAsBox(this.width, this.height);
        
        boxBodyDef.position.set(this.x, this.y);
        
        if (body != null)
            body.setTransform(x, y, 0);
    }

    public BodyDef getBoxBodyDef() {
        return boxBodyDef;
    }

    public PolygonShape getBox() {
        return box;
    }   

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
    
    
}
