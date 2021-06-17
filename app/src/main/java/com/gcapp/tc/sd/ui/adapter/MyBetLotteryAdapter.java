package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.R;

/**
 * 功能：投注详情的选号适配器
 * @author lenovo
 *
 */
/** 将 投注内容格式化 **/
public class MyBetLotteryAdapter extends BaseAdapter {
	private Context context;
	private Schemes scheme;
	// 所有胆拖的玩法
	private String[] danTuoId = new String[] { "502", "8704", "8707", "8710",
			"8712", "8715", "8717", "6808", "6810", "6811", "6813", "6815",
			"6816", "3906", "3907", "3908", "615", "8911", "8912", "8913",
			"8914", "9021", "5903", "6109", "3909", "8915", "8916", "9013",
			"9014", "9015", "9016", "9017", "9018", "9019", "9020", "2809",
			"2814", "6213", "6214", "6215", "6216", "6217", "6218", "6219",
			"6220", "6221", "3903", "3904", "7013", "7014", "7015", "7016",
			"7017", "7018", "7019", "7020", "7021", "6119", "7813", "7814",
			"7815", "7816", "7817", "7818", "7819", "7820", "7821" };
	private List<String> numbers;
	private List<Integer> max;

	public MyBetLotteryAdapter(Context context, Schemes scheme,
			List<String> numbers, List<Integer> max) {
		this.context = context;
		this.scheme = scheme;
		this.numbers = numbers;
		this.max = max;
	}

	@Override
	public int getCount() {
		return numbers.size();
	}

	@Override
	public Object getItem(int arg0) {
		return numbers.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (null == view) {
			LayoutInflater inflact = LayoutInflater.from(context);
			view = inflact.inflate(R.layout.my_betlottery_item, null);
			holder = new ViewHolder();
			holder.tv_lotteryName = (TextView) view
					.findViewById(R.id.tv_lotteryName);
			holder.tv_bei = (TextView) view.findViewById(R.id.tv_bei);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_other_num);
			holder.gv_number = (MyGridView) view
					.findViewById(R.id.gv_betNumber);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_num.setVisibility(View.GONE);
		if (numbers.get(position).equals("/")
				|| numbers.get(position).split("/").length == 1) {
			return view;
		}

		holder.tv_lotteryName.setText(LotteryUtils.transPlayName(
				numbers.get(position).split("/")[1], numbers.get(position)
						.split("/")[0]));

		String num = numbers.get(position).split("/")[0].trim();
		MyBetShowAdapter adapter = new MyBetShowAdapter(context,
				R.layout.betball_item);

		/** 设置开奖号码 **/
		if (null != scheme.getWinNumber() && scheme.getWinNumber().length() > 0) {
			String winNumber = scheme.getWinNumber().replaceAll(
					"\\s?[\\+-]\\s?", "+");

			if (winNumber.contains("+"))
				adapter.setWinNumber(winNumber.split("\\+")[0].split(" "),
						winNumber.split("\\+")[1].split(" "));
			else
				adapter.setWinNumber(scheme.getWinNumber().split(" "), null);

		}
		/** 设置显示号码 */
		adapter.setNumber(transNum(num), max.get(position));
		switch (Integer.parseInt(scheme.getLotteryID())) {

		case 5:// 双色球
		case 39:// 七乐彩
			holder.gv_number.setAdapter(adapter);
			break;

		// case 3:
		// case 6:
		// case 63:
		// case 64:
		// adapter.setNumber(getStrings(num),100);
		// adapter.setWinNumber(getStrings(scheme.getWinNumber()),null);
		// holder.gv_number.setAdapter(adapter);
		// break;
		case 13:// 七乐彩
		case 70:
		case 62:// 十一运夺金
		case 69:
			boolean flag = false;

			for (int i = 0; i < danTuoId.length; i++) {
				if (danTuoId[i].equals(scheme.getPlayTypeID() + "")) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				adapter.setNumber(
						num.replaceAll("\\s?[,\\|]\\s?", " ").split(" "), 100);
			} else {
				adapter.setNumber(num.replaceAll(",", ":").split(" "), 100);
			}
			adapter.setWinNumber(
					scheme.getWinNumber().replaceAll("\\s?[\\+-]\\s?", " ")
							.split(" "), null);
			holder.gv_number.setAdapter(adapter);
			break;
		case 87:
			holder.gv_number.setAdapter(adapter);
			break;

		default: // 3D,排 3 ，5,七星彩
			holder.gv_number.setVisibility(View.GONE);
			holder.tv_num.setVisibility(View.VISIBLE);
			holder.tv_num.setText(num.replaceAll("\\s?[,\\|]\\s?", " ") + "");
			break;
		}
		return view;
	}

	/** 将String拆分成 数组 */
	private String[] getStrings(String number) {
		if (null == number)
			return null;
		String str[] = new String[number.length()];
		for (int i = 0; i < number.length(); i++) {
			str[i] = number.substring(i, i + 1) + "";
		}
		return str;
	}

	private String[] transNum(String num) {
		num.replace("-", "+");
		if (num.contains(" + ")) {
			return num.replace(" + ", " ").split(" ");
		} else if (num.contains("+")) {
			return num.replace("+", " ").split(" ");
		}
		return num.split(" ");
	}

	static class ViewHolder {
		TextView tv_lotteryName, tv_bei, tv_num;
		MyGridView gv_number;
	}

}
