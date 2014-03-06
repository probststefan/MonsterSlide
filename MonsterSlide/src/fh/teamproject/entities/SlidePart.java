package fh.teamproject.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btIndexedMesh;
import com.badlogic.gdx.physics.bullet.collision.btTriangleIndexVertexArray;
import com.badlogic.gdx.physics.bullet.collision.btTriangleInfoMap;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.physics.PhysixBodyDef;

public class SlidePart extends CollisionEntity implements ISlidePart, Poolable {

	/* Die CatmullRomSpline von der alles abgeleitet wird */
	private CatmullRomSpline<Vector3> spline;

	// Bullet-Daten (Werden hier zum spaeteren disposen benoetigt)
	btTriangleIndexVertexArray triangleVertexArray;
	btIndexedMesh indexedMesh;

	private btTriangleInfoMap triangleInfoMap;

	Slide slide;

	public SlidePart(Slide slide) {
		this.slide = slide;
	}

	public SlidePart() {
	}

	@Override
	public ISlidePart setSlide(Slide slide) {
		this.slide = slide;
		return this;
	}

	@Override
	public void reset() {

	}

	@Override
	public ModelInstance getModelInstance() {
		return slide.getModelInstance();
	}

	public void dispose() {
		indexedMesh.dispose();
		Node node = slide.getModelInstance().getNode(String.valueOf(getID()));
		slide.getModelInstance().nodes.removeValue(node, true);
		super.dispose();
	}

	@Override
	public void releaseAll() {
		indexedMesh.release();
		super.releaseAll();
	}

	@Override
	public ISlidePart setSpline(CatmullRomSpline<Vector3> spline) {
		this.spline = spline;
		return this;
	}

	@Override
	public void initPhysix() {

		// FIXME: Node könnte in zukunft auch mehrere parts haben oder sogar
		// mehrere child-nodes!
		Node node = slide.getModelInstance().getNode(String.valueOf(getID()));
		indexedMesh = new btIndexedMesh(node.parts.first().meshPart.mesh);
		System.out.println("" + node.parts.first().meshPart.mesh);
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

		PhysixBodyDef bodyDef = new PhysixBodyDef(world.getPhysixManager(), mass,
				motionState, collisionShape);
		bodyDef.setFriction(0.1f);
		bodyDef.setRestitution(0f);

		rigidBody = bodyDef.create();
		rigidBody.userData = this;
		rigidBody.setUserValue(this.getID());
		rigidBody.setContactCallbackFlag(Slide.SLIDE_FLAG);
	}

	@Override
	public void initGraphix() {
		// TODO Auto-generated method stub

	}
}
