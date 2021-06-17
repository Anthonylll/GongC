package com.gcapp.tc.sd.ui;

import org.json.JSONObject;
import com.android.volley.VolleyError;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * @author dm
 * 用户昵称修改
 */
public class UserNameActivity extends Activity implements OnClickListener{

	private static final String TAG = "UserNameActivity";
	private Context mContext = UserNameActivity.this;;
	private ImageButton btn_back;
	private EditText user_name_edit;
	private Button user_name_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_name);
		App.activityS.add(this);

		initView();
		initListener();
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		user_name_edit = (EditText) findViewById(R.id.user_name_edit);
		user_name_btn = (Button) findViewById(R.id.user_name_btn);
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		user_name_btn.setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {

		if(AppTools.user.getName().equals(AppTools.user.getMobile())) {
			user_name_edit.setFocusable(true);
			user_name_edit.setFocusableInTouchMode(true);
			user_name_edit.requestFocus();
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}else{
			user_name_btn.setClickable(false);
			user_name_edit.setTextColor(this.getResources().getColor(R.color.gray2));
			user_name_edit.setText("您已经修改过用户名,无法再次修改！");
			user_name_edit.clearFocus();
			user_name_edit.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.user_name_btn:
			String name = user_name_edit.getText().toString();
			if(checkUserName(name)) {
				modificationNmae(name);
			}
			break;
		}
	}
	
	/**
	 * 修改昵称
	 */
	private void modificationNmae(final String userName) {
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"正在提交...") {
			@Override
			public void responseCallback(JSONObject item) {
				try {
					if("0".equals(item.optString("error"))) {
						AppTools.user.setName(userName);
						MyToast.getToast(UserNameActivity.this, "修改成功!");
						finish();
					}else{
						MyToast.getToast(UserNameActivity.this, item.optString("msg"));
					}
				} catch (Exception ex) {
					MyToast.getToast(UserNameActivity.this, "修改失败!");
				}

				if (item.toString().equals("-500")) {
					MyToast.getToast(UserNameActivity.this, "连接超时!");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(UserNameActivity.this, "抱歉，请求出现未知错误!");
				if (RequestUtil.DEBUG){
					Log.e(TAG, "请求报错" + error.getMessage());
				}
			}
		};
		requestUtil.modification_nmae(userName);
	}
	
	/**
	 * 检测用户昵称
	 */
	private boolean checkUserName(String userName) {
		int length = LotteryUtils.getRexStrLength(userName);
		if(length<4) {
			MyToast.getToast(mContext, "用户名长度过短!");
			return false;
		}else if(length > 15) {
			MyToast.getToast(mContext, "用户名长度过长!");
			return false;
		}else if(length==0) {
			MyToast.getToast(mContext, "用户名包含特殊字符!");
			return false;
		}else{
			return true;
		}
	}
	
}
