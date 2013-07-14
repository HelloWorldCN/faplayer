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

	private List<ChannelInfo> fav_infos = null;
	private List<POChannelList> listChannel = null;

	/* 频道收藏的数据库 */
	private DbHelper<POChannelList> mDbHelper;
	private Map<String, Object> mDbWhere = new HashMap<String, Object>(2);

	/* 顶部标题栏的控件 */
	private TextView button_back;
	private ImageView button_home;
	private ImageView button_delete;

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

		setFavView();
	}

	/*
	 * 设置其他未分类台源的channel list
	 */
	private void setFavView() {
		fav_infos = getFav();
		ChannelAdapter adapter = new ChannelAdapter(this, fav_infos);
		fav_list.setAdapter(adapter);
		fav_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ChannelInfo info = (ChannelInfo) fav_list
						.getItemAtPosition(arg2);
				// Log.d("ChannelInfo",
				// "name = " + info.getName() + "[" + info.getUrl() + "]");

				// startLiveMedia(info.getUrl(), info.getName());
				showAllSource(info.getAllUrl(), info.getName(),
						info.getProgram_path());
			}
		});

		// 增加长按频道收藏功能
		fav_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ChannelInfo info = (ChannelInfo) fav_list
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

	private List<ChannelInfo> getFav() {
		listChannel = ChannelListBusiness.getAllFavChannels();
		int size = listChannel.size();
		List<ChannelInfo> info = new ArrayList<ChannelInfo>();
		for (int i = 0; i < size; i++) {
			POChannelList channel = listChannel.get(i);
			info.add(channel.POCopyData());
			// just for test
			// example.setText(channel.toString());
		}
		return info;
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
	private void ClearFavMsg(View view, ChannelInfo info) {
		final ChannelInfo saveInfo = info;

		new AlertDialog.Builder(FavouriteActivity.this)
				.setIcon(R.drawable.ic_dialog_alert).setTitle("温馨提示")
				.setMessage("确定取消该直播频道的收藏吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing - it will close on its own
						// TODO 增加加入数据库操作
						clearDatabase(new POChannelList(saveInfo, false));
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
		mDbWhere.put("name", channelList.name);

		POChannelList newChannelList = mDbHelper.queryForEq(
				POChannelList.class, "name", channelList.name).get(0);
		channelList.poId = newChannelList.poId;

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
