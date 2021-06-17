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
import android.text.Html;
import android.text.Spanned;
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
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyBankSpinner;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 账户信息
 * 
 * @author SLS003
 */
public class AccountInformationActivity extends Activity {
	private static final String TAG = "AccountInformationActivity";
	private Activity context = AccountInformationActivity.this;
	private TextView accInfo_tv_mobilephone; // 手机号码
	private TextView tv_tip_info; // 温馨提示
	private EditText accInfo_tv_uname; // 用户名
//	private EditText accInfo_tv_qqnumber;
//	public EditText bankinfo_et_soft; // 问题
//	private EditText bangkinfo_et_answer; // 答案
	private EditText tv_name; // 真实姓名rr
	private EditText tv_cardNum; // 身份证号
	public TextView tv_bankName; // 银行名称
	public TextView tv_bankLocation; // 开户地点
	private EditText tv_fullName; // 全称
	private EditText tv_bankNum; // 银行号
	private EditText accInfo_tv_zfb;
	private ImageButton btn_back;
//	private LinearLayout bankinfo_btn_soft;// 安全问题图标
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
//	private LinearLayout layout_question;// 问题及答案布局
	private LinearLayout layout_mobile;// 箭头
	private LinearLayout layout_bankname;
	private LinearLayout layout_bankadress;
	private LinearLayout layout_mb;// 布局
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
//	private String qqNumber = "123456789";
	//securityQuestionId securityQuestionAnswer
	private String uname, bankTypeId, bankId, bankCardNumber,zfbNumber,
			bankInCityId, realityName, idCardNO, bankInProvinceId;
	private MyBankSpinner spinner_bank, spinner_province, spinner_city;
	private Button btn_improve;
	private MyAsynTask myAsynTask;
	private MyHandler myHandler;
	public static List<Activity> list = new ArrayList<Activity>();
	private ProgressDialog dialog;
	/** 验证结果的对话框*/
	private ConfirmDialog talkDialog;
	private String errormsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_information);
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
		talkDialog = new ConfirmDialog(this, R.style.dialog);
		
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
					AppTools.CITY_TYPE, R.style.dialog);
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
					AppTools.user.setFullName(object
							.optString("branchBankName"));
					AppTools.user.setZfbNum(object
							.optString("alipayAccount"));
//					AppTools.user.setSecurityQuestionId(object
//							.optString("securityquestion"));
					
					String str2 = AppTools.user.getBangNum();
					String str3 = AppTools.user.getIdcardnumber();
					String str4 = AppTools.user.getMobile();
					String str5 = AppTools.user.getRealityName();
//					String str6 = AppTools.user.getQqNumber();
					AppTools.user.setBangNum(str2.substring(0, 3) + "***"
							+ str2.substring(str2.length() - 3));
					AppTools.user.setIdcardnumber(str3.substring(0, 3) + "***"
							+ str3.substring(str3.length() - 3));
					AppTools.user.setMobile(str4.substring(0, 3) + "***"
							+ str4.substring(str4.length() - 3));
					AppTools.user.setRealityName(str5.substring(0, 1) + "**");
//					if(!str6.equals("")) {
//						AppTools.user.setQqNumber(str6.substring(0, 3) + "***"
//								+ str6.substring(str6.length() - 3));
//					}else{
//						AppTools.user.setQqNumber(str6);
//					}
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
		tv_name = (EditText) this.findViewById(R.id.accInfo_tv_name2);
		tv_cardNum = (EditText) this.findViewById(R.id.accInfo_tv_cardNum2);
		tv_bankLocation = (TextView) this
				.findViewById(R.id.accInfo_tv_location2);
		tv_tip_info = (TextView) this.findViewById(R.id.tv_tip_info);
		tv_bankName = (TextView) this.findViewById(R.id.accInfo_tv_bankName2);
		tv_fullName = (EditText) this.findViewById(R.id.accInfo_tv_fullName2);
		tv_bankNum = (EditText) this.findViewById(R.id.accInfo_tv_bankNum2);
		accInfo_tv_zfb = (EditText) this.findViewById(R.id.accInfo_tv_zfb);
		accInfo_tv_mobilephone = (TextView) this
				.findViewById(R.id.accInfo_tv_mobilephone);
		accInfo_tv_uname = (EditText) this.findViewById(R.id.accInfo_tv_uname);
//		accInfo_tv_qqnumber = (EditText) this
//				.findViewById(R.id.accInfo_tv_qqnumber);
//		bangkinfo_et_answer = (EditText) this
//				.findViewById(R.id.bangkinfo_et_answer);
//		bankinfo_et_soft = (EditText) this.findViewById(R.id.bankinfo_et_soft);
//		layout_question = (LinearLayout) this
//				.findViewById(R.id.layout_question);
		layout_mobile = (LinearLayout) this.findViewById(R.id.layout_mobile);
		layout_bankname = (LinearLayout) this
				.findViewById(R.id.layout_bankname);
		layout_bankadress = (LinearLayout) this
				.findViewById(R.id.layout_bankadress);
		layout_mb = (LinearLayout) this.findViewById(R.id.layout_mb);
		layout_yh = (LinearLayout) this.findViewById(R.id.layout_yh);
		layout_adress = (LinearLayout) this.findViewById(R.id.layout_adress);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
//		bankinfo_btn_soft = (LinearLayout) this
//				.findViewById(R.id.bankinfo_btn_soft);
		btn_improve = (Button) this.findViewById(R.id.accInfo_btn_improve);
		btn_improve.setVisibility(View.GONE);
		btn_back.setOnClickListener(new MyClickListener());
//		bankinfo_btn_soft.setOnClickListener(new MyClickListener());
		btn_improve.setOnClickListener(new MyClickListener());
		layout_mb.setOnClickListener(new MyClickListener());
		layout_yh.setOnClickListener(new MyClickListener());
		layout_adress.setOnClickListener(new MyClickListener());
		getSiteNameAndPhone();
	}

	/**
	 * 设置不可用/可用和隐藏/显示
	 * 
	 * @param enabled
	 *            ：是否可用
	 */
	public void setEditTextEnabled(boolean enabled) {
		if (!enabled) {
			layout_mobile.setVisibility(View.GONE);
			layout_bankname.setVisibility(View.GONE);
			layout_bankadress.setVisibility(View.GONE);
			btn_improve.setVisibility(View.GONE);
//			layout_question.setVisibility(View.GONE);
		} else {
			layout_mobile.setVisibility(View.VISIBLE);
			layout_bankname.setVisibility(View.VISIBLE);
			layout_bankadress.setVisibility(View.VISIBLE);
			btn_improve.setVisibility(View.VISIBLE);

//			if (null != AppTools.user.getSecurityQuestionId()
//					&& !"".equals(AppTools.user.getSecurityQuestionId())
//					&& null != AppTools.user.getSecurityQuestionAnswer()
//					&& !"".equals(AppTools.user.getSecurityQuestionAnswer())) {
//				layout_question.setVisibility(View.GONE);
//			} else {
//				layout_question.setVisibility(View.VISIBLE);
//			}
		}
		btn_improve.setEnabled(enabled);
//		bankinfo_btn_soft.setEnabled(enabled);
		layout_mb.setEnabled(enabled);
		layout_yh.setEnabled(enabled);
		layout_adress.setEnabled(enabled);
		tv_name.setEnabled(enabled);
		tv_cardNum.setEnabled(enabled);
		tv_bankLocation.setEnabled(enabled);
		tv_bankName.setEnabled(enabled);
		tv_fullName.setEnabled(enabled);
		tv_bankNum.setEnabled(enabled);
		accInfo_tv_zfb.setEnabled(enabled);
		accInfo_tv_mobilephone.setEnabled(enabled);
		accInfo_tv_uname.setEnabled(enabled);
//		accInfo_tv_qqnumber.setEnabled(enabled);
//		bangkinfo_et_answer.setEnabled(enabled);
	}

	/**
	 * 验证填写信息是否完整
	 * 
	 * @return
	 */
	public boolean checkInfo() {
//		if (null != AppTools.user.getSecurityQuestionId()
//				&& !"".equals(AppTools.user.getSecurityQuestionId())
//				|| null != AppTools.user.getSecurityQuestionAnswer()
//				&& !"".equals(AppTools.user.getSecurityQuestionAnswer())) {
//			securityQuestionId = AppTools.user.getSecurityQuestionId();
//			securityQuestionAnswer = AppTools.user.getSecurityQuestionAnswer();
//		} else {
//			securityQuestionId = this.getID(question_index, listQuestion);
//			securityQuestionAnswer = bangkinfo_et_answer.getText().toString()
//					.trim();
//		}
		if (null != AppTools.user.getIdcardnumber()
				&& !"".equals(AppTools.user.getIdcardnumber())) {
			idCardNO = AppTools.user.getIdcardnumber();
		} else {
			idCardNO = tv_cardNum.getText().toString().trim();
		}
		mobile = accInfo_tv_mobilephone.getText().toString().trim();
		uname = accInfo_tv_uname.getText().toString().trim();
//		qqNumber = accInfo_tv_qqnumber.getText().toString().trim();
		realityName = tv_name.getText().toString().trim();
		bankTypeId = this.getID(bank_index, listBank);
		bankInCityId = this.getID(city_index, listCity);
		bankId = tv_fullName.getText().toString().trim();
		bankCardNumber = tv_bankNum.getText().toString().trim();
		zfbNumber = accInfo_tv_zfb.getText().toString().trim();
//		String name_verification = "^[\u4e00-\u9fa5_a-zA-Z]{1,6}$";
		String name_verification = "[·_a-zA-Z\u4e00-\u9fa5]{2,25}";
//		String isIDCard2_verification = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
		
		if ("".equals(mobile)) {
			MyToast.getToast(context, "请绑定手机号码");
			return false;
		}
		if ("".equals(uname)) {
			MyToast.getToast(context, "请输入用户名");
			return false;
		}
		// 验证qq数字位数
//		if (!"".equals(qqNumber)) {
//			byte[] bytes = qqNumber.getBytes();
//			if (bytes.length < 6) {
//				MyToast.getToast(context, "qq号最少为6位");
//				return false;
//			}
//		}
//		int charLength = LotteryUtils.getRexStrLength(uname);
//		if (charLength < 4 || charLength > 15) {
//			MyToast.getToast(context, "用户名长度不合法或包含特殊字符！");
//			return false;
//		}
		
		if ("".equals(realityName)) {
			MyToast.getToast(context, "请输入真实姓名");
			return false;
		} else {
			if (!realityName.matches(name_verification)) {
				MyToast.getToast(context, "请输入正确的真实姓名");
				return false;
			}
		}
		if ("".equals(idCardNO)) {
			MyToast.getToast(context, "请输入身份证号码");
			return false;
		}
		if (!isFull()) {
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
//		if ("-1".equals(securityQuestionId)) {
//			MyToast.getToast(context, "请输入选择安全问题");
//			return false;
//		}
//		if ("".equals(securityQuestionAnswer)) {
//			MyToast.getToast(context, "请输入答案");
//			return false;
//		}
		return true;
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
			case R.id.layout_mb:// 绑定手机号码
				Intent intent = new Intent(context, CheckPhoneNumber.class);
				intent.putExtra("type", "phone");
				context.startActivityForResult(intent, 0);
				break;
			case R.id.layout_yh:// 选择银行
				spinner_bank = new MyBankSpinner(context, listBank, bank_index,
						AppTools.BANK_TYPE, R.style.dialog);
				spinner_bank.show();
				break;
			case R.id.layout_adress:// 选择银行支行地点
				spinner_province = new MyBankSpinner(context, listProvince,
						province_index, AppTools.PROVINCE_TYPE, R.style.dialog);
				spinner_province.show();
				break;
			case R.id.accInfo_btn_improve:
				if (checkInfo()) {
					BindAccountInfo();
				}
				break;
//			case R.id.bankinfo_btn_soft:// 选择安全问题
//				spinner_question = new MyBankSpinner(context, listQuestion,
//						question_index, AppTools.QUESTION_TYPE, R.style.dialog);
//				spinner_question.show();
//				break;
			}
		}
	}

	/**
	 * activity的回调方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			if (data != null)
				accInfo_tv_mobilephone.setText(data.getStringExtra("mobile"));
		}
	}

	/**
	 * 异步任务 用来后台获取数据
	 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			dialog = BaseHelper.showProgress(AccountInformationActivity.this,
					null, "加载中...", true, false);
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
	 * 获取站点名称和客服电话
	 */
	public void getSiteNameAndPhone() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0) {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (null != jsonObject) {
					String name = jsonObject.optString("SiteName");
					String phone = jsonObject.optString("Phone");
					if (!"".equals(name)) {
						AppTools.APP_NAME = name;
					}
					if (!"".equals(phone)) {
						AppTools.SERVICE_PHONE = phone;
					}
				} else {
					if (RequestUtil.DEBUG)
						Log.e(TAG, "获取站点名称和客服电话---返回结果为空");
				}
				tv_tip_info.setText(packagingData());
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取站点名称和客服电话出错--- " + error.getMessage());
				tv_tip_info.setText(packagingData());
			}
		};
		requestUtil.getSiteNameAndPhone();
	}
	
	private Spanned packagingData() {
		Spanned tip = Html
				.fromHtml("<font color='#000000'>温馨提示：</font><br/>"
						+ "<font color='#666666'>1、我们承诺对您的个人信息进行保密，请放心认证。</font><br/>"
						+ "<font color='#666666'>2、以上信息仅用于提款到银行卡，请真实填写否则将无法绑定信息，真实姓名须同银行卡户名一致。</font><br/>"
						+ "<font color='#666666'>3、除银行卡外，信息填写后将无法再次修改，请确保填写正确。</font><br/>"
						+ "<font color='#666666'>4、如需修改银行卡相关信息，可在</font>"
						+ "<font color='#EB1827'>“我的”-“系统设置”-“修改银行卡”</font>"
						+ "<font color='#666666'>页面中进行修改。</font><br/>"
						+ "<font color='#666666'>5、如需修改账户信息，请联系【"
						+ getResources().getString(R.string.app_logo)
						+ "】客服。"+ "</font><br/>");
		return tip;
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
				setEditTextEnabled(true);
				if (!"".equals(AppTools.user.getName())) {
					accInfo_tv_uname.setText(AppTools.user.getName());
					accInfo_tv_uname.setEnabled(false);
				}
				if (!"".equals(AppTools.user.getMobile())) {
					accInfo_tv_mobilephone.setText(AppTools.user.getMobile());
					layout_mobile.setVisibility(View.GONE);
					layout_mb.setEnabled(false);
				}
				if (!"".equals(AppTools.user.getRealityName())) {
					tv_name.setText(AppTools.user.getRealityName());
					tv_name.setEnabled(false);
				}
				if (!"".equals(AppTools.user.getIdcardnumber())) {
					tv_cardNum.setText(AppTools.user.getIdcardnumber());
					tv_cardNum.setEnabled(false);
				}
//				if (!"".equals(AppTools.user.getQqNumber())) {
//					accInfo_tv_qqnumber.setText(AppTools.user.getQqNumber());
//					accInfo_tv_qqnumber.setEnabled(false);
//				}
				break;

			case 1:// 绑定了信息
				findView();
				setEditTextEnabled(false);
				accInfo_tv_mobilephone.setText(AppTools.user.getMobile());
				accInfo_tv_uname.setText(AppTools.user.getName());
//				accInfo_tv_qqnumber.setText(AppTools.user.getQqNumber());
				tv_name.setText(AppTools.user.getRealityName());
				tv_cardNum.setText(AppTools.user.getIdcardnumber() + "");
				tv_bankLocation.setText(AppTools.user.getProvinceName() + "-"
						+ AppTools.user.getCityName());
				tv_fullName.setText(AppTools.user.getFullName());
				tv_bankNum.setText(AppTools.user.getBangNum());
				accInfo_tv_zfb.setText(AppTools.user.getZfbNum());
				tv_bankName.setText(AppTools.user.getBankName());
//				bankinfo_et_soft.setText(AppTools.user.getSecurityQuestionId());
//				bangkinfo_et_answer.setText("****");
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
				"信息验证中...", 1) {
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
//						MyToast.getToast(context, errormsg);
						showDialog(errormsg);
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
				MyToast.getToast(context, "抱歉，请求绑定账户出现未知错误！");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		//securityQuestionId securityQuestionAnswer qqNumber
		requestUtil.getBindInfo(mobile, uname, idCardNO, bankTypeId,
				bankId, bankCardNumber, bankInProvinceId, bankInCityId,
				realityName,zfbNumber);
	}
	
	private void showDialog(String errormsg2) {
		talkDialog.show();
		talkDialog.setDialogTitle("认证失败");
		talkDialog.hideCancelBtn(true);
		talkDialog.setDialogUpdateText("确定");
		talkDialog.setDialogContent(errormsg2+"!");
		talkDialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				if (1 == resultCode) {
					talkDialog.dismiss();
				}
			}
		});
	}

}
