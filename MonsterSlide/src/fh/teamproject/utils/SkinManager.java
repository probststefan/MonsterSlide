package fh.teamproject.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class SkinManager {
    public Skin skin = new Skin();
    
    public SkinManager() {
        setupDebugSkin();
    }
    private Skin setupDebugSkin() {
        BitmapFont font = new BitmapFont();

        LabelStyle labels = new LabelStyle();
        labels.font = font;
        labels.fontColor = Color.WHITE;

        this.skin.add("default", labels);
        return skin;
    }
}
