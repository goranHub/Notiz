package com.example.notiz;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author ll4
 * @date 1/27/2020
 */
public class NotizDatabaseHelper  extends SQLiteOpenHelper {

	private static final String LOG_TAG = NotizDatabaseHelper.class.getSimpleName();
	private static final String DB_NAME = "NotizMemoDbName.db";
	private static final int DB_VERSION = 1;


	public static final String TABLE_NOTIZ_LIST = "notizList";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NOTE1 = "note1";
	public static final String COLUMN_NOTE2 = "note2";
	public static final String COLUMN_CHECKED = "checked";



	public NotizDatabaseHelper( Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String SQL_CREATE =
				"CREATE TABLE " + TABLE_NOTIZ_LIST +
						"(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						COLUMN_NOTE1 + " TEXT NOT NULL, " +
						COLUMN_NOTE2 + " TEXT NOT NULL, " +
						COLUMN_CHECKED + " BOOLEAN NOT NULL DEFAULT 0);";
		try {
			Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
			db.execSQL(SQL_CREATE);
		} catch (SQLException e) {
			Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(LOG_TAG, "Die Tabelle mit Versionsnummer " + oldVersion + " wird entfernt.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIZ_LIST);
		onCreate(db);
	}
}
