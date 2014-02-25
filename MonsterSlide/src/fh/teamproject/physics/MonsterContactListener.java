package fh.teamproject.physics;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;

import fh.teamproject.entities.Coins;
import fh.teamproject.entities.Player;
import fh.teamproject.entities.Score;
import fh.teamproject.entities.Slide;

public class MonsterContactListener extends ContactListener {

	private Coins coins;
	private Score score;
	private Slide slide;
	private Player player;

	public MonsterContactListener(Coins coins, Score score, Slide slide, Player player) {
		this.coins = coins;
		this.score = score;
		this.slide = slide;
		this.player = player;
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
			// Setzen des aktuellen berutschten SlideParts.
			this.slide.setActualSlidePartId(userValue1);
			// Player sitzt auf der Slide.
			this.player.setGrounded(true);
		}
	}

	@Override
	public void onContactEnded(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		if (match1) {
			// Player sitzt nicht mehr auf der Slide (er ist abgehoben bzw.
			// springt).
			this.player.setGrounded(false);
		}
	}
}
