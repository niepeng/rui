package com.rui.android_client.persist;

import static android.provider.BaseColumns._ID;
import static com.rui.android_client.db.metadata.DBaseColumns.CREATED;
import static com.rui.android_client.db.metadata.DBaseColumns.UPDATED;
import static com.rui.android_client.db.metadata.DownloadInfoTable.Columns.*;
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
		String updateClause = URL + "=?";
		db.update(tableName, values, updateClause, new String[] { urlstr });
	}

	/**
	 * 下载完成后删除数据库中的数据
	 */
	public void delete(String url) {
		String whereClause = URL + "=?";
		db.delete(tableName, whereClause, new String[] { url });
	}
	
	public void deleteByPackageName(String packageName) {
		String whereClause = PACKAGE_NAME + "=?";
		db.delete(tableName, whereClause, new String[] { packageName });
	}

	/**
	 * 查看数据库中是否有数据
	 */
	public boolean isHasInfors(String url) {
		String sql = URL + "=?";
		int count = count(sql, new String[] { url });
		return count == 0;
	}

	/**
	 * 得到下载具体信息
	 */
	public List<DownloadInfo> getInfos(String url) {
		String clause = URL + "=?";
		return findList(clause, new String[] { url });
	}
	
	public List<DownloadInfo> getInfosByPackage(String packageName) {
		String clause = PACKAGE_NAME + "=?";
		return findList(clause, new String[] { packageName });
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
		item.setPackageName(c.getString(c.getColumnIndex(PACKAGE_NAME)));
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
		values.put(PACKAGE_NAME, t.getPackageName());
		return values;
	}

	@Override
	protected String[] colunms() {
		return new String[] { _ID, CREATED, UPDATED, URL, THREAD_ID, START_POS,
				END_POS, COMPLETE_SIZE, PACKAGE_NAME };
	}

	@Override
	protected String order() {
		return asc(CREATED);
	}

}
