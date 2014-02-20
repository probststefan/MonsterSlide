package fh.teamproject.entities;

import com.badlogic.gdx.utils.Pool;

public class CoinPool extends Pool<Coin> {

	@Override
	protected Coin newObject() {
		Coin coin = new Coin();
		return coin;
	}
}
