package org.stagex.danmaku.activity;

import java.util.ArrayList;

import org.stagex.danmaku.R;
import org.stagex.danmaku.adapter.ChannelInfo;
import org.stagex.danmaku.adapter.ChannelSourceAdapter;
import org.stagex.danmaku.util.SystemUtility;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_source);
//		mSourceAdapter = new ChannelSourceAdapter(this, infos);
		mFileList = (ListView) findViewById(R.id.channel_source);
		//防止滑动黑屏
		mFileList.setCacheColorHint(Color.TRANSPARENT);
		
		TextView text = (TextView) findViewById(R.id.channel_name);

		
		//设置监听事件
		mFileList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String info = (String)mFileList
						.getItemAtPosition(arg2);

				startLiveMedia(info, channel_name);
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
}
