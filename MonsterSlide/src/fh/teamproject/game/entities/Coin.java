package fh.teamproject.game.entities;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.game.World;
import fh.teamproject.physics.PhysixBody;
import fh.teamproject.physics.PhysixBodyDef;
import fh.teamproject.physics.callbacks.motion.MotionState;

;

public class Coin extends CollisionEntity implements Poolable {

	private float radius = 5f;

	public Coin(World world) {
		super(world);
		initGraphix();
		initPhysix();

	}

	@Override
	public void reset() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void setPosition(Vector3 position) {
		getModelInstance().transform.setToTranslation(position);
	}

	/**
	 * Erstellt das physikalische Kollisionsmodell. Es wird eine Sphere als
	 * Shape benutzt, weil dieses, fuer die Berechnung, am guenstigsten ist.
	 */
	@Override
	public void initPhysix() {
		btCollisionShape collisionShape = new btSphereShape(this.radius);
		PhysixBodyDef rigidBodyDef = new PhysixBodyDef(world.getPhysixManager(), mass,
				new MotionState(getModelInstance().transform), collisionShape);

		btTransform btTransform = new btTransform(getModelInstance().transform);
		rigidBodyDef.setStartWorldTransform(btTransform);

		rigidBody = rigidBodyDef.create();
		rigidBody.setContactCallbackFilter(Player.PLAYER_FLAG);
		rigidBody.setCollisionFlags(rigidBody.getContactCallbackFlag()
				| btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT
				| btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
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
			node.scale.set(0.3f, .3f, .3f);
			// node.translation.set(0f, -3f, 0f);
			node.rotation.set(new Vector3(0f, 1f, 0f), 200f);
			node.calculateTransforms(true);
		}
		instance = new ModelInstance(m);
		instance.transform.rotate(new Vector3(1.0f, 0, 0), 90.0f);
		instance.transform.scl(1f);
	}
}