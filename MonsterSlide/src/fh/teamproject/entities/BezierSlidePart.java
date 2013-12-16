package fh.teamproject.entities;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.ShortArray;

import fh.teamproject.interfaces.ISlidePart;

public class BezierSlidePart extends CollisionEntity implements ISlidePart, Poolable {

	private static DelaunayTriangulator triangulator = new DelaunayTriangulator();

	int width = 30;

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
		// FloatBuffer fBuffer = BufferUtils.newFloatBuffer(pointCloud.size);
		// fBuffer.put(pointCloud.items, 0, pointCloud.size);
		// setCollisionShape(new btConvexHullShape(instance.model.meshes.get(0)
		// .getVerticesBuffer(),
		// instance.model.meshes.get(0).getNumVertices()));

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

		for (float i = 0; i < 1.0f; i += splitting) {
			bezierCurve.valueAt(tmpBezierVec, i);
			// Punkte der Bezier Kurve setzen.
			pointCloud.add(tmpBezierVec.x);
			pointCloud.add(tmpBezierVec.y);
			pointCloud.add(tmpBezierVec.z);

			// Punkte der anderen Seiten setzen.
			pointCloud.add(tmpBezierVec.x + width);
			pointCloud.add(tmpBezierVec.y);
			pointCloud.add(tmpBezierVec.z);
		}

		tmpBezierVec = null;
	}

	private void createModelInstance() {
		ShortArray indices = BezierSlidePart.triangulator.computeTriangles(pointCloud,
				false);
		indices.shrink();
		Mesh mesh = new Mesh(true, pointCloud.size, indices.size, new VertexAttributes(
				new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE)));
		mesh.setVertices(pointCloud.items);
		mesh.setIndices(indices.items);
		Model m = new Model();
		m.meshes.add(mesh);
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
