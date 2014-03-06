package fh.teamproject.physics.callbacks;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.InternalTickCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;

import fh.teamproject.entities.Player;

public class PlayerTickCallback extends InternalTickCallback {

	Player player;
	ClosestRayResultCallback resultCallback;
	private float checkPlayerOnSlideRayDepth = 100;

	public PlayerTickCallback(Player player) {
		resultCallback = new ClosestRayResultCallback(player.getPosition(), new Vector3(
				player.getPosition().x, player.getPosition().y
						- this.checkPlayerOnSlideRayDepth, player.getPosition().z));
		this.player = player;
	}

	@Override
	public void onInternalTick(btDynamicsWorld dynamicsWorld, float timeStep) {
		super.onInternalTick(dynamicsWorld, timeStep);

		Vector3 velocity = this.player.rigidBody.getLinearVelocity();
		float speed = velocity.len();
		if (speed > this.player.MAX_SPEED) {
			velocity.scl(this.player.MAX_SPEED / speed);
			this.player.rigidBody.setLinearVelocity(velocity);
		}
		resultCallback.setCollisionObject(null);
		resultCallback.setClosestHitFraction(1f);
		Vector3 playerPos = player.getPosition();
		resultCallback.getRayFromWorld().setValue(playerPos.x, playerPos.y, playerPos.z);
		resultCallback.getRayToWorld().setValue(playerPos.x,
				playerPos.y - checkPlayerOnSlideRayDepth, playerPos.z);

		dynamicsWorld.rayTest(playerPos, new Vector3(playerPos.x, playerPos.y
				- checkPlayerOnSlideRayDepth, playerPos.z),
				resultCallback);
		btVector3 hitPointWorld = resultCallback.getHitPointWorld();
		Vector3 target = new Vector3(hitPointWorld.x(), hitPointWorld.y(),
				hitPointWorld.z());
		target.sub(player.getPosition());
		if (target.len() > 1f) {
			player.getRigidBody().applyCentralForce(target.scl(1500f));
		}
	}
}
