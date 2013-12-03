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

import fh.teamproject.input.InputHandling;
import fh.teamproject.interfaces.IPlayer;

public class Player extends CollisionEntity implements IPlayer {

	public float radius = 1f;
	public Vector3 direction = new Vector3(0, 0, 1f);
	public float velocity;
	boolean isGrounded = false;
	int state = 0;
	
	private float velocityY = 5.0f;


	//wird benoetigt, um die update() methode von InputHandling aufzurufen
	private InputHandling inputHandling;
	
	public Player() {
		super();

		inputHandling = new InputHandling(this);
		
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
		// TODO Auto-generated method stub
		super.update();
		
		//update() wird aufgerufen, um bei gedrückt-halten der Keys sich immer weiter zu bewegen
		inputHandling.update();
	}

	@Override
	public void accelerate(float amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void brake(float amount) {
		// TODO Auto-generated method stub
		if(this.getRigidBody().getLinearVelocity().z - amount > 10.0f){
			this.getRigidBody().applyForce(new Vector3(0, 0, -1000.0f), this.position);
		}
		
	}

	@Override
	public void slideLeft() {
		//this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getLinearVelocity().x + velocityX, this.getRigidBody().getLinearVelocity().y, this.getRigidBody().getLinearVelocity().z + velocityZ));
		this.getRigidBody().applyForce(new Vector3(1000, 0, 0), this.position);
		
	}

	@Override
	public void slideRight() {
		//this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getLinearVelocity().x - velocityX, this.getRigidBody().getLinearVelocity().y, this.getRigidBody().getLinearVelocity().z + velocityZ));
		this.getRigidBody().applyForce(new Vector3(-1000, 0, 0), this.position);
	}

	@Override
	public void jump() {
		this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getLinearVelocity().x, this.getRigidBody().getLinearVelocity().y + velocityY, this.getRigidBody().getLinearVelocity().z));
		//this.getRigidBody().applyForce(new Vector3(0, 1000, 0), this.position);
	}
}
