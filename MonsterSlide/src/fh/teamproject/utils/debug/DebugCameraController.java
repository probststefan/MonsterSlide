package fh.teamproject.utils.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;

import fh.teamproject.controller.camera.CameraController;
import fh.teamproject.interfaces.ICameraController;

public class DebugCameraController extends CameraController implements ICameraController,
		InputProcessor {

	private final IntIntMap keys = new IntIntMap();
	private final int STRAFE_LEFT = Keys.A;
	private final int STRAFE_RIGHT = Keys.D;
	private final int FORWARD = Keys.W;
	private final int BACKWARD = Keys.S;
	private final int UP = Keys.Q;
	private final int DOWN = Keys.E;
	private float velocity = 60;
	private float degreesPerPixel = 0.5f;
	private final Vector3 tmp = new Vector3();
	private final Vector3 tmp2 = new Vector3();

	public DebugCameraController(Camera camera) {
		super(camera);
	}

	@Override
	public void update() {
		this.update(Gdx.graphics.getDeltaTime());
	}

	public void update(float deltaTime) {
		if (this.keys.containsKey(this.FORWARD)) {
			this.tmp.set(this.camera.direction).nor().scl(deltaTime * this.velocity);
			this.camera.position.add(this.tmp);
		}
		if (this.keys.containsKey(this.BACKWARD)) {
			this.tmp.set(this.camera.direction).nor().scl(-deltaTime * this.velocity);
			this.camera.position.add(this.tmp);
		}
		if (this.keys.containsKey(this.STRAFE_LEFT)) {
			this.tmp.set(this.camera.direction).crs(this.camera.up).nor()
					.scl(-deltaTime * this.velocity);
			this.camera.position.add(this.tmp);
		}
		if (this.keys.containsKey(this.STRAFE_RIGHT)) {
			this.tmp.set(this.camera.direction).crs(this.camera.up).nor()
					.scl(deltaTime * this.velocity);
			this.camera.position.add(this.tmp);
		}
		if (this.keys.containsKey(this.UP)) {
			this.tmp.set(this.camera.up).nor().scl(deltaTime * this.velocity);
			this.camera.position.add(this.tmp);
		}
		if (this.keys.containsKey(this.DOWN)) {
			this.tmp.set(this.camera.up).nor().scl(-deltaTime * this.velocity);
			this.camera.position.add(this.tmp);
		}
		this.camera.update(true);
	}

	/**
	 * Sets the velocity in units per second for moving forward, backward and
	 * strafing left/right.
	 * 
	 * @param velocity
	 *            the velocity in units per second
	 */
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * Sets how many degrees to rotate per pixel the mouse moved.
	 * 
	 * @param degreesPerPixel
	 */
	public void setDegreesPerPixel(float degreesPerPixel) {
		this.degreesPerPixel = degreesPerPixel;
	}

	@Override
	public boolean keyDown(int keycode) {
		this.keys.put(keycode, keycode);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		this.keys.remove(keycode, 0);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float deltaX = -Gdx.input.getDeltaX() * this.degreesPerPixel;
		float deltaY = -Gdx.input.getDeltaY() * this.degreesPerPixel;
		this.camera.direction.rotate(this.camera.up, deltaX);
		this.tmp.set(this.camera.direction).crs(this.camera.up).nor();
		this.camera.direction.rotate(this.tmp, deltaY);
		// camera.up.rotate(tmp, deltaY);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}