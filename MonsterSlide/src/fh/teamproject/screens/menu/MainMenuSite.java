package fh.teamproject.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import fh.teamproject.screens.MenuScreen;
import fh.teamproject.screens.MenuScreen.SITES;
import fh.teamproject.utils.SkinManager;

public class MainMenuSite extends AbstractMenuSite {

	private TextButton[] buttons;
	private Skin skin;
	private ButtonListener[] listener;

	public MainMenuSite(MenuScreen menu) {
		// Table
		super();
		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// The Skin
		this.skin = SkinManager.skin;
		// Listener + Textbuttons
		this.listener = new ButtonListener[4];
		this.listener[0] = new ButtonListener("Game", menu);
		this.listener[1] = new ButtonListener("Credits", menu);
		this.listener[2] = new ButtonListener("Demo", menu);
		this.listener[3] = new ButtonListener("Exit", menu);
		this.buttons = new TextButton[4];
		this.buttons[0] = new TextButton("New Game", skin, "menuButton");
		this.buttons[0].setName("Game");
		this.buttons[1] = new TextButton("Credits", skin, "menuButton");
		this.buttons[1].setUserObject(SITES.CREDITS);
		this.buttons[2] = new TextButton("Demo", skin, "menuButton");
		this.buttons[2].setUserObject(SITES.DEMO);
		this.buttons[3] = new TextButton("Exit", skin, "menuButton");
		this.buttons[3].setName("Exit");
		// Adden Backwards from Created
		for (int i = 0; i < this.buttons.length; i++) {
			this.buttons[i].addListener(this.listener[i]);
			this.buttons[i].center();
			this.add(this.buttons[i]);
			this.row();
		}
	}

	@Override
	public void resize(float width, float height) {
		// TODO Auto-generated method stub
	}
}