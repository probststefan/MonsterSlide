package fh.teamproject.screens.menusites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.utils.ButtonListener;

public class DemoSite extends AbstractMenuSite {

    private TextButton start;
    private TextButtonStyle style;
    private ButtonListener listener;
    
    public DemoSite(MenuScreen menu)
    {
        super();
        //set the Table
        //this.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/mainmenu.jpg"), true))));
        this.setWidth(Gdx.graphics.getWidth());
        this.setHeight(Gdx.graphics.getHeight());
        //this.center();
        //Styles and Font
        //Build the Fontgenerator, Generate the Font and dispose the Generator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Outage.ttf"));
        BitmapFont font = generator.generateFont((int) (12 + Gdx.graphics.getHeight()/10)); // font size 12 pixels + 1 for every 100 Pixel in the Gdx.graphics.getHeight()
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        //Create a Style with the Colors and add fhe font to it
        style = new TextButtonStyle();
        style.checkedFontColor = new Color(Color.BLUE);
        style.checkedOverFontColor = new Color(Color.MAGENTA);
        style.downFontColor = new Color(Color.GREEN);
        style.fontColor = new Color(Color.BLUE);
        style.overFontColor = new Color(Color.MAGENTA);
        style.font = font;
        //Listener and TextButton
        listener = new ButtonListener("Game",menu);
        start = new TextButton("New Game", style);
        start.setWidth(Gdx.graphics.getWidth());
        start.setHeight(Gdx.graphics.getHeight());
        start.addListener(listener);
        //start.center();
        //Adden Rückwärts vom erstellen
        this.add(start).expandX().expandY();
        this.debug(Debug.all);
        // TODO Auto-generated method stub
        //System.out.println("ResizeBreite: " + width + "    ResizeHöhe: " + Gdx.graphics.getHeight());
        //System.out.println("TableBreite: " + this.getWidth() + "    TableHöhe: " + this.getGdx.graphics.getHeight()());
        //System.out.println("ButtonBreite: " + start.getWidth() + "    ButtonHöhe: " + start.getGdx.graphics.getHeight()());
    }

    @Override
    public void resize(float width, float height) {

    }
}
