package fh.teamproject.game;

import com.badlogic.gdx.utils.Pool;

import fh.teamproject.game.entities.Coin;

public class CoinPool extends Pool<Coin> {

	private World world;

	public CoinPool(World world) {
		this.world = world;
	}

	@Override
	protected Coin newObject() {
		return new Coin(this.world);
	}
}
