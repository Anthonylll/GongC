package com.gcapp.tc.fragment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestParams;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.AboutAppActivity;
import com.gcapp.tc.sd.ui.AccountInformationActivity;
import com.gcapp.tc.sd.ui.AllMessageActivity;
import com.gcapp.tc.sd.ui.CouponActivity;
import com.gcapp.tc.sd.ui.IntegralCenterAcitvity;
import com.gcapp.tc.sd.ui.MainActivity;
import com.gcapp.tc.sd.ui.MyAllAccountLotteryActivity;
import com.gcapp.tc.sd.ui.MyAllLotteryActivity;
import com.gcapp.tc.sd.ui.SettingActivity;
import com.gcapp.tc.sd.ui.SignCalendar_Activity;
import com.gcapp.tc.sd.ui.UserNameActivity;
import com.gcapp.tc.sd.ui.WithdrawalActivity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.FileUtil;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetUtil;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.CircleImg;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.SelectPicPopupWindow;
import com.gcapp.tc.R;

/**
 * 个人中心
 */
@SuppressLint("NewApi")
public class MyCenterFragment extends Fragment implements OnClickListener {

	private String resultStr = ""; // 服务端返回结果集
	private String auth, time, imei, crc; // 格式化后的参数
	private String info;
	private Bitmap bitmap_loadimage;
	private MyAsynTask_loadImage task;
	private MyHandler_image mHandler_image;
	private ProgressDialog proDialog;
	private CircleImg img_userlogo;// 头像
	private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
	private static final int REQUESTCODE_PICK = 0; // 相册选图标记
	private static final int REQUESTCODE_TAKE = 1; // 相机拍照标记
	private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
	private MyHandler3 mHandler_photo;
	private String myMsg;
	private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
	private String urlpath; // 图片本地路径

	private static final String TAG = "MyCenterFragment";
	private Context context;
	private ImageView btn_sign, great_man_flag;
	private TextView tv_freeze, center_tv_name, earning_text,
			lottery_money_text, center_text_msg;
	private TextView recharge_layout, draw_money_layout;
	private RelativeLayout lottery_layout, coupon_layout, setting_layout,
			news_layout;
	private RelativeLayout account_layout, bill_layout, integral_layout,
			waiter_layout;
	private long msgCount = 0;
	private MyHandler countHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("myfragment", "center");
		context = getActivity();
		// 删掉原来的布局文件
		View v = inflater.inflate(R.layout.activity_personal_center, container,
				false);
		mHandler_image = new MyHandler_image();
		mHandler_photo = new MyHandler3();
		findView(v);
		setListener();
		return v;
	}

	/**
	 * 初始化UI控件
	 * 
	 * @param v
	 */
	private void findView(View v) {
		btn_sign = (ImageView) v.findViewById(R.id.btn_sign);
		great_man_flag = (ImageView) v.findViewById(R.id.great_man_flag);
		tv_freeze = (TextView) v.findViewById(R.id.tv_freeze);
		earning_text = (TextView) v.findViewById(R.id.earning_text);
		lottery_money_text = (TextView) v.findViewById(R.id.lottery_money_text);
		recharge_layout = (TextView) v.findViewById(R.id.recharge_layout);
		draw_money_layout = (TextView) v.findViewById(R.id.draw_money_layout);
		setting_layout = (RelativeLayout) v.findViewById(R.id.setting_layout);
		lottery_layout = (RelativeLayout) v.findViewById(R.id.lottery_layout);
		waiter_layout = (RelativeLayout) v.findViewById(R.id.waiter_layout);
		coupon_layout = (RelativeLayout) v.findViewById(R.id.coupon_layout);
		account_layout = (RelativeLayout) v.findViewById(R.id.account_layout);
		bill_layout = (RelativeLayout) v.findViewById(R.id.bill_layout);
		integral_layout = (RelativeLayout) v.findViewById(R.id.integral_layout);
		news_layout = (RelativeLayout) v.findViewById(R.id.news_layout);
		center_tv_name = (TextView) v.findViewById(R.id.center_tv_name);
		center_text_msg = (TextView) v.findViewById(R.id.center_text_msg);
		img_userlogo = (CircleImg) v.findViewById(R.id.img_userlogo);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_sign.setOnClickListener(this);
		tv_freeze.setOnClickListener(this);
		recharge_layout.setOnClickListener(this);
		draw_money_layout.setOnClickListener(this);
		setting_layout.setOnClickListener(this);
		lottery_layout.setOnClickListener(this);
		waiter_layout.setOnClickListener(this);
		coupon_layout.setOnClickListener(this);
		account_layout.setOnClickListener(this);
		bill_layout.setOnClickListener(this);
		integral_layout.setOnClickListener(this);
		news_layout.setOnClickListener(this);
		integral_layout.setOnClickListener(this);
		news_layout.setOnClickListener(this);
		center_tv_name.setOnClickListener(this);
		img_userlogo.setOnClickListener(this);
	}

	/**
	 * 上传头像的窗口监听方法
	 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			// 拍照
			case R.id.takePhotoBtn:
				Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				takeIntent
						.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										IMAGE_FILE_NAME)));
				startActivityForResult(takeIntent, REQUESTCODE_TAKE);
				break;
			// 相册选择图片
			case R.id.pickPhotoBtn:
				Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
				// 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
				pickIntent
						.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
				startActivityForResult(pickIntent, REQUESTCODE_PICK);
				break;
			default:
				break;
			}
		}
	};

	private void initData() {
		if (AppTools.user.getIsgreatMan().equals("True")) {
			great_man_flag.setVisibility(View.VISIBLE);
		} else {
			great_man_flag.setVisibility(View.GONE);
		}
		countHandler = new MyHandler();
		getNewsCount();
	}

	/**
	 * 界面回调得到图片
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE_PICK:// 直接从相册获取
			try {
				startPhotoZoom(data.getData());
			} catch (NullPointerException e) {
				e.printStackTrace();// 用户点击取消操作
			}
			break;

		case REQUESTCODE_TAKE:// 调用相机拍照
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/" + IMAGE_FILE_NAME);
			startPhotoZoom(Uri.fromFile(temp));
			break;

		case REQUESTCODE_CUTTING:// 取得裁剪后的图片
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bitmap head = null;
		try {
			head = BitmapFactory.decodeStream(getActivity()
					.getContentResolver().openInputStream(uritempFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		img_userlogo.setImageBitmap(head);
		urlpath = FileUtil.saveFile(context,
				Environment.getExternalStorageDirectory() + "/eims",
				"temphead.jpg", head);
		// 要上传的图片文件
		MyAsynTask_Photo task = new MyAsynTask_Photo();
		task.execute();
	}

	/** 异步任务 */
	class MyAsynTask_Photo extends AsyncTask<Integer, Integer, String> {
		String error = "-11";

		@Override
		protected String doInBackground(Integer... params) {
			init();

			Map<String, String> textParams = new HashMap<String, String>();
			Map<String, File> fileparams = new HashMap<String, File>();
			try {
				// 创建一个URL对象
				URL url = new URL(AppTools.path);
				textParams = new HashMap<String, String>();
				fileparams = new HashMap<String, File>();
				// 要上传的图片文件
				File file = new File(urlpath);
				textParams.put("opt", "77");
				textParams.put("auth", auth);
				fileparams.put("info", file);
				// 利用HttpURLConnection对象从网络中获取网页数据
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
				conn.setConnectTimeout(5000);
				// 设置允许输出（发送POST请求必须设置允许输出）
				conn.setDoOutput(true);
				// 设置使用POST的方式发送
				conn.setRequestMethod("POST");
				// 设置不使用缓存（容易出现问题）
				conn.setUseCaches(false);
				conn.setRequestProperty("Charset", "UTF-8");// 设置编码
				// 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
				conn.setRequestProperty("ser-Agent", "Fiddler");
				// 设置contentType
				conn.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + NetUtil.BOUNDARY);

				OutputStream os = conn.getOutputStream();
				DataOutputStream ds = new DataOutputStream(os);
				NetUtil.writeStringParams(textParams, ds);
				NetUtil.writeFileParams(fileparams, ds);
				NetUtil.paramsEnd(ds);
				// 对文件流操作完,要记得及时关闭
				os.close();
				// 服务器返回的响应吗
				int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
				// 对响应码进行判断
				if (code == 200) {// 返回的响应码200,是成功
					// 得到网络返回的输入流
					InputStream is = conn.getInputStream();
					resultStr = NetUtil.readString(is);
					JSONObject object = new JSONObject(resultStr);
					error = object.optString("success");
					myMsg = object.optString("info");

					if (!object.optString("url").contains(AppTools.url)) {
						AppTools.user.setImage_url(AppTools.url
								+ object.optString("url"));
					} else {
						AppTools.user.setImage_url(object.optString("url"));
					}
					if (null == object.optString("url")
							|| "".equals(object.optString("url"))) {
						error = "-1";
					}
				} else {
					MyToast.getToast(context, "请求URL失败！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			mHandler_photo.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/**
	 * 使用HttpUrlConnection模拟post表单进行文件 上传平时很少使用，比较麻烦 原理是：
	 * 分析文件上传的数据格式，然后根据格式构造相应的发送给服务器的字符串。
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler3 extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (proDialog != null) {
				proDialog.dismiss();
			}
			switch (msg.what) {
			case 0:
				MyToast.getToast(context, "图片上传成功！");
				break;
			default:
				MyToast.getToast(context, myMsg);
				break;
			}
			super.handleMessage(msg);
		}
	}

	private Uri uritempFile;

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		// intent.putExtra("return-data", true);

		uritempFile = Uri.parse("file://" + "/"
				+ Environment.getExternalStorageDirectory().getPath() + "/"
				+ "small.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}

	/**
	 * 公用点击监听
	 * 
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sign:
			Intent intent = new Intent(context, SignCalendar_Activity.class);
			context.startActivity(intent);
			break;
		case R.id.recharge_layout:
			LotteryUtils.toRecharge(context, AppTools.canAlipay);
			break;
		case R.id.draw_money_layout:
			int withdrawMoney = (int) AppTools.user.getMinWithdraw();
			if (withdrawMoney > Double.parseDouble(AppTools.user.getBalance())) {
				MyToast.getToast(getActivity(), "可用余额不足" + withdrawMoney
						+ "元，不能提款！");
			} else {
				if (null == AppTools.user.getRealityName()
						|| AppTools.user.getRealityName().length() == 0) {
					MyToast.getToast(context, "请先绑定身份证信息");
					Intent mIntent = new Intent(context,
							AccountInformationActivity.class);
					context.startActivity(mIntent);
				} else if (null == AppTools.user.getBangNum()) {
					getIsBindOrNot();
				} else {
					Intent mIntent = new Intent(context,
							WithdrawalActivity.class);
					context.startActivity(mIntent);
				}
			}
			break;
		case R.id.coupon_layout:
			Intent intentCoupon = new Intent(context, CouponActivity.class);
			context.startActivity(intentCoupon);
			break;
		case R.id.lottery_layout:
			ToOtherActivity(0);
			break;
		case R.id.account_layout:
			intent = new Intent(context, AccountInformationActivity.class);
			context.startActivity(intent);
			break;
		case R.id.bill_layout:
			Intent Myintent = new Intent(context,
					MyAllAccountLotteryActivity.class);
			context.startActivity(Myintent);
			break;
		case R.id.img_userlogo:// 上传头像
			menuWindow = new SelectPicPopupWindow(context, itemsOnClick);
			menuWindow.showAtLocation(img_userlogo, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.setting_layout:
			intent = new Intent(context, SettingActivity.class);
			context.startActivity(intent);
			break;
		case R.id.integral_layout:// 积分中心
			intent = new Intent(context, IntegralCenterAcitvity.class);
			context.startActivity(intent);
			break;
		case R.id.center_tv_name:
			intent = new Intent(context, UserNameActivity.class);
			context.startActivity(intent);
			break;
		case R.id.news_layout:
			Intent intentNews2 = new Intent(context, AllMessageActivity.class);
			context.startActivity(intentNews2);
			break;
		case R.id.waiter_layout:
			intent = new Intent(context, AboutAppActivity.class);
			context.startActivity(intent);
			break;
		}
	}

	/**
	 * 初始化参数
	 */
	private void init() {
		// opt = "41";
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(context);
		String key = AppTools.key;
		info = "{}";
		crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());
	}

	/**
	 * 公用跳转方法
	 * 
	 */
	private void ToOtherActivity(int lotteryType) {
		Intent intent = new Intent(context, MyAllLotteryActivity.class);
		intent.putExtra("index", lotteryType);
		context.startActivity(intent);
	}

	@Override
	public void onResume() {
		if (null == AppTools.user) {
			Intent intent = new Intent(context, MainActivity.class);
			context.startActivity(intent);
		} else {
			doAutoLogin();
			initData();
			tv_freeze.setText("¥ " + AppTools.user.getBalance() + "元");
			earning_text.setText(AppTools.user.getTotalWinMoney() + "元");
			lottery_money_text.setText(AppTools.user.getHandselAmount() + "元");
			if (AppTools.user.getName().equals(AppTools.user.getMobile())) {
				center_tv_name.setText(AppTools.user.getName() + "[修改昵称]");
			} else {
				center_tv_name.setText(AppTools.user.getName());
			}
		}
		if (null != AppTools.user && !"".equals(AppTools.user)
				&& null != AppTools.user.getImage_url()
				&& !"".equals(AppTools.user.getImage_url())) {
			System.out.println("====头像URL========="
					+ AppTools.user.getImage_url());
			task = new MyAsynTask_loadImage();
			task.execute();
		}
		super.onResume();
	}

	/**
	 * 异步任务 用来后台获取数据
	 * 
	 */
	class MyAsynTask_loadImage extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			URL myFileUrl = null;
			HttpURLConnection conn = null;
			InputStream is = null;
			try {
				myFileUrl = new URL(AppTools.user.getImage_url());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				conn = (HttpURLConnection) myFileUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
				bitmap_loadimage = BitmapFactory.decodeStream(is);
				if (null == bitmap_loadimage) {
					return "-1";
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null && conn != null) {
						is.close();
						conn.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return "11";
		}

		@Override
		protected void onPostExecute(String result) {
			mHandler_image.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	/**
	 * 处理异步请求结果
	 * 
	 * @author lenovo
	 * 
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler_image extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (proDialog != null && proDialog.isShowing()) {
				proDialog.dismiss();
			}
			switch (msg.what) {
			case -500:
				break;
			case 11:// 成功
				img_userlogo.setImageBitmap(bitmap_loadimage);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 提交银行卡绑定请求
	 */
	public void getIsBindOrNot() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "支付方式请求结果" + object);
				try {
					String isBinded = object.optString("isBinded");
					if ("Yes".equals(isBinded)) {
						AppTools.user.setBangNum(object
								.optString("bankCardNumber"));
						AppTools.user.setProvinceName(object
								.optString("provinceName"));
						AppTools.user.setCityName(object.optString("cityName"));
						AppTools.user.setBankName(object
								.optString("bankTypeName"));
						AppTools.user.setFullName(object
								.optString("branchBankName"));
						AppTools.user.setZfbNum(object
								.optString("alipayAccount"));
						AppTools.user.setSecurityQuestionId(object
								.optString("securityquestion"));
						String str2 = AppTools.user.getBangNum();
						AppTools.user.setBangNum(str2.substring(0, 3) + "***"
								+ str2.substring(str2.length() - 3));
						// error = "0";
						Intent mIntent = new Intent(context,
								WithdrawalActivity.class);
						context.startActivity(mIntent);

					} else {
						MyToast.getToast(context, "请先绑定银行信息");
						Intent mIntent = new Intent(context,
								AccountInformationActivity.class);
						context.startActivity(mIntent);
						// error = "-2";
					}
				} catch (Exception e) {
					e.printStackTrace();
					// error = "1";
				}
				if (object.toString().equals("-500")) {
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
		requestUtil.getBindOrnotInfo();
	}

	/**
	 * 重新登录，刷新数据。
	 */
	public void doAutoLogin() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0) {
			@Override
			public void responseCallback(JSONObject reponseJson) {
				String retult = AppTools.doLogin(reponseJson);
				if ("0".equals(retult)) {
					tv_freeze.setText("¥ " + AppTools.user.getBalance() + "元");
					earning_text
							.setText(AppTools.user.getTotalWinMoney() + "元");
					lottery_money_text.setText(AppTools.user.getHandselAmount()
							+ "元");
					if (AppTools.user.getName().equals(
							AppTools.user.getMobile())) {
						center_tv_name.setText(AppTools.user.getName()
								+ "[修改昵称]");
					} else {
						center_tv_name.setText(AppTools.user.getName());
					}
					SharedPreferences settings = context.getSharedPreferences(
							"app_user", 0);
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
				}
			}

			@Override
			public void responseError(VolleyError error) {
				RequestParams.convertError(context, error, true);
			}
		};
		requestUtil.doAutoLogin();
	}

	/**
	 * 查询未读信息
	 */
	public void getNewsCount() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"加载中...", 0) {
			String error = "-1";

			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, " 查询未读信息result：" + object);
				try {
					error = object.getString("error");
					if (error.equals("0")) {
						msgCount = object.getLong("msgCount");
					}
				} catch (Exception e) {
					MyToast.getToast(context, "数据解析异常!");
				}
				countHandler.sendEmptyMessage(Integer.parseInt(error));
				if (object.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时！");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误...");
			}
		};
		requestUtil.getNewsCount();
	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (isAdded()) {
					if (msgCount != 0) {
						center_text_msg.setText("您有" + msgCount + "条未读消息");
						center_text_msg.setTextColor(getResources().getColor(
								R.color.main_red_new));
					} else {
						center_text_msg.setText("系统消息");
						center_text_msg.setTextColor(getResources().getColor(
								R.color.gray2));
					}
				}
				break;
			case -1:
				if (isAdded()) {
					center_text_msg.setText("系统消息");
					center_text_msg.setTextColor(getResources().getColor(
							R.color.gray2));
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
}
