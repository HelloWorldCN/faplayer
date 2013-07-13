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

	// 找出所有的收藏频道
	public static List<POChannelList> getAllFavFiles() {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao<POChannelList, Long> dao = db.getDao(POChannelList.class);
			return dao.queryForEq("save", true);
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return new ArrayList<POChannelList>();
	}

	// 清除所有的数据
	public static void clearAllOldDatabase() {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao<POChannelList, Long> dao = db.getDao(POChannelList.class);
			List<POChannelList> allChannelList = dao.queryForAll();
			int size = allChannelList.size();
			for (int i = 0; i < size; i++) {
				dao.delete(allChannelList.get(i));
			}
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
	}
}
