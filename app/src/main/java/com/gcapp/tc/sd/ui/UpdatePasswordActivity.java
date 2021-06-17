package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：设置里面的修改密码功能
 * 
 * @author lenovo
 * 
 */
public class UpdatePasswordActivity extends Activity implements
		View.OnClickListener {
	private final String TAG = "UpdatePasswordActivity";
	private Context context = UpdatePasswordActivity.this;
	private ImageButton btn_back;
	private EditText update_old_password_edit, update_new_password_edit,confirm_old_password;
	private Button update_password_submit;
	private String oldPass, NewPass,confirmPass;
	// private MyAsynTask myAsynTask;
	// private TextView tv_passAlert;
	private TextView tv_weaK, tv_middle, tv_strong;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	private String msg; // 错误信息

	private TextView tv_passSecurityLevel;// 密码安全度判断显示
	private LinearLayout ll_passSecurityLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_password);
		App.activityS.add(this);
		findView();
		setListener();
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		update_old_password_edit = (EditText) findViewById(R.id.update_old_password_edit);
		update_new_password_edit = (EditText) findViewById(R.id.update_new_password_edit);
		confirm_old_password = (EditText) findViewById(R.id.confirm_old_password);
		update_password_submit = (Button) findViewById(R.id.update_password_submit);
		// tv_passAlert = (TextView) findViewById(R.id.tv_passAlert);
		// tv_passAlert.setVisibility(View.GONE);
		tv_passSecurityLevel = (TextView) this
				.findViewById(R.id.tv_passSecurityLevel);// 密码安全度显示
		ll_passSecurityLevel = (LinearLayout) this
				.findViewById(R.id.ll_passSecurityLevel);// 密码安全度显示
		ll_passSecurityLevel.setVisibility(View.INVISIBLE);

		tv_weaK = (TextView) findViewById(R.id.tv_weak);
		tv_middle = (TextView) findViewById(R.id.tv_middle);
		tv_strong = (TextView) findViewById(R.id.tv_strong);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		update_password_submit.setOnClickListener(this);
		update_new_password_edit
				.addTextChangedListener(new EditChangedListener());
	}

	/**
	 * 监听密码文本输入
	 */
	class EditChangedListener implements TextWatcher {
		@Override
		public void afterTextChanged(Editable et) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// 输入文本之前的状态
		}

		@Override
		public void onTextChanged(CharSequence et, int start, int count,
				int after) {
			// tv_passAlert.setVisibility(View.GONE);
			ll_passSecurityLevel.setVisibility(View.INVISIBLE);
			// 输入文字中的状态，count是一次性输入字符数
			String str = et.toString();
			if (str.length() < 6 || str.length() > 16) {
				// tv_passAlert.setVisibility(View.VISIBLE);
				// tv_passAlert.setText("请输入6-16位数字、字母或常用符号，字母区分大小写");
			}

			if (quchong(str).size() < 3 || !ruoCheck(str)) {
				// tv_passAlert.setVisibility(View.VISIBLE);
				// tv_passAlert.setText("您输入的密码强度过弱，请重新输入，试试字母、数字、常用符号的组合");
			}

			if (null == str || "".equals(str)) {
				ll_passSecurityLevel.setVisibility(View.VISIBLE);
				tv_passSecurityLevel.setText("弱");
				changeTextView(tv_weaK);

			} else if (str.length() < 6 || quchong(str).size() < 3
					|| !ruoCheck(str)) {
				ll_passSecurityLevel.setVisibility(View.VISIBLE);
				tv_passSecurityLevel.setText("弱");
				changeTextView(tv_weaK);
				// 如果密码长度大于等于6 且 去重后密码长度大于等于3 且 密码中包含特殊字符 且 密码中包含英文字符 且 密码中包含数字
				// 的才为强密码。
			} else if (str.length() > 5 && quchong(str).size() > 2
					&& checkSpecial(str) && checkContainsLetter(str)
					&& checkContainsNum(str)) {
				ll_passSecurityLevel.setVisibility(View.VISIBLE);
				tv_passSecurityLevel.setText("强");
				changeTextView(tv_strong);

			} else {
				ll_passSecurityLevel.setVisibility(View.VISIBLE);
				tv_passSecurityLevel.setText("中");
				changeTextView(tv_middle);
			}
		}
	}

	/**
	 * 改变密码强度显示
	 * 
	 * @param tv
	 *            ：传入的文本框对象
	 */
	public void changeTextView(TextView tv) {
		tv_weaK.setBackgroundResource(R.color.hall_bg_grey2);
		tv_middle.setBackgroundResource(R.color.hall_bg_grey2);
		tv_strong.setBackgroundResource(R.color.hall_bg_grey2);
		// tv_weaK.setTextColor(Color.BLACK);
		// tv_middle.setTextColor(Color.BLACK);
		// tv_strong.setTextColor(Color.BLACK);

		tv.setBackgroundResource(R.color.common_bg_yellow);
		tv.setTextColor(Color.WHITE);
	}

	/**
	 * 是否包含特殊字符
	 * 
	 * @param password
	 *            ：传入的密码字符串
	 * @return
	 */
	public boolean checkSpecial(String password) {
		String all = "(\\~|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\_|\\-|\\=|\\+|\\\\|\\||\\[|\\]|\\{|\\}|\\;|\\'|\\:|\\\"|\\,|\\.|\\/|\\<|\\>|\\?)";
		Pattern pattern = Pattern.compile(all, Pattern.CASE_INSENSITIVE);
		boolean isSpecial = pattern.matcher(password).find();
		return isSpecial;
	}

	/**
	 * 是否包含数字
	 * 
	 * @param password
	 *            ：传入的密码字符串
	 * @return
	 */
	public boolean checkContainsNum(String password) {
		String all = "[0-9]";
		Pattern pattern = Pattern.compile(all, Pattern.CASE_INSENSITIVE);
		boolean isSpecial = pattern.matcher(password).find();
		return isSpecial;
	}

	/**
	 * 是否包含字母
	 * 
	 * @param password
	 *            ：传入的密码字符串
	 * @return
	 */
	public boolean checkContainsLetter(String password) {
		String all = "[a-zA-Z]";
		Pattern pattern = Pattern.compile(all, Pattern.CASE_INSENSITIVE);
		boolean isSpecial = pattern.matcher(password).find();
		return isSpecial;
	}

	/**
	 * 重写菜单创建方法
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_update_password, menu);
		return true;
	}

	/**
	 * 重写菜单按钮点击监听
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 公用的点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.update_password_submit:
			submit();
			break;
		}
	}

	/**
	 * 验证密码输入值并提交修改密码的请求
	 */
	private void submit() {
		oldPass = update_old_password_edit.getText().toString();
		NewPass = update_new_password_edit.getText().toString();
		confirmPass = confirm_old_password.getText().toString();
		
		if (TextUtils.isEmpty(oldPass)) {
			MyToast.getToast(this, "请输入旧的密码");
			return;
		}
		if (oldPass.trim().length() < 6) {
			MyToast.getToast(this, "旧的密码不少于6位数");
			return;
		}
		if (oldPass.trim().length() > 20) {
			MyToast.getToast(this, "旧的密码不大于20位数");
			return;
		}
		if ("".equals(NewPass) || TextUtils.isEmpty(NewPass)) {
			MyToast.getToast(context, "请输入新的密码");
			return;
		}
		if(!NewPass.equals(confirmPass)) {
			MyToast.getToast(this, "两次输入的新密码不一致");
			return;
		}
		if (NewPass.length() < 6 || NewPass.length() > 16) {
			MyToast.getToast(context, "请输入6-16位数字、字母或常用符号，字母区分大小写");
			return;
		}

		if (quchong(NewPass).size() < 3 || !ruoCheck(NewPass)) {
			MyToast.getToast(context, "您输入的密码强度过弱，请重新输入，试试字母、数字、常用符号的组合")
					;
			return;
		}

		if (NetWork.isConnect(this)) {
			UpdatePassword();
			// myAsynTask = new MyAsynTask();
			// myAsynTask.execute();
		} else {
			MyToast.getToast(this, "网络连接异常，请检查网络");
		}

	}

	/**
	 * 检验是否密码强度过弱
	 */
	public boolean ruoCheck(String password) {
		if (password.equals("123456") || password.equals("654321")
				|| password.equals("123456789") || password.equals("987654321")
				|| password.equals("12345678") || password.equals("87654321")
				|| password.equals("1234567") || password.equals("7654321"))
			return false;
		else
			return true;
	}

	/**
	 * 去除重复字符方法
	 * 
	 * @param password
	 *            ：密码字符串
	 * @return
	 */
	public List quchong(String password) {
		List list = new ArrayList();
		for (int i = 0; i < password.length(); i++) {
			if (!list.contains(password.substring(i, i + 1))) {
				list.add(password.substring(i, i + 1));
			}
		}
		return list;
	}

	/**
	 * 提交修改密码请求
	 */
	public void UpdatePassword() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"提交中...") {
			@Override
			public void responseCallback(JSONObject ob) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, " 修改密码请求的result：" + ob);
				try {
					msg = ob.getString("msg");
					String update = ob.getString("update");
					if ("1".equals(update)) {
						MyToast.getToast(context, "密码修改成功");
						exitLogin();
						// return AppTools.ERROR_SUCCESS + "";
					} else {
						MyToast.getToast(context, msg);
					}
				} catch (JSONException e) {
					// error = "-1";
					MyToast.getToast(context, "数据解析异常");
				}

				if (ob.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.commit_updatePass(AppTools.user.getUid(),
				MD5.md5(oldPass + AppTools.MD5_key),
				MD5.md5(NewPass + AppTools.MD5_key));
	}

	/**
	 * 退出登陆
	 */
	private void exitLogin() {
		AppTools.username = "";
		AppTools.userpass = "";
		AppTools.user = null;
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
		SharedPreferences settings = context
				.getSharedPreferences("app_user", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("isLogin", false);
		editor.putString("pass", "");
		editor.commit();
		finish();
	}

}
