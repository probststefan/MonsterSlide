package fh.teamproject.utils.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;

import fh.teamproject.game.entities.Player;
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
		case Keys.NUM_3:
			GameScreen.camManager.setMode(Mode.SMOOTH);
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
			gameScreen.getWorld().getPlayer().getRigidBody().activate(true);
			return true;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			PerspectiveCamera camera = (PerspectiveCamera) GameScreen.camManager
					.getActiveCamera();
			Vector3 direction = new Vector3(screenX, screenY, 0f);
			camera.unproject(direction);
			((Player) gameScreen.getWorld().getPlayer()).resetAt(direction);
		}
		return super.touchUp(screenX, screenY, pointer, button);
	}
}
