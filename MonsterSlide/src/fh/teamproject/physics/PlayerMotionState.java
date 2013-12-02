package fh.teamproject.physics;

import com.badlogic.gdx.math.Matrix4;

import fh.teamproject.entities.MotionState;
import fh.teamproject.entities.Player;

public class PlayerMotionState extends MotionState {

	Player player;

	public PlayerMotionState(Player player) {
		super(player.instance.transform);
		this.player = player;

	}

	@Override
	public void getWorldTransform(Matrix4 worldTrans) {
		super.getWorldTransform(worldTrans);
	}

	@Override
	public void setWorldTransform(Matrix4 worldTrans) {
		super.setWorldTransform(worldTrans);
		this.player.syncWithBullet();
	}


}
