package fh.teamproject.entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

import fh.teamproject.interfaces.ISlide;
import fh.teamproject.interfaces.ISlidePart;

public class Slide extends CollisionEntity implements ISlide {

	private final ArrayList<ISlidePart> slideParts;
	private SlidePartPool pool;
	private Vector3 tmpSlidePartPos;
	private btDiscreteDynamicsWorld dynamicsWorld;
	private btConvexHullShape convesHullShape;
	// Gefaelle der Slide.
	private int slope = 0;
	private Random rand;
	// Endpunkte des vorherigen SlideParts; sind also die Startpunkte der
	// naechsten Platte.
	private Vector3[] startPoints;
	private Vector3[] endPoints;

	public Slide(btDiscreteDynamicsWorld dynamicsWorld) {
		slideParts = new ArrayList<ISlidePart>();
		pool = new SlidePartPool();
		tmpSlidePartPos = new Vector3(0, 0, 0);
		this.dynamicsWorld = dynamicsWorld;
		rand = new Random();
		this.startPoints = new Vector3[2];
		this.endPoints = new Vector3[2];

		this.startPoints[0] = new Vector3(-10, 0, -10);
		this.startPoints[1] = new Vector3(10, 0, -10);
		this.endPoints[0] = new Vector3(-10, -15, 10);
		this.endPoints[1] = new Vector3(10, -15, 10);

		ModelBuilder builder = new ModelBuilder();
		Model m = builder.createRect(-10, 0, -10, 10, 0, -10, -10, -15, 10, 10, -15, 10,
				0, 1, 0, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);

		this.instance = new ModelInstance(m);

		// Bullet-Eigenschaften setzen.
		btConvexHullShape convesHullShape = new btConvexHullShape();
		convesHullShape.addPoint(this.startPoints[0]);
		convesHullShape.addPoint(this.endPoints[0]);
		convesHullShape.addPoint(this.endPoints[1]);
		convesHullShape.addPoint(this.startPoints[1]);
		btCollisionShape colShape = convesHullShape;

		this.setCollisionShape(colShape);
		this.setEntityWorldTransform(new Matrix4());
		this.setLocalInertia(new Vector3(0, 0, 0));
		this.createRigidBody();

		// Die ersten SlideParts erstellen.
		for (int i = 0; i < 5; ++i) {
			this.addSlidePart();
			tmpSlidePartPos.z += 20;
		}

		this.updateBullet();
	}

	@Override
	public void addSlidePart() {
		ISlidePart slidePart = pool.obtain();
		this.slideParts.add(slidePart);

		if (tmpSlidePartPos == null) {
			Gdx.app.log("slidePart Pos", "" + slidePart.getPosition());
			tmpSlidePartPos = slidePart.getPosition();
		}

		this.move(slidePart);
	}

	@Override
	public ArrayList<ISlidePart> getSlideParts() {
		return this.slideParts;
	}

	@Override
	public void removeSlidePart(ISlidePart slidePart) {
		this.slideParts.remove(slidePart);
		this.pool.free(slidePart);
	}

	public void disposeSlidePart(ISlidePart slidePart) {
		slidePart.dispose();
	}

	@Override
	public void update(Vector3 playerPosition) {
		for (int i = 0; i < this.slideParts.size(); i++) {
			if (this.slideParts.get(i).getVertice(4) > playerPosition.y + 4) {
				this.move(this.slideParts.get(i));
				this.updateBullet();
			}
		}
	}

	public void updateBullet() {
		// Die Bullet-Plane bewegen.
		dynamicsWorld.removeRigidBody(this.getRigidBody());

		this.getRigidBody().setCollisionFlags(
				this.getRigidBody().getCollisionFlags()
						| btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		this.getRigidBody().setActivationState(Collision.DISABLE_DEACTIVATION);

		// Die CollisionShape von Bullet an die Gerenderte anpassen.
		if (convesHullShape != null) {
			convesHullShape.dispose();
		}

		convesHullShape = new btConvexHullShape();

		for (ISlidePart slidePart : this.slideParts) {
			convesHullShape.addPoint(new Vector3(slidePart.getVertice(0), slidePart
					.getVertice(1), slidePart.getVertice(2)));
			convesHullShape.addPoint(new Vector3(slidePart.getVertice(3), slidePart
					.getVertice(4), slidePart.getVertice(5)));
			convesHullShape.addPoint(new Vector3(slidePart.getVertice(6), slidePart
					.getVertice(7), slidePart.getVertice(8)));
			convesHullShape.addPoint(new Vector3(slidePart.getVertice(9), slidePart
					.getVertice(10), slidePart.getVertice(11)));
		}

		btCollisionShape colShape = convesHullShape;

		this.getRigidBody().setCollisionShape(colShape);
		this.getRigidBody().setCollisionFlags(
				this.getRigidBody().getCollisionFlags()
						& ~(btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT));

		this.getRigidBody().forceActivationState(1);

		dynamicsWorld.addRigidBody(this.getRigidBody());
	}

	private void createRandomEndPoints() {
		this.slope -= 20;

		int randX = rand.nextInt(10);

		this.endPoints[0] = new Vector3(this.endPoints[0].x, this.slope,
				this.endPoints[0].z + 20);
		this.endPoints[1] = new Vector3(this.endPoints[1].x, this.slope,
				this.endPoints[1].z + 20);
	}

	private void move(ISlidePart slidePart) {
		slidePart.move(this.startPoints, this.endPoints, this.dynamicsWorld);

		this.startPoints[0] = this.endPoints[0];
		this.startPoints[1] = this.endPoints[1];

		this.createRandomEndPoints();
	}
}