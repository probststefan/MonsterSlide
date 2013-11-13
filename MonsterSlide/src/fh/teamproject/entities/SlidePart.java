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
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;

import fh.teamproject.interfaces.ISlidePart;

public class SlidePart extends CollisionEntity implements ISlidePart {

	public ModelInstance instance;

	public SlidePart() {
		super();

		ModelBuilder builder = new ModelBuilder();

		Texture texture = new Texture(Gdx.files.internal("data/floor.jpg"), true);
		TextureAttribute textureAttr = new TextureAttribute(TextureAttribute.Diffuse,
				texture);

		Material material = new Material(ColorAttribute.createDiffuse(Color.BLUE));
		material.set(textureAttr);

		Model m = builder.createRect(-10, 0, -10, -10, 0, 10, 10, 0, 10, 10, 0, -10, 0,
				1, 0, material, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		this.instance = new ModelInstance(m);

		// this.instance.transform.scl(4);
		this.instance.transform.rotate(new Vector3(1.0f, 1.0f, 0), 5);
		this.instance.transform.translate(0, 0, 0);

		// Bullet-Eigenschaften setzen.
		btConvexHullShape convesHullShape = new btConvexHullShape();
		convesHullShape.addPoint(new Vector3(-10, 0, -10));
		convesHullShape.addPoint(new Vector3(-10, 0, 10));
		convesHullShape.addPoint(new Vector3(10, 0, 10));
		convesHullShape.addPoint(new Vector3(10, 0, -10));
		btCollisionShape colShape = convesHullShape;

		this.setCollisionShape(colShape);
		this.setEntityWorldTransform(this.instance.transform);
		this.setLocalInertia(new Vector3(0, 0, 0));
		this.createRigidBody();
	}

	@Override
	public Vector3 getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelInstance getModelInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWidth(float width) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
}