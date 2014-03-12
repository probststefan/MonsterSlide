package fh.teamproject.physics.callbacks.contacts;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.game.World;
import fh.teamproject.interfaces.IContactCallback;

/**
 * Verwalterklasse fuer alle ContactListener.
 * 
 * @author stefanprobst
 * 
 */
public class MonsterContactListener extends ContactListener {

	private Array<IContactCallback> listener;
	private World world;

	public MonsterContactListener(World world) {
		this.listener = new Array<IContactCallback>();
		this.world = world;
	}

	public void addListener(IContactCallback contactListener) {
		this.listener.add(contactListener);
	}

	@Override
	public void onContactStarted(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		for (IContactCallback l : listener) {
			l.onContactStarted(userValue0, match0, userValue1, match1);
		}
	}

	@Override
	public void onContactEnded(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		for (IContactCallback c : listener) {
			c.onContactEnded(userValue0, match0, userValue1, match1);
		}
	}

	@Override
	public boolean onContactAdded(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
			boolean match0, btCollisionObjectWrapper colObj1Wrap, int partId1,
			int index1, boolean match1) {
		for (IContactCallback l : listener) {
			l.onContactAdded(cp, colObj0Wrap, partId0, index0, match0, colObj1Wrap,
					partId1, index1, match1);
		}
		return true;
	}
}