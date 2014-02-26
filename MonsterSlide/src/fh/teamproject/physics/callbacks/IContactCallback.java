package fh.teamproject.physics.callbacks;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

public interface IContactCallback {
	public boolean onContactAdded(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
			boolean match0, btCollisionObjectWrapper colObj1Wrap, int partId1,
			int index1, boolean match1);

	public void onContactStarted(int userValue0, boolean match0, int userValue1,
			boolean match1);

	public void onContactEnded(int userValue0, boolean match0, int userValue1,
			boolean match1);

}
