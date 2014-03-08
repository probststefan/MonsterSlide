package fh.teamproject.controller.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.game.entities.Player;

/**
 * Eine gedaempfte Verfolgerkamera aus den Unity3D-Beispielen uebernommen und
 * modifiziert.
 * 
 * @author stefanprobst
 * @see <a
 *      href="http://answers.unity3d.com/questions/38526/smooth-follow-camera.html">http://answers.unity3d.com/questions/38526/smooth-follow-camera.html</a>
 * 
 */
public class SmoothChaseCameraController extends CameraController {

	private Player target;

	// The distance in the x-z plane to the target
	private float distance = 2.0f;
	// the height we want the camera to be above the target
	private float height = 15.0f;
	// How much we
	private float heightDamping = 2.0f;
	private float rotationDamping = 3.0f;
	// Folgende Variablen werden waehrend den Berechnungen benoetigt.
	private double attitude;
	private Vector3 tmpForward;
	private Quaternion currentRotation, rotation;
	private Matrix4 camTransform;

	public SmoothChaseCameraController(Camera camera, Player player) {
		super(camera);
		this.target = player;
	}

	public void update() {
		this.rotation = new Quaternion();
		this.target.getModelInstance().transform.getRotation(this.rotation);
		this.calculateEulerValuesFromQuat(this.rotation);
		float wantedRotationAngle = (float) attitude;
		float wantedHeight = this.target.getPosition().y + this.height;

		this.camTransform = new Matrix4();
		this.camera.transform(this.camTransform);
		this.camTransform.getRotation(rotation);
		this.calculateEulerValuesFromQuat(rotation);
		float currentRotationAngle = (float) attitude;
		float currentHeight = camera.position.y;

		// Damp the rotation around the y-axis
		currentRotationAngle = this.lerpAngle(currentRotationAngle, wantedRotationAngle,
				rotationDamping * Gdx.graphics.getDeltaTime());

		// Damp the height
		currentHeight = lerp(currentHeight, wantedHeight,
				heightDamping * Gdx.graphics.getDeltaTime());

		// Convert the angle into a rotation
		this.currentRotation = new Quaternion();
		this.currentRotation.setEulerAngles(0, currentRotationAngle, 0);

		// Set the position of the camera on the x-z plane to:
		// distance meters behind the target
		this.camera.position.set(target.getPosition());
		this.tmpForward = new Vector3(0, 1, 0);
		this.camera.position.set(this.camera.position.sub(tmpForward.mul(
				this.currentRotation).scl(distance)));

		// Set the height of the camera
		this.camera.position.y = currentHeight;
		this.camera.up.set(0, 0, 1);

		this.camera.lookAt(target.getPosition());
		this.camera.rotate(new Vector3(0, 1, 0), 90.0f);
		this.camera.rotate(new Vector3(0, 0, 1), 40.0f);
		this.camera.translate(-10.0f, 0, 0.0f);
		this.camera.update(true);
	}

	/**
	 * Berechnet die Euler-Winkel aus einem nicht-normalisiertem Quaternion
	 * (heading, attitude, bank).
	 * 
	 * @param q1
	 */
	public void calculateEulerValuesFromQuat(Quaternion q1) {
		double sqw = q1.w * q1.w;
		double sqx = q1.x * q1.x;
		double sqy = q1.y * q1.y;
		double sqz = q1.z * q1.z;
		double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise
												// is correction factor
		double test = q1.x * q1.y + q1.z * q1.w;

		if (test > 0.499 * unit) { // singularity at north pole
			attitude = Math.PI / 2;
			return;
		}

		if (test < -0.499 * unit) { // singularity at south pole
			attitude = -Math.PI / 2;
			return;
		}

		attitude = Math.asin(2 * test / unit);
	}

	private float lerpAngle(float a, float b, float alpha) {
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