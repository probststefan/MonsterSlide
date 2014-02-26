package fh.teamproject.entities;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.physics.PhysixBodyDef;

public class Coin extends CollisionEntity implements Poolable {

	private float radius = 1f;
	private World world;

	public Coin(World world) {
		this.position = new Vector3(0, 2.0f, 0);
		this.world = world;
		this.createModelInstance();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	public void setToPosition(Vector3 position) {
		this.position = position;
		instance.transform.setToTranslation(position);
		this.getRigidBody().translate(position);
	}

	/**
	 * Erstellen des visuellen Repraesentation.
	 */
	private void createModelInstance() {
		Model m = this.world.gameScreen.getAssets().get("model/coin.g3db", Model.class);
		instance = new ModelInstance(m, position);
		instance.transform.rotate(new Vector3(1.0f, 0, 0), 90.0f);
		instance.transform.scl(1f);
	}

	/**
	 * Erstellt das physikalische Kollisionsmodell. Es wird eine Sphere als
	 * Shape benutzt, weil dieses, fuer die Berechnung, am guenstigsten ist.
	 */
	@Override
	public void initPhysix() {
		btCollisionShape collisionShape = new btSphereShape(this.radius);
		setCollisionShape(collisionShape);
		rigidBody = new PhysixBodyDef(world.getPhysixManager(), mass, motionState,
				collisionShape).create();
		setEntityWorldTransform(this.instance.transform);
		rigidBody.setContactCallbackFilter(Player.PLAYER_FLAG);

		rigidBody.setCollisionFlags(rigidBody.getCollisionFlags()
				| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

		rigidBody.setUserValue(this.getID());
	}
}