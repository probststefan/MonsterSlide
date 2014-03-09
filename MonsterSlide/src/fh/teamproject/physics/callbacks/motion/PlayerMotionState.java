package fh.teamproject.physics.callbacks.motion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;

import fh.teamproject.game.entities.Player;
import fh.teamproject.interfaces.IWorld;

public class PlayerMotionState extends MotionState {

	Player player;
	IWorld world;

	public PlayerMotionState(IWorld world, Player player) {
		super(player.getModelInstance().transform);
		this.world = world;
		this.player = player;
	}

	@Override
	public void getWorldTransform(final Matrix4 worldTrans) {
		super.getWorldTransform(worldTrans);
	}

	@Override
	public void setWorldTransform(final Matrix4 worldTrans) {
		super.setWorldTransform(worldTrans);
		this.player.syncWithBullet();


	}
}
