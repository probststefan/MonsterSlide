package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Vector3;

public interface ISlidePart extends ICollisionEntity {

	public ISlidePart setCatmullPoints(Vector3[] points);

	public Vector3[] getStartPoints();

	public void render();
}
