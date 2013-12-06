package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

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
	public float MAX_SPEED = 30;

	@Debug(name = "Acceleration", isModifiable = true)
	public float acceleration = 800f;

	@Debug(name = "Radius", isModifiable = false)
	public float radius = 1f;

	@Debug(name = "Is Grounded?", isModifiable = false)
	public boolean isGrounded = false;

	@Debug(name = "Turn Intensity", isModifiable = true)
	public float turnIntensity = 100000;

	@Debug(name = "Jump Amount", isModifiable = true)
	private float jumpAmount = 7.0f;

	// wird benoetigt, um die update() methode von InputHandling aufzurufen
	public InputHandling inputHandling;
	private AssetManager assets;

	public Player() {
		super();

		this.inputHandling = new InputHandling(this);

		// Grafische Darstellung erstellen.
		this.assets = new AssetManager();
		FileHandle handle = Gdx.files.internal("models/duck.obj");

		this.assets.load(handle.path(), Model.class);

		while (this.assets.update() == false) {

		}

		Model duckModel = this.assets.get(handle.path(), Model.class);

		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		// Durchmesser der Sphere berechnen.
		float diameter = this.radius * 2;
		Model m = builder.createSphere(diameter, diameter, diameter, 32, 32, material,
				Usage.Position | Usage.Normal);

		// this.instance = new ModelInstance(m, new Vector3(0f, 3.0f, 0f));
		this.instance = new ModelInstance(duckModel, new Vector3(0f, 3.0f, 0f));

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

		// update() wird aufgerufen, um bei gedrueckt-halten der Keys sich immer
		// weiter zu bewegen
		this.inputHandling.update();

	}

	public void syncWithBullet() {
		this.linearVelocity.set(this.rigidBody.getLinearVelocity());
		this.speed = this.linearVelocity.len();
		this.direction.set(this.linearVelocity.cpy().nor());
	}

	@Override
	public void accelerate(float amount) {

		// this.rigidBody.setLinearVelocity(this.direction.cpy().scl(1000.0f *
		// Gdx.graphics.getDeltaTime()));

		/*
		 * this.getRigidBody() .applyCentralForce( this.direction.cpy().scl(
		 * this.acceleration * Gdx.graphics.getDeltaTime()));
		 */

		this.getRigidBody().applyCentralForce(
				this.direction.cpy().scl(
						new Vector3(1, 1, 1).scl(this.turnIntensity
								* Gdx.graphics.getDeltaTime())));

	}

	@Override
	public void brake(float amount) {
		/*
		 * if (this.getRigidBody().getLinearVelocity().z > 0.0f) {
		 * this.getRigidBody().applyCentralForce( this.direction.cpy().scl( -1f
		 * * this.acceleration * Gdx.graphics.getDeltaTime())); }
		 */

		// um Begreunzugn der Bremse aufzuheben, einfach if() auskommentieren
		if (this.getRigidBody().getLinearVelocity().z > 5.0f) {
			this.getRigidBody().applyCentralForce(
					this.direction.cpy().scl(
							new Vector3(-1, -1, -1).scl(this.turnIntensity
									* Gdx.graphics.getDeltaTime())));
		}

	}

	@Override
	public void slideLeft() {
		this.getRigidBody()
				.applyCentralForce(
						new Vector3(1, 0, 0).scl(this.turnIntensity
								* Gdx.graphics.getDeltaTime()));

		// this.getRigidBody().applyForce(new Vector3(1, 0,
		// 0).scl(this.turnIntensity), this.position);
	}

	@Override
	public void slideRight() {
		this.getRigidBody().applyCentralForce(
				new Vector3(-1, 0, 0).scl(this.turnIntensity
						* Gdx.graphics.getDeltaTime()));

		// this.getRigidBody().applyForce(new Vector3(-1, 0,
		// 0).scl(this.turnIntensity), this.position);
	}

	@Override
	public void jump() {
		this.getRigidBody().applyForce(new Vector3(0, 1, 0).scl(50000), this.position);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Player";
	}
}
