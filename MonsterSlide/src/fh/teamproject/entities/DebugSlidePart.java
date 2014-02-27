package fh.teamproject.entities;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btStaticPlaneShape;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.physics.PhysixBodyDef;

public class DebugSlidePart extends CollisionEntity implements ISlidePart {

	@Override
	public void initPhysix() {
		// TODO Auto-generated method stub
		Vector3 rotate = Vector3.Y.rotate(-15f, 0f, 0f, 1f);
		collisionShape = new btStaticPlaneShape(rotate, 2f);
		PhysixBodyDef bodyDef = new PhysixBodyDef(world.getPhysixManager(), mass,
				motionState, collisionShape);
		rigidBody = bodyDef.create();
	}

	@Override
	public ISlidePart setSpline(CatmullRomSpline<Vector3> spline) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISlidePart setSlide(Slide slide) {
		// TODO Auto-generated method stub
		return null;
	}

}
