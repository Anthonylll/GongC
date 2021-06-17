package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.sd.ui.adapter.BaseBetAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import java.util.List;

import butterknife.OnCheckedChanged;
import butterknife.OnTextChanged;

/**
 * 投注的基本类
 * 
 * @author lenovo
 * 
 */
public abstract class BaseBetActivity extends Activity {
	/* 头部 */
	RelativeLayout layout_top_select;// 顶部布局
	ImageButton btn_back; // 返回
	LinearLayout layout_select_playtype;// 玩法选择
	ImageView iv_up_down;// 玩法提示图标
	Button btn_playtype;// 玩法
	ImageButton btn_help;// 帮助

	/* 尾部 */
	TextView btn_follow; // 发出合买
	TextView btn_clearall;
	Button btn_submit; // 选好了
	TextView tv_tatol_count;// 总注数
	TextView tv_tatol_money;// 总钱数

	// 期数倍数追号追加
	EditText et_qi;
	EditText et_bei;
	CheckBox bet_cb_stopfollow;
	LinearLayout layout_zhuijia;
	CheckBox bet_cb_zhuijia;

	// 中间
	LinearLayout btn_continue_select;
	LinearLayout btn_automatic_select;
	ScrollView bet_sv;
	MyListView2 bet_lv_scheme;
	CheckBox bet_cb_xieyi;
	TextView bet_tv_guize;
	ImageView bet_btn_deleteall;

	protected List<SelectedNumbers> listSchemes;
	protected BaseBetAdapter baseBetAdapter;
	protected ConfirmDialog dialog;
	protected int isStopChase = 1; // 中奖后是否停止追号
	protected int bei = 1;
	protected int qi = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_bet);
		initALL(getIntent());
	}

	/**
	 * 初始化控件和数据，监听事件
	 * 
	 * @param intent
	 */
	private void initALL(Intent intent) {
		initView();
		setListener();
		App.activityS.add(this);
		App.activityS1.add(this);
		setInitView(currentLotteryName());
		listSchemes = parseParamsToList(intent);
		baseBetAdapter = new BaseBetAdapter(this, listSchemes);
		bet_lv_scheme.setAdapter(baseBetAdapter);
		bet_lv_scheme.scrollTo(0, 0);
		dialog = new ConfirmDialog(this, R.style.dialog);
		updateCountShow();
	}

	/**
	 * 初始化UI控件
	 */
	private void initView() {
		/* 头部 */
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);// 顶部布局

		btn_back = (ImageButton) findViewById(R.id.btn_back); // 返回

		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);// 玩法选择

		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);// 玩法提示图标

		btn_playtype = (Button) findViewById(R.id.btn_playtype);// 玩法

		btn_help = (ImageButton) findViewById(R.id.btn_help);// 帮助

		/* 尾部 */
		btn_follow = (TextView) findViewById(R.id.btn_follow); // 发出合买
		btn_clearall = (TextView) findViewById(R.id.btn_clearall);
		btn_submit = (Button) findViewById(R.id.btn_submit); // 选好了
		tv_tatol_count = (TextView) findViewById(R.id.tv_tatol_count);// 总注数
		tv_tatol_money = (TextView) findViewById(R.id.tv_tatol_money);// 总钱数

		// 期数倍数追号追加
		et_qi = (EditText) findViewById(R.id.et_qi);
		et_bei = (EditText) findViewById(R.id.et_bei);
		bet_cb_stopfollow = (CheckBox) findViewById(R.id.bet_cb_stopfollow);
		layout_zhuijia = (LinearLayout) findViewById(R.id.layout_zhuijia);
		bet_cb_zhuijia = (CheckBox) findViewById(R.id.bet_cb_zhuijia);

		// 中间
		btn_continue_select = (LinearLayout) findViewById(R.id.btn_continue_select);
		btn_automatic_select = (LinearLayout) findViewById(R.id.btn_automatic_select);
		bet_sv = (ScrollView) findViewById(R.id.bet_sv);
		bet_lv_scheme = (MyListView2) findViewById(R.id.bet_lv_scheme);
		bet_cb_xieyi = (CheckBox) findViewById(R.id.bet_cb_xieyi);
		bet_tv_guize = (TextView) findViewById(R.id.bet_tv_guize);
		bet_btn_deleteall = (ImageView) findViewById(R.id.bet_btn_deleteall);

	}

	/**
	 * 重启activity调用
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initALL(intent);
		backNewIntent(intent);
	}

	/**
	 * 将参数解析成List
	 * 
	 * @return 方案list
	 */
	protected abstract List<SelectedNumbers> parseParamsToList(Intent intent);

	protected abstract void backNewIntent(Intent intent);

	/**
	 * 当前彩种名称
	 * 
	 * @return 彩种名称
	 */
	protected abstract String currentLotteryName();

	/**
	 * 发起合买
	 */
	protected abstract void startJoint();

	/**
	 * 付款
	 */
	protected abstract void startPay();

	/**
	 * 继续选号
	 */
	public void clickcontinueSelect(View view) {
		AppTools.list_numbers = listSchemes;
		continueSelect();
	}

	protected abstract void continueSelect();

	/**
	 * 机选一注
	 */
	protected abstract void automaticSelect();

	public abstract void automaticSelect(View view);

	protected abstract void clickListItem(int position);

	/**
	 * 返回按钮监听
	 */
	protected void btn_back() {
		if (hasSelected()) {
			dialog.show();
			dialog.setDialogContent("您退出后号码将会清空！");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						if (AppTools.list_numbers != null) {
							AppTools.list_numbers.clear();
						}
						finish();
					}
				}
			});
		} else {
			if (AppTools.list_numbers != null) {
				AppTools.list_numbers.clear();
			}
			finish();
		}
	}

	public void btn_back(View view) {
		btn_back();
	}

	public void click_guize(View view) {
		Intent intent = new Intent(this, PlayDescription.class);
		intent.putExtra("type", 0);
		startActivity(intent);
	}

	/**
	 * 投注下面的垃圾桶清空事件
	 * 
	 * @param view
	 */
	public void click_deleteall(View view) {
		if (hasSelected()) {
			dialog.show();
			dialog.setDialogContent("是否清空投注单号码");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						listSchemes.clear();
						baseBetAdapter.clear();
						baseBetAdapter.notifyDataSetChanged();
						updateCountShow();
					}
				}
			});
		} else {
			MyToast.getToast(this, "请先选择投注内容！");
		}
	}

	/**
	 * listView的子项点击和是否追号的监听事件
	 */
	private void setListener() {
		bet_lv_scheme.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppTools.list_numbers = listSchemes;
				clickListItem(position);
			}

		});

		bet_cb_stopfollow
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						isStopChase = isChecked ? 1 : 0;
					}
				});

		setTextChangedListener(et_bei, 1);
		setTextChangedListener(et_qi, 0);
	}

	/**
	 * 文本输入监听
	 * 
	 * @param et
	 * @param i
	 */
	private void setTextChangedListener(EditText et, final int i) {
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				OnWhichTextChanged(i, et_bei, s.toString().trim());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	@OnCheckedChanged(R.id.bet_cb_stopfollow)
	protected void select_checkbox(boolean enable) {
		isStopChase = enable ? 1 : 0;
	}

	@OnTextChanged(R.id.et_bei)
	protected void OnBeiTextChanged(CharSequence text) {
		OnWhichTextChanged(1, et_bei, text.toString().trim());
	}

	@OnTextChanged(R.id.et_qi)
	protected void OnQiTextChanged(CharSequence text) {
		OnWhichTextChanged(0, et_qi, text.toString().trim());
	}

	/**
	 * 是否发起追号
	 */
	protected void click_join() {
		if (hasSelected()) {
			if (bei > 1) {
				dialog.show();
				dialog.setDialogContent("发起合买时不能追号，是否只追一期并继续发起合买？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							et_qi.setText("1");
							qi = 1;
							startJoint();
						}
					}
				});
			} else {
				startJoint();
			}
		} else {
			MyToast.getToast(this, "请至少选择一注");
		}
	}

	public void click_join(View view) {
		click_join();
	}

	/**
	 * 付款方法
	 */
	protected void click_pay() {
		if (hasSelected()) {
			if (AppTools.user != null) {
				startPay();
			} else {
				MyToast.getToast(this, "请先登陆");
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				startActivity(intent);
			}
		} else {
			MyToast.getToast(this, "请至少选择一注");
		}
	}

	public void btn_submit(View view) {
		click_pay();
	}

	@Override
	public void onBackPressed() {
		btn_back();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_base_bet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	/**
	 * 初始化界面UI
	 * 
	 * @param lotteryname
	 */
	private void setInitView(String lotteryname) {
		iv_up_down.setVisibility(View.GONE);
		btn_help.setVisibility(View.GONE);
		btn_submit.setText("付款");
		btn_follow.setVisibility(View.VISIBLE);
		btn_clearall.setVisibility(View.GONE);
		btn_playtype.setText(lotteryname + "投注单");
	}

	/**
	 * 期数和倍数的监听
	 * 
	 * @param flag
	 *            :输入类型参数
	 * @param editText
	 *            ：输入的文本框对象
	 * @param changetext
	 *            ：文本框输入值
	 */
	private void OnWhichTextChanged(int flag, EditText editText,
			String changetext) {
		if (changetext.length() != 0) {
			int max = AppTools.lottery.getDtCanChaseIsuses().size();
			if (flag == 1 && Integer.parseInt(changetext) > 999) {
				editText.setSelection(editText.getText().length());
				MyToast.getToast(getApplicationContext(), "最大倍数为999");
				editText.setText("999");
			}
			if (flag == 0 && Integer.parseInt(changetext) > max) {
				editText.setSelection(editText.getText().length());
				MyToast.getToast(getApplicationContext(), "最多可追" + max + "期")
						;
				editText.setText(max + "");
			}
			if (Integer.parseInt(changetext) == 0) {
				editText.setText("1");
				editText.setSelection(editText.getText().length());
				MyToast.getToast(getApplicationContext(),
						flag == 0 ? "至少可追1期" : "最小为1倍");
			}
			if (changetext.substring(0, 1).equals("0")) {
				editText.setText(changetext.subSequence(1, changetext.length()));
				editText.setSelection(0);
			}
		}
		updateCountShow();
	}

	/**
	 * 删除一注
	 * 
	 * @param index
	 *            当前注
	 */
	public void deletOne(int index) {
		listSchemes.remove(index);
		updateCountShow();
	}

	/**
	 * 设置注数和金额
	 * 
	 * @param count
	 *            注数
	 */
	protected void setCountAndMoney(long count, int bei, int qi) {
		tv_tatol_count.setText(count + "");
		tv_tatol_money.setText(count * 2 * bei * qi + "");
	}

	/**
	 * 更新总注数
	 */
	public void updateCountShow() {
		int count = getCount();
		if (et_bei.getText().toString().trim().length() > 0) {
			bei = Integer.parseInt(et_bei.getText().toString().trim());
		}
		if (et_qi.getText().toString().trim().length() > 0) {
			qi = Integer.parseInt(et_qi.getText().toString().trim());
		}
		setCountAndMoney(count, bei, qi);
	}

	private boolean hasSelected() {
		return listSchemes != null && listSchemes.size() > 0;
	}

	/**
	 * 添加一注
	 * 
	 * @param numbers
	 *            实体类
	 */
	protected void addOneSelect(SelectedNumbers numbers) {
		listSchemes.add(0, numbers);
		baseBetAdapter.add(0, numbers);
		baseBetAdapter.notifyDataSetChanged();
		updateCountShow();
	}

	/**
	 * 得到注数
	 * 
	 * @return
	 */
	protected int getCount() {
		int temp = 0;
		if (listSchemes != null) {
			for (SelectedNumbers numbers : listSchemes) {
				temp += numbers.getCount();
			}
		}
		return temp;
	}
}
