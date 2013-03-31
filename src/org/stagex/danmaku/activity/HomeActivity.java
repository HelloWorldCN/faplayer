package org.stagex.danmaku.activity;

import org.stagex.danmaku.R;
import org.stagex.danmaku.adapter.ChannelAdapter;
import org.stagex.danmaku.util.ParseUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		findViews();
		setListensers();
	}

	private Button button_local;
	private Button button_live;

	private void findViews() {
		button_local = (Button) findViewById(R.id.local_media);
		button_live = (Button) findViewById(R.id.live_media);
	}

	// Listen for button clicks
	private void setListensers() {
		button_local.setOnClickListener(goListener);
		button_live.setOnClickListener(goListener);
	}

	private Button.OnClickListener goListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.local_media:
				startLocalMedia();
				break;
			case R.id.live_media:
				startLiveMedia();
				break;
			default:
				Log.d("HOME_TAG", "not supported btn id");
			}
		}
	};

	private void startLocalMedia() {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, FileBrowserActivity.class);
		startActivity(intent);
	};

	private void startLiveMedia() {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, ChannelListActivity.class);
		startActivity(intent);
	};

}
