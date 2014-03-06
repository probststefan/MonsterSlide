package fh.teamproject.entities;

import javax.activation.MailcapCommandMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btStaticPlaneShape;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.physics.PhysixBodyDef;

public class DebugSlidePart extends CollisionEntity implements ISlidePart {

	DebugSlidePart(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}

	Slide slide;
	@Override
	public void initPhysix() {
		Vector3 rotate = Vector3.Y.rotate(-15f, 0f, 0f, 1f);
		collisionShape = new btStaticPlaneShape(rotate, 2f);
		PhysixBodyDef bodyDef = new PhysixBodyDef(world.getPhysixManager(), mass,
				motionState, collisionShape);
		rigidBody = bodyDef.create();
		rigidBody.translate(new Vector3(0f, 0f, 0f));
	}

	@Override
	public ISlidePart setSpline(CatmullRomSpline<Vector3> spline) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISlidePart setSlide(Slide slide) {
		this.slide = slide;
		return this;
	}

	@Override
	public void initGraphix() {
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material();
		material.set(TextureAttribute.createDiffuse(new Texture(Gdx.files
				.internal("data/floor.jpg"))));
		Model m = builder
.createRect(-100f, 0f, -100f, -100f, 0f, 100f, 100f, 0f, 100f,
				100f, 0f, -100f,
						0f,
				1f, 0f, material, Usage.Position | Usage.TextureCoordinates);
		ModelInstance ins = new ModelInstance(m);
		ins.transform.translate(0f, 2f, 0f);
		// ins.transform.scl(10f, 0f, 10f);
		ins.transform.rotate(0f, 0f, 1f, -15f);
		ins.calculateTransforms();
		slide.setModelInstance(ins);
	}

}
