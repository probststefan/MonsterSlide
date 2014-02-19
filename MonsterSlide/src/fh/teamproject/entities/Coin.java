package fh.teamproject.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Coin extends CollisionEntity implements Poolable {

	private float radius = 1f;

	public Coin() {
		this.position = new Vector3(0, 2.0f, 0);
		this.createModelInstance();
		this.createCollisionShape();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	/**
	 * Erstellen des visuellen Repraesentation.
	 */
	private void createModelInstance() {
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(new Color(1f, 1f,
				1f, 1f)));
		// Durchmesser der Sphere berechnen.
		float diameter = radius * 2;
		float height = 0.5f;
		Model m = builder.createCylinder(diameter, height, diameter, 16, material,
				Usage.Position | Usage.Normal);

		instance = new ModelInstance(m, position);
		instance.transform.rotate(new Vector3(1.0f, 0, 0), 90.0f);
	}

	/**
	 * Erstellt das physikalische Kollisionsmodell. Es wird eine Sphere als
	 * Shape benutzt, weil dieses, fuer die Berechnung, am guenstigsten ist.
	 */
	private void createCollisionShape() {
		btCollisionShape collisionShape = new btSphereShape(this.radius);
		setCollisionShape(collisionShape);
		setEntityWorldTransform(this.instance.transform);
		createRigidBody();
		this.getRigidBody().setContactCallbackFilter(Player.PLAYER_FLAG);
		this.getRigidBody().setCollisionFlags(
				this.getRigidBody().getCollisionFlags()
						| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

	}
}
