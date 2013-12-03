package fh.teamproject.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import fh.teamproject.entities.Player;

public class InputHandling implements InputProcessor {
	
	private Player player;
	
	//der Cooldown zum springen, damit man nicht in der luft springen kann
	private float jumpCooldown = 0.0f;
	
	private boolean canJump = true;
	
	public InputHandling(Player player) {
		this.player = player;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.UP){
			player.accelerate(10.0f);
		}
		
		if(keycode == Input.Keys.LEFT){
			player.slideLeft();
		}
		
		if(keycode == Input.Keys.RIGHT) {
			player.slideRight();
		}
		
		if(keycode == Input.Keys.DOWN) {
			player.brake(5.0f);
		}
		
		if(keycode == Input.Keys.SPACE && canJump) {
			player.jump();
			jumpCooldown = 0.0f;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	
	//bewegt man die maus in die jeweiligen bereiche, wird slideLeft() / slideRight() aufgrufen
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		if(screenX < 50){
			player.slideLeft();

		}
		
		if(screenX > (Gdx.graphics.getWidth() - 50)) {
			player.slideRight();

		}
		
		if(screenX > 50 && screenX < (Gdx.graphics.getWidth() - 50) && screenY > (Gdx.graphics.getHeight() - 30)) {
			player.brake(1.0f);
			
		}
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	//abfrage, ob ein Key gedrückt wird, wird ein Key gedrückt, wird die entsprchende methode aufgrufen
	
	public void update(){
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			keyDown(Input.Keys.UP);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			keyDown(Input.Keys.LEFT);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			keyDown(Input.Keys.RIGHT);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			keyDown(Input.Keys.DOWN);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			keyDown(Input.Keys.SPACE);
		}
		
		if(Gdx.input.isTouched()) {
			
		}

		
		//hier wird der jumpCooldown hochgezählt bis er den wert 5.0f erreicht, hat jumpCooldown den wert 5.0f erreiht, wird canJump = true
		if(jumpCooldown <= 1.0f) {
			jumpCooldown += Gdx.graphics.getDeltaTime();
			canJump = false;
		} else {
			canJump = true;
		}
	}

}