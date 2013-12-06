package fh.teamproject.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SkinManager {
	public static Skin skin = new Skin();

	public SkinManager() {
		this.setupDebugSkin();
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
}
