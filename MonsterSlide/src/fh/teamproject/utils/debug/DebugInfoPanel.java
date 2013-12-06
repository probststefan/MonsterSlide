package fh.teamproject.utils.debug;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;

import fh.teamproject.utils.SkinManager;

public class DebugInfoPanel {

	public static Stage stage;
	Skin debugSkin = SkinManager.skin;
	InfoPanel panel;
	public Table root;

	public DebugInfoPanel() {
		DebugInfoPanel.stage = new Stage();
		this.root = new Table();
		this.root.setFillParent(true);
		this.root.top().right();
		this.root.setVisible(false);
		DebugInfoPanel.stage.addActor(this.root);
	}


	public void render() {
		DebugInfoPanel.stage.setViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		DebugInfoPanel.stage.act();
		this.panel.update();
		DebugInfoPanel.stage.draw();
	}

	public void showInfo(Object obj) {
		this.panel = null;
		this.root.clear();
		this.panel = new InfoPanel(obj, this.debugSkin);
		this.panel.setBackground("debug");
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
		this.add(obj.toString(), "debug").colspan(3).align(Align.center).prefWidth(300);
		this.row();
		this.add("Field", "debug");
		this.add("Value", "debug");
		this.add("Set to", "debug");
		this.row();
		if (obj != null) {
			Class<?> c = obj.getClass();
			Field[] fields = c.getFields();
			for (final Field field : fields) {
				if (field.isAnnotationPresent(Debug.class)) {
					this.add(field.getAnnotation(Debug.class).name(), "debug");
					Label l = new Label(InfoPanel.parseValueAsString(obj, field), skin,
							"debug");
					this.add(l);
					this.debugFields.put(field, l);
					if (field.getAnnotation(Debug.class).isModifiable()) {
						final TextField tmp = new TextField("", skin, "debug");
						this.add(tmp);

						tmp.addListener(new InputListener() {
							@Override
							public boolean keyUp(InputEvent event, int keycode) {
								if (keycode == Keys.ENTER) {
									try {
										field.setFloat(obj, Float.valueOf(tmp.getText()));
										tmp.setText("");
										DebugInfoPanel.stage.setKeyboardFocus(null);
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
				tmp = String.valueOf(df.format(field.getFloat(c)));
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