package org.stagex.danmaku.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.stagex.danmaku.R;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelAdapter extends BaseAdapter {
	private List<ChannelInfo> infos;
	private Context mContext;
	
	//自定义的img加载类，提升加载性能，防止OOM
    public ImageLoader imageLoader; 

	public ChannelAdapter(Context context, List<ChannelInfo> infos) {
		this.infos = infos;
		this.mContext = context;
		
		imageLoader=new ImageLoader(context);
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
		// TODO Auto-generated method stub
		View view = View.inflate(mContext, R.layout.channel_list_item, null);
		TextView text = (TextView) view.findViewById(R.id.channel_name);
		ImageView imageView = (ImageView) view.findViewById(R.id.channel_icon);
		text.setText(infos.get(position).getName());

		//TODO 新方法，防止OOM
		 imageLoader.DisplayImage(infos.get(position).getIcon_url(), null, imageView);
		
		return view;
	}
}
