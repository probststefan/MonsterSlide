package fh.teamproject.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMesh;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.ShortArray;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;

public class SlidePart extends CollisionEntity implements ISlidePart, Poolable {

	int width = 40;
	ShortArray indices = new ShortArray();

	public Vector3 start = new Vector3(), end = new Vector3(), control1 = new Vector3(),
			control2 = new Vector3();
	private FloatArray physicsPointCloud;
	Array<Vector3> bezierPoints = new Array<Vector3>();

	private float splitting = 0.25f;

	public Texture texture;
	public Mesh mesh;

	public SlidePart() {
		texture = new Texture(Gdx.files.internal("data/floor.jpg"));
	}

	public SlidePart(Vector3 start, Vector3 end, Vector3 control1, Vector3 control2,
			float splitting) {
		this.start.set(start);
		this.end.set(end);
		this.control1.set(control1);
		this.control2.set(control2);
		this.splitting = splitting;
		setup();
	}

	@Override
	public ISlidePart set(Vector3 start, Vector3 end, Vector3 control1, Vector3 control2,
			float splitting) {
		this.start.set(start);
		this.end.set(end);
		this.control1.set(control1);
		this.control2.set(control2);
		this.splitting = splitting;
		setup();
		return this;
	}

	private void setup() {
		computePointCloud();
		createModelInstance();
		btConvexHullShape collisionShape = new btConvexHullShape();
		for (int i = 0; i < physicsPointCloud.size; i += 3) {
			collisionShape.addPoint(new Vector3(physicsPointCloud.get(i),
					physicsPointCloud.get(i + 1), physicsPointCloud.get(i + 2)));
		}
		setCollisionShape(collisionShape);
		createRigidBody();
	}

	private void computePointCloud() {
		Bezier<Vector3> bezierCurve = new Bezier<Vector3>();

		bezierCurve.set(new Vector3[] { start, control1, control2, end });

		Vector3 tmpBezierVec = new Vector3();
		physicsPointCloud = new FloatArray();
		float epsilon = 0.001f;
		// for (float i = 0; i <= (1 + epsilon); i += splitting) {
		// // Punkte der Bezier Kurve setzen.
		// bezierCurve.valueAt(tmpBezierVec, i);
		// pointCloud.add(tmpBezierVec.x);
		// pointCloud.add(tmpBezierVec.y);
		// pointCloud.add(tmpBezierVec.z);
		// // Punkte der anderen Bezier Kurve setzen.
		// pointCloud.add(tmpBezierVec.x + width);
		// pointCloud.add(tmpBezierVec.y);
		// pointCloud.add(tmpBezierVec.z);
		// }
		//
		for (float i = 0; i <= (1 + epsilon); i += splitting) {
			// Punkte der Bezier Kurve setzen.
			bezierCurve.valueAt(tmpBezierVec, i);
			physicsPointCloud.add(tmpBezierVec.x);
			physicsPointCloud.add(tmpBezierVec.y);
			physicsPointCloud.add(tmpBezierVec.z);
			bezierPoints.add(new Vector3(tmpBezierVec));

		}
		for (int i = bezierPoints.size - 1; i >= 0; --i) {
			Vector3 v = bezierPoints.get(i);
			physicsPointCloud.addAll(v.x + width, v.y, v.z);
		}
		tmpBezierVec = null;
	}

	private void createModelInstance() {
		Array<VertexInfo> vertInfo = new Array<VertexInfo>();
		MeshBuilder builder = new MeshBuilder();
		builder.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3,
				ShaderProgram.POSITION_ATTRIBUTE)));
		// builder.begin(new VertexAttributes(new
		// VertexAttribute(Usage.Position, 3,
		// ShaderProgram.POSITION_ATTRIBUTE)));
		float uMin = 0, uMax = 1, vMin = 0, vMax = 1;

		Array<Vector3> graphicsVertices = new Array<Vector3>();
		for (int i = 0; i < bezierPoints.size; ++i) {
			Vector3 v = bezierPoints.get(i);
			graphicsVertices.add(new Vector3(v.x, v.y, v.z));
			graphicsVertices.add(new Vector3(v.x + width, v.y, v.z));
			if ((i == 0) || (i == (bezierPoints.size - 1))) {
				continue;
			}
			graphicsVertices.add(new Vector3(v.x, v.y, v.z));
			graphicsVertices.add(new Vector3(v.x + width, v.y, v.z));
		}

		// for (int i = 0; i <= (graphicsVertices.size - 4); i += 4) {
		// VertexInfo info = new VertexInfo();
		// Color col = null;// Color.BLUE;
		// Vector3 nor = new Vector3(0f, 1f, 0f);
		// info.set(graphicsVertices.get(i), nor, col, new Vector2(uMin, vMin));
		// vertInfo.add(info);
		//
		// info = new VertexInfo();
		// info.set(graphicsVertices.get(i + 1), nor, col, new Vector2(uMax,
		// vMin));
		// vertInfo.add(info);
		//
		// info = new VertexInfo();
		// info.set(graphicsVertices.get(i + 2), nor, col, new Vector2(uMin,
		// vMax));
		// vertInfo.add(info);
		//
		// info = new VertexInfo();
		// info.set(graphicsVertices.get(i + 3), nor, col, new Vector2(uMax,
		// vMax));
		// vertInfo.add(info);
		// }

		for (int i = 0; i <= (graphicsVertices.size - 4); i += 4) {
			VertexInfo info = new VertexInfo();
			Color col = null;// Color.BLUE;
			Vector3 nor = null;// new Vector3(0f, 1f, 0f);
			info.set(graphicsVertices.get(i), nor, col, null);
			vertInfo.add(info);

			info = new VertexInfo();
			info.set(graphicsVertices.get(i + 1), nor, col, null);
			vertInfo.add(info);

			info = new VertexInfo();
			info.set(graphicsVertices.get(i + 2), nor, col, null);
			vertInfo.add(info);

			info = new VertexInfo();
			info.set(graphicsVertices.get(i + 3), nor, col, null);
			vertInfo.add(info);
		}
		builder.part("part1", GL10.GL_TRIANGLES);
		for (int i = 0; i <= (vertInfo.size - 4); i += 4) {

			builder.triangle(vertInfo.get(i + 2), vertInfo.get(i + 1), vertInfo.get(i));
			builder.triangle(vertInfo.get(i + 2), vertInfo.get(i + 3),
					vertInfo.get(i + 1));
			// builder.rect(vertInfo.get(i), vertInfo.get(i + 1), vertInfo.get(i
			// + 3),
			// vertInfo.get(i + 2));

		}

		mesh = builder.end();

		MeshPart meshPart = new MeshPart();
		meshPart.id = "SlidePart" + id;
		meshPart.primitiveType = GL10.GL_TRIANGLES;
		meshPart.mesh = mesh;
		meshPart.indexOffset = 0;
		meshPart.numVertices = mesh.getNumVertices();


		Model m = new Model();

		ModelMesh mMesh = new ModelMesh();
		// mMesh.attributes = mesh.getVertexAttributes();
		m.nodes.add(new Node());
		Material material = new Material();
		material.set(TextureAttribute.createSpecular(texture));
		NodePart nodePart = new NodePart(meshPart, material);
		m.nodes.get(0).parts.add(nodePart);
		// m.meshes.add(mesh);
		// m.meshParts.add(meshPart);
		// m.materials.add(material);
		instance = new ModelInstance(m);

		out = new Renderable();
		nodePart.setRenderable(out);
		shader = new DefaultShader(out);
		renderContext = new RenderContext(new DefaultTextureBinder(
				DefaultTextureBinder.WEIGHTED, 1));
	}

	DefaultShader shader;
	Renderable out;
	public RenderContext renderContext;
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		renderContext.begin();
		shader.begin(GameScreen.camManager.getActiveCamera(), renderContext);
		shader.render(out);
		shader.end();
		renderContext.end();

	}

	public float[] getPointCloud() {
		return physicsPointCloud.toArray();
	}
}
