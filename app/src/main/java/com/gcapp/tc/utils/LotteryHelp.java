package com.gcapp.tc.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 江西时时彩的帮助类
 * 
 * @author lenovo
 * 
 */
public class LotteryHelp {
	public final static int JXSSC_ID = 61;
	public final static String[] JXSSC_TYPES = { "6113任选一", "6114任选二",
			"6115任选三", "6104大小单双", "6103一星", "6103二星直选", "6106二星组选",
			"6110二星和值", "6133趣味二星", "6103三星直选", "6111三星组三", "6112三星组六",
			"6119三组包胆", "6117三星直选和值", "6118三星组选和值", "6116三星组合", "6103四星直选",
			"6132四星组选4", "6131四星组选6", "6130四星组选12", "6129四星组选24", "6103五星直选",
			"6105五星通选", "6121五星组选5", "6120五星组选10", "6126五星组选20", "6125五星组选30",
			"6124五星组选60", "6123五星组选120", "6127五星好事成双", "6128五星三星报喜",
			"6122五星四季发财" };

	public static String setToString(HashSet<String> set) {
		return AppTools.sortSet(set).toString().replace("[", "")
				.replace("]", "").replace(", ", ",");
	}

	/**
	 * 区分玩法ID一样的不同玩法
	 * 
	 * @param ID
	 *            ：玩法ID
	 * @param lotteryNum
	 *            :选的号码格式
	 * @return
	 */
	public static String getNameByID(String ID, String lotteryNum) {
		if (!ID.equals("6103")) {
			for (String JXSSC_TYPE : JXSSC_TYPES) {
				if ((ID + "").equals(JXSSC_TYPE.substring(0, 4))) {
					return JXSSC_TYPE.substring(4);
				}
			}
		} else {
			if (lotteryNum.contains("-")) {
				switch (lotteryNum.lastIndexOf("-")) {
				case 0:
					return "四星直选";
				case 1:
					return "三星直选";
				case 2:
					return "二星直选";
				case 3:
					return "一星直选";
				}
			} else {
				return "五星直选";
			}
		}
		return null;
	}

	/**
	 * 获得随机一注的随机玩法Map
	 */
	public static Map<String, String> getRandomType() {
		Map<String, String> map = new HashMap<String, String>();
		List<String> except = new ArrayList<String>();
		except.add("6110");
		except.add("6111");
		except.add("6119");
		except.add("6117");
		except.add("6118");
		except.add("6116");
		int index;
		while (true) {
			index = (int) (Math.random() * (JXSSC_TYPES.length));
			String typeId = JXSSC_TYPES[index].substring(0, 4);
			if (except.contains(typeId)) {
				continue;
			}
			String typeName = JXSSC_TYPES[index].substring(4);
			if (typeName.equals("一星")) {
				typeId += "1";
			} else if (typeName.equals("二星直选")) {
				typeId += "2";
			} else if (typeName.equals("三星直选")) {
				typeId += "3";
			} else if (typeName.equals("四星直选")) {
				typeId += "4";
			} else if (typeName.equals("五星直选")) {
				typeId += "5";
			}
			map.put("typeId", typeId);
			map.put("typeName", typeName);
			map.put("count", "1");
			return map;
		}
	}

	/**
	 * 是否包含需要屏蔽的玩法id
	 */
	public static boolean containHideType(String typeId) {
		// 二星组选和值、三星和值、三星组合 、三星组选和值、三星组选包胆、
		int[] hides = new int[] { 6110, 6117, 6116, 6118, 6119 };
		if (TextUtils.isEmpty(typeId))
			return false;
		for (int hide : hides) {
			if (hide == Integer.valueOf(typeId))
				return true;
		}
		return false;
	}

	/**
	 * 如果方案包含已暂停的彩种玩法，就不能继续投注
	 */
	public static boolean canToBet() {
		if (AppTools.list_numbers != null) {
			for (int i = 0; i < AppTools.list_numbers.size(); i++) {
				if (containHideType(AppTools.list_numbers.get(i).getPlayType()
						+ "")) {
					return false;
				}
			}
		}
		return true;
	}
}
