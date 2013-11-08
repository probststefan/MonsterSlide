package fh.teamproject.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

/**
 * Abstrahiert ein Kollisionsobjekt fuer die Bullet-World.
 * 
 * @author stefanprobst
 */
public abstract class CollisionEntity {
	private btCollisionShape collisionShape;
	private btDefaultMotionState motionState;
	private btRigidBody rigidBody;
	private Vector3 localInertia;
	private int mass;

	CollisionEntity() {
		this.motionState = new btDefaultMotionState();
		this.localInertia = new Vector3();
		// Standardmaessig erstmal keine Masse setzen.
		this.mass = 0;
	}

	/**
	 * Erstellt den endgueltigen RigidBody der von Bullet benutzt wird.
	 */
	public void createRigidBody() {
		btRigidBodyConstructionInfo rigidBodyInfo = new btRigidBodyConstructionInfo(
				this.mass, motionState, this.collisionShape, this.localInertia);

		this.rigidBody = new btRigidBody(rigidBodyInfo);
	}

	/**
	 * Gibt den RigidBody des Kollisionsobjekts zurueck der dann an die
	 * dynamicsWorld uebergeben werden kann.
	 * 
	 * @return btRigidBody
	 */
	public btRigidBody getRigidBody() {
		return this.rigidBody;
	}

	/**
	 * Typ des Shape setzen. Soll es zum Beispiel eine Sphere oder eine Ebene
	 * sein.
	 * 
	 * @param collisionShape
	 */
	public void setCollisionShape(btCollisionShape collisionShape) {
		this.collisionShape = collisionShape;
	}

	/**
	 * Position des Kollisionskoerpers setzen. Dieser sollte sich dort befinden
	 * wo auch das Objekt gerendert wird. ;)
	 * 
	 * @param transform
	 */
	public void setEntityWorldTransform(Matrix4 transform) {
		motionState.setWorldTransform(transform);
	}

	/**
	 * Hier kann die Traegheit eines Objekt gesetzt werden.
	 * 
	 * @param localInertia
	 */
	public void setLocalInertia(Vector3 localInertia) {
		this.localInertia = localInertia;
	}

	/**
	 * Masse eines Objekts setzen.
	 * 
	 * @param mass
	 */
	public void setMass(int mass) {
		this.mass = mass;
		this.collisionShape.calculateLocalInertia(this.mass, this.localInertia);
	}
}