package fh.teamproject.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;

public class Slide implements ISlide {

	private ArrayList<ISlidePart> slideParts;

	public Slide() {
		this.slideParts = new ArrayList<ISlidePart>();
	}

	@Override
	public Array<ISlidePart> getSlideParts() {
		// TODO Auto-generated method stub
		return null;
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