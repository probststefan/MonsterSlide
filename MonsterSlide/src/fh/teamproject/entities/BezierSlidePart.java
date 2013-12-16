package fh.teamproject.entities;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.ShortArray;

import fh.teamproject.interfaces.ISlidePart;

public class BezierSlidePart extends CollisionEntity implements ISlidePart, Poolable {

	private static DelaunayTriangulator triangulator = new DelaunayTriangulator();

	private Vector3 start = new Vector3(), end = new Vector3(), control1 = new Vector3(),
			control2 = new Vector3();
	private float[] pointCloud;
	private float splitting = 1;

	public BezierSlidePart(Vector3 start, Vector3 end, Vector3 control1,
			Vector3 control2, float splitting) {
		this.start.set(start);
		this.end.set(end);
		this.control1.set(control1);
		this.control2.set(control2);
		this.splitting = splitting;
		computePointCloud();
		createModelInstance();
	}

	private void computePointCloud() {

	}

	private void createModelInstance() {
		ShortArray indices = BezierSlidePart.triangulator.computeTriangles(pointCloud,
				false);
		indices.shrink();
		Mesh mesh = new Mesh(true, pointCloud.length, indices.size, new VertexAttributes(
				new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE)));
		mesh.setVertices(pointCloud);
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
		return pointCloud;
	}
}
