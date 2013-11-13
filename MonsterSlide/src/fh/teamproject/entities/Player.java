package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

public class Player extends CollisionEntity implements InputProcessor {

	public Vector3 position = new Vector3();
	public ModelInstance instance;
	public float radius = 1f;
	public Vector3 direction;
	public float velocity;
	boolean isGrounded = false;
	int state = 0;

	public Player() {
		super();

		// Grafische Darstellung erstellen.
		this.direction = new Vector3(0f, -1f, 0f);
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		// Durchmesser der Sphere berechnen.
		float diameter = this.radius * 2;
		Model m = builder.createSphere(diameter, diameter, diameter, 16, 16, material,
				Usage.Position | Usage.Normal);
		this.instance = new ModelInstance(m, new Vector3(0f, 20.0f, 0f));

		// Bullet-Eigenschaften setzen.
		this.setCollisionShape(new btSphereShape(this.radius));
		this.setEntityWorldTransform(this.instance.transform);
		this.setLocalInertia(new Vector3(0, 0, 0));
		this.setMass(1.0f); // Masse der Sphere.
		this.createRigidBody();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.DOWN) {
			this.instance.transform.translate(this.direction.cpy().scl(this.velocity));
		}
		if (keycode == Keys.UP) {
			this.instance.transform.translate(this.direction.cpy()
					.scl(-1 * this.velocity));

		}
		this.instance.transform.getTranslation(this.position);
		Gdx.app.log("Sphere", "Sphere position: " + this.position);

		if (keycode == Keys.SPACE) {
			state = (state + 1) % 3;
			Vector3 dir = new Vector3();
			switch (state) {
			case 0:
				dir.x = 1f;
				break;
			case 1:
				dir.y = 1f;
				break;
			case 2:
				dir.z = 1f;
				break;
			default:
				break;
			}
			this.direction = dir;
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

	@Override
	public Vector3 getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelInstance getModelInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}