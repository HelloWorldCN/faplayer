package org.stagex.danmaku.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keke.player.R;
import org.stagex.danmaku.adapter.ChannelAdapter;
import org.stagex.danmaku.adapter.ChannelInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.nmbb.oplayer.scanner.ChannelListBusiness;
import com.nmbb.oplayer.scanner.DbHelper;
import com.nmbb.oplayer.scanner.POChannelList;

public class SearchActivity extends Activity {

	private static final String LOGTAG = "SearchActivity";
	private ListView search_list = null;
	private EditText mSearch = null;

	private List<ChannelInfo> search_infos = null;
	private List<POChannelList> listChannel = null;

	/* 频道收藏的数据库 */
	private DbHelper<POChannelList> mDbHelper;
	private Map<String, Object> mDbWhere = new HashMap<String, Object>(2);

	/* 顶部标题栏的控件 */
	private TextView button_back;
	private ImageView button_delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search);

		/* 获得收藏类别的list */
		search_list = (ListView) findViewById(R.id.search_list);
		// 防止滑动黑屏
		search_list.setCacheColorHint(Color.TRANSPARENT);

		/* 频道收藏的数据库 */
		mDbHelper = new DbHelper<POChannelList>();

		/* 顶部标题栏的控件 */
		button_back = (TextView) findViewById(R.id.back_btn);
		button_delete = (ImageView) findViewById(R.id.delete_btn);
		/* 搜索栏 */
		mSearch = (EditText) findViewById(R.id.search_txt);

		mSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				setSearchView();
			}
		});

		/* 设置监听 */
		setListensers();
	}

	/*
	 * 设置其他未分类台源的channel list
	 */
	private void setSearchView() {
		// clear old listview
		if (search_infos != null) {
			search_infos.clear();
			listChannel.clear();
		}

		// get edit text
		String searchName = mSearch.getText().toString();
		if (searchName.length() > 0) {
			// get search channel list
			search_infos = getSearch(searchName);
			ChannelAdapter adapter = new ChannelAdapter(this, search_infos);
			search_list.setAdapter(adapter);
			search_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					ChannelInfo info = (ChannelInfo) search_list
							.getItemAtPosition(arg2);
					// Log.d("ChannelInfo",
					// "name = " + info.getName() + "[" + info.getUrl() + "]");

					// startLiveMedia(info.getUrl(), info.getName());
					showAllSource(info.getAllUrl(), info.getName(),
							info.getProgram_path());
				}
			});

			// 增加长按频道收藏功能
			search_list
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							ChannelInfo info = (ChannelInfo) search_list
									.getItemAtPosition(arg2);
							showFavMsg(arg1, info);
							return true;
						}
					});

			search_list.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	private List<ChannelInfo> getSearch(String name) {
		listChannel = ChannelListBusiness.getAllSearchChannels(name);
		int size = listChannel.size();

		Log.d(LOGTAG, "===>find [" + size + "] channels");

		List<ChannelInfo> info = new ArrayList<ChannelInfo>();
		for (int i = 0; i < size; i++) {
			POChannelList channel = listChannel.get(i);
			info.add(channel.POCopyData());
		}
		return info;
	}

	/**
	 * 显示所有的台源
	 */
	private void showAllSource(ArrayList<String> all_url, String name,
			String path) {
		Intent intent = new Intent(SearchActivity.this,
				ChannelSourceActivity.class);
		intent.putExtra("all_url", all_url);
		intent.putExtra("channel_name", name);
		intent.putExtra("program_path", path);
		startActivity(intent);
	}

	/**
	 * 提示是否收藏
	 */
	private void showFavMsg(View view, ChannelInfo info) {

		final ImageView favView = (ImageView) view.findViewById(R.id.fav_icon);
		final ChannelInfo saveInfo = info;

		new AlertDialog.Builder(SearchActivity.this)
				.setIcon(R.drawable.ic_dialog_alert).setTitle("温馨提示")
				.setMessage("确定收藏该直播频道吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing - it will close on its own
						// TODO 增加加入数据库操作
						favView.setVisibility(View.VISIBLE);
						updateDatabase(new POChannelList(saveInfo, true));
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
	 * 收藏后更新某一条数据信息
	 * 
	 */
	private void updateDatabase(POChannelList channelList) {

		List<POChannelList> newChannelList = mDbHelper.queryForEq(
				POChannelList.class, "name", channelList.name);

		if (newChannelList.size() > 0) {
			channelList.poId = newChannelList.get(0).poId;

			// update
			Log.i(LOGTAG, "==============>" + channelList.name + "###"
					+ channelList.poId + "###" + channelList.save);

			mDbHelper.update(channelList);
		}
	}

	// Listen for button clicks
	private void setListensers() {
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
			case R.id.delete_btn:
				// 删除所有的收藏的频道
				// clearAllFavMsg();
				break;
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};
}
