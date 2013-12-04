package fh.teamproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.bullet.Bullet;

import fh.teamproject.screens.GameScreen;
import fh.teamproject.screens.MenuScreen;
import fh.teamproject.utils.SkinManager;

public class MonsterSlide extends Game {

    public static SkinManager skinManager;
	@Override
	public void create() {
		Bullet.init();
		skinManager = new SkinManager();
		this.setScreen(new MenuScreen(this));
	}

}
