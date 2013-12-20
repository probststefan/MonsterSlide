package fh.teamproject.utils.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

import fh.teamproject.entities.SlidePart;
import fh.teamproject.interfaces.ICameraController;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;

public class DebugDrawer {

	public GameScreen gameScreen;
	public static boolean isDebug = false;
	public DebugInfoPanel infoPanel;
	public BulletDebugDrawer bulletdebugDrawer = null;

	private ShapeRenderer renderer;

	public DebugDrawer(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		renderer = new ShapeRenderer();
		infoPanel = new DebugInfoPanel();
		infoPanel.showInfo(this.gameScreen.player);

		setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe,
				gameScreen.camManager.getActiveCamera().combined);

	}

	public void render() {
		renderCameras();
		renderBullet();
		renderSlideParts();
		infoPanel.render();
	}

	public void toggleDebug() {
		DebugDrawer.isDebug=!DebugDrawer.isDebug;
		infoPanel.root.setVisible(!infoPanel.root.isVisible());
		Gdx.app.log("Debugger", "" + infoPanel.root.isVisible());
	}

	private void renderBullet() {

		if ((bulletdebugDrawer.getDebugMode() > 0)) {
			bulletdebugDrawer.begin();
			gameScreen.world.getWorld().debugDrawWorld();
			bulletdebugDrawer.end();
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
			setDebugMode(getDebugMode(),
					gameScreen.camManager.getActiveCamera().combined);
			Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		}
	}

	private void renderCameras() {
		renderer.setProjectionMatrix(gameScreen.camManager.activeCamera
				.getCamera().combined);
		renderer.begin(ShapeType.Line);

		for (ICameraController cont : gameScreen.camManager.getController()) {
			if (cont instanceof DebugCameraController) {
				continue;
			}
			Camera camera = cont.getCamera();
			renderer.setColor(Color.WHITE);
			Vector3 pos = camera.position.cpy();
			renderer.box(pos.x - 1f, pos.y - 1, pos.z + 1f, 2, 2, 2);

			renderer.setColor(Color.BLUE);
			renderer.line(camera.position,
					camera.position.cpy().add(camera.direction));

			renderer.setColor(Color.RED);
			renderer.line(camera.position, camera.position.cpy().add(camera.up));
		}
		renderer.end();
	}

	private void renderSlideParts() {
		for (ISlidePart part : gameScreen.world.getSlide().getSlideParts()) {
			SlidePart bPart = (SlidePart) part;
			renderer.begin(ShapeType.Point);
			renderer.setColor(Color.RED);
			renderer.point(bPart.start.x, bPart.start.y, bPart.start.z);
			renderer.point(bPart.control1.x, bPart.control1.y, bPart.control1.z);
			renderer.point(bPart.control2.x, bPart.control2.y, bPart.control2.z);
			renderer.point(bPart.end.x, bPart.end.y, bPart.end.z);
			renderer.end();

			renderer.begin(ShapeType.Line);
			renderer.setColor(Color.WHITE);
			renderer.line(bPart.start, bPart.control1);
			renderer.line(bPart.control2, bPart.end);
			renderer.end();
		}

	}
	public void setDebugMode(final int mode, final Matrix4 projMatrix) {
		if ((mode == btIDebugDraw.DebugDrawModes.DBG_NoDebug)
				&& (bulletdebugDrawer == null)) {
			return;
		}
		if (bulletdebugDrawer == null) {
			gameScreen.world.getWorld().setDebugDrawer(
					bulletdebugDrawer = new BulletDebugDrawer());
		}
		bulletdebugDrawer.lineRenderer.setProjectionMatrix(projMatrix);
		bulletdebugDrawer.setDebugMode(mode);
	}

	public int getDebugMode() {
		return (bulletdebugDrawer == null) ? 0 : bulletdebugDrawer
				.getDebugMode();
	}
}
