package org.stagex.danmaku.activity;

import org.keke.player.R;
import org.stagex.danmaku.util.SystemUtility;

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
	private static final String LOGTAG = "HomeActivity";

	private Button button_local;
	private Button button_live;
	private Button button_userdef;
	private Button button_about;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		//判断CPU类型，如果低于ARMV6，则不让其运行
		if (SystemUtility.getArmArchitecture() < 6) {
			new AlertDialog.Builder(HomeActivity.this)
		    .setTitle("警告")
//		    .setMessage("抱歉！软件解码库暂时不支持您的CPU，请到设置中选择硬解模式")
		    .setMessage("抱歉！软件解码库暂时不支持您的CPU")
		    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            //do nothing - it will close on its own
		        	android.os.Process.killProcess(android.os.Process.myPid());
		        }
		     })
		   .show();
		}
		
		//启动广告
		AppConnect.getInstance(this); 
		
		findViews();
		setListensers();
	}

	private void findViews() {
		button_local = (Button) findViewById(R.id.local_media);
		button_live = (Button) findViewById(R.id.live_media);
		button_userdef = (Button)findViewById(R.id.userdef_media);
		button_about = (Button)findViewById(R.id.about_media);
	}

	// Listen for button clicks
	private void setListensers() {
		button_local.setOnClickListener(goListener);
		button_live.setOnClickListener(goListener);
		button_userdef.setOnClickListener(goListener);
		button_about.setOnClickListener(goListener);
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
			case R.id.userdef_media:
				startUserdefMedia();
				break;
			case R.id.about_media:
				startAboutMedia();
				break;
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};

	/**
	 * 本地媒体界面
	 */
	private void startLocalMedia() {
		Intent intent = new Intent();
		// intent.setClass(HomeActivity.this, FileBrowserActivity.class);
		// 启动新的媒体扫描的activity
		intent.setClass(HomeActivity.this, MainActivity.class);
		startActivity(intent);
	};

	/**
	 * 直播电视视频界面
	 */
	private void startLiveMedia() {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, ChannelTabActivity.class);
		startActivity(intent);
	};
	
	/**
	 * 用户自定义网络视频播放界面
	 */
	private void startUserdefMedia() {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, UserDefActivity.class);
		startActivity(intent);
	};
	
	/**
	 * 程序关于界面
	 */
	private void startAboutMedia() {
		new AlertDialog.Builder(HomeActivity.this)
	    .setTitle("关于")
	    .setMessage("版本：可可电视v1.0.0\n作者：可可工作室\n企鹅：1956733072\n联系：keke_player@163.com\n许可：FFmpeg & VLC")
	    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            //do nothing - it will close on its own
	        }
	     })
	   .show();
	};

	/**
	 * 在主界面按下返回键，提示用户是否退出应用
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
//					.setIcon(R.drawable.ic_decode)
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
