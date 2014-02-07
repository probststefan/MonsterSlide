package fh.teamproject.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.screens.GameScreen;
import fh.teamproject.utils.SkinManager;

public class Hud {

	public Stage stage;
	public Table root;

	Skin skin;
	GameScreen gameScreen;
	HudEventListener listener;
	Image mapOverview;

	public Hud(GameScreen gameScreen) {
		skin = SkinManager.skin;
		mapOverview = new Image();
		this.gameScreen = gameScreen;
		listener = new HudEventListener(gameScreen);
		stage = new Stage();
		root = new Table();
		root.defaults().expand();
		root.setFillParent(true);
		root.add(mapOverview).top().left();
		stage.addActor(root);
		root.debug(Debug.all);

		Table pause = new Table();
		Button tmp = new Button(new Label("Reset", skin, "debug"), skin, "debug");
		tmp.setName("reset");
		tmp.addListener(listener);
		pause.add(tmp);

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

	public void generateSlideOverview() {
		int offset = (int) GameScreen.settings.SLIDE_DIMENSION / 3;
		Pixmap pixmap = new Pixmap((int) GameScreen.settings.SLIDE_DIMENSION + offset,
				(int) GameScreen.settings.SLIDE_DIMENSION + offset, Format.RGBA8888);

		Array<ISlidePart> slideParts = gameScreen.world.getSlide().getSlideParts();
		Array<Vector3> vertices = slideParts.first().getInterpolatedVertices();
		pixmap.setColor(Color.valueOf("001A33"));
		pixmap.fill();
		pixmap.setColor(Color.ORANGE);
		for (int i = 0; i < (vertices.size - 1); ++i) {
			Vector3 first = vertices.get(i);
			Vector3 next = vertices.get(i + 1);
			pixmap.drawLine((int) first.x + (offset / 2), (int) first.z + (offset / 2),
					(int) next.x + (offset / 2), (int) next.z + (offset / 2));
		}

		pixmap.setColor(Color.RED);

		for (Vector3 v : slideParts.first().getControlPoints()) {
			pixmap.fillCircle((int) v.x + (offset / 2), (int) v.z + (offset / 2), 3);
		}

		Vector3 tmp = slideParts.first().getControlPoints().first();
		pixmap.setColor(Color.YELLOW);
		pixmap.fillCircle((int) tmp.x + (offset / 2), (int) tmp.z + (offset / 2), 3);

		tmp = slideParts.first().getControlPoints().get(slideParts.first()
				.getControlPoints().size - 1);
		pixmap.setColor(Color.GREEN);
		pixmap.fillCircle((int) tmp.x + (offset / 2), (int) tmp.z + (offset / 2), 3);
		mapOverview.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(
				pixmap))));
	}
}
