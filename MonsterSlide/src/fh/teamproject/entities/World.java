package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import fh.teamproject.interfaces.IPlayer;
import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.IWorld;

public class World implements IWorld {

	// Die Welt haelt die wichtigsten Elemente inne.
	private Slide slide;
	private Player player;

	// Bullet Infos.
	private btDiscreteDynamicsWorld dynamicsWorld;
	private int maxSubSteps = 5;
	private float fixedTimeStep = 1f / 60f;
	private float worldGravtiy = -9.81f;

	public World() {
		// "Bullet-Welt" erstellen.
		btBroadphaseInterface broadphase = new btDbvtBroadphase();
		btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
		btCollisionDispatcher dispatcher = new btCollisionDispatcher(
				collisionConfiguration);
		btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();

		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver,
				collisionConfiguration);
		dynamicsWorld.setGravity(new Vector3(0, this.worldGravtiy, 0));

		this.slide = new Slide();
		this.player = new Player();
	}

	public void update() {
		// Bullet update.
		if (this.getWorld() instanceof btDynamicsWorld) {
			((btDynamicsWorld) this.getWorld()).stepSimulation(
					Gdx.graphics.getDeltaTime(), this.getMaxSubSteps(),
					this.getFixedTimeStep());
		}
	}

	public void addRigidBody(btRigidBody rigidBody) {
		this.dynamicsWorld.addRigidBody(rigidBody);
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