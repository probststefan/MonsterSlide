package fh.teamproject.entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;

public class Slide implements ISlide {

	private ArrayList<ISlidePart> slideParts;

	public Slide() {
		this.slideParts = new ArrayList<ISlidePart>();
		Random rand = new Random();
		int randomSlope = 0;

		// Einen SlidePart zum Test erstellen.
		Vector3[] tmpVertices = new Vector3[4];
		tmpVertices[0] = new Vector3(-10, 0, -10);
		tmpVertices[1] = new Vector3(-10, -5, 10);
		tmpVertices[2] = new Vector3(10, -5, 10);
		tmpVertices[3] = new Vector3(10, 0, -10);

		for (int i = 0; i < 10; ++i) {
			SlidePart slidePart = new SlidePart();
			slidePart.setVertice(tmpVertices[0], 0);
			slidePart.setVertice(tmpVertices[1], 1);
			slidePart.setVertice(tmpVertices[2], 2);
			slidePart.setVertice(tmpVertices[3], 3);
			slidePart.createSlidePart();
			this.slideParts.add(slidePart);

			// Gefaelle zufaellig bestimmen.
			randomSlope = rand.nextInt(10);

			// tmpVertices fuer das naechste Elem. updaten.
			tmpVertices[0] = tmpVertices[1];
			tmpVertices[3] = tmpVertices[2];
			tmpVertices[1] = new Vector3(tmpVertices[1].x,
					tmpVertices[1].y - randomSlope, tmpVertices[1].z
							+ slidePart.getWidth());
			tmpVertices[2] = new Vector3(tmpVertices[2].x,
					tmpVertices[2].y - randomSlope, tmpVertices[2].z
							+ slidePart.getWidth());
		}
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