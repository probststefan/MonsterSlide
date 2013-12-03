package fh.teamproject.controller.camera;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

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
			this.gameScreen.camManager.setMode(Mode.CHASE);
			return true;
		case Keys.NUM_2:
			this.gameScreen.camManager.setMode(Mode.FREE);
			return true;
		case Keys.NUM_0:
			GameScreen.isDebug = !GameScreen.isDebug;
			return true;
		}

		return false;
	}
}
