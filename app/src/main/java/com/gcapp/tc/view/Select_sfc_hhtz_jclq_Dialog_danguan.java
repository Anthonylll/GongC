package com.gcapp.tc.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.DtMatch_Basketball;
import com.gcapp.tc.fragment.CJLQMatchItemFragment_danguan;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapter_jclq_52_danguan;
import com.gcapp.tc.utils.ColorUtil;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.R;

/**
 * 自定义 过关方式 Dialog
 * 
 * @author SLS003
 */
public class Select_sfc_hhtz_jclq_Dialog_danguan extends Dialog implements
		View.OnClickListener {
	private RelativeLayout rl_sethight;
	private TextView tv_mainteam, tv_guestteam;
	private TextView tv_main, tv_0, tv_rq;
	private TextView tv_rangfen, tv_sumscore;
	private TextView tv_burangfen_mianwin, tv_burangfen_mianlos,
			tv_rangfen_mianwin, tv_rangfen_mianlos;
	private TextView tv_show_burangfen_mianwin, tv_show_burangfen_mianlos,
			tv_show_rangfen_mianwin, tv_show_rangfen_mianlos, tv_show_bigscore,
			tv_show_smallscore;
	private TextView tv_bigscore, tv_smallscore;
	private LinearLayout ll_main_win, ll_main_lose, ll_main_r_win,
			ll_main_r_lose, ll_main_big, ll_main_small;// 点击按钮
	private LinearLayout layout_rangqiu_notrangqiu;// <!-- 让分和非让分-->
	private LinearLayout yszf_, layout_zjq;// <!-- 让分和非让分-->
	private LinearLayout ll_mian_win, ll_mian_Losin;// <!-- 让分和非让分-->
	private LinearLayout ll_frf, ll_rf;// 非让分 和 让分
	private TextView tv_sfc_1s, tv_sfc_2s, tv_sfc_3s, tv_sfc_4s, tv_sfc_5s,
			tv_sfc_6s, tv_sfc_7s, tv_sfc_8s, tv_sfc_9s, tv_sfc_10s, tv_sfc_11s,
			tv_sfc_12s;
	private TextView tv_7, tv_1, tv_8, tv_2, tv_9, tv_3, tv_10, tv_4, tv_11,
			tv_5, tv_12, tv_6;
	TextView[] tv_show_view = { tv_7, tv_1, tv_8, tv_2, tv_9, tv_3, tv_10,
			tv_4, tv_11, tv_5, tv_12, tv_6 };
	TextView[] tv_view = { tv_sfc_1s, tv_sfc_2s, tv_sfc_3s, tv_sfc_4s,
			tv_sfc_5s, tv_sfc_6s, tv_sfc_7s, tv_sfc_8s, tv_sfc_9s, tv_sfc_10s,
			tv_sfc_11s, tv_sfc_12s };
	private LinearLayout tv_sfc_1, tv_sfc_2, tv_sfc_3, tv_sfc_4, tv_sfc_5,
			tv_sfc_6, tv_sfc_7, tv_sfc_8, tv_sfc_9, tv_sfc_10, tv_sfc_11,
			tv_sfc_12;
	LinearLayout[] ll_view = { tv_sfc_1, tv_sfc_2, tv_sfc_3, tv_sfc_4,
			tv_sfc_5, tv_sfc_6, tv_sfc_7, tv_sfc_8, tv_sfc_9, tv_sfc_10,
			tv_sfc_11, tv_sfc_12 };
	int[] ll_r_view = { R.id.tv_sfc_1, R.id.tv_sfc_2, R.id.tv_sfc_3,
			R.id.tv_sfc_4, R.id.tv_sfc_5, R.id.tv_sfc_6, R.id.tv_sfc_7,
			R.id.tv_sfc_8, R.id.tv_sfc_9, R.id.tv_sfc_10, R.id.tv_sfc_11,
			R.id.tv_sfc_12 };
	int[] tv_r_view = { R.id.tv_sfc_1s, R.id.tv_sfc_2s, R.id.tv_sfc_3s,
			R.id.tv_sfc_4s, R.id.tv_sfc_5s, R.id.tv_sfc_6s, R.id.tv_sfc_7s,
			R.id.tv_sfc_8s, R.id.tv_sfc_9s, R.id.tv_sfc_10s, R.id.tv_sfc_11s,
			R.id.tv_sfc_12s };
	int[] tv_show_r_view = { R.id.tv_7, R.id.tv_1, R.id.tv_8, R.id.tv_2,
			R.id.tv_9, R.id.tv_3, R.id.tv_10, R.id.tv_4, R.id.tv_11, R.id.tv_5,
			R.id.tv_12, R.id.tv_6 };

	private int type = 0;// 0表示胜分差 1 表示混合投注
	private int adapterType = 0;
	private DtMatch_Basketball dtMatch_Basketball;

	private Context context;
	private CJLQMatchItemFragment_danguan fragment;
	private Button btn_ok, btn_cancel;
	private List<String> result;
	LinkedHashMap<Integer, String> map;
	private int grouid, index; //

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_jclq_dialog);
		findView();
		init();
		setListener();

	}

	public Select_sfc_hhtz_jclq_Dialog_danguan(Context context) {
		super(context);
		this.context = context;

	}

	public Select_sfc_hhtz_jclq_Dialog_danguan(Context context,
			boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;

	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param fragment
	 *            当前的fragment
	 * @param theme
	 *            dialog主题
	 * @param adapterType
	 *            ExpandAdapter_jclq_52中的playType
	 * @param type
	 *            0胜分差弹出窗 ，1混合投注弹出窗
	 * @param groupId
	 *            父位置
	 * @param index
	 *            子位置
	 * @param DtMatch_Basketball
	 *            数据类
	 * @param result
	 *            已有的选号
	 */
	public Select_sfc_hhtz_jclq_Dialog_danguan(Context context,
			CJLQMatchItemFragment_danguan fragment, int theme, int adapterType,
			int type, int groupId, int index,
			DtMatch_Basketball DtMatch_Basketball,
			LinkedHashMap<Integer, LinkedHashMap<Integer, String>> result) {
		super(context, theme);
		this.context = context;
		this.fragment = fragment;
		this.type = type;
		this.adapterType = adapterType;
		this.dtMatch_Basketball = DtMatch_Basketball;
		this.grouid = groupId;
		this.index = index;
		setresult(result);
	}

	private void init() {
		setshow();
		dateinit();
		showr_rsult();
	}

	public void setresult(LinkedHashMap<Integer, LinkedHashMap<Integer, String>> sult) {
		if (result == null)
			result = new ArrayList<String>();
		if (sult != null) {
			if (sult.get(grouid) != null && sult.get(grouid).get(index) != null) {
				for (int i = 0; i < sult.get(grouid).get(index).split(",").length; i++) {
					result.add(sult.get(grouid).get(index).split(",")[i]);
				}
			}
		}
	}

	private void setshow() {
		tv_mainteam.setText(dtMatch_Basketball.getMainTeam());
		tv_guestteam.setText(dtMatch_Basketball.getGuestTeam());
		switch (type) {
		case 0:
			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) rl_sethight
					.getLayoutParams();
			linearParams.height = (int) (LotteryUtils
					.getScreenWidthAndHeight(context)[1] * 0.6);
			rl_sethight.setLayoutParams(linearParams);
			layout_rangqiu_notrangqiu.setVisibility(View.GONE);
			yszf_.setVisibility(View.GONE);
			layout_zjq.setVisibility(View.GONE);
			yszf_.setVisibility(View.GONE);
			ll_mian_win.setVisibility(View.VISIBLE);
			ll_mian_Losin.setVisibility(View.VISIBLE);
			break;
		case 1:
			RelativeLayout.LayoutParams linearParams1 = (RelativeLayout.LayoutParams) rl_sethight
					.getLayoutParams();
			linearParams1.height = (int) (LotteryUtils
					.getScreenWidthAndHeight(context)[1] * 0.8);
			rl_sethight.setLayoutParams(linearParams1);
			layout_rangqiu_notrangqiu.setVisibility(View.VISIBLE);
			ll_rf.setVisibility(View.VISIBLE);
			ll_frf.setVisibility(View.VISIBLE);
			yszf_.setVisibility(View.VISIBLE);
			layout_zjq.setVisibility(View.VISIBLE);
			ll_mian_win.setVisibility(View.VISIBLE);
			ll_mian_Losin.setVisibility(View.VISIBLE);
			if (!dtMatch_Basketball.isRFSF())
				ll_rf.setVisibility(View.GONE);
			if (!dtMatch_Basketball.isSF())
				ll_frf.setVisibility(View.GONE);
			if (!dtMatch_Basketball.isDXF()) {
				yszf_.setVisibility(View.GONE);
				layout_zjq.setVisibility(View.GONE);
			}
			if (!dtMatch_Basketball.isSFC()) {
				ll_mian_Losin.setVisibility(View.GONE);
				ll_mian_win.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	private void dateinit() {
		tv_view[1].setText(dtMatch_Basketball.getDifferMain1_5());
		tv_view[3].setText(dtMatch_Basketball.getDifferMain6_10());
		tv_view[5].setText(dtMatch_Basketball.getDifferMain11_15());
		tv_view[7].setText(dtMatch_Basketball.getDifferMain16_20());
		tv_view[9].setText(dtMatch_Basketball.getDifferMain21_25());
		tv_view[11].setText(dtMatch_Basketball.getDifferMain26());

		tv_view[0].setText(dtMatch_Basketball.getDifferGuest1_5());
		tv_view[2].setText(dtMatch_Basketball.getDifferGuest6_10());
		tv_view[4].setText(dtMatch_Basketball.getDifferGuest11_15());
		tv_view[6].setText(dtMatch_Basketball.getDifferGuest16_20());
		tv_view[8].setText(dtMatch_Basketball.getDifferGuest21_25());
		tv_view[10].setText(dtMatch_Basketball.getDifferGuest26());
		if (type == 1) {
			String LetScore = dtMatch_Basketball.getLetScore();
			if (Double.parseDouble(LetScore.equals("") ? "0" : LetScore) < 0)
				tv_rangfen.setTextColor(context.getResources().getColor(
						R.color.select_jczq_tvcolor_green));
			tv_main.setText(dtMatch_Basketball.getMainTeam());
			tv_rangfen.setText(dtMatch_Basketball.getLetScore());
			tv_show_burangfen_mianwin.setText("主胜");
			tv_show_burangfen_mianlos.setText("主负");
			tv_show_rangfen_mianwin.setText("主胜");
			tv_show_rangfen_mianlos.setText("主负");
			tv_burangfen_mianwin.setText(dtMatch_Basketball.getMainWin());
			tv_burangfen_mianlos.setText(dtMatch_Basketball.getMainLose());
			tv_rangfen_mianwin.setText(dtMatch_Basketball.getLetMainWin());
			tv_rangfen_mianlos.setText(dtMatch_Basketball.getLetMainLose());
			tv_sumscore.setText(dtMatch_Basketball.getBigSmallScore());
			tv_bigscore.setText(dtMatch_Basketball.getBig());
			tv_smallscore.setText(dtMatch_Basketball.getSmall());
		}
	}

	/** 绑定监听 **/
	private void setListener() {
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		for (int i = 0; i < 12; i++) {
			ll_view[i].setOnClickListener(this);
		}
		if (type == 1) {
			ll_main_win.setOnClickListener(this);
			ll_main_lose.setOnClickListener(this);
			ll_main_r_win.setOnClickListener(this);
			ll_main_r_lose.setOnClickListener(this);
			ll_main_big.setOnClickListener(this);
			ll_main_small.setOnClickListener(this);
		}
	}

	/** 初始化UI **/
	private void findView() {
		rl_sethight = (RelativeLayout) this.findViewById(R.id.rl_sethight);
		layout_rangqiu_notrangqiu = (LinearLayout) this
				.findViewById(R.id.layout_rangqiu_notrangqiu);
		yszf_ = (LinearLayout) this.findViewById(R.id.yszf_);
		layout_zjq = (LinearLayout) this.findViewById(R.id.layout_zjq);
		ll_mian_win = (LinearLayout) this.findViewById(R.id.ll_mian_win);
		ll_mian_Losin = (LinearLayout) this.findViewById(R.id.ll_mian_Losin);
		tv_mainteam = (TextView) this.findViewById(R.id.tv_guestteam);
		tv_guestteam = (TextView) this.findViewById(R.id.tv_mainteam);
		btn_ok = (Button) this.findViewById(R.id.dl_btn_confirm);
		btn_cancel = (Button) this.findViewById(R.id.dl_btn_cancel);
		for (int i = 0; i < 12; i++) {
			tv_view[i] = (TextView) this.findViewById(tv_r_view[i]);
			tv_show_view[i] = (TextView) this.findViewById(tv_show_r_view[i]);
			ll_view[i] = (LinearLayout) this.findViewById(ll_r_view[i]);
		}
		ll_frf = (LinearLayout) this.findViewById(R.id.ll_frf);
		ll_rf = (LinearLayout) this.findViewById(R.id.ll_rf);
		tv_rangfen = (TextView) this.findViewById(R.id.tv_rangfen);
		tv_burangfen_mianwin = (TextView) this
				.findViewById(R.id.tv_burangfen_mianlos);
		tv_burangfen_mianlos = (TextView) this
				.findViewById(R.id.tv_burangfen_mianwin);
		tv_main = (TextView) this.findViewById(R.id.tv_main);
		tv_rangfen_mianwin = (TextView) this
				.findViewById(R.id.tv_rangfen_mianlos);
		tv_rangfen_mianlos = (TextView) this
				.findViewById(R.id.tv_rangfen_mianwin);
		tv_sumscore = (TextView) this.findViewById(R.id.sumscore);
		tv_bigscore = (TextView) this.findViewById(R.id.tv_dx_big);
		tv_show_burangfen_mianwin = (TextView) this.findViewById(R.id.tv_102);
		tv_show_burangfen_mianlos = (TextView) this.findViewById(R.id.tv_101);
		tv_show_rangfen_mianwin = (TextView) this.findViewById(R.id.tv_502);
		tv_show_rangfen_mianlos = (TextView) this.findViewById(R.id.tv_501);
		tv_show_bigscore = (TextView) this.findViewById(R.id.tv_201);
		tv_show_smallscore = (TextView) this.findViewById(R.id.tv_202);
		tv_smallscore = (TextView) this.findViewById(R.id.tv_dx_small);

		ll_main_win = (LinearLayout) this.findViewById(R.id.ll_main_lose);
		ll_main_lose = (LinearLayout) this.findViewById(R.id.ll_main_win);
		ll_main_r_win = (LinearLayout) this.findViewById(R.id.ll_main_r_lose);
		ll_main_r_lose = (LinearLayout) this.findViewById(R.id.ll_main_r_win);
		ll_main_big = (LinearLayout) this.findViewById(R.id.ll_main_big);
		ll_main_small = (LinearLayout) this.findViewById(R.id.ll_main_small);
		tv_0 = (TextView) findViewById(R.id.tv_0);
		tv_rq = (TextView) findViewById(R.id.tv_rq);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dl_btn_confirm:
			getresult();
			this.dismiss();
			break;
		case R.id.dl_btn_cancel:
			this.dismiss();
			break;
		default:
			if (result == null)
				result = new ArrayList<String>();
			for (int i = 0; i < 12; i++) {
				if (v == ll_view[i]) {
					if (result.contains(i + 1 + ""))
						result.remove(i + 1 + "");
					else
						result.add(i + 1 + "");
				}
			}
			if (type == 1) {
				switch (v.getId()) {
				case R.id.ll_main_lose:
					if (result.contains("101"))
						result.remove("101");
					else
						result.add("101");
					break;
				case R.id.ll_main_win:
					if (result.contains("100"))
						result.remove("100");
					else
						result.add("100");
					break;
				case R.id.ll_main_r_lose:
					if (result.contains("201"))
						result.remove("201");
					else
						result.add("201");
					break;
				case R.id.ll_main_r_win:
					if (result.contains("200"))
						result.remove("200");
					else
						result.add("200");
					break;
				case R.id.ll_main_big:
					if (result.contains("301"))
						result.remove("301");
					else
						result.add("301");
					break;
				case R.id.ll_main_small:
					if (result.contains("300"))
						result.remove("300");
					else
						result.add("300");
					break;
				default:
					break;
				}
			}
			showr_rsult();
			break;
		}
	}

	private void showr_rsult() {

		for (int i = 0; i < ll_view.length; i++) {
			ll_view[i]
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			tv_view[i].setTextColor(ColorUtil.BET_GRAY);
			tv_show_view[i].setTextColor(Color.BLACK);
		}
		tv_burangfen_mianwin.setTextColor(ColorUtil.BET_GRAY);
		tv_burangfen_mianlos.setTextColor(ColorUtil.BET_GRAY);
		tv_rangfen_mianwin.setTextColor(ColorUtil.BET_GRAY);
		tv_rangfen_mianlos.setTextColor(ColorUtil.BET_GRAY);
		tv_bigscore.setTextColor(ColorUtil.BET_GRAY);
		tv_smallscore.setTextColor(ColorUtil.BET_GRAY);
		tv_show_burangfen_mianwin.setTextColor(Color.BLACK);
		tv_show_burangfen_mianlos.setTextColor(Color.BLACK);
		tv_show_rangfen_mianwin.setTextColor(Color.BLACK);
		tv_show_rangfen_mianlos.setTextColor(Color.BLACK);
		tv_show_bigscore.setTextColor(Color.BLACK);
		tv_show_smallscore.setTextColor(Color.BLACK);
		ll_main_win
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		ll_main_lose
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		ll_main_r_win
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		ll_main_r_lose
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		ll_main_big
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		ll_main_small
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		for (int i = 0; i < result.size(); i++) {
			// System.out.println("show_result======" + result.get(i));
			if (Integer.parseInt(result.get(i)) < 13) {
				int index = Integer.parseInt(result.get(i)) - 1;
				ll_view[index]
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				tv_view[index].setTextColor(Color.WHITE);
				tv_show_view[index].setTextColor(Color.WHITE);
			} else {
				switch (Integer.parseInt(result.get(i))) {
				case 101:
					ll_main_win
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
					tv_burangfen_mianwin.setTextColor(Color.WHITE);
					tv_show_burangfen_mianwin.setTextColor(Color.WHITE);
					break;
				case 100:
					ll_main_lose
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
					tv_burangfen_mianlos.setTextColor(Color.WHITE);
					tv_show_burangfen_mianlos.setTextColor(Color.WHITE);
					break;
				case 201:
					ll_main_r_win
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
					tv_rangfen_mianwin.setTextColor(Color.WHITE);
					tv_show_rangfen_mianwin.setTextColor(Color.WHITE);
					break;
				case 200:
					ll_main_r_lose
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
					tv_rangfen_mianlos.setTextColor(Color.WHITE);
					tv_show_rangfen_mianlos.setTextColor(Color.WHITE);
					break;
				case 301:
					ll_main_big
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
					tv_bigscore.setTextColor(Color.WHITE);
					tv_show_bigscore.setTextColor(Color.WHITE);
					break;
				case 300:
					ll_main_small
							.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
					tv_smallscore.setTextColor(Color.WHITE);
					tv_show_smallscore.setTextColor(Color.WHITE);
					break;
				default:
					break;
				}
			}
		}
	}

	public void getresult() {
		if (type == 0) {
			map = ExpandAdapter_jclq_52_danguan.map_hashMap_cbf.get(grouid);
			if (map == null) {
				map = new LinkedHashMap<Integer, String>();
			}
			map.remove(index);
			for (int i = 0; i < result.size(); i++) {
				System.out.println("result" + result.get(i));
			}
			String re = "";
			for (int i = 0; i < result.size(); i++) {
				if (i == 0)
					re += result.get(i);
				else
					re += "," + result.get(i);
				map.put(index, re);
			}
			ExpandAdapter_jclq_52_danguan.map_hashMap_cbf.put(grouid, map);
			if (ExpandAdapter_jclq_52_danguan.map_hashMap_cbf.get(grouid)
					.size() == 0)
				ExpandAdapter_jclq_52_danguan.map_hashMap_cbf.remove(grouid);
			fragment.update();
			fragment.onButtonPressed(adapterType, null);
		} else {
			map = ExpandAdapter_jclq_52_danguan.map_hashMap_hhtz.get(grouid);
			if (map == null) {
				map = new LinkedHashMap<Integer, String>();
			}
			map.remove(index);
			for (int i = 0; i < result.size(); i++) {
				System.out.println("result" + result.get(i));
			}
			String re = "";
			for (int i = 0; i < result.size(); i++) {
				if (i == 0)
					re += result.get(i);
				else
					re += "," + result.get(i);
				map.put(index, re);
			}
			ExpandAdapter_jclq_52_danguan.map_hashMap_hhtz.put(grouid, map);
			fragment.update();
			fragment.onButtonPressed(adapterType, null);
		}
	}
}
