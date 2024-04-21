package com.moonsurvivors.game.fireball;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Fireball {
    
    private Vector2 position;
    private Vector2 direction;
    private float speed;
    private float distanceTraveled;
    private boolean active;
    private Animation<TextureRegion> animation;
    private float deltaTime;
    private Body body;
    private String sender;

    public Fireball(Vector2 position, Vector2 target, World world, String color, String sender) {
        this.position = new Vector2(position);
        this.direction = new Vector2(target).sub(position).nor();
        this.speed = 150;
        this.distanceTraveled = 0;
        this.active = true;
        this.sender = sender;
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
        body = world.createBody(bodyDef);
        body.setUserData(this);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(9.0f); // Raio da fireball

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();
        
        Texture fireballTexture = new Texture("sprites/fireball_"+color+".png");
        TextureRegion[][] tmp = TextureRegion.split(fireballTexture, fireballTexture.getWidth() / 4, fireballTexture.getHeight());
        TextureRegion[] frames = new TextureRegion[4];
        
        int index = 0;
        for (int i = 0; i < 4; i++) {
            frames[index++] = tmp[0][i];
        }
        
        animation = new Animation<>(0.12f, frames);
    }

    public void update(float deltaTime) {
    	this.deltaTime += deltaTime;
        if (active) {
            position.add(direction.x * speed * deltaTime, direction.y * speed * deltaTime);
            float offsetX = 8.0f;
            float offsetY = 8.0f;
            body.setTransform(position.x + offsetX, position.y + offsetY, 0);
            distanceTraveled += speed * deltaTime;
            if (distanceTraveled > 150) {
                active = false;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (active) {
        	TextureRegion currentFrame = animation.getKeyFrame(deltaTime, true);
        	batch.draw(currentFrame, position.x, position.y);
        }
    }

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }
    
    public void dispose() {
        body.getWorld().destroyBody(body);
    }
    
    public String getSender() {
		return sender;
	}
}
