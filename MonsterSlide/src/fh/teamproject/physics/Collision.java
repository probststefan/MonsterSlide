package fh.teamproject.physics;

import com.badlogic.gdx.math.Vector3;

import fh.teamproject.entities.Plane;
import fh.teamproject.entities.Sphere;

public class Collision {

	public Collision() {
		// TODO Auto-generated constructor stub
	}

	public boolean intersectSphereToPlane(Sphere sphere, Plane plane) {
		Vector3 tmpVector = plane.normal.cpy();
		// Vektor wird umgedreht.
		tmpVector.scl(sphere.radius).scl(-1);

		Vector3 sumVec = sphere.direction.cpy().scl(sphere.velocity);
		Vector3 pos = new Vector3();
		sphere.instance.transform.getTranslation(pos);
		System.out.println(pos.cpy().add(sumVec));
		if (pos.len() < 0.01) {
			return true;
		} else {

			return false;
		}
	}
}