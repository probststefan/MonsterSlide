package fh.teamproject.screens.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fh.teamproject.MonsterSlide;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.SkinManager;

public class LoadingSite extends AbstractMenuSite {

	private MonsterSlide game;
	private Table table;
	private Label loadingLabel;

	public LoadingSite(Game game) {
		this.game = (MonsterSlide) game;

		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.center();
		table = new Table();
		table.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		table.center();

		// Loading Text
		loadingLabel = new Label("Loading ...", SkinManager.skin);
		table.add(loadingLabel);
		table.row();

		this.add(table);
		this.loadAssets();
	}

	public void render(float delta) {
		if (getAssets().update()) {
			GameScreen gameScreen = new GameScreen(this.game);
			this.game.setScreen(gameScreen);
		} else {
			this.loadingLabel.setText("Loading "
					+ MathUtils.round(getAssets().getProgress() * 100) + "%");
		}
	}

	@Override
	public void resize(float width, float height) {
		// TODO Auto-generated method stub

	}

	public AssetManager getAssets() {
		return game.getAssets();
	}

	private void loadAssets() {
		getAssets().load("model/coins/coin.g3db", Model.class);
		getAssets().load("data/g3d/skydome.g3db", Model.class);
		getAssets().load("model/orc/micro_orc.g3db", Model.class);
		getAssets().load("model/orc/micro_orc_mobile.g3db", Model.class);
		getAssets().load("model/orc/micro_orc.obj", Model.class);
		getAssets().load("model/pumpkin/pumpkin_01_01_a.g3db", Model.class);
		getAssets().load("model/pumpkin/pumpkin_02_01_a.g3db", Model.class);
		getAssets().load("model/pumpkin/pumpkin_03_01_a.g3db", Model.class);
		getAssets().load("model/pumpkin/pumpkin_04_01_a.g3db", Model.class);
	}
}