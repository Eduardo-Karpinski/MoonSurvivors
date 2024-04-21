package com.moonsurvivors.game.fireball;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.moonsurvivors.game.AudioManager;
import com.moonsurvivors.game.Player;

public class FireballManager {

    private static List<Fireball> activeFireballs = new ArrayList<>();
    private static final float FIREBALL_COOLDOWN = 1.0f;
    private static float timeSinceLastFireball = FIREBALL_COOLDOWN + 1;;
    
    public static void handleFireballs(Camera camera, Player player, World world, AudioManager audioManeger) {
    	if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && canShootFireball()) {
            Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(clickCoordinates);
            Vector2 clickPosition = new Vector2(clickCoordinates.x, clickCoordinates.y);
            activeFireballs.add(new Fireball(player.getPosition(), clickPosition, world, "red", "player"));
            timeSinceLastFireball = 0;
            audioManeger.startFireball();
        }
	}
    
    public static void addFireball(Vector2 startPosition, Vector2 targetPosition, World world) {
        activeFireballs.add(new Fireball(startPosition, targetPosition, world, "blue", "enemy"));
    }
    
    public static void update(float deltaTime) {
    	timeSinceLastFireball += deltaTime;
        Iterator<Fireball> iterator = activeFireballs.iterator();
        while (iterator.hasNext()) {
            Fireball fireball = iterator.next();
            fireball.update(deltaTime);
            if (!fireball.isActive()) {
                iterator.remove();
                fireball.dispose();
            }
        }
    }
    
    public static boolean canShootFireball() {
        return timeSinceLastFireball >= FIREBALL_COOLDOWN;
    }

    public static void render(SpriteBatch batch) {
        for (Fireball fireball : activeFireballs) {
            fireball.render(batch);
        }
    }
}