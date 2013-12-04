package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

public interface ISlidePart extends ICollisionEntity {

	public void setWidth(float width);

	public float getVertice(int position);

	public float getWidth();

	public void move(Vector3[] startPoints, Vector3[] endPoints,
			btDiscreteDynamicsWorld dynamicsWorld);
}
