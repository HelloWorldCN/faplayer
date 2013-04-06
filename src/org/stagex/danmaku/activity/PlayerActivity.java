package org.stagex.danmaku.activity;

import java.util.ArrayList;

import org.stagex.danmaku.R;
import org.stagex.danmaku.helper.SystemUtility;
import org.stagex.danmaku.player.AbsMediaPlayer;
import org.stagex.danmaku.player.DefMediaPlayer;
import org.stagex.danmaku.player.VlcMediaPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerActivity extends Activity implements
		AbsMediaPlayer.OnBufferingUpdateListener,
		AbsMediaPlayer.OnCompletionListener, AbsMediaPlayer.OnErrorListener,
		AbsMediaPlayer.OnInfoListener, AbsMediaPlayer.OnPreparedListener,
		AbsMediaPlayer.OnProgressUpdateListener,
		AbsMediaPlayer.OnVideoSizeChangedListener, OnTouchListener,
		OnClickListener, OnSeekBarChangeListener {

	static final String LOGTAG = "DANMAKU-PlayerActivity";

	private static final int SURFACE_NONE = 0;
	private static final int SURFACE_FILL = 1;
	private static final int SURFACE_ORIG = 2;
	private static final int SURFACE_4_3 = 3;
	private static final int SURFACE_16_9 = 4;
	private static final int SURFACE_16_10 = 5;
	private static final int SURFACE_MAX = 6;

	private static final int MEDIA_PLAYER_BUFFERING_UPDATE = 0x4001;
	private static final int MEDIA_PLAYER_COMPLETION = 0x4002;
	private static final int MEDIA_PLAYER_ERROR = 0x4003;
	private static final int MEDIA_PLAYER_INFO = 0x4004;
	private static final int MEDIA_PLAYER_PREPARED = 0x4005;
	private static final int MEDIA_PLAYER_PROGRESS_UPDATE = 0x4006;
	private static final int MEDIA_PLAYER_VIDEO_SIZE_CHANGED = 0x4007;

	/* the media player */
	private AbsMediaPlayer mMediaPlayer = null;

	/* */
	private ArrayList<String> mPlayListArray = null;
	private int mPlayListSelected = -1;

	/* GUI evnet handler */
	private Handler mEventHandler;

	/* player misc */
	private ProgressBar mProgressBarPreparing;
	private TextView mLoadingTxt;

	/* player controls */
	private TextView mTextViewTime;
	private SeekBar mSeekBarProgress;
	private TextView mTextViewLength;
	private ImageButton mImageButtonToggleMessage;
	private ImageButton mImageButtonSwitchAudio;
	private ImageButton mImageButtonSwitchSubtitle;
	private ImageButton mImageButtonPrevious;
	private ImageButton mImageButtonTogglePlay;
	private ImageButton mImageButtonNext;
	private ImageButton mImageButtonSwitchAspectRatio;

	private LinearLayout mLinearLayoutControlBar;

	/* player video */
	private SurfaceView mSurfaceViewDef;
	private SurfaceHolder mSurfaceHolderDef;
	private SurfaceView mSurfaceViewVlc;
	private SurfaceHolder mSurfaceHolderVlc;

	/* misc */
	private boolean mMediaPlayerLoaded = false;
	private boolean mMediaPlayerStarted = false;

	/* misc */
	private int mTime = -1;
	private int mLength = -1;
	private boolean mCanSeek = true;
	private int mAspectRatio = 1;				//直接全屏

	private int mAudioTrackIndex = 0;
	private int mAudioTrackCount = 0;
	private int mSubtitleTrackIndex = 0;
	private int mSubtitleTrackCount = 0;

	private static boolean isDefMediaPlayer(Object obj) {
		return obj.getClass().getName()
				.compareTo(DefMediaPlayer.class.getName()) == 0;
	}

	private static boolean isVlcMediaPlayer(Object obj) {
		return obj.getClass().getName()
				.compareTo(VlcMediaPlayer.class.getName()) == 0;
	}
	
	/**
	 * 播放过程中的事件响应的核心处理方法
	 */
	protected void initializeEvents() {
		mEventHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MEDIA_PLAYER_BUFFERING_UPDATE: {
					if (mMediaPlayerLoaded) {
						mProgressBarPreparing
								.setVisibility(msg.arg1 < 100 ? View.VISIBLE
										: View.GONE);
						mLoadingTxt
						.setVisibility(msg.arg1 < 100 ? View.VISIBLE
								: View.GONE);
					}
					break;
				}
				case MEDIA_PLAYER_COMPLETION: {
					/* TODO 播放结束后，如何处理 */
					//使用通知窗口
//					Toast.makeText(getApplicationContext(),"播放结束，请按返回键", Toast.LENGTH_LONG).show();
					//使用警告窗口 @{
					new AlertDialog.Builder(PlayerActivity.this)
				    .setTitle("播放结束")
				    .setMessage("该视频已经播放结束.")
				    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				            //do nothing - it will close on its own
				        	//关闭当前的PlayerActivity，退回listview的界面
				        	finish();
				        }
				     })
				   .show();
					//@}
					break;
				}
				/* FIXME 这里的处理有待进一步细化 */
				case MEDIA_PLAYER_ERROR: {
					Log.e(LOGTAG, "MEDIA_PLAYER_ERROR");
					/* fall back to VlcMediaPlayer if possible */
					if (isDefMediaPlayer(msg.obj)) {
//						Log.i(LOGTAG, "DefMediaPlayer selectMediaPlayer（VLC）");
						selectMediaPlayer(
								mPlayListArray.get(mPlayListSelected), true);
						break;
					} else if (isVlcMediaPlayer(msg.obj)) {
//						Log.i(LOGTAG, "VlcMediaPlayer");
						/* update status */
						mMediaPlayerLoaded = true;
						/* destroy media player */
						mSurfaceViewVlc.setVisibility(View.GONE);
					}
					/* update UI */
					if (mMediaPlayerLoaded) {
//						Log.i(LOGTAG, "VlcMediaPlayer update UI");
						mProgressBarPreparing.setVisibility(View.GONE);
						mLoadingTxt.setVisibility(View.GONE);
					}
					//FIXME not use it
//					startMediaPlayer();
					//弹出播放失败的窗口@{
					new AlertDialog.Builder(PlayerActivity.this)
				    .setTitle("播放失败")
				    .setMessage("很遗憾，该视频无法播放.")
				    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				            //do nothing - it will close on its own
				        	//关闭当前的PlayerActivity，退回listview的界面
				        	finish();
				        }
				     })
				   .show();
					//@}
//					Log.i(LOGTAG, "get out of alert");
					break;
				}
				case MEDIA_PLAYER_INFO: {
					if (msg.arg1 == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
						mCanSeek = false;
					}
					break;
				}
				case MEDIA_PLAYER_PREPARED: {
					if (isDefMediaPlayer(msg.obj) || isVlcMediaPlayer(msg.obj)) {
						/* update status */
						mMediaPlayerLoaded = true;
					}
					/* update UI */
					if (mMediaPlayerLoaded) {
						mProgressBarPreparing.setVisibility(View.GONE);
						mLoadingTxt.setVisibility(View.GONE);
					}
					startMediaPlayer();
					break;
				}
				case MEDIA_PLAYER_PROGRESS_UPDATE: {
					if (mMediaPlayer != null) {
						int length = msg.arg2;
						if (length >= 0) {
							mLength = length;
							mTextViewLength.setText(SystemUtility
									.getTimeString(mLength));
							mSeekBarProgress.setMax(mLength);
						}
						int time = msg.arg1;
						if (time >= 0) {
							mTime = time;
							mTextViewTime.setText(SystemUtility
									.getTimeString(mTime));
							mSeekBarProgress.setProgress(mTime);
						}
					}
					break;
				}
				case MEDIA_PLAYER_VIDEO_SIZE_CHANGED: {
					AbsMediaPlayer player = (AbsMediaPlayer) msg.obj;
					SurfaceView surface = isDefMediaPlayer(player) ? mSurfaceViewDef
							: mSurfaceViewVlc;
					int ar = mAspectRatio;
					//根据设置，改变播放界面大小和比例
					changeSurfaceSize(player, surface, ar);
					break;
				}
				default:
					break;
				}
			}
		};
	}

	/**
	 * 播放控件初始化：创建surface、获取各子控件的id
	 */
	protected void initializeControls() {
		/* SufaceView used by VLC is a normal surface */
		mSurfaceViewVlc = (SurfaceView) findViewById(R.id.player_surface_vlc);
		mSurfaceHolderVlc = mSurfaceViewVlc.getHolder();
		mSurfaceHolderVlc.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
		mSurfaceHolderVlc.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				createMediaPlayer(false, mPlayListArray.get(mPlayListSelected),
						mSurfaceHolderVlc);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				mMediaPlayer.setDisplay(holder);
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				destroyMediaPlayer(false);
			}

		});
		mSurfaceViewVlc.setOnTouchListener(this);
		/* SurfaceView used by MediaPlayer is a PUSH_BUFFERS surface */
		mSurfaceViewDef = (SurfaceView) findViewById(R.id.player_surface_def);
		mSurfaceViewDef.setOnTouchListener(this);
		mSurfaceHolderDef = mSurfaceViewDef.getHolder();
		mSurfaceHolderDef.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolderDef.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				createMediaPlayer(true, mPlayListArray.get(mPlayListSelected),
						mSurfaceHolderDef);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				mMediaPlayer.setDisplay(holder);
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				destroyMediaPlayer(true);
			}

		});

		mTextViewTime = (TextView) findViewById(R.id.player_text_position);
		mSeekBarProgress = (SeekBar) findViewById(R.id.player_seekbar_progress);
		mSeekBarProgress.setOnSeekBarChangeListener(this);
		mTextViewLength = (TextView) findViewById(R.id.player_text_length);
		mImageButtonToggleMessage = (ImageButton) findViewById(R.id.player_button_toggle_message);
		mImageButtonToggleMessage.setOnClickListener(this);
		mImageButtonSwitchAudio = (ImageButton) findViewById(R.id.player_button_switch_audio);
		mImageButtonSwitchAudio.setOnClickListener(this);
		mImageButtonSwitchSubtitle = (ImageButton) findViewById(R.id.player_button_switch_subtitle);
		mImageButtonSwitchSubtitle.setOnClickListener(this);
		mImageButtonPrevious = (ImageButton) findViewById(R.id.player_button_previous);
		mImageButtonPrevious.setOnClickListener(this);
		mImageButtonTogglePlay = (ImageButton) findViewById(R.id.player_button_toggle_play);
		mImageButtonTogglePlay.setOnClickListener(this);
		mImageButtonNext = (ImageButton) findViewById(R.id.player_button_next);
		mImageButtonNext.setOnClickListener(this);
		mImageButtonSwitchAspectRatio = (ImageButton) findViewById(R.id.player_button_switch_aspect_ratio);
		mImageButtonSwitchAspectRatio.setOnClickListener(this);

		mLinearLayoutControlBar = (LinearLayout) findViewById(R.id.player_control_bar);

		//缓冲进度圈
		mProgressBarPreparing = (ProgressBar) findViewById(R.id.player_prepairing);
		//缓冲提示语言
		mLoadingTxt =  (TextView)findViewById(R.id.player_loading);
	}

	protected void initializeData() {
		Intent intent = getIntent();
		String action = intent.getAction();
		if (action != null && action.equals(Intent.ACTION_VIEW)) {
			String one = intent.getDataString();
			mPlayListSelected = 0;
			mPlayListArray = new ArrayList<String>();
			mPlayListArray.add(one);
		} else {
			mPlayListSelected = intent.getIntExtra("selected", 0);
			mPlayListArray = intent.getStringArrayListExtra("playlist");
		}
		if (mPlayListArray == null || mPlayListArray.size() == 0) {
			Log.e(LOGTAG, "initializeData(): empty");
			finish();
			return;
		}
	}

	/**
	 * 重新设置播放器，控制各控件界面显示与否
	 */
	protected void resetMediaPlayer() {
		int resource = -1;
		/* initial status */
		mMediaPlayerLoaded = false;
		mTime = -1;
		mLength = -1;
		mCanSeek = true;
		mAspectRatio = 1;			//直接全屏
		/* */
		mImageButtonToggleMessage.setVisibility(View.GONE);
		mImageButtonSwitchAudio.setVisibility(View.GONE);
		mImageButtonSwitchSubtitle.setVisibility(View.GONE);
		mImageButtonPrevious
				.setVisibility((mPlayListArray.size() == 1) ? View.GONE
						: View.VISIBLE);
		mImageButtonTogglePlay.setVisibility(View.VISIBLE);
		resource = SystemUtility.getDrawableId("btn_play_0");
		mImageButtonTogglePlay.setBackgroundResource(resource);
		mImageButtonNext.setVisibility((mPlayListArray.size() == 1) ? View.GONE
				: View.VISIBLE);
		mImageButtonSwitchAspectRatio.setVisibility(View.VISIBLE);
		resource = SystemUtility.getDrawableId("btn_aspect_ratio_0");
		mImageButtonSwitchAspectRatio.setBackgroundResource(resource);
		/* */
		mLinearLayoutControlBar.setVisibility(View.GONE);
	}

	/**
	 * TODO 选择播放器：软解（VLC）或者硬解（MP），后续可以通过设置选项让用户来选择
	 * @param uri
	 * @param forceVlc
	 */
	protected void selectMediaPlayer(String uri, boolean forceVlc) {
		/* TODO: do this through configuration */
		boolean useDefault = true;
		int indexOfDot = uri.lastIndexOf('.');
		if (indexOfDot != -1) {
			String extension = uri.substring(indexOfDot).toLowerCase();
			/* used for mms network radio */
			boolean mms_radio_flag = uri.contains("mms://");
			boolean http_live_flag = uri.contains("http://");
			if (extension.compareTo(".flv") == 0
					|| extension.compareTo(".hlv") == 0
					|| extension.compareTo(".m3u8") == 0
					|| extension.compareTo(".mkv") == 0
					|| extension.compareTo(".rm") == 0
					|| extension.compareTo(".rmvb") == 0
					|| extension.compareTo(".ts") == 0
					|| mms_radio_flag 
					|| http_live_flag) {
				useDefault = false;
			}
		}
		if (forceVlc) {
			useDefault = false;
		}
		mSurfaceViewDef.setVisibility(useDefault ? View.VISIBLE : View.GONE);
		mSurfaceViewVlc.setVisibility(useDefault ? View.GONE : View.VISIBLE);
	}

	/**
	 * 创建MP
	 * @param useDefault
	 * @param uri
	 * @param holder
	 */
	protected void createMediaPlayer(boolean useDefault, String uri,
			SurfaceHolder holder) {
		Log.d(LOGTAG, "createMediaPlayer() " + uri);
		/* */
		resetMediaPlayer();
		/* */
		mMediaPlayer = AbsMediaPlayer.getMediaPlayer(useDefault);
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnInfoListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnProgressUpdateListener(this);
		mMediaPlayer.setOnVideoSizeChangedListener(this);
		mMediaPlayer.reset();
		mMediaPlayer.setDisplay(holder);
		mMediaPlayer.setDataSource(uri);
		mMediaPlayer.prepareAsync();
	}

	/**
	 * 销毁MP
	 * @param isDefault
	 */
	protected void destroyMediaPlayer(boolean isDefault) {
		boolean testDefault = isDefMediaPlayer(mMediaPlayer);
		if (isDefault == testDefault) {
			mMediaPlayer.setDisplay(null);
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	/**
	 * 启动播放器
	 */
	protected void startMediaPlayer() {
//		Log.i(LOGTAG, "startMediaPlayer() ");
		if (mMediaPlayerStarted || !mMediaPlayerLoaded) {
//			Log.i(LOGTAG, "(mMediaPlayerStarted || !mMediaPlayerLoaded) return");
			return;
		}
		if (mMediaPlayer != null) {
//			Log.i(LOGTAG, "mMediaPlayer.start()");
			mMediaPlayer.start();
			mMediaPlayerStarted = true;
		}
	}

	/**
	 * TODO 处理surface的界面比例
	 * @param player
	 * @param surface
	 * @param ar
	 */
	protected void changeSurfaceSize(AbsMediaPlayer player,
			SurfaceView surface, int ar) {
		int videoWidth = player.getVideoWidth();
		int videoHeight = player.getVideoHeight();
		if (videoWidth <= 0 || videoHeight <= 0) {
			return;
		}
		SurfaceHolder holder = surface.getHolder();
		holder.setFixedSize(videoWidth, videoHeight);
		int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
		int displayHeight = getWindowManager().getDefaultDisplay().getHeight();
		int targetWidth = -1;
		int targetHeight = -1;
		switch (ar) {
		case SURFACE_NONE: {
			targetWidth = videoWidth;
			targetHeight = videoHeight;
			break;
		}
		case SURFACE_FILL: {
			break;
		}
		case SURFACE_ORIG: {
			displayWidth = videoWidth;
			displayHeight = videoHeight;
			break;
		}
		case SURFACE_4_3: {
			targetWidth = 4;
			targetHeight = 3;
			break;
		}
		case SURFACE_16_9: {
			targetWidth = 16;
			targetHeight = 9;
			break;
		}
		case SURFACE_16_10: {
			targetWidth = 16;
			targetHeight = 10;
			break;
		}
		default:
			break;
		}
		if (targetWidth > 0 && targetHeight > 0) {
			double ard = (double) displayWidth / (double) displayHeight;
			double art = (double) targetWidth / (double) targetHeight;
			if (ard > art) {
				displayWidth = displayHeight * targetWidth / targetHeight;
			} else {
				displayHeight = displayWidth * targetHeight / targetWidth;
			}
		}
		LayoutParams lp = surface.getLayoutParams();
		lp.width = displayWidth;
		lp.height = displayHeight;
		surface.setLayoutParams(lp);
		surface.invalidate();
	}

	/**
	 * 入口方法
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//播放事件初始化
		initializeEvents();
		//加载布局
		setContentView(R.layout.player);
		//播放控件初始化
		initializeControls();
		//缓冲环显示
		mProgressBarPreparing.setVisibility(View.VISIBLE);
		//缓冲提示语
		mLoadingTxt.setVisibility(View.VISIBLE);
		//数据初始化
		initializeData();
		String uri = mPlayListArray.get(mPlayListSelected);
		//选择播放器
		selectMediaPlayer(uri, false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
		}
	}
	
	/**
	 * 播放过程中点击屏幕，显示相关控件
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!mMediaPlayerLoaded) {
			return true;
		}
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			int visibility = mLinearLayoutControlBar.getVisibility();
			if (visibility != View.VISIBLE) {
				mLinearLayoutControlBar.setVisibility(View.VISIBLE);
			} else {
				mLinearLayoutControlBar.setVisibility(View.GONE);
			}
			return true;
		}
		return false;
	}

	/**
	 * 对上述控件的子控件的操作响应
	 */
	@Override
	public void onClick(View v) {
		if (!mMediaPlayerLoaded)
			return;
		int id = v.getId();
		switch (id) {
		case R.id.player_button_switch_audio: {

			break;
		}
		case R.id.player_button_switch_subtitle: {

			break;
		}
		case R.id.player_button_previous: {

			break;
		}
		case R.id.player_button_toggle_play: {
			boolean playing = false;
			if (mMediaPlayer != null)
				playing = mMediaPlayer.isPlaying();
			if (playing) {
				if (mMediaPlayer != null)
					mMediaPlayer.pause();
			} else {
				if (mMediaPlayer != null)
					mMediaPlayer.start();
			}
			String name = String.format("btn_play_%d", !playing ? 1 : 0);
			int resouce = SystemUtility.getDrawableId(name);
			mImageButtonTogglePlay.setBackgroundResource(resouce);
			break;
		}
		case R.id.player_button_next: {
			break;
		}
		case R.id.player_button_switch_aspect_ratio: {
			mAspectRatio = (mAspectRatio + 1) % SURFACE_MAX;
			if (mMediaPlayer != null)
				changeSurfaceSize(mMediaPlayer,
						isDefMediaPlayer(mMediaPlayer) ? mSurfaceViewDef
								: mSurfaceViewVlc, mAspectRatio);
			String name = String.format("btn_aspect_ratio_%d", mAspectRatio);
			int resource = SystemUtility.getDrawableId(name);
			mImageButtonSwitchAspectRatio.setBackgroundResource(resource);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		/* not used */
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		/* not used */
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (!mMediaPlayerLoaded)
			return;
		int id = seekBar.getId();
		switch (id) {
		case R.id.player_seekbar_progress: {
			if (mCanSeek && mLength > 0) {
				int position = seekBar.getProgress();
				if (mMediaPlayer != null)
					mMediaPlayer.seekTo(position);
			}
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 以下：接收事件，做中间处理，再调用handleMessage方法处理之
	 * @{
	 */
	@Override
	public void onBufferingUpdate(AbsMediaPlayer mp, int percent) {
		Message msg = new Message();
		msg.obj = mp;
		msg.what = MEDIA_PLAYER_BUFFERING_UPDATE;
		msg.arg1 = percent;
		mEventHandler.sendMessage(msg);
	}

	@Override
	public void onCompletion(AbsMediaPlayer mp) {
		Message msg = new Message();
		msg.obj = mp;
		msg.what = MEDIA_PLAYER_COMPLETION;
		mEventHandler.sendMessage(msg);
	}

	@Override
	public boolean onError(AbsMediaPlayer mp, int what, int extra) {
		Message msg = new Message();
		msg.obj = mp;
		msg.what = MEDIA_PLAYER_ERROR;
		msg.arg1 = what;
		msg.arg2 = extra;
		mEventHandler.sendMessage(msg);
		return true;
	}

	@Override
	public boolean onInfo(AbsMediaPlayer mp, int what, int extra) {
		Message msg = new Message();
		msg.obj = mp;
		msg.what = MEDIA_PLAYER_INFO;
		msg.arg1 = what;
		msg.arg2 = extra;
		mEventHandler.sendMessage(msg);
		return true;
	}

	@Override
	public void onPrepared(AbsMediaPlayer mp) {
		Message msg = new Message();
		msg.obj = mp;
		msg.what = MEDIA_PLAYER_PREPARED;
		mEventHandler.sendMessage(msg);
	}

	@Override
	public void onProgressUpdate(AbsMediaPlayer mp, int time, int length) {
		Message msg = new Message();
		msg.obj = mp;
		msg.what = MEDIA_PLAYER_PROGRESS_UPDATE;
		msg.arg1 = time;
		msg.arg2 = length;
		mEventHandler.sendMessage(msg);
	}

	@Override
	public void onVideoSizeChangedListener(AbsMediaPlayer mp, int width,
			int height) {
		Message msg = new Message();
		msg.obj = mp;
		msg.what = MEDIA_PLAYER_VIDEO_SIZE_CHANGED;
		msg.arg1 = width;
		msg.arg2 = height;
		mEventHandler.sendMessage(msg);
	}
	/* @} */
}
