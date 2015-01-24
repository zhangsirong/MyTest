package com.zhang.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mobilesafe.db.dao.BlackNumberDao;
import com.zhang.mobilesafe.domain.BlackNumberInfo;
import com.zhang.mobliesafe.R;

public class CallSmsSafeActivity extends Activity {
	public static final String TAG = "CallSmsSafeActivity";
	private ListView lv_callsms_safe;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> infos;
	private CallSmsAdapter adapter;
	private LinearLayout ll_loading;
	private int offset = 0;
	private int maxnumber = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		dao = new BlackNumberDao(this);
		fillData();
		// listView注册一个可滚动的事件监听器
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {
			// 当滚动的状态发生变化的时候
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
					// 判断当前listiew滚动的位置
					// 获取最后一个可见条目在集合中的位置
					int lastposition = lv_callsms_safe.getLastVisiblePosition();
					System.out.println(lastposition);
					// 集合里面有20个item 位置从0开始,最后一个item位为19
					if (lastposition == (infos.size() - 1)) {
						System.out.println("列表移动到最后一个位置加载数据");
						offset += maxnumber;
						fillData();
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 手指触摸滚动状态

					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 惯性滑行状态

					break;
				}

			}

			// 滚动时调用的方法
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (infos == null) {
					infos = dao.findPart(offset, maxnumber);
				} else {// 原来已经加载过数据了
					infos.addAll(dao.findPart(offset, maxnumber));
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							adapter = new CallSmsAdapter();
							lv_callsms_safe.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
					}
				});
			};
		}.start();
	}

	private class CallSmsAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			ViewHolder holder;
			// 1.减少内存中view对象产生的次数
			if (convertView == null) {
				Log.i(TAG, "创建新的View对象" + position);
				// 把一个布局文件转化为View对象
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_callsms, null);
				// 2减少孩子的查询次数
				holder = new ViewHolder();
				holder.tv_number = (TextView) view
						.findViewById(R.id.tv_black_number);
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_black_mode);
				holder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);

				// 孩子生出来的时候找到他们的引用,存放在记事本,放在父亲的口袋
				view.setTag(holder);
			} else {
				Log.i(TAG, "复用历史缓存的view对象：" + position);
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getMode();
			if ("1".equals(mode)) {
				holder.tv_mode.setText("电话拦截");
			} else if ("2".equals(mode)) {
				holder.tv_mode.setText("短信拦截");
			} else {
				holder.tv_mode.setText("全部拦截");
			}

			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(
							CallSmsSafeActivity.this);
					builder.setTitle("警告");
					builder.setMessage("确定要删除这条记录吗?");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 删除数据库的内容
									dao.delete(infos.get(position).getNumber());
									// 更新界面
									infos.remove(position);
									// 通知listview适配器更新界面
									adapter.notifyDataSetChanged();

								}
							});
					builder.setNegativeButton("取消", null);
					builder.show();

				}
			});
			return view;
		}
	}

	/**
	 * View对象的容器 记录孩子的内存地址
	 */
	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}

	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private Button bt_ok;
	private Button bt_cancel;

	public void addBlackNumber(View view) {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View contentView = View.inflate(this, R.layout.dialog_add_blacknumber,
				null);
		et_blacknumber = (EditText) contentView
				.findViewById(R.id.et_blacknumber);
		cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
		cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
		bt_ok = (Button) contentView.findViewById(R.id.bt_ok);
		bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String blacknumber = et_blacknumber.getText().toString();
				if (TextUtils.isEmpty(blacknumber)) {
					Toast.makeText(getApplicationContext(), "黑名单号码不能为空", 0)
							.show();
					return;
				}
				String mode;
				if (cb_phone.isChecked() && cb_sms.isChecked()) {
					// 全部拦截
					mode = "3";
				} else if (cb_phone.isChecked()) {
					// 电话拦截
					mode = "1";
				} else if (cb_sms.isChecked()) {
					// 短信拦截
					mode = "2";
				} else {
					Toast.makeText(getApplicationContext(), "请选择拦截模式", 0)
							.show();
					return;
				}
				// 数据加到数据库
				dao.add(blacknumber, mode);
				// 更新ListView集合的内容
				BlackNumberInfo info = new BlackNumberInfo();
				info.setNumber(blacknumber);
				info.setMode(mode);
				infos.add(0, info);
				// 通知listView适配器数据更新了
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});

	}
}
