package org.stagex.danmaku.adapter;

public class ChannelInfo {
	private int id;
	private String name;
	private String icon_url;
	private String mode;
	private String url;
	private String[] second_url;
	private String types;

	public ChannelInfo() {

	}

	public ChannelInfo(int id, String name, String icon_url, String mode,
			String url, String[] second_url, String types) {
		this.id = id;
		this.name = name;
		this.icon_url = icon_url;
		this.mode = mode;
		this.url = url;
		this.second_url = second_url;
		this.types = types;
	}

	public ChannelInfo(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String[] getSecond_url() {
		return second_url;
	}

	public void setSecond_url(String[] second_url) {
		this.second_url = second_url;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}
	
	

}
