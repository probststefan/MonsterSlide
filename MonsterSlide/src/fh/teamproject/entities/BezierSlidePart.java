package fh.teamproject.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
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
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.ShortArray;

import fh.teamproject.interfaces.ISlidePart;

public class BezierSlidePart extends CollisionEntity implements ISlidePart, Poolable {

	int width = 30;
	ShortArray indices = new ShortArray();

	private Vector3 start = new Vector3(), end = new Vector3(), control1 = new Vector3(),
			control2 = new Vector3();
	private FloatArray pointCloud;
	private float splitting = 0.25f;

	public BezierSlidePart(Vector3 start, Vector3 end, Vector3 control1,
			Vector3 control2, float splitting) {
		this.start.set(start);
		this.end.set(end);
		this.control1.set(control1);
		this.control2.set(control2);
		this.splitting = splitting;
		computePointCloud();
		createModelInstance();
		btConvexHullShape collisionShape = new btConvexHullShape();
		for (int i = 0; i < pointCloud.size; i += 3) {
			collisionShape.addPoint(new Vector3(pointCloud.get(i), pointCloud.get(i + 1),
					pointCloud.get(i + 2)));
		}
		setCollisionShape(collisionShape);
		createRigidBody();

	}

	private void computePointCloud() {
		Bezier<Vector3> bezierCurve = new Bezier<Vector3>();

		bezierCurve.set(new Vector3[] { start, control1, control2, end });

		Vector3 tmpBezierVec = new Vector3();
		pointCloud = new FloatArray();

		int id = 0;
		for (float i = 0; i < 1.0f; i += splitting) {
			// Punkte der Bezier Kurve setzen.
			bezierCurve.valueAt(tmpBezierVec, i);
			pointCloud.add(tmpBezierVec.x);
			pointCloud.add(tmpBezierVec.y);
			pointCloud.add(tmpBezierVec.z);
			// Punkte der anderen Bezier Kurve setzen.
			pointCloud.add(tmpBezierVec.x + width);
			pointCloud.add(tmpBezierVec.y);
			pointCloud.add(tmpBezierVec.z);
		}

		tmpBezierVec = null;
	}

	private void createModelInstance() {

		for (int i = 0; i < ((pointCloud.size / 3) - 2); i += 2) {
			indices.add(i + 2);
			indices.add(i + 1);
			indices.add(i);

			indices.add(i + 2);
			indices.add(i + 3);
			indices.add(i + 1);
		}

		MeshBuilder builder = new MeshBuilder();
		builder.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3,
				ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.Color, 4,
						ShaderProgram.COLOR_ATTRIBUTE), new VertexAttribute(
								Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE)));

		float uMin = 0, uMax = 1, vMin = 0, vMax = 1;

		for (int i = 0; i < pointCloud.size; i += 3) {
			VertexInfo info = new VertexInfo();
			info.setPos(pointCloud.get(i), pointCloud.get(i + 1), pointCloud.get(i + 2));
			info.setCol(Color.BLUE);
			builder.vertex(info);
		}

		for (short s : indices.items) {
			builder.index(s);
		}

		Mesh mesh = builder.end();

		MeshPart meshPart = new MeshPart();
		meshPart.id = "SlidePart" + id;
		meshPart.primitiveType = GL10.GL_TRIANGLES;
		meshPart.mesh = mesh;
		meshPart.indexOffset = 0;
		meshPart.numVertices = pointCloud.size;

		Model m = new Model();
		m.nodes.add(new Node());
		m.nodes.get(0).parts.add(new NodePart(meshPart, new Material()));
		instance = new ModelInstance(m);

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWidth(float width) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getVertice(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void move(Vector3[] startPoints, Vector3[] endPoints,
			btDiscreteDynamicsWorld dynamicsWorld) {
		// TODO Auto-generated method stub

	}

	public float[] getPointCloud() {
		return pointCloud.toArray();
	}
}
