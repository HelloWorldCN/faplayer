package org.stagex.danmaku.activity;

import java.util.ArrayList;
import java.util.List;

import org.stagex.danmaku.R;
import org.stagex.danmaku.adapter.ChannelAdapter;
import org.stagex.danmaku.adapter.ChannelInfo;
import org.stagex.danmaku.util.ParseUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChannelListActivity extends Activity {
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_list_activity);

		list = (ListView) findViewById(R.id.channel_list);

		List<ChannelInfo> infos = ParseUtil.parse(this);

		ChannelAdapter adapter = new ChannelAdapter(this, infos);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ChannelInfo info = (ChannelInfo) list.getItemAtPosition(arg2);
				Log.d("ChannelInfo",
						"  name = " + info.getName() + "[" + info.getUrl()
								+ "]");

				startLiveMedia(info.getUrl());
			}
		});
	}

	private void startLiveMedia(String liveUrl) {
		Intent intent = new Intent(ChannelListActivity.this,
				PlayerActivity.class);
		ArrayList<String> playlist = new ArrayList<String>();
		playlist.add(liveUrl);
		intent.putExtra("selected", 0);
		intent.putExtra("playlist", playlist);
		startActivity(intent);
	}

}
