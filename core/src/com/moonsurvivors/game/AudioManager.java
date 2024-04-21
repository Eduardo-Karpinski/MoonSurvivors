package com.moonsurvivors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
	
	private Music backgroundMusic;
	private Sound fireball;
	
	public AudioManager() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
		fireball = Gdx.audio.newSound(Gdx.files.internal("audio/fireball.mp3"));
		
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(0.02f);
	}
	
	public void startMusic() {
		backgroundMusic.play();
	}
	
	public void startFireball() {
		fireball.play(0.05f);
	}
	
	public void dispose() {
		backgroundMusic.stop();
		backgroundMusic.dispose();
		fireball.dispose();
	}
	
}