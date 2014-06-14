package com.rui.android_client.db.metadata;

import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.COMPLETE_SIZE;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.END_POS;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.PACKAGE_NAME;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.START_POS;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.THREAD_ID;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.URL;
import android.database.sqlite.SQLiteDatabase;

public class DownloadInfoTable extends BaseTable {

	public static final String T_NAME = "download_info";

	@Override
	protected String getTableName() {
		return T_NAME;
	}

	public static class Columns extends DBaseColumns {
		public static final String THREAD_ID = "thread_id";
		public static final String START_POS = "start_pos";
		public static final String END_POS = "end_pos";
		public static final String COMPLETE_SIZE = "complete_size";
		public static final String URL = "url";
		public static final String PACKAGE_NAME = "package_name";
	}

	public void create(SQLiteDatabase db) {
		StringBuilder columns = new StringBuilder();
		columns.append(THREAD_ID).append(SQL_INTEGER);
		columns.append(START_POS).append(SQL_INTEGER);
		columns.append(END_POS).append(SQL_INTEGER);
		columns.append(COMPLETE_SIZE).append(SQL_INTEGER);
		columns.append(PACKAGE_NAME).append(SQL_INTEGER);
		columns.append(URL).append(SQL_TEXT_NO_COMMA);
		create(db, columns);
	}

}
