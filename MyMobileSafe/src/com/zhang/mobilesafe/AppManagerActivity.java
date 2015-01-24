package com.zhang.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhang.mobilesafe.domain.AppInfo;
import com.zhang.mobilesafe.engine.AppInfoProvider;
import com.zhang.mobliesafe.R;

public class AppManagerActivity extends Activity {
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private ListView lv_app_manager;
	private LinearLayout ll_loading;
	/**
	 * ���е�Ӧ�ó�����Ϣ
	 */
	private List<AppInfo> appInfos;

	/**
	 * �û�Ӧ�ó������Ϣ
	 */
	private List<AppInfo> userAppInfos;

	/**
	 * ϵͳӦ�ó������Ϣ
	 */
	private List<AppInfo> systemAppInfos;
	
	/**
	 * ��ǰ������Ϣ��״̬��
	 */
	private TextView tv_status;

	/**
	 * ������������
	 */
	private PopupWindow popupWindow;

	/**
	 * ����
	 */
	private LinearLayout ll_start;
	/**
	 * ����
	 */
	private LinearLayout ll_share;
	/**
	 * ж��
	 */
	private LinearLayout ll_uninstall;

	/**
	 * ���������Ŀ��
	 */
	private AppInfo appInfo;

	private AppManagerAdapter adapter;
	
	//private ApplockDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
		tv_status = (TextView) findViewById(R.id.tv_status);
		long sdsize = getAvailSpace(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		long romsize = getAvailSpace(Environment.getDataDirectory()
				.getAbsolutePath());
		tv_avail_sd.setText("SD������:" + Formatter.formatFileSize(this, sdsize));
		tv_avail_rom.setText("�ڲ�����:" + Formatter.formatFileSize(this, romsize));

		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo info : appInfos) {
					if (info.isUserApp()) {
						// �û�����
						userAppInfos.add(info);
					} else {
						// ϵͳ����
						systemAppInfos.add(info);
					}

				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						lv_app_manager.setAdapter(new AppManagerAdapter());
						ll_loading.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();

		// ��listviewע��һ�������ļ�����
		lv_app_manager.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			// ������ʱ����õķ�����
			// firstVisibleItem ��һ���ɼ���Ŀ��listview���������λ�á�
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//dismissPopupWindow();
				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem > userAppInfos.size()) {
						tv_status.setText("ϵͳ����" + systemAppInfos.size() + "��");
					} else {
						tv_status.setText("�û�����" + userAppInfos.size() + "��");
					}
				}
			}
		});
		
		/**
		 * ����listview�ĵ���¼�
		 */
		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return;
				} else if (position == (userAppInfos.size() + 1)) {
					return;
				} else if (position <= userAppInfos.size()) {// �û�����
					int newposition = position - 1;
					appInfo = userAppInfos.get(newposition);
				} else {// ϵͳ����
					int newposition = position - 1 - userAppInfos.size() - 1;
					appInfo = systemAppInfos.get(newposition);
				}
				 //System.out.println(appInfo.getPackname());
//				dismissPopupWindow();
//				View contentView = View.inflate(getApplicationContext(),
//						R.layout.popup_app_item, null);
//				ll_start = (LinearLayout) contentView
//						.findViewById(R.id.ll_start);
//				ll_share = (LinearLayout) contentView
//						.findViewById(R.id.ll_share);
//				ll_uninstall = (LinearLayout) contentView
//						.findViewById(R.id.ll_uninstall);
//
//				ll_start.setOnClickListener(AppManagerActivity.this);
//				ll_share.setOnClickListener(AppManagerActivity.this);
//				ll_uninstall.setOnClickListener(AppManagerActivity.this);
//
//				popupWindow = new PopupWindow(contentView, -2, -2);
//				// ����Ч���Ĳ��ű���Ҫ�����б�����ɫ��
//				// ͸����ɫҲ����ɫ
//				popupWindow.setBackgroundDrawable(new ColorDrawable(
//						Color.TRANSPARENT));
//				int[] location = new int[2];
//				view.getLocationInWindow(location);
//				// �ڴ����������õĿ��ֵ �������ء�---��dip
//				int dip = 60;
//				int px = DensityUtil.dip2px(getApplicationContext(), dip);
//				System.out.println("px=" + px);
//				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,
//						px, location[1]);
//				ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
//						Animation.RELATIVE_TO_SELF, 0,
//						Animation.RELATIVE_TO_SELF, 0.5f);
//				sa.setDuration(300);
//				AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
//				aa.setDuration(300);
//				AnimationSet set = new AnimationSet(false);
//				set.addAnimation(aa);
//				set.addAnimation(sa);
//				contentView.startAnimation(set);
			}
		});
		

	}
	private void dismissPopupWindow() {
		// �Ѿɵĵ�������رյ���
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	private class AppManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// return appInfos.size();
			return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AppInfo appInfo;
			if (position == 0) {// ��ʾ�����ó����ж��ٸ���С��ǩ
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("�û�����" + userAppInfos.size() + "��");
				return tv;
			} else if (position == (userAppInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("ϵͳ����" + systemAppInfos.size() + "��");
				return tv;
			} else if (position <= userAppInfos.size()) {// �û�����
				int newposition = position - 1;// ��Ϊ����һ��textview���ı�ռ����λ��
				appInfo = userAppInfos.get(newposition);
			} else {// ϵͳ����
				int newposition = position - 1 - userAppInfos.size() - 1;
				appInfo = systemAppInfos.get(newposition);
			}
			View view;
			ViewHolder holder;

			// if(position<userAppInfos.size()){//��Щλ���������û�������ʾ�ġ�
			// appInfo = userAppInfos.get(position);
			// }else{//��Щλ��������ϵͳ����ġ�
			// int newposition = position - userAppInfos.size();
			// appInfo = systemAppInfos.get(newposition);
			// }
			if (convertView != null && convertView instanceof RelativeLayout) {
				// ������Ҫ����Ƿ�Ϊ�գ���Ҫ�ж��Ƿ��Ǻ��ʵ�����ȥ����
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_appinfo, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.tv_location = (TextView) view
						.findViewById(R.id.tv_app_location);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
				//holder.iv_status = (ImageView) view.findViewById(R.id.iv_status);
				view.setTag(holder);
			}
			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getName());
			if (appInfo.isInRom()) {
				holder.tv_location.setText("�ֻ��ڴ�");
			} else {
				holder.tv_location.setText("�ⲿ�洢");
			}
//			if(dao.find(appInfo.getPackname())){
//				holder.iv_status.setImageResource(R.drawable.lock);
//			}else{
//				holder.iv_status.setImageResource(R.drawable.unlock);
//			}
			return view;
		}
	}

	static class ViewHolder {
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;
	}

	/**
	 * ��ȡĳ��Ŀ¼�Ŀ��ÿռ�
	 * 
	 * @param path
	 * @return
	 */
	private long getAvailSpace(String path) {
		StatFs statf = new StatFs(path);
		statf.getBlockCount();// ��ȡ�����ĸ���
		long size = statf.getBlockSize();// ��ȡ������С
		long count = statf.getAvailableBlocks();// ��ȡ���õ��������
		return size * count;
	}
}
