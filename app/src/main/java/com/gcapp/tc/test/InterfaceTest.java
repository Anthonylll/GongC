package com.gcapp.tc.test;

import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.gcapp.tc.utils.RequestUtil;

/**
 * 接口测试类
 */
public class InterfaceTest {
	
	public static void postTest(Context mContext) {
		
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"测试接口...") {
			@Override
			public void responseCallback(JSONObject item) {
				Log.d("anthony","item:"+item);
			}

			@Override
			public void responseError(VolleyError error) {
				
			}
		};
		requestUtil.test_interface();
	}
	
}
