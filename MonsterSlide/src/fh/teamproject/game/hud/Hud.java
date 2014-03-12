package fh.teamproject.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import fh.teamproject.game.World;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.SkinManager;

public class Hud {

	public Stage stage;
	public Table hud, pause, points, coins;
	private Label labelPoints, labelCoins;

	// Abstand zu den Kanten.
	private float padding = 15.0f;

	Skin skin;
	HudEventListener listener;
	Image mapOverview, coin;
	World world;

	public Hud(World world) {
		skin = SkinManager.skin;
		this.world = world;
		mapOverview = new Image();

		Texture coinTexture = new Texture(Gdx.files.internal("data/hud/coin.png"));
		coin = new Image(coinTexture);

		listener = new HudEventListener(world);
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

		coins = new Table().top().left().padLeft(this.padding).padTop(this.padding);
		coins.setFillParent(true);
		coins.add(coin);
		labelCoins = new Label("0", skin);
		labelCoins.setName("coins");
		coins.add(labelCoins);
		stage.addActor(coins);

		points = new Table().bottom().left().padLeft(this.padding)
				.padBottom(this.padding);
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

	public void setCoinCount(int count) {
		this.labelCoins.setText(count + "");
	}

	public void setViewport(float width, float height) {
		stage.setViewport(width, height);
	}

	public void togglePauseMenu() {
		pause.setVisible(!pause.isVisible());
	}

	public void update() {
		stage.act();
		setCoinCount(world.getScore().getCoinsScore());
		setPoints(world.getScore().getSlidedDistance());
	}

	public void render() {
		stage.draw();
		Table.drawDebug(stage);
	}

	public void generateSlideOverview() {

	}

}
