package fh.teamproject.controller.camera;

import com.badlogic.gdx.graphics.Camera;

import fh.teamproject.interfaces.ICameraController;

public abstract class CameraController implements ICameraController {

	protected Camera camera;
	boolean autoupdate = true;

	public CameraController(Camera camera) {
		this.camera = camera;
	}

	@Override
	public void update() {
		if (this.autoupdate) {
			this.camera.update();
		}
	}

	@Override
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	@Override
	public Camera getCamera() {
		return this.camera;
	}

}
