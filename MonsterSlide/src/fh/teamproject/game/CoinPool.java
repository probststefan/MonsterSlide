package fh.teamproject.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.game.entities.Coin;

public class CoinPool extends Pool<Coin> {

	private World world;

	public CoinPool(World world) {
		this.world = world;
	}

	public CoinPool(World world, int initialCapacity, int max) {
		super(initialCapacity, max);
		this.world = world;
	}

	public CoinPool(World world, int initialCapacity) {
		super(initialCapacity);
		this.world = world;
	}

	@Override
	protected Coin newObject() {
		return new Coin(this.world);
	}

}
