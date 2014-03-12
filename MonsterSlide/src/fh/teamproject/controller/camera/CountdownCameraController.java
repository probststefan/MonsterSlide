package fh.teamproject.controller.camera;

import com.badlogic.gdx.Gdx;
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
	float angle = 180.0f;

	public CountdownCameraController(Camera camera, Player player) {
		super(camera);
		this.target = player;
		this.camera.position.set(this.target.getPosition());
		this.camera.translate(15f, 15.0f, 0);
		this.camera.lookAt(this.target.getPosition());
		getCamera().far = 500f;
		this.camera.update(true);
	}

	public void update() {
		this.camera.lookAt(target.getPosition());
		this.camera.rotateAround(target.getPosition(), Vector3.Y,
				angle * Gdx.graphics.getDeltaTime());
		this.camera.update(true);
		System.out.println(camera.position);
	}
}