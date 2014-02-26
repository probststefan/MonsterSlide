package fh.teamproject.entities;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

/**
 * Diese Klasse verwaltet alle Coins.
 * 
 * @author stefanprobst
 * 
 */
public class Coins {

	private Array<Coin> coins;
	private IntArray toDeleteIDs;
	private CoinPool coinPool;
	private World world;

	// public Coins(GameScreen gameScreen, btDynamicsWorld dynamicsWorld) {
	public Coins(World world) {
		this.world = world;
		this.coins = new Array<Coin>();
		this.toDeleteIDs = new IntArray();
		this.coinPool = new CoinPool(this.world);
	}

	public void update() {
		this.removeCoins();
	}

	public void addCoin(Vector3 position) {
		Coin tmpCoin = coinPool.obtain();
		tmpCoin.setToPosition(position);
		coins.add(tmpCoin);
		this.world.getPhysixManager().addRigidBody(tmpCoin.getRigidBody());
	}

	/**
	 * Markiert einen Coin als "aufgehoben" vom Spieler.
	 * 
	 * @param id
	 */
	public void pickCoin(int id) {
		int index = this.getCoinIndex(id);

		if (index >= 0) {
			// Coins als geloescht markieren.
			this.toDeleteIDs.add(index);
		}
	}

	/**
	 * Liefert alle Coins.
	 * 
	 * @return Array<Coin>
	 */
	public Array<Coin> getCoins() {
		return this.coins;
	}

	/**
	 * Liefert den Index eines Coins, anhand seiner Id, im coins-Array.
	 * 
	 * @param id
	 * @return int
	 */
	private int getCoinIndex(int id) {
		int index = -1;

		for (int i = 0; i < this.coins.size; i++) {
			if (this.coins.get(i).getID() == id) {
				index = i;
				continue;
			}
		}

		return index;
	}

	/**
	 * Entfernt die als geloescht markierten Coins aus der Bullet-Welt und dem
	 * coins-Array.
	 */
	private void removeCoins() {
		if (this.toDeleteIDs.size > 0) {
			for (int i = 0; i < this.toDeleteIDs.size; i++) {
				this.world.getPhysixManager().removeRigidBody(
						this.coins.get(this.toDeleteIDs.get(i)).getRigidBody());
				this.coins.removeIndex(this.toDeleteIDs.get(i));
				this.toDeleteIDs.removeIndex(i);
			}
		}
	}
}