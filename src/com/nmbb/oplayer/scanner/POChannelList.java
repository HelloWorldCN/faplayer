package com.nmbb.oplayer.scanner;

import org.stagex.danmaku.adapter.ChannelInfo;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 节目列表PO类
 * 
 */
@DatabaseTable(tableName = "channeLlist")
public class POChannelList {
	@DatabaseField(generatedId = true)
	public long poId;
	/** 频道ID */
	@DatabaseField
	public int id;
	/** 频道名称 */
	@DatabaseField
	public String name;
	/** 电视台台标 */
	@DatabaseField
	public String icon_url;
	/** 节目源模式 */
	@DatabaseField
	public String mode;
	/** 节目源首选地址 */
	@DatabaseField
	public String url;
	/** 节目源候选地址 */
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	public String[] second_url;
	/** 节目频道分类 */
	@DatabaseField
	public String types;
	/** 节目预告地址 */
	@DatabaseField
	public String program_path;
	/** 是否收藏 */
	@DatabaseField
	public Boolean save;

	public POChannelList() {

	}

	public POChannelList(ChannelInfo info, Boolean save_flag) {
		id = info.getId();
		name = info.getName();
		icon_url = info.getIcon_url();
		mode = info.getMode();
		url = info.getUrl();
		this.second_url = info.getSecond_url();
		types = info.getTypes();
		program_path = info.getProgram_path();
		// 默认不收藏
		save = save_flag;
	}

	// copy 数据库数据到ChannelInfo
	public ChannelInfo POCopyData() {
		ChannelInfo info = new ChannelInfo(id, name, icon_url, mode, url,
				second_url, types, program_path);

		return info;
	}

	// just for test
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name=").append(name);
		return sb.toString();
	}
}
