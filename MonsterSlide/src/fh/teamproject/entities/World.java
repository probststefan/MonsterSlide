package fh.teamproject.entities;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import fh.teamproject.interfaces.IPlayer;
import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.IWorld;

public class World implements IWorld {
	private btDiscreteDynamicsWorld dynamicsWorld;
	// Bullet Infos.
	private int maxSubSteps = 5;
	private float fixedTimeStep = 1f / 60f;

	public World() {
		btBroadphaseInterface broadphase = new btDbvtBroadphase();
		btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
		btCollisionDispatcher dispatcher = new btCollisionDispatcher(
				collisionConfiguration);
		btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();

		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver,
				collisionConfiguration);
		dynamicsWorld.setGravity(new Vector3(0, -9.81f, 0));
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

	@Override
	public ISlide getSlide() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPlayer getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}
}