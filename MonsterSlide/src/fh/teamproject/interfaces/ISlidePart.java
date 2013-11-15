package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Vector3;

public interface ISlidePart extends ICollisionEntity {

	public void setWidth(float width);

	public Vector3 getVertice(int position);

	public float getWidth();

	public boolean getAliveState();

	public void setAliveState(boolean alive);
}
