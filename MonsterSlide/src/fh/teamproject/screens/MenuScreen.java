package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;

import fh.teamproject.screens.menu.AbstractMenuSite;
import fh.teamproject.screens.menu.CreditsSite;
import fh.teamproject.screens.menu.HighscoresSite;
import fh.teamproject.screens.menu.LoadingSite;
import fh.teamproject.screens.menu.MainMenuSite;
import fh.teamproject.screens.menu.ScoreSite;

public class MenuScreen implements Screen {

	private AbstractMenuSite actualSite;
	private AbstractMenuSite[] siteList;
	private String[] nameList;
	private Stage stage;
	private Game game;

	private Table invisible;

	public enum SITES {
		MAIN_MENU, SCORE, LOADING, CREDITS, HIGHSCORES
	}

	ObjectMap<SITES, AbstractMenuSite> sites = new ObjectMap<MenuScreen.SITES, AbstractMenuSite>();

	public MenuScreen(Game game) {
		this.game = game;

		this.siteList = new AbstractMenuSite[5];
		this.nameList = new String[5];

		// Stage
		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(this.stage);
		this.invisible = new Table();
		this.invisible.setWidth(Gdx.graphics.getWidth());
		this.invisible.setHeight(Gdx.graphics.getHeight());

		this.invisible.center();
		this.makeSites();
		this.actualSite = sites.get(SITES.MAIN_MENU);
		this.stage.addActor(this.invisible);
		this.invisible.add(this.actualSite);
	}

	@Override
	public void render(float delta) {
		Gdx.graphics.getGL20().glClear(
				GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		this.stage.act();
		this.stage.draw();

		this.actualSite.render(delta);

		Table.drawDebug(this.stage);
	}

	@Override
	public void resize(int width, int height) {
		this.stage.setViewport(width, height, true);
		this.invisible.setFillParent(true);
		this.invisible.invalidate();
		this.actualSite.resize(width, height);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	public Game getGame() {
		return this.game;
	}

	public Table getActualSite() {
		return this.actualSite;
	}

	public void setActualSite(SITES site) {
		if (this.stage != null)
			this.stage.clear();
		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(this.stage);
		this.actualSite = sites.get(site);
		this.stage.addActor(this.actualSite);
	}

	private void makeSites() {
		sites.put(SITES.MAIN_MENU, new MainMenuSite(this));
		sites.put(SITES.SCORE, new ScoreSite(game, this));
		sites.put(SITES.LOADING, new LoadingSite(game));
		sites.put(SITES.CREDITS, new CreditsSite(this));
		sites.put(SITES.HIGHSCORES, new HighscoresSite(this));
	}
}