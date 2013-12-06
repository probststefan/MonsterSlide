package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
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
	private float width = 10.0f;
	private float[] tmpVertices;
	private ModelBuilder builder;
	private Material material;

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
		tmpVertices = new float[20 + 12];

		Texture texture = new Texture(Gdx.files.internal("data/snow.jpg"), true);
		TextureAttribute textureAttr = new TextureAttribute(TextureAttribute.Diffuse,
				texture);

		// Das einzelne Rutschenelement zufaellig einfaerben.
		Color color;
		color = new Color();
		color.set((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);

		// material = new Material(ColorAttribute.createDiffuse(color));
		material = new Material();
		material.set(textureAttr);

		// Ein Model erstellen, welches dann spaeter durch die Welt
		// verschoben wird.
		Model m = builder.createRect(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
				material, Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		this.instance = new ModelInstance(m);
	}

	public void move(Vector3[] startPoints, Vector3[] endPoints,
			btDiscreteDynamicsWorld dynamicsWorld) {
		// Die gerenderte Plane bewegen.
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

		this.setVertice(tmpVertices[0], 0);
		this.setVertice(tmpVertices[1], 1);
		this.setVertice(tmpVertices[2], 2);

		this.setVertice(tmpVertices[8], 3);
		this.setVertice(tmpVertices[9], 4);
		this.setVertice(tmpVertices[10], 5);

		this.setVertice(tmpVertices[16], 6);
		this.setVertice(tmpVertices[17], 7);
		this.setVertice(tmpVertices[18], 8);

		this.setVertice(tmpVertices[24], 9);
		this.setVertice(tmpVertices[25], 10);
		this.setVertice(tmpVertices[26], 11);
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