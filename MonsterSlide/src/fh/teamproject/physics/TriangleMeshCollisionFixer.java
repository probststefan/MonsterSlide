package fh.teamproject.physics;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

public class TriangleMeshCollisionFixer extends ContactListener {

	public boolean onContactAdded(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
			btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {

		System.out.println("callBackFlag0 "
				+ colObj0Wrap.getCollisionObject().getContactCallbackFlag());
		System.out.println("callBackFlag1 "
				+ colObj1Wrap.getCollisionObject().getContactCallbackFlag());
		System.out.println("part0 " + partId0 + " index0 " + index0 + " part1 " + partId1
				+ " index1 " + index1);

		Collision.btAdjustInternalEdgeContacts(cp, colObj1Wrap, colObj0Wrap, partId1,
				index1);
		/*
		 * float friction0 = colObj0Wrap.getCollisionObject().getFriction();
		 * float friction1 = colObj1Wrap.getCollisionObject().getFriction();
		 * float restitution0 =
		 * colObj0Wrap.getCollisionObject().getRestitution(); float restitution1
		 * = colObj1Wrap.getCollisionObject().getRestitution();
		 * 
		 * if ((colObj0Wrap.getCollisionObject().getCollisionFlags() &
		 * btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK) != 1) {
		 * friction0 = 1.0f;// partId0,index0 restitution0 = 0.f; }
		 * 
		 * if ((colObj1Wrap.getCollisionObject().getCollisionFlags() &
		 * btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK) != 1) {
		 * if ((index1 & 1) != 1) { friction1 = 1.0f;// partId1,index1 } else {
		 * } friction1 = 0.f; } restitution1 = 0.f; }
		 * 
		 * cp.setCombinedFriction(this.calculateCombinedFriction(friction0,
		 * friction1)); cp.setCombinedRestitution(restitution0 * restitution1);
		 */
		return true;
	}

	private float calculateCombinedFriction(float friction0, float friction1) {
		float friction = friction0 * friction1;

		float MAX_FRICTION = 10.f;
		if (friction < -MAX_FRICTION)
			friction = -MAX_FRICTION;
		if (friction > MAX_FRICTION)
			friction = MAX_FRICTION;
		return friction;

	}
}
