package fh.teamproject.interfaces;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public interface ICollisionEntity extends IEntity {

	public btRigidBody getRigidBody();

	public void setMass(float mass);

	public void releaseAll();

	public void initPhysix();

}
