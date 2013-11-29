package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fh.teamproject.screens.menusites.CreditsSite;
import fh.teamproject.screens.menusites.MainMenuSite;

public class MenuScreen implements Screen {
    
    private Table actualSite;
    private Table[] siteList;
    private String[] nameList;
    private Stage stage;
    private Game game;

    public MenuScreen(Game g)
    {
        actualSite = null;
        siteList = new Table[5];
        nameList = new String[5];
        stage = null;
        game = g;
    }
    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        //Stage
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        siteList[0] = new MainMenuSite(this);
        nameList[0] = "MainMenu";
        siteList[1] = new CreditsSite(this);
        nameList[1] = "Credits";
        siteList[2] = null;
        nameList[2] = "No Site";
        siteList[3] = null;
        nameList[3] = "No Site";
        siteList[4] = null;
        nameList[4] = "No Site";
        actualSite = siteList[0];
        stage.addActor(actualSite);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
    
    public Game getGame() {
        return this.game;
    }
    public Table getActualSite() {
        return actualSite;
    }
    public void setActualSite(int i) {
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        this.actualSite = siteList[i];
        stage.addActor(actualSite);
    }
    public int getIndex(String name) {
        for (int i = 0; i < nameList.length; i++) {
            if (name.equals(nameList[i])) {
                return i;
            }
        }
        return -1;
    }
}
