package fh.teamproject.screens.menusites;

import org.omg.CORBA.FREE_MEM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;
import com.esotericsoftware.tablelayout.Cell;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.utils.ButtonListener;

public class DemoSite extends AbstractMenuSite {

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
        for (@SuppressWarnings("rawtypes") com.esotericsoftware.tablelayout.Cell c : this.getCells()) {
            c.size(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        //Styles and Font
        //Build the Fontgenerator, Generate the Font and dispose the Generator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Outage.ttf"));
        BitmapFont font = generator.generateFont(12 + Gdx.graphics.getHeight()/10); // font size 12 pixels + 1 for every 100 Pixel in the Height
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
        start.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        start.addListener(listener);
        start.center();
        //Adden Rückwärts vom erstellen
        this.add(start);
        this.debug(Debug.all);
    }

    @Override
    public void resize(float width, float height) {
        // TODO Auto-generated method stub
        this.setSize(width, height);
        //float w = 0;
        //float h = 0;
        //System.out.println("Breite: " + width + "    Höhe: " + height);
        for (@SuppressWarnings("rawtypes") Cell c : this.getCells()) {
            c.size(width, height);
        }
        start.setSize(width, height);
        //System.out.println("TableBreite: " + this.getWidth() + "    TableHöhe: " + this.getHeight());
        //System.out.println("ButtonBreite: " + start.getWidth() + "    ButtonHöhe: " + start.getHeight());
        //System.out.println("CellBreite: " + width + "    CellHöhe: " + height);
        start.center();
    }
}
