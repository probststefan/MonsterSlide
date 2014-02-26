package fh.teamproject.physics;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.utils.Array;

/**
 * Verwalterklasse fuer alle ContactListener.
 * 
 * @author stefanprobst
 * 
 */
public class GameContactListener extends ContactListener {

	private Array<ContactListener> listener;

	public GameContactListener() {
		this.listener = new Array<ContactListener>();
	}

	public void addListener(ContactListener contactListener) {
		this.listener.add(contactListener);
	}

	@Override
	public boolean onContactAdded(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
			btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
		for (ContactListener listener : this.listener) {
			listener.onContactAdded(cp, colObj0Wrap, partId0, index0, colObj1Wrap,
					partId1, index1);
		}
		return true;
	}

	@Override
	public void onContactStarted(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		for (ContactListener listener : this.listener) {
			listener.onContactStarted(userValue0, match0, userValue1, match1);
		}
	}

	@Override
	public void onContactEnded(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		for (ContactListener listener : this.listener) {
			listener.onContactEnded(userValue0, match0, userValue1, match1);
		}
	}
}