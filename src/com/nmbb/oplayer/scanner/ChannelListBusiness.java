package com.nmbb.oplayer.scanner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.stagex.danmaku.util.Logger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public final class ChannelListBusiness {
	private static final String TABLE_NAME = "channeLlist";
	private static final String TAG = "ChannelListBusiness";

	public static List<POChannelList> getAllSortFiles() {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao<POChannelList, Long> dao = db.getDao(POChannelList.class);
			QueryBuilder<POChannelList, Long> query = dao.queryBuilder();
			query.orderBy("save", true);
			return dao.query(query.prepare());
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return new ArrayList<POChannelList>();
	}
}
