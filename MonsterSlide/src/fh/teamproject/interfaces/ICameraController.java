package fh.teamproject.interfaces;

import com.badlogic.gdx.graphics.Camera;

public interface ICameraController {

	public void update();

	public void setCamera(Camera camera);
	public Camera getCamera();
}
