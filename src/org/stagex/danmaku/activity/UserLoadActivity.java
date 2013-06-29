package org.stagex.danmaku.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.keke.player.R;
import org.stagex.danmaku.adapter.ChannelInfo;
import org.stagex.danmaku.adapter.ChannelLoadAdapter;
import org.stagex.danmaku.adapter.ChannelSourceAdapter;
import org.stagex.danmaku.util.ParseUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UserLoadActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String LOGTAG = "UserLoadActivity";

	/* 顶部标题栏的控件 */
	private ImageView button_home;
	private TextView button_back;
	private ImageView button_search;
	/* ListView */
	private ListView mTvList;
	private ChannelLoadAdapter mSourceAdapter;
	private List<ChannelInfo> infos;

	private WebView mWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_load);

		/* 顶部标题栏的控件 */
		button_home = (ImageView) findViewById(R.id.home_btn);
		button_back = (TextView) findViewById(R.id.back_btn);
		button_search = (ImageView) findViewById(R.id.search_btn);

		mWebView = (WebView) findViewById(R.id.wv);

		/* 设置监听 */
		setListensers();

		mTvList = (ListView) findViewById(R.id.tv_list);
		// 防止滑动黑屏
		mTvList.setCacheColorHint(Color.TRANSPARENT);

		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/kekePlayer/tvlist.txt";
		File listFile = new File(path);
		if (listFile.exists()) {
			mTvList.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.GONE);
			// 解析本地的自定义列表
			infos = ParseUtil.parseDef(this, path);

			mSourceAdapter = new ChannelLoadAdapter(this, infos);
			mTvList.setAdapter(mSourceAdapter);
			// 设置监听事件
			mTvList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					ChannelInfo info = (ChannelInfo) mTvList
							.getItemAtPosition(arg2);

					startLiveMedia(info.getUrl(), info.getName());
				}
			});
		} else
			readHtmlFormAssets();
	}

	// Listen for button clicks
	private void setListensers() {
		button_home.setOnClickListener(goListener);
		button_back.setOnClickListener(goListener);
		button_search.setOnClickListener(goListener);
	}

	// 打开网络媒体
	private void startLiveMedia(String liveUrl, String name) {
		Intent intent = new Intent(UserLoadActivity.this, PlayerActivity.class);
		ArrayList<String> playlist = new ArrayList<String>();
		playlist.add(liveUrl);
		intent.putExtra("selected", 0);
		intent.putExtra("playlist", playlist);
		intent.putExtra("title", name);
		startActivity(intent);
	}

	// 按键监听
	private Button.OnClickListener goListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.home_btn:
				// 退回主界面(homeActivity)
				finish();
				break;
			case R.id.back_btn:
				// 回到上一个界面(Activity)
				finish();
				break;
			case R.id.search_btn:
				showHelp();
				break;
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};

	// 显示帮助对话框
	private void showHelp() {
		readHtmlFormAssets();
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
		mWebView.loadUrl("file:///android_asset/html/tvList_help.html");
	}
}
