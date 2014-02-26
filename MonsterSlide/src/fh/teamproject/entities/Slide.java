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
	private int actualSlidePartId = -1;

	Array<ISlidePart> slideParts = new Array<ISlidePart>();
	btDiscreteDynamicsWorld dynamicsWorld;
	SlidePartPool pool = new SlidePartPool();
	Array<SlideBorder> borders = new Array<SlideBorder>();
	private float slidedDistance = 0.0f;
	private float displayedPoints = 0.0f;
	CatmullRomSpline<Vector3> spline = new CatmullRomSpline<Vector3>();
	Model slideModel;
	ModelInstance slideModelInstance;
	World world;

	public Slide(World world, btDiscreteDynamicsWorld dynamicsWorld, Coins coins) {
		this.dynamicsWorld = dynamicsWorld;
		this.world = world;
		this.coins = coins;

		Array<Vector3> controlPoints = Slide.slideGenerator.initControlPoints();
		controlPoints.shrink();
		spline.set(controlPoints.items, false);
		slideModelInstance = new ModelInstance(new Model());
		addSlidePart();
		// addSlidePart();
	}

	@Override
	public void update() {
		for (ISlidePart p : disposables) {
			p.dispose();
		}
		disposables.clear();
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
		nextPart.setWorld(world);
		nextPart.initPhysix();
		slideParts.add(nextPart);
	}

	private Array<ISlidePart> disposables = new Array<ISlidePart>(4);
	/**
	 * Setzt die ID des aktuell berutschten SlideParts.
	 */
	public void setActualSlidePartId(int id) {
		if (actualSlidePartId != id) {
			for (ISlidePart part : slideParts) {
				if (part.getID() == id) {
					disposables.add(part);
				}
			}
			addSlidePart();
		}
		this.actualSlidePartId = id;
	}

	/**
	 * Liefert die ID der SlidePart die zur Zeit berutscht wird.
	 */
	public int getActualSlidePartId() {
		return this.actualSlidePartId;
	}
}
