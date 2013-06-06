package org.stagex.danmaku.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.stagex.danmaku.adapter.ChannelInfo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class ParseUtil {
	public static List<ChannelInfo> parse(Context context, Boolean pathFlag) {
		List<ChannelInfo> list = new ArrayList<ChannelInfo>();
		StringBuffer stringBuffer = new StringBuffer();
		int len = -1;
		int all = 0;
		
		try {
			byte[] readBuffer = new byte[1024];
			//pathFlag为true，表示采用更新后的地址
			//pathFlag为false，表示采用assert目录下默认地址
			if (pathFlag) {
				FileInputStream fos  = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + "/.channel_list_cn.list.api2");
				while ((len = fos.read(readBuffer)) != -1) {
					all += len;
					String readString = new String(readBuffer, 0, len);
					stringBuffer.append(readString);
				}
				fos.close();
			} else {
				InputStream fip  = context.getAssets().open("channel_list_cn.list.api2");
				while ((len = fip.read(readBuffer)) != -1) {
					all += len;
					String readString = new String(readBuffer, 0, len);
					stringBuffer.append(readString);
				}
				fip.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d("ParseUtil",
				"all = " + all + " buff len = " + stringBuffer.length());
		
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
