package fh.teamproject.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.BroadphaseNativeTypes;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.collision.btTriangleInfoMap;
import com.badlogic.gdx.physics.bullet.collision.btTriangleShape;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;

public class TriangleMeshCollisionFixer extends ContactListener {

	@Override
	public boolean onContactAdded(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
			btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
		// Gdx.app.log("CollisionFixer", "contact added");
		// System.out.println(colObj0Wrap.getCollisionShape().getShapeType());
		if (colObj1Wrap.getCollisionShape().getShapeType() != BroadphaseNativeTypes.TRIANGLE_SHAPE_PROXYTYPE) {
			// System.out.println("########");
		}

		Collision.btAdjustInternalEdgeContacts(cp, colObj1Wrap, colObj0Wrap, partId1,
				index1, 0);

		// this.btAdjustInternalEdgeContacts2(cp, colObj1Wrap, colObj0Wrap,
		// partId0, index0,
		// btInternalEdgeAdjustFlags.BT_TRIANGLE_CONCAVE_DOUBLE_SIDED);
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

	public float calculateCombinedFriction(float friction0, float friction1) {
		float friction = friction0 * friction0;

		float MAX_FRICTION = 10.f;
		if (friction < -MAX_FRICTION) {
			friction = -MAX_FRICTION;
		}
		if (friction > MAX_FRICTION) {
			friction = MAX_FRICTION;
		}
		return friction;
	}

	public void btAdjustInternalEdgeContacts2(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, btCollisionObjectWrapper colObj1Wrap,
			int partId0, int index0, int normalAdjustFlags) {
		if (colObj0Wrap.getCollisionShape().getShapeType() != BroadphaseNativeTypes.TRIANGLE_SHAPE_PROXYTYPE) {
			System.out.println("out");
			return;
		}

		btBvhTriangleMeshShape trimesh = null;
		trimesh = (btBvhTriangleMeshShape) colObj0Wrap.getCollisionObject()
				.getCollisionShape();
		btTriangleInfoMap triangleInfoMapPtr = trimesh
				.getTriangleInfoMap();

		if (trimesh.getTriangleInfoMap() == null) {
			System.out.println("TriangeInfoMap is null!!!");
			return;
		}

		btTriangleShape tri_shape = (btTriangleShape) colObj0Wrap.getCollisionShape();
		Vector3 v0 = null, v1 = null, v2 = null;
		tri_shape.getVertex(0, v0);
		tri_shape.getVertex(1, v1);
		tri_shape.getVertex(2, v2);
		Vector3 tri_normal = null;
		tri_shape.calcNormal(tri_normal);
		// if( tri_normal.dot( cp.m_normalWorldOnB ) < 0 )

		tri_normal.mul(-1.0f);
		btVector3 result = new btVector3();
		result.setX(tri_normal.x);
		result.setY(tri_normal.y);
		result.setZ(tri_normal.z);
		// cp.setNormalWorldOnB(result);
	}

	private int btGetHash(int partId, int triangleIndex) {
		int hash = (partId << (31 - 10)) | triangleIndex;
		return hash;
	}
}
