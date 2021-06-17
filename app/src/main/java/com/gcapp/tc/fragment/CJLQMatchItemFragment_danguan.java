package com.gcapp.tc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcapp.tc.dataaccess.DtMatch_Basketball;
import com.gcapp.tc.sd.ui.Select_JCLQ_DAN_Activity;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapter_jclq_52_danguan;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.view.IphoneTreeView;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the
 * {@link CJLQMatchItemFragment_danguan.OnFragmentInteractionListener} interface
 * to handle interaction events. Use the
 * {@link CJLQMatchItemFragment_danguan#newInstance} factory method to create an
 * instance of this fragment.
 */
public class CJLQMatchItemFragment_danguan extends Fragment {

	private OnFragmentInteractionListener mListener;
	private Select_JCLQ_DAN_Activity activity;
	/**
	 * 玩法id
	 */
	private int type;
	/**
	 * 0为过关，1单关
	 */
	private int ways;
	private IphoneTreeView jclq_fragment_item_exListView;
	private ExpandAdapter_jclq_52_danguan exAdapter;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param type
	 *            Parameter 1.
	 * @return A new instance of fragment CJLQMatchItemFragment.
	 */
	public static CJLQMatchItemFragment_danguan newInstance(int type, int ways) {
		CJLQMatchItemFragment_danguan fragment = new CJLQMatchItemFragment_danguan();
		Bundle args = new Bundle();
		args.putInt("type", type);
		args.putInt("ways", ways);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			type = getArguments().getInt("type");
			ways = getArguments().getInt("ways");
		}
		activity = (Select_JCLQ_DAN_Activity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cjlqmatch_item,
				container, false);
		findView(view);
		init();
		return view;
	}

	private void findView(View view) {
		jclq_fragment_item_exListView = (IphoneTreeView) view
				.findViewById(R.id.jclq_fragment_item_exListView);
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		View pinView = getActivity().getLayoutInflater().inflate(
				R.layout.select_jczq_spf_groups, jclq_fragment_item_exListView,
				false);
		jclq_fragment_item_exListView.setHeaderView(pinView);

		exAdapter = new ExpandAdapter_jclq_52_danguan(getActivity(), this,
				getListDMBasketball(ways), jclq_fragment_item_exListView,
				getAdapterPlayType(type), ways);

		jclq_fragment_item_exListView.setAdapter(exAdapter);
		// 设置全部展开
		for (int i = 0; i < exAdapter.getGroupCount(); i++) {
			jclq_fragment_item_exListView.expandGroup(i);
		}
	}

	/**
	 * 根据单关或者过关选择不同数据
	 * 
	 * @param ways
	 *            ：区分过关和单关
	 * @return
	 */
	private List<List<DtMatch_Basketball>> getListDMBasketball(int ways) {
		if (ways == 0) { // 过关
			return setList_Matchs(setList_Matchs(ways,
					activity.getGuoguan_set(), 100));
		} else { // 单关
			return setList_Matchs(setList_Matchs(ways,
					activity.getDanguan_set(), 100));
		}
	}

	/**
	 * 将玩法ID转换成类型
	 * 
	 * @param type
	 *            :玩法ID
	 * @return
	 */
	private int getAdapterPlayType(int type) {
		int adapter_type = 1;
		switch (type) {
		case 7301: // 胜负
			adapter_type = 1;
			break;
		case 7302: // 让分
			adapter_type = 3;
			break;
		case 7303: // 胜分差
			adapter_type = 4;
			break;
		case 7304: // 大小分
			adapter_type = 2;
			break;
		case 7306: // 混合投注
			adapter_type = 5;
			break;
		}
		return adapter_type;
	}

	/**
	 * 根据玩法筛选出 队伍 *
	 */
	private List<List<DtMatch_Basketball>> setList_Matchs(
			List<List<DtMatch_Basketball>> _list) {
		List<List<DtMatch_Basketball>> list = new ArrayList<List<DtMatch_Basketball>>();
		for (List<DtMatch_Basketball> list_dt : _list) {
			List<DtMatch_Basketball> list2 = new ArrayList<DtMatch_Basketball>();
			for (DtMatch_Basketball dt : list_dt) {
				if (setList_Matchs(dt))
					if (setList_Matchs(dt))
						list2.add(dt);
			}
			list.add(list2);
		}
		return list;
	}

	/**
	 * 根据类型筛选出 对阵
	 * 
	 * @param dt
	 *            ：对阵
	 * @return
	 */
	private boolean setList_Matchs(DtMatch_Basketball dt) {
		switch (type) {
		case 7301:
			return dt.isSF();
		case 7304:
			return dt.isDXF();
		case 7302:
			return dt.isRFSF();
		case 7303:
			return dt.isSFC();
		case 7306:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 根据所选的赛事 筛选出队
	 * 
	 * @param ways
	 *            ：过关标识
	 * @param _list
	 *            ：对阵list
	 * @param type
	 *            :类型
	 */
	public List<List<DtMatch_Basketball>> setList_Matchs(int ways,
			List<String> _list, int type) {
		List<List<DtMatch_Basketball>> list_Matchs = new ArrayList<List<DtMatch_Basketball>>();

		for (List<DtMatch_Basketball> list : ways == 0 ? AppTools.DtMatch_Basketball
				: AppTools.DtMatch_Basketball_single) {

			List<DtMatch_Basketball> listM = new ArrayList<DtMatch_Basketball>();

			for (DtMatch_Basketball dt : list) {
				for (String str : _list) {
					if (dt.getGame().equals(str)) {
						switch (type) {
						case 0:
							if (Integer.parseInt(dt.getLetScore()) == 0)
								listM.add(dt);
							break;
						case 1:
							if (Integer.parseInt(dt.getLetScore()) == 0)
								listM.add(dt);
							break;
						case 100:
							listM.add(dt);
							break;
						}
					}
				}
			}
			if (listM.size() != 0)
				list_Matchs.add(listM);
		}

		return list_Matchs;
	}

	/**
	 * 按下投注项调用
	 * 
	 * @param argument
	 */
	public void onButtonPressed(int adapterType, String argument) {
		if (mListener != null) {
			mListener.onFragmentInteraction(adapterType, argument);
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
	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(int adapterType, String argument);
	}

	/**
	 * 清空选号
	 */
	public void clearSelect() {
		ExpandAdapter_jclq_52_danguan.map_hashMap_dx.clear();
		ExpandAdapter_jclq_52_danguan.map_hashMap_sf.clear();
		ExpandAdapter_jclq_52_danguan.map_hashMap_cbf.clear();
		ExpandAdapter_jclq_52_danguan.map_hashMap_rfsf.clear();
		ExpandAdapter_jclq_52_danguan.map_hashMap_hhtz.clear();
		exAdapter.notifyDataSetChanged();
	}

	/**
	 * 刷新界面UI
	 */
	public void update() {
		exAdapter.notifyDataSetChanged();
	}

	/**
	 * 刷新已选择的选项
	 * 
	 * @param indexs
	 * @param results
	 */
	public void update(List<String> indexs, List<String> results) {
		exAdapter.setMapSelects(indexs, results);
		exAdapter.notifyDataSetChanged();
	}
}
