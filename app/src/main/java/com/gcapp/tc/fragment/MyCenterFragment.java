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
 * ????????????
 */
@SuppressLint("NewApi")
public class MyCenterFragment extends Fragment implements OnClickListener {

	private String resultStr = ""; // ????????????????????????
	private String auth, time, imei, crc; // ?????????????????????
	private String info;
	private Bitmap bitmap_loadimage;
	private MyAsynTask_loadImage task;
	private MyHandler_image mHandler_image;
	private ProgressDialog proDialog;
	private CircleImg img_userlogo;// ??????
	private SelectPicPopupWindow menuWindow; // ?????????????????????????????????
	private static final int REQUESTCODE_PICK = 0; // ??????????????????
	private static final int REQUESTCODE_TAKE = 1; // ??????????????????
	private static final int REQUESTCODE_CUTTING = 2; // ??????????????????
	private MyHandler3 mHandler_photo;
	private String myMsg;
	private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// ??????????????????
	private String urlpath; // ??????????????????

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
		// ???????????????????????????
		View v = inflater.inflate(R.layout.activity_personal_center, container,
				false);
		mHandler_image = new MyHandler_image();
		mHandler_photo = new MyHandler3();
		findView(v);
		setListener();
		return v;
	}

	/**
	 * ?????????UI??????
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
	 * ????????????
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
	 * ?????????????????????????????????
	 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			// ??????
			case R.id.takePhotoBtn:
				Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// ???????????????????????????????????????????????????????????????
				takeIntent
						.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										IMAGE_FILE_NAME)));
				startActivityForResult(takeIntent, REQUESTCODE_TAKE);
				break;
			// ??????????????????
			case R.id.pickPhotoBtn:
				Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
				// ?????????????????????????????????????????????????????????????????????????????????"image/jpeg ??? image/png????????????"
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
	 * ????????????????????????
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE_PICK:// ?????????????????????
			try {
				startPhotoZoom(data.getData());
			} catch (NullPointerException e) {
				e.printStackTrace();// ????????????????????????
			}
			break;

		case REQUESTCODE_TAKE:// ??????????????????
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/" + IMAGE_FILE_NAME);
			startPhotoZoom(Uri.fromFile(temp));
			break;

		case REQUESTCODE_CUTTING:// ????????????????????????
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ?????????????????????????????????
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
		// ????????????????????????
		MyAsynTask_Photo task = new MyAsynTask_Photo();
		task.execute();
	}

	/** ???????????? */
	class MyAsynTask_Photo extends AsyncTask<Integer, Integer, String> {
		String error = "-11";

		@Override
		protected String doInBackground(Integer... params) {
			init();

			Map<String, String> textParams = new HashMap<String, String>();
			Map<String, File> fileparams = new HashMap<String, File>();
			try {
				// ????????????URL??????
				URL url = new URL(AppTools.path);
				textParams = new HashMap<String, String>();
				fileparams = new HashMap<String, File>();
				// ????????????????????????
				File file = new File(urlpath);
				textParams.put("opt", "77");
				textParams.put("auth", auth);
				fileparams.put("info", file);
				// ??????HttpURLConnection????????????????????????????????????
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// ?????????????????????????????????????????????,??????????????????,Android?????????????????????????????????????????????????????????
				conn.setConnectTimeout(5000);
				// ???????????????????????????POST?????????????????????????????????
				conn.setDoOutput(true);
				// ????????????POST???????????????
				conn.setRequestMethod("POST");
				// ?????????????????????????????????????????????
				conn.setUseCaches(false);
				conn.setRequestProperty("Charset", "UTF-8");// ????????????
				// ????????????HttpURLConnection?????????setRequestProperty()??????,????????????HTML?????????
				conn.setRequestProperty("ser-Agent", "Fiddler");
				// ??????contentType
				conn.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + NetUtil.BOUNDARY);

				OutputStream os = conn.getOutputStream();
				DataOutputStream ds = new DataOutputStream(os);
				NetUtil.writeStringParams(textParams, ds);
				NetUtil.writeFileParams(fileparams, ds);
				NetUtil.paramsEnd(ds);
				// ?????????????????????,?????????????????????
				os.close();
				// ???????????????????????????
				int code = conn.getResponseCode(); // ???Internet????????????,????????????,?????????????????????????????????
				// ????????????????????????
				if (code == 200) {// ??????????????????200,?????????
					// ??????????????????????????????
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
					MyToast.getToast(context, "??????URL?????????");
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
	 * ??????HttpUrlConnection??????post?????????????????? ??????????????????????????????????????? ????????????
	 * ??????????????????????????????????????????????????????????????????????????????????????????????????????
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
				MyToast.getToast(context, "?????????????????????");
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
	 * ????????????????????????
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true?????????????????????Intent??????????????????VIEW?????????
		intent.putExtra("crop", "true");
		// aspectX aspectY ??????????????????
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY ?????????????????????
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
	 * ??????????????????
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
				MyToast.getToast(getActivity(), "??????????????????" + withdrawMoney
						+ "?????????????????????");
			} else {
				if (null == AppTools.user.getRealityName()
						|| AppTools.user.getRealityName().length() == 0) {
					MyToast.getToast(context, "???????????????????????????");
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
		case R.id.img_userlogo:// ????????????
			menuWindow = new SelectPicPopupWindow(context, itemsOnClick);
			menuWindow.showAtLocation(img_userlogo, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.setting_layout:
			intent = new Intent(context, SettingActivity.class);
			context.startActivity(intent);
			break;
		case R.id.integral_layout:// ????????????
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
	 * ???????????????
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
	 * ??????????????????
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
			tv_freeze.setText("?? " + AppTools.user.getBalance() + "???");
			earning_text.setText(AppTools.user.getTotalWinMoney() + "???");
			lottery_money_text.setText(AppTools.user.getHandselAmount() + "???");
			if (AppTools.user.getName().equals(AppTools.user.getMobile())) {
				center_tv_name.setText(AppTools.user.getName() + "[????????????]");
			} else {
				center_tv_name.setText(AppTools.user.getName());
			}
		}
		if (null != AppTools.user && !"".equals(AppTools.user)
				&& null != AppTools.user.getImage_url()
				&& !"".equals(AppTools.user.getImage_url())) {
			System.out.println("====??????URL========="
					+ AppTools.user.getImage_url());
			task = new MyAsynTask_loadImage();
			task.execute();
		}
		super.onResume();
	}

	/**
	 * ???????????? ????????????????????????
	 * 
	 */
	class MyAsynTask_loadImage extends AsyncTask<Void, Integer, String> {
		/** ???????????????????????? */
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
	 * ????????????????????????
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
			case 11:// ??????
				img_userlogo.setImageBitmap(bitmap_loadimage);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * ???????????????????????????
	 */
	public void getIsBindOrNot() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"????????????...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "????????????????????????" + object);
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
						MyToast.getToast(context, "????????????????????????");
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
					MyToast.getToast(context, "????????????");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "?????????????????????????????????..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "????????????" + error.getMessage());
			}
		};
		requestUtil.getBindOrnotInfo();
	}

	/**
	 * ??????????????????????????????
	 */
	public void doAutoLogin() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0) {
			@Override
			public void responseCallback(JSONObject reponseJson) {
				String retult = AppTools.doLogin(reponseJson);
				if ("0".equals(retult)) {
					tv_freeze.setText("?? " + AppTools.user.getBalance() + "???");
					earning_text
							.setText(AppTools.user.getTotalWinMoney() + "???");
					lottery_money_text.setText(AppTools.user.getHandselAmount()
							+ "???");
					if (AppTools.user.getName().equals(
							AppTools.user.getMobile())) {
						center_tv_name.setText(AppTools.user.getName()
								+ "[????????????]");
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
						// ?????????????????? ??????
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
	 * ??????????????????
	 */
	public void getNewsCount() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"?????????...", 0) {
			String error = "-1";

			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, " ??????????????????result???" + object);
				try {
					error = object.getString("error");
					if (error.equals("0")) {
						msgCount = object.getLong("msgCount");
					}
				} catch (Exception e) {
					MyToast.getToast(context, "??????????????????!");
				}
				countHandler.sendEmptyMessage(Integer.parseInt(error));
				if (object.toString().equals("-500")) {
					MyToast.getToast(context, "???????????????");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "?????????????????????????????????...");
			}
		};
		requestUtil.getNewsCount();
	}

	/**
	 * ?????????????????????
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (isAdded()) {
					if (msgCount != 0) {
						center_text_msg.setText("??????" + msgCount + "???????????????");
						center_text_msg.setTextColor(getResources().getColor(
								R.color.main_red_new));
					} else {
						center_text_msg.setText("????????????");
						center_text_msg.setTextColor(getResources().getColor(
								R.color.gray2));
					}
				}
				break;
			case -1:
				if (isAdded()) {
					center_text_msg.setText("????????????");
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
