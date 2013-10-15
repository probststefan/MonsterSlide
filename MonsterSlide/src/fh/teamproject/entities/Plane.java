package fh.teamproject.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Plane {

	public ModelInstance instance;

	public Plane() {
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		Model m = builder.createRect(-1, 0, 1, 1, 0, 1, 1, 0, -1, -1, 0, -1, 0, 1, 1,
				material, Usage.Position | Usage.Normal);
		this.instance = new ModelInstance(m);
	}
}
