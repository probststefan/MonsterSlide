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
		if(keycode == Input.Keys.A){
			player.slideLeft();
			System.out.println("keyDown A");
		}
		
		if(keycode == Input.Keys.D) {
			player.slideRight();
			System.out.println("keyDown D");
		}
		
		if(keycode == Input.Keys.S) {
			player.brake(5.0f);
			System.out.println("Brake");
		}
		
		if(keycode == Input.Keys.F && canJump) {
			player.jump();
			jumpCooldown = 0.0f;
			System.out.println("jump");
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
			System.out.println("Maus Links");
		}
		
		if(screenX > (Gdx.graphics.getWidth() - 50)) {
			player.slideRight();
			System.out.println("Maus Rechts");
		}
		
		if(screenX > 50 && screenX < (Gdx.graphics.getWidth() - 50) && screenY > (Gdx.graphics.getHeight() - 30)) {
			player.brake(1.0f);
			System.out.println("Maus Brake");
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
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			keyDown(Input.Keys.A);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			keyDown(Input.Keys.D);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			keyDown(Input.Keys.S);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			keyDown(Input.Keys.F);
		}
		
		if(Gdx.input.isTouched()) {
			
		}

		
		//hier wird der jumpCooldown hochgezählt bis er den wert 5.0f erreicht, hat jumpCooldown den wert 5.0f erreiht, wird canJump = true
		if(jumpCooldown <= 5.0f) {
			jumpCooldown += Gdx.graphics.getDeltaTime();
			canJump = false;
		} else {
			canJump = true;
		}
	}

}