package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.controller.player.pc.InputHandling;
import fh.teamproject.interfaces.IPlayer;
import fh.teamproject.physics.PlayerMotionState;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.debug.Debug;

public class Player extends CollisionEntity implements IPlayer {

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
	public float acceleration;

	@Debug(name = "Radius", isModifiable = false)
	public float radius = 1f;

	@Debug(name = "Is Grounded?", isModifiable = false)
	public boolean isGrounded = false;

	@Debug(name = "Turn Intensity", isModifiable = true)
	public float turnIntensity;

	@Debug(name = "Jump Amount", isModifiable = true)
	private float jumpAmount = 7.0f;

	// wird benoetigt, um die update() methode von InputHandling aufzurufen
	public InputHandling inputHandling;

	private ParticleEffect effect;
	private Array<ParticleEmitter> emitters;
	private Vector3 particlePos;

	public Player() {
		super();
		buildPlayer();
	}

	public Player(Vector3 position) {
		super();
		this.position = position;
		buildPlayer();
		acceleration = GameScreen.settings.PLAYER_ACCEL;
		turnIntensity = GameScreen.settings.PLAYER_TURN_INTENSITY;
		MAX_SPEED = GameScreen.settings.PLAYER_MAX_SPEED;

		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("data/particle.p"), Gdx.files.internal("data"));
		effect.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		emitters = new Array(effect.getEmitters());
		effect.getEmitters().clear();
		effect.getEmitters().add(emitters.get(0));
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

		particlePos = position.cpy();
		GameScreen.camManager.getActiveCamera().project(particlePos);
		effect.setPosition(this.particlePos.x, this.particlePos.y);
	}

	SpriteBatch batch = new SpriteBatch();

	public void render() {
		batch.setProjectionMatrix(GameScreen.camManager.getActiveCamera().combined);
		float delta = Gdx.graphics.getDeltaTime();
		batch.begin();
		effect.draw(batch, delta);
		batch.end();
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
		Vector3 dir = direction.cpy().crs(Vector3.Y).scl(-1f);
		getRigidBody().applyCentralForce(
				dir.scl(turnIntensity * Gdx.graphics.getDeltaTime()));
	}

	@Override
	public void slideRight() {
		Vector3 dir = direction.cpy().crs(Vector3.Y);
		getRigidBody().applyCentralForce(
				dir.scl(turnIntensity * Gdx.graphics.getDeltaTime()));
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
		getRigidBody().setWorldTransform(transform);
		rigidBody.setLinearVelocity(Vector3.X);
		rigidBody.setAngularVelocity(new Vector3());
	}

	private void buildPlayer() {
		inputHandling = new InputHandling(this);

		// Grafische Darstellung erstellen
		ModelBuilder builder = new ModelBuilder();

		Material material = new Material(ColorAttribute.createDiffuse(new Color(1f, 1f,
				1f, 1f)));
		// Durchmesser der Sphere berechnen.
		float diameter = radius * 2;
		float height = 0.5f;
		Model m = builder.createCylinder(diameter, height, diameter, 16, material,
				Usage.Position | Usage.Normal);

		instance = new ModelInstance(m, position);

		// Bullet-Eigenschaften setzen.
		// setCollisionShape(new btCylinderShape(new Vector3(radius, height,
		// radius)));
		setCollisionShape(new btCapsuleShape(radius, height * 3.0f));

		setLocalInertia(new Vector3(0, 0, 0));
		setMass(GameScreen.settings.PLAYER_MASS); // Masse der Sphere.
		createMotionState();
		createRigidBody();
		// Damit rutscht die Sphere nur noch und rollt nicht mehr.
		getRigidBody().setAngularFactor(new Vector3(0, 1.0f, 0));
		rigidBody.setFriction(0.1f);
		rigidBody.setRestitution(0f);
		// rigidBody.setMassProps(10.0f, new Vector3(0, 0, 0));

		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		// rigidBody.setInvInertiaDiagLocal(new Vector3());

		setEntityWorldTransform(instance.transform);
		Gdx.app.log("player", "Masse: " + rigidBody.getInvInertiaTensorWorld());
	}

	@Override
	public Vector3 getDirection() {
		return direction;
	}
}
