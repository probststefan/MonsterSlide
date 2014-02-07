package fh.teamproject.utils;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.screens.GameScreen;

public class SlideGenerator {

	public Array<Vector3> generateControlPoints() {
		Array<Vector3> controlPoints = new Array<Vector3>(false, 16, Vector3.class);
		float slope = GameScreen.settings.SLIDE_SLOPE;
		float segments = GameScreen.settings.SLIDE_SEGMENTS;
		float curveOffset = 5f; // verschiebungsweite
		float curveDirection = 1f;// verschiebungsrichtung auf z achse
		Vector3 start = new Vector3(0f, 0f, 0f);
		Vector3 end = new Vector3(start).add(getStraightLineYValue(start, slope,
				GameScreen.settings.SLIDE_LENGTH));
		controlPoints.add(start.cpy()); // Anfangskontrollpunkt
		controlPoints.add(start.cpy()); // Erster Punkt

		for (int i = 0; i < GameScreen.settings.SLIDE_SEGMENTS; i++) {
			float x = start.x + (((end.x - start.x) / segments) * (i + 1));
			Vector3 tmp = getStraightLineYValue(start, slope, x);

			if (i == 2) {
				tmp.y += 10;
			}
			tmp.z += curveOffset * curveDirection;
			curveDirection *= -1f;

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
	private Vector3 getStraightLineYValue(Vector3 point, float slope, float x) {
		return new Vector3(x, (slope * (x - point.x)) + point.y, 0f);
	}
}
