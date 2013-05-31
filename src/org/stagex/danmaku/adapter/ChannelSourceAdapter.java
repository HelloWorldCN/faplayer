package org.stagex.danmaku.adapter;

import java.util.ArrayList;
import java.util.List;

import org.keke.player.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelSourceAdapter extends BaseAdapter {
	private static final String LOGTAG = "ChannelSourceAdapter";
	private ArrayList<String> infos;
	private Context mContext;

	public ChannelSourceAdapter(Context context, ArrayList<String> infos) {
		this.infos = infos;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.d(LOGTAG, infos.get(position));
		// TODO Auto-generated method stub
		View view = View.inflate(mContext, R.layout.channel_source_list_item, null);
		TextView text = (TextView) view.findViewById(R.id.channel_name);
		//text.setText(infos.get(position));
		text.setText("地址" + Integer.toString(position + 1) + "    ===>[" + infos.get(position) + "]");

		//Log.d(LOGTAG, "地址" + Integer.toString(position));
		
		return view;
	}
}
