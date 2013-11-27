package fh.teamproject.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;

public class Slide extends Entitiy implements ISlide {

	private final ArrayList<ISlidePart> slideParts;
	private SlidePartPool pool;
	private Vector3 tmpSlidePartPos;
	private btDiscreteDynamicsWorld dynamicsWorld;

	public Slide(btDiscreteDynamicsWorld dynamicsWorld) {
		slideParts = new ArrayList<ISlidePart>();
		pool = new SlidePartPool();
		tmpSlidePartPos = new Vector3(0, 0, 0);
		this.dynamicsWorld = dynamicsWorld;

		for (int i = 0; i < 5; ++i) {
			this.addSlidePart();
		}
	}

	@Override
	public void addSlidePart() {
		ISlidePart slidePart = pool.obtain();
		this.slideParts.add(slidePart);
		slidePart.move(tmpSlidePartPos, this.dynamicsWorld);

		tmpSlidePartPos.z += slidePart.getWidth();
	}

	@Override
	public ArrayList<ISlidePart> getSlideParts() {
		return this.slideParts;
	}

	@Override
	public void removeSlidePart(ISlidePart slidePart) {
		this.slideParts.remove(slidePart);
		this.pool.free(slidePart);
	}

	public void disposeSlidePart(ISlidePart slidePart) {
		slidePart.dispose();
	}

	@Override
	public void update(Vector3 playerPosition) {
		for (int i = 0; i < this.slideParts.size(); i++) {
			if (this.slideParts.get(i).getVertice(1).y > playerPosition.y) {
				this.pool.free(this.slideParts.get(i));
				this.removeSlidePart(this.slideParts.get(i));
			}
		}
	}
}