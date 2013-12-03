package fh.teamproject.controller.player.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;

import fh.teamproject.entities.Player;

public class SwipeController extends GestureAdapter {
	Player player;
	private final float PIXEL_PER_IMPULSE = 0;

	public SwipeController(Player p) {
		this.player = p;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Gdx.app.log("SwipeController", "TAP " + x + " " + y + " " + count + " " + button);
		this.player.brake(0f);
		return super.tap(x, y, count, button);
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		boolean handled = false;
		// Gdx.app.log("SwipeController", "PAN " + x + " " + y + " " + deltaX +
		// " " + deltaY);
		if (deltaX > this.PIXEL_PER_IMPULSE) {
			this.player.slideRight();
			handled = true;
		} else if (deltaX < -this.PIXEL_PER_IMPULSE) {
			this.player.slideLeft();
			handled = true;
		}
		return handled;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return super.fling(velocityX, velocityY, button);
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;

	};
}
