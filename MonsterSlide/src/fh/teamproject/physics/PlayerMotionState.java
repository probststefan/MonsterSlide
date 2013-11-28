package fh.teamproject.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

import fh.teamproject.entities.Player;

public class PlayerMotionState extends btDefaultMotionState {

	Player player;

	public PlayerMotionState(Player player) {
		this.player = player;

	}

	@Override
	public void getWorldTransform(Matrix4 worldTrans) {

		worldTrans.set(this.player.instance.transform);
	}

	@Override
	public void setWorldTransform(Matrix4 worldTrans) {
		super.setWorldTransform(worldTrans);
		Vector3 oldPos = new Vector3();
		this.player.instance.transform.getTranslation(oldPos);
		Vector3 newPos = new Vector3();
		worldTrans.getTranslation(newPos);
		this.player.instance.transform.set(worldTrans);
		this.player.direction = oldPos.sub(newPos);

	}


}
