package fh.teamproject.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.screens.GameScreen;

public class SlideGenerator {
	public enum Plane {
		XY, XZ, YZ
	}

	public Array<Vector3> generateControlPoints() {
		Array<Vector3> controlPoints = new Array<Vector3>(false, 16, Vector3.class);
		float slope = GameScreen.settings.SLIDE_SLOPE;
		float segments = GameScreen.settings.SLIDE_SEGMENTS;
		float slideLength = GameScreen.settings.SLIDE_LENGTH;
		float curveDirection = 1f;// verschiebungsrichtung auf z achse
		float maxSlope = 0f;// -0.5f;
		float minSlope = 0f;// 0.1f;
		float maxCurvyness = 0f;// 0.1f;
		float minCurvyness = 0f;// -0.1f;
		float curvyness;
		Vector3 start = new Vector3(0f, 0f, 0f);
		controlPoints.add(start.cpy()); // Anfangskontrollpunkt
		controlPoints.add(start.cpy()); // Erster Punkt

		for (int i = 0; i < segments; i++) {
			float x = start.x + (((slideLength - start.x) / segments) * (i + 1));
			slope = -0.2f;// MathUtils.random(maxSlope, minSlope);
			curvyness = MathUtils.random(maxCurvyness, minCurvyness);
			Vector3 tmp = getStraightLineYValue(controlPoints.peek(), slope, x, Plane.XY);
			Vector3 curvy = getStraightLineYValue(controlPoints.peek(), curvyness, x,
					Plane.XZ);
			tmp.z += curvy.z * curveDirection;
			Gdx.app.log("Generator ", " Segment " + i + " " + tmp + " - Slope " + slope
					+ " - Z " + curvy.z);

			controlPoints.add(tmp);
		}
		/* Hier wird der letzte Punkt dupliziert und nochmal eingefügt */
		Vector3 last = controlPoints.peek().cpy(); // Endkontrollpunkt
		controlPoints.add(last);
		/* das dahinterliegende Array auf tatsächliche größe schrumpfen */
		controlPoints.shrink();
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
	private Vector3 getStraightLineYValue(Vector3 point, float slope, float value,
			Plane plane) {
		switch (plane) {
		case XY:
			return new Vector3(value, (slope * (value - point.x)) + point.y, 0f);
		case XZ:
			return new Vector3(value, 0f, (slope * (value - point.z)));
		case YZ:
			return new Vector3(0f, value, (slope * (value - point.z)));
		default:
			return null;
		}
	}
}
