package fh.teamproject.screens.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class AbstractMenuSite extends Table {
	public abstract void resize(float width, float height);

	public abstract void render(float delta);
}
