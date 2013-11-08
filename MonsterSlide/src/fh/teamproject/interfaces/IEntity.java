package fh.teamproject.interfaces;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public interface IEntity {

	public Vector3 getPosition();

	public ModelInstance getModelInstance();

	public int getID();
}
