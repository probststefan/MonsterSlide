package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public interface ISlidePart extends ICollisionEntity {

	public ISlidePart setCatmullPoints(Vector3[] points);

	public Vector3[] getStartPoints();

	public void render();

	/**
	 * Gibt die Vertices zurück, die zur Erstellung des Models genutzt werden.
	 * Sie sind anders sortiert als die Physikvertices.
	 * 
	 * @return
	 */
	public Array<Vector3> getGraphicVertices();

	/**
	 * Gibt die interpolierten Punkte zurück, die zum Aufbau der Physik und
	 * Rendering genutzt werden
	 * 
	 * @return Liste der ursprünglichen Vertices
	 */
	public Array<Vector3> getInterpolatedVertices();

	/**
	 * Gibt die Kontrollpunkte der SlidePart zurück
	 * 
	 * @return
	 */
	public Array<Vector3> getControlPoints();

	public ISlidePart setControlPoints(Array<Vector3> controlPoints);
}
