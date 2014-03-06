package fh.teamproject.entities;

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
	public static final int SLIDE_FLAG = 4;

	private static SlideGenerator slideGenerator = new SlideGenerator();
	private SlideBuilder slideBuilder = new SlideBuilder();
	private Coins coins;
	private int actualSlidePartId = 0;

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

	public Slide(World world) {
		this.dynamicsWorld = world.getPhysixManager().getWorld();
		this.world = world;
		this.coins = this.world.getCoins();

		Array<Vector3> controlPoints = Slide.slideGenerator.initControlPoints();
		controlPoints.shrink();
		spline.set(controlPoints.items, false);
		slideModelInstance = new ModelInstance(new Model());
		if (!world.gameScreen.settings.DEBUG_HILL) {
			addSlidePart();
			addSlidePart();
			// for (int i = 0; i < 15; i++) {
			// addSlidePart();
			// }
		} else {
			DebugSlidePart part = new DebugSlidePart();
			part.setWorld(world);
			part.setSlide(this);
			part.initGraphix();
			part.initPhysix();
			slideParts.add(part);
		}
		actualSlidePartId = slideParts.first().getID();
	}

	@Override
	public void update() {
		if (addNextPart) {
			addSlidePart();
			addNextPart = false;
		}
		for (ISlidePart p : disposables) {
			p.releaseAll();
			p.dispose();
			slideParts.removeValue(p, false);
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

	public ISlide setModelInstance(ModelInstance instance) {
		this.slideModelInstance = instance;
		return this;
	}
	@Override
	public void removeSlidePart(ISlidePart slidePart) {
		this.disposables.add(slidePart);
	}

	public ModelInstance getModelInstance() {
		return slideModelInstance;
	}

	@Override
	public void addSlidePart() {
		ISlidePart nextPart = pool.obtain().setSlide(this).setSpline(spline);
		System.out.println("addSlidePart " + String.valueOf(nextPart.getID()));
		Array<Vector3> controlPoints = new Array<Vector3>(spline.controlPoints);
		Slide.slideGenerator.addSpan(controlPoints);
		controlPoints.shrink();
		spline.set(controlPoints.items, false);

		Node slidePartNode = slideBuilder.createSlidePart(spline, 1);
		slidePartNode.id = String.valueOf(nextPart.getID());
		slideModelInstance.nodes.add(slidePartNode);
		nextPart.setWorld(world);
		nextPart.initPhysix();
		slideParts.add(nextPart);
	}

	private Array<ISlidePart> disposables = new Array<ISlidePart>(4);
	private boolean addNextPart = false;

	/**
	 * Setzt die ID des aktuell berutschten SlideParts.
	 */
	public synchronized void setActualSlidePartId(int id) {
		if (actualSlidePartId != id && actualSlidePartId < id) {
			System.out.println("current " + actualSlidePartId + " --- new " + id);
			for (ISlidePart part : slideParts) {
				if (part.getID() == actualSlidePartId) {
					removeSlidePart(part);
				}
			}
			System.out.println("adding");
			addNextPart = true;
			this.actualSlidePartId = id;
		}
	}

	/**
	 * Liefert die ID der SlidePart die zur Zeit berutscht wird.
	 */
	public int getActualSlidePartId() {
		return this.actualSlidePartId;
	}
}
