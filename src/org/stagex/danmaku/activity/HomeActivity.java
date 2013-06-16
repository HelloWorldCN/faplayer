package org.stagex.danmaku.activity;

import java.io.File;

import org.keke.player.R;
import org.stagex.danmaku.util.Network;
import org.stagex.danmaku.util.SystemUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.waps.AppConnect;

import com.nmbb.oplayer.ui.MainActivity;

public class HomeActivity extends Activity {
	private static final String LOGTAG = "HomeActivity";

	private LinearLayout button_local;
	private LinearLayout button_live;
	private LinearLayout button_userdef;
	private LinearLayout button_setup;

	private SharedPreferences sharedPreferences;

	/* 旋转图标 */
	private Animation operatingAnim;
	private LinearInterpolator lin;
	private ImageView homeImage;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		/* 旋转图标 */
		operatingAnim = AnimationUtils.loadAnimation(this, R.anim.icon);
		lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		homeImage = (ImageView) findViewById(R.id.home_icon);
		// 开始转圈
		if (operatingAnim != null) {
			homeImage.startAnimation(operatingAnim);
		}

		sharedPreferences = getSharedPreferences("keke_player", MODE_PRIVATE);

		// 判断CPU类型，如果低于ARMV6，则不让其运行
		if (SystemUtility.getArmArchitecture() <= 6) {
			// 如果已经是硬解码模式，则无需设置
			boolean isHardDec = sharedPreferences
					.getBoolean("isHardDec", false);
			if (isHardDec) {
				// do nothing
			} else
				new AlertDialog.Builder(HomeActivity.this)
						.setIcon(R.drawable.ic_dialog_alert)
						.setTitle("警告")
						.setMessage(
								"抱歉！软件解码库暂时不支持您的CPU\n\n请到设置中选择【硬解码】模式，且只能使用硬解码")
						// .setMessage("抱歉！软件解码库暂时不支持您的CPU")
						.setPositiveButton("设置",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// do nothing - it will close on
										// its own
										Intent intent = new Intent();
										// 跳转至设置界面
										intent.setClass(HomeActivity.this,
												SetupActivity.class);
										startActivity(intent);
									}
								}).show();
		}

		Network network = new Network(this);
		// 判断是否打开了网络
		if (network.isOpenNetwork()) {
			// 如果连接的是移动网络，对用户作出警告
			if (network.isMobileNetwork())
				new AlertDialog.Builder(HomeActivity.this)
						.setIcon(R.drawable.ic_dialog_alert)
						.setTitle("警告")
						.setMessage(
								"您正在使用移动网络，由此产生的流量费用由运营商收取！\n\n是否切换至WIFI网络？")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// do nothing - it will close on its own
										Intent intent = null;
										try {
											intent = new Intent(
											// android.provider.Settings.ACTION_WIRELESS_SETTINGS);
											// 直接跳转到WIFI网络设置
													android.provider.Settings.ACTION_WIFI_SETTINGS);
											startActivity(intent);
										} catch (Exception e) {
											Log.w(LOGTAG,
													"open network settings failed, please check...");
											e.printStackTrace();
										}
									}
								})
						.setNegativeButton("否",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								}).show();
		} else {
			// 如果没有网络连接
			new AlertDialog.Builder(HomeActivity.this)
					.setIcon(R.drawable.ic_dialog_alert)
					.setTitle("没有可用的网络")
					.setMessage("推荐您只在WIFI模式下观看直播电视节目！\n\n是否对WIFI网络进行设置？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = null;
									try {
										intent = new Intent(
										// android.provider.Settings.ACTION_WIRELESS_SETTINGS);
										// 直接跳转到WIFI网络设置
												android.provider.Settings.ACTION_WIFI_SETTINGS);
										startActivity(intent);
									} catch (Exception e) {
										Log.w(LOGTAG,
												"open network settings failed, please check...");
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).show();
		}

		// 启动广告
		AppConnect.getInstance(this);

		// 创建应用程序工作目录
		File dir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/kekePlayer");
		if (dir.exists()) {
			/* do nothing */
		} else {
			dir.mkdirs();
		}

		findViews();
		setListensers();
	}

	private void findViews() {
		button_local = (LinearLayout) findViewById(R.id.go_local);
		button_live = (LinearLayout) findViewById(R.id.go_live);
		button_userdef = (LinearLayout) findViewById(R.id.go_userdef);
		button_setup = (LinearLayout) findViewById(R.id.go_setup);
	}

	// Listen for button clicks
	private void setListensers() {
		button_local.setOnClickListener(goListener);
		button_live.setOnClickListener(goListener);
		button_userdef.setOnClickListener(goListener);
		button_setup.setOnClickListener(goListener);
	}

	private Button.OnClickListener goListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.go_local:
				startLocalMedia();
				break;
			case R.id.go_live:
				startLiveMedia();
				break;
			case R.id.go_userdef:
				startUserdefMedia();
				break;
			case R.id.go_setup:
				startSetupMedia();
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
	 * 用户设置界面
	 */
	private void startSetupMedia() {
		Intent intent = new Intent();
		intent.setClass(HomeActivity.this, SetupActivity.class);
		startActivity(intent);
	}

	/**
	 * 在主界面按下返回键，提示用户是否退出应用
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_lock_power_off)
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
		// 停止转圈
		homeImage.clearAnimation();
		// 关闭广告
		AppConnect.getInstance(this).finalize();
		// System.exit(0);
		// 或者下面这种方式
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
