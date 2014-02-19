package fh.teamproject.entities;

import com.badlogic.gdx.utils.Pool;

import fh.teamproject.interfaces.ISlidePart;

public class SlidePartPool extends Pool<ISlidePart> {

	@Override
	protected ISlidePart newObject() {
		ISlidePart slidePart = new SlidePart();
		return slidePart;
	}
}