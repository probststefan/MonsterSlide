package fh.teamproject.screens.menusites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.utils.ButtonListener;
import fh.teamproject.screens.utils.IMenuScreen;

public class MainMenuSite implements IMenuScreen{

    private MenuScreen menu;
    private Stage stage;
    private Table table;
    private TextButton[] buttons;
    private TextButtonStyle style;
    private ButtonListener[] listener;
    
    public MainMenuSite(MenuScreen menu)
    {
        this.menu = menu;
        stage = null;
        table = null;
        buttons = null;
        style = null;
        listener = null;
    }
    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        Table.drawDebug(stage);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void show() {
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/mainmenu.jpg"), true))));
        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.right();
        table.bottom();
        style = new TextButtonStyle();
        style.checkedFontColor = new Color(Color.YELLOW);
        style.checkedOverFontColor = new Color(Color.RED);
        style.downFontColor = new Color(Color.GREEN);
        style.fontColor = new Color(Color.BLUE);
        style.overFontColor = new Color(Color.MAGENTA);
        style.font = new BitmapFont();
        listener = new ButtonListener[5];
        listener[0] = new ButtonListener("Game", menu);
        listener[1] = new ButtonListener(null, menu);
        listener[2] = new ButtonListener(null, menu);
        listener[3] = new ButtonListener(null, menu);
        listener[4] = new ButtonListener(null, menu);
        buttons = new TextButton[5];
        buttons[0] = new TextButton("New Game", style);
        buttons[1] = new TextButton("Exit 2", style);
        buttons[2] = new TextButton("Exit 3", style);
        buttons[4] = new TextButton("Exit 4", style);
        buttons[3] = new TextButton("Exit", style);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addListener(listener[i]);
            buttons[i].center();
            buttons[i].setPosition(i*50, i*50);
            table.add(buttons[i]);
            table.row();
        }
        stage.addActor(table);
        table.debug(Debug.all);
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
