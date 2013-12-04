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

import fh.teamproject.screens.GameScreen;

public class DebugDrawer {

	public GameScreen gameScreen;

	public DebugInfoPanel infoPanel;
	public BulletDebugDrawer bulletdebugDrawer = null;

	private ShapeRenderer renderer;

	public DebugDrawer(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		this.renderer = new ShapeRenderer();
		this.infoPanel = new DebugInfoPanel();
		this.infoPanel.showInfo(this.gameScreen.player);

		this.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe,
				gameScreen.camManager.getActiveCamera().combined);

	}

	public void render() {
		this.renderCameras();
		this.renderBullet();
		this.infoPanel.render();
	}

	private void renderBullet() {

		if ((this.bulletdebugDrawer.getDebugMode() > 0)) {
			this.bulletdebugDrawer.begin();
			this.gameScreen.world.getWorld().debugDrawWorld();
			this.bulletdebugDrawer.end();
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
			this.setDebugMode(this.getDebugMode(),
					this.gameScreen.camManager.getActiveCamera().combined);
			Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		}
	}

	private void renderCameras() {
		this.renderer.setProjectionMatrix(this.gameScreen.camManager.activeCamera
				.getCamera().combined);
		this.renderer.begin(ShapeType.Line);

		for (Camera camera : this.gameScreen.camManager.getCameras()) {

			this.renderer.setColor(Color.WHITE);
			Vector3 pos = camera.position.cpy();
			this.renderer.box(pos.x - 1f, pos.y - 1, pos.z + 1f, 2, 2, 2);

			this.renderer.setColor(Color.BLUE);
			this.renderer.line(camera.position,
					camera.position.cpy().add(camera.direction));

			this.renderer.setColor(Color.RED);
			this.renderer.line(camera.position, camera.position.cpy().add(camera.up));
		}
		this.renderer.end();
	}

	public void setDebugMode(final int mode, final Matrix4 projMatrix) {
		if ((mode == btIDebugDraw.DebugDrawModes.DBG_NoDebug)
				&& (this.bulletdebugDrawer == null)) {
			return;
		}
		if (this.bulletdebugDrawer == null) {
			this.gameScreen.world.getWorld().setDebugDrawer(
					this.bulletdebugDrawer = new BulletDebugDrawer());
		}
		this.bulletdebugDrawer.lineRenderer.setProjectionMatrix(projMatrix);
		this.bulletdebugDrawer.setDebugMode(mode);
	}

	public int getDebugMode() {
		return (this.bulletdebugDrawer == null) ? 0 : this.bulletdebugDrawer
				.getDebugMode();
	}
}
