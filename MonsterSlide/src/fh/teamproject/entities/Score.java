package fh.teamproject.entities;

/**
 * Die Score-Klasse verwaltet den aktuellen Punktestand eines Spiels. Dazu
 * zaehlen zur Zeit die Anzahl der gesammelten Coins und die gerutschte Strecke.
 * 
 * @author stefanprobst
 * 
 */
public class Score {
	private int coins, distance;

	public Score() {
		this.coins = 0;
		this.distance = 0;
	}

	public void incrementCoinScore() {
		this.coins++;
	}

	public void incrementSlidedDistance(int distance) {
		this.distance += distance;
	}

	public int getCoinsScore() {
		return this.coins;
	}

	public int getSlidedDistance() {
		return this.distance;
	}
}