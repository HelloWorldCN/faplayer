package org.stagex.danmaku.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.stagex.danmaku.R;

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

	public ChannelAdapter(Context context, List<ChannelInfo> infos) {
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
		// TODO Auto-generated method stub
		View view = View.inflate(mContext, R.layout.channel_list_item, null);
		TextView text = (TextView) view.findViewById(R.id.channel_name);
		ImageView imageView = (ImageView) view.findViewById(R.id.channel_icon);
		text.setText(infos.get(position).getName());

		// 网络资源
		// imageView.setImageBitmap(httpGetBitmap(infos.get(position).getIcon_url()));
		//本地资源
		imageView.setImageBitmap(localGetBitmap(infos.get(position).getIcon_url()));
		return view;
	}

	/**
	 * 这个是根据网络url去下载图标
	 * 
	 * @param icon_url
	 * @return
	 */
	private Bitmap httpGetBitmap(String icon_url) {
		try {
			URL url = new URL(icon_url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			InputStream inputStream = connection.getInputStream();
			Bitmap bit = BitmapFactory.decodeStream(inputStream);
			return bit;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 这个是根据网络url，提取出文件名，到本地下载图标
	 * 
	 * @param icon_url
	 * @return
	 */
	private Bitmap localGetBitmap(String icon_url) {
		int index = icon_url.lastIndexOf("/");
		if (index == -1) {
			return null;
		}
		String url = icon_url.substring(index + 1);
		try {
			InputStream is = mContext.getAssets().open("tv_icon/" + url);
			
			//以最省的方式读取本地图片资源，防止out of memory
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inSampleSize = 2;
			opt.inPurgeable = true;
			opt.inInputShareable = true;

			return BitmapFactory.decodeStream(is, null, opt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
