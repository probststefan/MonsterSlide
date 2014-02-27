package fh.teamproject.interfaces;

import fh.teamproject.entities.Entitiy;
import fh.teamproject.entities.World;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface IEntity {


	public Vector3 getPosition();

	public void update();

	public ModelInstance getModelInstance();

	public int getID();

	public void setWorld(World world);

	public void dispose();

	public void initGraphix();
}
