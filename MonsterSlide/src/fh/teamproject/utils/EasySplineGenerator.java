package fh.teamproject.utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * 
 * @author stefanprobst
 * 
 */
public class EasySplineGenerator {

	private ArrayList<Bezier<Vector3>> bezierCurves;
	private ArrayList<Vector3> baseCoordinates;
	private int numOfSplines = 5;
	private float slope = 10.0f;

	public EasySplineGenerator() {
		this.bezierCurves = new ArrayList<Bezier<Vector3>>();
		this.generateSplines();
	}

	public EasySplineGenerator(int numOfSplines) {
		this();
		this.numOfSplines = numOfSplines;
	}

	public ArrayList<Bezier<Vector3>> getSplines() {
		return this.bezierCurves;
	}

	private void generateSplines() {
		Bezier<Vector3> bezier;

		Vector3[] points = new Vector3[] { new Vector3(-10.0f, 0.0f, 0.0f),
				new Vector3(25.0f, -this.slope / 2.0f, 0.0f),
				new Vector3(25.0f, -this.slope / 2.0f, 0.0f),
				new Vector3(50.0f, -this.slope, 0.0f) };

		for (int i = 0; i < this.numOfSplines; i++) {
			bezier = new Bezier<Vector3>();
			bezier.set(points);
			bezierCurves.add(bezier);

			// Neue Punkte erstellen.
			points[0] = points[3];

			// Tangenstetig und so :)
			points[1] = new Vector3(points[3].x - points[2].x + points[3].x, points[3].y
					- points[2].y + points[3].y, 0.0f);

			// Endpunkt der Spline
			// points[3] = new Vector3(MathUtils.random(0.0f, this.width), 0.0f,
			// MathUtils.random(0.0f, this.width));

			// points[3] = new Vector3(points[3].x + 50, points[3].y -
			// this.slope, 0);
			points[3] = new Vector3(points[3].x + 50, MathUtils.random(points[3].y,
					-50.0f), 0);

			points[2] = new Vector3(MathUtils.random(points[0].x, points[3].x),
					points[3].y + this.slope / 2.0f, 0.0f);
		}
	}
}