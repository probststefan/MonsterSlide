package fh.teamproject.physics;

import com.badlogic.gdx.Gdx;
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

import fh.teamproject.game.World;
import fh.teamproject.physics.callbacks.contacts.MonsterContactListener;
import fh.teamproject.physics.callbacks.contacts.PlayerContactCallback;
import fh.teamproject.physics.callbacks.contacts.TriangleMeshCollisionFixer;

public class PhysixManager {

	private World world;

	// Bullet Infos.
	private btDiscreteDynamicsWorld dynamicsWorld;
	private btBroadphaseInterface broadphase;
	private btSequentialImpulseConstraintSolver solver;
	private btCollisionDispatcher dispatcher;
	private btDefaultCollisionConfiguration collisionConfiguration;
	private int maxSubSteps = 3;
	private float fixedTimeStep = 1 / 60f;
	private float worldGravtiy = -9.81f;
	private final float checkPlayerOnSlideRayDepth = 100.0f;

	private MonsterContactListener gameContactListener;

	public PerformanceCounter performanceCounter = new PerformanceCounter(this.getClass()
			.getSimpleName());

	public PhysixManager(World world) {
		this.world = world;
		// "Bullet-Welt" erstellen.
		broadphase = new btDbvtBroadphase();
		collisionConfiguration = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfiguration);
		solver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver,
				collisionConfiguration);

		btContactSolverInfo info = dynamicsWorld.getSolverInfo();
		// info.setRestitution(0.0f);
		// info.setNumIterations(20);

		// info.setSplitImpulse(1); // enable split impulse feature
		// optionally set the m_splitImpulsePenetrationThreshold (only used when
		// m_splitImpulse is enabled)
		// only enable split impulse position correction when the penetration is
		// deeper than this m_splitImpulsePenetrationThreshold, otherwise use
		// the regular velocity/position constraint coupling (Baumgarte).
		// info.setSplitImpulsePenetrationThreshold(-0.02f);

		dynamicsWorld.setGravity(new Vector3(0, worldGravtiy, 0));

		// ContactListener initialisieren.
		gameContactListener = new MonsterContactListener(world);
		gameContactListener.addListener(new TriangleMeshCollisionFixer());
		gameContactListener.addListener(new PlayerContactCallback(world));
		gameContactListener.enable();

	}

	public void update() {
		performanceCounter.tick();
		performanceCounter.start();
		dynamicsWorld.stepSimulation(Gdx.graphics.getDeltaTime(), getMaxSubSteps(),
				getFixedTimeStep());
		performanceCounter.stop();
	}

	private float getFixedTimeStep() {
		return fixedTimeStep;
	}

	private int getMaxSubSteps() {
		return maxSubSteps;
	}

	public btDiscreteDynamicsWorld getWorld() {
		return dynamicsWorld;
	}

	public void addRigidBody(btRigidBody rigidBody) {
		dynamicsWorld.addRigidBody(rigidBody);
	}

	public void removeRigidBody(btRigidBody rigidBody) {
		dynamicsWorld.removeRigidBody(rigidBody);
	}

	public void dispose() {
		dynamicsWorld.dispose();
		solver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfiguration.dispose();
	}

	public PerformanceCounter getPerformanceCounter() {
		return performanceCounter;
	}

}
