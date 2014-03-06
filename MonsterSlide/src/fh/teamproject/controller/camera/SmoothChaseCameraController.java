package fh.teamproject.controller.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.entities.Player;

public class SmoothChaseCameraController extends CameraController {

	private Player target;
	private float radius = 10;
	private Vector3 offset;

	// The distance in the x-z plane to the target
	private float distance = 10.0f;
	// the height we want the camera to be above the target
	private float height = 5.0f;
	// How much we
	private float heightDamping = 2.0f;
	private float rotationDamping = 3.0f;
	private double heading, attitude, bank;

	public SmoothChaseCameraController(Camera camera, Player player) {
		super(camera);
		this.target = player;

		camera.up.set(0, 1, 0);
		offset = player.direction.cpy().scl(-1f * radius);
		Vector3 rotAxis = player.direction.cpy().crs(Vector3.Y);
		Vector3 pos = player.getPosition();
		Vector3 orbitPos = pos.cpy().add(offset);
		camera.position.set(orbitPos);
		camera.rotateAround(pos, rotAxis, 315);
		camera.lookAt(pos.cpy().add(player.direction));
	}

	public void update() {
		Quaternion rotation = new Quaternion();
		this.target.getModelInstance().transform.getRotation(rotation);
		this.set(rotation);
		float wantedRotationAngle = (float) attitude;
		float wantedHeight = this.target.position.y + this.height;

		Matrix4 camTransform = new Matrix4();
		this.camera.transform(camTransform);
		camTransform.getRotation(rotation);
		this.set(rotation);
		float currentRotationAngle = (float) attitude;
		float currentHeight = camera.position.y;

		// Damp the rotation around the y-axis
		currentRotationAngle = SmoothChaseCameraController.lerpAngle(
				currentRotationAngle, wantedRotationAngle,
				rotationDamping * Gdx.graphics.getDeltaTime());

		// Damp the height
		currentHeight = lerp(currentHeight, wantedHeight,
				heightDamping * Gdx.graphics.getDeltaTime());

		// Convert the angle into a rotation
		Quaternion currentRotation = new Quaternion();
		currentRotation.setEulerAngles(0, currentRotationAngle, 0);

		// Set the position of the camera on the x-z plane to:
		// distance meters behind the target
		this.camera.position.set(target.position);
		Vector3 forward = new Vector3(0, 1, 0);
		this.camera.position.set(this.camera.position.sub(forward.mul(currentRotation)
				.mul(distance)));

		// Set the height of the camera
		this.camera.position.y = currentHeight;
		this.camera.up.set(0, 0, 1);

		this.camera.lookAt(target.position);
		this.camera.rotate(new Vector3(0, 1, 0), 90.0f);
		this.camera.rotate(new Vector3(0, 0, 1), 40.0f);
		this.camera.translate(-10.0f, 0, 0.0f);
		this.camera.update(true);
	}

	/** q1 can be non-normalised quaternion */

	public void set(Quaternion q1) {
		double sqw = q1.w * q1.w;
		double sqx = q1.x * q1.x;
		double sqy = q1.y * q1.y;
		double sqz = q1.z * q1.z;
		double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise
												// is correction factor
		double test = q1.x * q1.y + q1.z * q1.w;

		if (test > 0.499 * unit) { // singularity at north pole
			heading = 2 * Math.atan2(q1.x, q1.w);
			attitude = Math.PI / 2;
			bank = 0;
			return;
		}

		if (test < -0.499 * unit) { // singularity at south pole
			heading = -2 * Math.atan2(q1.x, q1.w);
			attitude = -Math.PI / 2;
			bank = 0;
			return;
		}

		heading = Math.atan2(2 * q1.y * q1.w - 2 * q1.x * q1.z, sqx - sqy - sqz + sqw);
		attitude = Math.asin(2 * test / unit);
		bank = Math.atan2(2 * q1.x * q1.w - 2 * q1.y * q1.z, -sqx + sqy - sqz + sqw);
	}

	private static float lerpAngle(float a, float b, float alpha) {
		float delta;
		a %= 360;
		b %= 360;
		delta = b - a;

		if (Math.abs(delta) <= 180)
			return a + delta * alpha;

		if (a > b) {
			delta = 360 - a;
			a = 0;
			b += delta;
		} else {
			delta = 360 - b;
			b = 0;
			a += delta;
		}

		return ((a + (b - a) * alpha) - delta) % 360;
	}

	private float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}
}
