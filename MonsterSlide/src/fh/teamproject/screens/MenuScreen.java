package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fh.teamproject.screens.menusites.AbstractMenuSite;
import fh.teamproject.screens.menusites.CreditsSite;
import fh.teamproject.screens.menusites.DemoSite;
import fh.teamproject.screens.menusites.MainMenuSite;

public class MenuScreen implements Screen {
    
    private AbstractMenuSite actualSite;
    private AbstractMenuSite[] siteList;
    private String[] nameList;
    private Stage stage;
    private Game game;

    public MenuScreen(Game g)
    {
        actualSite = null;
        siteList = new AbstractMenuSite[5];
        nameList = new String[5];
        stage = null;
        game = g;
    }
    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        stage.act();
        stage.draw();
        Table.drawDebug(stage);
    }

    @Override
    public void resize(int width, int height) {
        //System.out.println(width + "    " + height);
        stage.setViewport(width, height, true);
        actualSite.resize(width, height);
    }

    @Override
    public void show() {
        //Stage
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        makeSites();
        actualSite = siteList[2];
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
        stage.clear();
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
    private void makeSites() {
        siteList[0] = new MainMenuSite(this);
        nameList[0] = "MainMenu";
        siteList[1] = new CreditsSite(this);
        nameList[1] = "Credits";
        siteList[2] = new DemoSite(this);
        nameList[2] = "Demo";
        siteList[3] = null;
        nameList[3] = "No Site";
        siteList[4] = null;
        nameList[4] = "No Site";
    }
}
