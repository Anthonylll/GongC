package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcapp.tc.R;

/**
 * 功能：竞彩奖金优化的帮助类
 * 
 * @author lenovo
 * 
 */
public class BonusHelpActivity extends Activity implements OnClickListener {
	
	private ImageButton btnBonusHelp;
	private TextView description_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bonushelp);
		initViews();
		initData();
	}

	/**
	 * 初始化自定义控件和绑定监听
	 */
	private void initViews() {
		description_text = (TextView) findViewById(R.id.description_text);
		btnBonusHelp = (ImageButton) findViewById(R.id.btn_help_back);
		btnBonusHelp.setOnClickListener(this);
	}
	
	private void initData() {
		Spanned tip = Html
				.fromHtml("<font color='#000000'>一、什么是奖金优化</font><br/>"
						+ "<font color='#000000'>奖金优化就是针对投注的复式倍投神器，用户可以根据自己的需求，自行对单注注数进行分配的功能，我们提供了三种快捷的优化方式：平均优化，博热优化，博冷优化助您合理分配投注倍数，提高中奖收益。</font><br/>"
						+ "<font color='#000000'>平均优化：以方案单注奖金趋于平均的规则进行优化</font><br/>"
						+ "<font color='#000000'>博热优化：在方案单注保本前提下，使概率最高的单注奖金最大化</font><br/>"
						+ "<font color='#000000'>博冷优化：在方案单注保本前提下，使奖金最高的单注奖金最大化</font><br/>"
						+ "<font color='#000000'></font><br/>"
						+ "<font color='#000000'></font>二、奖金优化支持的过关方式<br/>"
						+ "<font color='#000000'></font>(1)最多支持对用户选择的15场比赛串关进行优化。<br/>"
						+ "<font color='#000000'>(2)在平均优化方案无法确保每种组合方案保本的情况下，博热优化和搏冷优化将保持同平均优化方案一致。</font><br/>");
		description_text.setText(tip);
	}

	/**
	 * 公用监听结果处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_help_back:
			BonusHelpActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
