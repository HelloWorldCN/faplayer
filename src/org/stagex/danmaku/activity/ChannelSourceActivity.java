package org.stagex.danmaku.activity;

import java.util.ArrayList;

import org.keke.player.R;
import org.stagex.danmaku.adapter.ChannelInfo;
import org.stagex.danmaku.adapter.ChannelSourceAdapter;
import org.stagex.danmaku.util.SystemUtility;

import cn.waps.AdView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ChannelSourceActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String LOGTAG = "ChannelSourceActivity";
	private ListView mFileList;
	private ChannelSourceAdapter mSourceAdapter;
	private  ArrayList<String> infos;
	private String channel_name;
	
	/* 顶部标题栏的控件 */
	private Button button_home;
	private Button button_back;
	private Button button_refresh;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_source);
		
		/* 顶部标题栏的控件 */
		button_home = (Button) findViewById(R.id.home_btn);
		button_back = (Button) findViewById(R.id.back_btn);
		button_refresh = (Button) findViewById(R.id.refresh_btn);
		/* 设置监听 */
		setListensers();
		
//		mSourceAdapter = new ChannelSourceAdapter(this, infos);
		mFileList = (ListView) findViewById(R.id.channel_source);
		//防止滑动黑屏
		mFileList.setCacheColorHint(Color.TRANSPARENT);
		
		TextView text = (TextView) findViewById(R.id.channel_name);

		/* 广告栏控件 */
		LinearLayout container =(LinearLayout)findViewById(R.id.AdLinearLayout);
		new AdView(this,container).DisplayAd();
		
		//设置监听事件
		mFileList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String info = (String)mFileList
						.getItemAtPosition(arg2);

				startLiveMedia(info, channel_name + "    ===>[" + "地址" + Integer.toString(arg2 + 1) + "]");
			}
		});
//		mFileList.setOnItemLongClickListener(mSourceAdapter);
		
		Intent intent = getIntent();
		infos = intent.getStringArrayListExtra("all_url");
		if (infos == null)
			Log.e(LOGTAG, "infos is null");
		channel_name = intent.getStringExtra("channel_name");
		text.setText(channel_name);
		
		mSourceAdapter = new ChannelSourceAdapter(this, infos);
		mFileList.setAdapter(mSourceAdapter);
	}
	
	private void startLiveMedia(String liveUrl, String name) {
		Intent intent = new Intent(ChannelSourceActivity.this,
				PlayerActivity.class);
		ArrayList<String> playlist = new ArrayList<String>();
		playlist.add(liveUrl);
		intent.putExtra("selected", 0);
		intent.putExtra("playlist", playlist);
		intent.putExtra("title", name);
		startActivity(intent);
	}
	
	// Listen for button clicks
	private void setListensers() {
		button_home.setOnClickListener(goListener);
		button_back.setOnClickListener(goListener);
		button_refresh.setOnClickListener(goListener);
	}
	
	//按键监听
	private Button.OnClickListener goListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.home_btn:
				//退回主界面(homeActivity)
				finish();
				Intent intent = new Intent(ChannelSourceActivity.this,
						HomeActivity.class);
				startActivity(intent);
				break;
			case R.id.back_btn:
				//回到上一个界面(Activity)
				finish();
				break;
			case R.id.refresh_btn:
				//TODO 重新刷新电视界面列表
				break;
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};
}
