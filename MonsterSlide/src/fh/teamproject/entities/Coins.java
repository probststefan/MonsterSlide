package fh.teamproject.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Diese Klasse verwaltet alle Coins.
 * 
 * @author stefanprobst
 * 
 */
public class Coins {

	private Array<Coin> coins;
	private ArrayList<Integer> toDeleteIDs;
	private CoinPool coinPool;
	private World world;

	// public Coins(GameScreen gameScreen, btDynamicsWorld dynamicsWorld) {
	public Coins(World world) {
		this.world = world;
		this.coins = new Array<Coin>();
		this.toDeleteIDs = new ArrayList<Integer>();
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
			// Filter auf 0 setzen, damit keine Kollision mehr auftreten kann.
			this.coins.get(index).getRigidBody().setContactCallbackFilter(0);
			this.coins.get(index).getRigidBody().setActivationState(0);
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
		for (int i = 0; i < this.coins.size; i++) {
			if (this.coins.get(i).getID() == id) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Entfernt die als geloescht markierten Coins aus der Bullet-Welt und dem
	 * coins-Array.
	 */
	private void removeCoins() {
		if (this.toDeleteIDs.size() > 0) {
			for (int i = 0; i < this.toDeleteIDs.size(); i++) {
				coinPool.free(this.coins.get(this.toDeleteIDs.get(i)));
				this.coins.removeIndex(this.toDeleteIDs.get(i));
			}

			this.toDeleteIDs.clear();
		}
	}
}