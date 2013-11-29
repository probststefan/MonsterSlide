package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

public interface ISlidePart extends ICollisionEntity {

	public void setWidth(float width);

	public Vector3 getVertice(int position);

	public float getWidth();

	public boolean getAliveState();

	public void setAliveState(boolean alive);

	public void move(Vector3 tmpSlidePartPos, btDiscreteDynamicsWorld dynamicsWorld);
}
