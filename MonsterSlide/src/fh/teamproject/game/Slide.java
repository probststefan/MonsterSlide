package fh.teamproject.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.game.entities.Coins;
import fh.teamproject.game.entities.DebugSlidePart;
import fh.teamproject.game.entities.SlideBorder;
import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.utils.CRSpline;
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

	private SlideGenerator slideGenerator = new SlideGenerator();
	private SlideBuilder slideBuilder = new SlideBuilder();
	private Coins coins;
	private int actualSlidePartId = 0;

	Array<ISlidePart> slideParts = new Array<ISlidePart>();
	btDiscreteDynamicsWorld dynamicsWorld;
	SlidePartPool pool;
	Array<SlideBorder> borders = new Array<SlideBorder>();
	CRSpline spline = new CRSpline();
	Model slideModel;
	ModelInstance slideModelInstance;
	World world;

	public Slide(World world) {
		this.world = world;

	}

	public void init() {
		this.dynamicsWorld = world.getPhysixManager().getWorld();
		this.coins = this.world.getCoins();
		this.pool = new SlidePartPool(world);
		Array<Vector3> controlPoints = slideGenerator.initControlPoints();
		controlPoints.shrink();
		spline.set(controlPoints.items, false);
		slideModelInstance = new ModelInstance(new Model());
		if (!world.getGameScreen().settings.DEBUG_HILL) {
			addSlidePart();
			addSlidePart();
		} else {
			DebugSlidePart part = new DebugSlidePart(world);
			part.setSlide(this);
			part.initGraphix();
			part.initPhysix();
			slideParts.add(part);
		}
		actualSlidePartId = slideParts.first().getID();
	}

	Vector3 lastMeasurement = new Vector3();

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

		int span = spline.nearest(world.getPlayer().getPosition(), 1, spline.spanCount);
		float t = spline.approximate(world.getPlayer().getPosition(), span);
		Vector3 closest = new Vector3();
		spline.valueAt(closest, span - 1, t);
		float dist = closest.cpy().sub(lastMeasurement).len();
		world.getScore().incrementSlidedDistance(dist);
		Gdx.app.debug("Slide", "Span: " + span + " - T-Faktor: " + t + "\n - Punkt:"
				+ closest
				+ " - Distance: " + dist);
		lastMeasurement.set(closest);
	}

	@Override
	public CRSpline getSpline() {
		return spline;
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
		Array<Vector3> controlPoints = new Array<Vector3>(spline.controlPoints);
		slideGenerator.addSpan(controlPoints);
		controlPoints.shrink();
		spline.set(controlPoints.items, false);
		ISlidePart nextPart = pool.obtain().setSlide(this);
		slideParts.add(nextPart);
		// coins.addCoin(spline.controlPoints[spline.controlPoints.length - 1]);
		coins.generateCoinsforSpan(spline.spanCount - 1);
	}

	private Array<ISlidePart> disposables = new Array<ISlidePart>(4);
	private boolean addNextPart = false;

	/**
	 * Setzt die ID des aktuell berutschten SlideParts.
	 */
	public synchronized void setActualSlidePartId(int id) {
		// Gdx.app.debug("Slide", "Actual Slide Part ID: " + id);
		if (actualSlidePartId != id && actualSlidePartId < id) {
			for (ISlidePart part : slideParts) {
				if (part.getID() == actualSlidePartId) {
					removeSlidePart(part);
				}
			}
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

	public SlideGenerator getSlideGenerator() {
		return slideGenerator;
	}

	public SlideBuilder getSlideBuilder() {
		return slideBuilder;
	}

}
