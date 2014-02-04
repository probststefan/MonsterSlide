package fh.teamproject.entities;

import java.util.ArrayList;

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
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.ShortArray;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;

public class SlidePart extends CollisionEntity implements ISlidePart, Poolable {

	int width = 10;
	ShortArray indices = new ShortArray();

	public Vector3 start = new Vector3(), end = new Vector3(), control1 = new Vector3(),
			control2 = new Vector3();
	private FloatArray physicsPointCloud;
	public Array<Vector3> bezierPoints = new Array<Vector3>();
	public ArrayList<Vector3> baseCoordinates = new ArrayList<Vector3>();
	public Array<Vector3> graphicsVertices = new Array<Vector3>();

	private Vector3[] points;

	private float splitting = 0.01f;

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

	@Override
	public ISlidePart setCatmullPoints(Vector3[] points) {
		this.points = points;
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
		CatmullRomSpline<Vector3> catmullRom = new CatmullRomSpline<Vector3>();
		catmullRom.set(points, false);

		Vector3 tmpBezierVec = new Vector3();
		physicsPointCloud = new FloatArray();
		float epsilon = 0.01f;

		for (float i = 0; i <= (1 + epsilon); i += splitting) {
			// Punkte der Bezier Kurve setzen.
			catmullRom.valueAt(tmpBezierVec, i);

			// Base Koordinaten zu diesem Punkt berechnen und ablegen.
			calcBaseCoordinates(catmullRom, i);

			physicsPointCloud.add(tmpBezierVec.x);
			physicsPointCloud.add(tmpBezierVec.y);
			physicsPointCloud.add(tmpBezierVec.z);
			bezierPoints.add(new Vector3(tmpBezierVec));
		}

		for (int i = bezierPoints.size - 1; i >= 0; --i) {
			Vector3 v = bezierPoints.get(i);
			Vector3 binormal = baseCoordinates.get(i);
			binormal.scl(width);
			physicsPointCloud
					.addAll(v.x + binormal.x, v.y + binormal.y, v.z + binormal.z);
		}

		tmpBezierVec = null;
	}

	/**
	 * Die Basis-Koordinaten zu den Punkten in der Spline berechnen.
	 * 
	 * @param bezier
	 * @param t
	 */
	private void calcBaseCoordinates(CatmullRomSpline<Vector3> catmullRom, float t) {
		Vector3 derivation = new Vector3();

		// 1. und 2. Ableitung bilden.
		derivation = catmullRom.derivativeAt(derivation, t);

		Vector3 tangent = derivation.cpy().nor();

		/*
		 * Mit dem upVector wird das Problem der springenden Normalen behoben.
		 * 
		 * @link http://www.it.hiof.no/~borres/j3d/explain/frames/p-frames.html
		 */
		Vector3 upVector = new Vector3(0.0f, -1.0f, 0.0f);
		Vector3 binormal = derivation.cpy().crs(upVector).nor();
		// Vector3 normal = tangent.crs(binormal);

		baseCoordinates.add(binormal);
	}

	private void createModelInstance() {
		Array<VertexInfo> vertInfo = new Array<VertexInfo>();
		MeshBuilder builder = new MeshBuilder();
		builder.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3,
				ShaderProgram.POSITION_ATTRIBUTE)));

		for (int i = 0; i < bezierPoints.size; ++i) {
			Vector3 v = bezierPoints.get(i);
			Vector3 binormal = baseCoordinates.get(i);

			graphicsVertices.add(new Vector3(v.x, v.y, v.z));
			graphicsVertices.add(new Vector3(v.x + binormal.x, v.y + binormal.y, v.z
					+ binormal.z));
			if ((i == 0) || (i == (bezierPoints.size - 1))) {
				continue;
			}
			graphicsVertices.add(new Vector3(v.x, v.y, v.z));
			graphicsVertices.add(new Vector3(v.x + binormal.x, v.y + binormal.y, v.z
					+ binormal.z));
		}

		for (int i = 0; i <= (graphicsVertices.size - 4); i += 4) {
			VertexInfo info = new VertexInfo();
			Color col = null; // Color.BLUE;
			Vector3 nor = null; // new Vector3(0f, 1f, 0f);
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

		TextureAttribute textureAttribute = new TextureAttribute(
				TextureAttribute.Diffuse, texture);
		material.set(textureAttribute);

		// material.set(TextureAttribute.createSpecular(texture));
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