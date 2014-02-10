package fh.teamproject.utils.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;

/** @author xoppa */
public class BulletDebugDrawer extends btIDebugDraw {
	public int debugMode = 0;
	public ShapeRenderer lineRenderer = new ShapeRenderer();
	private int i = 0;
	private Color colortmp;

	@Override
	public void drawLine(btVector3 from, btVector3 to, btVector3 color) {
		switch (i) {
		case 0:
			colortmp = Color.BLUE;
			break;
		case 1:
			colortmp = Color.GREEN;
			break;
		case 2:
			colortmp = Color.ORANGE;
			break;
		default:
			break;
		}

		lineRenderer.setColor(colortmp.r, colortmp.g, colortmp.b, 1f);
		// lineRenderer.setColor(color.getX(), color.getY(), color.getZ(), 1f);
		lineRenderer.line(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(),
				to.getZ());

		if (i == 2) {
			i = 0;
		} else {
			i++;
		}
	}

	@Override
	public void drawContactPoint(btVector3 PointOnB, btVector3 normalOnB, float distance,
			int lifeTime, btVector3 color) {
		lineRenderer.setColor(color.getX(), color.getY(), color.getZ(), 1f);
		lineRenderer.line(0.0f, 0.0f, 0.0f, PointOnB.getX(), PointOnB.getY(),
				PointOnB.getZ());
	}

	@Override
	public void reportErrorWarning(String warningString) {
		System.out.println(warningString);
	}

	@Override
	public void draw3dText(btVector3 location, String textString) {
	}

	@Override
	public void setDebugMode(int debugMode) {
		this.debugMode = debugMode;
	}

	@Override
	public int getDebugMode() {
		return debugMode;
	}

	public void begin() {
		lineRenderer.begin(ShapeType.Line);
	}

	public void end() {
		lineRenderer.end();
	}
}