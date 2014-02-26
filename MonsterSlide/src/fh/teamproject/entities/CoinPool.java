package fh.teamproject.entities;

import com.badlogic.gdx.utils.Pool;

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
