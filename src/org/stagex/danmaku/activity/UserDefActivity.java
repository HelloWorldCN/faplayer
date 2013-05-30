package org.stagex.danmaku.activity;

import java.util.ArrayList;

import org.stagex.danmaku.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UserDefActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String LOGTAG = "UserDefActivity";
	EditText mTextUri = null;
	Button mButtonPlay = null;
	
	/* 顶部标题栏的控件 */
	private Button button_home;
	private Button button_back;
	/* 文本编辑框 */
	private Button button_clear;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_def);
		
		/* 顶部标题栏的控件 */
		button_home = (Button) findViewById(R.id.home_btn);
		button_back = (Button) findViewById(R.id.back_btn);
		/* 文本编辑框 */
		button_clear = (Button) findViewById(R.id.clear_play);
		mTextUri = (EditText) findViewById(R.id.test_uri);
		mButtonPlay = (Button) findViewById(R.id.test_play);
		
		/* 设置监听 */
		setListensers();
	}
	
	// Listen for button clicks
	private void setListensers() {
		button_home.setOnClickListener(goListener);
		button_back.setOnClickListener(goListener);
		button_clear.setOnClickListener(goListener);
		mButtonPlay.setOnClickListener(goListener);
	}
	
	//打开网络媒体
	private void startMedia() {
		String uri = mTextUri.getText().toString();
		//TODO 判断url的合法性
		if (uri.length() > 0) {
			Intent intent = new Intent(UserDefActivity.this,
					PlayerActivity.class);
			ArrayList<String> playlist = new ArrayList<String>();
			playlist.add(uri);
			intent.putExtra("selected", 0);
			intent.putExtra("playlist", playlist);
			startActivity(intent);
		}
	}

	//按键监听
	private Button.OnClickListener goListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.home_btn:
				//退回主界面(homeActivity)
				finish();
				Intent intent = new Intent(UserDefActivity.this,
						HomeActivity.class);
				startActivity(intent);
				break;
			case R.id.back_btn:
				//回到上一个界面(Activity)
				finish();
				break;
			case R.id.test_play:
				startMedia();
				break;
			case R.id.clear_play:
				//清除文本框内容
				mTextUri.setText("");
				break;
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};

}
