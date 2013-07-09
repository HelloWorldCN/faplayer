package org.stagex.danmaku.activity;

import org.keke.player.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class TvProgramActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String LOGTAG = "TvProgramActivity";

	/* 顶部标题栏的控件 */
	private TextView button_back;
	/* 需要显示的文本信息 */
	private WebView mWebView;
	private String mProgramPath;
	private String mChannelName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv_program);

		/* 顶部标题栏的控件 */
		button_back = (TextView) findViewById(R.id.back_btn);
		mWebView = (WebView) findViewById(R.id.wv);
		/* 设置监听 */
		setListensers();

		Intent intent = getIntent();
		mProgramPath = intent.getStringExtra("ProgramPath");
		mChannelName = intent.getStringExtra("ChannelName");

		button_back.setText(mChannelName);
		readHtmlFormAssets();
	}

	// Listen for button clicks
	private void setListensers() {
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
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};

	// 利用webview来显示帮助的文本信息
	private void readHtmlFormAssets() {
		WebSettings webSettings = mWebView.getSettings();

		// 充满全屏
		webSettings.setLoadWithOverviewMode(true);
		// WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
		// webSettings.setUseWideViewPort(true);
		// 设置WebView可触摸放大缩小：
		webSettings.setBuiltInZoomControls(true);
		// WebView 背景透明效果
		mWebView.setBackgroundColor(Color.TRANSPARENT);

		// webview中的新链接仍在当前browser中响应
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl("http://www.tvmao.com/ext/show_tv.jsp?p="
				+ mProgramPath);
	}

	// 首先响应webview的返回事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
