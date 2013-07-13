package org.stagex.danmaku.activity;

import java.util.ArrayList;
import java.util.List;

import org.keke.player.R;
import org.stagex.danmaku.adapter.ChannelAdapter;
import org.stagex.danmaku.adapter.ChannelInfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.nmbb.oplayer.scanner.ChannelListBusiness;
import com.nmbb.oplayer.scanner.POChannelList;
import com.nmbb.oplayer.scanner.SQLiteHelperOrm;

public class FavouriteActivity extends OrmLiteBaseActivity<SQLiteHelperOrm> {

	private ListView fav_list;

	List<ChannelInfo> fav_infos = null;
	List<POChannelList> listChannel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.favourite);
		
		/* 获得收藏类别的list */
		fav_list = (ListView) findViewById(R.id.fav_list);
		// 防止滑动黑屏
		fav_list.setCacheColorHint(Color.TRANSPARENT);

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
//				showFavMsg(arg1, info);
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
		listChannel = ChannelListBusiness.getAllSortFiles();
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
}
