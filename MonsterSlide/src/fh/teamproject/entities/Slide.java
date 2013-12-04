package fh.teamproject.entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;

public class Slide extends Entitiy implements ISlide {

	private final ArrayList<ISlidePart> slideParts;
	private SlidePartPool pool;
	private Vector3 tmpSlidePartPos;
	private btDiscreteDynamicsWorld dynamicsWorld;
	// Gefaelle der Slide.
	private int slope = 0;
	private Random rand;
	// Endpunkte des vorherigen SlideParts; sind also die Startpunkte der
	// naechsten Platte.
	private Vector3[] startPoints;
	private Vector3[] endPoints;

	public Slide(btDiscreteDynamicsWorld dynamicsWorld) {
		slideParts = new ArrayList<ISlidePart>();
		pool = new SlidePartPool();
		tmpSlidePartPos = new Vector3(0, 0, 0);
		this.dynamicsWorld = dynamicsWorld;
		rand = new Random();
		this.startPoints = new Vector3[2];
		this.endPoints = new Vector3[2];

		this.startPoints[0] = new Vector3(-10, 0, -10);
		this.startPoints[1] = new Vector3(10, 0, -10);
		this.endPoints[0] = new Vector3(-10, -15, 10);
		this.endPoints[1] = new Vector3(10, -15, 10);

		for (int i = 0; i < 5; ++i) {
			this.addSlidePart();
			tmpSlidePartPos.z += 20;
		}
	}

	@Override
	public void addSlidePart() {
		ISlidePart slidePart = pool.obtain();
		this.slideParts.add(slidePart);

		if (tmpSlidePartPos == null) {
			Gdx.app.log("slidePart Pos", "" + slidePart.getPosition());
			tmpSlidePartPos = slidePart.getPosition();
		}

		this.move(slidePart);
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
			if (this.slideParts.get(i).getVertice(4) > playerPosition.y + 4) {
				this.move(this.slideParts.get(i));
			}
		}
	}

	private void createRandomEndPoints() {
		this.slope -= 20;

		int randX = rand.nextInt(10);

		this.endPoints[0] = new Vector3(this.endPoints[0].x, this.slope,
				this.endPoints[0].z + 20);
		this.endPoints[1] = new Vector3(this.endPoints[1].x, this.slope,
				this.endPoints[1].z + 20);
	}

	private void move(ISlidePart slidePart) {
		slidePart.move(this.startPoints, this.endPoints, this.dynamicsWorld);

		this.startPoints[0] = this.endPoints[0];
		this.startPoints[1] = this.endPoints[1];

		this.createRandomEndPoints();
	}
}