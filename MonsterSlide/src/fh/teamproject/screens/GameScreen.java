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

import fh.teamproject.entities.Plane;
import fh.teamproject.entities.Sphere;
import fh.teamproject.physics.Collision;
import fh.teamproject.utils.Skybox;

public class GameScreen implements Screen {

	PerspectiveCamera camera;
	CameraInputController controller;
	Skybox skybox;

	ModelBatch batch;
	Environment lights;

	Sphere sphere;
	Plane plane;
	Collision collision;

	public GameScreen() {

		this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.camera.translate(0f, 0f, 10f);
		this.camera.lookAt(0, 0, 0);
		this.controller = new CameraInputController(this.camera);
		this.sphere = new Sphere();
		this.plane = new Plane();

		this.skybox = new Skybox();
		this.batch = new ModelBatch();

		this.lights = new Environment();
		this.lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f,
				1f));
		this.lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 0f, 10f));

		this.collision = new Collision();

		InputMultiplexer inputMul = new InputMultiplexer();
		inputMul.addProcessor(this.sphere);
		inputMul.addProcessor(this.controller);

		Gdx.input.setInputProcessor(inputMul);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		/* UPDATE */
		this.camera.update();
		this.collision.intersectSphereToPlane(this.sphere, this.plane);
		/* RENDER */
		this.batch.begin(this.camera);
		// this.batch.render(this.skybox.box, this.lights);
		this.batch.render(this.sphere.instance);
		this.batch.render(this.plane.instance);
		this.batch.end();
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

}
