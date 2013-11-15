package fh.teamproject.entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;

public class Slide extends Entitiy implements ISlide {

	private final ArrayList<ISlidePart> slideParts;
	// Hilfsvariabeln
	private final Vector3[] tmpVertices;
	private final Random rand;
	private int randomSlope = 0;

	// SlidePart pool.
	private final Pool<ISlidePart> slidePartPool = new Pool<ISlidePart>() {
		@Override
		protected SlidePart newObject() {
			return new SlidePart();
		}
	};

	public Slide() {
		this.slideParts = new ArrayList<ISlidePart>();
		this.rand = new Random();

		// Einen SlidePart zum Test erstellen.
		this.tmpVertices = new Vector3[4];
		this.tmpVertices[0] = new Vector3(-10, 0, -10);
		this.tmpVertices[1] = new Vector3(-10, -5, 10);
		this.tmpVertices[2] = new Vector3(10, -5, 10);
		this.tmpVertices[3] = new Vector3(10, 0, -10);

		// Zum Start existieren schon direkt einige SlideParts.
		for (int i = 0; i < 3; ++i) {
			this.addSlidePart();
		}
	}

	/**
	 * 
	 * @param playerPosition
	 */
	@Override
	public void update(Vector3 playerPosition) {
		for (int i = 0; i < this.slideParts.size(); ++i) {
			// Kontrollieren ob der Spieler schon unter dem Rutschelement ist.
			if (playerPosition.y < slideParts.get(i).getVertice(1).y - 2) {
				slideParts.get(i).setAliveState(false);
				// Rutschelement aus der Liste entfernen.
				slidePartPool.free(this.slideParts.get(i));
				this.slideParts.remove(i);

				this.addSlidePart();
			}
		}
	}

	/**
	 * Fuegt ein Rutschelement an die Rutsche an.
	 */
	@Override
	public void addSlidePart() {
		// Rutschelement erstellen und hinzufuegen.
		SlidePart slidePart = (SlidePart) slidePartPool.obtain();
		slidePart.setVertice(tmpVertices[0], 0);
		slidePart.setVertice(tmpVertices[1], 1);
		slidePart.setVertice(tmpVertices[2], 2);
		slidePart.setVertice(tmpVertices[3], 3);
		slidePart.createSlidePart();
		this.slideParts.add(slidePart);

		// tmpVertices fuer das naechste Elem. updaten.
		// Gefaelle zufaellig bestimmen.
		this.randomSlope = this.rand.nextInt(10);
		randomSlope = 2;

		this.tmpVertices[0] = this.tmpVertices[1];
		this.tmpVertices[3] = this.tmpVertices[2];
		this.tmpVertices[1] = new Vector3(this.tmpVertices[1].x, this.tmpVertices[1].y - this.randomSlope,
				this.tmpVertices[1].z + slidePart.getWidth());
		this.tmpVertices[2] = new Vector3(this.tmpVertices[2].x, this.tmpVertices[2].y - this.randomSlope,
				this.tmpVertices[2].z + slidePart.getWidth());
	}

	@Override
	public void removeSlidePart(ISlidePart slidePart) {
		this.slideParts.remove(slidePart);
	}

	@Override
	public ArrayList<ISlidePart> getSlideParts() {
		return this.slideParts;
	}
}