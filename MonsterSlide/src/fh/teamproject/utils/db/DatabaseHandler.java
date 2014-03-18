package fh.teamproject.utils.db;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

public class DatabaseHandler {

	private Database dbHandler;
	public DatabaseCursor cursor = null;

	private String name, create;
	private int version;

	public DatabaseHandler(String name, int version, String create) {
		this.name = name;
		this.version = version;
		this.create = create;

		this.openDatabase();
	}

	public void openDatabase() {
		Gdx.app.log("DatabaseTest", "creation started");
		dbHandler = DatabaseFactory.getNewDatabase(name, version, create, null);

		dbHandler.setupDatabase();
		try {
			dbHandler.openOrCreateDatabase();
			dbHandler.execSQL(create);
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		Gdx.app.log("DatabaseTest", "created successfully");
	}

	public Database getDbHandler() {
		return this.dbHandler;
	}

	public void dispose() {
		try {
			dbHandler.closeDatabase();
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		dbHandler = null;
		cursor = null;
		Gdx.app.log("DatabaseTest", "dispose");
	}
}