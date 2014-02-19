package fh.teamproject.physics;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

public class TriangleMeshCollisionFixer extends ContactListener {

	@Override
	public boolean onContactAdded(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
			btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {

		Collision.btAdjustInternalEdgeContacts(cp, colObj1Wrap, colObj0Wrap, partId1,
				index1, 0);
		return true;
	}

	@Override
	public void onContactEnded(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		if (match0) {
			System.out.println("match 0 ################");
			// collision object 0 (userValue0) matches
		}
		if (match1) {
			System.out.println("match 1 ################");
			// collision object 1 (userValue1) matches
		}
	}
}
