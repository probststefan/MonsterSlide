package fh.teamproject.physics.callbacks;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

public class TriangleMeshCollisionFixer implements IContactCallback {

	@Override
	public boolean onContactAdded(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
			boolean match0, btCollisionObjectWrapper colObj1Wrap, int partId1,
			int index1, boolean match1) {
		Collision.btAdjustInternalEdgeContacts(cp, colObj1Wrap, colObj0Wrap, partId1,
				index1, 0);
		return true;

	}

	@Override
	public void onContactStarted(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContactEnded(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		// TODO Auto-generated method stub

	}

}
