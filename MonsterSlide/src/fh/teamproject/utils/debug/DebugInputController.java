package fh.teamproject.utils.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.CameraManager.Mode;

public class DebugInputController extends InputAdapter {

	GameScreen gameScreen;

	public DebugInputController(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.NUM_1:
			GameScreen.camManager.setMode(Mode.CHASE);
			return true;
		case Keys.NUM_2:
			GameScreen.camManager.setMode(Mode.FREE);
			return true;
		case Keys.P:
			gameScreen.debugDrawer.toggleDebug();
			// Muss gesetzt werden sonst schreibt man weiter in Textfelder.. :/
			DebugInfoPanel.stage.setKeyboardFocus(null);
			return true;
		case Keys.ESCAPE:
			gameScreen.hud.togglePauseMenu();
			return true;
		case Keys.F:
			gameScreen.isPaused = !gameScreen.isPaused;
			return true;
		case Keys.G:
			gameScreen.player.getRigidBody().activate(true);
			return true;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			Ray ray = GameScreen.camManager.getActiveCamera()
					.getPickRay(screenX, screenY);
			Vector3 intersection = new Vector3();
			new Vector3();
			boolean isIntersecting = Intersector.intersectRayPlane(ray, new Plane(
					Vector3.Y, 0f), intersection);
			if (isIntersecting) {
				intersection.add(0f, 2f, 0f);
				// Matrix4 transform =
				// gameScreen.player.getModelInstance().transform;
				// transform.setToTranslation(intersection);
				// gameScreen.player.getRigidBody().setWorldTransform(transform);
				gameScreen.player.resetAt(intersection);
				Gdx.app.log("DebugInput", "set to" + intersection);

			}
		}
		return super.touchUp(screenX, screenY, pointer, button);
	}
}
