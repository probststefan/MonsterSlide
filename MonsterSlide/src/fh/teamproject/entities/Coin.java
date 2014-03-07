package fh.teamproject.entities;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.physics.PhysixBody;
import fh.teamproject.physics.PhysixBodyDef;
import fh.teamproject.physics.callbacks.MotionState;

public class Coin extends CollisionEntity implements Poolable {

	private float radius = 1f;

	public Coin(World world) {
		super(world);
		initGraphix();
		initPhysix();
		setPosition(new Vector3(0, 2.0f, 0));
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	public void setToPosition(Vector3 position) {
		setPosition(position);
		instance.transform.setToTranslation(position);
		this.getRigidBody().translate(position);
	}

	/**
	 * Erstellt das physikalische Kollisionsmodell. Es wird eine Sphere als
	 * Shape benutzt, weil dieses, fuer die Berechnung, am guenstigsten ist.
	 */
	@Override
	public void initPhysix() {
		btCollisionShape collisionShape = new btSphereShape(this.radius);
		PhysixBodyDef rigidBodyDef = new PhysixBodyDef(world.getPhysixManager(), mass,
				new MotionState(instance.transform), collisionShape);

		btTransform btTransform = new btTransform(instance.transform);
		rigidBodyDef.setStartWorldTransform(btTransform);

		rigidBody = rigidBodyDef.create();
		rigidBody.setContactCallbackFilter(Player.PLAYER_FLAG);

		rigidBody.setUserValue(this.getID());

		rigidBodyDef.dispose();
		btTransform.dispose();
	}

	@Override
	public void initGraphix() {
		// Model m = this.world.getGameScreen().getAssets()
		// .get("model/coins/coin.g3db", Model.class);
		Model m = this.world.getGameScreen().getAssets()
				.get("model/pumpkin/pumpkin_04_01_a.g3db", Model.class);
		for (Node node : m.nodes) {
			node.scale.set(0.1f, .1f, .1f);
			// node.translation.set(0f, -3f, 0f);
			node.rotation.set(new Vector3(0f, 1f, 0f), 200f);
			node.calculateTransforms(true);
		}
		instance = new ModelInstance(m);
		instance.transform.rotate(new Vector3(1.0f, 0, 0), 90.0f);
		instance.transform.scl(1f);
	}
}