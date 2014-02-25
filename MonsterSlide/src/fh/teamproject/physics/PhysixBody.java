package fh.teamproject.physics;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;

import fh.teamproject.interfaces.ICollisionEntity;

public class PhysixBody extends btRigidBody {

	PhysixManager manager;
	ICollisionEntity owner;

	public PhysixBody(btRigidBodyConstructionInfo constructionInfo, PhysixManager manager) {
		super(constructionInfo);
		this.manager = manager;
		manager.addRigidBody(this);
	}

	public ICollisionEntity getOwner() {
		return owner;
	}

	public void setOwner(ICollisionEntity owner) {
		this.owner = owner;
	}

}
