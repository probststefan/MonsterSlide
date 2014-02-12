package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
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

	// Bullet Infos.
	private btDiscreteDynamicsWorld dynamicsWorld;
	private btBroadphaseInterface broadphase;
	private btSequentialImpulseConstraintSolver solver;
	private btCollisionDispatcher dispatcher;
	private btDefaultCollisionConfiguration collisionConfiguration;
	private int maxSubSteps = 3;
	private float fixedTimeStep = 1 / 30f;
	private float worldGravtiy = -9.81f;

	private TriangleMeshCollisionFixer myContactListener;

	public PerformanceCounter performanceCounter = new PerformanceCounter(this.getClass()
			.getSimpleName());
	GameScreen gameScreen;

	public World(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
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

		// Spieler zur Bullet-Welt hinzufuegen.
		dynamicsWorld.addRigidBody(player.getRigidBody());

		// ContactListener initialisieren.
		myContactListener = new TriangleMeshCollisionFixer();
		myContactListener.enable();
	}

	public void update() {
		// Bullet update.
		performanceCounter.tick();
		performanceCounter.start();
		dynamicsWorld.stepSimulation(Gdx.graphics.getDeltaTime(), getMaxSubSteps(),
				getFixedTimeStep());
		performanceCounter.stop();
		player.update();
	}

	public void dispose() {
		dynamicsWorld.dispose();
		solver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfiguration.dispose();

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
	public void render(ModelBatch batch, Environment lights) {
		batch.render(player.getModelInstance(), lights);
		player.render();
		slide.render(batch, lights);
		batch.render(coin.getModelInstance(), lights);
	}
}
