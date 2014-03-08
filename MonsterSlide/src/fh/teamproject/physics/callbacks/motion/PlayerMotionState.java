package fh.teamproject.physics.callbacks.motion;

import com.badlogic.gdx.math.Matrix4;

import fh.teamproject.game.entities.Player;

public class PlayerMotionState extends MotionState {

	Player player;

	public PlayerMotionState(Player player) {
		super(player.getModelInstance().transform);
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
