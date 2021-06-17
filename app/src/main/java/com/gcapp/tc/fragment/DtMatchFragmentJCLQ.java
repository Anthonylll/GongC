package com.gcapp.tc.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcapp.tc.wheel.widget.TabPageIndicator;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the
 * {@link DtMatchFragmentJCLQ.OnFatherFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link DtMatchFragmentJCLQ#newInstance}
 * factory method to create an instance of this fragment.
 */
public class DtMatchFragmentJCLQ extends Fragment {
	private int playtype;
	private int currentIndex;
	private OnFatherFragmentInteractionListener mListener;

	// 选过关和单关控件
	private String[] CONTENT;
	private int[] NEWTYPE;
	private TabPageIndicator indicator;
	public ViewPager viewPager;
	private MyPagerAdapter adapter;
	private List<CJLQMatchItemFragment> fragments;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param playtype
	 *            玩法类型
	 * @param viewPagerCurrentIndex
	 * @return A new instance of fragment DtMatchFragmentCJLQ.
	 */
	public static DtMatchFragmentJCLQ newInstance(int playtype,
			int viewPagerCurrentIndex) {
		DtMatchFragmentJCLQ fragment = new DtMatchFragmentJCLQ();
		Bundle args = new Bundle();
		args.putInt("playtype", playtype);
		args.putInt("index", viewPagerCurrentIndex);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			playtype = getArguments().getInt("playtype", 7301);
			currentIndex = getArguments().getInt("index", 0);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), R.style.StyledIndicators);
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		View parent = localInflater.inflate(R.layout.fragment_dt_match_fcjlq,
				container, false);
		init();
		findView(parent);
		setListener();
		return parent;
	}

	private void findView(View v) {
		fragments = new ArrayList<CJLQMatchItemFragment>();
		viewPager = (ViewPager) v.findViewById(R.id.select_jclq_guoguan);
		adapter = new MyPagerAdapter(getChildFragmentManager());
		viewPager.setAdapter(adapter);

		indicator = (TabPageIndicator) v.findViewById(R.id.indicator_jclq);
		indicator.setViewPager(viewPager, currentIndex);
		indicator
				.setOnMyPagerChangeListener(new TabPageIndicator.OnMyPagerChangeListener() {
					@Override
					public void pagerChanged() {
						int position = viewPager.getCurrentItem();
						onButtonPressed(position);
					}
				});
		// if (playtype == 7306) {
		indicator.setVisibility(View.GONE);
		// }
	}

	/**
	 * 初始化过关类型
	 */
	private void init() {
		if (playtype != 7306) {
			CONTENT = new String[] { "过关", "单关" };
			NEWTYPE = new int[] { 0, 1 };
		} else {
			CONTENT = new String[] { "过关" };
			NEWTYPE = new int[] { 0 };
		}
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {

	}

	public void onButtonPressed(int position) {
		if (mListener != null) {
			mListener.onFatherFragmentInteraction(playtype, position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFatherFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFatherFragmentInteractionListener {
		public void onFatherFragmentInteraction(int type, int position);
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			CJLQMatchItemFragment fragment = CJLQMatchItemFragment.newInstance(
					playtype, NEWTYPE[arg0]);
			fragments.add(fragment);

			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length];
		}
	}

	public void setPlaytype(int playtype) {
		this.playtype = playtype;
	}

	/**
	 * 清空所选数据
	 */
	public void clearSelect() {
		if (fragments != null) {
			for (int i = 0; i < fragments.size(); i++) {
				CJLQMatchItemFragment fragment = fragments.get(i);
				fragment.clearSelect();
			}
		}
	}

	/**
	 * 刷新相应关的已选选项
	 * 
	 * @param ways
	 * @param indexs
	 * @param results
	 */
	public void update(int ways, List<String> indexs, List<String> results) {
		CJLQMatchItemFragment fragment = fragments.get(ways);
		fragment.update(indexs, results);
	}
}
