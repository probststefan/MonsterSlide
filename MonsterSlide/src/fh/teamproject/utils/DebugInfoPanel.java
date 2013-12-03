package fh.teamproject.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class DebugInfoPanel {

	Stage stage;
	Table table;
	Skin debugSkin = new Skin();
	Object displayedObject;
	Label l;
	DecimalFormat df = new DecimalFormat("###.##");

	public DebugInfoPanel() {
		this.stage = new Stage();
		this.table = new Table(this.debugSkin);
		this.table.setFillParent(true);
		this.table.top().right();
		this.stage.addActor(this.table);
		this.setupSkin();
	}

	private void setupSkin() {
		BitmapFont font = new BitmapFont();

		LabelStyle labels = new LabelStyle();
		labels.font = font;
		labels.fontColor = Color.WHITE;

		this.debugSkin.add("default", labels);
	}

	public void render() {
		this.stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.stage.act();

		this.table.clear();
		if (this.displayedObject != null) {
			Class<?> c = this.displayedObject.getClass();
			Field[] fields = c.getFields();
			for (Field field : fields) {
				Annotation[] anns = field.getAnnotations();
				if (field.isAnnotationPresent(Debug.class)) {
					this.table.add(field.getAnnotation(Debug.class).value());
					this.table.add(this.parseValueAsString(this.displayedObject, field));
					this.table.row();
				}
			}
		}
		this.stage.draw();
	}

	public void showInfo(Object obj) {
		this.displayedObject = obj;
	}

	private String parseValueAsString(Object c, Field field) {
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
				tmp = this.df.format(x) + ", " + this.df.format(y) + " ,"
						+ this.df.format(z);
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
