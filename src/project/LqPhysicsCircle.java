/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import java.io.Serializable;

/**
 *
 * @author GabrielBiga
 */
public class LqPhysicsCircle extends LqMapObject implements Serializable {
    public String name = "PhyCircle";
    
    private float radius;
    
    private transient BodyDef circleDef;
    private transient FixtureDef circleFixture;
    private transient CircleShape circleShape;
    
    //phy
    private float density;
    private float friction;
    private float restitution;
 
    private transient Body body;
    
    public LqPhysicsCircle(float radius) {
        this.radius = radius;
        
        reAlloc();
    }
    
    public void reAlloc() {
        circleDef = new BodyDef();
        circleDef.type = BodyType.DynamicBody;
        
        circleShape = new CircleShape();
        circleShape.setRadius(radius);
        
        circleFixture = new FixtureDef();
        circleFixture.shape = circleShape;
        circleFixture.density = density;
        circleFixture.friction = friction;
        circleFixture.restitution = restitution;
        
       // circleShape.
    }
    
    @Override
    public void setX(int x) {
        this.x = x;
        updateCirclePos();
    }
    
    @Override
    public void setY(int y) {
        this.y = y;
        updateCirclePos();
    }
    
    public void updateCirclePos() {
        circleDef.position.set(this.getX(), this.getY());
        
        if (body != null)
            body.setTransform(x, y, 0);
    }

    public BodyDef getCircleDef() {
        return circleDef;
    }

    public FixtureDef getCircleFixture() {
        return circleFixture;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        circleShape.setRadius(radius);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }
    
    
    
}
