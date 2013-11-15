package fh.teamproject.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

import fh.teamproject.interfaces.IPlayer;

public class Player extends CollisionEntity implements IPlayer {

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

	public void update() {
		// Status der Sphere aktualisieren.
		this.getRigidBody().getMotionState().getWorldTransform(this.instance.transform);

		// Die Position der Sphere aktualisieren.
		this.instance.transform.getTranslation(this.position);
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

	@Override
	public void accelerate(float amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void brake(float amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void slideLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void slideRight() {
		// TODO Auto-generated method stub

	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub

	}
}