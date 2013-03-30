package org.stagex.danmaku.activity;

import java.util.ArrayList;

import org.stagex.danmaku.R;
import org.stagex.danmaku.adapter.ChannelAdapter;
import org.stagex.danmaku.adapter.ChannelInfo;

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
		
		ArrayList<ChannelInfo> infos = new ArrayList<ChannelInfo>();
		
		//----------------------------------------------------------------------------------------------------------------------------------------
		/* TODO just for test */
		ChannelInfo info0 = new ChannelInfo("CCTV1", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv1&type=ipad");
		ChannelInfo info1 = new ChannelInfo("CCTV2", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv2&type=ipad");
		ChannelInfo info2 = new ChannelInfo("CCTV3", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv3&type=ipad");
		ChannelInfo info3 = new ChannelInfo("CCTV4", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv4&type=ipad");
		ChannelInfo info4 = new ChannelInfo("CCTV5", "http://biz.vsdn.tv380.com/playlive.php?5B63686E5D445830303030303038307C333137357C317C313030307C6C6235302E636E7C687474707C74735B2F63686E5DVSDNSOOONERCOM00");
		ChannelInfo info5 = new ChannelInfo("CCTV6", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv6&type=ipad");
		ChannelInfo info6 = new ChannelInfo("CCTV7", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv7&type=ipad");
		ChannelInfo info7 = new ChannelInfo("CCTV8", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv8&type=ipad");
		ChannelInfo info8 = new ChannelInfo("CCTV9", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctvjilu&type=ipad");
		ChannelInfo info9 = new ChannelInfo("CCTV10", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv10&type=ipad");
		ChannelInfo info10 = new ChannelInfo("CCTV11", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv11&type=ipad");
		ChannelInfo info11 = new ChannelInfo("CCTV12", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv12&type=ipad");
		ChannelInfo info12 = new ChannelInfo("CCTV13", "http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=pa://cctv_p2p_hdcctv13&type=ipad");

		ChannelInfo info13 = new ChannelInfo("风云足球", "http://rtmp.cntv.lxdns.com/live/cctv-fengyunzuqiu/playlist.m3u8");
		
		ChannelInfo info14 = new ChannelInfo("北京卫视","http://gslb.tv.sohu.com/live?cid=36&type=hls");
		ChannelInfo info15 = new ChannelInfo("广东卫视","http://gslb.tv.sohu.com/live?cid=60&type=hls");
		
		infos.add(info0);infos.add(info1);infos.add(info2);infos.add(info3);infos.add(info4);infos.add(info5);
		infos.add(info6);infos.add(info7);infos.add(info8);infos.add(info9);infos.add(info10);infos.add(info11);
		infos.add(info12);infos.add(info13);infos.add(info14);infos.add(info15);
		//----------------------------------------------------------------------------------------------------------------------------------------
		
		ChannelAdapter adapter = new ChannelAdapter(this, infos);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ChannelInfo info = (ChannelInfo) list.getItemAtPosition(arg2);
				Log.d("ChannelInfo", "  name = " + info.getName() + "[" + info.getUrl() + "]");
				
				startLiveMedia(info.getUrl());
			}
		});
	}
	
	private void startLiveMedia(String liveUrl) {
		Intent intent = new Intent(ChannelListActivity.this, 	PlayerActivity.class);
		ArrayList<String> playlist = new ArrayList<String>();
		playlist.add(liveUrl);
		intent.putExtra("selected", 0);
		intent.putExtra("playlist", playlist);
		startActivity(intent);
	}

}
