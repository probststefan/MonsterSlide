package fh.teamproject.screens.menusites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.utils.ButtonListener;

public class DemoSite extends Table{

    private TextButton start;
    private TextButtonStyle style;
    private ButtonListener listener;
    
    public DemoSite(MenuScreen menu)
    {
        //Tables
        super();
        //this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/mainmenu.jpg"), true))));
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.center();
        //Styles
        style = new TextButtonStyle();
        style.checkedFontColor = new Color(Color.BLUE);
        style.checkedOverFontColor = new Color(Color.MAGENTA);
        style.downFontColor = new Color(Color.GREEN);
        style.fontColor = new Color(Color.BLUE);
        style.overFontColor = new Color(Color.MAGENTA);
        style.font = new BitmapFont();
        style.font.scale(2.0f);
        //Listener + TextButton
        listener = new ButtonListener("Game",menu);
        start = new TextButton("New Game", style);
        start.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        start.addListener(listener);
        start.center();
        //Adden Rückwärts vom erstellen
        this.add(start);
        for (@SuppressWarnings("rawtypes") com.esotericsoftware.tablelayout.Cell c : this.getCells()) {
            c.size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        this.debug(Debug.all);
    }
}
