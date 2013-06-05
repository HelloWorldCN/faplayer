package org.stagex.danmaku.activity;

import org.keke.player.R;
import org.stagex.danmaku.util.SystemUtility;

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

public class SetupActivity  extends Activity {
	/** Called when the activity is first created. */
	private static final String LOGTAG = "SetupActivity";
	
	/* 顶部标题栏的控件 */
	private Button button_home;
	private Button button_back;
	/* 设置控件 */
	private Button button_codec;
	private Button button_about;
	/* 记录硬解码与软解码的状态 */
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private boolean isHardDec;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		
		/* 顶部标题栏的控件 */
		button_home = (Button) findViewById(R.id.home_btn);
		button_back = (Button) findViewById(R.id.back_btn);
		/* 设置控件 */
		button_codec = (Button) findViewById(R.id.codec_mode);
		button_about = (Button) findViewById(R.id.about);
		
		/* 判断解码器状态 */
	    sharedPreferences = getSharedPreferences("keke_player", MODE_PRIVATE);
	    editor = sharedPreferences.edit();
	    isHardDec = sharedPreferences.getBoolean("isHardDec", false);
	    if (isHardDec)
	    {
	        int resource = SystemUtility.getDrawableId("mini_operate_selected");
			button_codec.setBackgroundResource(resource);
	        Log.d(LOGTAG, "检测到为硬解码模式");
	    }
	    else
	    {
			int resource = SystemUtility.getDrawableId("mini_operate_unselected");
			button_codec.setBackgroundResource(resource);
	        Log.d(LOGTAG, "检测到为软解码模式");
	    }
	    
		/* 设置监听 */
		setListensers();
	}
	
	// Listen for button clicks
	private void setListensers() {
		button_home.setOnClickListener(goListener);
		button_back.setOnClickListener(goListener);
		button_codec.setOnClickListener(goListener);
		button_about.setOnClickListener(goListener);
	}

	//按键监听
	private Button.OnClickListener goListener = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.home_btn:
				//退回主界面(homeActivity)
				finish();
				Intent intent = new Intent(SetupActivity.this,
						HomeActivity.class);
				startActivity(intent);
				break;
			case R.id.back_btn:
				//回到上一个界面(Activity)
				finish();
				break;
			case R.id.codec_mode:
				isHardDec = sharedPreferences.getBoolean("isHardDec", false);
			    if (isHardDec)  
			    {
			        int resource = SystemUtility.getDrawableId("mini_operate_unselected");
					button_codec.setBackgroundResource(resource);
			        editor.putBoolean("isHardDec", false);
			        editor.commit();
			        Log.d(LOGTAG, "设置为软解码模式");
			    }
			    else
			    {
					int resource = SystemUtility.getDrawableId("mini_operate_selected");
					button_codec.setBackgroundResource(resource);
			        editor.putBoolean("isHardDec", true);  
			        editor.commit();
			        Log.d(LOGTAG, "设置为硬解码模式");
			    }
				break;
			case R.id.about:
				startAboutMedia();
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
		new AlertDialog.Builder(SetupActivity.this)
	    .setTitle("关于")
	    .setMessage("版本：可可电视v1.1.0\n作者：可可工作室\n企鹅：1956733072\n鹅群：336809417\n联系：keke_player@163.com\n许可：FFmpeg & VLC")
	    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            //do nothing - it will close on its own
	        }
	     })
	   .show();
	};
	
}
