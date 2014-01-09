package fh.teamproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.bullet.Bullet;

import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.SkinManager;

public class MonsterSlide extends Game {

	public static SkinManager skinManager;
	@Override
	public void create() {
		Bullet.init();
		MonsterSlide.skinManager = new SkinManager();
		setScreen(new GameScreen(this));
	}

	@Override
	public void setScreen(Screen screen) {
		// TODO Auto-generated method stub

		super.setScreen(screen);
	}
}
