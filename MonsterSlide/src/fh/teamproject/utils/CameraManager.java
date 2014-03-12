package fh.teamproject.utils;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;

import fh.teamproject.controller.camera.ChaseCameraController;
import fh.teamproject.controller.camera.CountdownCameraController;
import fh.teamproject.controller.camera.SmoothChaseCameraController;
import fh.teamproject.game.entities.Player;
import fh.teamproject.interfaces.ICameraController;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.debug.DebugCameraController;

public class CameraManager implements Disposable {
	public enum Mode {
		CHASE, FREE, SMOOTH, COUNTDOWN
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
		SmoothChaseCameraController smoothCamContr = new SmoothChaseCameraController(
				new PerspectiveCamera(67, Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight()), (Player) gameScreen.getWorld()
						.getPlayer());
		CountdownCameraController countdownCamContr = new CountdownCameraController(
				new PerspectiveCamera(67, Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight()), (Player) gameScreen.getWorld()
						.getPlayer());
		addCamera(debugCamera, Mode.FREE);
		addCamera(chaseCamContr, Mode.CHASE);
		addCamera(smoothCamContr, Mode.SMOOTH);
		addCamera(countdownCamContr, Mode.COUNTDOWN);
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
		if (mode == Mode.FREE && activeCamera != null) {
			Camera camera = cameras.get(mode).getCamera();
			camera.position.set(activeCamera.getCamera().position);
			camera.direction.set(1f, 0f, 0f);
			camera.up.set(0f, 1f, 0f);
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