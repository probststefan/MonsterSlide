package fh.teamproject.controller.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.game.entities.Player;

/**
 * Diese Kamera rotiert zu Beginn um den Spieler.
 * 
 * @author stefanprobst
 * 
 */
public class CountdownCameraController extends CameraController {

	private Player target;

	public CountdownCameraController(Camera camera, Player player) {
		super(camera);
		this.target = player;
		this.camera.position.set(this.target.getPosition());
		this.camera.lookAt(this.target.getPosition());
		this.camera.translate(0, 5.0f, 0);
		// this.camera.position.sub(1.0f);
		// this.camera.up.set(new Vector3(0, 1, 0));
		getCamera().far = 500f;
		this.camera.update(true);
	}

	public void update() {
		this.camera.lookAt(target.position);
		this.camera.rotateAround(target.position, Vector3.Y, 2.0f);
		// this.camera.position.add(0, 0.01f, 0);
		this.camera.update(true);
	}
}