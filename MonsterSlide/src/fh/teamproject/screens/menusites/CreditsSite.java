package fh.teamproject.screens.menusites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.utils.ButtonListener;

public class CreditsSite extends AbstractMenuSite {

    private Table nametable;
    private Table backtable;
    private TextButton[] names;
    private TextButton back;
    private TextButtonStyle nameStyle;
    private TextButtonStyle backStyle;
    private ButtonListener listener;
    
    public CreditsSite(MenuScreen menu)
    {
        //Tables
        super();
        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/mainmenu.jpg"), true))));
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.center();
        nametable = new Table();
        nametable.setSize(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        nametable.center();
        backtable = new Table();
        backtable.setSize(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        backtable.right().bottom();
        //Styles
        nameStyle = new TextButtonStyle();
        nameStyle.fontColor = new Color(Color.RED);
        nameStyle.overFontColor = new Color(Color.WHITE);
        nameStyle.font = new BitmapFont();
        backStyle = new TextButtonStyle();
        backStyle.checkedFontColor = new Color(Color.BLUE);
        backStyle.checkedOverFontColor = new Color(Color.MAGENTA);
        backStyle.downFontColor = new Color(Color.GREEN);
        backStyle.fontColor = new Color(Color.BLUE);
        backStyle.overFontColor = new Color(Color.MAGENTA);
        backStyle.font = new BitmapFont();
        //Listener + TextButtons
        listener = new ButtonListener("MainMenu",menu);
        back = new TextButton("Back", backStyle);
        back.addListener(listener);
        back.center();
        names = new TextButton[4];
        names[0] = new TextButton("Attila Djerdj", nameStyle);
        names[1] = new TextButton("Kevin Korte", nameStyle);
        names[2] = new TextButton("Stefan Probst", nameStyle);
        names[3] = new TextButton("Torsten Scholer", nameStyle);
        //Adden Rückwärts vom erstellen
        for (int i = 0; i < names.length; i++) {
            names[i].center();
            nametable.add(names[i]);
            names[i].setDisabled(true);
            nametable.row();
        }
        backtable.add(back);
        this.add(nametable);
        this.row();
        this.add(backtable);
        this.debug(Debug.all);
    }

    @Override
    public void resize(float width, float height) {
        // TODO Auto-generated method stub
        
    }
}
