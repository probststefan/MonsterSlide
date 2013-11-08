package fh.teamproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.collision.btStaticPlaneShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw.DebugDrawModes;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;

import fh.teamproject.entities.Plane;
import fh.teamproject.entities.Sphere;
import fh.teamproject.utils.DebugDrawer;

public class GameScreen implements Screen {
	// Den Debug-Modus von Bullet ein- und ausschalten.
	private boolean debuggerOn = false;
	public DebugDrawer debugDrawer = null;

	PerspectiveCamera camera;
	CameraInputController controller;

	ModelBatch batch;
	Environment lights;

	Sphere sphere;
	Plane plane;

	// Bullet Infos.
	public int maxSubSteps = 5;
	public float fixedTimeStep = 1f / 60f;
	public btDiscreteDynamicsWorld dynamicsWorld;
	public btRigidBody fallRigidBody;

	public GameScreen() {

		this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.camera.translate(0f, 0f, 10f);
		// this.camera.lookAt(0, 0, 0);
		this.controller = new CameraInputController(this.camera);
		this.sphere = new Sphere();
		this.plane = new Plane();

		this.batch = new ModelBatch();

		this.lights = new Environment();
		this.lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f,
				1f));

		this.lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		InputMultiplexer inputMul = new InputMultiplexer();
		inputMul.addProcessor(this.sphere);
		inputMul.addProcessor(this.controller);

		Gdx.input.setInputProcessor(inputMul);

		initBullet();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		if (debuggerOn) {
			// Stellt die Kollisionsobjekt von Bullet grafisch dar.
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
			setDebugMode(DebugDrawModes.DBG_DrawWireframe, camera.combined);
			Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

			if (debugDrawer != null && debugDrawer.getDebugMode() > 0) {
				debugDrawer.begin();
				dynamicsWorld.debugDrawWorld();
				debugDrawer.end();
			}
		}

		/* UPDATE */
		this.camera.lookAt(this.sphere.position);
		this.camera.update();

		// Bullet update.
		if (dynamicsWorld instanceof btDynamicsWorld) {
			((btDynamicsWorld) dynamicsWorld).stepSimulation(Gdx.graphics.getDeltaTime(),
					maxSubSteps, fixedTimeStep);

			fallRigidBody.getMotionState().getWorldTransform(
					this.sphere.instance.transform);
			// Position-Vector in Sphere aktualisieren.
			this.sphere.instance.transform.getTranslation(this.sphere.position);
		}

		/* RENDER */
		this.batch.begin(this.camera);
		this.batch.render(this.sphere.instance, this.lights);
		this.batch.render(this.plane.instance, this.lights);
		this.batch.end();
	}

	public void initBullet() {
		btBroadphaseInterface broadphase = new btDbvtBroadphase();

		btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
		btCollisionDispatcher dispatcher = new btCollisionDispatcher(
				collisionConfiguration);

		btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();

		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver,
				collisionConfiguration);

		dynamicsWorld.setGravity(new Vector3(0, -9.81f, 0));

		// Plane-Shape erzeugen, die den Boden darstellt.
		// Plane-Constant beschreibt wahrscheinlich die Dicke.
		btCollisionShape groundShape = new btStaticPlaneShape(new Vector3(0, 1, 0), 0);
		// Ball-Shape erstellen.
		btCollisionShape fallShape = new btSphereShape(this.sphere.radius);

		btDefaultMotionState groundMotionState = new btDefaultMotionState();
		groundMotionState.setWorldTransform(this.plane.instance.transform);

		btTransform btTrans = new btTransform();
		btTrans.setRotation(new Quaternion(0, 0, 0, 1));
		// btTrans.setOrigin(new Vector3(0, 0, 0));
		// TODO: Bin mir nicht sicher ob hier alle Methoden richtig gewaehlt
		// wurden.
		groundMotionState.setStartWorldTrans(btTrans);

		btRigidBodyConstructionInfo rigidBodyInfo = new btRigidBodyConstructionInfo(0,
				groundMotionState, groundShape, new Vector3(0, 0, 0));
		btRigidBody groundRigidBody = new btRigidBody(rigidBodyInfo);
		dynamicsWorld.addRigidBody(groundRigidBody);

		// Fallenden Ball erstellen.
		btDefaultMotionState fallMotionState = new btDefaultMotionState();
		fallMotionState.setWorldTransform(this.sphere.instance.transform);

		Vector3 fallInertia = new Vector3(0, 0, 0);
		fallShape.calculateLocalInertia(1, fallInertia);

		btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(1,
				fallMotionState, fallShape, fallInertia);
		fallRigidBody = new btRigidBody(fallRigidBodyCI);
		// fallRigidBody.setDamping(0.3f, 0.8f);
		dynamicsWorld.addRigidBody(fallRigidBody);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void setDebugMode(final int mode, final Matrix4 projMatrix) {
		if (mode == btIDebugDraw.DebugDrawModes.DBG_NoDebug && debugDrawer == null)
			return;
		if (debugDrawer == null)
			dynamicsWorld.setDebugDrawer(debugDrawer = new DebugDrawer());
		debugDrawer.lineRenderer.setProjectionMatrix(projMatrix);
		debugDrawer.setDebugMode(mode);
	}

	public int getDebugMode() {
		return (debugDrawer == null) ? 0 : debugDrawer.getDebugMode();
	}
}
