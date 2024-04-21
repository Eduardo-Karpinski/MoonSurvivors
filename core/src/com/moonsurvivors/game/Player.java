package com.moonsurvivors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

    private static final float SPEED = 60.0f;

    private Vector2 position;
    private Vector2 velocity;
    private String lastDirection;
    private Animation<TextureRegion> walkAnimation;
    private float stateTime;
    private Body body;
    private int life;
    private Texture lifeTexture;
    private int score;
    private boolean isDead;

    public Player(float x, float y, World world) {
    	score = 0;
        position = new Vector2(x, y);
        velocity = new Vector2();
        lastDirection = "RIGHT";

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
        body.createFixture(shape, 0.0f);
        shape.dispose();

        Texture playerTexture = new Texture("player/player.png");
        TextureRegion[][] tmp = TextureRegion.split(playerTexture, playerTexture.getWidth() / 4, playerTexture.getHeight());
        TextureRegion[] walkFrames = new TextureRegion[4];

        int index = 0;
        for (int i = 0; i < 4; i++) {
            walkFrames[index++] = tmp[0][i];
        }

        walkAnimation = new Animation<>(0.25f, walkFrames);
        stateTime = 0f;

        life = 10;
        
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        lifeTexture = new Texture(pixmap);
        pixmap.dispose();
    }
    
    public void takeDamage(int damage) {
		life -= damage;
		
		if (life <= 0) {
			isDead = true;
		}
		
	}

    public void update(float deltaTime) {
        handleInput();
        body.setLinearVelocity(velocity);
        position.set(body.getPosition().x, body.getPosition().y);

        if (velocity.x < 0) {
            lastDirection = "LEFT";
        } else if (velocity.x > 0) {
            lastDirection = "RIGHT";
        }

        stateTime += deltaTime;
    }

    private void handleInput() {
        velocity.x = 0;
        velocity.y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y = SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y = -SPEED;
        }
    }

    public void render(SpriteBatch gameBatch) {
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        if (velocity.x < 0 || lastDirection.equals("LEFT")) {
        	gameBatch.draw(currentFrame, position.x + currentFrame.getRegionWidth(), position.y, -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        } else {
        	gameBatch.draw(currentFrame, position.x, position.y);
        }

        drawHealthBar(gameBatch);
    }

    private void drawHealthBar(SpriteBatch gameBatch) {
        float barWidth = 16;
        float barHeight = 2;
        float barX = position.x;
        float barY = position.y + 17;

        gameBatch.setColor(Color.BLACK);
        gameBatch.draw(lifeTexture, barX, barY, barWidth, barHeight);

        gameBatch.setColor(Color.GREEN);
        gameBatch.draw(lifeTexture, barX, barY, barWidth * ((float) life / 10), barHeight);

        gameBatch.setColor(Color.WHITE);
    }
    
    public Vector2 getPosition() {
        return position;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void dispose() {
    	lifeTexture.dispose();
    }

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getLife() {
		return life;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	
}
