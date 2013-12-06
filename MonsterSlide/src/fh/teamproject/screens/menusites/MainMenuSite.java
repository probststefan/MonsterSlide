package fh.teamproject.screens.menusites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.utils.ButtonListener;

public class MainMenuSite extends AbstractMenuSite {

    private TextButton[] buttons;
    private TextButtonStyle style;
    private ButtonListener[] listener;
    
    public MainMenuSite(MenuScreen menu)
    {
        //Table
        super();
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// this.setBackground(new TextureRegionDrawable(new TextureRegion(new
		// Texture(Gdx.files.internal("data/mainmenu.jpg"), true))));
        //Style
        this.style = new TextButtonStyle();
        this.style.checkedFontColor = new Color(Color.BLUE);
        this.style.checkedOverFontColor = new Color(Color.MAGENTA);
        this.style.downFontColor = new Color(Color.GREEN);
        this.style.fontColor = new Color(Color.BLUE);
        this.style.overFontColor = new Color(Color.MAGENTA);
        this.style.font = new BitmapFont();
        //Listener + Textbuttons
        this.listener = new ButtonListener[5];
        this.listener[0] = new ButtonListener("Game", menu);
        this.listener[1] = new ButtonListener("Credits", menu);
        this.listener[2] = new ButtonListener(null, menu);
        this.listener[3] = new ButtonListener(null, menu);
        this.listener[4] = new ButtonListener("Exit", menu);
        this.buttons = new TextButton[5];
        this.buttons[0] = new TextButton("New Game", this.style);
        this.buttons[0].center();
        this.buttons[1] = new TextButton("Credits", this.style);
        this.buttons[2] = new TextButton("NoAction", this.style);
        this.buttons[3] = new TextButton("NoAction", this.style);
        this.buttons[4] = new TextButton("Exit", this.style);
        //Adden Rückwärts vom erstellen
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i].addListener(this.listener[i]);
            this.buttons[i].center();
            this.add(this.buttons[i]);
            this.row();
        }
        //this.debug(Debug.all);
    }

    @Override
    public void resize(float width, float height) {
        // TODO Auto-generated method stub
        
    }

}
