package fh.teamproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.bullet.Bullet;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.utils.SkinManager;

public class MonsterSlide extends Game {

	public static SkinManager skinManager;
	public AssetManager assets;

	@Override
	public void create() {
		assets = new AssetManager();
		Bullet.init();
		MonsterSlide.skinManager = new SkinManager();
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		setScreen(new MenuScreen(this));
	}

	public AssetManager getAssets() {
		return assets;
	}

}
