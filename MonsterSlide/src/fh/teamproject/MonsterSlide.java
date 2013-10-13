package fh.teamproject;

import com.badlogic.gdx.Game;

import fh.teamproject.screens.GameScreen;

public class MonsterSlide extends Game {

	@Override
	public void create() {
		this.setScreen(new GameScreen());
	}

}
