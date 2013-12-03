package fh.teamproject.utils.debug;

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
		case Keys.P:
			GameScreen.isDebug = !GameScreen.isDebug;
			// Muss gesetzt werden sonst schreibt man weiter in Textfelder.. :/
			DebugInfoPanel.stage.setKeyboardFocus(null);
			return true;
		}

		return false;
	}
}
