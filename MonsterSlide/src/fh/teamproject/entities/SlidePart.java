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
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btIndexedMesh;
import com.badlogic.gdx.physics.bullet.collision.btTriangleIndexVertexArray;
import com.badlogic.gdx.physics.bullet.collision.btTriangleInfoMap;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMesh;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;

public class SlidePart extends CollisionEntity implements ISlidePart, Poolable {

	/* Die CatmullRomSpline von der alles abgeleitet wird */
	private CatmullRomSpline<Vector3> catmullRom;
	Array<Vector3> controlPoints;
	/*
	 * Die abgeleiteten Punkte der Spline für Rendering und Physik basierend auf
	 * einem splitting
	 */
	private Array<Vector3> vertices = new Array<Vector3>();
	/* Das Splitting mit dem die Spline diskretisiert wird */
	private float splitting;
	/* Die Punkte zur Erstellung der konvexen Hülle als Polygon angeordnet */
	private FloatArray physicsPointCloud;
	/*
	 * Die Punkte zum Rendering in alternierender Reihenfolge (links, rechts,
	 * links, rechts)
	 */
	public Array<Vector3> graphicsVertices = new Array<Vector3>();
	/* */
	public ArrayList<Vector3> baseCoordinates = new ArrayList<Vector3>();
	public ArrayList<Vector3> baseNormalCoordinates = new ArrayList<Vector3>();

	private Vector3[] startPoints;

	public Texture texture;
	public Mesh mesh;

	private btTriangleInfoMap triangleInfoMap;

	public SlidePart() {
		controlPoints = new Array<Vector3>();
		catmullRom = new CatmullRomSpline<Vector3>();
		texture = new Texture(Gdx.files.internal("data/floor.jpg"));
	}

	@Override
	public ISlidePart setCatmullPoints(Vector3[] points) {
		startPoints = new Vector3[2];
		catmullRom.set(points, false);
		setup();
		return this;
	}

	@Override
	public ISlidePart setControlPoints(Array<Vector3> controlPoints) {
		startPoints = new Vector3[2];
		this.controlPoints = controlPoints;
		catmullRom.set(controlPoints.items, false);
		setup();
		startPoints[0] = graphicsVertices.get(0).cpy();
		startPoints[1] = graphicsVertices.get(1).cpy();

		return this;
	}

	/**
	 * Liefert den Startpunkt der Rutschbahn.
	 * 
	 * @return Vector3
	 */
	@Override
	public Vector3[] getStartPoints() {
		return startPoints;
	}

	private void setup() {
		computePointCloud();
		createModelInstance();
		btTriangleMesh tetraMesh = new btTriangleMesh();

		btTriangleIndexVertexArray vertexArray = new btTriangleIndexVertexArray();

		for (int i = 0; i <= (graphicsVertices.size - 4); i += 4) {
			tetraMesh.addTriangle(graphicsVertices.get(i + 2),
					graphicsVertices.get(i + 1), graphicsVertices.get(i));

			tetraMesh.addTriangle(graphicsVertices.get(i + 3),
					graphicsVertices.get(i + 2), graphicsVertices.get(i + 1));
		}

		// btBvhTriangleMeshShape collisionShape = new
		// btBvhTriangleMeshShape(tetraMesh,
		// true);

		// triangleInfoMap = new btTriangleInfoMap();
		// now you can adjust some thresholds in triangleInfoMap if needed.
		// triangleInfoMap.setEdgeDistanceThreshold(25.0f);

		// btGenerateInternalEdgeInfo fills in the btTriangleInfoMap and stores
		// it as a user pointer of collisionShape
		// (collisionShape->setUserPointer(triangleInfoMap))
		// Collision.btGenerateInternalEdgeInfo(collisionShape,
		// triangleInfoMap);

		// setCollisionShape(collisionShape);
		createRigidBody();
	}

	private void computePointCloud() {
		Vector3 tmpBezierVec = new Vector3();
		physicsPointCloud = new FloatArray();
		/* Der SlidePart wird im Abstand von jeweils 1 Meter diskretisiert */
		splitting = 1f / GameScreen.settings.SLIDE_LENGTH * 5.0f;
		float epsilon = 0.01f;

		for (float i = 0; i <= (1 + epsilon); i += splitting) {
			// Punkte der Bezier Kurve setzen.
			catmullRom.valueAt(tmpBezierVec, i);

			// Base Koordinaten zu diesem Punkt berechnen und ablegen.
			calcBaseCoordinates(catmullRom, i);

			physicsPointCloud.add(tmpBezierVec.x);
			physicsPointCloud.add(tmpBezierVec.y);
			physicsPointCloud.add(tmpBezierVec.z);
			vertices.add(new Vector3(tmpBezierVec));

		}

		for (int i = vertices.size - 1; i >= 0; --i) {
			Vector3 v = vertices.get(i);
			Vector3 binormal = baseCoordinates.get(i);

			binormal.scl(GameScreen.settings.SLIDE_WIDTH);
			physicsPointCloud
					.addAll(v.x + binormal.x, v.y + binormal.y, v.z + binormal.z);

			if (i == 0) {
				startPoints[1] = new Vector3(v.x + binormal.x, v.y + binormal.y, v.z
						+ binormal.z);
			}
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
		Vector3 normal = tangent.crs(binormal);

		baseNormalCoordinates.add(normal);
		baseCoordinates.add(binormal);
	}

	private void createModelInstance() {
		Array<VertexInfo> vertInfo = new Array<VertexInfo>();
		MeshBuilder builder = new MeshBuilder();
		builder.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3,
				ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.Color, 4,
				ShaderProgram.COLOR_ATTRIBUTE), new VertexAttribute(Usage.Normal, 3,
				ShaderProgram.NORMAL_ATTRIBUTE)));
		// , new VertexAttribute(
		// Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE)

		for (int i = 0; i < vertices.size; ++i) {
			Vector3 v = vertices.get(i);
			Vector3 binormal = baseCoordinates.get(i);

			graphicsVertices.add(new Vector3(v.x, v.y, v.z));
			graphicsVertices.add(new Vector3(v.x + binormal.x, v.y + binormal.y, v.z
					+ binormal.z));
			if ((i == 0) || (i == (vertices.size - 1))) {
				continue;
			}
			graphicsVertices.add(new Vector3(v.x, v.y, v.z));
			graphicsVertices.add(new Vector3(v.x + binormal.x, v.y + binormal.y, v.z
					+ binormal.z));
		}

		for (int i = 0; i <= (graphicsVertices.size - 4); i += 4) {
			VertexInfo info = new VertexInfo();
			Color col = Color.BLUE;
			Vector3 nor = null; // new Vector3(0f, 1f, 0f);

			info.set(graphicsVertices.get(i), baseNormalCoordinates.get(i / 4), col,
					new Vector2(1, 0));
			vertInfo.add(info);

			info = new VertexInfo();
			col = Color.RED;
			info.set(graphicsVertices.get(i + 1), baseNormalCoordinates.get(i / 4), col,
					new Vector2(1, 1));
			vertInfo.add(info);

			info = new VertexInfo();
			col = Color.GREEN;
			info.set(graphicsVertices.get(i + 2), baseNormalCoordinates.get(i / 4), col,
					new Vector2(0, 0));
			vertInfo.add(info);

			info = new VertexInfo();
			col = Color.YELLOW;
			info.set(graphicsVertices.get(i + 3), baseNormalCoordinates.get(i / 4), col,
					new Vector2(0, 1));
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
		m.nodes.add(new Node());

		Material material = new Material();
		// material.set(ColorAttribute.createDiffuse(Color.BLUE));
		// material.set(TextureAttribute.createDiffuse(new Texture(Gdx.files
		// .internal("data/floor.jpg"))));
		NodePart nodePart = new NodePart(meshPart, material);
		m.nodes.get(0).parts.add(nodePart);
		instance = new ModelInstance(m);

		// Stefans Test
		btIndexedMesh indexedMesh = new btIndexedMesh(mesh);
		btTriangleIndexVertexArray triangleVertexArray = new btTriangleIndexVertexArray();
		triangleVertexArray.addIndexedMesh(indexedMesh);

		btBvhTriangleMeshShape collisionShape = new btBvhTriangleMeshShape(
				triangleVertexArray, true);

		triangleInfoMap = new btTriangleInfoMap();
		// triangleInfoMap.setEdgeDistanceThreshold(50.0f);
		Collision.btGenerateInternalEdgeInfo(collisionShape, triangleInfoMap);

		setCollisionShape(collisionShape);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public float[] getPhysicsVertices() {
		return physicsPointCloud.toArray();
	}

	@Override
	public Array<Vector3> getInterpolatedVertices() {
		return vertices;
	}

	@Override
	public Array<Vector3> getControlPoints() {
		return controlPoints;
	}

	@Override
	public Array<Vector3> getGraphicVertices() {
		return graphicsVertices;
	}
}
