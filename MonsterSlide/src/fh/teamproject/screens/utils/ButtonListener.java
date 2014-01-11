package fh.teamproject.screens.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fh.teamproject.screens.GameScreen;
import fh.teamproject.screens.MenuScreen;

public class ButtonListener extends ChangeListener{
    private MenuScreen menu;
    private String name;
    public ButtonListener(String name, MenuScreen menu)
    {
        this.name = name;
        this.menu = menu;
    }
    
    @Override
	public void changed(ChangeEvent event, Actor actor) {
        if (this.name.equals("")) {}
        else if (this.name.equals("Game")) {
			this.menu.getGame().setScreen(new GameScreen(this.menu.getGame()));
        }
        else if (this.name.equals("Exit")) {
            System.exit(0);
        }
        else {
            this.menu.setActualSite(this.menu.getIndex(this.name));
        }
    }

}
