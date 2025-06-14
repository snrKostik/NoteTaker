package com.example.notetaker_java.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME         = "note_taker.db";
	private static final int    DB_VERSION      = 1; // Конечно же я не забуду менять версию :clueless:
	private static final String TABLE_NAME      = "notes";
	private static final String COL_ID          = "id";
	private static final String COL_TITLE       = "title";
	private static final String COL_CONTENT     = "content";
	private static final String COL_ATTACHMENTS = "attachments";
	private static final String COL_TIMESTAMP   = "timestamp";

	private static final String CREATE_TABLE = String.format(
					"CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," + //               CREATE TABLE {TABLE_NAME} ({COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
					"%s TEXT NOT NULL," + //                                                    {COL_TITLE} TEXT,
					"%s TEXT," + //                                                             {COL_CONTENT} TEXT,
					"%s TEXT" + //                                                              {COL_ATTACHMENTS} TEXT,
					"%s DATETIME DEFAULT CURRENT_TIMESTAMP)", TABLE_NAME, COL_ID, COL_TITLE,//  {COL_TimeStamp} TEXT)
			COL_CONTENT, COL_ATTACHMENTS, COL_TIMESTAMP);

	public DBHelper(@Nullable Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean InsertNote(String title, String content) {
		SQLiteDatabase db     = this.getWritableDatabase();
		ContentValues  values = new ContentValues();
		values.put(COL_TITLE, title);
		values.put(COL_CONTENT, content);
		long result = db.insert(TABLE_NAME, null, values);
		db.close();
		return result != -1;
	}

	public Cursor getAllNotes() {
		SQLiteDatabase db = this.getReadableDatabase();
		//Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " ORDER BY " + COL_TIMESTAMP + " DESC", null);
		//return res;
		return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_TIMESTAMP + " DESC", null);

	}

}
