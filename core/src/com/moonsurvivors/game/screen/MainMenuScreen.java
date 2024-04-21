package com.moonsurvivors.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.moonsurvivors.game.GameScreen;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Image background;
    private float backgroundSpeedX = 25;
    private float backgroundMaxOffsetX = 20;
    private boolean moveRight = true;

    public MainMenuScreen(MoonSurvivors game) {
        this.stage = new Stage();

        Texture backgroundImage = new Texture(Gdx.files.internal("ui/background.png"));
        
        background = new Image(backgroundImage);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(background);

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.setFillParent(true);
        verticalGroup.space(10);
        verticalGroup.align(Align.center);
        stage.addActor(verticalGroup);

        TextButton playButton = new TextButton("Iniciar", game.getSkin());
        TextButton configButton = new TextButton("Ajustes", game.getSkin());
        TextButton exitButton = new TextButton("Sair", game.getSkin());
        
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen());
            }
        });
        
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        verticalGroup.addActor(playButton);
        verticalGroup.addActor(configButton);
        verticalGroup.addActor(exitButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (moveRight) {
            background.moveBy(backgroundSpeedX * delta, 0);
            if (background.getX() >= backgroundMaxOffsetX) {
                moveRight = false;
            }
        } else {
            background.moveBy(-backgroundSpeedX * delta, 0);
            if (background.getX() <= -backgroundMaxOffsetX) {
                moveRight = true;
            }
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
