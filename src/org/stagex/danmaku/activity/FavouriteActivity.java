package org.stagex.danmaku.activity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keke.player.R;
import org.stagex.danmaku.adapter.ChannelAdapter;
import org.stagex.danmaku.adapter.ChannelInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import cn.waps.AppConnect;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.nmbb.oplayer.scanner.ChannelListBusiness;
import com.nmbb.oplayer.scanner.DbHelper;
import com.nmbb.oplayer.scanner.POChannelList;
import com.nmbb.oplayer.scanner.SQLiteHelperOrm;

public class FavouriteActivity extends OrmLiteBaseActivity<SQLiteHelperOrm> {

	private static final String LOGTAG = "FavouriteActivity";
	private ListView fav_list;

	private List<POChannelList> fav_infos = null;

	/* 频道收藏的数据库 */
	private DbHelper<POChannelList> mDbHelper;
	private Map<String, Object> mDbWhere = new HashMap<String, Object>(2);

	/* 顶部标题栏的控件 */
	private TextView button_back;
	private ImageView button_home;
	private ImageView button_delete;

	// 更新收藏频道的数目
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private int fav_num = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.favourite);

		/* 获得收藏类别的list */
		fav_list = (ListView) findViewById(R.id.fav_list);
		// 防止滑动黑屏
		fav_list.setCacheColorHint(Color.TRANSPARENT);

		/* 频道收藏的数据库 */
		mDbHelper = new DbHelper<POChannelList>();

		/* 顶部标题栏的控件 */
		button_back = (TextView) findViewById(R.id.back_btn);
		button_home = (ImageView) findViewById(R.id.home_btn);
		button_delete = (ImageView) findViewById(R.id.delete_btn);
		/* 设置监听 */
		setListensers();

		// 更新收藏频道的数目
		sharedPreferences = getSharedPreferences("keke_player", MODE_PRIVATE);
		editor = sharedPreferences.edit();

		// ===============================================================
		if (sharedPreferences.getBoolean("no_fav_help", false) == false) {
			new AlertDialog.Builder(FavouriteActivity.this)
			.setIcon(R.drawable.ic_dialog_alert)
			.setTitle("温馨提示")
			.setMessage(
					"【长按】直播电视频道列表的频道名称即可以实现收藏，并且在这里可以看到收藏的频道！\n" +
					"在此的收藏频道【长按】可以实现取消收藏功能！\n同时，播放节目点击【心型】按钮也可以收藏")
			.setPositiveButton("不再提醒",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							// 不再收藏
							editor.putBoolean("no_fav_help", true);
							editor.commit();
						}
					})
			.setNegativeButton("知道了",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							dialog.cancel();
						}
					}).show();
		}
		// ===============================================================
		
		setFavView();
	}

	/*
	 * 设置其他未分类台源的channel list
	 */
	private void setFavView() {
		fav_infos = ChannelListBusiness.getAllFavChannels();
		ChannelAdapter adapter = new ChannelAdapter(this, fav_infos);
		fav_list.setAdapter(adapter);
		fav_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				POChannelList info = (POChannelList) fav_list
						.getItemAtPosition(arg2);
				// Log.d("ChannelInfo",
				// "name = " + info.getName() + "[" + info.getUrl() + "]");

				// startLiveMedia(info.getUrl(), info.getName());
				showAllSource(info.getAllUrl(), info.name, info.program_path);
			}
		});

		// 增加长按频道收藏功能
		fav_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				POChannelList info = (POChannelList) fav_list
						.getItemAtPosition(arg2);
				ClearFavMsg(arg1, info);
				return true;
			}
		});

		fav_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 显示所有的台源
	 */
	private void showAllSource(ArrayList<String> all_url, String name,
			String path) {
		Intent intent = new Intent(FavouriteActivity.this,
				ChannelSourceActivity.class);
		intent.putExtra("all_url", all_url);
		intent.putExtra("channel_name", name);
		intent.putExtra("program_path", path);
		startActivity(intent);
	}

	/**
	 * 提示是否取消收藏
	 */
	private void ClearFavMsg(View view, POChannelList info) {
		final POChannelList saveInfo = info;

		fav_num = sharedPreferences.getInt("fav_num", 0);
		Log.d(LOGTAG, "===>current fav_num = " + fav_num);

		new AlertDialog.Builder(FavouriteActivity.this)
				.setIcon(R.drawable.ic_dialog_alert).setTitle("温馨提示")
				.setMessage("确定取消该直播频道的收藏吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 收藏数目减1
						editor.putInt("fav_num", fav_num - 1);
						editor.commit();
						// TODO 增加加入数据库操作
						clearDatabase(saveInfo);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

	/**
	 * 提示是否取消所有收藏
	 */
	private void clearAllFavMsg() {
		new AlertDialog.Builder(FavouriteActivity.this)
				.setIcon(R.drawable.ic_dialog_alert)
				.setTitle("警告")
				.setMessage("确定取消所有收藏频道吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 取消所有收藏频道
						List<POChannelList> allFavChannel = ChannelListBusiness
								.getAllFavChannels();
						int size = allFavChannel.size();
						POChannelList favChannel;
						for (int i = 0; i < size; i++) {
							favChannel = allFavChannel.get(i);
							favChannel.save = false;
							mDbHelper.update(favChannel);
						}
						// 重新加載listView
						reloadFavList();

						// 收藏数目置为0
						editor.putInt("fav_num", 0);
						editor.commit();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

	/**
	 * 取消收藏
	 * 
	 * @throws FileNotFoundException
	 */
	private void clearDatabase(POChannelList channelList) {
		channelList.save = false;
		// update
		Log.i(LOGTAG, "==============>" + channelList.name + "###"
				+ channelList.poId + "###" + channelList.save);
		mDbHelper.update(channelList);

		// 重新加載listView
		reloadFavList();
	}

	// 重新加載listView
	private void reloadFavList() {
		fav_infos.clear();
		setFavView();
	}

	// Listen for button clicks
	private void setListensers() {
		button_home.setOnClickListener(goListener);
		button_delete.setOnClickListener(goListener);
		button_back.setOnClickListener(goListener);
	}

	// 按键监听
	private Button.OnClickListener goListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				// 回到上一个界面(Activity)
				finish();
				break;
			case R.id.home_btn:
				// 回到上一个界面(Activity)
				finish();
				break;
			case R.id.delete_btn:
				// 删除所有的收藏的频道
				clearAllFavMsg();
				break;
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};
}
