package com.moonsurvivors.game.enemy;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.moonsurvivors.game.Player;
import com.moonsurvivors.game.fireball.FireballManager;

public class Enemy {

    private Vector2 position;
    private Sprite sprite;
    private Texture enemyTexture;
    private TextureRegion[] frames;
    private int currentFrame;
    private float stateTime;
    private float speed;
    private Body body;
    private World world;
    private int life;
    private boolean isDead;
    private float invulnerabilityTime = 0;
    private static final float INVULNERABILITY_DURATION = 0.5f;
    private Vector2 recoilMovement = new Vector2();
    private static final float ATTACK_COOLDOWN = 5.0f;
    private float attackCooldown = ATTACK_COOLDOWN + 1;
    
    public Enemy(float x, float y, World world) {
    	life = 6;
    	this.isDead = false;
    	this.world = world;
        this.position = new Vector2(x, y);
        this.enemyTexture = new Texture("enemy/enemy.png");
        this.speed = 0.5f;
        
        TextureRegion[][] tmp = TextureRegion.split(enemyTexture, enemyTexture.getWidth() / 4, enemyTexture.getHeight());
        frames = new TextureRegion[4];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            frames[index++] = tmp[0][i];
        }

        sprite = new Sprite(frames[0]);
        sprite.setPosition(x, y);

        currentFrame = 0;
        stateTime = 0f;
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        
        PolygonShape shape = new PolygonShape();
        float offsetX = 8.0f;
        float offsetY = 8.0f;
        shape.setAsBox(15.0f - offsetX, 15.0f - offsetY, new Vector2(offsetX, offsetY), 0);
        body.createFixture(shape, 1.0f);
        shape.dispose();
    }
    
    public void takeDamage(int damage) {
        if (invulnerabilityTime <= 0) {
            life -= damage;
            if (life <= 0) {
                isDead = true;
            }
            invulnerabilityTime = INVULNERABILITY_DURATION;
        }
    }
    
    public void attack(Player player, World world) {
        if (attackCooldown <= 0) {
            Vector2 direction = new Vector2(player.getPosition().x - position.x, player.getPosition().y - position.y).nor();
            FireballManager.addFireball(position.cpy().add(direction.scl(20)), player.getPosition(), world);
            attackCooldown = ATTACK_COOLDOWN; // Reseta o cooldown
        }
    }

    public void update(float delta) {
    	
    	if (invulnerabilityTime > 0) {
            invulnerabilityTime -= delta;
        }
    	
    	if (attackCooldown > 0) {
            attackCooldown -= delta;
        }
        
        position.add(recoilMovement.x * delta, recoilMovement.y * delta);
        recoilMovement.scl(0.9f);
    	
    	if (isDead) {
            world.destroyBody(body);
            isDead = true;
        }
    	
    	stateTime += delta;
        updateAnimation(delta);
    }

    private void updateAnimation(float delta) {
        if (stateTime >= 0.25f) {
            currentFrame = (currentFrame + 1) % 4;
            sprite.setRegion(frames[currentFrame]);
            stateTime = 0f;
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void followPlayer(Player player) {
        Vector2 direction = new Vector2(player.getPosition().x - position.x, player.getPosition().y - position.y).nor();
        position.x += direction.x * speed;
        position.y += direction.y * speed;
        body.setTransform(position, 0);
        sprite.setPosition(position.x, position.y);
    }
    
    public void avoidCollision(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy != this) {
                Vector2 directionToEnemy = new Vector2(enemy.getPosition().x - position.x, enemy.getPosition().y - position.y).nor();
                float distanceToEnemy = position.dst(enemy.getPosition());
                float safeDistance = 16.0f;

                if (distanceToEnemy < safeDistance) {
                    position.x -= directionToEnemy.x * speed;
                    position.y -= directionToEnemy.y * speed;
                    body.setTransform(position, 0);
                    sprite.setPosition(position.x, position.y);
                }
            }
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.sprite.setPosition(position.x, position.y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    
    public void dispose() {
    	enemyTexture.dispose();
    	world.destroyBody(body);
	}

	public boolean isDead() {
		return isDead;
	}
	
	public float getInvulnerabilityTime() {
		return invulnerabilityTime;
	}
	
	public void setRecoilMovement(Vector2 recoil) {
	    this.recoilMovement.set(recoil);
	}

	public Vector2 getRecoilMovement() {
	    return recoilMovement;
	}
    
}
