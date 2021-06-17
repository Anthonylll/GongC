package com.gcapp.tc.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gcapp.tc.sd.ui.AccountInformationActivity;
import com.gcapp.tc.sd.ui.AlterBankCardActivity;
import com.gcapp.tc.sd.ui.WithdrawalActivity;
import com.gcapp.tc.sd.ui.adapter.MySpinnerAdapter;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.R;

import java.util.List;
import java.util.Map;

/**
 * 功能：账户信息的银行卡信息弹出框
 * 
 * @author lenovo
 * 
 */
public class MyBankSpinner extends Dialog {
	private ListView listView;
	private MySpinnerAdapter sAdapter;
	private List<Map<String, String>> listString;
	private Context context;
	private int index;
	private int type; // 类型
	private static String province;

	public MyBankSpinner(Context context, List<Map<String, String>> list,
			int index, int type) {
		super(context);
		init(context, list, index, type);
	}

	public MyBankSpinner(Context context, List<Map<String, String>> list,
			int index, int type, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context, list, index, type);
	}

	public MyBankSpinner(Context context, List<Map<String, String>> list,
			int index, int type, int theme) {
		super(context, theme);
		init(context, list, index, type);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinner_dialog);
		listView = (ListView) this.findViewById(R.id.spinner_dialog_listView);
		sAdapter = new MySpinnerAdapter(context, listString, index);
		listView.setAdapter(sAdapter);
		listView.setOnItemClickListener(new MyItemClickListener());
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 * @param list
	 */
	private void init(Context context, List<Map<String, String>> list,
			int index, int type) {
		this.context = context;
		this.listString = list;
		this.index = index;
		this.type = type;
	}

	public void updateAdapter() {
		sAdapter.notifyDataSetChanged();
	}

	/**
	 * ListView Item 点击监听
	 * 
	 * @author SLS003
	 */
	class MyItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {
			String str = listString.get(position).get("name");
			switch (type) {
			case AppTools.BANK_TYPE:
				AccountInformationActivity activity = (AccountInformationActivity) context;
				activity.tv_bankName.setText(str);
				activity.bank_index = position;
				break;
			case AppTools.BANK_TYPE_TWO:
				AlterBankCardActivity activitytwo = (AlterBankCardActivity) context;
				activitytwo.tv_bankName.setText(str);
				activitytwo.bank_index = position;
				break;
			case AppTools.PROVINCE_TYPE:
				province = str;
				activity = (AccountInformationActivity) context;
				// 如果省份改变了 则将城市的值清空
				if (activity.province_index != position) {
					activity.city_index = -1;
					activity.tv_bankLocation.setText("");
				}
				activity.province_index = position;
				sAdapter.setIndex(position);
				activity.changCity();
				break;
			case AppTools.PROVINCE_TYPE_TWO:
				province = str;
				activitytwo = (AlterBankCardActivity) context;
				activitytwo = (AlterBankCardActivity) context;
				// 如果省份改变了 则将城市的值清空
				if (activitytwo.province_index != position) {
					activitytwo.city_index = -1;
					activitytwo.tv_bankLocation.setText("");
				}
				activitytwo.province_index = position;
				sAdapter.setIndex(position);
				activitytwo.changCity();
				break;
			case AppTools.CITY_TYPE:
				activity = (AccountInformationActivity) context;
				// 如果省份改变了 则将城市的值清空
				if (activity.city_index != position) {
					activity.tv_bankLocation.setText("");
				}
				activity.tv_bankLocation.setText(province + "-" + str);
				activity.city_index = position;
				break;
			case AppTools.CITY_TYPE_TWO:
				activitytwo = (AlterBankCardActivity) context;
				activitytwo = (AlterBankCardActivity) context;
				// 如果省份改变了 则将城市的值清空
				if (activitytwo.city_index != position) {
					activitytwo.tv_bankLocation.setText("");
				}
				activitytwo.tv_bankLocation.setText(province + "-" + str);
				activitytwo.city_index = position;
				break;
			case AppTools.ZHI_TYPE:
				// activity = (AccountInformationActivity) context;
				// activity.et_zhi.setText(str);
				// activity.zhi_index = position;
				break;
//			case AppTools.QUESTION_TYPE:
//				activity = (AccountInformationActivity) context;
//				activity.bankinfo_et_soft.setText(str);
//				activity.question_index = position;
//				break;
//			case AppTools.QUESTION_TYPE2:
//				WithdrawalActivity activity2 = (WithdrawalActivity) context;
//				activity2.et_question.setText(str);
//				activity2.question_index = position;
//				break;
			case AppTools.MONEY_TYPE:

				WithdrawalActivity activity3 = (WithdrawalActivity) context;
				Log.e("Str", str);
				activity3.edMoneyType.setText(str);
				activity3.money_index = position;
				break;
			}
			sAdapter.setIndex(position);
			sAdapter.notifyDataSetChanged();
			MyBankSpinner.this.dismiss();
		}

	}

}
