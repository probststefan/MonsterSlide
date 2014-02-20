package fh.teamproject.physics;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;

import fh.teamproject.entities.Coins;
import fh.teamproject.entities.Score;

public class CoinContactListener extends ContactListener {

	private Coins coins;
	private Score score;

	public CoinContactListener(Coins coins, Score score) {
		this.coins = coins;
		this.score = score;
	}

	@Override
	public void onContactStarted(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		if (match0) {
			// Kollision mit einem Coin.
			this.coins.pickCoin(userValue0);
			this.score.incrementCoinScore();
		}

		if (match1) {
			// Kollision mit der Slide.
		}
	}
}
