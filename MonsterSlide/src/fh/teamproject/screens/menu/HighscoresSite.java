package fh.teamproject.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.sql.DatabaseCursor;

import fh.teamproject.MonsterSlide;
import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.MenuScreen.SITES;
import fh.teamproject.utils.SkinManager;

public class HighscoresSite extends AbstractMenuSite {

	private TextButton start;
	Skin skin = SkinManager.skin;
	private ButtonListener listener;
	private Table table;
	private Label scoreLabel;

	public HighscoresSite(MenuScreen menu) {
		// super();
		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.center();
		table = new Table();
		table.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		table.center();

		// Liste mit den Highscores.
		DatabaseCursor cursor = MonsterSlide.scoreTable.getScores(10);

		while (cursor.next()) {

			scoreLabel = new Label(String.valueOf(cursor.getString(1)) + "",
					SkinManager.skin);
			table.add(scoreLabel);
			table.row();
		}

		// Listener + TextButton
		listener = new ButtonListener("Game", menu);
		start = new TextButton("New Game", skin, "menuButton");
		start.setUserObject(SITES.LOADING);
		start.setWidth(Gdx.graphics.getWidth());
		start.setHeight(Gdx.graphics.getHeight());
		start.addListener(listener);
		start.center();
		table.add(start);

		this.add(table);
	}

	@Override
	public void resize(float width, float height) {
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}
}
