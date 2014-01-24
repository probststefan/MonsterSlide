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
		// addSlidePart();
		addCatmullSlidePart();

		/*
		 * EasySplineGenerator easyGenerator = new EasySplineGenerator(2);
		 * ArrayList<Bezier<Vector3>> bezierCurves = easyGenerator.getSplines();
		 * ISlidePart tmpBezPart;
		 * 
		 * for (Bezier<Vector3> bezier : bezierCurves) { Array<Vector3> points =
		 * bezier.points; tmpBezPart = pool.obtain().set(points.get(0),
		 * points.get(3), points.get(1), points.get(2), 0.01f);
		 * slideParts.add(tmpBezPart);
		 * dynamicsWorld.addRigidBody(tmpBezPart.getRigidBody()); }
		 */
	}

	private void addCatmullSlidePart() {
		CatmullSplineGenerator generator = new CatmullSplineGenerator();
		generator.generateSlide();

		ISlidePart tmpSlidePart = pool.obtain().setCatmullPoints(generator.getPoints());
		slideParts.add(tmpSlidePart);
		dynamicsWorld.addRigidBody(tmpSlidePart.getRigidBody());
		createSlidePartBorders(tmpSlidePart);

	}

	@Override
	public void addSlidePart() {
		ISlidePart bezPart;
		Vector3 start = new Vector3(-5f, 0.0f, -5.0f);
		Vector3 control1 = new Vector3(-5.0f, -10.0f, -5.0f);
		Vector3 control2 = new Vector3(0.0f, -10.0f, 60.0f);
		Vector3 end = new Vector3(0.0f, 0.0f, 60.0f);
		bezPart = pool.obtain().set(start, end, control1, control2, 1f);
		slideParts.add(bezPart);
		dynamicsWorld.addRigidBody(bezPart.getRigidBody());
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
}