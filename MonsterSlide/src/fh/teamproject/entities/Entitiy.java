package fh.teamproject.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.interfaces.IEntity;

public abstract class Entitiy implements IEntity {
	public ModelInstance instance;
	Vector3 position = new Vector3();
	int id;

	@Override
	public Vector3 getPosition() {
		return this.instance.transform.getTranslation(this.position);
	}

	@Override
	public ModelInstance getModelInstance() {
		// TODO Auto-generated method stub
		return this.instance;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return this.id;
	}

}
