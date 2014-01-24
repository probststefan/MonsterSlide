package fh.teamproject.utils;

import com.badlogic.gdx.math.ConvexHull;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

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

	public void generateSlide() {
		// http://bordeen.blogspot.de/2013/12/how-to-generate-procedural-racetracks.html
		int pointCount = MathUtils.random(10, 20);

		pointCount = 5;
		float[] points = new float[pointCount * 2];

		points[0] = -10.0f;
		points[1] = 10.0f;

		points[2] = 20.0f;
		points[3] = 10.0f;

		points[4] = 40.0f;
		points[5] = 40.0f;

		points[6] = 80.0f;
		points[7] = 80.0f;

		/*
		 * float[] points = new float[pointCount * 2 + 2];
		 * 
		 * points[0] = 0.0f; points[1] = 0.0f;
		 * 
		 * for (int i = 1, j = 1; i < pointCount; ++i, j += 2) { float x =
		 * MathUtils.random(0.0f, Gdx.graphics.getWidth()); float y =
		 * MathUtils.random(0.0f, Gdx.graphics.getHeight());
		 * 
		 * points[j] = x; points[j + 1] = y; }
		 */

		FloatArray dataSet = convexHull.computePolygon(points, false, false);

		resultSet = new Vector2[dataSet.size / 2];
		for (int i = 0, j = 0; i < dataSet.size; i += 2, j++) {
			resultSet[j] = new Vector2(dataSet.get(i), dataSet.get(i + 1));
		}

		int pushIterations = 3;
		for (int i = 0; i < pushIterations; ++i) {
			this.pushApart(resultSet);
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