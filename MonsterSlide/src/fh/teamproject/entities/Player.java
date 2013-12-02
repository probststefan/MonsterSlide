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
import fh.teamproject.physics.PlayerMotionState;

public class Player extends CollisionEntity implements IPlayer {

	public Vector3 position = new Vector3();
	public Vector3 direction = new Vector3(0, 0, 1);
	public Vector3 linearVelocity = new Vector3();
	public float radius = 1f;
	public float turnIntensity = 300;
	public boolean isGrounded = false;
	public int state = 0;
	public float acceleration = 1f;

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
		this.setLocalInertia(new Vector3(0, 0, 0));
		this.setMass(100.0f); // Masse der Sphere.
		this.createMotionState();
		this.createRigidBody();
		// this.getRigidBody().getMotionState().getWorldTransform(this.instance.transform);
		// Damit rutscht die Sphere nur noch und rollt nicht mehr.
		this.getRigidBody().setAngularFactor(0);
		this.setEntityWorldTransform(this.instance.transform);
	}

	@Override
	public void createMotionState() {
		this.motionState = new PlayerMotionState(this);

	}

	@Override
	public void update() {
		super.update();
		// TODO In Controller-Klasse bauen!
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.slideLeft();
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.slideRight();
		}
	}

	public void syncWithBullet() {
		this.linearVelocity.set(this.rigidBody.getLinearVelocity());
		this.direction.set(this.linearVelocity.cpy().nor());
	}

	@Override
	public void accelerate(float amount) {
		this.getRigidBody()
				.applyCentralForce(this.direction.cpy().scl(this.acceleration));

		this.rigidBody.setLinearVelocity(this.direction.cpy().scl(
				this.acceleration * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void brake(float amount) {
		// this.getRigidBody().applyCentralForce(
		// this.direction.cpy().scl(-this.acceleration));

		this.rigidBody.setLinearVelocity(this.direction.cpy().scl(
				-this.acceleration * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void slideLeft() {
		this.getRigidBody().applyCentralForce(
				new Vector3(1, 0, 0).scl(this.turnIntensity));

	}

	@Override
	public void slideRight() {
		this.getRigidBody().applyCentralForce(
				new Vector3(-1, 0, 0).scl(this.turnIntensity));
	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub

	}
}