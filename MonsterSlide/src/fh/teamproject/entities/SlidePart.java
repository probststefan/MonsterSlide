package fh.teamproject.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
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
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btIndexedMesh;
import com.badlogic.gdx.physics.bullet.collision.btTriangleIndexVertexArray;
import com.badlogic.gdx.physics.bullet.collision.btTriangleInfoMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;

public class SlidePart extends CollisionEntity implements ISlidePart, Poolable {

	/* Die CatmullRomSpline von der alles abgeleitet wird */
	private CatmullRomSpline<Vector3> catmullRom;
	Array<Vector3> controlPoints;
	/*
	 * Die interpolierten Punkte der Spline
	 */
	private Array<Vector3> vertices = new Array<Vector3>();
	/* Das Splitting mit dem die Spline diskretisiert wird */
	private float splitting;
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

	// Bullet-Daten (Werden hier zum spaeteren disposen benoetigt)
	btTriangleIndexVertexArray triangleVertexArray;
	btIndexedMesh indexedMesh;

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
		/* Die Schrittweite zum interpolieren der Spline */
		float stepSize = 10f;
		/*
		 * Der SlidePart wird im Abstand von jeweils stepSize Meter
		 * diskretisiert
		 */
		splitting = (1f / GameScreen.settings.SLIDE_LENGTH) * stepSize;

		Vector3 interpolatedVertex = new Vector3();
		float epsilon = 0.01f;

		for (float i = 0; i <= (1 + epsilon); i += splitting) {
			catmullRom.valueAt(interpolatedVertex, i);
			// Normale und Binormale berechnen
			calcBaseCoordinates(catmullRom, i);
			vertices.add(new Vector3(interpolatedVertex));

		}

		createModelInstance();
		createCollisionShape();

		createRigidBody();
		setMass(1000f);
		getRigidBody().setFriction(0.1f);
		getRigidBody().setRestitution(0f);
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

	private void createCollisionShape() {
		btCollisionShape collisionShape = null;

		indexedMesh = new btIndexedMesh(mesh);
		triangleVertexArray = new btTriangleIndexVertexArray();
		triangleVertexArray.addIndexedMesh(indexedMesh);

		collisionShape = new btBvhTriangleMeshShape(triangleVertexArray, true);
		collisionShape.setMargin(0.01f);

		triangleInfoMap = new btTriangleInfoMap();
		// now you can adjust some thresholds in triangleInfoMap if needed.
		triangleInfoMap.setEdgeDistanceThreshold(25.0f);

		// btGenerateInternalEdgeInfo fills in the btTriangleInfoMap and
		// stores
		// it as a user pointer of collisionShape
		// (collisionShape->setUserPointer(triangleInfoMap))
		Collision.btGenerateInternalEdgeInfo((btBvhTriangleMeshShape) collisionShape,
				triangleInfoMap);

		setCollisionShape(collisionShape);
	}

	private void createModelInstance() {
		Array<VertexInfo> vertInfo = new Array<VertexInfo>();

		for (int i = 0; i < vertices.size; ++i) {
			Vector3 v = vertices.get(i);
			Vector3 binormal = baseCoordinates.get(i);
			binormal.scl(GameScreen.settings.SLIDE_WIDTH);

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

			Vector3 normal = baseNormalCoordinates.get(i / 4);

			float uMin = 0f, uMax = 1f, vMin = 0f, vMax = 1f;

			VertexInfo info = new VertexInfo();
			info.setPos(graphicsVertices.get(i)).setNor(normal)
					.setUV(new Vector2(uMin, vMin));
			vertInfo.add(info);

			info = new VertexInfo();
			info.setPos(graphicsVertices.get(i + 1)).setNor(normal)
					.setUV(new Vector2(uMax, vMin));
			vertInfo.add(info);

			info = new VertexInfo();
			info.setPos(graphicsVertices.get(i + 2)).setNor(normal)
					.setUV(new Vector2(uMin, vMax));
			vertInfo.add(info);

			info = new VertexInfo();
			info.setPos(graphicsVertices.get(i + 3)).setNor(normal)
					.setUV(new Vector2(uMax, vMax));
			vertInfo.add(info);

		}
		MeshBuilder builder = new MeshBuilder();
		builder.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3,
				ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.Normal, 3,
				ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(
				Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE)));
		builder.part("part1", GL20.GL_TRIANGLES);

		for (int i = 0; i <= (vertInfo.size - 4); i += 4) {
			builder.triangle(vertInfo.get(i + 2), vertInfo.get(i + 3),
					vertInfo.get(i + 1));
			builder.triangle(vertInfo.get(i + 1), vertInfo.get(i), vertInfo.get(i + 2));
		}

		mesh = builder.end();

		MeshPart meshPart = new MeshPart();
		meshPart.id = "SlidePart" + id;
		meshPart.primitiveType = GL20.GL_TRIANGLES;
		meshPart.mesh = mesh;
		meshPart.indexOffset = 0;
		meshPart.numVertices = mesh.getNumVertices();

		Model model = new Model();
		Node node = new Node();
		model.nodes.add(node);
		Material material = new Material();
		TextureAttribute texAttr = TextureAttribute.createDiffuse(new Texture(Gdx.files
				.internal("data/floor2.png")));
		material.set(texAttr);
		NodePart nodePart = new NodePart(meshPart, material);
		node.parts.add(nodePart);
		model.materials.add(material);

		instance = new ModelInstance(model);
		instance.userData = "slidepart";
	}

	@Override
	public void reset() {

	}

	public void dispose() {
		triangleInfoMap.dispose();
		triangleVertexArray.dispose();
		indexedMesh.dispose();
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
