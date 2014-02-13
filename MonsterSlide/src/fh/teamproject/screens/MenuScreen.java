package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fh.teamproject.screens.menusites.AbstractMenuSite;
import fh.teamproject.screens.menusites.CreditsSite;
import fh.teamproject.screens.menusites.DemoSite;
import fh.teamproject.screens.menusites.MainMenuSite;

public class MenuScreen implements Screen {

	private AbstractMenuSite actualSite;
	private AbstractMenuSite[] siteList;
	private String[] nameList;
	private Stage stage;
	private Game game;

	private Table invisible;

	public MenuScreen(Game game) {
		this.game = game;

		this.invisible = null;
		this.actualSite = null;
		this.siteList = new AbstractMenuSite[5];
		this.nameList = new String[5];
		this.stage = null;
		// this.game = g;
	}

	@Override
	public void render(float delta) {
		Gdx.graphics.getGL20().glClear(
				GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		this.stage.act();
		this.stage.draw();
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
		// Stage
		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(this.stage);
		this.invisible = new Table();
		this.invisible.setWidth(Gdx.graphics.getWidth());
		this.invisible.setHeight(Gdx.graphics.getHeight());
		// invisible.setBackground(new TextureRegionDrawable(new
		// TextureRegion(new Texture(Gdx.files.internal("data/mainmenu.jpg"),
		// true))));

		this.invisible.center();
		this.makeSites();
		this.actualSite = this.siteList[0];
		this.stage.addActor(this.invisible);
		this.invisible.add(this.actualSite);
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
		Gdx.app.log("Dispose ", "Dispose von MenuScreen");
	}

	public Game getGame() {
		return this.game;
	}

	public Table getActualSite() {
		return this.actualSite;
	}

	public void setActualSite(int i) {
		this.stage.clear();
		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(this.stage);
		this.actualSite = this.siteList[i];
		this.stage.addActor(this.actualSite);
	}

	public int getIndex(String name) {
		for (int i = 0; i < this.nameList.length; i++) {
			if (name.equals(this.nameList[i])) {
				return i;
			}
		}
		return -1;
	}

	private void makeSites() {
		this.siteList[0] = new MainMenuSite(this);
		this.nameList[0] = "MainMenu";
		this.siteList[1] = new CreditsSite(this);
		this.nameList[1] = "Credits";
		this.siteList[2] = new DemoSite(this);
		this.nameList[2] = "Demo";
		this.siteList[3] = null;
		this.nameList[3] = "No Site";
		this.siteList[4] = null;
		this.nameList[4] = "No Site";
	}
}
