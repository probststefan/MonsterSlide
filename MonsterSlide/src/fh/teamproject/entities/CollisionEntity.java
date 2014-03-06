package fh.teamproject.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;

import fh.teamproject.interfaces.ICollisionEntity;
import fh.teamproject.physics.PhysixBody;
import fh.teamproject.physics.callbacks.MotionState;

/**
 * Abstrahiert ein Kollisionsobjekt fuer die Bullet-World.
 * 
 * @author stefanprobst
 */
public abstract class CollisionEntity extends Entity implements ICollisionEntity {
	public PhysixBody rigidBody;
	protected float mass;

	CollisionEntity(World world) {
		super(world);
		// Standardmaessig erstmal keine Masse setzen.
		mass = 0;
		// initPhysix();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void dispose() {
		this.world.getPhysixManager().getWorld()
				.removeRigidBody((btRigidBody) getRigidBody());
		((btRigidBody) rigidBody).dispose();
		super.dispose();
	}

	@Override
	public void releaseAll() {
		rigidBody.release();
	}


	/**
	 * Gibt den RigidBody des Kollisionsobjekts zurueck der dann an die
	 * dynamicsWorld uebergeben werden kann.
	 * 
	 * @return btRigidBody
	 */
	@Override
	public PhysixBody getRigidBody() {
		return rigidBody;
	}

	/**
	 * Masse eines Objekts setzen.
	 * 
	 * @param mass
	 */
	@Override
	public void setMass(float mass) {
		this.mass = mass;
	}

}
