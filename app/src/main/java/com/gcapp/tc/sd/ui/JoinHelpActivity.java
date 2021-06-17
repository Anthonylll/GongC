package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.gcapp.tc.R;

/**
 * 功能：合买帮助类
 * 
 * @author SLS003
 */
public class JoinHelpActivity extends Activity {
	private ImageButton btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_help);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				JoinHelpActivity.this.finish();
			}
		});
	}

}
