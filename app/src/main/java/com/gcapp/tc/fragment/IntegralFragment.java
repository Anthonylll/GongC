package com.gcapp.tc.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcapp.tc.R;

import butterknife.ButterKnife;

/**
 * 积分中心类
 * 
 * @author lenovo
 */
public class IntegralFragment extends Fragment {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final String ARG_PARAM3 = "param3";
	private String mParam1;
	private String mParam2;
	private String mParam3;
	private OnFragmentInteractionListener mListener;
	TextView text_oval;
	TextView text_get, text_exchange;// 累计获得积分,累计兑换积分

	/**
	 * 获得单例IntegralFragment
	 * 
	 * @param param1
	 *            当前积分
	 * @param param2
	 *            累计获得
	 * @param param3
	 *            累计兑换
	 */
	public static IntegralFragment newInstance(String param1, String param2,
			String param3) {
		IntegralFragment fragment = new IntegralFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		args.putString(ARG_PARAM3, param3);
		fragment.setArguments(args);
		return fragment;
	}

	public IntegralFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
			mParam3 = getArguments().getString(ARG_PARAM3);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_integral, container,
				false);
		text_oval = (TextView) view.findViewById(R.id.text_oval);

		text_get = (TextView) view.findViewById(R.id.text_get);// 累计获得积分
		text_exchange = (TextView) view.findViewById(R.id.text_exchange);// 累计兑换积分

		initAll();
		return view;
	}

	private void initAll() {
		updateAll(mParam1, mParam2, mParam3);
	}

	public void onButtonPressed() {
		if (mListener != null) {
			mListener.onFragmentInteraction(null);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		/**
		 * 圆圈点击
		 */
		public void onFragmentInteraction(Uri uri);
	}

	/**
	 * 设置当前积分
	 * 
	 * @param current
	 *            当前积分
	 */
	public void setOval(String current) {
		// Spanned html = Html
		// .fromHtml("<big>当前积分</big><br><font color=\"#c20202\">"
		// + current + "分</font>");
		text_oval.setText(current + "分");
	}

	/**
	 * 设置累计积分，累计兑换
	 * 
	 * @param total
	 *            累计积分
	 * @param already
	 *            累计兑换
	 */
	public void setTotal(String total, String already) {

		text_get.setText(total + "分");
		text_exchange.setText(already + "分");

		// String html = "累计获得积分：" + total + "分\t 累计兑换积分：" + already + "分";
	}

	/**
	 * 更新所有
	 * 
	 * @param current
	 *            设置当前积分
	 * @param total
	 *            累计积分
	 * @param already
	 *            累计兑换
	 */
	public void updateAll(String current, String total, String already) {
		setOval(current);
		setTotal(total, already);
	}
}
