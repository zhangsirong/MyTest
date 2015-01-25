package com.zhang.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhang.mobilesafe.domain.TaskInfo;
import com.zhang.mobilesafe.engine.TaskInfoProvider;
import com.zhang.mobilesafe.utils.SystemInfoUtils;
import com.zhang.mobliesafe.R;

public class TaskManagerActivity extends Activity {
	private TextView tv_mem_info;
	private TextView tv_process_count;
	private LinearLayout ll_loading;
	private ListView lv_task_manager;
	private List<TaskInfo> allTaskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	private TaskManagerAdapter adapter;
	private TextView tv_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		int processCount = SystemInfoUtils.getRunningProcessCount(this);
		tv_process_count.setText("运行中的内存:" + processCount + "个");
		long availMen = SystemInfoUtils.getAvailMem(this);
		long totalMen = SystemInfoUtils.getTotalMem(this);
		tv_mem_info.setText("剩余/总内存大小:"
				+ Formatter.formatFileSize(this, availMen) + "/"
				+ Formatter.formatFileSize(this, totalMen));
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
		fillData();
		tv_status = (TextView) findViewById(R.id.tv_status);
		lv_task_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (userTaskInfos != null & systemTaskInfos != null) {
					if (firstVisibleItem >= userTaskInfos.size() + 1) {
						tv_status.setText("系统进程:" + systemTaskInfos.size());
					} else {
						tv_status.setText("用户进程:" + userTaskInfos.size());

					}
				}

			}
		});
		
		
//		new CountDownTimer(5000, 2000) {
//			
//			@Override
//			public void onTick(long millisUntilFinished) {
//				System.out.println("millisUntilFinished"+millisUntilFinished);
//				
//			}
//			
//			@Override
//			public void onFinish() {
//				System.out.println("finish");
//				
//			}
//		}.start();
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TaskInfo taskInfo;
				if (position == 0) {// 用户进程标签
					return;
				} else if (position == (userTaskInfos.size() + 1)) {// 系统标签
					return;
				} else if (position <= userTaskInfos.size()) {
					taskInfo = userTaskInfos.get(position - 1);
				} else {
					taskInfo = systemTaskInfos.get(position - 1
							- userTaskInfos.size() - 1);
				}
				ViewHolder holder =(ViewHolder) view.getTag();
				if (taskInfo.isChecked()) {
					taskInfo.setChecked(false);
					holder.cb_status.setChecked(false);
				}else{
					taskInfo.setChecked(true);
					holder.cb_status.setChecked(true);
				}
			}
		});
	}

	/**
	 * 填充数据
	 */

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				allTaskInfos = TaskInfoProvider
						.getAppInfos(getApplicationContext());
				userTaskInfos = new ArrayList<TaskInfo>();
				systemTaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo info : allTaskInfos) {
					if (info.isUserTask()) {
						userTaskInfos.add(info);
					} else {
						systemTaskInfos.add(info);
					}
				}

				// 更新设置界面
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ll_loading.setVisibility(View.VISIBLE);
						adapter = new TaskManagerAdapter();
						lv_task_manager.setAdapter(adapter);

					}
				});
			}
		}.start();
	}

	private class TaskManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			if(sp.getBoolean("showsystem", false)){
				// return allTaskInfos.size()+2;
				return userTaskInfos.size() + 1 + systemTaskInfos.size() + 1;
			}else{
				return userTaskInfos.size() + 1 ;
			}
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo taskInfo;
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户进程: " + userTaskInfos.size() + "个");
				return tv;

			} else if (position == (userTaskInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统进程: " + systemTaskInfos.size() + "个");
				return tv;
			} else if (position <= userTaskInfos.size()) {
				taskInfo = userTaskInfos.get(position - 1);
			} else {
				taskInfo = systemTaskInfos.get(position - 1
						- userTaskInfos.size() - 1);
			}
			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),R.layout.list_item_taskinfo, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_task_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_task_name);
				holder.tv_memsize = (TextView) view.findViewById(R.id.tv_task_memsize);
				holder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
				view.setTag(holder);

			}
			holder.iv_icon.setImageDrawable(taskInfo.getIcon());
			holder.tv_name.setText(taskInfo.getName());
			holder.tv_memsize.setText("内存占用:"	+ Formatter.formatFileSize(getApplicationContext(),taskInfo.getMemsize()));
			holder.cb_status.setChecked(taskInfo.isChecked());
			return view;
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
	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_memsize;
		CheckBox cb_status;
	}
	/**
	 * 进入设置
	 */
	public void enterSetting(View view){
		Intent intent = new Intent(this,TaskSettingActivity.class);
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
