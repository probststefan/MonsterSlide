package fh.teamproject.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Sphere implements InputProcessor {

	Vector3 position;
	public ModelInstance instance;
	float radius;
	Vector3 direction;
	float velocity;


	public Sphere() {
		this.radius = 2f;
		this.position = new Vector3(0f, 3f, 0f);
		this.direction = new Vector3(0f, -1f, 0f);
		this.velocity = 0.5f;

		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		Model m = builder.createSphere(2f, 2f, 2f, 10, 10, material, Usage.Position);
		this.instance = new ModelInstance(m);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.DOWN) {

			this.position.add(this.direction.cpy().scl(this.velocity));
		}
		if (keycode == Keys.UP) {

			this.position.add(this.direction.cpy().scl(-1 * this.velocity));
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
