package com.moonsurvivors.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;

public class MoonSurvivorsCursor {
	
	private final Cursor customCursor;
	
	public MoonSurvivorsCursor() {
		Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("ui/cursor.png"));
        int pixmapWidth = MathUtils.nextPowerOfTwo(cursorPixmap.getWidth());
        int pixmapHeight = MathUtils.nextPowerOfTwo(cursorPixmap.getHeight());
        Pixmap pixmap = new Pixmap(pixmapWidth, pixmapHeight, Pixmap.Format.RGBA8888);
        pixmap.drawPixmap(cursorPixmap, 0, 0);
        customCursor = Gdx.graphics.newCursor(pixmap, 0, 0);
	}
	
	public Cursor getCursor() {
		return customCursor;
	}
	
	public void dispose() {
		customCursor.dispose();
	}
	
}