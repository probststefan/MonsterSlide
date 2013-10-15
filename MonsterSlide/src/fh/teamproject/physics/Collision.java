package fh.teamproject.physics;

import com.badlogic.gdx.math.Vector3;

public class Collision {

	boolean intersectSphereToPlane(Vector3 sphereCenter, int sphereRadius,
			Vector3 pointOnPlane, Vector3 planeNormal) {
		// Calculate a vector from the point on the plane to the center of the
		// sphere
		Vector3 vecTemp = sphereCenter;
		vecTemp.sub(pointOnPlane);

		// Calculate the distance: dot product of the new vector with the
		// plane's normal
		float distance = Vector3.dot(vecTemp.x, vecTemp.y, vecTemp.z, planeNormal.x,
				planeNormal.y, planeNormal.z);

		if (distance > sphereRadius) {
			// The sphere is not touching the plane.
			return false;
		}

		// The sphere is colliding with the plane
		return true;
	}
}