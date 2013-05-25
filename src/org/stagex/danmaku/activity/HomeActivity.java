package org.stagex.danmaku.activity;

import org.stagex.danmaku.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import cn.waps.AppConnect;

import com.nmbb.oplayer.ui.MainActivity;

public class HomeActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		//启动广告
		AppConnect.getInstance(this); 
		
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
		// intent.setClass(HomeActivity.this, FileBrowserActivity.class);
		// 启动新的媒体扫描的activity
		intent.setClass(HomeActivity.this, MainActivity.class);
		startActivity(intent);
	};

	private void startLiveMedia() {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, ChannelTabActivity.class);
		startActivity(intent);
	};

	/**
	 * 在主界面按下返回键，提示用户是否退出应用
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_decode)
					.setTitle(R.string.prompt)
					.setMessage(R.string.quit_desc)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//关闭广告
		AppConnect.getInstance(this).finalize(); 
//		System.exit(0);
		// 或者下面这种方式
		 android.os.Process.killProcess(android.os.Process.myPid());
	}

}
