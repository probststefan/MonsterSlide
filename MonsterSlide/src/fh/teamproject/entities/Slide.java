package fh.teamproject.entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;

public class Slide implements ISlide {

	private ArrayList<ISlidePart> slideParts;
	// Hilfsvariabeln
	private Vector3[] tmpVertices;
	private Random rand;
	private int randomSlope = 0;

	// SlidePart pool.
	private final Pool<SlidePart> slidePartPool = new Pool<SlidePart>() {
		protected SlidePart newObject() {
			return new SlidePart();
		}
	};

	public Slide() {
		this.slideParts = new ArrayList<ISlidePart>();
		rand = new Random();

		// Einen SlidePart zum Test erstellen.
		tmpVertices = new Vector3[4];
		tmpVertices[0] = new Vector3(-10, 0, -10);
		tmpVertices[1] = new Vector3(-10, -5, 10);
		tmpVertices[2] = new Vector3(10, -5, 10);
		tmpVertices[3] = new Vector3(10, 0, -10);

		// Zum Start existieren schon direkt einige SlideParts.
		for (int i = 0; i < 5; ++i) {
			this.addSlidePart();
		}
	}

	/**
	 * 
	 * @param playerPosition
	 */
	public void update(Vector3 playerPosition) {
		for (int i = 0; i < slideParts.size(); ++i) {
			// Kontrollieren ob der Spieler schon unter dem Rutschelement ist.
			if (playerPosition.y < slideParts.get(i).getVertice(1).y - 5) {
				// Rutschelement aus der Liste entfernen. Den Rest sollte der
				// den Garbage collector regeln, theoretisch zumindestens.
				slidePartPool.free((SlidePart) this.slideParts.get(i));
				this.slideParts.remove(i);

				this.addSlidePart();
			}
		}
	}

	/**
	 * Fuegt ein Rutschelement an die Rutsche an.
	 */
	public void addSlidePart() {
		// Rutschelement erstellen und hinzufuegen.
		SlidePart slidePart = slidePartPool.obtain();
		slidePart.setVertice(tmpVertices[0], 0);
		slidePart.setVertice(tmpVertices[1], 1);
		slidePart.setVertice(tmpVertices[2], 2);
		slidePart.setVertice(tmpVertices[3], 3);
		slidePart.createSlidePart();
		this.slideParts.add(slidePart);

		// tmpVertices fuer das naechste Elem. updaten.
		// Gefaelle zufaellig bestimmen.
		randomSlope = rand.nextInt(10);

		tmpVertices[0] = tmpVertices[1];
		tmpVertices[3] = tmpVertices[2];
		tmpVertices[1] = new Vector3(tmpVertices[1].x, tmpVertices[1].y - randomSlope,
				tmpVertices[1].z + slidePart.getWidth());
		tmpVertices[2] = new Vector3(tmpVertices[2].x, tmpVertices[2].y - randomSlope,
				tmpVertices[2].z + slidePart.getWidth());
	}

	@Override
	public void removeSlideParty(ISlidePart slidePart) {
		this.slideParts.remove(slidePart);
	}

	@Override
	public ArrayList<ISlidePart> getSlideParts() {
		return this.slideParts;
	}

	@Override
	public Vector3 getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelInstance getModelInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}