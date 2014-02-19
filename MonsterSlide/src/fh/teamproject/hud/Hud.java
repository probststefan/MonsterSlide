package fh.teamproject.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.SkinManager;

public class Hud {

	public Stage stage;
	public Table hud, pause, points;
	private Label labelPoints;

	// Abstand zu den Kanten.
	private float padding = 15.0f;

	Skin skin;
	GameScreen gameScreen;
	HudEventListener listener;
	Image mapOverview;

	public Hud(GameScreen gameScreen) {
		skin = SkinManager.skin;
		mapOverview = new Image();
		this.gameScreen = gameScreen;
		listener = new HudEventListener(gameScreen);
		stage = new Stage();
		hud = new Table().top().left();
		hud.setFillParent(true);
		hud.defaults();
		hud.add(mapOverview).top().left();
		stage.addActor(hud);
		hud.debug(Debug.all);

		pause = new Table();
		pause.setFillParent(true);
		Button tmp = new Button(new Label("Reset", skin, "debug"), skin, "debug");
		tmp.setName("reset");
		tmp.addListener(listener);
		pause.add(tmp);
		stage.addActor(pause);
		pause.setVisible(false);
		pause.debug(Debug.all);

		points = new Table().bottom().left().padLeft(this.padding).padTop(this.padding);
		points.setFillParent(true);
		labelPoints = new Label("1234m", skin);
		labelPoints.setName("points");
		points.add(labelPoints);
		stage.addActor(points);
	}

	/**
	 * Setzt die aktuelle Punktezahl des Spielers auf dem Bildschirm.
	 * 
	 * @param points
	 */
	public void setPoints(float points) {
		this.labelPoints.setText((int) points + "");
	}

	public void setViewport(float width, float height) {
		stage.setViewport(width, height);
	}

	public void togglePauseMenu() {
		pause.setVisible(!pause.isVisible());
	}

	public void update() {
		stage.act();
	}

	public void render() {
		stage.draw();
		Table.drawDebug(stage);
	}

	public void generateSlideOverview() {

	}

}
