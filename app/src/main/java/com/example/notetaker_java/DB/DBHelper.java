package com.example.notetaker_java.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
	private static final String TAG = "DB HELPER";

	public static final String DB_NAME         = "note_taker.db";
	public static final int    DB_VERSION      = 3; // Конечно же я не забуду менять версию :clueless:
	public static final String TABLE_NAME      = "notes";
	public static final String COL_ID          = "id";
	public static final String COL_TITLE       = "title";
	public static final String COL_CONTENT     = "content";
	public static final String COL_ATTACHMENTS = "attachments";
	public static final String COL_TIMESTAMP   = "timestamp";
	public static final String COL_IS_REPEATED = "is_repeated";
	public static final String COL_QUALITY     = "quality";


	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT NOT NULL," + COL_CONTENT + " TEXT," +
//			COL_ATTACHMENTS + " TEXT," +
			COL_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

	public DBHelper(@Nullable Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_TABLE);
			Log.d(TAG, "Таблица " + TABLE_NAME + " успешно создана.");
		} catch (android.database.SQLException e) {
			Log.e(TAG, "Ошибка при создании таблицы " + TABLE_NAME + ": " + e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Нет времени мигрировать дб
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public boolean InsertNote(String title, String content) {
		SQLiteDatabase db     = this.getWritableDatabase();
		ContentValues  values = new ContentValues();
		values.put(COL_TITLE, title);
		values.put(COL_CONTENT, content);
//		values.put(COL_TIMESTAMP, timestamp);
		long result = db.insert(TABLE_NAME, null, values);
		db.close();
		return result != -1;
	}

	public Cursor getAllNotes() {
		SQLiteDatabase db     = this.getReadableDatabase();
		Cursor         cursor = null;
		try {
			cursor = db.query(TABLE_NAME, null, null, null, null, null, COL_TIMESTAMP + " DESC");
			return cursor;
		} finally {
//nothing here
		}
	}

	public Cursor getNotRepeatedNotes() {
		SQLiteDatabase db     = this.getReadableDatabase();
		Cursor         cursor = null;
		try {
			cursor = db.query(TABLE_NAME, null, null, null, null, null, COL_TIMESTAMP + " DESC");
			return cursor;
		} finally {
//nothing here
		}
	}

	public boolean UpdateNote(String id, String content) {
		SQLiteDatabase db     = this.getWritableDatabase();
		ContentValues  values = new ContentValues();

//		values.put(COL_TITLE, title);
		values.put(COL_CONTENT, content);
		values.put(COL_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

		long result = db.update(TABLE_NAME, values, COL_ID + "=?", new String[] {id});
		db.close();
		return result != -1;
	}

	public int DeleteNote(String id) {
		SQLiteDatabase db     = this.getWritableDatabase();
		int            result = db.delete(TABLE_NAME, COL_ID + "=?", new String[] {id});
		db.close();
		return result;
	}


	public static class Note {
		private int    id;
		private String title;
		private String content;
		private String timestamp;

		public Note(int id, String title, String content, String timestamp) {
			this.id = id;
			this.title = title;
			this.content = content;
			this.timestamp = timestamp;
		}

		public int getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getContent() {
			return content;
		}

		public String getTimestamp() {
			return timestamp;
		}


		// Старое дерьмо. Просььюа не обращать внимания
		@Override
		public String toString() {
			return "ID: " + id + "\n" + "Заголовок: " + title + "\n" + "Содержимое: " + content + "\n" + "Время: " + timestamp + "\n" + "--------------------";
		}
	}
}



