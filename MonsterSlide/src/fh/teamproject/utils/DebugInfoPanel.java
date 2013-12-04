package fh.teamproject.utils;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fh.teamproject.MonsterSlide;

public class DebugInfoPanel {

	Stage stage;
	Table table;
	Object displayedObject;
	DecimalFormat df = new DecimalFormat("###.##");

	public DebugInfoPanel() {
		this.stage = new Stage();
		this.table = new Table(MonsterSlide.skinManager.skin);
		this.table.setFillParent(true);
		this.table.top().right();
		this.stage.addActor(this.table);
	}

	public void render() {
		this.stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.stage.act();

		this.table.clear();
		if (this.displayedObject != null) {
			Class<?> c = this.displayedObject.getClass();
			Field[] fields = c.getFields();
			for (Field field : fields) {

				this.table.add(field.getName());
				this.table.add(this.parseValueAsString(this.displayedObject, field));

				this.table.row();

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
