package com.rui.android_client.persist;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDao<T> {

	protected String tableName;
	protected final SQLiteDatabase db;

	public BaseDao(String tableName, SQLiteDatabase db) {
		this.tableName = tableName;
		this.db = db;
	}

	public int count(String clause) {
		Cursor c = db.query(tableName, colunms(), clause, null, null, null, order());
		int numRows = c.getCount();
		c.close();
		return numRows;
	}

	public ArrayList<T> findList(String clause) {
		return findList(clause, null);
	}
	
	public ArrayList<T> findList(String clause, String[] values) {
		Cursor c = db.query(tableName, colunms(), clause, values, null, null, order());
		return cursorToList(c);
	}

	public T findOne(String clause) {
		return findOne(clause, null);
	}
	
	public T findOne(String clause, String[] values) {
		Cursor c = db.query(tableName, colunms(), clause, values, null, null, order());
		int numRows = c.getCount();
		if (numRows == 0) {
			c.close();
			return null;
		}
		c.moveToFirst();
		T t = cursorToObject(c);
		c.close();
		return t;
	}

	protected void create(T t, boolean isDownload) {
		ContentValues values = objectToContentValues(t, isDownload);
		db.insert(tableName, null, values);
		afterCreate(t, isDownload);
	}

	protected void afterCreate(T t, boolean isDownload) {
	}

	protected void update(T t, String updateClause, boolean isDownload) {
		ContentValues values = objectToContentValues(t, isDownload);
		db.update(tableName, values, updateClause, null);
		afterUpdate(t, isDownload);
	}

	protected void afterUpdate(T t, boolean isDownload) {
	}

	protected String desc(String field) {
		return field + " desc";
	}

	protected String asc(String field) {
		return field + " asc";
	}

	protected boolean intToBoolean(int value) {
		if (value <= 0) {
			return false;
		}
		return true;
	}

	private ArrayList<T> cursorToList(Cursor c) {
		int numRows = c.getCount();
		c.moveToFirst();
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < numRows; i++) {
			T t = cursorToObject(c);
			list.add(t);
			c.moveToNext();
		}
		c.close();
		return list;
	}

	protected abstract T cursorToObject(Cursor c);

	protected abstract ContentValues objectToContentValues(T t, boolean isDownload);

	protected abstract String[] colunms();

	protected abstract String order();

}
