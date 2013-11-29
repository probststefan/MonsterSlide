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
    
    public void changed(ChangeEvent event, Actor actor) {
        if (name.equals("Game")) {
            menu.getGame().setScreen(new GameScreen());
        }
        else if (name.equals("Exit")) {
            System.exit(0);
        }
        else {
            menu.setActualSite(menu.getIndex(name));
        }
    }

}
