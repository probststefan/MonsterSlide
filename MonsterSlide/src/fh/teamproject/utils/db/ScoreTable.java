package fh.teamproject.utils.db;

import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;

public class ScoreTable extends DatabaseHandler {

	public static final String TABLE_NAME = "score";
	public static final String COLUMN_ID = "score_id";
	public static final String COLUMN_COMMENT = "comment";

	private static final String DATABASE_NAME = "score.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLE_NAME
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement, "
			+ "'score' INTEGER NOT NULL DEFAULT 0, 'coins' INTEGER NOT NULL DEFAULT 0, 'date_input' DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP);";

	public ScoreTable() {
		super(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE);
	}

	/**
	 * Erstellt einen neuen Eintrag in der Tabelle "Score" mit der gerutschten
	 * Strecke und der Anzahl an Coins.
	 * 
	 * @param distance
	 * @param coins
	 */
	public void insertPoins(int distance, int coins) {
		try {
			this.getDbHandler().execSQL(
					"INSERT INTO score ('score', 'coins') VALUES ('" + distance + "', '"
							+ coins + "')");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
	}

	public DatabaseCursor getLatestScore() {
		try {
			cursor = this.getDbHandler().rawQuery(
					"SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID
							+ " = (SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_NAME
							+ ")");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		return cursor;
	}

	/**
	 * Gibt die besten Scores zurueck.
	 * 
	 * @param limit
	 * @return DatabaseCursor
	 */
	public DatabaseCursor getScores(int limit) {
		try {
			cursor = this.getDbHandler()
					.rawQuery(
							"SELECT * FROM " + TABLE_NAME + " ORDER BY score DESC LIMIT "
									+ limit);
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		return cursor;
		// while (cursor.next()) {
		// Gdx.app.log("FromDb", String.valueOf(cursor.getString(1)));
		// }
	}
}