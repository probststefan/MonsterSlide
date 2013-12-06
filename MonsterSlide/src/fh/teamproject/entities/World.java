package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
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
		this.broadphase = new btDbvtBroadphase();
		this.collisionConfiguration = new btDefaultCollisionConfiguration();
		this.dispatcher = new btCollisionDispatcher(this.collisionConfiguration);
		this.solver = new btSequentialImpulseConstraintSolver();

		this.dynamicsWorld = new btDiscreteDynamicsWorld(this.dispatcher,
				this.broadphase, this.solver, this.collisionConfiguration);
		this.dynamicsWorld.setGravity(new Vector3(0, this.worldGravtiy, 0));

		// Rutsche und Spieler erzeugen.
		this.slide = new Slide(this.dynamicsWorld);
		this.player = new Player();
		PlayerTickCallback playerCallback = new PlayerTickCallback(this.player);
		playerCallback.attach(this.dynamicsWorld, false);

		// Rutsche zur Welt hinzufuegen.
		this.dynamicsWorld.addRigidBody(this.slide.getRigidBody());
		// Spieler zur Bullet-Welt hinzufuegen.
		this.dynamicsWorld.addRigidBody(this.player.getRigidBody());
	}

	public void update() {
		// Bullet update.
		this.performanceCounter.tick();
		this.performanceCounter.start();
		this.dynamicsWorld.stepSimulation(Gdx.graphics.getDeltaTime(),
				this.getMaxSubSteps(), this.getFixedTimeStep());
		this.performanceCounter.stop();

		this.player.update();
		this.slide.update(this.player.getPosition());
	}

	public void dispose() {
		this.dynamicsWorld.dispose();
		this.solver.dispose();
		this.broadphase.dispose();
		this.dispatcher.dispose();
		this.collisionConfiguration.dispose();

		for (int i = 0; i < this.slide.getSlideParts().size(); ++i) {
			this.slide.getSlideParts().get(i).dispose();
			this.slide.removeSlidePart(this.slide.getSlideParts().get(i));
		}
	}

	public void addRigidBody(btRigidBody rigidBody) {
		this.dynamicsWorld.addRigidBody(rigidBody);
	}

	public void removeRigidBody(btRigidBody rigidBody) {
		this.dynamicsWorld.removeRigidBody(rigidBody);
	}

	public int getMaxSubSteps() {
		return this.maxSubSteps;
	}

	public float getFixedTimeStep() {
		return this.fixedTimeStep;
	}

	public btDiscreteDynamicsWorld getWorld() {
		return this.dynamicsWorld;
	}

	/**
	 * Liefert die aktuelle Rutsche.
	 */
	@Override
	public ISlide getSlide() {
		return this.slide;
	}

	/**
	 * Liefert das aktuelle Objekt zum Spieler.
	 */
	@Override
	public IPlayer getPlayer() {
		return this.player;
	}

	public void reset() {
		Gdx.app.log("World", "resetting");
		this.gameScreen.game.setScreen(new MenuScreen(this.gameScreen.game));
		// this.dynamicsWorld.removeRigidBody(this.player.getRigidBody());
		//
		// this.player.getRigidBody().setCollisionFlags(
		// this.player.getRigidBody().getCollisionFlags()
		// | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		// this.player.getRigidBody().setActivationState(
		// CollisionConstants.DISABLE_DEACTIVATION);
		//
		// this.player.instance.transform.setToTranslation(new Vector3(0f, 5f,
		// 0f));
		//
		// this.player.getRigidBody().setCollisionFlags(
		// this.player.getRigidBody().getCollisionFlags()
		// & ~(btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT));
		//
		// this.player.getRigidBody().forceActivationState(1);
		//
		// this.dynamicsWorld.addRigidBody(this.player.getRigidBody());
	}
}
