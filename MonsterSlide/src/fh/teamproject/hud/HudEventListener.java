package fh.teamproject.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fh.teamproject.screens.GameScreen;

public class HudEventListener extends ChangeListener {

	GameScreen gameScreen;

	public HudEventListener(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		if (actor.getName().equals("reset")) {
			this.gameScreen.world.reset();
		}
	}
}
