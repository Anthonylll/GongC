package com.gcapp.tc.sd.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.BaseHelper;
import com.gcapp.tc.utils.HttpUtils;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyBankSpinner;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 修改银行卡信息
 * @author SLS003
 */
public class AlterBankCardActivity extends Activity {
	private static final String TAG = "AlterBankCardActivity";
	private Activity context = AlterBankCardActivity.this;
	public TextView tv_bankName; // 银行名称
	public TextView tv_bankLocation; // 开户地点
	private EditText tv_fullName; // 全称
	private EditText tv_bankNum; // 银行号
	private EditText accInfo_tv_zfb;
	private ImageButton btn_back;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
//	private LinearLayout layout_bankname;
//	private LinearLayout layout_bankadress;
	private LinearLayout layout_yh;
	private LinearLayout layout_adress;

	private List<Map<String, String>> listBank = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listProvince = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listCity = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> listQuestion = new ArrayList<Map<String, String>>();

	public int bank_index = -1;
	public int province_index = -1;
	public int city_index = -1;
	public int question_index = -1;
	private String mobile;
	/**去掉该字段，现默认为123456789*/
//	private String qqNumber = "123456789";securityQuestionAnswer securityQuestionId
	private String uname, bankTypeId, bankId, bankCardNumber,zfbNumber,
			bankInCityId, realityName, idCardNO, bankInProvinceId;
	private MyBankSpinner spinner_bank, spinner_province, spinner_city;
	private Button alter_btn;
	private MyAsynTask myAsynTask;
	private MyHandler myHandler;
	public static List<Activity> list = new ArrayList<Activity>();
	private ProgressDialog dialog;
	private String errormsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alter_bank_card);
		App.activityS.add(this);
		list.add(this);
		init();
		myAsynTask = new MyAsynTask();
		myAsynTask.execute();
		getData(listProvince, "provincename", R.xml.province);
		getData(listBank, "bankname", R.xml.bank);
		getData(listQuestion, "question", R.xml.question);
	}

	/**
	 * 初始化参数
	 */
	private void init() {
		if (null != AppTools.user && AppTools.user.getUid() != null) {
			myHandler = new MyHandler();
			opt = "41";
			time = RspBodyBaseBean.getTime();
			imei = RspBodyBaseBean.getIMEI(this);
			info = "{}";
			String key = AppTools.key;
			crc = RspBodyBaseBean.getCrc(time, imei, key, info,
					AppTools.user.getUid());
			auth = RspBodyBaseBean.getAuth(crc, time, imei,
					AppTools.user.getUid());
		} else {
			RequestUtil requestUtil = new RequestUtil(context, false, 0) {
				@Override
				public void responseCallback(JSONObject reponseJson) {
					String retult = AppTools.doLogin(reponseJson);
					if ("0".equals(retult)) {
						SharedPreferences settings = context
								.getSharedPreferences("app_user", 0);
						boolean isLogin = false;
						String pass = "";
						if (settings.contains("isLogin")) {
							isLogin = settings.getBoolean("isLogin", false);
						}
						if (isLogin) {
							// 判断是否有存 密码
							if (settings.contains("pass")) {
								pass = settings.getString("pass", null);
							}
						}
						if (null != AppTools.user)
							AppTools.user.setUserPass(pass);
						if (RequestUtil.DEBUG)
							Log.i(TAG, "自动登录成功");
					} else {
						if (RequestUtil.DEBUG)
							Log.i(TAG, "自动登录失败" + retult);
					}
				}

				@Override
				public void responseError(VolleyError error) {
					if (RequestUtil.DEBUG)
						Log.i(TAG, "自动登录失败" + error.getMessage());
				}
			};
			requestUtil.doAutoLogin();
		}
	}

	/**
	 * 解析银行和省的XML
	 * 
	 * @param list
	 *            ：需要解析的list值
	 * @param name2
	 *            ：判断解析的参数
	 * @param xml
	 *            ：xml参数
	 */
	private void getData(List<Map<String, String>> list, String name2, int xml) {
		// 先清除
		list.clear();
		XmlResourceParser xrp = getResources().getXml(xml);
		try {
			Map<String, String> map = null;
			// 直到文档的结尾处
			while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
				// 如果遇到了开始标签
				if (xrp.getEventType() == XmlResourceParser.START_TAG) {
					String tagName = xrp.getName();// 获取标签的名字
					if (tagName.equals("row")) {
						map = new HashMap<String, String>();
						String id = xrp.getAttributeValue(null, "id");// 通过属性名来获取属性值
						String nm = xrp.getAttributeValue(null, name2);// 通过属性名来获取属性值
						map.put("id", id);
						map.put("name", nm);
						list.add(map);
					}
				}
				xrp.next();// 获取解析下一个事件
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析城市的XML
	 * 
	 * @param bankInProvinceId
	 *            :省份ID
	 */
	private void getData2(String bankInProvinceId) {
		listCity.clear();
		XmlResourceParser xrp = getResources().getXml(R.xml.city);
		try {
			// 直到文档的结尾处
			while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
				// 如果遇到了开始标签
				if (xrp.getEventType() == XmlResourceParser.START_TAG) {
					String tagName = xrp.getName();// 获取标签的名字
					if (tagName.equals("row")) {
						Map<String, String> map = new HashMap<String, String>();
						String proId = xrp
								.getAttributeValue(null, "provinceid");// 通过属性名来获取属性值
						if (bankInProvinceId.equals(proId)) {
							String id = xrp.getAttributeValue(null, "id");// 通过属性名来获取属性值
							String cityname = xrp.getAttributeValue(null,
									"cityname");// 通过属性名来获取属性值
							map.put("id", id);
							map.put("name", cityname);
							listCity.add(map);
						}
					}
				}
				xrp.next();// 获取解析下一个事件
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据省份得到各个城市
	 * 
	 * @return
	 */
	public boolean changCity() {
		// 判断是否选择了省份
		if (null != spinner_city && spinner_city.isShowing()) {
			spinner_city.dismiss();
		}
		if (province_index != -1) {
			for (Object o : listProvince.get(province_index).entrySet()) {
				Map.Entry entry = (Map.Entry) o;
				String key = (String) entry.getKey();
				if ("id".equals(key)) {
					bankInProvinceId = (String) entry.getValue();
					getData2(bankInProvinceId);
				}
			}
			spinner_city = new MyBankSpinner(context, listCity, city_index,
					AppTools.CITY_TYPE_TWO, R.style.dialog);
			spinner_city.show();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断银行卡是否绑定
	 */
	private boolean chechBank() {
		if (null == AppTools.user.getBangNum()
				|| AppTools.user.getBangNum().length() == 0) {
			String[] values = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			System.out.println("银行信息：" + result);
			try {
				JSONObject object = new JSONObject(result);
				String isBinded = object.optString("isBinded");
				if ("Yes".equals(isBinded)) {
					AppTools.user.setName(object.optString("name"));
					AppTools.user.setRealityName(object
							.optString("realityName"));
//					AppTools.user.setQqNumber(object.optString("qqnumber"));
					AppTools.user.setIdcardnumber(object
							.optString("idCardnumber"));
					AppTools.user.setMobile(object.optString("mobile"));
					AppTools.user
							.setBangNum(object.optString("bankCardNumber"));
					AppTools.user.setProvinceName(object
							.optString("provinceName"));
					AppTools.user.setCityName(object.optString("cityName"));
					AppTools.user.setBankName(object.optString("bankTypeName"));
					AppTools.user.setZfbNum(object.optString("alipayAccount"));
					AppTools.user.setFullName(object
							.optString("branchBankName"));
//					AppTools.user.setSecurityQuestionId(object
//							.optString("securityQuestionId"));
					
					AppTools.user.setBangNum(AppTools.user.getBangNum());
					AppTools.user.setIdcardnumber(AppTools.user.getIdcardnumber());
					AppTools.user.setMobile(AppTools.user.getMobile());
					AppTools.user.setRealityName(AppTools.user.getRealityName());
//					AppTools.user.setQqNumber(AppTools.user.getQqNumber());
//					AppTools.user.setSecurityQuestionAnswer(object
//							.optString("securityQuestionAnswer"));
					return true;
				} else {
					AppTools.user.setName(object.optString("name"));
					AppTools.user.setMobile(object.optString("mobile"));
//					AppTools.user.setQqNumber(object.optString("qqnumber"));

					AppTools.user.setRealityName(object
							.optString("realityName"));
					AppTools.user.setIdcardnumber(object
							.optString("idCardnumber"));
//					AppTools.user.setSecurityQuestionId(object
//							.optString("securityQuestionId"));
//					AppTools.user.setSecurityQuestionAnswer(object
//							.optString("securityQuestionAnswer"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("x", "AccountInfornation异常" + e.getMessage());
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		tv_bankLocation = (TextView) this
				.findViewById(R.id.accInfo_tv_location2);
		tv_bankName = (TextView) this.findViewById(R.id.accInfo_tv_bankName2);
		tv_fullName = (EditText) this.findViewById(R.id.accInfo_tv_fullName2);
		tv_bankNum = (EditText) this.findViewById(R.id.accInfo_tv_bankNum2);
		accInfo_tv_zfb = (EditText) this.findViewById(R.id.accInfo_tv_zfb);
//		layout_bankname = (LinearLayout) this
//				.findViewById(R.id.layout_bankname);
//		layout_bankadress = (LinearLayout) this
//				.findViewById(R.id.layout_bankadress);
		layout_yh = (LinearLayout) this.findViewById(R.id.layout_yh);
		layout_adress = (LinearLayout) this.findViewById(R.id.layout_adress);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		alter_btn = (Button) this.findViewById(R.id.alter_btn);
		btn_back.setOnClickListener(new MyClickListener());
		alter_btn.setOnClickListener(new MyClickListener());
		layout_yh.setOnClickListener(new MyClickListener());
		layout_adress.setOnClickListener(new MyClickListener());
	}

	/**
	 * 验证填写信息是否完整
	 * 
	 * @return
	 */
	public boolean checkInfo() {
		
//		if(null != AppTools.user.getSecurityQuestionId()
//				&& !"".equals(AppTools.user.getSecurityQuestionId())) {
//			securityQuestionId = AppTools.user.getSecurityQuestionId();
//		}else{
//			showToast();
//			return false;
//		}
//		if(null != AppTools.user.getSecurityQuestionAnswer()
//				&& !"".equals(AppTools.user.getSecurityQuestionAnswer())) {
//			securityQuestionAnswer = AppTools.user.getSecurityQuestionAnswer();
//		}else{
//			showToast();
//			return false;
//		}
		if(null != AppTools.user.getIdcardnumber() && !"".equals(AppTools.user.getIdcardnumber())) {
			idCardNO = AppTools.user.getIdcardnumber();
		}else{
			showToast();
			return false;
		}
		if(null != AppTools.user.getMobile() && !"".equals(AppTools.user.getMobile())) {
			mobile = AppTools.user.getMobile();
		}else{
			showToast();
			return false;
		}
		if(null != AppTools.user.getName() && !"".equals(AppTools.user.getName())){
			uname = AppTools.user.getName();
		}else{
			showToast();
			return false;
		}
//		if(null != AppTools.user.getQqNumber() && !"".equals(AppTools.user.getQqNumber())){
//			qqNumber = AppTools.user.getQqNumber();
//		}else{
//			showToast();
//			return false;
//		}
		if(null != AppTools.user.getRealityName() && !"".equals(AppTools.user.getRealityName())){
			realityName = AppTools.user.getRealityName();
		}else{
			showToast();
			return false;
		}
		
		bankTypeId = this.getID(bank_index, listBank);
		bankInCityId = this.getID(city_index, listCity);
		bankId = tv_fullName.getText().toString().trim();
		bankCardNumber = tv_bankNum.getText().toString().trim();
		zfbNumber = accInfo_tv_zfb.getText().toString().trim();
		String bankName = tv_bankName.getText().toString().trim();
		String bankAddress = tv_bankLocation.getText().toString().trim();

		if (bankName.equals("") || bankAddress.equals("") ) {
			MyToast.getToast(context, "请选择银行和开户地点");
			return false;
		}
		if ("".equals(bankId)) {
			MyToast.getToast(context, "请输入开户支行");
			return false;
		}
		if ("".equals(bankCardNumber)) {
			MyToast.getToast(context, "请输入银行卡号码");
			return false;
		}
		if ("".equals(zfbNumber)) {
			MyToast.getToast(context, "请输入支付宝账号");
			return false;
		}

		return true;
	}
	
	private void showToast() {
		MyToast.getToast(context, "请先完善信息！");
	}

	/**
	 * 拿到选项的 ID
	 * 
	 * @param index
	 *            ：下标
	 * @param list
	 *            ：集合list
	 * @return
	 */
	private String getID(int index, List<Map<String, String>> list) {
		// 判断是否选择了省份
		if (index != -1) {
			for (Object o : list.get(index).entrySet()) {
				Map.Entry entry = (Map.Entry) o;
				String key = (String) entry.getKey();
				if ("id".equals(key)) {
					return (String) entry.getValue();
				}
			}
		}
		return "-1";
	}

	/**
	 * 点击监听
	 */
	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.layout_yh:// 选择银行
				spinner_bank = new MyBankSpinner(context, listBank, bank_index,
						AppTools.BANK_TYPE_TWO, R.style.dialog);
				spinner_bank.show();
				break;
			case R.id.layout_adress:// 选择银行支行地点
				spinner_province = new MyBankSpinner(context, listProvince,
						province_index, AppTools.PROVINCE_TYPE_TWO, R.style.dialog);
				spinner_province.show();
				break;
			case R.id.alter_btn:
				if (checkInfo()) {
					BindAccountInfo();
				}
				break;
			}
		}
	}

	/**
	 * 异步任务 用来后台获取数据
	 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			dialog = BaseHelper.showProgress(AlterBankCardActivity.this,
					null, "加载中..", true, false);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// 如果有绑定银行
			if (!chechBank()) {
				myHandler.sendEmptyMessage(0);// 没有绑定信息
			} else {
				myHandler.sendEmptyMessage(1);// 绑定了信息
			}
			return null;
		}
	}

	/**
	 * 处理页面显示的
	 */
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			switch (msg.what) {
			case 0:// 没有绑定信息
				findView();
				break;

			case 1:// 绑定了信息
				findView();

//				tv_bankLocation.setText(AppTools.user.getProvinceName() + "-"
//						+ AppTools.user.getCityName());
				tv_fullName.setText(AppTools.user.getFullName());
				tv_bankNum.setText(AppTools.user.getBangNum());
				accInfo_tv_zfb.setText(AppTools.user.getZfbNum());
				break;

			case 2:// 绑定信息成功
				String qid = question_index + "";
//				AppTools.user.setSecurityQuestionId(qid);
				Intent intent = new Intent(context, BindSuccessActivity.class);
				context.startActivity(intent);
				context.finish();
				break;

			case -100:// 绑定失败
				MyToast.getToast(getApplicationContext(), errormsg);
				errormsg = "";
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 判断银行信息 和省信息是否为空
	 */
	private boolean isFull() {
		boolean b = true;
		// 判断银行类型是否为空
		if (bank_index != -1) {
			for (Object o : listBank.get(bank_index).entrySet()) {
				Map.Entry entry = (Map.Entry) o;
				String key = (String) entry.getKey();
				if ("id".equals(key)) {
					bankTypeId = (String) entry.getValue();
				}
			}
		} else {
			b = false;
		}
		// 判断是否选择城市
		if (city_index != -1) {
			for (Object o : listCity.get(city_index).entrySet()) {
				Map.Entry entry = (Map.Entry) o;
				String key = (String) entry.getKey();
				if ("id".equals(key)) {
//					cityId = (String) entry.getValue();
				}
			}
		} else {
			b = false;
		}
		return b;
	}

	/**
	 * 绑定账户信息
	 */
	public void BindAccountInfo() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			@Override
			public void responseCallback(JSONObject ob) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "绑定信息result：" + ob);

				try {
					if ("0".equals(ob.getString("error"))) {
						String qid = question_index + "";
//						AppTools.user.setSecurityQuestionId(qid);
						Intent intent = new Intent(context,
								BindSuccessActivity.class);
						context.startActivity(intent);
						context.finish();
					} else {
						errormsg = ob.getString("msg");
						MyToast.getToast(context, errormsg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (ob.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求修改账户出现未知错误!");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		//qqNumber securityQuestionAnswer securityQuestionId
		requestUtil.getBindInfo(mobile, uname, idCardNO, bankTypeId,
				bankId, bankCardNumber, bankInProvinceId, bankInCityId,
				realityName,zfbNumber);
	}

}
