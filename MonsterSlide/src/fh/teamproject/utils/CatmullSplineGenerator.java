package fh.teamproject.utils;

import com.badlogic.gdx.math.ConvexHull;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

import fh.teamproject.screens.GameScreen;

public class CatmullSplineGenerator {

	private ConvexHull convexHull;
	private Vector2[] resultSet;

	public CatmullSplineGenerator() {
		this.convexHull = new ConvexHull();
	}

	public Vector3[] getPoints() {
		Vector3[] result = new Vector3[this.resultSet.length];

		for (int i = 0; i < this.resultSet.length; i++) {
			result[i] = new Vector3(this.resultSet[i].x, 0.0f, this.resultSet[i].y);
		}

		return result;
	}

	/**
	 * Liefert Torsten eine gerade, rutschbare Strecke.
	 * 
	 * @return Vector3[]
	 */
	public Vector3[] getPlankPoints() {
		Vector3[] result = new Vector3[4];

		result[0] = new Vector3(-10.0f, 0.0f, 0.0f);
		result[1] = new Vector3(0.0f, 0.0f, 0.0f);
		result[2] = new Vector3(GameScreen.settings.DEBUG_HILL_SLOPE, 0.0f, 0.0f);
		result[3] = new Vector3(GameScreen.settings.DEBUG_HILL_SLOPE + 40.0f, 0.0f, 0.0f);

		return result;
	}

	public void generateSlide() {
		// http://bordeen.blogspot.de/2013/12/how-to-generate-procedural-racetracks.html
		int pointCount = MathUtils.random(10, 15);

		float[] points = new float[pointCount * 2];

		float x, y;
		for (int i = 0; i < points.length; i += 2) {
			x = MathUtils.random(20.0f, 200);
			y = MathUtils.random(20.0f, 200);

			points[i] = x;
			points[i + 1] = y;
		}

		FloatArray dataSet = convexHull.computePolygon(points, false, false);

		resultSet = new Vector2[dataSet.size / 2];

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i] = new Vector2(dataSet.get(i), dataSet.get(i * 2 + 1));
		}

		// Den Startpunkt auf 0 setzen, damit wir die Spline auch finden.
		resultSet[1] = new Vector2(0.0f, 0.0f);

		int pushIterations = 10;
		for (int i = 0; i < pushIterations; ++i) {
			this.fixAngles(resultSet);
			this.pushApart(resultSet);
		}
	}

	public void fixAngles(Vector2[] dataSet) {
		for (int i = 0; i < dataSet.length; ++i) {
			int previous = (i - 1 < 0) ? dataSet.length - 1 : i - 1;
			int next = (i + 1) % dataSet.length;
			float px = dataSet[i].x - dataSet[previous].x;
			float py = dataSet[i].y - dataSet[previous].y;
			float pl = (float) Math.sqrt(px * px + py * py);
			px /= pl;
			py /= pl;

			float nx = dataSet[i].x - dataSet[next].x;
			float ny = dataSet[i].y - dataSet[next].y;
			nx = -nx;
			ny = -ny;
			float nl = (float) Math.sqrt(nx * nx + ny * ny);
			nx /= nl;
			ny /= nl;
			// I got a vector going to the next and to the previous points,
			// normalised.

			float a = (float) MathUtils.atan2(px * ny - py * nx, px * nx + py * ny); // perp
																						// dot
																						// product
																						// between
																						// the
																						// previous
																						// and
																						// next
																						// point.
																						// Google
																						// it
																						// you
																						// should
																						// learn
																						// about
																						// it!

			if (Math.abs(a * MathUtils.radDeg) <= 100)
				continue;

			float nA = 100 * Math.signum(a) * MathUtils.degRad;
			float diff = nA - a;
			float cos = (float) Math.cos(diff);
			float sin = (float) Math.sin(diff);
			float newX = nx * cos - ny * sin;
			float newY = nx * sin + ny * cos;
			newX *= nl;
			newY *= nl;
			dataSet[next].x = dataSet[i].x + newX;
			dataSet[next].y = dataSet[i].y + newY;
			// I got the difference between the current angle and 100degrees,
			// and built a new vector that puts the next point at 100 degrees.
		}
	}

	/**
	 * Naheliegende Punkte auseinanderschieben.
	 * 
	 * @param dataSet
	 */
	private void pushApart(Vector2[] dataSet) {
		float dst = 15; // I found that 15 is a good value, though maybe,
						// depending on your scale you'll need other value.
		float dst2 = dst * dst;
		for (int i = 0; i < dataSet.length; ++i) {
			for (int j = i + 1; j < dataSet.length; ++j) {
				if (dataSet[i].dst2(dataSet[j]) < dst2) {
					float hx = dataSet[j].x - dataSet[i].x;
					float hy = dataSet[j].y - dataSet[i].y;
					float hl = (float) Math.sqrt(hx * hx + hy * hy);
					hx /= hl;
					hy /= hl;
					float dif = dst - hl;
					hx *= dif;
					hy *= dif;
					dataSet[j].x += hx;
					dataSet[j].y += hy;
					dataSet[i].x -= hx;
					dataSet[i].y -= hy;
				}
			}
		}
	}
}