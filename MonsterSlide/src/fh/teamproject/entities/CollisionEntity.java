package fh.teamproject.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;

import fh.teamproject.interfaces.ICollisionEntity;

/**
 * Abstrahiert ein Kollisionsobjekt fuer die Bullet-World.
 * 
 * @author stefanprobst
 */
public abstract class CollisionEntity extends Entitiy implements ICollisionEntity {
	private btCollisionShape collisionShape;
	public MotionState motionState;
	private btRigidBodyConstructionInfo rigidBodyInfo;
	public btRigidBody rigidBody;
	private Vector3 localInertia;
	private float mass;

	CollisionEntity() {
		// this.motionState = new btDefaultMotionState();
		this.localInertia = new Vector3();
		// Standardmaessig erstmal keine Masse setzen.
		this.mass = 0;
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void dispose() {
		this.rigidBody.dispose();
		this.motionState.dispose();
		this.collisionShape.dispose();
		this.rigidBodyInfo.dispose();
	}

	@Override
	public void releaseAll() {
		this.collisionShape.release();
		this.motionState.release();
		this.rigidBody.release();
		this.localInertia = null;
	}

	/**
	 * Erstellt den endgueltigen RigidBody der von Bullet benutzt wird.
	 */
	@Override
	public void createRigidBody() {
		this.rigidBodyInfo = new btRigidBodyConstructionInfo(this.mass, this.motionState,
				this.collisionShape, this.localInertia);

		this.rigidBody = new btRigidBody(this.rigidBodyInfo);
	}

	public void createMotionState() {
		this.motionState = new MotionState(this.instance.transform);
	}

	/**
	 * Gibt den RigidBody des Kollisionsobjekts zurueck der dann an die
	 * dynamicsWorld uebergeben werden kann.
	 * 
	 * @return btRigidBody
	 */
	@Override
	public btRigidBody getRigidBody() {
		return this.rigidBody;
	}

	/**
	 * Typ des Shape setzen. Soll es zum Beispiel eine Sphere oder eine Ebene
	 * sein.
	 * 
	 * @param collisionShape
	 */
	@Override
	public void setCollisionShape(btCollisionShape collisionShape) {
		this.collisionShape = collisionShape;
	}

	/**
	 * Position des Kollisionskoerpers setzen. Dieser sollte sich dort befinden
	 * wo auch das Objekt gerendert wird. ;)
	 * 
	 * @param transform
	 */
	@Override
	public void setEntityWorldTransform(Matrix4 transform) {
		if (null == this.motionState) {
			this.createMotionState();
		}

		this.motionState.setWorldTransform(transform);
	}

	/**
	 * Hier kann die Traegheit eines Objekt gesetzt werden.
	 * 
	 * @param localInertia
	 */
	@Override
	public void setLocalInertia(Vector3 localInertia) {
		this.localInertia = localInertia;
	}

	/**
	 * Masse eines Objekts setzen.
	 * 
	 * @param mass
	 */
	@Override
	public void setMass(float mass) {
		this.mass = mass;
		this.collisionShape.calculateLocalInertia(this.mass, this.localInertia);
	}

}
