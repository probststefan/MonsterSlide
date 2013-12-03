package fh.teamproject.utils.debug;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;

public class DebugInfoPanel {

	public Stage stage;
	Skin debugSkin = new Skin();
	InfoPanel panel;
	Table root;
	public DebugInfoPanel() {
		this.stage = new Stage();
		this.root = new Table();
		this.root.setFillParent(true);
		this.root.top().right();
		this.stage.addActor(this.root);
		this.setupSkin();
	}

	private void setupSkin() {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGB565);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		TextureRegion region = new TextureRegion(new Texture(pixmap));
		this.debugSkin.add("default", region);
		pixmap.setColor(0.33f, 0.33f, 0.33f, 1f);
		pixmap.fill();
		TextureRegion lightBG = new TextureRegion(new Texture(pixmap));
		TextureRegionDrawable bgTex = new TextureRegionDrawable(lightBG);

		BitmapFont font = new BitmapFont();
		LabelStyle labels = new LabelStyle();
		labels.font = font;
		labels.fontColor = Color.WHITE;
		labels.background = bgTex;
		this.debugSkin.add("default", labels);

		TextFieldStyle tfStyle = new TextFieldStyle();
		tfStyle.font = font;
		tfStyle.fontColor = Color.WHITE;
		tfStyle.background = bgTex;
		this.debugSkin.add("default", tfStyle);

	}

	public void render() {
		this.stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.stage.act();
		this.panel.update();
		this.stage.draw();
	}

	public void showInfo(Object obj) {
		this.panel = null;
		this.root.clear();
		this.panel = new InfoPanel(obj, this.debugSkin);
		this.panel.setBackground("default");
		this.root.add(this.panel);
	}

}

class InfoPanel extends Table {

	ObjectMap<Field, Label> debugFields = new ObjectMap<Field, Label>(15);
	Object displayedObject;
	public InfoPanel(final Object obj, Skin skin) {
		super(skin);
		this.displayedObject = obj;
		this.defaults().align(Align.left).pad(5f).prefSize(150, 15);
		this.add(obj.toString()).colspan(3).align(Align.center).prefWidth(300);
		this.row();
		this.add("Field");
		this.add("Value");
		this.add("Set to");
		this.row();
		if (obj != null) {
			Class<?> c = obj.getClass();
			Field[] fields = c.getFields();
			for (final Field field : fields) {
				Annotation[] anns = field.getAnnotations();
				if (field.isAnnotationPresent(Debug.class)) {
					this.add(field.getAnnotation(Debug.class).name());
					Label l = new Label(InfoPanel.parseValueAsString(obj, field), skin);
					this.add(l);
					this.debugFields.put(field, l);
					if (field.getAnnotation(Debug.class).isModifiable()) {
						final TextField tmp = new TextField("", skin);
						this.add(tmp);

						tmp.addListener(new InputListener() {
							@Override
							public boolean keyUp(InputEvent event, int keycode) {
								if(keycode == Keys.ENTER ) {
									try {
										field.setFloat(obj, Float.valueOf(tmp.getText()));
									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IllegalArgumentException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IllegalAccessException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									return true;

								}
								return false;
							}

						});
					}
					this.row();
				}
			}
		}
	}

	public void update() {
		for (Field f : this.debugFields.keys()) {
			this.debugFields.get(f).setText(parseValueAsString(this.displayedObject, f));
		}
	}

	static DecimalFormat df = new DecimalFormat("###.#");

	static private String parseValueAsString(Object c, Field field) {
		String tmp = "";
		String type = field.getType().getName();
		try {
			if (type.equals("float")) {
				tmp = String.valueOf(field.getFloat(c));
			} else if (type.equals("int")) {
				tmp = String.valueOf(field.getInt(c));
			} else if (type.equals("boolean")) {
				tmp = String.valueOf(field.getBoolean(c));
			} else if (type.equals("com.badlogic.gdx.math.Vector3")) {
				Class<?> class1 = field.getType();
				Field field2 = class1.getField("x");
				Float x = field2.getFloat(field.get(c));
				float y = class1.getField("y").getFloat(field.get(c));
				float z = class1.getField("z").getFloat(field.get(c));
				tmp = df.format(x) + ", " + df.format(y) + " ," + df.format(z);
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tmp;

	}
}