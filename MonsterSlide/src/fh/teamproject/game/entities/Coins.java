package fh.teamproject.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.game.CoinPool;
import fh.teamproject.game.World;
import fh.teamproject.screens.GameScreen;

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

	private float COIN_ROTATION_SPEED;

	// public Coins(GameScreen gameScreen, btDynamicsWorld dynamicsWorld) {
	public Coins(World world) {
		this.world = world;
		this.coins = new Array<Coin>();
		this.toDeleteIDs = new ArrayList<Integer>();
		this.coinPool = new CoinPool(this.world, 64, 64);

		this.COIN_ROTATION_SPEED = GameScreen.settings.COIN_ROTATION_SPEED;
	}

	public void update() {
		this.removeCoins();

		// Coins rotieren.
		for (Coin coin : this.coins) {
			coin.instance.transform.rotate(Vector3.Y, this.COIN_ROTATION_SPEED
					* Gdx.graphics.getDeltaTime());
		}
	}

	public void addCoin(Vector3 position) {
		Coin tmpCoin = coinPool.obtain();
		tmpCoin.setPosition(position);
		tmpCoin.instance.transform.rotate(Vector3.Y, MathUtils.random(0.0f, 360.0f));
		tmpCoin.getRigidBody().setActivationState(Collision.ACTIVE_TAG);
		tmpCoin.getRigidBody().setContactCallbackFilter(Player.PLAYER_FLAG);
		coins.add(tmpCoin);
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
			// FIXME: das hier könnte in reset Mehtode des Coins
			// Filter auf 0 setzen, damit keine Kollision mehr auftreten
			// kann.
			this.coins.get(index).getRigidBody().setContactCallbackFilter(0);
			this.coins.get(index).getRigidBody()
					.setActivationState(Collision.DISABLE_SIMULATION);
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

	public void generateCoinsforSpan(int span) {
		CatmullRomSpline<Vector3> spline = world.getSlide().getSpline();
		float epsilon = 0.01f;
		// FIXME: Magic number for max collectables on span
		int maxCollectables = MathUtils.random(1, 5);
		float splitting = 1f / (float) maxCollectables;
		// Gdx.app.log("Coins", maxCollectables + "collectables on span " +
		// span);
		Vector3 interpolatedVertex = new Vector3();
		/*
		 * Used to translate a collectable along the binormal, distributing it
		 * randomly over the spline
		 */
		float scale = MathUtils.random(0.1f, 0.9f);
		for (float i = 0; i < 1; i += splitting) {

			spline.valueAt(interpolatedVertex, span, i);
			// 1. und 2. Ableitung bilden.
			Vector3 derivation = new Vector3();
			derivation = spline.derivativeAt(derivation, span, i);
			derivation.nor();
			Vector3 tangent = derivation.cpy();
			/*
			 * Mit dem upVector wird das Problem der springenden Normalen
			 * behoben.
			 * 
			 * @link
			 * http://www.it.hiof.no/~borres/j3d/explain/frames/p-frames.html
			 */
			Vector3 upVector = new Vector3(0.0f, -1.0f, 0.0f);
			Vector3 binormal = derivation.cpy().crs(upVector).nor();
			Vector3 normal = tangent.cpy().crs(binormal);
			// warum wird derivation nicht normiert aber tangent schon
			binormal.scl(GameScreen.settings.SLIDE_WIDTH * scale);

			addCoin(interpolatedVertex.cpy().add(binormal));
		}
	}
}