package fh.teamproject.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.entities.Plane;
import fh.teamproject.entities.Sphere;

public class Collision {

	public Collision() {
		// TODO Auto-generated constructor stub
	}

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
}