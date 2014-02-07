package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.PerformanceCounter;

import fh.teamproject.interfaces.IPlayer;
import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.IWorld;
import fh.teamproject.physics.PlayerTickCallback;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.screens.MenuScreen;

public class World implements IWorld {

	// Die Welt haelt die wichtigsten Elemente inne.
	private Slide slide;
	private Player player;

	// Bullet Infos.
	private btDiscreteDynamicsWorld dynamicsWorld;
	private btBroadphaseInterface broadphase;
	private btSequentialImpulseConstraintSolver solver;
	private btCollisionDispatcher dispatcher;
	private btDefaultCollisionConfiguration collisionConfiguration;
	private int maxSubSteps = 5;
	private float fixedTimeStep = 1f / 60f;
	private float worldGravtiy = -9.81f;

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
		dynamicsWorld.setGravity(new Vector3(0, worldGravtiy, 0));

		// Rutsche und Spieler erzeugen.
		slide = new Slide(dynamicsWorld);
		player = new Player(slide.getStartPosition());
		PlayerTickCallback playerCallback = new PlayerTickCallback(player);
		playerCallback.attach(dynamicsWorld, false);

		// Spieler zur Bullet-Welt hinzufuegen.
		dynamicsWorld.addRigidBody(player.getRigidBody());

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
		slide.render(batch, lights);
	}
}
