package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public interface ICollisionEntity extends IEntity {

	public void createRigidBody();

	public btRigidBody getRigidBody();

	public void setCollisionShape(btCollisionShape shape);

	public void setEntityWorldTransform(Matrix4 transform);

	public void setLocalInertia(Vector3 localInertia);

	public void setMass(float mass);

}
