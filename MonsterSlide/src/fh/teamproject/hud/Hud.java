package fh.teamproject.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.SkinManager;

public class Hud {

	public Stage stage;
	public Table root;

	Skin skin;
	GameScreen gameScreen;
	HudEventListener listener;

	public Hud(GameScreen gameScreen) {
		skin = SkinManager.skin;
		this.gameScreen = gameScreen;
		listener = new HudEventListener(gameScreen);
		stage = new Stage();
		root = new Table();
		root.setFillParent(true);
		root.setVisible(false);
		root.add(createHud());
		stage.addActor(root);
		root.debug(Debug.all);

	}
	public void setViewport(float width, float height) {
		stage.setViewport(width, height);
	}

	public void update() {
		stage.act();
	}

	public void render() {
		Table.drawDebug(stage);
		stage.draw();
	}

	private Table createHud() {
		Table content = new Table();
		content.defaults().pad(10);
		Button tmp = new Button(new Label("Reset", skin, "debug"), skin,
				"debug");
		tmp.setName("reset");
		tmp.addListener(listener);
		content.add(tmp);
		return content;

	}

}
