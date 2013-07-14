package com.nmbb.oplayer.scanner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.stagex.danmaku.adapter.ChannelInfo;
import org.stagex.danmaku.util.Logger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public final class ChannelListBusiness {
	private static final String TABLE_NAME = "channeLlist";
	private static final String TAG = "ChannelListBusiness";

	// 找出所有的收藏频道
	public static List<POChannelList> getAllFavChannels() {
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
		// 此删除方法效率很低
		// try {
		// Dao<POChannelList, Long> dao = db.getDao(POChannelList.class);
		// List<POChannelList> allChannelList = dao.queryForAll();
		// int size = allChannelList.size();
		// for (int i = 0; i < size; i++) {
		// dao.delete(allChannelList.get(i));
		// }
		// } catch (SQLException e) {
		// Logger.e(e);
		// } finally {
		// FIXME 此种方式，效率很高，很快，直接删除所有行数据
		db.getWritableDatabase().delete("channeLlist", null, null);
		if (db != null)
			db.close();
		// }
	}

	// 获取所以模糊查询的频道
	public static List<POChannelList> getAllSearchChannels(String name) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao<POChannelList, Long> dao = db.getDao(POChannelList.class);
			QueryBuilder<POChannelList, Long> query = dao.queryBuilder();
			return query.where().like("name", "%" + name + "%").query();
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return new ArrayList<POChannelList>();
	}

	// 建立数据库所有数据
	public static void buildDatabase(List<ChannelInfo> channelList) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao<POChannelList, Long> dao = db.getDao(POChannelList.class);
			int size = channelList.size();
			for (int i = 0; i < size; i++) {
				dao.create(new POChannelList(channelList.get(i), false));
			}
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
	}
}
