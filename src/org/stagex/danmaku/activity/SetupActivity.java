package org.stagex.danmaku.activity;

import org.keke.player.R;
import org.stagex.danmaku.util.SystemUtility;

import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetupActivity extends Activity implements UpdatePointsNotifier {
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
	private RelativeLayout tuangou_sel;
	private RelativeLayout noad_sel;
	private ImageView button_ad;
	/* 记录硬解码与软解码的状态 */
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private boolean isHardDec;
	private boolean noAd;

	private TextView pointsTextView;
	private String displayPointsText;
	private String currencyName = "积分";
	final Handler mHandler = new Handler();

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
		noad_sel = (RelativeLayout) findViewById(R.id.noad_sel);
		button_ad = (ImageView) findViewById(R.id.ad_mode);
		about_sel = (RelativeLayout) findViewById(R.id.about_sel);
		help_sel = (RelativeLayout) findViewById(R.id.help_sel);
		feedback_sel = (RelativeLayout) findViewById(R.id.feedback_sel);
		update_sel = (RelativeLayout) findViewById(R.id.update_sel);
		appList_sel = (RelativeLayout) findViewById(R.id.appList_sel);
		tuangou_sel = (RelativeLayout) findViewById(R.id.tuangou_sel);

		pointsTextView = (TextView) findViewById(R.id.points_txt);

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

		/* 检测是否需要显示广告 */
		sharedPreferences = getSharedPreferences("keke_player", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		noAd = sharedPreferences.getBoolean("noAd", false);
		if (noAd) {
			int resource = SystemUtility.getDrawableId("mini_operate_selected");
			button_ad.setImageResource(resource);
			Log.d(LOGTAG, "检测到无广告模式");
		} else {
			int resource = SystemUtility
					.getDrawableId("mini_operate_unselected");
			button_ad.setImageResource(resource);
			Log.d(LOGTAG, "检测到有广告模式");
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
		tuangou_sel.setOnClickListener(goListener);
		noad_sel.setOnClickListener(goListener);
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
			case R.id.noad_sel:
				noAd = sharedPreferences.getBoolean("noAd", false);
				if (noAd) {
					int resource = SystemUtility
							.getDrawableId("mini_operate_unselected");
					button_ad.setImageResource(resource);
					editor.putBoolean("noAd", false);
					editor.commit();
					Log.d(LOGTAG, "设置为有广告模式");
				} else {

					if (sharedPreferences.getInt("pointTotal", 0) <= 500) {
						new AlertDialog.Builder(SetupActivity.this)
								.setIcon(R.drawable.ic_dialog_alert)
								.setTitle("温馨提示")
								.setMessage(
										"您的积分不足500分，暂时无法去除广告！\n您可以打开应用推荐赚取相应的积分，感谢您的支持！")
								.setPositiveButton("赚积分",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												AppConnect
														.getInstance(
																SetupActivity.this)
														.showOffers(
																SetupActivity.this);
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
											}
										}).show();
					} else {

						int resource = SystemUtility
								.getDrawableId("mini_operate_selected");
						button_ad.setImageResource(resource);
						editor.putBoolean("noAd", true);
						editor.commit();
						Log.d(LOGTAG, "设置为无广告模式");
					}
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
			case R.id.tuangou_sel:
				AppConnect.getInstance(SetupActivity.this).showTuanOffers(
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
		intent.putExtra("msgPath", "help.html");
		intent.putExtra("msgName", "使用帮助");
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// 从服务器端获取当前用户的虚拟货币.
		// 返回结果在回调函数getUpdatePoints(...)中处理
		AppConnect.getInstance(this).getPoints(this);
		super.onResume();
	}

	// 创建一个线程
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			if (pointsTextView != null) {
				pointsTextView.setText("(" + "当前" + displayPointsText + ")");
			}
		}
	};

	/**
	 * AppConnect.getPoints()方法的实现，必须实现
	 * 
	 * @param currencyName
	 *            虚拟货币名称.
	 * @param pointTotal
	 *            虚拟货币余额.
	 */
	public void getUpdatePoints(String currencyName, int pointTotal) {
		this.currencyName = currencyName;
		displayPointsText = currencyName + ": " + pointTotal;
		// 保存积分值
		editor.putInt("pointTotal", pointTotal);
		editor.commit();
		mHandler.post(mUpdateResults);
	}

	/**
	 * AppConnect.getPoints() 方法的实现，必须实现
	 * 
	 * @param error
	 *            请求失败的错误信息
	 */
	public void getUpdatePointsFailed(String error) {
		displayPointsText = error;
		mHandler.post(mUpdateResults);
	}
}
