package fh.teamproject.physics.listener;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

import fh.teamproject.entities.World;

public class MonsterContactListener extends ContactListener {

	private World world;

	public MonsterContactListener(World world) {
		this.world = world;
	}

	@Override
	public void onContactStarted(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		if (match0) {
			// Kollision mit einem Coin.
			this.world.getCoins().pickCoin(userValue0);
			this.world.getScore().incrementCoinScore();
		}

		if (match1) {
			// Setzen des aktuellen berutschten SlideParts.
			this.world.getSlide().setActualSlidePartId(userValue1);
			// Player sitzt auf der Slide.
			this.world.getPlayer().setGrounded(true);
		}
	}

	@Override
	public void onContactEnded(int userValue0, boolean match0, int userValue1,
			boolean match1) {
		if (match1) {
			// Player sitzt nicht mehr auf der Slide (er ist abgehoben bzw.
			// springt).
			this.world.getPlayer().setGrounded(false);
		}
	}

	@Override
	public boolean onContactAdded(btManifoldPoint cp,
			btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
			btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
		Collision.btAdjustInternalEdgeContacts(cp, colObj1Wrap, colObj0Wrap, partId1,
				index1, 0);
		return true;
	}
}
