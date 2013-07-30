package org.stagex.danmaku.activity;

import java.util.ArrayList;
import java.util.List;

import org.keke.player.R;
import org.stagex.danmaku.adapter.ChannelDefFavAdapter;

import com.nmbb.oplayer.scanner.ChannelListBusiness;
import com.nmbb.oplayer.scanner.DbHelper;
import com.nmbb.oplayer.scanner.POUserDefChannel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class UserDefFavActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String LOGTAG = "UserDefFavActivity";

	/* 顶部标题栏的控件 */
	private TextView button_back;
	private ImageView button_help;
	/* ListView */
	private ListView mTvList;
	private ChannelDefFavAdapter mSourceAdapter;
	private List<POUserDefChannel> infos;

	private WebView mWebView;
	
	/* 频道收藏的数据库 */
	private DbHelper<POUserDefChannel> mDbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_def_fav);

		/* 顶部标题栏的控件 */
		button_back = (TextView) findViewById(R.id.back_btn);
		button_help = (ImageView) findViewById(R.id.msg_btn);

		mWebView = (WebView) findViewById(R.id.wv);

		/* 设置监听 */
		setListensers();

		mTvList = (ListView) findViewById(R.id.tv_list);
		// 防止滑动黑屏
		mTvList.setCacheColorHint(Color.TRANSPARENT);

		/* 频道收藏的数据库 */
		mDbHelper = new DbHelper<POUserDefChannel>();
		
		showList();
	}

	private void showList() {
		/* 获取所有的自定义收藏频道 */
		infos = ChannelListBusiness.getAllDefFavChannels();

		mSourceAdapter = new ChannelDefFavAdapter(this, infos);
		mTvList.setAdapter(mSourceAdapter);
		// 设置监听事件
		mTvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				POUserDefChannel info = (POUserDefChannel) mTvList
						.getItemAtPosition(arg2);

				startLiveMedia(info.getAllUrl(), info.name);
			}
		});
		// 增加长按频道删除收藏的个性频道
		mTvList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				POUserDefChannel info = (POUserDefChannel) mTvList
						.getItemAtPosition(arg2);
				showFavMsg(arg1, info);
				return true;
			}
		});
	}

	// Listen for button clicks
	private void setListensers() {
		button_back.setOnClickListener(goListener);
		button_help.setOnClickListener(goListener);
	}

	// 打开网络媒体
	private void startLiveMedia(ArrayList<String> all_url, String name) {
		// 如果该节目只有一个候选源地址，那么直接进入播放界面
		if (all_url.size() == 1) {
			Intent intent = new Intent(UserDefFavActivity.this,
					PlayerActivity.class);
			ArrayList<String> playlist = new ArrayList<String>();
			playlist.add(all_url.get(0));
			intent.putExtra("selected", 0);
			intent.putExtra("playlist", playlist);
			intent.putExtra("title", name);
			startActivity(intent);
		} else {
			// 否则进入候选源界面
			Intent intent = new Intent(UserDefFavActivity.this,
					ChannelSourceActivity.class);
			intent.putExtra("all_url", all_url);
			intent.putExtra("channel_name", name);
			startActivity(intent);
		}
	}

	// 按键监听
	private Button.OnClickListener goListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				// 回到上一个界面(Activity)
				finish();
				break;
			case R.id.msg_btn:
				// TODO 显示帮助
				readHtmlFormAssets();
				break;
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};

	/**
	 * 提示是否删除收藏的个性频道
	 */
	private void showFavMsg(View view, POUserDefChannel info) {

		final POUserDefChannel saveInfo = info;

		new AlertDialog.Builder(UserDefFavActivity.this)
				.setIcon(R.drawable.ic_dialog_alert).setTitle("温馨提示")
				.setMessage("确定删除该自定义频道吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 从数据库中删除一条数据
						mDbHelper.remove(saveInfo);
						// 重新加载list
						if (infos != null) {
							infos.clear();
							showList();
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}
	
	// 利用webview来显示帮助的文本信息
	private void readHtmlFormAssets() {
		mTvList.setVisibility(View.GONE);
		mWebView.setVisibility(View.VISIBLE);
		WebSettings webSettings = mWebView.getSettings();

		webSettings.setLoadWithOverviewMode(true);
		// WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
		// webSettings.setUseWideViewPort(true);
		// 设置WebView可触摸放大缩小：
		// webSettings.setBuiltInZoomControls(true);
		// WebView 背景透明效果
		mWebView.setBackgroundColor(Color.TRANSPARENT);
		mWebView.loadUrl("file:///android_asset/html/SelfFavTVList_help.html");
	}
}
