package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.utils.SlideBuilder;
import fh.teamproject.utils.SlideGenerator;

/**
 * Diese Klasse uebernimmt die Generierung und Darstellung der Slide. Es wird
 * zusaetzlich die Erstellung des BulletCollisionShape erledigt.
 * 
 * @author stefanprobst
 * 
 */
public class Slide implements ISlide {
	private static SlideGenerator slideGenerator = new SlideGenerator();
	private SlideBuilder slideBuilder = new SlideBuilder();
	private Coins coins;
	private int actualSlidePartId;

	Array<ISlidePart> slideParts = new Array<ISlidePart>();
	btDiscreteDynamicsWorld dynamicsWorld;
	SlidePartPool pool = new SlidePartPool();
	Array<SlideBorder> borders = new Array<SlideBorder>();
	private float slidedDistance = 0.0f;
	private float displayedPoints = 0.0f;
	CatmullRomSpline<Vector3> spline = new CatmullRomSpline<Vector3>();
	Model slideModel;
	ModelInstance slideModelInstance;

	public Slide(btDiscreteDynamicsWorld dynamicsWorld, Coins coins) {
		this.dynamicsWorld = dynamicsWorld;
		this.coins = coins;

		Array<Vector3> controlPoints = Slide.slideGenerator.initControlPoints();
		controlPoints.shrink();
		spline.set(controlPoints.items, false);
		slideModelInstance = new ModelInstance(new Model());

		Node slidePartNode = slideBuilder.createSlidePart(spline);
		String id = "slidePart_" + slideParts.size + 1;
		slidePartNode.id = id;
		slideModelInstance.nodes.add(slidePartNode);
		ISlidePart nextPart = pool.obtain().setSlide(this).setID(id).setSpline(spline);
		nextPart.init();
		slideParts.add(nextPart);
		dynamicsWorld.addRigidBody(nextPart.getRigidBody());
		for (int i = 0; i < 20; i++) {
			addSlidePart();
		}
		for (Vector3 vector3 : controlPoints) {
			Gdx.app.log("slide", "" + vector3);
			// Coins adden.
			this.coins.addCoin(vector3);
		}
	}

	@Override
	public void update(Vector3 playerPosition) {
		ISlidePart slidePart = slideParts.get(0);

		// // Update slideDistance.
		// for (Vector3 controlPoint : slidePart.getGraphicVertices()) {
		// if (nearestControlPoint == null) {
		// nearestControlPoint = controlPoint;
		// slidedDistance += nearestControlPoint.dst(new Vector3());
		// }
		//
		// if (playerPosition.dst(controlPoint) < playerPosition
		// .dst(nearestControlPoint)) {
		// slidedDistance += nearestControlPoint.dst(controlPoint);
		// nearestControlPoint = controlPoint;
		// }
		// }
	}

	@Override
	public CatmullRomSpline<Vector3> getSpline() {
		return spline;
	}

	@Override
	public float getSlidedDistance() {
		this.displayedPoints = Interpolation.linear.apply(this.displayedPoints,
				this.slidedDistance, 0.1f);
		return this.displayedPoints;
	}

	@Override
	public Array<ISlidePart> getSlideParts() {
		return slideParts;
	}

	@Override
	public void removeSlidePart(ISlidePart slidePart) {
		slideParts.removeValue(slidePart, false);
	}

	public ModelInstance getModelInstance() {
		return slideModelInstance;
	}

	@Override
	public void addSlidePart() {
		String id = "slidePart_" + slideParts.size + 1;
		Array<Vector3> controlPoints = new Array<Vector3>(spline.controlPoints);
		Slide.slideGenerator.addSpan(controlPoints);
		controlPoints.shrink();
		spline.set(controlPoints.items, false);

		Node slidePartNode = slideBuilder.createSlidePart(spline);
		slidePartNode.id = id;
		slideModelInstance.nodes.add(slidePartNode);
		ISlidePart nextPart = pool.obtain().setSlide(this).setID(id).setSpline(spline);
		nextPart.init();
		slideParts.add(nextPart);
		dynamicsWorld.addRigidBody(nextPart.getRigidBody());
	}

	/**
	 * Setzt die ID des aktuell berutschten SlideParts.
	 */
	public void setActualSlidePartId(int id) {
		this.actualSlidePartId = id;
	}

	/**
	 * Liefert die ID der SlidePart die zur Zeit berutscht wird.
	 */
	public int getActualSlidePartId() {
		return this.actualSlidePartId;
	}
}
