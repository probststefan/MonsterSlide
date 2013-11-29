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
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.interfaces.IWorld;

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

	public World() {
		// "Bullet-Welt" erstellen.
		broadphase = new btDbvtBroadphase();
		collisionConfiguration = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfiguration);
		solver = new btSequentialImpulseConstraintSolver();

		this.dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver,
				collisionConfiguration);
		this.dynamicsWorld.setGravity(new Vector3(0, this.worldGravtiy, 0));

		// Rutsche und Spieler erzeugen.
		this.slide = new Slide(this.dynamicsWorld);
		this.player = new Player();

		// Elemente der Rutsche zur Bullet-Welt hinzufuegen.
		for (ISlidePart slidePart : this.slide.getSlideParts()) {
			this.dynamicsWorld.addRigidBody(slidePart.getRigidBody());
		}

		// Spieler zur Bullet-Welt hinzufuegen.
		this.dynamicsWorld.addRigidBody(this.player.getRigidBody());
	}

	public void update() {
		// Bullet update.
		performanceCounter.tick();
		performanceCounter.start();
		this.dynamicsWorld.stepSimulation(Gdx.graphics.getDeltaTime(),
				this.getMaxSubSteps(), this.getFixedTimeStep());
		performanceCounter.stop();

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
	public ISlide getSlide() {
		return this.slide;
	}

	/**
	 * Liefert das aktuelle Objekt zum Spieler.
	 */
	public IPlayer getPlayer() {
		return this.player;
	}
}