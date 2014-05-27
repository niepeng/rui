package com.rui.android_client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rui.android_client.db.metadata.DownloadInfoTable;

public class DBHelper extends SQLiteOpenHelper {

	private Context mContext;

	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "rui.db";

	DBHelper(final Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		new DownloadInfoTable().create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
