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

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.utils.ButtonListener;

public class MainMenuSite extends Table{

    private TextButton[] buttons;
    private TextButtonStyle style;
    private ButtonListener[] listener;
    
    public MainMenuSite(MenuScreen menu)
    {
        //Table
        super();
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/mainmenu.jpg"), true))));
        this.right().bottom();
        //Style
        style = new TextButtonStyle();
        style.checkedFontColor = new Color(Color.BLUE);
        style.checkedOverFontColor = new Color(Color.MAGENTA);
        style.downFontColor = new Color(Color.GREEN);
        style.fontColor = new Color(Color.BLUE);
        style.overFontColor = new Color(Color.MAGENTA);
        style.font = new BitmapFont();
        //Listener + Textbuttons
        listener = new ButtonListener[5];
        listener[0] = new ButtonListener("Game", menu);
        listener[1] = new ButtonListener("Credits", menu);
        listener[2] = new ButtonListener(null, menu);
        listener[3] = new ButtonListener(null, menu);
        listener[4] = new ButtonListener("Exit", menu);
        buttons = new TextButton[5];
        buttons[0] = new TextButton("New Game", style);
        buttons[1] = new TextButton("Credits", style);
        buttons[2] = new TextButton("NoAction", style);
        buttons[3] = new TextButton("NoAction", style);
        buttons[4] = new TextButton("Exit", style);
        //Adden Rückwärts vom erstellen
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addListener(listener[i]);
            buttons[i].center();
            this.add(buttons[i]);
            this.row();
        }
    }

}
