package fh.teamproject.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.screens.GameScreen;

public class SlideGenerator {
	public enum Plane {
		XY, XZ, YZ
	}

	float segments = GameScreen.settings.SLIDE_SEGMENTS;
	float segment_Min_Length = 50f;
	float segment_Max_Length = 200f;
	float curveDirection = 1f;// verschiebungsrichtung auf z achse
	float maxSlope = -0.1f;
	float minSlope = -0.6f;
	float maxCurvyness = 0.5f;
	float minCurvyness = -0.5f;
	float curvyness;

	public Array<Vector3> initControlPoints() {
		Array<Vector3> controlPoints = new Array<Vector3>(false, 4, Vector3.class);
		Vector3 start = new Vector3();
		controlPoints.add(start.cpy()); // Anfangskontrollpunkt
		controlPoints.add(start.cpy()); // Erster Punkt

		addSpan(controlPoints);

		/* das dahinterliegende Array auf tatsächliche größe schrumpfen */
		controlPoints.shrink();
		return controlPoints;
	}

	public Array<Vector3> addSpan(Array<Vector3> controlPoints) {
		float segmentLength = MathUtils.random(segment_Min_Length, segment_Max_Length);
		Vector3 refPoint = controlPoints.get(controlPoints.size - 1);
		float x = refPoint.x + segmentLength;

		float slope = MathUtils.random(maxSlope, minSlope);
		Vector3 tmp = getPointOnLine(refPoint, slope, x, Plane.XY);
		if (controlPoints.size % 2 == 0) {
			curvyness = MathUtils.random(maxCurvyness, minCurvyness);
			Vector3 curvy = getPointOnLine(refPoint, curvyness, x, Plane.XZ);
			tmp.z = curvy.z * curveDirection;
			// curveDirection *= -1f;
		}
		controlPoints.add(tmp);
		// Gdx.app.log("slide generator", "Length: " + segmentLength + "Point: "
		// + tmp
		// + " -- Curvyness: " + curvyness
		// + " -- Slope: " + slope);
		return controlPoints;
	}

	/**
	 * Berechnet den Y Wert auf der Geraden durch den Punkt point mit Steigung
	 * slope. Die Berechnung findet gegen die XY-Ebene statt.
	 * 
	 * @param point
	 * @param slope
	 * @param x
	 * @return
	 */
	private Vector3 getPointOnLine(Vector3 point, float slope, float value, Plane plane) {
		switch (plane) {
		case XY:
			return new Vector3(value, (slope * (value - point.x)) + point.y, 0f);
		case XZ:
			return new Vector3(value, 0f, (slope * (value - point.x)) + point.z);
		case YZ:
			return new Vector3(0f, value, (slope * (value - point.z)));
		default:
			return null;
		}
	}
}
