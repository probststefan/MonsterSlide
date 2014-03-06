package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.interfaces.ICollisionEntity;
import fh.teamproject.interfaces.IPlayer;
import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.interfaces.IWorld;
import fh.teamproject.physics.PhysixManager;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.screens.MenuScreen;

public class World implements IWorld {

	// Die Welt haelt die wichtigsten Elemente inne.
	private Slide slide;
	private Player player;
	private Coins coins;
	private Score score;
	ModelInstance skydome;

	// Rendering
	public ModelBatch batch;
	public Environment lights;
	public PhysixManager physixManager;
	private ClosestRayResultCallback resultCallback;
	final float checkPlayerOnSlideRayDepth = 100.0f;
	Array<ICollisionEntity> entities = new Array<ICollisionEntity>(32);

	GameScreen gameScreen;

	public World(GameScreen gameScreen) {
		this.gameScreen = gameScreen;

		// Rendering
		batch = new ModelBatch();
		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		physixManager = new PhysixManager(this);
		player = new Player(this);

		btDiscreteDynamicsWorld dynamicsWorld = physixManager.getWorld();
		// Rutsche, Spieler und Coins erzeugen.
		coins = new Coins(this);
		coins.addCoin(new Vector3());
		score = new Score();
		slide = new Slide(this);
		slide.init();

		resultCallback = new ClosestRayResultCallback(getPlayer().getPosition(),
				new Vector3(getPlayer().getPosition().x, getPlayer().getPosition().y
						- this.checkPlayerOnSlideRayDepth, getPlayer().getPosition().z));
		// Skydome laden.
		skydome = new ModelInstance(gameScreen.getAssets().get("data/g3d/skydome.g3db",
				Model.class));
	}

	public void update() {
		if (!gameScreen.isPaused) {
			physixManager.update();
		}

		if (!checkIsPlayerOnSlide() && !gameScreen.isPaused) {
			gameScreen.getGame().setScreen(new MenuScreen(gameScreen.getGame()));
		} else {
			player.update();
			slide.update();
			coins.update();
			// Der Skydome soll den Player verfolgen.
			skydome.transform.setToTranslation(player.position);
		}
	}

	@Override
	public void render() {
		batch.begin(GameScreen.camManager.getActiveCamera());
		if (skydome != null)
			batch.render(skydome, lights);

		batch.render(player.getModelInstance(), lights);
		batch.render(slide.getModelInstance(), lights);

		for (Coin coin : coins.getCoins()) {
			batch.render(coin.getModelInstance(), lights);
		}

		batch.end();
	}

	public void dispose() {
		physixManager.dispose();
		for (ISlidePart part : slide.getSlideParts()) {
			part.dispose();
			slide.removeSlidePart(part);
		}
		resultCallback.dispose();
	}

	/**
	 * Liefert die aktuelle Rutsche.
	 */
	@Override
	public ISlide getSlide() {
		return slide;
	}

	/**
	 * Liefert das aktuelle Score-Objekt.
	 * 
	 * @return Score
	 */
	public Score getScore() {
		return this.score;
	}

	/**
	 * Liefert das aktuelle Objekt zum Spieler.
	 */
	@Override
	public IPlayer getPlayer() {
		return player;
	}

	@Override
	public Coins getCoins() {
		return this.coins;
	}

	public void reset() {
		Gdx.app.log("World", "resetting");
		gameScreen.game.setScreen(new MenuScreen(gameScreen.game));
	}

	/**
	 * Gibt an ob der Player sich noch auf der Rutsche befindet oder schon
	 * runtergefallen ist.
	 * 
	 * @return boolean
	 */
	public boolean checkIsPlayerOnSlide() {
		btDiscreteDynamicsWorld dynamicsWorld = physixManager.getWorld();

		resultCallback.setCollisionObject(null);
		resultCallback.setClosestHitFraction(1f);
		resultCallback.getRayFromWorld().setValue(player.position.x, player.position.y,
				player.position.z);
		resultCallback.getRayToWorld().setValue(player.position.x,
				player.position.y - checkPlayerOnSlideRayDepth, player.position.z);

		dynamicsWorld.rayTest(player.position, new Vector3(player.position.x,
				player.position.y - checkPlayerOnSlideRayDepth, player.position.z),
				resultCallback);

		return resultCallback.hasHit();
	}

	public PhysixManager getPhysixManager() {
		return physixManager;
	}
}
