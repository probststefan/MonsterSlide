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
	public Vector3 direction = new Vector3(0, 0, 1);
	public float velocity;
	boolean isGrounded = false;
	int state = 0;
	
	private float velocityX = 0.1f;
	private float velocityY = 1.0f;
	private float velocityZ = 0.0f;


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
		this.setMass(1.0f); // Masse der Sphere.
		this.createRigidBody();
		this.getRigidBody().getMotionState().getWorldTransform(this.instance.transform);

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		//if(Gdx.input.isKeyPressed(Input.Keys.A)){
			//this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getLinearVelocity().x + velocityX, this.getRigidBody().getLinearVelocity().y, this.getRigidBody().getLinearVelocity().z + velocityZ));
		//}
		
	}

	@Override
	public void accelerate(float amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void brake(float amount) {
		// TODO Auto-generated method stub
		if(this.getRigidBody().getLinearVelocity().z - amount > 0.0f){
			this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getLinearVelocity().x, this.getRigidBody().getLinearVelocity().y, this.getRigidBody().getLinearVelocity().z - amount));

		}
	}

	@Override
	public void slideLeft() {
		// TODO Auto-generated method stub
		this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getLinearVelocity().x + velocityX, this.getRigidBody().getLinearVelocity().y, this.getRigidBody().getLinearVelocity().z + velocityZ));
		//this.getRigidBody().getLinearVelocity().x += velocityX;
		
		
	}

	@Override
	public void slideRight() {
		// TODO Auto-generated method stub
		//this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getVelocityInLocalPoint(this.getPosition()).x, 0.0f, 0.0f));
		//this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getVelocityInLocalPoint(this.getRigidBody().getCenterOfMassPosition()).x - 1.0f, this.getRigidBody().getVelocityInLocalPoint(this.getRigidBody().getCenterOfMassPosition()).y, this.getRigidBody().getVelocityInLocalPoint(this.getRigidBody().getCenterOfMassPosition()).z));
				
		this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getLinearVelocity().x - velocityX, this.getRigidBody().getLinearVelocity().y, this.getRigidBody().getLinearVelocity().z + velocityZ));
		
	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		this.getRigidBody().setLinearVelocity(new Vector3(this.getRigidBody().getLinearVelocity().x, this.getRigidBody().getLinearVelocity().y + velocityY, this.getRigidBody().getLinearVelocity().z));
	}
}