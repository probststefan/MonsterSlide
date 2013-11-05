package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Plane {

	public ModelInstance instance;
	public Vector3 normal;

	public Plane() {
		ModelBuilder builder = new ModelBuilder();

		Texture texture = new Texture(Gdx.files.internal("data/floor.jpg"), true);
		TextureAttribute textureAttr = new TextureAttribute(TextureAttribute.Diffuse,
				texture);

		Material material = new Material(ColorAttribute.createDiffuse(Color.BLUE));
		material.set(textureAttr);

		Model m = builder.createRect(-1, 0, -1, -1, 0, 1, 1, 0, 1, 1, 0, -1, 0, 1, 0,
				material, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		this.instance = new ModelInstance(m);

		this.instance.transform.scl(4);
		this.instance.transform.rotate(new Vector3(1.0f, 1.0f, 0), 45);
		// this.instance.transform.translate(0, -1f, 0);

		// Normale erstellen und diese auch rotieren, damit die Kollision
		// richtig abgefragt werden kann.
		this.normal = new Vector3(0, 1f, 0);
		this.normal.rotate(new Vector3(1.0f, 1.0f, 0), 45);
	}
}
