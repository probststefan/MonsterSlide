package fh.teamproject.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

public class CatmullRomSpline<T extends Vector<T>> implements Path<T> {

	Array<T> controlPoints;
	int spanCount;

	private T tmp;

	public CatmullRomSpline(Array<T> controlPoints) {
		set(controlPoints);
	}

	public CatmullRomSpline() {
	}

	public void set(Array<T> controlPoints) {
		this.controlPoints = controlPoints;
		spanCount = controlPoints.size - 3;
		tmp = controlPoints.first();
	}

	@Override
	public T derivativeAt(T out, float t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T valueAt(T out, float t) {
		return null;
	}

	public T valueAt(T out, int i, float u) {
		final int n = controlPoints.size;
		final float u2 = u * u;
		final float u3 = u2 * u;
		out.set(controlPoints.get(i)).scl(1.5f * u3 - 2.5f * u2 + 1.0f);
		if (i > 0)
			out.add(tmp.set(controlPoints.get((n + i - 1) % n)).scl(
					-0.5f * u3 + u2 - 0.5f * u));
		if (i < (n - 1))
			out.add(tmp.set(controlPoints.get((i + 1) % n)).scl(
					-1.5f * u3 + 2f * u2 + 0.5f * u));
		if (i < (n - 2))
			out.add(tmp.set(controlPoints.get((i + 2) % n)).scl(0.5f * u3 - 0.5f * u2));
		return out;
	}

	@Override
	public float approximate(T v) {
		return approximate(v, nearest(v, 0, spanCount));
	}

	@Override
	public float locate(T v) {
		return approximate(v);
	}

	public int nearest(final T in, int start, final int count) {
		while (start < 0)
			start += spanCount;
		int result = start;
		float dst = in.dst2(controlPoints.get(result));
		for (int i = 1; i <= count; i++) {
			final int idx = (start + i);
			final float d = in.dst2(controlPoints.get(idx));
			if (d < dst) {
				dst = d;
				result = idx;
			}
		}
		return result;
	}

	public float approximate(final T in, final int near) {
		int n = near;
		final T nearest = controlPoints.get(n);
		final T previous = controlPoints.get(n > 0 ? n - 1 : spanCount - 1);
		final T next = controlPoints.get((n + 1) % spanCount);
		final float dstPrev2 = in.dst2(previous);
		final float dstNext2 = in.dst2(next);
		T P1, P2, P3;
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
		return ((float) n + u) / spanCount;
	}

}
