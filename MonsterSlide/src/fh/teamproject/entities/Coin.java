package fh.teamproject.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Coin extends CollisionEntity implements Poolable {

	private float radius = 1f;

	public Coin() {
		this.createModel();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	/**
	 * Erstellen des visuellen Repraesentation.
	 */
	private void createModel() {
		ModelBuilder builder = new ModelBuilder();

		Material material = new Material(ColorAttribute.createDiffuse(new Color(1f, 1f,
				1f, 1f)));
		// Durchmesser der Sphere berechnen.
		float diameter = radius * 2;
		float height = 0.5f;
		Model m = builder.createCylinder(diameter, height, diameter, 16, material,
				Usage.Position | Usage.Normal);

		instance = new ModelInstance(m, position);
	}
}
