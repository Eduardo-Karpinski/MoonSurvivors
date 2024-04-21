package com.moonsurvivors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.moonsurvivors.game.enemy.EnemyManager;
import com.moonsurvivors.game.fireball.FireballManager;
import com.moonsurvivors.game.map.Map;
import com.moonsurvivors.game.screen.MainMenuScreen;
import com.moonsurvivors.game.screen.MoonSurvivors;
import com.moonsurvivors.game.util.ContactListenerImplementation;
import com.moonsurvivors.game.util.MoonSurvivorsCursor;

public class GameScreen extends ScreenAdapter {

	private OrthographicCamera camera;
    private Map map;
    private SpriteBatch gameBatch;
    private SpriteBatch hudBatch;
    private Player player;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private MoonSurvivorsCursor cursor;
    private EnemyManager enemyManager;
    private BitmapFont font;
    private AudioManager audioManager;
    
    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        map = new Map();
        map.create(world);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        camera.zoom = 0.25f;

        gameBatch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        
        font = new BitmapFont();

        player = new Player(500, 500, world);
        
        cursor = new MoonSurvivorsCursor();
        
        Gdx.graphics.setCursor(cursor.getCursor());

        world.setContactListener(new ContactListenerImplementation(player));
        
        enemyManager = new EnemyManager(world);
        
        audioManager = new AudioManager();
        
        audioManager.startMusic();
    }

    @Override
    public void render(float delta) {

    	ScreenUtils.clear(71 / 255f, 83 / 255f, 107 / 255f, 1);

        map.render(camera);

        player.update(delta);

        enemyManager.update(delta, player, world);
        FireballManager.update(delta);
        
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        gameBatch.setProjectionMatrix(camera.combined);
        
        gameBatch.begin();
        player.render(gameBatch);
        enemyManager.render(gameBatch);
        FireballManager.render(gameBatch);
        gameBatch.end();

        if (player.isDead()) {
        	MoonSurvivors game = (MoonSurvivors) Gdx.app.getApplicationListener();
        	game.setScreen(new MainMenuScreen(game));
        	this.dispose();
		}
        
        world.step(1 / 60f, 6, 2);
        debugRenderer.render(world, camera.combined);
        
        FireballManager.handleFireballs(camera, player, world, audioManager);
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            MoonSurvivors game = (MoonSurvivors) Gdx.app.getApplicationListener();
            game.setScreen(new MainMenuScreen(game));
        }
        
        drawHud();
    }

    private void drawHud() {
        hudBatch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        hudBatch.begin();
        
        String fpsText = "FPS: " + Gdx.graphics.getFramesPerSecond();
        String playerCoord = "PLAYER X: " + (int) player.getPosition().x + " Y: " + (int) player.getPosition().y;
        String enemyCount = "ENEMIES: " + enemyManager.getEnemies().size();
        String playerLife = "LIFE: " + player.getLife();
        String scoreText = "SCORE: " + player.getScore();
        
        font.setColor(Color.YELLOW);
        
        font.draw(hudBatch, fpsText, 10, Gdx.graphics.getHeight() - 10);
        font.draw(hudBatch, playerCoord, 10, Gdx.graphics.getHeight() - 25);
        font.draw(hudBatch, enemyCount, 10, Gdx.graphics.getHeight() - 40);
        font.draw(hudBatch, playerLife, 10, Gdx.graphics.getHeight() - 55);
        
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, scoreText);
        float width = layout.width;
        
        font.draw(hudBatch, scoreText, Gdx.graphics.getWidth() - width - 10, Gdx.graphics.getHeight() - 10);

        hudBatch.end();
    }

    @Override
    public void dispose() {
        map.dispose();
        gameBatch.dispose();
        hudBatch.dispose();
        font.dispose();
        player.dispose();
        cursor.dispose();
        enemyManager.dispose();
        audioManager.dispose();
    }
    
}