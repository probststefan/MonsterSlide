package fh.teamproject.physics;

import com.badlogic.gdx.math.Matrix4;

import fh.teamproject.entities.Player;

public class PlayerMotionState extends MotionState {

	Player player;

	public PlayerMotionState(Player player) {
		super(player.instance.transform);
		this.player = player;

	}

	@Override
	public void getWorldTransform(final Matrix4 worldTrans) {
		super.getWorldTransform(worldTrans);
	}

	@Override
	public void setWorldTransform(final Matrix4 worldTrans) {
		super.setWorldTransform(worldTrans);
		worldTrans.getTranslation(this.player.position);
		this.player.syncWithBullet();
	}

}
