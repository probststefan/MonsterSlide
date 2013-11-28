package fh.teamproject.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.interfaces.ISlidePart;

/**
 * Klasse stellt ein einzelnes Rutschelement dar. Es wird das Poolable-Interface
 * von libgdx implementiert.
 * 
 * @url https://code.google.com/p/libgdx/wiki/MemoryManagment
 * @author stefanprobst
 * 
 */
public class SlidePart extends CollisionEntity implements ISlidePart, Poolable {

	// Eckpunkte des SlidePart.
	private Vector3[] vertices;
	private float width = 20.0f;
	public boolean alive;

	public SlidePart() {
		super();

		vertices = new Vector3[4];
		alive = false;
	}

	/**
	 * Es wird das zu rendernde Modell und der Bullet Collision Shape erstellt.
	 * Vorher kann der SlidePart nicht genutzt werden.
	 */
	public void createSlidePart() {
		ModelBuilder builder = new ModelBuilder();

		Texture texture = new Texture(Gdx.files.internal("data/floor.jpg"), true);
		TextureAttribute textureAttr = new TextureAttribute(TextureAttribute.Diffuse,
				texture);

		// Das einzelne Rutschenelement zufaellig einfaerben.
		Random ran = new Random();
		Color color;
		switch (ran.nextInt(3)) {
		case 1:
			color = Color.BLUE;
			break;
		case 2:
			color = Color.GREEN;
			break;
		default:
			color = Color.YELLOW;
			break;
		}

		Material material = new Material(ColorAttribute.createDiffuse(color));
		material.set(textureAttr);

		this.vertices = new Vector3[4];
		this.vertices[0] = new Vector3(-10, 0, -10);
		this.vertices[1] = new Vector3(-10, 0, 10);
		this.vertices[2] = new Vector3(10, 0, 10);
		this.vertices[3] = new Vector3(10, 0, -10);

		Model m = builder.createRect(this.vertices[0].x, this.vertices[0].y,
				this.vertices[0].z, this.vertices[1].x, this.vertices[1].y,
				this.vertices[1].z, this.vertices[2].x, this.vertices[2].y,
				this.vertices[2].z, this.vertices[3].x, this.vertices[3].y,
				this.vertices[3].z, 0, 1, 0, material, Usage.Position | Usage.Normal
						| Usage.TextureCoordinates);

		this.instance = new ModelInstance(m);
		this.instance.transform.rotate(new Vector3(1.0f, 0, 0), 8);

		// Bullet-Eigenschaften setzen.
		btConvexHullShape convesHullShape = new btConvexHullShape();
		convesHullShape.addPoint(this.vertices[0]);
		convesHullShape.addPoint(this.vertices[1]);
		convesHullShape.addPoint(this.vertices[2]);
		convesHullShape.addPoint(this.vertices[3]);
		btCollisionShape colShape = convesHullShape;

		this.setCollisionShape(colShape);
		this.setEntityWorldTransform(this.instance.transform);
		this.setLocalInertia(new Vector3(0, 0, 0));
		this.createRigidBody();

		this.alive = true;
	}

	public void move(Vector3 tmpSlidePartPos, btDiscreteDynamicsWorld dynamicsWorld) {
		// Die gerenderte Plane bewegen.
		// this.instance.transform.translate(new Vector3(0, 0, 0));
		//
		this.instance.transform.translate(tmpSlidePartPos);

		// Die Bullet-Plane bewegen.
		dynamicsWorld.removeRigidBody(this.getRigidBody());

		this.getRigidBody().setCollisionFlags(
				this.getRigidBody().getCollisionFlags()
						| btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		this.getRigidBody().setActivationState(Collision.DISABLE_DEACTIVATION);

		this.getRigidBody().setWorldTransform(this.instance.transform);

		this.getRigidBody().setCollisionFlags(
				this.getRigidBody().getCollisionFlags()
						& ~(btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT));

		this.getRigidBody().forceActivationState(1);

		dynamicsWorld.addRigidBody(this.getRigidBody());
	}

	/**
	 * Liefert einen der vier Eckpunkte der Plane.
	 * 
	 * @param int position
	 * @return Vector3 vertice
	 */
	public Vector3 getVertice(int position) {
		if (position > this.vertices.length) {
			throw new IndexOutOfBoundsException("Der SlidePart hat nur "
					+ this.vertices.length + " Vertices!");
		}

		return this.vertices[position];
	}

	/**
	 * Setzt einen Punkt eines Vertice.
	 * 
	 * @param vertice
	 * @param position
	 */
	public void setVertice(Vector3 vertice, int position) {
		this.vertices[position] = vertice;
	}

	public boolean getAliveState() {
		return this.alive;
	}

	public void setAliveState(boolean alive) {
		this.alive = alive;
	}

	@Override
	public ModelInstance getModelInstance() {
		return this.instance;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWidth(float width) {
		this.width = width;
	}

	@Override
	public float getWidth() {
		return this.width;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}
}