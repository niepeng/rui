package com.rui.android_client.db.metadata;

import static com.rui.android_client.db.metadata.DBaseColumns.CREATED;
import static com.rui.android_client.db.metadata.DBaseColumns.UPDATED;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public abstract class BaseTable {

	protected static final String SQL_INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
	public static final String SQL_BLOB = " BLOB, ";
	public static final String SQL_TEXT = " TEXT, ";
	public static final String SQL_INTEGER = " NUMERIC, ";
	public static final String SQL_INTEGER_NO_COMMA = " NUMERIC ";
	public static final String SQL_TEXT_NO_COMMA = " TEXT ";

	protected abstract String getTableName();

	protected void create(SQLiteDatabase db, StringBuilder columns) {
		StringBuilder sb = new StringBuilder();
		sb.append(CREATED).append(SQL_INTEGER);
		sb.append(UPDATED).append(SQL_INTEGER);
		sb.append(columns);
		createWithoutNormalColumns(db, sb);
	}

	protected void createWithoutNormalColumns(SQLiteDatabase db,
			StringBuilder columns) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(getTableName()).append(" (")
				.append(BaseColumns._ID).append(SQL_INTEGER_PRIMARY_KEY)
				.append(columns).append(");");
		db.execSQL(sb.toString());
		Log.d("RUI", sb.toString());
	}

}
