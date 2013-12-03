package fh.teamproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.bullet.Bullet;

import fh.teamproject.screens.GameScreen;
import fh.teamproject.screens.MenuScreen;

public class MonsterSlide extends Game {

	@Override
	public void create() {
		Bullet.init();
		this.setScreen(new GameScreen());
	}

}
