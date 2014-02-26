package fh.teamproject.entities;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.screens.GameScreen;

/**
 * Diese Klasse verwaltet alles Coins.
 * 
 * Hallo Torsten; Test
 * 
 * @author stefanprobst
 * 
 */
public class Coins {

	private Array<Coin> coins = new Array<Coin>();
	private CoinPool coinPool;
	private btDynamicsWorld dynamicsWorld;
	GameScreen gameScreen;

	public Coins(GameScreen gameScreen, btDynamicsWorld dynamicsWorld) {
		this.dynamicsWorld = dynamicsWorld;
		this.gameScreen = gameScreen;
		this.coinPool = new CoinPool(gameScreen);
	}

	public void addCoin(Vector3 position) {
		Coin tmpCoin = coinPool.obtain();
		tmpCoin.setToPosition(position);
		coins.add(tmpCoin);
		this.dynamicsWorld.addRigidBody(tmpCoin.getRigidBody());
	}

	/**
	 * Entfernt einen Coin aus dem coins-Array.
	 * 
	 * @param id
	 */
	public void pickCoin(int id) {
		int index = this.getCoinIndex(id);

		if (index >= 0) {
			// Coins duerfen hier nicht geloescht werden (Als geloescht
			// markieren).
			// this.dynamicsWorld.removeRigidBody(this.coins.get(index).getRigidBody());
			// this.coins.removeIndex(index);
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
}