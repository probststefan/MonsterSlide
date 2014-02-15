package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btContactSolverInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.PerformanceCounter;

import fh.teamproject.interfaces.IPlayer;
import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.interfaces.IWorld;
import fh.teamproject.physics.PlayerTickCallback;
import fh.teamproject.physics.TriangleMeshCollisionFixer;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.screens.MenuScreen;

public class World implements IWorld {

	// Die Welt haelt die wichtigsten Elemente inne.
	private Slide slide;
	private Player player;
	private Coin coin;
	ModelInstance skydome;

	// Rendering
	public ModelBatch batch, batch2;
	public Environment lights;

	// Bullet Infos.
	private btDiscreteDynamicsWorld dynamicsWorld;
	private btBroadphaseInterface broadphase;
	private btSequentialImpulseConstraintSolver solver;
	private btCollisionDispatcher dispatcher;
	private btDefaultCollisionConfiguration collisionConfiguration;
	private int maxSubSteps = 3;
	private float fixedTimeStep = 1 / 30f;
	private float worldGravtiy = -9.81f;
	private final float checkPlayerOnSlideRayDepth = 100.0f;

	private TriangleMeshCollisionFixer myContactListener;
	private ClosestRayResultCallback resultCallback;

	public PerformanceCounter performanceCounter = new PerformanceCounter(this.getClass()
			.getSimpleName());
	GameScreen gameScreen;

	public World(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		// Rendering
		batch = new ModelBatch();
		batch2 = new ModelBatch();
		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// "Bullet-Welt" erstellen.
		broadphase = new btDbvtBroadphase();
		collisionConfiguration = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfiguration);
		solver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver,
				collisionConfiguration);

		btContactSolverInfo info = dynamicsWorld.getSolverInfo();
		info.setRestitution(0.0f);
		info.setNumIterations(20);

		// info.setSplitImpulse(1); // enable split impulse feature
		// optionally set the m_splitImpulsePenetrationThreshold (only used when
		// m_splitImpulse is enabled)
		// only enable split impulse position correction when the penetration is
		// deeper than this m_splitImpulsePenetrationThreshold, otherwise use
		// the regular velocity/position constraint coupling (Baumgarte).
		// info.setSplitImpulsePenetrationThreshold(-0.02f);

		dynamicsWorld.setGravity(new Vector3(0, worldGravtiy, 0));

		// Rutsche und Spieler erzeugen.
		slide = new Slide(dynamicsWorld);
		slide.getSlideParts().get(0).getRigidBody().setContactCallbackFlag(2);
		player = new Player(slide.getStartPosition());
		player.getRigidBody().setContactCallbackFlag(4);
		PlayerTickCallback playerCallback = new PlayerTickCallback(player);
		playerCallback.attach(dynamicsWorld, false);

		coin = new Coin();

		// Skydome laden.
		AssetManager asset = new AssetManager();
		asset.load("data/g3d/skydome.g3db", Model.class);
		asset.finishLoading();

		skydome = new ModelInstance(asset.get("data/g3d/skydome.g3db", Model.class));

		// Spieler zur Bullet-Welt hinzufuegen.
		dynamicsWorld.addRigidBody(player.getRigidBody());

		// ContactListener initialisieren.
		myContactListener = new TriangleMeshCollisionFixer();
		myContactListener.enable();

		// Wird fuer checkIsPlayerOnSlide() benoetigt.
		resultCallback = new ClosestRayResultCallback(player.position, new Vector3(
				player.position.x, player.position.y - this.checkPlayerOnSlideRayDepth,
				player.position.z));
	}

	public void update() {
		// Bullet update.
		performanceCounter.tick();
		performanceCounter.start();
		dynamicsWorld.stepSimulation(Gdx.graphics.getDeltaTime(), getMaxSubSteps(),
				getFixedTimeStep());
		performanceCounter.stop();
		player.update();
		slide.update(player.position);
		// Der Skydome soll den Player verfolgen.
		skydome.transform.setToTranslation(player.position);
	}

	public void dispose() {
		dynamicsWorld.dispose();
		solver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfiguration.dispose();
		resultCallback.dispose();

		for (int i = 0; i < slide.getSlideParts().size; ++i) {
			slide.getSlideParts().get(i).dispose();
			slide.removeSlidePart(slide.getSlideParts().get(i));
		}
	}

	public void addRigidBody(btRigidBody rigidBody) {
		dynamicsWorld.addRigidBody(rigidBody);
	}

	public void removeRigidBody(btRigidBody rigidBody) {
		dynamicsWorld.removeRigidBody(rigidBody);
	}

	public int getMaxSubSteps() {
		return maxSubSteps;
	}

	public float getFixedTimeStep() {
		return fixedTimeStep;
	}

	public btDiscreteDynamicsWorld getWorld() {
		return dynamicsWorld;
	}

	/**
	 * Liefert die aktuelle Rutsche.
	 */
	@Override
	public ISlide getSlide() {
		return slide;
	}

	/**
	 * Liefert das aktuelle Objekt zum Spieler.
	 */
	@Override
	public IPlayer getPlayer() {
		return player;
	}

	public void reset() {
		Gdx.app.log("World", "resetting");
		gameScreen.game.setScreen(new MenuScreen(gameScreen.game));
	}

	@Override
	public void render() {
		batch.begin(GameScreen.camManager.getActiveCamera());
		if (skydome != null)
			batch.render(skydome, lights);
		batch.render(player.getModelInstance(), lights);
		for (ISlidePart part : slide.getSlideParts()) {
			batch.render(part.getModelInstance(), lights);
		}
		batch.end();
	}


	/**
	 * Gibt an ob der Player sich noch auf der Rutsche befindet oder schon
	 * runtergefallen ist.
	 * 
	 * @return boolean
	 */
	public boolean checkIsPlayerOnSlide() {
		resultCallback.setCollisionObject(null);
		resultCallback.setClosestHitFraction(1f);
		resultCallback.getRayFromWorld().setValue(player.position.x, player.position.y,
				player.position.z);
		resultCallback.getRayToWorld().setValue(player.position.x,
				player.position.y - this.checkPlayerOnSlideRayDepth, player.position.z);

		dynamicsWorld.rayTest(player.position, new Vector3(player.position.x,
				player.position.y - this.checkPlayerOnSlideRayDepth, player.position.z),
				resultCallback);

		return resultCallback.hasHit();
	}
}

