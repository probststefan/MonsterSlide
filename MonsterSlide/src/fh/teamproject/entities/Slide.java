package fh.teamproject.entities;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.utils.SlideGenerator;

/**
 * Diese Klasse uebernimmt die Generierung und Darstellung der Slide. Es wird
 * zusaetzlich die Erstellung des BulletCollisionShape erledigt.
 * 
 * @author stefanprobst
 * 
 */
public class Slide implements ISlide {
	private static SlideGenerator slideGenerator = new SlideGenerator();

	Array<ISlidePart> slideParts = new Array<ISlidePart>();
	btDiscreteDynamicsWorld dynamicsWorld;
	SlidePartPool pool = new SlidePartPool();
	Array<SlideBorder> borders = new Array<SlideBorder>();
	private ISlidePart tmpSlidePart;
	private Vector3 nearestControlPoint = null;
	private float slidedDistance = 0.0f;
	private float displayedPoints = 0.0f;

	public Slide(btDiscreteDynamicsWorld dynamicsWorld) {
		this.dynamicsWorld = dynamicsWorld;
		Vector3 start = new Vector3(0, 0, 0);
		tmpSlidePart = pool.obtain().setControlPoints(
				Slide.slideGenerator.generateControlPoints(start, start));

		slideParts.add(tmpSlidePart);
		dynamicsWorld.addRigidBody(tmpSlidePart.getRigidBody());
		addSlidePart();
	}

	public void createSlidePartBorders(ISlidePart part) {
		SlidePart bPart = (SlidePart) part;
		Vector3 v = null;
		for (int i = 0; i < bPart.graphicsVertices.size; i += 1) {
			v = bPart.graphicsVertices.get(i);
			SlideBorder border = new SlideBorder(v);
			borders.add(border);
			dynamicsWorld.addRigidBody(border.getRigidBody());
		}
	}

	@Override
	public void update(Vector3 playerPosition) {
		ISlidePart slidePart = slideParts.get(0);

		// Update slideDistance.
		for (Vector3 controlPoint : slidePart.getGraphicVertices()) {
			if (nearestControlPoint == null) {
				nearestControlPoint = controlPoint;
				slidedDistance += nearestControlPoint.dst(new Vector3());
			}

			if (playerPosition.dst(controlPoint) < playerPosition
					.dst(nearestControlPoint)) {
				slidedDistance += nearestControlPoint.dst(controlPoint);
				nearestControlPoint = controlPoint;
			}
		}

	}

	@Override
	public float getSlidedDistance() {
		this.displayedPoints = Interpolation.linear.apply(this.displayedPoints,
				this.slidedDistance, 0.1f);
		return this.displayedPoints;
	}

	/**
	 * Liefert den Startpunkt der Slide. Damit kann der Spieler oben in der
	 * Mitte der Slide abgesetzt werden.
	 * 
	 * @return Vector3
	 */
	public Vector3 getStartPosition() {
		Vector3[] startPoints = tmpSlidePart.getStartPoints();

		Vector3 tmpVec = startPoints[0].add(startPoints[1]);
		return tmpVec.crs(new Vector3(0.0f, 1.0f, 0.0f)).add(tmpVec).scl(0.5f);
	}

	@Override
	public Array<ISlidePart> getSlideParts() {
		return slideParts;
	}

	@Override
	public void removeSlidePart(ISlidePart slidePart) {
		slideParts.removeValue(slidePart, false);
	}

	@Override
	public void addSlidePart() {
		ISlidePart last = slideParts.peek();
		Array<Vector3> cPoints = last.getControlPoints();
		Vector3 v1 = cPoints.get(cPoints.size - 4);
		Vector3 v2 = cPoints.get(cPoints.size - 3);
		Vector3 start = cPoints.get(cPoints.size - 2);
		Vector3 tangent = cPoints.peek();
		tmpSlidePart = pool.obtain().setControlPoints(
				Slide.slideGenerator.generateControlPoints(tangent, start));

		slideParts.add(tmpSlidePart);
		dynamicsWorld.addRigidBody(tmpSlidePart.getRigidBody());
	}
}