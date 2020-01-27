package com.example.notiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ll4
 * @date 1/27/2020
 */
public class NotizDataSource {

		private static final String LOG_TAG = NotizDataSource.class.getSimpleName();

		private SQLiteDatabase database;
		private NotizDatabaseHelper dbHelper;

		private String[] columns = {NotizDatabaseHelper.COLUMN_ID,
				NotizDatabaseHelper.COLUMN_NOTE1,
				NotizDatabaseHelper.COLUMN_NOTE2,
				NotizDatabaseHelper.COLUMN_CHECKED};

		public NotizDataSource(Context context) {
			Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
			dbHelper = new NotizDatabaseHelper(context);
		}

		public void open() {
			Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
			database = dbHelper.getWritableDatabase();
			Log.d(LOG_TAG, "Datenbankreferenz erhalten. Pfad zur Datenbank: " + database.getPath());
		}

		public void close() {
			dbHelper.close();
			Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
		}

		public Notiz createShoppingMemo(String note1, String note2) {
			ContentValues values = new ContentValues();
			values.put(NotizDatabaseHelper.COLUMN_NOTE1, note1);
			values.put(NotizDatabaseHelper.COLUMN_NOTE2, note2);

			long insertId = database.insert(NotizDatabaseHelper.TABLE_NOTIZ_LIST, null, values);

			Cursor cursor = database.query(NotizDatabaseHelper.TABLE_NOTIZ_LIST,
					columns, NotizDatabaseHelper.COLUMN_ID + " = " + insertId,
					null, null, null, null);

			cursor.moveToFirst();
			Notiz shoppingMemo = cursorToShoppingMemo(cursor);
			cursor.close();

			return  shoppingMemo;
		}

		public void deleteShoppingMemo(Notiz shoppingMemo) {
			long id = shoppingMemo.getId();
			database.delete(NotizDatabaseHelper.TABLE_NOTIZ_LIST,
					NotizDatabaseHelper.COLUMN_ID + " = " + id,
					null);
			Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + shoppingMemo.toString());
		}

		public Notiz updateShoppingMemo(long id, String newNote1, int newNote2, boolean newChecked) {

			int intValueChecked = (newChecked) ? 1 : 0;

			ContentValues values = new ContentValues();
			values.put(NotizDatabaseHelper.COLUMN_NOTE1, newNote1);
			values.put(NotizDatabaseHelper.COLUMN_NOTE2, newNote2);
			values.put(NotizDatabaseHelper.COLUMN_CHECKED, intValueChecked);

			database.update(NotizDatabaseHelper.TABLE_NOTIZ_LIST,
					values,
					NotizDatabaseHelper.COLUMN_ID + " = " + id,
					null);

			Cursor cursor = database.query(NotizDatabaseHelper.TABLE_NOTIZ_LIST,
					columns, NotizDatabaseHelper.COLUMN_ID + " = " + id,
					null, null, null, null);

			cursor.moveToFirst();
			Notiz shoppingMemo = cursorToShoppingMemo(cursor);
			cursor.close();


			return shoppingMemo;
		}

		private Notiz cursorToShoppingMemo(Cursor cursor) {
			int idIndex = cursor.getColumnIndex(NotizDatabaseHelper.COLUMN_ID);
			int idnote1 = cursor.getColumnIndex(NotizDatabaseHelper.COLUMN_NOTE1);
			int idnote2 = cursor.getColumnIndex(NotizDatabaseHelper.COLUMN_NOTE2);
			int idChecked = cursor.getColumnIndex(NotizDatabaseHelper.COLUMN_CHECKED);

			String note1 = cursor.getString(idnote1);
			String note2 = cursor.getString(idnote2);
			long id = cursor.getLong(idIndex);

			int intValueChecked = cursor.getInt(idChecked);

			boolean isChecked = (intValueChecked != 0);

			Notiz note = new Notiz(id, note1, note2, isChecked);

			return note;
		}

		public List<Notiz> getAllShoppingMemos() {
			List<Notiz> shoppingMemoList = new ArrayList<>();

			Cursor cursor = database.query(NotizDatabaseHelper.TABLE_NOTIZ_LIST,
					columns, null, null, null, null, null);

			cursor.moveToFirst();

			Notiz shoppingMemo;

			while (!cursor.isAfterLast()) {
				shoppingMemo = cursorToShoppingMemo(cursor);
				shoppingMemoList.add(shoppingMemo);
				Log.d(LOG_TAG, "ID: " + shoppingMemo.getId() + ", Inhalt: " + shoppingMemo.toString());
				cursor.moveToNext();
			}
			cursor.close();

			return shoppingMemoList;
		}
	}


