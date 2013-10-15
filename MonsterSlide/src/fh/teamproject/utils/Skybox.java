package fh.teamproject.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Skybox {

	public ModelInstance box;

	public Skybox() {
		ModelBuilder builder = new ModelBuilder();
		Model model;

		Material material = new Material(new ColorAttribute(
				ColorAttribute.createDiffuse(Color.GRAY)));
		model = builder.createBox(1, 1, 1, material, Usage.Position | Usage.Normal);
		this.box = new ModelInstance(model);
		this.box.transform.setToTranslation(0f, 0f, 0f);
		//this.box.transform.scl(20f);
	}

}
