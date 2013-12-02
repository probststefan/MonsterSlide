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

	public float radius = 1f;
	public Vector3 direction = new Vector3(0, 0, 1f);
	public Vector3 linearVelocity = new Vector3();
	public float turnIntensity = 5;
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
		this.motionState = new PlayerMotionState(this);
		// Damit rutscht die Sphere nur noch und rollt nicht mehr.
		this.getRigidBody().setAngularFactor(0);
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
		this.linearVelocity = this.rigidBody.getLinearVelocity();
		this.direction = this.linearVelocity.nor();
	}

	@Override
	public void accelerate(float amount) {

	}

	@Override
	public void brake(float amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void slideLeft() {
		this.getRigidBody().applyImpulse(new Vector3(1, 0, 0).scl(this.turnIntensity),
				this.position);

	}

	@Override
	public void slideRight() {
		this.getRigidBody().applyImpulse(new Vector3(-1, 0, 0).scl(this.turnIntensity),
				this.position);
	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub

	}
}