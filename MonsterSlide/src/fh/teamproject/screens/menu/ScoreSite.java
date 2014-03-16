package fh.teamproject.screens.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.MenuScreen.SITES;
import fh.teamproject.utils.SkinManager;

/**
 * Zeigt dem Spieler seinen erreichten Punktestand und evtl. noch weitere
 * Informationen. Zusätzlich könnte hier auch Werbung angzeigt werden.
 * 
 * @author stefanprobst
 * 
 */
public class ScoreSite extends AbstractMenuSite {

	private Game game;

	private Table scoreTable;
	private Table buttonTable;
	private TextButton backButton, playButton;
	private Skin skin;
	private ButtonListener listener;

	public ScoreSite(Game game, MenuScreen menu) {
		this.game = game;
		this.skin = SkinManager.skin;

		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.center();
		scoreTable = new Table();
		scoreTable.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		scoreTable.center();
		buttonTable = new Table();
		buttonTable.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		buttonTable.right().bottom();

		// Score + Buttons
		Label scoreLabel = new Label("123098123", this.skin);
		scoreTable.add(scoreLabel);
		scoreTable.row();

		// Listener + TextButtons
		listener = new ButtonListener("MainMenu", menu);
		playButton = new TextButton("Play again", skin, "menuButton");
		playButton.setUserObject(SITES.LOADING);
		playButton.addListener(listener);

		backButton = new TextButton("Back", skin, "menuButton");
		backButton.setUserObject(SITES.MAIN_MENU);
		backButton.addListener(listener);

		buttonTable.add(playButton);
		buttonTable.row();
		buttonTable.add(backButton);

		this.add(scoreTable);
		this.row();
		this.add(buttonTable);
	}

	@Override
	public void resize(float width, float height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}
}