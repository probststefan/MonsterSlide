package fh.teamproject.interfaces;

import com.badlogic.gdx.math.Vector3;

public interface ISlidePart extends ICollisionEntity {

	public ISlidePart set(Vector3 start, Vector3 end, Vector3 control1, Vector3 control2,
			float splitting);
}
