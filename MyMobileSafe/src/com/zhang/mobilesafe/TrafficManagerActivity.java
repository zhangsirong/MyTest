package com.zhang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhang.mobilesafe.domain.TrafficInfo;
import com.zhang.mobilesafe.engine.TrafficInfoProvider;
import com.zhang.mobilesafe.utils.SortUtils;
import com.zhang.mobliesafe.R;

public class TrafficManagerActivity extends Activity {
	/**
	 * ���е�Ӧ�ó������Ϣ
	 */
	private List<TrafficInfo> trafficInfos;
	private LinearLayout ll_traffic_loading;
	private ListView lv_traffic_manager;
	private TrafficManagerAdapter adapter;
	
	private TextView tv_traffic_tx;
	private TextView tv_traffic_rx;
	private TextView tv_traffic_all;
	
	

	private long allTx;
	private long allRx;
	private long allTraffic;

	//����״̬
	private boolean sortFlag;
	
	private long codeSize;
	//����ʽ
	private Button ramSort; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager1);
		ll_traffic_loading = (LinearLayout) findViewById(R.id.ll_traffic_loading);
		lv_traffic_manager = (ListView) findViewById(R.id.lv_traffic_manager);
		tv_traffic_tx = (TextView) findViewById(R.id.tv_traffic_tx);
		tv_traffic_rx = (TextView) findViewById(R.id.tv_traffic_rx);
		tv_traffic_all = (TextView) findViewById(R.id.tv_traffic_all);
		ramSort = (Button) findViewById(R.id.ramSort);

		
		fillData();
	}

	private void fillData() {
		ll_traffic_loading.setVisibility(View.VISIBLE);

		new Thread() {
			public void run() {
			trafficInfos = TrafficInfoProvider.getTrafficInfos(TrafficManagerActivity.this);
			
				// �������ý��档
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ll_traffic_loading.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							adapter = new TrafficManagerAdapter();
							lv_traffic_manager.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						allTx = TrafficStats.getTotalTxBytes();// ��ȡȫ�� wifi 3g/2g�ϴ���������
						allRx = TrafficStats.getTotalRxBytes();// ��ȡȫ�� wifi 3g/2g���ص�������
						allTraffic = allRx + allTx;
						tv_traffic_tx.setText("���ϴ�:"+Formatter.formatFileSize(TrafficManagerActivity.this, allTx));
						tv_traffic_rx.setText("������:"+Formatter.formatFileSize(TrafficManagerActivity.this, allRx));
						tv_traffic_all.setText("������:"+Formatter.formatFileSize(TrafficManagerActivity.this, allTraffic));
					}
				});
				};
		}.start();
	}
	private class TrafficManagerAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return trafficInfos.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TrafficInfo trafficInfo;
			trafficInfo = trafficInfos.get(position);
			View view;
			ViewHolder holder;
			if (convertView != null) {
				// ������Ҫ����Ƿ�Ϊ�գ���Ҫ�ж��Ƿ��Ǻ��ʵ�����ȥ����
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),R.layout.list_item_trafficinfo, null);
				holder = new ViewHolder();
				holder.iv_traffic_icon = (ImageView) view.findViewById(R.id.iv_traffic_icon);
				holder.tv_traffic_tx = (TextView) view.findViewById(R.id.tv_traffic_tx);
				holder.tv_traffic_rx = (TextView) view.findViewById(R.id.tv_traffic_rx);
				holder.tv_traffic_all = (TextView) view.findViewById(R.id.tv_traffic_all);
				holder.tv_traffic_name = (TextView) view.findViewById(R.id.tv_traffic_name); 
				view.setTag(holder);
			}
			holder.iv_traffic_icon.setImageDrawable(trafficInfo.getIcon());
			holder.tv_traffic_name.setText(trafficInfo.getName());
			holder.tv_traffic_rx.setText("����:"+Formatter.formatFileSize(TrafficManagerActivity.this, trafficInfo.getRx_traffic()));
			holder.tv_traffic_tx.setText("�ϴ�:"+Formatter.formatFileSize(TrafficManagerActivity.this, trafficInfo.getTx_traffic()));
			holder.tv_traffic_all.setText("��:"+Formatter.formatFileSize(TrafficManagerActivity.this, trafficInfo.getTotal_traffic()));
			return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		
	}
	/**
	 * ��������
	 */
	public void sort(View view){
		SortUtils.sortList(sortFlag, trafficInfos);
		if(sortFlag){
			ramSort.setText("�������������ռ�ôӴ�С����");
			sortFlag = false;
		}else{
			ramSort.setText("�������������ռ�ô�С��������");
			sortFlag = true;
		}
		adapter.notifyDataSetChanged();
	}
	static class ViewHolder {
		TextView tv_traffic_tx;
		TextView tv_traffic_rx;
		TextView tv_traffic_all;
		TextView tv_traffic_name;
		ImageView iv_traffic_icon;
	}
	
		// 1��ȡһ����������
//		PackageManager pm = getPackageManager();
//		// 2�����ֻ�����ϵͳ,��ȡ����Ӧ�õ�uid
//		List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(0);
//		for (ApplicationInfo applicationInfo : applicationInfos) {
//			int uid = applicationInfo.uid;
//			long tx = TrafficStats.getUidTxBytes(uid);// �ϴ�������
//			long rx = TrafficStats.getUidRxBytes(uid);// ���ص�����byte
//
//			System.out.println(applicationInfo.packageName + ":�ϴ�����" + tx);
//			System.out.println(applicationInfo.packageName + ":��������" + rx);
//
//			
//
	//}
//		TrafficStats.getTotalTxBytes();// ��ȡȫ�� wifi 3g/2g�ϴ���������
//		TrafficStats.getTotalRxBytes();// ��ȡȫ�� wifi 3g/2g���ص�������

	
}
