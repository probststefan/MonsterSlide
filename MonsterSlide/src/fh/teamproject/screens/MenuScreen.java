package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {

    private Game game;
    private Stage stage;
    private Table table;
    private TextButton button;
    private TextButtonStyle style;
    public MenuScreen(Game g)
    {
        game = g;
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        style = new TextButtonStyle();
        style.checkedFontColor = new Color(Color.BLUE);
        style.checkedOverFontColor = new Color(Color.RED);
        style.downFontColor = new Color(Color.GREEN);
        style.fontColor = new Color(Color.YELLOW);
        style.overFontColor = new Color(Color.PINK);
        style.font = new BitmapFont();
        button = new TextButton("start Game", style);
        button.setSize(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        button.center();
        button.setPosition(0, 0);
        button.addListener(new ChangeListener() {
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO Auto-generated method stub
                game.setScreen(new GameScreen());
            }
        });
        table.add(button);
        stage.addActor(table);
    }
    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

}
