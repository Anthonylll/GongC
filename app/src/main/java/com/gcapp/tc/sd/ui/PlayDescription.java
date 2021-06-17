package com.gcapp.tc.sd.ui;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：各个彩种的玩法说明公用类
 * 
 * @author lenovo
 */
public class PlayDescription extends Activity {
	private Context context = PlayDescription.this;
	private final static String TAG = "PlayDescription";
	private ImageButton btn_back; // 返回
	private ImageView iv_up_down;// 玩法提示图标
	private TextView btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private TextView tv_Play;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_play_description);
		init();
	}

	/**
	 * 提交委托投注规则详情的请求
	 */
	public void getBetRule() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			private String Message;

			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "委托投注规则请求结果" + object);
				String error = object.optString("error");
				// String serverTime = object.optString("serverTime");
				String investAgreement = null;
				try {
					investAgreement = URLDecoder.decode(
							object.optString("investAgreement"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				Message = Html.fromHtml(investAgreement).toString();
				if (error.equals("0")) {
					tv_Play.setText(Message);
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
		requestUtil.commit_rule();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		type = getIntent().getIntExtra("type", 1);
		tv_Play = (TextView) findViewById(R.id.number_sv_Play);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		btn_playtype = (TextView) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		btn_help.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PlayDescription.this.finish();// 返回
			}
		});
		String LotterID = AppTools.lottery.getLotteryID();
		String lotteryName = AppTools.lottery.getLotteryName();
		String strFilePath = "";
		if (type == 0) {
			btn_playtype.setText("委托投注规则");
			getBetRule();
			// MyAsynTask myAsynTask = new MyAsynTask();
			// myAsynTask.execute();
		} else {
			btn_playtype.setText(lotteryName + "玩法说明");
			strFilePath = "play/" + LotterID + ".txt";
			String Play = ReadTxtFile(strFilePath);
			tv_Play.setText(Play);
		}
	}

	/**
	 * 根据文件路径读取文本
	 * 
	 * @param StrFile
	 * @return
	 */
	public String ReadTxtFile(String StrFile) {
		String res = "";
		try {
			// 读取raw文件夹中的txt文件,将它放入输入流中
			// InputStream in = getResources().openRawResource(R.raw.ansi);
			// 读取assets文件夹中的txt文件,将它放入输入流中
			InputStream in = getResources().getAssets().open(StrFile);
			// 获得输入流的长度
			int length = in.available();
			// 创建字节输入
			byte[] buffer = new byte[length];
			// 放入字节输入中
			in.read(buffer);
			// 获得编码格式
			res = EncodingUtils.getString(buffer, "UTF-8");
			// 关闭输入流
			in.close();
		} catch (Exception e) {
			Log.i("x", "错误" + e.getMessage());
		}
		return res;
	}
}
