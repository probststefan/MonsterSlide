package fh.teamproject.physics.callbacks.motion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.InternalTickCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;

import fh.teamproject.game.entities.Player;
import fh.teamproject.interfaces.IWorld;

public class PlayerTickCallback extends InternalTickCallback {

	Player player;
	ClosestRayResultCallback resultCallback;
	private float checkPlayerOnSlideRayDepth = 100;
	IWorld world;

	public PlayerTickCallback(IWorld world, Player player) {
		this.player = player;
		this.world = world;
		resultCallback = new ClosestRayResultCallback(new Vector3(), new Vector3());
	}

	float threshold = 8f;
	float force = 1500f;

	@Override
	public void onInternalTick(btDynamicsWorld dynamicsWorld, float timeStep) {
		super.onInternalTick(dynamicsWorld, timeStep);
		/* Geschwindigkeit cappen */
		Vector3 velocity = this.player.rigidBody.getLinearVelocity();
		float speed = velocity.len();
		if (speed > this.player.MAX_SPEED) {
			velocity.scl(this.player.MAX_SPEED / speed);
			this.player.rigidBody.setLinearVelocity(velocity);
		}

		/*
		 * HitPoint auf der Slide berechnen und eventuell Spieler in die
		 * Richtung bewegen
		 */
		resultCallback.setCollisionObject(null);
		resultCallback.setClosestHitFraction(1f);
		Vector3 playerPos = player.getPosition();
		Vector3 down = new Vector3(0f, -1f, 0f).mul(player.getRigidBody()
				.getOrientation());
		down.scl(0f, checkPlayerOnSlideRayDepth, 0f);
		resultCallback.getRayFromWorld().setValue(playerPos.x, playerPos.y, playerPos.z);
		resultCallback.getRayToWorld().setValue(down.x, down.y, down.z);

		dynamicsWorld.rayTest(playerPos, down, resultCallback);
		btVector3 hitPointWorld = resultCallback.getHitPointWorld();
		Vector3 target = new Vector3(hitPointWorld.x(), hitPointWorld.y(),
				hitPointWorld.z());
		player.projectedPointOnSlide.set(target);
		target.sub(player.getPosition());
		float distance = target.len();
		if (distance > threshold) {
			// player.getRigidBody().applyCentralForce(target.nor().scl(1500f));
			// Gdx.app.debug("PlayerTickCallback",
			// "Pushing player onto Slide. Distance to Slide: " + distance
			// + " Force used: " + force);

		}

	}
}
