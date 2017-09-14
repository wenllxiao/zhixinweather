package com.zhixin.bean;

public class IndexDataBean {

	private String title;// 指数名称
	private String zs; // 是否适宜
	private String tipt;// 建议
	private String des;// 描述
	public IndexDataBean(){
		
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	public String getTipt() {
		return tipt;
	}

	public void setTipt(String tipt) {
		this.tipt = tipt;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public IndexDataBean[] newArray(int size) {
		return new IndexDataBean[size];
	}
}
