package fh.teamproject.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.utils.SkinManager;

public class DemoSite extends AbstractMenuSite {

    private TextButton start;
    Skin skin = SkinManager.skin;
    private ButtonListener listener;
    
    public DemoSite(MenuScreen menu) {
        super();
        //set the Table
        this.setWidth(Gdx.graphics.getWidth());
        this.setHeight(Gdx.graphics.getHeight());
        this.center();
        //Listener + TextButton
        listener = new ButtonListener("Game",menu);
        start = new TextButton("New Game", skin, "menuButton");
        start.setWidth(Gdx.graphics.getWidth());
        start.setHeight(Gdx.graphics.getHeight());
        start.addListener(listener);
        start.center();
        //Adden of the Button
        this.add(start).expandX().expandY();
    }

    @Override
    public void resize(float width, float height) {
    }
}
