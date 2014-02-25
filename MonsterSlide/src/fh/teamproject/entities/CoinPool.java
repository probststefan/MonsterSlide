package fh.teamproject.entities;

import com.badlogic.gdx.utils.Pool;

import fh.teamproject.screens.GameScreen;

public class CoinPool extends Pool<Coin> {

	GameScreen screen;

	public CoinPool(GameScreen screen) {
		this.screen = screen;
	}
	@Override
	protected Coin newObject() {
		Coin coin = new Coin(screen);
		return coin;
	}
}
