package com.moonsurvivors.game.enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.moonsurvivors.game.Player;

public class EnemyManager {

    private List<Enemy> enemies;
    private Vector2[] spawnPoints = {
        new Vector2(384, 544),
        new Vector2(576, 544),
        new Vector2(304, 416),
        new Vector2(480, 352),
        new Vector2(256, 288)
    };
    private Map<Vector2, Float> spawnTimers;

    public EnemyManager(World world) {
        this.enemies = new ArrayList<>();
        this.spawnTimers = new HashMap<>();
        
        Enemy enemy1 = new Enemy(384, 544, world);
        Enemy enemy2 = new Enemy(576, 544, world);
        Enemy enemy3 = new Enemy(304, 416, world);
        Enemy enemy4 = new Enemy(480, 352, world);
        Enemy enemy5 = new Enemy(256, 288, world);
        
        enemies.add(enemy1);
        enemies.add(enemy2);
        enemies.add(enemy3);
        enemies.add(enemy4);
        enemies.add(enemy5);
        
        scheduleSpawnForAllPoints(world);
    }

    private void scheduleSpawnForAllPoints(World world) {
        for (Vector2 spawnPoint : spawnPoints) {
            scheduleSpawnForPoint(spawnPoint, world);
        }
    }

    private void scheduleSpawnForPoint(Vector2 spawnPoint, World world) {
        float delay = MathUtils.random(2f, 5f);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnEnemyAtPoint(spawnPoint, world);
                scheduleSpawnForPoint(spawnPoint, world); 
            }
        }, delay);

        spawnTimers.put(spawnPoint, delay);
    }

    private void spawnEnemyAtPoint(Vector2 spawnPoint, World world) {
        Enemy enemy = new Enemy(spawnPoint.x, spawnPoint.y, world);
        enemies.add(enemy);
    }
    
    public List<Enemy> getEnemies() {
		return enemies;
	}

    public void update(float delta, Player player, World world) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.isDead()) {
                iterator.remove();
                enemy.dispose();
            } else {
            	enemy.update(delta);
                enemy.followPlayer(player);
                enemy.avoidCollision(enemies);
                enemy.attack(player, world);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
    }
    
    public void dispose() {
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        enemies.clear();
    }
}
