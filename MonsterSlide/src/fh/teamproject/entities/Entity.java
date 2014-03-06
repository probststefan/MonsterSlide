package fh.teamproject.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.interfaces.IEntity;
import fh.teamproject.screens.GameScreen;

public abstract class Entity implements IEntity {
	private static int idCounter = 0;

	protected final int id;
	public ModelInstance instance;
	Vector3 position = new Vector3();
	World world;

	public Entity(World world) {
		id = Entity.idCounter;
		Entity.idCounter++;
		this.world = world;
	}

	@Override
	public Vector3 getPosition() {
		return instance.transform.getTranslation(position);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public ModelInstance getModelInstance() {
		return instance;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
