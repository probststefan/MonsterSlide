package fh.teamproject.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btIndexedMesh;
import com.badlogic.gdx.physics.bullet.collision.btTriangleIndexVertexArray;
import com.badlogic.gdx.physics.bullet.collision.btTriangleInfoMap;
import com.badlogic.gdx.utils.Pool.Poolable;

import fh.teamproject.game.Slide;
import fh.teamproject.game.World;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.physics.PhysixBodyDef;
import fh.teamproject.physics.callbacks.motion.MotionState;

public class SlidePart extends CollisionEntity implements ISlidePart, Poolable {

	/* Die CatmullRomSpline von der alles abgeleitet wird */
	private CatmullRomSpline<Vector3> spline;

	// Bullet-Daten (Werden hier zum spaeteren disposen benoetigt)
	btTriangleIndexVertexArray partTriangleVertexArray;
	btIndexedMesh partMesh, borderMesh;

	private btTriangleInfoMap triangleInfoMap;

	Slide slide;

	public SlidePart(World world) {
		super(world);
		initGraphix();
		initPhysix();
	}

	@Override
	public void reset() {

	}

	@Override
	public ModelInstance getModelInstance() {
		return slide.getModelInstance();
	}

	public void dispose() {
		Node node = slide.getModelInstance().getNode(String.valueOf(getID()));
		slide.getModelInstance().nodes.removeValue(node, true);
		partMesh.dispose();
		triangleInfoMap.dispose();
		partTriangleVertexArray.dispose();
		borderMesh.dispose();
		super.dispose();

	}

	@Override
	public void releaseAll() {
		partMesh.release();
		borderMesh.release();
		super.releaseAll();
	}

	@Override
	public void initPhysix() {

		// FIXME: Node könnte in zukunft auch mehrere parts haben oder sogar
		// mehrere child-nodes!

		Node node = slide.getModelInstance().getNode(String.valueOf(getID()));
		partMesh = new btIndexedMesh(node.parts.first().meshPart.mesh);
		borderMesh = new btIndexedMesh(node.parts.get(1).meshPart.mesh);

		partTriangleVertexArray = new btTriangleIndexVertexArray();
		partTriangleVertexArray.addIndexedMesh(partMesh);
		partTriangleVertexArray.addIndexedMesh(borderMesh);

		triangleInfoMap = new btTriangleInfoMap();
		// now you can adjust some thresholds in triangleInfoMap if needed.
		triangleInfoMap.setEdgeDistanceThreshold(25.0f);

		// btGenerateInternalEdgeInfo fills in the btTriangleInfoMap and
		// stores
		// it as a user pointer of collisionShape
		// (collisionShape->setUserPointer(triangleInfoMap))
		btBvhTriangleMeshShape partCollisionShape = new btBvhTriangleMeshShape(
				partTriangleVertexArray, true);

		Collision.btGenerateInternalEdgeInfo((btBvhTriangleMeshShape) partCollisionShape,
				triangleInfoMap);
		PhysixBodyDef bodyDef = new PhysixBodyDef(world.getPhysixManager(), mass,
				new MotionState(getModelInstance().transform), partCollisionShape);
		bodyDef.setFriction(1f);
		bodyDef.setRestitution(0.1f);

		rigidBody = bodyDef.create();
		rigidBody.userData = this;
		rigidBody.setUserValue(this.getID());
		rigidBody.setContactCallbackFlag(Slide.SLIDE_FLAG);
		bodyDef.dispose();

	}

	@Override
	public void initGraphix() {
		slide = (Slide) world.getSlide();
		Node slidePartNode = slide.getSlideBuilder()
				.createSlidePart(slide.getSpline(), 1);
		slidePartNode.id = String.valueOf(getID());
		getModelInstance().nodes.add(slidePartNode);

	}
}
