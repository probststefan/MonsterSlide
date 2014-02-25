package fh.teamproject.utils;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

import fh.teamproject.controller.camera.ChaseCameraController;
import fh.teamproject.entities.Player;
import fh.teamproject.interfaces.ICameraController;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.debug.DebugCameraController;

public class CameraManager implements Disposable {
	public enum Mode {
		CHASE, FREE
	};

	GameScreen gameScreen;
	ObjectMap<Mode, ICameraController> cameras = new ObjectMap<CameraManager.Mode, ICameraController>();

	public ICameraController activeCamera;

	public CameraManager(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		DebugCameraController debugCamera = new DebugCameraController(
				new PerspectiveCamera(67, Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight()));
		debugCamera.getCamera().far = 1000f;
		ChaseCameraController chaseCamContr = new ChaseCameraController(
				new PerspectiveCamera(67, Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight()), (Player) gameScreen.getWorld()
						.getPlayer());
		addCamera(debugCamera, Mode.FREE);
		addCamera(chaseCamContr, Mode.CHASE);
	}

	public void update() {
		// this.activeCamera.update();
		for (ICameraController c : cameras.values()) {
			c.update();
		}
	}

	public void addCamera(ICameraController controller, Mode mode) {
		cameras.put(mode, controller);
	}

	public void setMode(Mode mode) {
		if (mode == Mode.FREE) {
			// Camera original = this.activeCamera.getCamera();
			// this.activeCamera = this.cameras.get(mode);
			// Camera freeCam = this.activeCamera.getCamera();
			// // freeCam.direction.set(original.direction);
			// // freeCam.up.set(original.up);
			// freeCam.position.set(original.position);
			// // freeCam.lookAt(this.gameScreen.player.position);
			// // freeCam.near = original.near;
			// // freeCam.far = original.far;
			// // freeCam.view.set(original.view);
			// // freeCam.projection.set(original.projection);
			// this.activeCamera.getCamera().update(true);
		} else {
		}
		activeCamera = cameras.get(mode);
	}

	public Camera getActiveCamera() {
		return activeCamera.getCamera();
	}

	public ICameraController getController(Mode mode) {

		return cameras.get(mode);
	}

	public void setViewport(float width, float height) {
		for (ICameraController c : cameras.values()) {
			c.getCamera().viewportWidth = width;
			c.getCamera().viewportHeight = height;
			c.getCamera().update();
		}
	}

	public Values<ICameraController> getController() {
		return cameras.values();
	}

	public LinkedList<Camera> getCameras() {
		LinkedList<Camera> cameras = new LinkedList<Camera>();
		for (ICameraController c : this.cameras.values()) {
			cameras.add(c.getCamera());
		}
		return cameras;
	}

	@Override
	public void dispose() {
		gameScreen = null;
		cameras = null;
		activeCamera = null;
	}
}