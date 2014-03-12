package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public interface ICollisionEntity extends IEntity {


	public btRigidBody getRigidBody();

	public void setMass(float mass);

	public void releaseAll();

	public void initPhysix();

}
