package fh.teamproject.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.MenuScreen.SITES;
import fh.teamproject.utils.SkinManager;

public class CreditsSite extends AbstractMenuSite {

	private Table nametable;
	private Table backtable;
	private TextButton[] names;
	private TextButton back;
	private Skin skin;
	private ButtonListener listener;

	public CreditsSite(MenuScreen menu) {
		// Tables
		super();
		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.center();
		nametable = new Table();
		nametable.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		nametable.center();
		backtable = new Table();
		backtable.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		backtable.right().bottom();
		// The Skin
		skin = SkinManager.skin;
		// Listener + TextButtons
		listener = new ButtonListener("MainMenu", menu);
		back = new TextButton("Back", skin, "menuButton");
		back.setUserObject(SITES.MAIN_MENU);
		back.addListener(listener);
		back.center();
		names = new TextButton[8];
		names[0] = new TextButton("Programming & Design", skin, "creditsHeadline");
		names[1] = new TextButton("Attila Djerdj", skin, "creditsText");
		names[2] = new TextButton("Kevin Korte", skin, "creditsText");
		names[3] = new TextButton("Stefan Probst", skin, "creditsText");
		names[4] = new TextButton("Torsten Scholer", skin, "creditsText");
		names[5] = new TextButton("Art", skin, "creditsHeadline");
		names[6] = new TextButton("Artistin 1", skin, "creditsText");
		names[7] = new TextButton("Artistin 2", skin, "creditsText");
		// Adden Backwards from Created
		for (int i = 0; i < names.length; i++) {
			names[i].center();
			nametable.add(names[i]);
			names[i].setDisabled(true);
			nametable.row();
		}
		backtable.add(back);
		this.add(nametable);
		this.row();
		this.add(backtable);
	}

	@Override
	public void resize(float width, float height) {
		// TODO Auto-generated method stub

	}
}
