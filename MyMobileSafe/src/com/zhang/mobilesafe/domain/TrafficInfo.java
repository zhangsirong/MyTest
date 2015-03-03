package com.zhang.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 流量信息的业务Bean
 * 
 * @author hp
 *
 */
public class TrafficInfo {
	private String name;
	/**
	 * 下载流量
	 */
	private long rx_traffic;
	/**
	 * 上传流量
	 */
	private long tx_traffic;
	
	private long total_traffic;
	private Drawable icon;
	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getRx_traffic() {
		return rx_traffic;
	}

	public void setRx_traffic(long rx_traffic) {
		this.rx_traffic = rx_traffic;
	}

	public long getTx_traffic() {
		return tx_traffic;
	}

	public void setTx_traffic(long tx_traffic) {
		this.tx_traffic = tx_traffic;
	}

	public long getTotal_traffic() {
		return total_traffic;
	}

	public void setTotal_traffic(long total_traffic) {
		this.total_traffic = total_traffic;
	}


}
