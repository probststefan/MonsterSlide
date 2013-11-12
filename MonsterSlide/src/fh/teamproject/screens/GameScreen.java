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
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw.DebugDrawModes;

import fh.teamproject.entities.Plane;
import fh.teamproject.entities.Sphere;
import fh.teamproject.entities.World;
import fh.teamproject.utils.DebugDrawer;

public class GameScreen implements Screen {
	// Den Debug-Modus von Bullet ein- und ausschalten.
	private boolean debuggerOn = false;
	public DebugDrawer debugDrawer = null;

	PerspectiveCamera camera;
	CameraInputController controller;

	ModelBatch batch;
	Environment lights;

	World world;
	Sphere sphere;
	Plane plane;

	public GameScreen() {
		this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.camera.translate(0f, 0f, 10f);
		// this.camera.lookAt(0, 0, 0);
		this.controller = new CameraInputController(this.camera);
		this.world = new World();
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

		// Objekte zur Bullet-Welt hinzufuegen.
		world.addRigidBody(this.plane.getRigidBody());
		world.addRigidBody(this.sphere.getRigidBody());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		if (debuggerOn) {
			// Stellt die Kollisionsobjekte von Bullet grafisch dar.
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
			setDebugMode(DebugDrawModes.DBG_DrawWireframe, camera.combined);
			Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

			if (debugDrawer != null && debugDrawer.getDebugMode() > 0) {
				debugDrawer.begin();
				world.getWorld().debugDrawWorld();
				debugDrawer.end();
			}
		}

		/* UPDATE */
		this.camera.lookAt(this.sphere.position);
		this.camera.update();

		// Bullet update.
		if (world.getWorld() instanceof btDynamicsWorld) {
			((btDynamicsWorld) world.getWorld()).stepSimulation(
					Gdx.graphics.getDeltaTime(), this.world.getMaxSubSteps(),
					this.world.getFixedTimeStep());
		}

		/* RENDER */
		this.batch.begin(this.camera);
		this.batch.render(this.sphere.instance, this.lights);
		this.batch.render(this.plane.instance, this.lights);
		this.batch.end();

		// Status der Sphere aktualisieren.
		this.sphere.getRigidBody().getMotionState()
				.getWorldTransform(this.sphere.instance.transform);

		// Die Position der Sphere aktualisieren.
		this.sphere.instance.transform.getTranslation(this.sphere.position);
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
			world.getWorld().setDebugDrawer(debugDrawer = new DebugDrawer());
		debugDrawer.lineRenderer.setProjectionMatrix(projMatrix);
		debugDrawer.setDebugMode(mode);
	}

	public int getDebugMode() {
		return (debugDrawer == null) ? 0 : debugDrawer.getDebugMode();
	}
}