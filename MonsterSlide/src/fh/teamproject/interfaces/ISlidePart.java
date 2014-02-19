package fh.teamproject.interfaces;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.entities.Slide;

public interface ISlidePart extends ICollisionEntity {

	public ISlidePart setSpline(CatmullRomSpline<Vector3> spline);

	public abstract ISlidePart setID(String id);

	public abstract ISlidePart setSlide(Slide slide);

	public void init();
}
