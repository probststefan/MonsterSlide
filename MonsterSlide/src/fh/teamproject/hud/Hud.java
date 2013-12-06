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
		this.skin = SkinManager.skin;
		this.gameScreen = gameScreen;
		this.listener = new HudEventListener(gameScreen);
		this.stage = new Stage();
		this.root = new Table();
		this.root.setFillParent(true);
		this.root.setVisible(false);
		this.root.add(this.createHud());
		this.stage.addActor(this.root);
		this.root.debug(Debug.all);

	}

	public void setViewport(float width, float height) {
		this.stage.setViewport(width, height);
	}

	public void update() {
		this.stage.act();
	}
	public void render() {
		Table.drawDebug(this.stage);
		this.stage.draw();
	}

	private Table createHud() {
		Table content = new Table();
		content.defaults().pad(10);
		Button tmp = new Button(new Label("Reset", this.skin, "debug"), this.skin,
				"debug");
		tmp.setName("reset");
		tmp.addListener(this.listener);
		content.add(tmp);
		return content;

	}

}
