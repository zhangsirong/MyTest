package com.zhang.mobilesafe.test;

import java.util.List;
import java.util.Random;

import android.test.AndroidTestCase;

import com.zhang.mobilesafe.db.BlackNumberDBOpenHelper;
import com.zhang.mobilesafe.db.dao.BlackNumberDao;
import com.zhang.mobilesafe.domain.BlackNumberInfo;
import com.zhang.mobilesafe.domain.TaskInfo;
import com.zhang.mobilesafe.engine.TaskInfoProvider;

public class TestTaskInfoProvider extends AndroidTestCase {
	public void testGetTaskInfos() throws Exception {
		List<TaskInfo> infos = TaskInfoProvider.getAppInfos(getContext());
		for(TaskInfo info:infos){
			System.out.println(info.toString());
		}
	}

}
