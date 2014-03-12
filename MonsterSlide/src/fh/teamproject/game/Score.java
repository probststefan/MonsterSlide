package fh.teamproject.game;

/**
 * Die Score-Klasse verwaltet den aktuellen Punktestand eines Spiels. Dazu
 * zaehlen zur Zeit die Anzahl der gesammelten Coins und die gerutschte Strecke.
 * 
 * @author stefanprobst
 * 
 */
public class Score {
	private int coins;
	private float distance;

	public Score() {
		this.coins = 0;
		this.distance = 0f;
	}

	public void incrementCoinScore() {
		this.coins++;
	}

	public void incrementSlidedDistance(float distance) {
		this.distance += distance;
	}

	public int getCoinsScore() {
		return this.coins;
	}

	public float getSlidedDistance() {
		return this.distance;
	}
}