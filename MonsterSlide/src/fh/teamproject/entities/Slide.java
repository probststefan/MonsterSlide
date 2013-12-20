package fh.teamproject.entities;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;

/**
 * Diese Klasse uebernimmt die Generierung und Darstellung der Slide. Es wird
 * zusaetzlich die Erstellung des BulletCollisionShape erledigt.
 * 
 * @author stefanprobst
 * 
 */
public class Slide implements ISlide {

	Array<ISlidePart> slideParts = new Array<ISlidePart>();
	btDiscreteDynamicsWorld dynamicsWorld;
	SlidePartPool pool = new SlidePartPool();

	public Slide(btDiscreteDynamicsWorld dynamicsWorld) {
		this.dynamicsWorld = dynamicsWorld;
		addSlidePart();
	}

	@Override
	public void addSlidePart() {
		ISlidePart bezPart;
		Vector3 start = new Vector3(-10.0f, -3.0f, -40.0f);
		Vector3 control1 = new Vector3(0.0f, 10.0f, 10.0f);
		Vector3 control2 = new Vector3(30.0f, -15.0f, 15.0f);
		Vector3 end = new Vector3(50.0f, -40.0f, 200.0f);
		bezPart = pool.obtain().set(start, end, control1, control2, 0.1f);
		slideParts.add(bezPart);
		dynamicsWorld.addRigidBody(bezPart.getRigidBody());

	}

	@Override
	public void update(Vector3 playerPosition) {
	}


	@Override
	public Array<ISlidePart> getSlideParts() {
		return slideParts;
	}

	@Override
	public void removeSlidePart(ISlidePart slidePart) {
		slideParts.removeValue(slidePart, false);
	}

	@Override
	public void render(ModelBatch batch, Environment lights) {
		for (ISlidePart part : slideParts) {
			batch.render(part.getModelInstance(), lights);
		}
	}

}