package fh.teamproject.entities;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btIndexedMesh;
import com.badlogic.gdx.physics.bullet.collision.btTriangleIndexVertexArray;
import com.badlogic.gdx.physics.bullet.collision.btTriangleInfoMap;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.interfaces.ISlidePart;

public class SlidePart extends CollisionEntity implements ISlidePart, Poolable {

	/* Die CatmullRomSpline von der alles abgeleitet wird */
	private CatmullRomSpline<Vector3> spline;

	// Bullet-Daten (Werden hier zum spaeteren disposen benoetigt)
	btTriangleIndexVertexArray triangleVertexArray;
	btIndexedMesh indexedMesh;

	private btTriangleInfoMap triangleInfoMap;

	Slide slide;
	String id;

	public SlidePart(Slide slide, String id) {
		this.slide = slide;
		this.id = id;
	}

	public SlidePart() {
	}

	@Override
	public ISlidePart setSlide(Slide slide) {
		this.slide = slide;
		return this;
	}

	@Override
	public ISlidePart setID(String id) {
		this.id = id;
		return this;
	}

	private void createCollisionShape() {
		btCollisionShape collisionShape = null;
		// FIXME: Node könnte in zukunft auch mehrere parts haben oder sogar
		// mehrere child-nodes!
		Node node = slide.getModelInstance().getNode(id);
		indexedMesh = new btIndexedMesh(node.parts.first().meshPart.mesh);
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

	@Override
	public void reset() {

	}

	public void dispose() {
		triangleInfoMap.dispose();
		triangleVertexArray.dispose();
		indexedMesh.dispose();
	}

	@Override
	public ISlidePart setSpline(CatmullRomSpline<Vector3> spline) {
		this.spline = spline;
		return this;
	}

	@Override
	public void init() {
		createCollisionShape();
		createRigidBody();
		getRigidBody().setUserValue(this.getID());
		getRigidBody().setFriction(0f);
		getRigidBody().setRestitution(0f);
	}

	@Override
	public void initPhysix() {
		// TODO Auto-generated method stub

	}
}
