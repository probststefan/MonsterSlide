package fh.teamproject.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SkinManager {
	public static Skin skin = new Skin();
	public String density;
	public int densityAsInt;

	public SkinManager() {
	    if (Gdx.graphics.getWidth() >= 960) {
	        density = "xlarge";
	        densityAsInt = 4;
	    } else if (Gdx.graphics.getWidth() >= 640) {
	        density = "large";
	        densityAsInt = 3;
	    } else if (Gdx.graphics.getWidth() >= 470) {
	        density = "medium";
	        densityAsInt = 2;
	    } else {
	        density = "small";
	        densityAsInt = 1;
	    }
		this.setupDebugSkin();
		this.setupMenuButtonSkin();
		this.setupCreditsHeadLineSkin();
		this.setupCreditsTextSkin();
	}

	private void setupDebugSkin() {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGB565);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		TextureRegion defaultBlackBackground = new TextureRegion(new Texture(pixmap));
		SkinManager.skin.add("debug", defaultBlackBackground);

		pixmap.setColor(0.33f, 0.33f, 0.33f, 1f);
		pixmap.fill();
		TextureRegionDrawable bgTex = new TextureRegionDrawable(new TextureRegion(
				new Texture(pixmap)));

		pixmap.setColor(Color.WHITE);
		pixmap.drawRectangle(0, 0, 5, 15);
		TextureRegionDrawable cursor = new TextureRegionDrawable(new TextureRegion(
				new Texture(pixmap)));

		BitmapFont font = new BitmapFont();
		LabelStyle labels = new LabelStyle();
		labels.font = font;
		labels.fontColor = Color.WHITE;
		labels.background = bgTex;
		SkinManager.skin.add("debug", labels);

		TextFieldStyle tfStyle = new TextFieldStyle();
		tfStyle.font = font;
		tfStyle.fontColor = Color.WHITE;
		tfStyle.background = bgTex;
		tfStyle.cursor = cursor;
		SkinManager.skin.add("debug", tfStyle);

		ButtonStyle buttons = new ButtonStyle();
		SkinManager.skin.add("debug", buttons);

	}
	
	private void setupMenuButtonSkin() {
	    BitmapFont font = this.generateFont();
        //Create a Style with the Colors and add fhe font to it
        TextButtonStyle style = new TextButtonStyle();
        style.checkedFontColor = new Color(Color.BLUE);
        style.checkedOverFontColor = new Color(Color.MAGENTA);
        style.downFontColor = new Color(Color.GREEN);
        style.fontColor = new Color(Color.BLUE);
        style.overFontColor = new Color(Color.MAGENTA);
        style.font = font;
        SkinManager.skin.add("menuButton", style);
	}
	
	private void setupCreditsTextSkin() {
	    BitmapFont font = this.generateFont();
        //Create a Style with the Colors and add fhe font to it
        TextButtonStyle style = new TextButtonStyle();
        style.fontColor = new Color(Color.RED);
        style.overFontColor = new Color(Color.WHITE);
        style.font = font;
        SkinManager.skin.add("creditsText", style);
	}
	
	private void setupCreditsHeadLineSkin() {
	    BitmapFont font = this.generateFont();
        //Create a Style with the Colors and add fhe font to it
        TextButtonStyle style = new TextButtonStyle();
        style.fontColor = new Color(Color.CYAN);
        style.overFontColor = new Color(Color.CYAN);
        style.font = font;
        SkinManager.skin.add("creditsHeadline", style);
	}
	
	private BitmapFont generateFont() {
	    BitmapFont font;
        //Build the Fontgenerator, Generate the Font and dispose the Generator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Outage.ttf"));
        switch(densityAsInt) {
        case 4:
            font = generator.generateFont((int) (12 + Gdx.graphics.getHeight()/20)); 
            // font size 12 pixels + 1 for every 20 Pixel in the Gdx.graphics.getHeight()
            break;
        case 3:
            font = generator.generateFont((int) (12 + Gdx.graphics.getHeight()/30)); 
            // font size 12 pixels + 1 for every 30 Pixel in the Gdx.graphics.getHeight()
            break;
        case 2:
            font = generator.generateFont((int) (12 + Gdx.graphics.getHeight()/50)); 
            // font size 12 pixels + 1 for every 50 Pixel in the Gdx.graphics.getHeight()
            break;
        case 1:
            font = generator.generateFont((int) (12 + Gdx.graphics.getHeight()/60)); 
            // font size 12 pixels + 1 for every 60 Pixel in the Gdx.graphics.getHeight()
            break;
        default:
            font = generator.generateFont((int) (12 + Gdx.graphics.getHeight()/40)); 
            // font size 12 pixels + 1 for every 40 Pixel in the Gdx.graphics.getHeight()
        }
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        return font;
    }
}
