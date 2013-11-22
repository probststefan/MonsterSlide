package fh.teamproject.controller.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.entities.Player;

public class ChaseCameraController extends CameraController {

	private final Player player;
	private float radius = 5;
	private Vector3 offset;

	public ChaseCameraController(Camera camera, Player player) {
		super(camera);
		this.player = player;
	}

	@Override
	public void update() {
		this.offset = this.player.direction.cpy().scl(-1f * this.radius);
		Vector3 rotAxis = this.player.direction.cpy().crs(Vector3.Y);
		Vector3 pos = this.player.getPosition();
		Vector3 orbitPos = pos.cpy().add(this.offset);
		this.camera.position.set(orbitPos);
		this.camera.rotateAround(pos, rotAxis, 315);
		this.camera.lookAt(pos);
		super.update();
	}

}
