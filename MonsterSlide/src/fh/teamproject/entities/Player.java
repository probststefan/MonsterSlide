package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

	public float radius = 1f;
	public Vector3 direction = new Vector3(0, 0, 1f);
	public float velocity;
	boolean isGrounded = false;
	int state = 0;

	public Player() {
		super();

		// Grafische Darstellung erstellen.
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
		this.setMass(100.0f); // Masse der Sphere.
		this.createRigidBody();
		this.getRigidBody().getMotionState().getWorldTransform(this.instance.transform);

		// Damit rutscht die Sphere nur noch und rollt nicht mehr.
		this.getRigidBody().setAngularFactor(0);
	}

	@Override
	public void update() {
		super.update();
		this.direction = this.rigidBody.getLinearVelocity().nor();
		// Gdx.app.log("Player", "" + this.direction);
		// TODO In Controller-Klasse bauen!
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.getRigidBody().applyForce(new Vector3(1000, 0, 0), this.position);
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.getRigidBody().applyForce(new Vector3(-1000, 0, 0), this.position);
		} else {
			this.getRigidBody().applyForce(new Vector3(0, 0, 0), this.position);
		}
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