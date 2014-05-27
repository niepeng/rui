package com.rui.android_client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rui.android_client.db.metadata.DownloadInfoTable;
import com.rui.android_client.persist.DownloadInfoDao;

public class Persist {

	private Context mContext;
	private SQLiteDatabase db;

	public DownloadInfoDao downloadInfoDao;

	public Persist(Context context) {
		mContext = context;
		SQLiteDatabase db = db();
		downloadInfoDao = new DownloadInfoDao(db);
	}

	public SQLiteDatabase db() {
		if (db == null) {
			DBHelper helper = new DBHelper(mContext);
			db = helper.getWritableDatabase();
		}
		return db;
	}

	public void clear() {
		db().delete(DownloadInfoTable.T_NAME, null, null);
	}
}
