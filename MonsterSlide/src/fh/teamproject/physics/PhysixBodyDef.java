package fh.teamproject.physics;

import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class PhysixBodyDef extends btRigidBodyConstructionInfo {
	PhysixManager manager;
	public PhysixBodyDef(PhysixManager manager, float mass, btMotionState motionState,
			btCollisionShape collisionShape) {
		super(mass, motionState, collisionShape);
		this.manager = manager;
	}

	public PhysixBody create() {
		return new PhysixBody(this, manager);
	}
}
