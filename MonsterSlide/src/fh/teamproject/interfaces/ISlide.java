package fh.teamproject.interfaces;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public interface ISlide {

	/*
	 * Fuegt einen ISlidePart zur Liste hinzu.
	 */
	public void addSlidePart();

	public Array<ISlidePart> getSlideParts();

	public void removeSlidePart(ISlidePart slidePart);

	public void update();

	public float getSlidedDistance();

	public void setActualSlidePartId(int id);

	public int getActualSlidePartId();

	public abstract CatmullRomSpline<Vector3> getSpline();
}
