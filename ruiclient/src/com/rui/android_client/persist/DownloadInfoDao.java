package com.rui.android_client.persist;

import static android.provider.BaseColumns._ID;
import static com.rui.android_client.db.metadata.DBaseColumns.CREATED;
import static com.rui.android_client.db.metadata.DBaseColumns.UPDATED;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.COMPLETE_SIZE;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.END_POS;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.START_POS;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.THREAD_ID;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.URL;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rui.android_client.db.metadata.DownloadInfoTable;
import com.rui.android_client.model.DownloadInfo;

public class DownloadInfoDao extends BaseDao<DownloadInfo> {

	public DownloadInfoDao(SQLiteDatabase db) {
		super(DownloadInfoTable.T_NAME, db);
	}

	/**
	 * 保存 下载的具体信息
	 */
	public void saveInfos(List<DownloadInfo> infos) {
		for (DownloadInfo info : infos) {
			create(info, false);
		}
	}

	/**
	 * 更新数据库中的下载信息
	 */
	public void updataInfos(int threadId, int compeleteSize, String urlstr) {
		ContentValues values = new ContentValues();
		values.put(THREAD_ID, threadId);
		values.put(COMPLETE_SIZE, compeleteSize);
		String updateClause = URL + "=" + urlstr;
		db.update(tableName, values, updateClause, null);
	}

	/**
	 * 下载完成后删除数据库中的数据
	 */
	public void delete(String url) {
		String whereClause = URL + "=?";
		db.delete(tableName, whereClause, new String[] { url });
	}

	/**
	 * 查看数据库中是否有数据
	 */
	public boolean isHasInfors(String url) {
		String sql = URL + "=" + url;
		int count = count(sql);
		return count == 0;
	}

	/**
	 * 得到下载具体信息
	 */
	public List<DownloadInfo> getInfos(String urlstr) {
		String clause = URL + "=" + urlstr;
		return findList(clause);
	}

	@Override
	protected DownloadInfo cursorToObject(Cursor c) {
		DownloadInfo item = new DownloadInfo();
		item.setId(c.getLong(c.getColumnIndex(_ID)));
		item.setCreated(c.getLong(c.getColumnIndex(CREATED)));
		item.setUpdated(c.getLong(c.getColumnIndex(UPDATED)));
		item.setUrl(c.getString(c.getColumnIndex(URL)));
		item.setThreadId(c.getInt(c.getColumnIndex(THREAD_ID)));
		item.setStartPos(c.getInt(c.getColumnIndex(START_POS)));
		item.setEndPos(c.getInt(c.getColumnIndex(END_POS)));
		item.setCompeleteSize(c.getInt(c.getColumnIndex(COMPLETE_SIZE)));
		return item;
	}

	@Override
	protected ContentValues objectToContentValues(DownloadInfo t,
			boolean isDownload) {
		ContentValues values = new ContentValues();
		values.put(CREATED, t.getCreated());
		values.put(UPDATED, t.getUpdated());
		values.put(URL, t.getUrl());
		values.put(THREAD_ID, t.getThreadId());
		values.put(START_POS, t.getStartPos());
		values.put(END_POS, t.getEndPos());
		values.put(COMPLETE_SIZE, t.getCompeleteSize());
		return values;
	}

	@Override
	protected String[] colunms() {
		return new String[] { _ID, CREATED, UPDATED, URL, THREAD_ID, START_POS,
				END_POS, COMPLETE_SIZE };
	}

	@Override
	protected String order() {
		return asc(CREATED);
	}

}
