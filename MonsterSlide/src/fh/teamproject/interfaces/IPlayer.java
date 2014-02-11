package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Vector3;

public interface IPlayer extends ICollisionEntity {

	public void accelerate(float amount);

	public void brake(float amount);

	public void slideLeft();

	public void slideRight();

	public void jump();

	public Vector3 getDirection();

}
