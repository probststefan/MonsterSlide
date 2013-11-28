package fh.teamproject.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.ObjectMap;

import fh.teamproject.interfaces.ICameraController;

public class CameraManager {
	public enum Mode {
		CHASE, FREE
	};

	ObjectMap<Mode, ICameraController> cameras = new ObjectMap<CameraManager.Mode, ICameraController>();

	ICameraController activeCamera;

	public CameraManager() {
	}

	public void update() {
		this.activeCamera.update();
	}

	public void addCamera(ICameraController controller, Mode mode) {
		this.cameras.put(mode, controller);
	}

	public void setMode(Mode mode) {
		this.activeCamera = this.cameras.get(mode);
	}

	public Camera getActiveCamera() {
		return this.activeCamera.getCamera();
	}

	public void setViewport(float width, float height) {
		for (ICameraController c : this.cameras.values()) {
			c.getCamera().viewportWidth = width;
			c.getCamera().viewportHeight = height;
			c.getCamera().update();
		}
	}
}