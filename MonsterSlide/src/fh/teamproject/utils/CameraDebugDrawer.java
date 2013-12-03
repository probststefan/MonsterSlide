package fh.teamproject.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class CameraDebugDrawer {

	CameraManager manager;
	ShapeRenderer renderer;

	public CameraDebugDrawer(CameraManager camManager) {
		this.manager = camManager;
		this.renderer = new ShapeRenderer();
	}

	public void render() {
		this.renderer.setProjectionMatrix(this.manager.activeCamera.getCamera().combined);
		this.renderer.begin(ShapeType.Line);
		for (Camera c : this.manager.getCameras()) {
			this.drawCamera(c);
		}
		this.renderer.end();
	}

	private void drawCamera(Camera camera) {
		this.renderer.setColor(Color.WHITE);
		Vector3 pos = camera.position.cpy();
		this.renderer.box(pos.x - 1f, pos.y - 1, pos.z + 1f, 2, 2, 2);

		this.renderer.setColor(Color.BLUE);
		this.renderer.line(camera.position, camera.position.cpy().add(camera.direction));

		this.renderer.setColor(Color.RED);
		this.renderer.line(camera.position, camera.position.cpy().add(camera.up));
	}
}
