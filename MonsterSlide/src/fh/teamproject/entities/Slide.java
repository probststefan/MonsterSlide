package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.CatmullSplineGenerator;

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
	Array<SlideBorder> borders = new Array<SlideBorder>();

	public Slide(btDiscreteDynamicsWorld dynamicsWorld) {
		this.dynamicsWorld = dynamicsWorld;
		addCatmullSlidePart();
	}

	private void addCatmullSlidePart() {
		CatmullSplineGenerator generator = new CatmullSplineGenerator();
		generator.generateSlide();

		ISlidePart tmpSlidePart;
		if (GameScreen.settings.DEBUG_HILL) {
			tmpSlidePart = pool.obtain().setCatmullPoints(generator.getPlankPoints());
		} else {
			tmpSlidePart = pool.obtain().setCatmullPoints(generator.getPoints());
		}

		slideParts.add(tmpSlidePart);
		dynamicsWorld.addRigidBody(tmpSlidePart.getRigidBody());
		// createSlidePartBorders(tmpSlidePart);
	}

	public void createSlidePartBorders(ISlidePart part) {
		SlidePart bPart = (SlidePart) part;
		Vector3 v = null;
		for (int i = 0; i < bPart.graphicsVertices.size; i += 1) {
			v = bPart.graphicsVertices.get(i);
			SlideBorder border = new SlideBorder(v);
			borders.add(border);
			dynamicsWorld.addRigidBody(border.getRigidBody());
		}
	}

	@Override
	public void update(Vector3 playerPosition) {
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {

		}
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
		for (SlideBorder b : borders) {
			batch.render(b.getModelInstance(), lights);
		}
	}

	@Override
	public void addSlidePart() {
		// TODO Auto-generated method stub

	}
}