package fh.teamproject.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.MenuScreen.SITES;

public class ButtonListener extends ChangeListener {
	private MenuScreen menu;

	public ButtonListener(String name, MenuScreen menu) {
		this.menu = menu;
	}

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		if (actor.getUserObject() == null) {
			if (actor.getName().equals("Game")) {
				// this.menu.getGame().setScreen(new
				// LoadingSite(this.menu.getGame()));

			}

			if (actor.getName().equals("Exit")) {
				Gdx.app.exit();
			}
		} else {
			this.menu.setActualSite((SITES) actor.getUserObject());
		}
	}
}