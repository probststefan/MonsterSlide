package fh.teamproject.utils;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class CRSpline extends CatmullRomSpline<Vector3> {

	@Override
	public int nearest(Vector3 in, int start, int count) {
		while (start < 0)
			start += spanCount;
		int result = start;
		float dst = in.dst2(controlPoints[result]);
		for (int i = 1; i <= count; i++) {
			final int idx = (start + i);
			final float d = in.dst2(controlPoints[idx]);
			if (d < dst) {
				dst = d;
				result = idx;
			}
		}
		System.out.println("nearest");
		return result;

	}

	public float approximate(final Vector3 in, final int near) {
		System.out.println("approx");

		int n = near;
		final Vector3 nearest = controlPoints[n];
		final Vector3 previous = controlPoints[n > 0 ? n - 1 : spanCount - 1];
		final Vector3 next = controlPoints[(n + 1)];
		final float dstPrev2 = in.dst2(previous);
		final float dstNext2 = in.dst2(next);
		Vector3 P1, P2, P3;
		if (dstNext2 < dstPrev2) {
			P1 = nearest;
			P2 = next;
			P3 = in;
		} else {
			P1 = previous;
			P2 = nearest;
			P3 = in;
			n = n > 0 ? n - 1 : spanCount - 1;
		}
		float L1 = P1.dst(P2);
		float L2 = P3.dst(P2);
		float L3 = P3.dst(P1);
		float s = (L2 * L2 + L1 * L1 - L3 * L3) / (2 * L1);
		float u = MathUtils.clamp((L1 - s) / L1, 0f, 1f);
		// return ((float) n + u) / spanCount;
		return u;
	}

}
