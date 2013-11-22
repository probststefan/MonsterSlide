package fh.teamproject.controller.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.entities.Player;

public class ChaseCameraController extends CameraController {

	private final Player player;
	private final float radius = 5;
	private final Vector3 offset;

	public ChaseCameraController(Camera camera, Player player) {
		super(camera);
		this.player = player;
		this.offset = player.direction.cpy().scl(-1f * this.radius);
		// Vector3 rotAxis = player.direction.cpy().crs(vector)
		this.offset.rotate(Vector3.X, 45);
		Vector3 pos = player.getPosition();
		Vector3 orbitPos = pos.cpy().add(this.offset);
		// orbitPos.mul(player.getModelInstance().transform);
		// orbitPos.rotate(axis, degrees)
		camera.position.set(orbitPos);
		camera.lookAt(pos);

	}

	Vector3 oldPos = new Vector3();
	@Override
	public void update() {
		Vector3 pos = this.player.getPosition().cpy();
		this.camera.position.set(pos.cpy().add(this.offset));
		this.camera.lookAt(pos.cpy().add(this.player.direction));
		super.update();
	}
}
