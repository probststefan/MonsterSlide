package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import fh.teamproject.screens.menusites.MainMenuSite;
import fh.teamproject.screens.utils.IMenuScreen;

public class MenuScreen implements Screen {
    
    private IMenuScreen actualSite;
    private String[] menuNames;
    private IMenuScreen[] siteList;
    private Game game;

    public MenuScreen(Game g)
    {
        game = g;
        siteList = new IMenuScreen[5];
        menuNames = new String[5];
        actualSite = siteList[0] = new MainMenuSite(this);
        menuNames[0] = "MainMenu";
    }
    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        actualSite.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        actualSite.resize(width, height);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        actualSite.show();
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        actualSite.hide();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        actualSite.pause();
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        actualSite.resume();
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        actualSite.dispose();
    }
    
    public Game getGame() {
        return this.game;
    }
    
    public IMenuScreen findMenuScreen(String name)
    {
        for (int i = 0; i < menuNames.length; i++)
        {
            if (name.equals(menuNames[i])) {
                return siteList[i];
            }
        }
        return null;
    }
    public IMenuScreen getActualSite() {
        return actualSite;
    }
    public void setActualSite(IMenuScreen actualSite) {
        this.actualSite = actualSite;
    }
}
