package fh.teamproject.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.entities.Plane;
import fh.teamproject.entities.Sphere;

@Deprecated
public class Collision {

	public Collision() {
		// TODO Auto-generated constructor stub
	}

	@Deprecated
	public boolean intersectSphereToPlane(Sphere sphere, Plane plane) {
		Vector3 circleBorderPoint = plane.normal.cpy();
		// Vektor wird umgedreht.
		circleBorderPoint.scl(-1).scl(sphere.radius);

		Vector3 moveDistance = sphere.direction.cpy().scl(sphere.velocity);
		Vector3 intersectPoint = new Vector3();
		sphere.instance.transform.getTranslation(intersectPoint);

		intersectPoint.add(circleBorderPoint);

		if (intersectPoint.len() < 0.01) {
			Gdx.app.log("Sphere", "intersecting at Point: " + intersectPoint);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <code>intersection</code> compares a dynamix sphere to a stationary
	 * plane. The velocity of the sphere is given as well as the period of time
	 * for movement. If a collision occurs somewhere along this time period,
	 * true is returned. False is returned otherwise.
	 * 
	 * @param plane
	 *            the stationary plane to test against.
	 * @param sphere
	 *            the dynamic sphere to test.
	 * @param velocity
	 *            the velocity of the sphere.
	 * @param time
	 *            the time range to test.
	 * @return true if intersection occurs, false otherwise.
	 */
	public static boolean intersection(Plane plane, Sphere sphere, Vector3 velocity,
			float time) {

		/*
		 * Normale der Plane aus der Klasse holen. Diese wurde auch bei der
		 * Translation beruecksichtigt.
		 */
		Vector3 planeNormal = plane.normal;

		// float sdist = planeNormal.dot(sphere.position) - plane.getConstant();
		float sdist = planeNormal.dot(sphere.position) - 0f;

		// System.out.println("Sphere rad. " + sphere.radius + " sdist: " +
		// sdist);
		// TODO: Kontrolle ob der Ball noch nicht zu weit kollidiert ist
		// (Backtracing?).

		if (sdist > sphere.radius) {
			float dotNW = planeNormal.dot(velocity);
			return (sphere.radius - sdist / dotNW) < time;
		} else if (sdist < -sphere.radius) {
			float dotNW = planeNormal.dot(velocity);
			return (-(sphere.radius + sdist) / dotNW) < time;
		} else {
			return true;
		}
	}
}