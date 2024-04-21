package com.moonsurvivors.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Map {
    
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private MapObjects mapCollisions;
    private Array<Rectangle> collisionObjects = new Array<>();
    
    public void create(World world) {
        map = new TmxMapLoader().load("maps/map_01.tmx");
        mapCollisions = map.getLayers().get("collision").getObjects();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

        for (MapObject object : mapCollisions) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                collisionObjects.add(rect);

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rect.x + rect.width / 2, rect.y + rect.height / 2);
                Body body = world.createBody(bodyDef);

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rect.width / 2, rect.height / 2);
                body.createFixture(shape, 0f);
                shape.dispose();
            }
        }
    }

    public void render(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void dispose() {
        map.dispose();
    }
}
