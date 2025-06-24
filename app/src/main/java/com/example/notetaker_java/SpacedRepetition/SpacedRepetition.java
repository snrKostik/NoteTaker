package com.example.notetaker_java.SpacedRepetition;


import static java.lang.Math.log;
import static java.lang.Math.pow;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.notetaker_java.DB.DBHelper;

import java.text.BreakIterator;

public class SpacedRepetition {
	private SQLiteDatabase db;
//			getReadableDatabase().query(DBHelper.TABLE_NAME, null, null, null, null, null, null);

	private double formula(double t, double k, double c){
		double b = 0;
		if (t >= 0) {
			try {
				b = (100 * k) / (pow(log(t), c) + k);
			} catch (Exception e) {
				Log.e("class SpacedRepetition", "Ошибка в методе formula " + e.getMessage());
			}
			return b;
		} else {
			return -1;
		}
	}
//	private float HardCoded(){
//		db = this.getReadableDatabase();
//		DBHelper db = new DBHelper();
//		if (repetition == 0){
//
//		}
//	}
}
