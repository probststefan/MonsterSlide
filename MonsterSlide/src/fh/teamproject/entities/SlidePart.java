package fh.teamproject.entities;

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
	private float[] vertices;
	private float width = 20.0f;
	private ModelBuilder builder;
	private Material material;
	private btConvexHullShape convesHullShape;

	public SlidePart() {
		super();
		vertices = new float[12];
	}

	/**
	 * Es wird das zu rendernde Modell und der Bullet Collision Shape erstellt.
	 * Vorher kann der SlidePart nicht genutzt werden.
	 */
	public void createSlidePart() {
		builder = new ModelBuilder();

		Texture texture = new Texture(Gdx.files.internal("data/floor.jpg"), true);
		TextureAttribute textureAttr = new TextureAttribute(TextureAttribute.Diffuse,
				texture);

		// Das einzelne Rutschenelement zufaellig einfaerben.
		Color color;
		color = new Color();
		color.set((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);

		material = new Material(ColorAttribute.createDiffuse(color));
		material.set(textureAttr);

		// this.vertices = new Vector3[4];
		this.vertices[0] = -10;
		this.vertices[1] = 0;
		this.vertices[2] = -10;

		this.vertices[3] = -10;
		this.vertices[4] = 0;
		this.vertices[5] = 10;

		this.vertices[6] = 10;
		this.vertices[7] = 0;
		this.vertices[8] = 10;

		this.vertices[9] = 10;
		this.vertices[10] = 0;
		this.vertices[11] = -10;

		/*
		 * this.vertices[0] = new Vector3(-10, 0, -10); this.vertices[1] = new
		 * Vector3(-10, 0, 10); this.vertices[2] = new Vector3(10, 0, 10);
		 * this.vertices[3] = new Vector3(10, 0, -10);
		 */

		Model m = builder.createRect(this.vertices[0], this.vertices[1],
				this.vertices[2], this.vertices[3], this.vertices[4], this.vertices[5],
				this.vertices[6], this.vertices[7], this.vertices[8], this.vertices[9],
				this.vertices[10], this.vertices[11], 0, 1, 0, material, Usage.Position
						| Usage.Normal | Usage.TextureCoordinates);

		this.instance = new ModelInstance(m);
		this.instance.transform.rotate(new Vector3(1.0f, 0, 0), 0);

		// Bullet-Eigenschaften setzen.
		btConvexHullShape convesHullShape = new btConvexHullShape();
		convesHullShape.addPoint(new Vector3(this.vertices[0], this.vertices[1],
				this.vertices[2]));
		convesHullShape.addPoint(new Vector3(this.vertices[3], this.vertices[4],
				this.vertices[5]));
		convesHullShape.addPoint(new Vector3(this.vertices[6], this.vertices[7],
				this.vertices[8]));
		convesHullShape.addPoint(new Vector3(this.vertices[9], this.vertices[10],
				this.vertices[11]));
		btCollisionShape colShape = convesHullShape;

		this.setCollisionShape(colShape);
		this.setEntityWorldTransform(this.instance.transform);
		this.setLocalInertia(new Vector3(0, 0, 0));
		this.createRigidBody();
	}

	// public void move(Vector3 tmpSlidePartPos, btDiscreteDynamicsWorld
	// dynamicsWorld) {
	public void move(Vector3[] startPoints, Vector3[] endPoints,
			btDiscreteDynamicsWorld dynamicsWorld) {
		// Die gerenderte Plane bewegen.
		// this.instance.transform.translate(tmpSlidePartPos);
		float[] tmpVertices = new float[20 + 12];
		this.instance.model.meshes.get(0).getVertices(tmpVertices);

		// Die Indices sind etwas merkwuerdig. Zwischendrin sind immer noch 5
		// Felder fuer die Farbe.
		tmpVertices[0] = startPoints[0].x;
		tmpVertices[1] = startPoints[0].y;
		tmpVertices[2] = startPoints[0].z;

		tmpVertices[8] = endPoints[0].x;
		tmpVertices[9] = endPoints[0].y;
		tmpVertices[10] = endPoints[0].z;

		tmpVertices[16] = endPoints[1].x;
		tmpVertices[17] = endPoints[1].y;
		tmpVertices[18] = endPoints[1].z;

		tmpVertices[24] = startPoints[1].x;
		tmpVertices[25] = startPoints[1].y;
		tmpVertices[26] = startPoints[1].z;

		this.instance.model.meshes.get(0).setVertices(tmpVertices);

		this.vertices[0] = tmpVertices[0];
		this.vertices[1] = tmpVertices[1];
		this.vertices[2] = tmpVertices[2];

		this.vertices[3] = tmpVertices[8];
		this.vertices[4] = tmpVertices[9];
		this.vertices[5] = tmpVertices[10];

		this.vertices[6] = tmpVertices[16];
		this.vertices[7] = tmpVertices[17];
		this.vertices[8] = tmpVertices[18];

		this.vertices[9] = tmpVertices[24];
		this.vertices[10] = tmpVertices[25];
		this.vertices[11] = tmpVertices[26];

		// Die Bullet-Plane bewegen.
		dynamicsWorld.removeRigidBody(this.getRigidBody());

		this.getRigidBody().setCollisionFlags(
				this.getRigidBody().getCollisionFlags()
						| btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		this.getRigidBody().setActivationState(Collision.DISABLE_DEACTIVATION);

		// TEST START
		// Die CollisionShape von Bullet an die Gerenderte anpassen.
		if (convesHullShape != null) {
			convesHullShape.dispose();
		}

		convesHullShape = new btConvexHullShape();
		convesHullShape.addPoint(startPoints[0]);
		convesHullShape.addPoint(endPoints[0]);
		convesHullShape.addPoint(endPoints[1]);
		convesHullShape.addPoint(startPoints[1]);
		btCollisionShape colShape = convesHullShape;

		this.getRigidBody().setCollisionShape(colShape);
		// TEST ENDE

		// this.getRigidBody().setWorldTransform(this.instance.transform);

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
	public float getVertice(int position) {
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
	public void setVertice(float vertice, int position) {
		this.vertices[position] = vertice;
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