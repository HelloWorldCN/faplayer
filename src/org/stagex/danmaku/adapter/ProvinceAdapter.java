package org.stagex.danmaku.adapter;

import java.util.List;

import org.keke.player.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProvinceAdapter extends BaseAdapter {
	private static final String LOGTAG = "ProvinceAdapter";
	private List<ProvinceInfo> infos;
	private Context mContext;

	public ProvinceAdapter(Context context, List<ProvinceInfo> infos) {
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
		// Log.d(LOGTAG, infos.get(position));
		// TODO Auto-generated method stub
		View view = View.inflate(mContext, R.layout.province_list_item, null);
		TextView text1 = (TextView) view.findViewById(R.id.number);
		TextView text2 = (TextView) view.findViewById(R.id.province_name);

		text1.setText(String.valueOf(position + 1));
		text2.setText(infos.get(position).getProvinceName());

		return view;
	}

}
