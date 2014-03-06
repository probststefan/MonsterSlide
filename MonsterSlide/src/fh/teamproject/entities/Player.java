package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

import fh.teamproject.controller.player.pc.InputHandling;
import fh.teamproject.interfaces.IPlayer;
import fh.teamproject.physics.PhysixBody;
import fh.teamproject.physics.PhysixBodyDef;
import fh.teamproject.physics.callbacks.PlayerMotionState;
import fh.teamproject.physics.callbacks.PlayerTickCallback;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.debug.Debug;

public class Player extends CollisionEntity implements IPlayer {

	public static final int PLAYER_FLAG = 2;

	@Debug(name = "Position", isModifiable = false)
	public Vector3 position = new Vector3(-5.0f, 0.0f, -5.0f);

	@Debug(name = "Direction", isModifiable = false)
	public Vector3 direction = new Vector3(0, 0, 1);

	@Debug(name = "Linear Velocity", isModifiable = false)
	public Vector3 linearVelocity = new Vector3();

	@Debug(name = "Speed", isModifiable = false)
	public float speed;

	@Debug(name = "Max Speed", isModifiable = true)
	public float MAX_SPEED;

	@Debug(name = "Acceleration", isModifiable = true)
	public float ACCELERATION;

	@Debug(name = "Radius", isModifiable = false)
	public float radius = 1f;

	@Debug(name = "Is Grounded?", isModifiable = false)
	public boolean isGrounded = false;

	@Debug(name = "Turn Intensity", isModifiable = true)
	public float TURN_INTENSITIY;

	@Debug(name = "Jump Amount", isModifiable = true)
	private float jumpAmount = 7.0f;

	// wird benoetigt, um die update() methode von InputHandling aufzurufen
	public InputHandling inputHandling;

	public Player(World world) {
		super(world);
		inputHandling = new InputHandling(this);
		buildPlayer();
		this.ACCELERATION = GameScreen.settings.PLAYER_ACCEL;
		this.TURN_INTENSITIY = GameScreen.settings.PLAYER_TURN_INTENSITY;
		this.MAX_SPEED = GameScreen.settings.PLAYER_MAX_SPEED;
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
				direction.cpy().scl(ACCELERATION * Gdx.graphics.getDeltaTime()));

	}

	@Override
	public void brake(float amount) {
		getRigidBody().applyCentralForce(
				direction.cpy().scl(-1f * ACCELERATION * Gdx.graphics.getDeltaTime()));

	}

	@Override
	public void slideLeft() {
		Vector3 dir = direction.cpy().crs(Vector3.Y).scl(-1f);
		getRigidBody().applyCentralForce(
				dir.scl(TURN_INTENSITIY * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void slideRight() {
		Vector3 dir = direction.cpy().crs(Vector3.Y);
		getRigidBody().applyCentralForce(
				dir.scl(TURN_INTENSITIY * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void jump() {
		getRigidBody().applyForce(new Vector3(0, 1, 0).scl(50000), position);
	}

	@Override
	public String toString() {
		return "Player";
	}

	public void resetAt(Vector3 position) {
		Matrix4 transform = getModelInstance().transform;
		transform.setToTranslation(position);
		transform.rotate(Vector3.Z, 90);

		getRigidBody().setWorldTransform(transform);
		rigidBody.setLinearVelocity(Vector3.X);
		rigidBody.setAngularVelocity(new Vector3());
	}

	private void buildPlayer() {
		// Grafische Darstellung erstellen
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(new Color(1f, 1f,
				1f, 1f)));

		// Durchmesser der Sphere berechnen.
		float height = radius * 2f;
		Model m = builder.createCapsule(radius, height * 2, 16, material, Usage.Position
				| Usage.Normal);
		instance = new ModelInstance(m, position);
		instance.userData = "player";
	}

	@Override
	public Vector3 getDirection() {
		return direction;
	}

	@Override
	public void setGrounded(boolean grounded) {
		this.isGrounded = grounded;
	}

	@Override
	public void initPhysix() {
		float height = radius * 2f; // FIXME: aus buildPlayer kopiert! magic
									// number
		btCapsuleShape collisionShape = new btCapsuleShape(radius, height);
		setCollisionShape(collisionShape);
		setLocalInertia(new Vector3(0, 0, 0));
		setMass(GameScreen.settings.PLAYER_MASS);
		this.motionState = new PlayerMotionState(this);

		PhysixBodyDef bodyDef = new PhysixBodyDef(world.getPhysixManager(), mass,
				motionState, collisionShape);
		// Damit rutscht die Sphere nur noch und rollt nicht mehr.
		bodyDef.setFriction(0.1f);
		bodyDef.setRestitution(0f);

		PhysixBody body = bodyDef.create();
		body.setAngularFactor(new Vector3(0, 1.0f, 0));
		body.setContactCallbackFlag(Player.PLAYER_FLAG);
		body.setContactCallbackFilter(Slide.SLIDE_FLAG);
		// Wird gebraucht um die Kollisionen mit den Coins zu filtern.
		body.setCollisionFlags(body.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

		PlayerTickCallback playerCallback = new PlayerTickCallback(this);
		playerCallback.attach(world.getPhysixManager().getWorld(), false);

		rigidBody = body;
		setEntityWorldTransform(instance.transform);
	}

	@Override
	public void initGraphix() {
		// TODO Auto-generated method stub

	}
}