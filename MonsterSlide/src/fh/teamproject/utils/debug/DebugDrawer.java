package fh.teamproject.utils.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import fh.teamproject.entities.SlidePart;
import fh.teamproject.interfaces.ICameraController;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;

public class DebugDrawer {

	public GameScreen gameScreen;
	public static boolean isDebug = false;
	public DebugInfoPanel infoPanel;
	public BulletDebugDrawer bulletdebugDrawer = null;
	ModelBatch batch = new ModelBatch();
	private ShapeRenderer renderer;

	public DebugDrawer(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		renderer = new ShapeRenderer();
		infoPanel = new DebugInfoPanel();
		infoPanel.showInfo(this.gameScreen.player);

		setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe,
				GameScreen.camManager.getActiveCamera().combined);

	}

	public void render() {
		renderCameras();
		renderBullet();
		renderSlideParts();
		infoPanel.render();
	}

	public void toggleDebug() {
		DebugDrawer.isDebug = !DebugDrawer.isDebug;
		infoPanel.root.setVisible(!infoPanel.root.isVisible());
		Gdx.app.log("Debugger", "" + infoPanel.root.isVisible());
	}

	private void renderBullet() {

		if ((bulletdebugDrawer.getDebugMode() > 0)) {
			bulletdebugDrawer.begin();
			gameScreen.world.getWorld().debugDrawWorld();
			bulletdebugDrawer.end();
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
			setDebugMode(getDebugMode(), GameScreen.camManager.getActiveCamera().combined);
			Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		}
	}

	private void renderCameras() {
		renderer.setProjectionMatrix(GameScreen.camManager.activeCamera.getCamera().combined);
		renderer.begin(ShapeType.Line);

		for (ICameraController cont : GameScreen.camManager.getController()) {
			if (cont instanceof DebugCameraController) {
				continue;
			}
			Camera camera = cont.getCamera();
			renderer.setColor(Color.WHITE);
			Vector3 pos = camera.position.cpy();
			renderer.box(pos.x - 1f, pos.y - 1, pos.z + 1f, 2, 2, 2);

			renderer.setColor(Color.BLUE);
			renderer.line(camera.position, camera.position.cpy().add(camera.direction));

			renderer.setColor(Color.RED);
			renderer.line(camera.position, camera.position.cpy().add(camera.up));
		}
		renderer.end();
	}

	Pool<ModelInstance> spherePool = new Pool<ModelInstance>() {
		float diameter = 0.3f;
		Model sphereModel = new ModelBuilder().createSphere(diameter, diameter, diameter,
				4, 4, new Material(ColorAttribute.createDiffuse(Color.WHITE)),
				Usage.Position);

		@Override
		protected ModelInstance newObject() {
			return new ModelInstance(sphereModel);
		}
	};

	private void renderSlideParts() {
		Array<ModelInstance> usedSpheres = new Array<ModelInstance>();
		batch.begin(GameScreen.camManager.getActiveCamera());
		ModelInstance tmp;
		for (ISlidePart part : gameScreen.world.getSlide().getSlideParts()) {
			SlidePart bPart = (SlidePart) part;
			/* Graphic Punkte */
			for (Vector3 v : bPart.getGraphicVertices()) {
				tmp = spherePool.obtain();
				ColorAttribute attr = (ColorAttribute) tmp.materials.first().get(
						ColorAttribute.Diffuse);
				attr.color.set(Color.YELLOW);
				tmp.transform.setToTranslation(v);
				// batch.render(tmp);
				usedSpheres.add(tmp);
			}

			/* Spline Punkte */
			for (Vector3 v : bPart.getInterpolatedVertices()) {
				tmp = spherePool.obtain();
				tmp.transform.setToTranslation(v.cpy());
				ColorAttribute attr = (ColorAttribute) tmp.materials.first().get(
						ColorAttribute.Diffuse);
				attr.color.set(Color.BLUE);
				batch.render(tmp);
				usedSpheres.add(tmp);
			}
			for (Vector3 v : bPart.getControlPoints()) {
				tmp = spherePool.obtain();
				tmp.transform.setToTranslation(v.cpy());
				ColorAttribute attr = (ColorAttribute) tmp.materials.first().get(
						ColorAttribute.Diffuse);
				attr.color.set(Color.MAGENTA);
				batch.render(tmp);
				usedSpheres.add(tmp);
			}

		}
		batch.end();
		spherePool.freeAll(usedSpheres);

		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.BLUE);
		renderer.setProjectionMatrix(GameScreen.camManager.getActiveCamera().combined);
		for (ISlidePart part : gameScreen.world.getSlide().getSlideParts()) {
			SlidePart bPart = (SlidePart) part;
			Mesh mesh = bPart.mesh; // instance.nodes.first().parts.first().meshPart.mesh;
			float[] verts = new float[mesh.getNumVertices() * 10];
			short[] indices = new short[mesh.getNumIndices()];
			mesh.getIndices(indices);
			mesh.getVertices(verts);
			Vector3 point = new Vector3();
			Vector3 normal = new Vector3();
			for (int i = 0; i < verts.length; i += 10) {
				point.set(verts[i + 0], verts[i + 1], verts[i + 2]);
				normal.set(point).add(verts[i + 7],
						verts[i + 8], verts[i + 9]);
				// Gdx.app.log("debugdrawer", "Point " + point + " Normal " +
				// normal);

				// Gdx.app.log("debugdrawer", );
				renderer.line(point, normal);
			}
		}
		renderer.end();
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
		return (bulletdebugDrawer == null) ? 0 : bulletdebugDrawer.getDebugMode();
	}
}
