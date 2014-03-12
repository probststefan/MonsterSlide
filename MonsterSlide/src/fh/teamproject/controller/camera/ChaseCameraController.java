package fh.teamproject.controller.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.game.entities.Player;

public class ChaseCameraController extends CameraController {

	private final Player player;
	private float radius = 10;
	private Vector3 offset;

	public ChaseCameraController(Camera camera, Player player) {
		super(camera);
		this.player = player;
		camera.up.set(0, 1, 0);
		offset = player.direction.cpy().scl(-1f * radius);
		Vector3 rotAxis = player.direction.cpy().crs(Vector3.Y);
		Vector3 pos = player.getPosition();
		Vector3 orbitPos = pos.cpy().add(offset);
		camera.position.set(orbitPos);
		camera.rotateAround(pos, rotAxis, 315);
		camera.lookAt(pos.cpy().add(player.direction));

	}

	@Override
	public void update() {
		camera.up.set(0, 1, 0);
		offset = player.direction.cpy().scl(-1f * radius);
		Vector3 rotAxis = player.direction.cpy().crs(Vector3.Y);
		Vector3 pos = player.getPosition();
		Vector3 orbitPos = pos.cpy().add(offset);
		camera.position.set(orbitPos);
		camera.rotateAround(pos, rotAxis, 315);
		camera.lookAt(pos.cpy().add(player.direction.cpy().scl(3f)));
		super.update();

	}

}
