package com.gcapp.tc.sd.ui;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.fragment.FollowFragment;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.adapter.MyFollowNumberAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.RoundProgressBar;
import com.gcapp.tc.R;

/**
 * 功能：点击合买大厅进入普通彩种的方案详情页面
 * 
 * @author lenovo
 * 
 */
@SuppressLint("HandlerLeak")
public class FollowInfoActivity extends Activity implements OnClickListener {
	private static final String TAG = "FollowInfoActivity";
	private Context context = FollowInfoActivity.this;
	private Schemes schemes;
	private TextView follow_lv_tv_username, tv_yong, tv_numberInfo, tv_title,
			tv_content, tv_userName2,
			follow_lv_tv_lotteryname,
			follow_lv_iv_eachmoney, // 彩种名称// //每份金额
			follow_lv_iv_remain, follow_lv_iv_tatolmoney, tv_playType,
			tv_playType2; // 剩余份数// 总金额
	private ImageView follow_lv_iv_user_record1, follow_lv_iv_user_record2,
			follow_lv_iv_user_record3, follow_lv_iv_user_record4;
	private TextView btn_submit; // 付款
	private LinearLayout btn_info;// 查看详情
	private EditText et_count; // 购买份数
	private TextView follow_detail_tv_remain; // 剩余份数
	private LinearLayout btn_back;// 返回
	private ImageButton ib_back;
	private Intent intent;
	private Bundle bundle;
	private int buyShare = 1, remainShare = -1, schedule = 1;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	// private ImageView img_lottery;
	private RoundProgressBar RoundPr;
	private ImageButton ib_schemeinfo, ib_betinfo;
	private LinearLayout ll_schemeInfo, ll_betInfo;
	private int baodiPercent = 0;// 保底百分百数
	private TextView follow_lv_tv_baodi;

	private TextView tv_totalmoney;
	private TextView tv_rengouinfo;
	private TextView tv_state;
	private TextView tv_playtype;

	// 非竞彩的选号详情
	private MyListView2 listView;
	private MyFollowNumberAdapter fAdapter;
	private TextView tv_touzhu;// 显示注数
	private LinearLayout ll_betinfo_detail, ll_follow1, ll_follow2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_followinfo2);
		App.activityS.add(this);
		findView();
		init();
		setListener();
	}

	/**
	 * 初始化自定义控件
	 */
	private void findView() {
		ll_follow1 = (LinearLayout) this.findViewById(R.id.ll_follow1);
		ll_follow2 = (LinearLayout) this.findViewById(R.id.ll_follow2);
		ib_schemeinfo = (ImageButton) this.findViewById(R.id.ib_follow1);
		ib_betinfo = (ImageButton) this.findViewById(R.id.ib_follow2);
		ll_betinfo_detail = (LinearLayout) this
				.findViewById(R.id.ll_betinfo_detail);
		ll_schemeInfo = (LinearLayout) this.findViewById(R.id.ll_schemeInfo);
		ll_betInfo = (LinearLayout) this.findViewById(R.id.ll_betInfo);
		tv_totalmoney = (TextView) this.findViewById(R.id.info_tv_totalMoney);// 总金额
		tv_rengouinfo = (TextView) this.findViewById(R.id.info_tv_rengouInfo);// 认购人次
		tv_state = (TextView) this.findViewById(R.id.info_tv_state);// 状态
		tv_playtype = (TextView) this.findViewById(R.id.info_tv_passtype);// 过关方式
		follow_lv_tv_baodi = (TextView) this
				.findViewById(R.id.follow_lv_tv_baodi);// 保底百分百
		follow_lv_tv_lotteryname = (TextView) this
				.findViewById(R.id.follow_lv_tv_lotteryname);
		follow_lv_iv_eachmoney = (TextView) this
				.findViewById(R.id.follow_lv_iv_eachmoney);
		follow_lv_iv_remain = (TextView) this
				.findViewById(R.id.follow_lv_iv_remain);
		follow_lv_iv_tatolmoney = (TextView) this
				.findViewById(R.id.follow_lv_iv_tatolmoney);
		follow_lv_tv_username = (TextView) this
				.findViewById(R.id.follow_lv_tv_username);
		RoundPr = (RoundProgressBar) this.findViewById(R.id.RoundProgressBar);
		tv_userName2 = (TextView) this.findViewById(R.id.info_tv_userName2);
		tv_yong = (TextView) this.findViewById(R.id.info_tv_yongjin2);
		tv_title = (TextView) this.findViewById(R.id.info_tv_title2);
		tv_content = (TextView) this.findViewById(R.id.info_tv_content2);
		tv_playType = (TextView) this.findViewById(R.id.follow_tv_playType);
		tv_playType2 = (TextView) this.findViewById(R.id.follow_tv_playType2);
		follow_detail_tv_remain = (TextView) this
				.findViewById(R.id.follow_detail_tv_remain);
		btn_back = (LinearLayout) this.findViewById(R.id.btn_back);
		ib_back = (ImageButton) this.findViewById(R.id.ib_back);
		follow_lv_iv_user_record1 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record1);
		follow_lv_iv_user_record2 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record2);
		follow_lv_iv_user_record3 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record3);
		follow_lv_iv_user_record4 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record4);
		et_count = (EditText) this.findViewById(R.id.et_count);
		btn_info = (LinearLayout) this.findViewById(R.id.btn_numberInfo);
		btn_submit = (TextView) this.findViewById(R.id.btn_submit);
	}

	/**
	 * 初始化属性
	 */
	private void init() {
		intent = getIntent();
		bundle = intent.getBundleExtra("bundle");
		schemes = (Schemes) bundle.getSerializable("schem");
		follow_lv_tv_username.setText(schemes.getInitiateName());
		tv_userName2.setText(schemes.getInitiateName());
		tv_yong.setText((int) (schemes.getSchemeBonusScale() * 100) + "%");
		follow_lv_tv_lotteryname.setText(schemes.getLotteryName());

		int remain = schemes.getSurplusShare();
		follow_detail_tv_remain.setText(remain + "");
		follow_lv_iv_remain.setText(remain + "份");
		follow_lv_iv_eachmoney.setText(schemes.getShareMoney() + "元");
		follow_lv_iv_tatolmoney.setText(schemes.getMoney() + "元");
		tv_playType2.setText(schemes.getPlayTypeName());

		baodiPercent = (int) (schemes.getAssureMoney() / schemes.getMoney() * 100);
		RoundPr.setjidu(schemes.getSchedule(), 0); // 方案进度 保底进度 半径
		follow_lv_tv_baodi.setText("保" + baodiPercent + "%");
		tv_totalmoney.setText(schemes.getTotalmoney() + "元");
		tv_rengouinfo.setText(schemes.getRengou_peoplesum() + "人，共"
				+ schemes.getRengou_money() + "元");
		if (!"".equals(schemes.getFollow_state())) {
			tv_state.setText(schemes.getFollow_state() + "");
			tv_state.setBackgroundResource(R.color.pur);
		} else {
			tv_state.setBackgroundResource(R.color.white);
		}
		tv_playtype.setText(schemes.getPlaytypeName() + "  "
				+ schemes.getMultiple() + "倍");
		// 先让奖牌不可见
		follow_lv_iv_user_record1.setVisibility(View.GONE);
		follow_lv_iv_user_record2.setVisibility(View.GONE);
		follow_lv_iv_user_record3.setVisibility(View.GONE);
		follow_lv_iv_user_record4.setVisibility(View.GONE);

		if (schemes.getLevel() == 0)
			follow_lv_iv_user_record1.setVisibility(View.VISIBLE);
		else if (schemes.getLevel() != 1) {
			follow_lv_iv_user_record1.setVisibility(View.VISIBLE);

			int level = schemes.getLevel();
			if (9999 < level) {// 1000以上
				setxin(follow_lv_iv_user_record1, follow_lv_iv_user_record2,
						follow_lv_iv_user_record3, follow_lv_iv_user_record4,
						View.VISIBLE);
				follow_lv_iv_user_record1
						.setBackgroundResource(AppTools.level_crown_list.get(8));
				follow_lv_iv_user_record2
						.setBackgroundResource(AppTools.level_cup_list.get(8));
				follow_lv_iv_user_record3
						.setBackgroundResource(AppTools.level_medal_list.get(8));
				follow_lv_iv_user_record4
						.setBackgroundResource(AppTools.level_star_list.get(8));
			} else if (level <= 9999 && level > 999) {// 含皇冠,奖杯，奖牌，星星
				setxin(follow_lv_iv_user_record1, follow_lv_iv_user_record2,
						follow_lv_iv_user_record3, follow_lv_iv_user_record4,
						View.VISIBLE);
				int crown = level / 1000;// 皇冠个数
				follow_lv_iv_user_record1
						.setBackgroundResource(AppTools.level_crown_list
								.get(crown - 1));
				int cup = (level - 1000 * crown) / 100;// 奖杯个数
				// 从第二个图标开始，有可能取0个，需要进行判断，往下同理
				if (cup != 0) {
					follow_lv_iv_user_record2
							.setBackgroundResource(AppTools.level_cup_list
									.get(cup - 1));
				} else {
					follow_lv_iv_user_record2.setVisibility(View.GONE);
				}
				int medal = (level - 1000 * crown - cup * 100) / 10;// 奖杯个数
				if (medal != 0) {
					follow_lv_iv_user_record3
							.setBackgroundResource(AppTools.level_medal_list
									.get(medal - 1));
				} else {
					follow_lv_iv_user_record3.setVisibility(View.GONE);
				}
				int star = level - 1000 * crown - cup * 100 - medal * 10;// 星星个数
				if (star != 0) {
					follow_lv_iv_user_record4
							.setBackgroundResource(AppTools.level_star_list
									.get(star - 1));
				} else {
					follow_lv_iv_user_record4.setVisibility(View.GONE);
				}
			} else if (level <= 999 && level > 99) {// 含奖杯，奖牌，星星
				follow_lv_iv_user_record2.setVisibility(View.VISIBLE);
				follow_lv_iv_user_record3.setVisibility(View.VISIBLE);
				follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
				int cup = level / 100;// 奖杯个数
				follow_lv_iv_user_record2
						.setBackgroundResource(AppTools.level_cup_list
								.get(cup - 1));
				int medal = (level - cup * 100) / 10;// 奖杯个数
				if (medal != 0) {
					follow_lv_iv_user_record3
							.setBackgroundResource(AppTools.level_medal_list
									.get(medal - 1));
				} else {
					follow_lv_iv_user_record3.setVisibility(View.GONE);
				}
				int star = level - cup * 100 - medal * 10;// 星星个数
				if (star != 0) {
					follow_lv_iv_user_record4
							.setBackgroundResource(AppTools.level_star_list
									.get(star - 1));
				} else {
					follow_lv_iv_user_record4.setVisibility(View.GONE);
				}
			} else if (level <= 99 && level > 9) {// 含奖牌，星星
				follow_lv_iv_user_record3.setVisibility(View.VISIBLE);
				follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
				int medal = level / 10;// 奖杯个数
				follow_lv_iv_user_record3
						.setBackgroundResource(AppTools.level_medal_list
								.get(medal - 1));
				int star = level - medal * 10;// 星星个数
				if (star != 0) {
					follow_lv_iv_user_record4
							.setBackgroundResource(AppTools.level_star_list
									.get(star - 1));
				} else {
					follow_lv_iv_user_record4.setVisibility(View.GONE);
				}
			} else if (level >= 1) {
				follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
				follow_lv_iv_user_record4
						.setBackgroundResource(AppTools.level_star_list
								.get(level - 1));
			}
		}
		btn_info.setVisibility(View.VISIBLE);
		tv_touzhu = (TextView) this.findViewById(R.id.tv_touzhu);
		// tv_touzhu.setText("投注（" + schemes.getCountNotes() + "注）");
		listView = (MyListView2) this.findViewById(R.id.followinfo_jc_listView);
		fAdapter = new MyFollowNumberAdapter(this, schemes.getBuyContent(),
				schemes.getMultiple());
		listView.setAdapter(fAdapter);

		fAdapter.notifyDataSetChanged();
		tv_title.setText(schemes.getTitle());
		tv_content.setText(schemes.getDescription());
		opt = "12";
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(getApplicationContext());
	}

	/**
	 * 设置奖牌
	 * 
	 * @param v
	 *            ：第一颗奖牌
	 * @param v1
	 *            ：第2颗奖牌
	 * @param v2
	 *            ：3颗奖牌
	 * @param v3
	 *            ：第4颗奖牌
	 * @param visible
	 *            :控制奖牌显示
	 */
	public void setxin(View v, View v1, View v2, View v3, int visible) {
		v.setVisibility(visible);
		v1.setVisibility(visible);
		v2.setVisibility(visible);
		v3.setVisibility(visible);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_info.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		ib_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		ib_schemeinfo.setOnClickListener(this);
		ib_betinfo.setOnClickListener(this);
		ll_follow1.setOnClickListener(this);
		ll_follow2.setOnClickListener(this);
		et_count.addTextChangedListener(watcher);
	}

	/**
	 * 监听购买份数的文本值的改变
	 * 
	 */
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			if (edt.toString().trim().length() != 0) {
				if (Integer.parseInt(edt.toString().trim()) > schemes
						.getSurplusShare()) {
					et_count.setText(schemes.getSurplusShare() + "");
					MyToast.getToast(getApplicationContext(),
							"最多购买" + schemes.getSurplusShare() + "份");
				}
				if (edt.toString().substring(0, 1).equals("0")) {
					et_count.setText(edt.toString().substring(1,
							edt.toString().length()));
				}
			}
			show();
		}
	};

	/**
	 * 公用按钮点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 查看认购列表详情 **/
		case R.id.btn_numberInfo:
			betInfo();
			break;
		/** 付款 **/
		case R.id.btn_submit:
			dopay();
			break;
		/** 点击方案信息的下拉列表 **/
		case R.id.ib_follow1:
		case R.id.ll_follow1:
			if (!ib_schemeinfo.isSelected()) {
				ib_schemeinfo.setSelected(true);
				ll_schemeInfo.setVisibility(View.GONE);

			} else {
				ib_schemeinfo.setSelected(false);
				ll_schemeInfo.setVisibility(View.VISIBLE);
			}
			break;

		/** 点击投注信息的下拉列表 **/
		case R.id.ib_follow2:
		case R.id.ll_follow2:
			if (!ib_betinfo.isSelected()) {
				ib_betinfo.setSelected(true);
				ll_betinfo_detail.setVisibility(View.GONE);
			} else {
				ib_betinfo.setSelected(false);
				ll_betinfo_detail.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.btn_back:// 返回
		case R.id.tv_back:
		case R.id.ib_back:
			FollowInfoActivity.this.finish();
			break;
		}
	}

	/**
	 * 显示购买份数
	 */
	private void show() {
		if (et_count.getText().toString().trim().length() == 0)
			buyShare = 1;
		else
			buyShare = Integer.parseInt(et_count.getText().toString().trim());
	}

	/**
	 * 购买付款
	 */
	private void dopay() {
		if (AppTools.user != null) {
			commit();
		} else {
			MyToast.getToast(FollowInfoActivity.this, "请先登陆");
			Intent intent = new Intent(FollowInfoActivity.this,
					LoginActivity.class);
			intent.putExtra("loginType", "bet");
			FollowInfoActivity.this.startActivity(intent);
		}
	}

	/**
	 * 提交购买请求
	 */
	public void commit() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在支付...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "加入合买结果---" + object);
				if (null != object) {
					try {
						if ("0".equals(object.optString("error"))) {
							if (RequestUtil.DEBUG)
								Log.i(TAG, "加入合买成功  ");
							AppTools.user.setBalance(object.getLong("balance"));
							AppTools.user.setFreeze(object.getDouble("freeze"));
							remainShare = object.getInt("remainShare");
							schedule = object.getInt("currentSchedule");
							MyToast.getToast(FollowInfoActivity.this, "加入合买成功");
							for (int i = 0; i < FollowFragment.listSchemes
									.size(); i++) {
								if (FollowFragment.listSchemes.get(i).getId()
										.equals(schemes.getId())) {
									FollowFragment.listSchemes.get(i)
											.setSurplusShare(remainShare);
									FollowFragment.listSchemes.get(i)
											.setSchedule(schedule);
									if (schedule == 100)
										FollowFragment.listSchemes.remove(i);
								}
							}
							FollowInfoActivity.this.finish();
						} else if ("-115".equals(object.optString("error"))) {
							String msg = object.optString("msg");
							Toast.makeText(FollowInfoActivity.this, msg,
									Toast.LENGTH_SHORT);
							intent = new Intent(FollowInfoActivity.this,
									SelectRechargeTypeActivity.class);
							FollowInfoActivity.this.startActivity(intent);
						} else {
							String msg = object.optString("msg");
							MyToast.getToast(context, msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
						MyToast.getToast(context, "很抱歉,系统异常...");
						if (RequestUtil.DEBUG)
							Log.e(TAG, e.getMessage());
					}
				} else {
					MyToast.getToast(context, "很抱歉,系统异常...");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "很抱歉,系统异常...");
				if (RequestUtil.DEBUG)
					Log.e(TAG, error.getMessage());
			}
		};
		requestUtil.joinFollow(schemes.getId(), buyShare,
				schemes.getShareMoney());
	}

	/**
	 * 查看认购列表
	 */
	private void betInfo() {
		intent = new Intent(FollowInfoActivity.this,
				FollowPurchase_Activity.class);
		intent.putExtra("bundle", bundle);
		FollowInfoActivity.this.startActivity(intent);
	}

}
