package com.gcapp.tc.sd.ui;

import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2018-12-11 下午2:23:44
 * @version 5.5.0 
 * @Description 人工充值
 */
public class ArtificialPayActivity extends Activity{

	private TextView btn_playtype;
	private ImageButton btn_back;
	private ImageButton btn_help;
	private ImageView iv_up_down;
	private WebView webview_pay;
	private String pUrl = "/Admin/zhifulogo.aspx";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_artificial_pay);
		App.activityS1.add(this);

		initView();
		initListener();
		initData();
	}

	private void initView() {
		webview_pay = (WebView) this.findViewById(R.id.webview_pay);
		btn_playtype = (TextView) this.findViewById(R.id.btn_playtype);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		btn_help = (ImageButton) this.findViewById(R.id.btn_help);
		iv_up_down = (ImageView) this.findViewById(R.id.iv_up_down);
		iv_up_down.setVisibility(View.GONE);
		btn_help.setVisibility(View.GONE);
		btn_playtype.setText("人工充值");
	}

	private void initListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		String mark = getIntent().getStringExtra("mark");
		if(mark.equals("2013")){
			pUrl = "/Admin/WeiXinLogo.aspx";
		}
		webview_pay.getSettings().setJavaScriptEnabled(true);
		webview_pay.loadUrl(AppTools.url+pUrl);
	}
	
	/**
	 * 返回键监听
	 */
	public void onclickBack() {
		for (Activity activity : App.activityS1) {
			activity.finish();
		}
	}

	/**
	 * 重写返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onclickBack();
		}
		return super.onKeyDown(keyCode, event);
	}
}
