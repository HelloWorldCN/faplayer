package org.stagex.danmaku.activity;

import org.keke.player.R;
import org.stagex.danmaku.util.SystemUtility;

import cn.waps.AppConnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetupActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String LOGTAG = "SetupActivity";

	/* 顶部标题栏的控件 */
	private ImageView button_home;
	private TextView button_back;
	/* 设置控件 */
	private RelativeLayout codec_sel;
	private ImageView button_codec;
	private RelativeLayout about_sel;
	private RelativeLayout help_sel;
	private RelativeLayout feedback_sel;
	private RelativeLayout update_sel;
	private RelativeLayout appList_sel;
	/* 记录硬解码与软解码的状态 */
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private boolean isHardDec;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);

		/* 顶部标题栏的控件 */
		button_home = (ImageView) findViewById(R.id.home_btn);
		button_back = (TextView) findViewById(R.id.back_btn);
		/* 设置控件 */
		codec_sel = (RelativeLayout) findViewById(R.id.codec_sel);
		button_codec = (ImageView) findViewById(R.id.codec_mode);
		about_sel = (RelativeLayout) findViewById(R.id.about_sel);
		help_sel = (RelativeLayout) findViewById(R.id.help_sel);
		feedback_sel = (RelativeLayout) findViewById(R.id.feedback_sel);
		update_sel = (RelativeLayout) findViewById(R.id.update_sel);
		appList_sel = (RelativeLayout) findViewById(R.id.appList_sel);

		/* 判断解码器状态 */
		sharedPreferences = getSharedPreferences("keke_player", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		isHardDec = sharedPreferences.getBoolean("isHardDec", false);
		if (isHardDec) {
			int resource = SystemUtility.getDrawableId("mini_operate_selected");
			button_codec.setImageResource(resource);
			Log.d(LOGTAG, "检测到为硬解码模式");
		} else {
			int resource = SystemUtility
					.getDrawableId("mini_operate_unselected");
			button_codec.setImageResource(resource);
			Log.d(LOGTAG, "检测到为软解码模式");
		}

		/* 设置监听 */
		setListensers();
	}

	// Listen for button clicks
	private void setListensers() {
		button_home.setOnClickListener(goListener);
		button_back.setOnClickListener(goListener);
		codec_sel.setOnClickListener(goListener);
		about_sel.setOnClickListener(goListener);
		help_sel.setOnClickListener(goListener);
		feedback_sel.setOnClickListener(goListener);
		update_sel.setOnClickListener(goListener);
		appList_sel.setOnClickListener(goListener);
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
			case R.id.codec_sel:
				isHardDec = sharedPreferences.getBoolean("isHardDec", false);
				if (isHardDec) {
					int resource = SystemUtility
							.getDrawableId("mini_operate_unselected");
					button_codec.setImageResource(resource);
					editor.putBoolean("isHardDec", false);
					editor.commit();
					Log.d(LOGTAG, "设置为软解码模式");
				} else {
					int resource = SystemUtility
							.getDrawableId("mini_operate_selected");
					button_codec.setImageResource(resource);
					editor.putBoolean("isHardDec", true);
					editor.commit();
					Log.d(LOGTAG, "设置为硬解码模式");
				}
				break;
			case R.id.about_sel:
				startAboutMedia();
				break;
			case R.id.help_sel:
				startHelpMedia();
				break;
			case R.id.feedback_sel:
				AppConnect.getInstance(SetupActivity.this).showFeedback();
				break;
			case R.id.update_sel:
				AppConnect.getInstance(SetupActivity.this).checkUpdate(
						SetupActivity.this);
				break;
			case R.id.appList_sel:
				AppConnect.getInstance(SetupActivity.this).showOffers(
						SetupActivity.this);
				break;
			default:
				Log.d(LOGTAG, "not supported btn id");
			}
		}
	};

	/**
	 * 程序关于界面
	 */
	private void startAboutMedia() {
		Intent intent = new Intent(SetupActivity.this, MessageActivity.class);
		intent.putExtra("msgPath", "about.html");
		intent.putExtra("msgName", "关于");
		startActivity(intent);
	}

	/**
	 * 程序帮助界面
	 */
	private void startHelpMedia() {
		Intent intent = new Intent(SetupActivity.this, MessageActivity.class);
		intent.putExtra("msgPath", "codec.html");
		intent.putExtra("msgName", "解码模式介绍");
		startActivity(intent);
	}
}
