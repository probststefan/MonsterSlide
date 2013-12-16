package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;

import fh.teamproject.controller.player.pc.InputHandling;
import fh.teamproject.interfaces.IPlayer;
import fh.teamproject.physics.PlayerMotionState;
import fh.teamproject.utils.debug.Debug;

public class Player extends CollisionEntity implements IPlayer {

	@Debug(name = "Position", isModifiable = false)
	public Vector3 position = new Vector3();

	@Debug(name = "Direction", isModifiable = false)
	public Vector3 direction = new Vector3(0, 0, 1);

	@Debug(name = "Linear Velocity", isModifiable = false)
	public Vector3 linearVelocity = new Vector3();

	@Debug(name = "Speed", isModifiable = false)
	public float speed = 0;

	@Debug(name = "Max Speed", isModifiable = true)
	public float MAX_SPEED = 50;

	@Debug(name = "Acceleration", isModifiable = true)
	public float acceleration = 16000f;

	@Debug(name = "Radius", isModifiable = false)
	public float radius = 1f;

	@Debug(name = "Is Grounded?", isModifiable = false)
	public boolean isGrounded = false;

	@Debug(name = "Turn Intensity", isModifiable = true)
	public float turnIntensity = 130000;

	@Debug(name = "Jump Amount", isModifiable = true)
	private float jumpAmount = 7.0f;

	// wird benoetigt, um die update() methode von InputHandling aufzurufen
	public InputHandling inputHandling;

	public Player() {
		super();

		inputHandling = new InputHandling(this);

		// Grafische Darstellung erstellen.
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		// Durchmesser der Sphere berechnen.
		float diameter = radius * 2;
		Model m = builder.createSphere(diameter, diameter, diameter, 16, 16, material,
				Usage.Position | Usage.Normal);

		instance = new ModelInstance(m, new Vector3(0f, 3.0f, 0f));

		// Bullet-Eigenschaften setzen.
		// setCollisionShape(new btSphereShape(radius));
		setCollisionShape(new btConvexHullShape(instance.model.meshes.get(0)
				.getVerticesBuffer(), instance.model.meshes.get(0).getNumVertices()));
		setLocalInertia(new Vector3(0, 0, 0));
		setMass(100.0f); // Masse der Sphere.
		createMotionState();
		createRigidBody();
		getRigidBody().setAngularFactor(0); // Damit rutscht die Sphere nur noch
		// und rollt nicht mehr.
		setEntityWorldTransform(instance.transform);

	}

	@Override
	public void createMotionState() {
		motionState = new PlayerMotionState(this);

	}

	@Override
	public void update() {
		super.update();

		// update() wird aufgerufen, um bei gedrueckt-halten der Keys sich immer
		// weiter zu bewegen
		inputHandling.update();

	}

	public void syncWithBullet() {
		linearVelocity.set(rigidBody.getLinearVelocity());
		speed = linearVelocity.len();
		direction.set(linearVelocity.cpy().nor());
	}

	@Override
	public void accelerate(float amount) {
		getRigidBody().applyCentralForce(
				direction.cpy().scl(acceleration * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void brake(float amount) {
		getRigidBody().applyCentralForce(
				direction.cpy().scl(-1f * acceleration * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void slideLeft() {
		getRigidBody().applyCentralForce(
				new Vector3(1, 0, 0).scl(turnIntensity * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void slideRight() {
		getRigidBody().applyCentralForce(
				new Vector3(-1, 0, 0).scl(turnIntensity * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void jump() {
		getRigidBody().applyForce(new Vector3(0, 1, 0).scl(50000), position);
	}

	@Override
	public String toString() {
		return "Player";
	}
}
