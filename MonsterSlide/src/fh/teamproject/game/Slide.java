package fh.teamproject.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.game.entities.Coins;
import fh.teamproject.game.entities.DebugSlidePart;
import fh.teamproject.game.entities.SlideBorder;
import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;
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
	public final int MAX_ACTIVE_SLIDEPARTS = 5;
	private SlideGenerator slideGenerator = new SlideGenerator();
	private SlideBuilder slideBuilder = new SlideBuilder();
	private Coins coins;

	Array<ISlidePart> slideParts = new Array<ISlidePart>();
	btDiscreteDynamicsWorld dynamicsWorld;
	SlidePartPool pool;
	Array<SlideBorder> borders = new Array<SlideBorder>();
	CRSpline spline = new CRSpline();
	Model slideModel;
	ModelInstance slideModelInstance;
	World world;

	private Array<ISlidePart> disposables = new Array<ISlidePart>(4);
	/* Benötigt zur Berechnung der gerutschten Strecke */
	Vector3 lastMeasurement = new Vector3();
	private int currentSpan = 1;
	private float pathOnCurrentSpan = 0f;

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
			addSlidePart();
		} else {
			DebugSlidePart part = new DebugSlidePart(world);
			part.initGraphix();
			part.initPhysix();
			slideParts.add(part);
		}

		// Spieler auf Startposition setzen.
		Vector3 startPoint = this.spline.controlPoints[1].cpy();
		Vector3 derivation = new Vector3();
		derivation = spline.derivativeAt(derivation, 1, 0.0f);

		Vector3 upVector = new Vector3(0.0f, -1.0f, 0.0f);
		Vector3 binormal = derivation.cpy().crs(upVector).nor();
		binormal.scl(0.5f * GameScreen.settings.SLIDE_WIDTH);

		// FIXME: Magic Number - Die Weite vom Startpunkt ist hard coded!
		startPoint.add(binormal).add(derivation.nor().scl(10.0f));
		startPoint.y += 5.0f;

		world.getPlayer().resetAt(startPoint);
	}

	@Override
	public void update() {

		/* Berechnung der gerutschten Strecke */
		int span = spline.getSpan(world.getPlayer().getPosition(), 1, spline.spanCount);
		float t = spline.locate(world.getPlayer().getPosition());
		Vector3 closest = new Vector3();
		float dist = 0f;
		if (span > currentSpan) {
			addSlidePart();
			removeCompletedParts();
			pathOnCurrentSpan = 0f;
		}
		currentSpan = span;
		if (t >= pathOnCurrentSpan) {
			spline.valueAt(closest, span - 1, t);
			dist = closest.cpy().sub(lastMeasurement).len();
			world.getScore().incrementSlidedDistance(dist);
			pathOnCurrentSpan = t;
			lastMeasurement.set(closest);
		}
		// Gdx.app.debug("Slide", "Span: " + span + " - T-Faktor: " + t +
		// "\n Punkt:"
		// + closest + " - Distance: " + dist);
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
		ISlidePart nextPart = pool.obtain();
		slideParts.add(nextPart);
		coins.generateCoinsforSpan(spline.spanCount - 1);
	}

	public SlideGenerator getSlideGenerator() {
		return slideGenerator;
	}

	public SlideBuilder getSlideBuilder() {
		return slideBuilder;
	}

	@Override
	public void removeCompletedParts() {
		int size = slideParts.size;
		if (size > MAX_ACTIVE_SLIDEPARTS) {
			ISlidePart p = slideParts.first();
			p.releaseAll();
			p.dispose();
			slideParts.removeValue(p, false);
		}
	}

}
