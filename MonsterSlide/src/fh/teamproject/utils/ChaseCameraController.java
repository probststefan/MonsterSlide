package fh.teamproject.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.entities.Player;

public class ChaseCameraController {

	Player player;
	Camera camera;
	float radius = 5;

	Vector3 offset;

	public ChaseCameraController(Camera camera, Player player) {
		this.player = player;
		this.camera = camera;
		Vector3 pos = player.getPosition().cpy();
		this.offset = player.direction.cpy().scl(-1f * this.radius);
		// new Vector3(0, this.radius, -this.radius);

		Vector3 orbitPos = pos.cpy().add(this.offset);
		Vector3 delta = orbitPos.cpy().sub(camera.position.cpy());
		this.camera.translate(delta);
		camera.rotateAround(pos, Vector3.X, 45f);
		this.camera.lookAt(pos);
	}

	public void update() {
		Vector3 pos = this.player.getPosition().cpy();

		Vector3 orbitPos = pos.cpy().add(this.offset);
		Vector3 delta = orbitPos.cpy().sub(this.camera.position.cpy());

		this.camera.translate(delta);
		this.camera.rotateAround(pos, Vector3.X, 45f);
		this.camera.lookAt(pos);
	}

}
