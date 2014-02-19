package fh.teamproject.interfaces;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
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

	public void update(Vector3 playerPosition);

	public float getSlidedDistance();

	public abstract CatmullRomSpline<Vector3> getSpline();
}
