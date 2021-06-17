package com.gcapp.tc.sd.ui;

import com.gcapp.tc.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author anthony
 * 跟单玩法说明
 */
public class FollowExplainActivity extends Activity implements OnClickListener {

	private TextView explain_text_one;
	private TextView explain_text_two;
	private TextView explain_text_three;
	private TextView explain_text_four;
	private ImageButton explain_btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_follow_explain);

		initView();
		initListener();
		initData();
	}

	private void initView() {
		explain_text_one = (TextView) findViewById(R.id.explain_text_one);
		explain_text_two = (TextView) findViewById(R.id.explain_text_two);
		explain_text_three = (TextView) findViewById(R.id.explain_text_three);
		explain_text_four = (TextView) findViewById(R.id.explain_text_four);
		explain_btn_back = (ImageButton) findViewById(R.id.explain_btn_back);
	}

	private void initListener() {
		explain_btn_back.setOnClickListener(this);
	}

	private void initData() {
		Spanned tip1 = Html
				.fromHtml("<font >跟单说明：</font><br/>"
						+ "<font ></font><br/>"
						+ "<font >1、本平台注册用户，申请成为大神后即有资格进行发单；</font><br/>"
						+ "<font >2、发单者所发单中奖，并[税后奖金-（税后奖金*发单提成）]>投注金，则有佣金提成；</font><br/>"
						+ "<font >3、几中几：指发单者近七单已开奖发单方案数量及中奖方案数量；</font><br/>"
						+ "<font >4、胜率：指发单者在本平台历史记录中的红单数与总购彩单数的百分比，其中总购彩单数包括“非跟单”订单；</font><br/>"
						+ "<font >5、预计回报：指当前方案的回报倍数。预计回报倍数是根据发单者支付时的赔率计算而来;</font><br/>"
						+ "<font >6、余额不足时，无法使用彩金进行跟单；</font><br/>"
						+ "<font >7、订单排序：根据订单当前的（跟单金额/自购金额）大小进行排序，该数值越大，排的越前；</font><br/>"
						+ "<font >8、发单目前仅支持篮球、篮球单关、足球、足球单关；</font><br/>");
		explain_text_one.setText(tip1);

		Spanned tip2 = Html
				.fromHtml("<font >如何发单：</font><br/>"
						+ "<font ></font><br/>"
						+ "<font >1、选择好比赛场次后点击“选好了”按钮进入付款页面，在该页面左下角，点击“发单”按钮即可进入发单页面；</font><br/>"
						+ "<font >2、发单页面可进行佣金的设置，为0%-10%；方案有公开、跟单可见、开赛可见、永久保密四种保密设置；“方案描述”中填写的内容将在跟单页面进行展示，请谨慎填写；</font><br/>"
						+ "<font >3、付款即完成发单；</font><br/>");
		explain_text_two.setText(tip2);

		Spanned tip3 = Html
				.fromHtml("<font >如何获得佣金：</font><br/>"
						+ "<font ></font><br/>"
						+ "<font >1、发单时，可选择0%-10%的佣金提成</font><br/>"
						+ "<font >2、必须是中奖的跟单，且[税后奖金-（税后奖金*发单提成）]>投注金，才有佣金提成；</font><br/>"
						+ "<font >3、必须有跟单者跟投方案；</font><br/>"
						+ "<font >4、跟单者跟投方案中奖后，则从该方案奖金里扣除发单者所设佣金并返于发单者；</font><br/>");
		explain_text_three.setText(tip3);
		
		Spanned tip4 = Html
				.fromHtml("<font >本规则最终解释权归本平台所有，如有疑问请联系客服"+ "。</font><br/>");
		explain_text_four.setText(tip4);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.explain_btn_back:
			finish();
			break;

		default:
			break;
		}
	}

}
