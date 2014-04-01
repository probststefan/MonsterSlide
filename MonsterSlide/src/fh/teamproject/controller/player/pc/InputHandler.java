package fh.teamproject.controller.player.pc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.IntIntMap;

import fh.teamproject.game.entities.Player;

public class InputHandler implements InputProcessor {

	private Player player;

	// der Cooldown zum springen, damit man nicht in der luft springen kann
	private float jumpCooldown = 0.0f;

	private boolean canJump = true;
	private IntIntMap keys = new IntIntMap();
	private int LEFT = Keys.LEFT;
	private int RIGHT = Keys.RIGHT;
	private int ACCELERATE = Keys.UP;
	private int BRAKE = Keys.DOWN;
	private int JUMP = Keys.SPACE;

	public InputHandler(Player player) {
		this.player = player;
	}

	@Override
	public boolean keyDown(int keycode) {
		keys.put(keycode, keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys.remove(keycode, keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX < (Gdx.graphics.getWidth() / 4)) {
			keys.put(LEFT, LEFT);
		}

		if (screenX > (3 * (Gdx.graphics.getWidth() / 4))) {
			keys.put(RIGHT, RIGHT);

		}
		if ((screenX > (Gdx.graphics.getWidth() / 4))
				&& (screenX < (3 * (Gdx.graphics.getWidth() / 4)))
				&& (screenY > (2 * (Gdx.graphics.getHeight() / 3)))) {
			keys.put(BRAKE, BRAKE);
		}
		if ((screenX > (Gdx.graphics.getWidth() / 4))
				&& (screenX < (3 * (Gdx.graphics.getWidth() / 4)))
				&& (screenY > (Gdx.graphics.getHeight() / 3))
				&& (screenY < (2 * (Gdx.graphics.getHeight() / 3)))) {
			keys.put(JUMP, JUMP);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (screenX < (Gdx.graphics.getWidth() / 4)) {
			keys.remove(LEFT, LEFT);
		}

		if (screenX > (3 * (Gdx.graphics.getWidth() / 4))) {
			keys.remove(RIGHT, RIGHT);

		}
		if ((screenX > (Gdx.graphics.getWidth() / 4))
				&& (screenX < (3 * (Gdx.graphics.getWidth() / 4)))
				&& (screenY > (2 * (Gdx.graphics.getHeight() / 3)))) {
			keys.remove(BRAKE, BRAKE);
		}
		if ((screenX > (Gdx.graphics.getWidth() / 4))
				&& (screenX < (3 * (Gdx.graphics.getWidth() / 4)))
				&& (screenY > (Gdx.graphics.getHeight() / 3))
				&& (screenY < (2 * (Gdx.graphics.getHeight() / 3)))) {
			keys.remove(JUMP, JUMP);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void update() {
		if (keys.containsKey(LEFT)) {
			player.slideLeft();
		}

		if (keys.containsKey(RIGHT)) {
			player.slideRight();
		}

		if (keys.containsKey(BRAKE)) {
			player.brake();
		}

		if (keys.containsKey(JUMP)) {
			player.jump();
		}
		if (keys.containsKey(BRAKE) == false && keys.containsKey(JUMP) == false
				&& (player.speed <= player.MAX_SPEED - 10)) {
			player.accelerate();
		}
	}

}