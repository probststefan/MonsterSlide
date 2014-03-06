package fh.teamproject.entities;

import com.badlogic.gdx.utils.Pool;

import fh.teamproject.interfaces.ISlidePart;

public class SlidePartPool extends Pool<ISlidePart> {

	World world;

	public SlidePartPool(World world) {
		this.world = world;
	}
	@Override
	protected ISlidePart newObject() {
		ISlidePart slidePart = new SlidePart(world);
		return slidePart;
	}
}