package org.stagex.danmaku.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.stagex.danmaku.adapter.ChannelInfo;

import android.content.Context;
import android.util.Log;

public class ParseUtil {
	public static List<ChannelInfo> parse(Context context) {
		List<ChannelInfo> list = new ArrayList<ChannelInfo>();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			byte[] readBuffer = new byte[1024];
			InputStream fip = context.getAssets().open(
					"channel_list_cn.list.api2");
			int len = -1;
			int all = 0;
			while ((len = fip.read(readBuffer)) != -1) {
				// Log.d("ParseUtil", "len = " + len);
				all += len;
				String readString = new String(readBuffer, 0, len);
				stringBuffer.append(readString);
			}
			Log.d("ParseUtil",
					"all = " + all + " buff len = " + stringBuffer.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONArray arr = new JSONArray(stringBuffer.toString());

			int nums = arr.length();

			for (int i = 0; i < nums; i++) {
				JSONObject obj = arr.getJSONObject(i);
				int id = obj.getInt("channel_id");
				String name = obj.getString("channel_name");
				String icon_url = obj.getString("icon_url");
				String mode = obj.getString("mode");
				String url = obj.getString("url");
				JSONArray secArr = obj.getJSONArray("second_url");
				int size = secArr.length();
				String[] second_url = null;
				if (size > 0) {
					second_url = new String[size];
					for (int j = 0; j < size; j++) {
						second_url[j] = (String) secArr.get(j);
					}
				}
				String types = obj.getString("types");
				ChannelInfo info = new ChannelInfo(id, name, icon_url, mode, url, second_url, types);
				list.add(info);
			}

			Log.d("ParseUtil", "nums = " + nums);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
}
