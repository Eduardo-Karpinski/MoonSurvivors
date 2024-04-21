package com.moonsurvivors.game.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.moonsurvivors.game.Player;
import com.moonsurvivors.game.enemy.Enemy;
import com.moonsurvivors.game.fireball.Fireball;

public class ContactListenerImplementation implements ContactListener {

	private final Player player;

	public ContactListenerImplementation(Player player) {
		this.player = player;
	}

	@Override
	public void beginContact(Contact contact) {
	    Fixture fixtureA = contact.getFixtureA();
	    Fixture fixtureB = contact.getFixtureB();

	    Object userDataA = fixtureA.getBody().getUserData();
	    Object userDataB = fixtureB.getBody().getUserData();

	    if (userDataA instanceof Fireball && userDataB instanceof Enemy) {
	        Fireball fireball = (Fireball) userDataA;
	        if ("player".equals(fireball.getSender())) {
	            Enemy enemy = (Enemy) userDataB;
	            handleFireballHitEnemy(fireball, enemy);
	        }
	    }

	    if (userDataA instanceof Enemy && userDataB instanceof Fireball) {
	        Fireball fireball = (Fireball) userDataB;
	        if ("player".equals(fireball.getSender())) {
	            Enemy enemy = (Enemy) userDataA;
	            handleFireballHitEnemy(fireball, enemy);
	        }
	    }

	    if (userDataA instanceof Fireball && userDataB instanceof Player) {
	        Fireball fireball = (Fireball) userDataA;
	        if ("enemy".equals(fireball.getSender())) {
	            player.takeDamage(1);
	        }
	    }

	    if (userDataA instanceof Player && userDataB instanceof Fireball) {
	        Fireball fireball = (Fireball) userDataB;
	        if ("enemy".equals(fireball.getSender())) {
	            player.takeDamage(1);
	        }
	    }

	    if (userDataA instanceof Enemy && userDataB instanceof Player) {
	        player.takeDamage(1);
	    }

	    if (userDataA instanceof Player && userDataB instanceof Enemy) {
	        player.takeDamage(1);
	    }

	    Vector2 normal = contact.getWorldManifold().getNormal();
	    float speed = player.getVelocity().len();
	    Vector2 slideVelocity = new Vector2(normal).scl(speed * 0.5f);
	    fixtureA.getBody().setLinearVelocity(slideVelocity);
	    fixtureB.getBody().setLinearVelocity(slideVelocity);
	}

	private void handleFireballHitEnemy(Fireball fireball, Enemy enemy) {
	    if (enemy.getInvulnerabilityTime() <= 0) {
	        enemy.takeDamage(5);
	        Vector2 recoilDirection = new Vector2(player.getPosition().x - enemy.getPosition().x, player.getPosition().y - enemy.getPosition().y).nor();
	        float recoilSpeed = -500f;
	        enemy.setRecoilMovement(recoilDirection.scl(recoilSpeed));
	        if (enemy.isDead()) {
	            player.setScore(player.getScore() + 1);
	        }
	    }
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}