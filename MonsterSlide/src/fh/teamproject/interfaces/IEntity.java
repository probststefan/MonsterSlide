package fh.teamproject.interfaces;

import fh.teamproject.game.World;
import fh.teamproject.game.entities.Entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface IEntity {


	public Vector3 getPosition();

	public void setPosition(Vector3 position);

	public void update();

	public ModelInstance getModelInstance();

	public int getID();

	public void dispose();

	public void initGraphix();
}
