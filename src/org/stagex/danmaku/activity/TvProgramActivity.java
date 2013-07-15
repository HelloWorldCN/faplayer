package org.stagex.danmaku.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.keke.player.R;
import org.stagex.danmaku.adapter.ChannelSourceAdapter;
import org.stagex.danmaku.adapter.ProgramAdapter;
import org.stagex.danmaku.adapter.ProgramInfo;

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
import android.widget.ListView;
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
	
	private TextView test_txt;
	private ListView program_list;
	
	private ProgramAdapter mProgramAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv_program);

		/* 顶部标题栏的控件 */
		button_back = (TextView) findViewById(R.id.back_btn);
		mWebView = (WebView) findViewById(R.id.wv);
		test_txt =  (TextView) findViewById(R.id.test_txt);
		program_list = (ListView)findViewById(R.id.program_list);
		// 防止滑动黑屏
		program_list.setCacheColorHint(Color.TRANSPARENT);
		/* 设置监听 */
		setListensers();

		Intent intent = getIntent();
		mProgramPath = intent.getStringExtra("ProgramPath");
		mChannelName = intent.getStringExtra("ChannelName");

		button_back.setText("节目预告");
		
		/* ====================================================== */
		/* 用webview方式显示节目预告 */
//		readHtmlFormAssets();
		/* ====================================================== */
		/* TODO 以listView文本方式显示节目预告 */
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.tvmao.com/ext/show_tv.jsp?p=" + mProgramPath).get();
			
			Elements links = doc.select("li"); //带有href属性的a元素

			ArrayList<ProgramInfo> infos = new ArrayList<ProgramInfo>();
			
			int size = links.size();
			for (int i = 0; i < size; i++) {
				String linkText = links.get(i).text();
				String[] pair = linkText.split(" ");
				if (pair.length < 2)
					continue;
				String time = pair[0].trim();
				String program = pair[1].trim();
				
				ProgramInfo info = new ProgramInfo(time, program);
				infos.add(info);

//				test_txt.setText(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
				test_txt.setText(mChannelName);
			}
			
			mProgramAdapter = new ProgramAdapter(this, infos);
			program_list.setAdapter(mProgramAdapter);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test_txt.setText("抱歉，暂时无法获取该频道的节目预告！");
		}
		/* ====================================================== */
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
