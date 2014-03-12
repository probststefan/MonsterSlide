package fh.teamproject.game.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fh.teamproject.game.World;
import fh.teamproject.screens.GameScreen;

public class HudEventListener extends ChangeListener {

	World world;

	public HudEventListener(World world) {
		this.world = world;
	}

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		if (actor.getName().equals("reset")) {
			this.world.reset();
		}
	}
}
