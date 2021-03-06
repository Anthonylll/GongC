package com.gcapp.tc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.text.TextUtils;
import android.util.Log;

import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.sd.ui.adapter.GridViewCJDLTAdapter;
import com.gcapp.tc.sd.ui.adapter.MyGridViewAdapter;

/**
 * 选号工具类
 * 
 * @author echo
 */
public class NumberTools {
	public static final String TAG = "NumberTools";

	/**
	 * @param playType
	 *            玩法Id
	 * @param list1
	 *            第一排的选号list
	 * @param list2
	 *            第二排的选号list
	 * @param list3
	 *            同一界面的第二种玩法的选号list
	 */
	public static int getCountFor_k3(int playType, List<String> list1,
			List<String> list2, List<String> list3) {
		int count = 0;
		switch (playType) {
		case 8301:// 和值 3 --18
		case 8302:// 三同通选111 222 333 444 555 666
		case 8304:// 二同号11 22 33 44 55 66
		case 8308:// 三连号 123 234 345 456
			count = list1.size();
			break;
		case 8303: // 三同单选111 222 333 444 555 666
			count = list1.size() + list3.size();
			break;
		case 8305:// 二同单选//11 22 33 44 55 66 ---1 2 3 4 5 6
			count = list1.size() * list2.size() + list3.size();
			break;
		case 8306:// 三不同号 1 2 3 4 5 6
			if (list1.size() < 3)
				count = 0 + list3.size();
			else {
				count = (list1.size() * (list1.size() - 1) * (list1.size() - 2))
						/ 6 + list3.size();
			}
			break;
		case 8307:// 二不同号1 2 3 4 5 6
			count = (list1.size() * (list1.size() - 1)) / 2;
			break;
		case 8309:// 胆拖三不同
			if (list1.size() == 1) {
				count = (list2.size() * (list2.size() - 1)) / 2;
			} else if (list1.size() == 2)
				count = list2.size();
			else
				count = 0;
			break;
		case 8310:// 胆拖2不同
			if (list1.size() == 1) {
				count = list2.size();
			} else {
				count = 0;
			}
			break;
		default:
			break;
		}
		return count;
	}

	/**
	 * 根据红球 和篮球个数获取注数（ 双色球 (S) 6+1大乐透(D)是 5+2）
	 * 
	 * @param RedCount
	 *            所选的红球个数
	 * @param BlueCount
	 *            所选的蓝球个数
	 * @param RedNumber
	 *            5,6 标准的红球个数
	 * @param BlueNumber
	 *            标准的蓝球个数
	 */
	public static long getCountForSD(int RedCount, int BlueCount,
			int RedNumber, int BlueNumber) {
		// 如果没有选满数字 则返回 0
		if ((RedCount < RedNumber) || (BlueCount < BlueNumber)) {
			return 0;
		}
		// 如果选择的红球和篮球 数量 等于 应该选的数量
		else if (RedCount == RedNumber && BlueCount == BlueNumber) {
			return 1;
		} else {
			// 红球的排列总数 变量
			long RedInvestNum = 1;

			for (int i = 0; i < RedNumber; i++) {
				RedInvestNum = RedInvestNum * RedCount;
				RedCount--;
			}
			long RedInvestNum2 = 1;
			for (int i = RedNumber; i > 0; i--) {
				RedInvestNum2 *= i;
			}
			RedInvestNum = RedInvestNum / RedInvestNum2;
			// 蓝球的排列总数 变量
			long BlueInvestNum = 1;
			// 如果篮球应该选的数量大于1 （也就是大乐透 5+2 形式的）
			if (BlueNumber > 1) {
				if (BlueCount > 2) {
					for (int i = 3; i <= BlueCount; i++)
						BlueInvestNum *= i;
					for (int i = 2; i <= (BlueCount - 2); i++)
						BlueInvestNum = Math.round(BlueInvestNum / i);
				}
				// 如果篮球应该选的数量==1 （也就是双色球 6+1 形式的）
			} else {
				BlueInvestNum = BlueCount;
			}
			return (RedInvestNum * BlueInvestNum);
		}
	}

	/**
	 * 将已投注彩种转换成投注页面所需信息
	 * 
	 * @param scheme
	 *            已投注彩种方案对象
	 * @return
	 */
	public static boolean changeSchemesToSelectedNumbers(Schemes scheme) {
		// try {
		List<LotteryContent> contents = scheme.getBuyContent();
		Log.i("继续投注 记录号码", "contents: " + contents.size()
				+ "// scheme.getLotteryID(): " + scheme.getLotteryID()
				+ "// scheme.getIsuseID(): " + scheme.getIsuseID());
		if (contents == null || contents.isEmpty()
				|| TextUtils.isEmpty(scheme.getLotteryID())
				|| TextUtils.isEmpty(scheme.getIsuseID())) {
			return false;
		}

		if (AppTools.list_numbers == null) {
			AppTools.list_numbers = new ArrayList<SelectedNumbers>();
		}
		AppTools.list_numbers.clear();

		for (Lottery lottery : HallFragment.listLottery) {
			if (lottery.getLotteryID().equals(scheme.getLotteryID())) {
				AppTools.lottery = lottery;
				if (lottery.getDtCanChaseIsuses() != null) {
					AppTools.lottery.setDtCanChaseIsuses(lottery
							.getDtCanChaseIsuses());
				}
			}
		}
		String[] numbers = new String[1];
		for (int a = 0; a < contents.size(); a++) {
			if (TextUtils.isEmpty(contents.get(a).getPlayType())) {
				Log.i("继续投注 记录号码getPlayType()", "getPlayType(): "
						+ contents.get(a).getPlayType());
				return false;
			}
			int playType = Integer.valueOf(contents.get(a).getPlayType());
			numbers[0] = contents.get(a).getLotteryNumber()
					.replaceAll("\\s?[+]\\s?", "-");
			switch (playType) {
			/**
			 * 普通投注
			 */
			case 501:// 双色球
			case 3901:// 大乐透
			case 3902:// 大乐透追号
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String selectedRed = numbers[i].split("-")[0];// 所选红球
					String selectedBlue = numbers[i].split("-")[1];// 所选篮球
					String[] redNums = selectedRed.split(" ");// 所选每个红球
					List<String> redList = new ArrayList<String>();
					Collections.addAll(redList, redNums);
					if (numbers.length != 1 && i != numbers.length) {// 去除每一个方案后面的空格
						selectedBlue = selectedBlue.substring(0,
								selectedBlue.length() - 1);
					}
					String[] blueNums = selectedBlue.split(" ");// 所选每个篮球
					List<String> blueList = new ArrayList<String>();
					Collections.addAll(blueList, blueNums);
					Collections.sort(redList);
					Collections.sort(blueList);
					selectedNum.setPlayType(playType);
					/**
					 * 红篮球的标准个数
					 */
					int redNumCount = 0;
					int blueNumcount = 0;
					if (501 == playType) {// 双色球
						redNumCount = 6;
						blueNumcount = 1;
					} else if (3901 == playType || 3902 == playType) {// 大乐透
						redNumCount = 5;
						blueNumcount = 2;
					}
					long count = getCountForSD(redNums.length, blueNums.length,
							redNumCount, blueNumcount);
					selectedNum.setCount(count);// 设置注数
					selectedNum.setMoney(2 * count);// 设置金额
					selectedNum.setLotteryNumber(numbers[i]);
					selectedNum.setRedNumbers(redList);// 将篮球集合放入选号对象
					selectedNum.setBlueNumbers(blueList);// 将篮球集合放入选号对象
					AppTools.list_numbers.add(selectedNum);// 将选号对象放入集合中
				}
				/**
				 * 设置玩法id
				 */
				if (501 == playType) {// 双色球
					MyGridViewAdapter.playType = 501;
				} else if (3901 == playType) {// 大乐透
					GridViewCJDLTAdapter.playType = 3901;
				}
				break;
			/**
			 * 胆拖投注
			 */
			case 502:// 双色球胆拖投注
			case 3903:// 大乐透胆拖投注
			case 3904:// 大乐透胆拖投注追号
			case 3906:// 大乐透后区胆拖投注
			case 3907:// 大乐透双区胆拖投注
			case 3908:// 大乐透后区胆拖投注追号
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String selectedRed = numbers[i].split(",")[0];// 所选红球
					if (3906 == playType) {
						selectedRed = numbers[i].split("-")[0];// 所选红球
					}
					String selectedRedTuo = numbers[i].split(",")[1].split("-")[0];// 红球胆拖
					selectedRedTuo = selectedRedTuo.substring(1,
							selectedRedTuo.length());// 去掉拖码前面的空格
					String selectedBlueTuo = numbers[i].split("-")[1]
							.split(",")[0];// 篮球拖码
					String selectedBlue = numbers[i].split("-")[1];// 所选篮球
					if (3906 == playType) {// 大乐透后区胆拖
						selectedBlue = numbers[i].split(",")[1];// 所选篮球
					} else if (3907 == playType) {// 大乐透双区胆拖
						selectedBlue = numbers[i].split(",")[2];// 所选篮球
					}
					String[] redNums = selectedRed.split(" ");// 所选每个红球
					List<String> redList = new ArrayList<String>();
					Collections.addAll(redList, redNums);
					String[] redTuoNums = selectedRedTuo.split(" ");// 所选每个红球胆拖
					List<String> redTuoList = new ArrayList<String>();
					Collections.addAll(redTuoList, redTuoNums);
					String[] blueTuoNums = selectedBlueTuo.split(" ");// 所选每个蓝球胆拖
					List<String> blueTuoList = new ArrayList<String>();
					Collections.addAll(blueTuoList, blueTuoNums);
					if (numbers.length != 1 && i != numbers.length) {// 去除每一个方案后面的空格
						selectedBlue = selectedBlue.substring(0,
								selectedBlue.length() - 1);
					}
					if (3906 == playType || 3907 == playType) {// 去除前面的空格
						selectedBlue = selectedBlue.substring(1,
								selectedBlue.length());
					}
					String[] blueNums = selectedBlue.split(" ");// 所选每个篮球
					List<String> blueList = new ArrayList<String>();
					Collections.addAll(blueList, blueNums);
					Collections.sort(redList);
					Collections.sort(redTuoList);
					Collections.sort(blueList);
					selectedNum.setPlayType(playType);
					/**
					 * 红篮球的标准个数
					 */
					int redNumCount = 0;
					int blueNumcount = 0;
					if (502 == playType) {// 双色球
						redNumCount = 6;
						blueNumcount = 1;
					} else if (3903 == playType || 3904 == playType) {// 大乐透前区胆拖|追号
						redNumCount = 5;
						blueNumcount = 2;
					}
					long count = 0;
					if (3906 == playType || 3908 == playType) {// 大乐透后区投注|追号
						count = NumberTools.getCountForDLT(redNums.length, 0,
								blueTuoNums.length, blueNums.length, 1);

					} else if (3907 == playType) {
						count = NumberTools.getCountForDLT(redNums.length,
								redTuoNums.length, blueTuoNums.length,
								blueNums.length, 2);
					} else {
						count = getCountForSSQ_tuo(redNums.length,
								redTuoNums.length, blueNums.length,
								redNumCount, blueNumcount);
					}
					selectedNum.setCount(count);// 设置注数
					selectedNum.setMoney(2 * count);// 设置金额
					selectedNum.setLotteryNumber(numbers[i]);
					if (3906 == playType || 3908 == playType) {
						selectedNum.setBlueNumbers(blueTuoList);// 将蓝球胆拖集合放入选号对象
						selectedNum.setblueTuoNum(blueList);// 将篮球集合放入选号对象
					} else if (3907 == playType) {
						selectedNum.setBlueNumbers(blueTuoList);// 将蓝球胆拖集合放入选号对象
						selectedNum.setblueTuoNum(blueList);// 将篮球集合放入选号对象
						selectedNum.setRedTuoNum(redTuoList);// 将红球胆拖集合放入选号对象
					} else {
						selectedNum.setRedTuoNum(redTuoList);// 将红球胆拖集合放入选号对象
						selectedNum.setBlueNumbers(blueList);// 将篮球集合放入选号对象
					}
					selectedNum.setRedNumbers(redList);// 将红球集合放入选号对象
					AppTools.list_numbers.add(selectedNum);// 将选号对象放入集合中
				}
				/**
				 * 设置玩法id
				 */
				if (502 == playType) {// 双色球
					MyGridViewAdapter.playType = playType;
				} else if (3903 == playType// 大乐透前区胆拖
						|| 3904 == playType// 大乐透前区胆拖追号
						|| 3906 == playType// 大乐透后区胆拖
						|| 3908 == playType// 大乐透双区胆拖
						|| 3907 == playType) {// 大乐透后区胆拖追号
					GridViewCJDLTAdapter.playType = playType;
				}
				break;
			/**
			 * 七星彩/排列五
			 */
			case 301:
			case 6401:
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String seleteNum = numbers[i];
					if (numbers.length != 1 && i != numbers.length
							&& i != numbers.length - 1) {// 去除每一个方案后面的空格
						seleteNum = seleteNum.substring(0,
								seleteNum.length() - 2);
					}
					ArrayList<String> showList = changeStringtoArray(seleteNum);
					long count = 0;
					if (301 == playType) {// 七星彩
						count = NumberTools.getCountForQXC(playType, showList
								.get(0).length(), showList.get(1).length(),
								showList.get(2).length(), showList.get(3)
										.length(), showList.get(4).length(),
								showList.get(5).length(), showList.get(6)
										.length());
					} else {// 排列五
						count = NumberTools.getCountForPL5(playType, showList
								.get(0).length(), showList.get(1).length(),
								showList.get(2).length(), showList.get(3)
										.length(), showList.get(4).length());
					}
					StringBuilder showNumber = new StringBuilder();
					for (int j = 0; j < showList.size(); j++) {
						if (1 == showList.size()) {
							showNumber.append(showList.get(j));
						} else {
							if (j == showList.size() - 1) {
								showNumber.append(showList.get(j));
							} else {
								showNumber.append(showList.get(j)).append(" ");
							}
						}
					}
					selectedNum.setCount(count);// 设置注数
					selectedNum.setMoney(2 * count);// 设置金额
					selectedNum.setPlayType(playType);// 设置玩法id
					selectedNum.setLotteryNumber(numbers[i]);// 设置投注号码
					selectedNum.setShowLotteryNumber(showNumber.toString());// 设置显示号码
					AppTools.list_numbers.add(selectedNum);// 将选号对象放入集合中
				}
				break;
			/**
			 * 七乐彩
			 */
			case 1301:
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String seleteNum = numbers[i];
					if (numbers.length != 1 && i != numbers.length
							&& i != numbers.length - 1) {// 去除每一个方案后面的空格
						seleteNum = seleteNum.substring(0,
								seleteNum.length() - 2);
					}
					String[] redNumber = seleteNum.split(" ");
					List<String> redNumbers = new ArrayList<String>();
					Collections.addAll(redNumbers, redNumber);
					long count = 0;
					long f1 = 1, f2 = 1;
					for (int m = seleteNum.split(" ").length; m > 7; m--)
						f1 *= m;
					for (int m = seleteNum.split(" ").length - 7; m > 0; m--)
						f2 *= m;
					count = f1 / f2;
					selectedNum.setRedNumbers(redNumbers);// 设置红球list
					selectedNum.setCount(count);// 设置注数
					selectedNum.setMoney(2 * count);// 设置金额
					selectedNum.setPlayType(playType);// 设置玩法id
					selectedNum.setLotteryNumber(numbers[i]);// 设置投注号码
					selectedNum.setShowLotteryNumber(numbers[i]);// 设置显示号码
					AppTools.list_numbers.add(selectedNum);// 将选号对象放入集合中
				}
				break;
			/**
			 * 时时彩
			 */
			case 2803:// 一星复式/五星复式/三星复式/二星复式
			case 2804:// 大小单双
			case 2805:// 五星通选
			case 2806:// 二星组选
			case 2811:// 三星组三
			case 2812:// 三星组六
			case 2814:// 组三包胆
			case 2815:// 组三和值
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String seleteNum = numbers[i];
					Log.i(TAG, "号码----->" + seleteNum);
					if (numbers.length != 1 && i != numbers.length
							&& i != numbers.length - 1) {// 去除每一个方案后面的空格
						seleteNum = seleteNum.substring(0,
								seleteNum.length() - 2);
					}
					seleteNum = seleteNum.replace("-", "");
					List<String> redNumbers = new ArrayList<String>();
					StringBuilder number = new StringBuilder();
					/**
					 * 拼接所需特定字符串
					 */
					if (2806 == playType || 2811 == playType
							|| 2812 == playType) {// 组选
						redNumbers.add(seleteNum);
						number.append(seleteNum);
					} else {
						redNumbers = changeStringtoArray(seleteNum);
						if (2804 != playType) {
							for (int j = 0; j < redNumbers.size(); j++) {
								if (1 == redNumbers.size()) {
									number.append(redNumbers.get(j));
								} else {
									if (j != redNumbers.size() - 1) {
										number.append(redNumbers.get(j))
												.append("-");
									} else {
										number.append(redNumbers.get(j));
									}
								}
							}
						}
					}
					/**
					 * 时时彩的玩法类型
					 */
					int type = 1;
					if (2803 == playType) {
						if (1 == redNumbers.size()) {
							type = 1;
						} else if (2 == redNumbers.size()) {
							type = 2;
						} else if (3 == redNumbers.size()) {
							type = 4;
						} else if (5 == redNumbers.size()) {
							type = 7;
						}
					} else if (2806 == playType) {
						type = 3;
					} else if (2811 == playType) {
						type = 5;
					} else if (2812 == playType) {
						type = 6;
					} else if (2805 == playType) {
						type = 8;
					} else if (2804 == playType) {
						type = 9;
					} else if (2814 == playType) {
						type = 10;
					} else if (2815 == playType) {
						type = 11;
					}
					/**
					 * 计算注数
					 */
					long count = 0;
					if (type == 9) {// 大小单双
						count = 1;
					} else {
						/**
						 * 获取计算注数所需set
						 */
						HashSet<String> oneSet = new HashSet<String>();
						HashSet<String> twoSet = new HashSet<String>();
						HashSet<String> threeSet = new HashSet<String>();
						HashSet<String> fourSet = new HashSet<String>();
						HashSet<String> fiveSet = new HashSet<String>();
						if (playType == 2814) {
							String num = numbers[i];
							if (num.contains(",")) {
								String[] nums = num.split(",");
								oneSet.add(nums[0]);
								twoSet.add(nums[1]);
							} else
								oneSet.add(num);
						} else if (playType == 2815) {
							String num = numbers[i];
							if (num.contains(",")) {
								String[] nums = num.split(",");
								Collections.addAll(oneSet, nums);
							} else
								oneSet.add(num);
						} else {
							for (int j = 0; j < redNumbers.size(); j++) {
								String nums = redNumbers.get(j);
								String[] numberSplit = nums.split("");
								for (int k = 1; k < numberSplit.length; k++) {
									if (0 == j) {
										oneSet.add(numberSplit[k]);
									} else if (1 == j) {
										twoSet.add(numberSplit[k]);
									} else if (2 == j) {
										threeSet.add(numberSplit[k]);
									} else if (3 == j) {
										fourSet.add(numberSplit[k]);
									} else if (4 == j) {
										fiveSet.add(numberSplit[k]);
									}
								}
							}
						}
						count = NumberTools.getSSC_count(oneSet, twoSet,
								threeSet, fourSet, fiveSet, type);
					}
					selectedNum.setCount(count);// 设置注数
					selectedNum.setMoney(2 * count);// 设置金额
					selectedNum.setPlayType(playType);// 设置玩法id
					selectedNum.setType(type);// 设置时时彩玩法类型
					if (2804 != playType) {
						selectedNum.setNumber(number.toString());// 设置特定格式号码
					}
					selectedNum.setLotteryNumber(numbers[i]);// 设置投注号码
					selectedNum.setShowLotteryNumber(numbers[i]);// 设置显示号码
					if (2804 == playType) {
						String dxdsNum = numbers[i].charAt(0) + ","
								+ numbers[i].charAt(1);
						selectedNum.setLotteryNumber(dxdsNum);// 设置投注号码
						selectedNum.setShowLotteryNumber(dxdsNum);// 设置显示号码
					}
					if (playType == 2814) {
						String num = numbers[i];
						selectedNum.setNumber(num);
						selectedNum.setShowLotteryNumber(num);
						selectedNum.setLotteryNumber(num);
					}
					if (playType == 2815) {
						String num = numbers[i];
						selectedNum.setNumber(num);
						selectedNum.setShowLotteryNumber(num);
						selectedNum.setLotteryNumber(num);
					}
					if (2804 != playType) {
						String showNum[] = new String[1];
						StringBuilder showNumbers = new StringBuilder();
						if (number.toString().contains("-")) {
							showNum = number.toString().split("-");
							for (String aShowNum : showNum) {
								if (1 != aShowNum.length()) {
									showNumbers.append("(").append(aShowNum)
											.append(")");
								} else {
									showNumbers.append(aShowNum);
								}
							}
							selectedNum.setShowLotteryNumber(showNumbers
									.toString());// 设置特定格式号码
						} else {
							selectedNum.setShowLotteryNumber(number.toString());// 设置特定格式号码
						}
					}
					AppTools.list_numbers.add(selectedNum);// 将选号对象放入集合中
				}
				break;

			/**
			 * 时时彩
			 */
			case 6603:// 一星复式/五星复式/三星复式/二星复式
			case 6604:// 大小单双
			case 6605:// 五星通选
			case 6606:// 二星组选
			case 6611:// 三星组三
			case 6612:// 三星组六
			case 6614:// 组三包胆
			case 6615:// 组三和值
			case 6619:// 三星任选
			case 6620:// 二星任选
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String seleteNum = numbers[i];
					Log.i(TAG, "号码----->" + seleteNum);
					if (numbers.length != 1 && i != numbers.length
							&& i != numbers.length - 1) {// 去除每一个方案后面的空格
						seleteNum = seleteNum.substring(0,
								seleteNum.length() - 2);
					}
					seleteNum = seleteNum.replace("-", "");
					List<String> redNumbers = new ArrayList<String>();
					StringBuilder number = new StringBuilder();
					/**
					 * 拼接所需特定字符串
					 */
					if (6606 == playType || 6611 == playType
							|| 6612 == playType) {// 组选
						redNumbers.add(seleteNum);
						number.append(seleteNum);
					} else {
						redNumbers = changeStringtoArray(seleteNum);
						if (6604 != playType) {
							for (int j = 0; j < redNumbers.size(); j++) {
								if (1 == redNumbers.size()) {
									number.append(redNumbers.get(j));
								} else {
									if (j != redNumbers.size() - 1) {
										number.append(redNumbers.get(j))
												.append("-");
									} else {
										number.append(redNumbers.get(j));
									}
								}
							}
						}
					}
					/**
					 * 时时彩的玩法类型
					 */
					int type = 1;
					if (6603 == playType) {
						if (1 == redNumbers.size()) {
							type = 1;
						} else if (2 == redNumbers.size()) {
							type = 2;
						} else if (3 == redNumbers.size()) {
							type = 5;
						} else if (4 == redNumbers.size()) {
							type = 9;
						} else if (5 == redNumbers.size()) {
							type = 10;
						}
					} else if (6606 == playType) {
						type = 3;
					} else if (6611 == playType) {
						type = 6;
					} else if (6612 == playType) {
						type = 7;
					} else if (6605 == playType) {
						type = 11;
					} else if (6604 == playType) {
						type = 12;
					} else if (6620 == playType) {
						type = 4;
					} else if (6619 == playType) {
						type = 8;
					}
					/**
					 * 计算注数
					 */
					long count = 0;
					if (type == 12) {// 大小单双
						count = 1;
					} else {
						/**
						 * 获取计算注数所需set
						 */
						HashSet<String> oneSet = new HashSet<String>();
						HashSet<String> twoSet = new HashSet<String>();
						HashSet<String> threeSet = new HashSet<String>();
						HashSet<String> fourSet = new HashSet<String>();
						HashSet<String> fiveSet = new HashSet<String>();
						if (playType == 6614) {
							String num = numbers[i];
							if (num.contains(",")) {
								String[] nums = num.split(",");
								oneSet.add(nums[0]);
								twoSet.add(nums[1]);
							} else
								oneSet.add(num);
						} else if (playType == 6615) {
							String num = numbers[i];
							if (num.contains(",")) {
								String[] nums = num.split(",");
								Collections.addAll(oneSet, nums);
							} else
								oneSet.add(num);
						} else {
							for (int j = 0; j < redNumbers.size(); j++) {
								String nums = redNumbers.get(j);
								String[] numberSplit = nums.split("");
								for (int k = 1; k < numberSplit.length; k++) {
									if (0 == j) {
										oneSet.add(numberSplit[k]);
									} else if (1 == j) {
										twoSet.add(numberSplit[k]);
									} else if (2 == j) {
										threeSet.add(numberSplit[k]);
									} else if (3 == j) {
										fourSet.add(numberSplit[k]);
									} else if (4 == j) {
										fiveSet.add(numberSplit[k]);
									}
								}
							}
						}
						count = NumberTools.getXJSSC_count(oneSet, twoSet,
								threeSet, fourSet, fiveSet, type);
					}
					selectedNum.setCount(count);// 设置注数
					selectedNum.setMoney(2 * count);// 设置金额
					selectedNum.setPlayType(playType);// 设置玩法id
					selectedNum.setType(type);// 设置时时彩玩法类型
					if (6604 != playType) {
						selectedNum.setNumber(number.toString());// 设置特定格式号码
					}
					selectedNum.setLotteryNumber(numbers[i]);// 设置投注号码
					selectedNum.setShowLotteryNumber(numbers[i]);// 设置显示号码
					if (6604 == playType) {
						String dxdsNum = numbers[i].charAt(0) + ","
								+ numbers[i].charAt(1);
						selectedNum.setLotteryNumber(dxdsNum);// 设置投注号码
						selectedNum.setShowLotteryNumber(dxdsNum);// 设置显示号码
					}
					// if(playType == 6614){
					// String num=numbers[i];
					// selectedNum.setNumber(num);
					// selectedNum.setShowLotteryNumber(num);
					// selectedNum.setLotteryNumber(num);
					// }
					// if(playType == 6615){
					// String num=numbers[i];
					// selectedNum.setNumber(num);
					// selectedNum.setShowLotteryNumber(num);
					// selectedNum.setLotteryNumber(num);
					// }
					if (6604 != playType) {
						String showNum[] = new String[1];
						StringBuilder showNumbers = new StringBuilder();
						if (number.toString().contains("-")) {
							showNum = number.toString().split("-");
							for (String aShowNum : showNum) {
								if (1 != aShowNum.length()) {
									showNumbers.append("(").append(aShowNum)
											.append(")");
								} else {
									showNumbers.append(aShowNum);
								}
							}
							selectedNum.setShowLotteryNumber(showNumbers
									.toString());// 设置特定格式号码
						} else {
							selectedNum.setShowLotteryNumber(number.toString());// 设置特定格式号码
						}
					}
					AppTools.list_numbers.add(selectedNum);// 将选号对象放入集合中
				}
				break;
			/**
			 * 江西时时彩
			 */
			case 6103:
			case 6106:
			case 6119:
			case 6112:
			case 6111:
			case 6116:
			case 6129:
			case 6123:
			case 6127:
			case 6128:
			case 6122:
			case 6104:
			case 6130:
			case 6131:
			case 6132:
			case 6124:
			case 6126:
			case 6120:
			case 6121:
			case 6125:
			case 6113:
			case 6114:
			case 6115:
			case 6105:
			case 6110:
			case 6118:
			case 6117:
			case 6133:
				// 这里6103为所有一到五星的直选，为了加以区分这里后面加上1-5，在投注的时候消除即可
				int[] typeIDs = { 6113, 6114, 6115, 6104, 61031, 61032, 6106,
						6110, 6133, 61033, 6111, 6112, 6119, 6117, 6118, 6116,
						61034, 6132, 6131, 6130, 6129, 61035, 6105, 6121, 6120,
						6126, 6125, 6124, 6123, 6127, 6128, 6122 };
				String[] typeNames = { "任选一", "任选二", "任选三", "大小单双", "一星",
						"二星直选", "二星组选", "二星和值", "趣味二星", "三星直选", "三星组三", "三星组六",
						"三组包胆", "三星直选和值", "三星组选和值", "三星组合", "四星直选", "四星组选4",
						"四星组选6", "四星组选12", "四星组选24", "五星直选", "五星通选", "五星组选5",
						"五星组选10", "五星组选20", "五星组选30", "五星组选60", "五星组选120",
						"五星好事成双", "五星三星报喜", "五星四季发财" };
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String seleteNum = numbers[i];
					if (numbers.length != 1 && i != numbers.length
							&& i != numbers.length - 1) {// 去除每一个方案后面的空格
						seleteNum = seleteNum.substring(0,
								seleteNum.length() - 2);
					}
					List<String> redNumbers;
					HashSet<String> oneSet = new HashSet<String>();
					HashSet<String> twoSet = new HashSet<String>();
					HashSet<String> threeSet = new HashSet<String>();
					HashSet<String> fourSet = new HashSet<String>();
					HashSet<String> fiveSet = new HashSet<String>();
					redNumbers = changeStringtoArray(seleteNum);
					int type = 0;
					String playTypeName = "";
					for (int n = 0; n < typeIDs.length; n++) {
						if (playType == 6103) {
							int listSize = redNumbers.size();
							playType = playType * 10 + listSize;

						}
						if (playType == typeIDs[n]) {
							type = n + 1;
							playTypeName = typeNames[n];
						}
					}
					/**
					 * 计算注数
					 */
					long count = 0;
					switch (playType) {
					case 61031:
					case 61032:
					case 61033:
					case 61034:
					case 61035:
					case 6113:
					case 6114:
					case 6115:
					case 6105:
						for (int m = 0; m < redNumbers.size(); m++) {
							String nums = redNumbers.get(m);
							String[] numberSplit = nums.split("");
							for (int k = 1; k < numberSplit.length; k++) {
								if (0 == m) {
									oneSet.add(numberSplit[k]);
								} else if (1 == m) {
									twoSet.add(numberSplit[k]);
								} else if (2 == m) {
									threeSet.add(numberSplit[k]);
								} else if (3 == m) {
									fourSet.add(numberSplit[k]);
								} else if (4 == m) {
									fiveSet.add(numberSplit[k]);
								}
							}
						}
						break;
					case 6106:
					case 6112:
					case 6111:
					case 6116:
					case 6129:
					case 6123:
						String[] num = seleteNum.split(" ");
						Collections.addAll(oneSet, num);
						break;
					case 6119:
					case 6128:
					case 6122:
					case 6110:
					case 6118:
					case 6117:
					case 6127:
						String[] num1 = seleteNum.split(",");
						Collections.addAll(oneSet, num1);
						break;
					case 6130:
					case 6131:
					case 6132:
					case 6124:
					case 6126:
					case 6120:
					case 6121:
					case 6125:
						String[] num2 = seleteNum.split(",");
						for (int k = 0; k < num2.length; k++) {
							if (2 == num2.length) {
								if (0 == k) {
									String[] num3 = num2[0].split(" ");
									Collections.addAll(twoSet, num3);
								} else if (1 == k) {
									String[] num4 = num2[1].split(" ");
									Collections.addAll(oneSet, num4);
								}
							} else if (3 == num2.length) {
								if (0 == k) {
									String[] num3 = num2[0].split(" ");
									Collections.addAll(threeSet, num3);
								} else if (1 == k) {
									String[] num4 = num2[1].split(" ");
									Collections.addAll(twoSet, num4);
								} else if (2 == k) {
									String[] num5 = num2[2].split(" ");
									Collections.addAll(oneSet, num5);
								}
							}
						}
						break;
					}
					if (6133 == playType) {
						String[] num = seleteNum.split(" ");
						if (3 == num.length) {
							count = 1;
							for (String aNum : num) {
								count *= aNum.length();
							}
						}
					} else if (6104 == playType) {
						count = 1;
					} else {
						count = NumberTools.getJXSSC_count(oneSet, twoSet,
								threeSet, fourSet, fiveSet, type);
					}
					selectedNum.setCount(count);// 设置注数
					selectedNum.setMoney(2 * count);// 设置金额
					selectedNum.setPlayType(playType);// 设置玩法id
					selectedNum.setLotteryNumber(seleteNum);// 设置投注号码
					selectedNum.setPlayTypeName(playTypeName);// 设置玩法名称
					AppTools.list_numbers.add(selectedNum);// 将选号对象放入集合中
				}
				break;
			/**
			 * 广东/江西/山东 11选5
			 */
			case 7801:
			case 7802:
			case 7803:
			case 7804:
			case 7805:
			case 7806:
			case 7807:
			case 7808:
			case 7809:
			case 7810:
			case 7811:
			case 7812:
			case 7813:
			case 7814:
			case 7815:
			case 7816:
			case 7817:
			case 7818:
			case 7819:
			case 7820:
			case 7821:
			case 7822:
			case 7823:
			case 7824:
			case 7825:

			case 7001:
			case 7002:
			case 7003:
			case 7004:
			case 7005:
			case 7006:
			case 7007:
			case 7008:
			case 7009:
			case 7010:
			case 7011:
			case 7012:
			case 7013:
			case 7014:
			case 7015:
			case 7016:
			case 7017:
			case 7018:
			case 7019:
			case 7020:
			case 7021:
			case 7022:
			case 7023:
			case 7024:
			case 7025:

			case 6201:
			case 6202:
			case 6203:
			case 6204:
			case 6205:
			case 6206:
			case 6207:
			case 6208:
			case 6209:
			case 6210:
			case 6211:
			case 6212:
			case 6213:
			case 6214:
			case 6215:
			case 6216:
			case 6217:
			case 6218:
			case 6219:
			case 6220:
			case 6221:
			case 6222:
			case 6223:
			case 6224:
			case 6225:
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String seleteNum = numbers[i];
					if (numbers.length != 1 && i != numbers.length
							&& i != numbers.length - 1) {// 去除每一个方案后面的空格
						seleteNum = seleteNum.substring(0,
								seleteNum.length() - 2);
					}
					HashSet<String> oneSet = new HashSet<String>();
					HashSet<String> twoSet = new HashSet<String>();
					HashSet<String> threeSet = new HashSet<String>();
					String one = "";
					String two = "";
					String three = "";
					int play1 = playType / 100;
					int play = playType - play1 * 100;
					// 转换投注格式和拼接计算注数set
					if (play == 1) {// 前一
						one = seleteNum;
						oneSet.add(seleteNum);
					} else if ((play >= 2 && play <= 8) || 11 == play
							|| 12 == play || 24 == play || 25 == play) {// 乐选4,5
						one = seleteNum;
						if (one.contains(" "))
							one = one.replace(" ", ", ");
						String[] array1 = seleteNum.split(" ");
						Collections.addAll(oneSet, array1);
					} else if (9 == play || 10 == play) {// 前二/前三
						String[] array2 = seleteNum.split(",|\\|");
						one = array2[0];
						if (one.contains(" "))
							one = one.replace(" ", ", ");
						two = array2[1];
						if (two.contains(" "))
							two = two.replace(" ", ", ");
						String[] array3 = array2[0].split(" ");
						Collections.addAll(oneSet, array3);
						String[] array4 = array2[1].split(" ");
						Collections.addAll(twoSet, array4);
						if (3 == array2.length) {// 前三
							three = array2[2];
							if (three.contains(" "))
								three = three.replace(" ", ", ");
							String[] array5 = array2[2].split(" ");
							Collections.addAll(threeSet, array5);
						}
					} else if (22 == play || 23 == play) {// 乐选2,3
						String[] array = seleteNum.split(" ");
						for (int j = 0; j < array.length; j++) {
							if (j == 0) {
								one = array[0];
								oneSet.add(one);
							} else if (j == 1) {
								two = array[1];
								twoSet.add(two);
							} else if (j == 2) {
								three = array[2];
								threeSet.add(three);
							}

						}

					} else {
						String[] array6 = seleteNum.split(",");
						one = array6[0];
						one = one.substring(0, one.length() - 1);
						if (one.contains(" "))
							one = one.replace(" ", ", ");
						String[] array7 = one.split(" ");
						Collections.addAll(oneSet, array7);
						two = array6[1];
						two = two.substring(1, two.length());
						if (two.contains(" "))
							two = two.replace(" ", ", ");
						String[] array8 = two.split(" ");
						Collections.addAll(twoSet, array8);
					}
					// 直选二
					if (playType >= (play1 * 100 + 22)) {
						selectedNum.setShowLotteryNumber(seleteNum);
						selectedNum.setLotteryNumber(seleteNum);
					}
					// 直选二
					else if (playType == (play1 * 100 + 9)) {
						selectedNum.setShowLotteryNumber(one.replace(",", "")
								+ "," + two.replace(",", ""));
						selectedNum.setLotteryNumber(one.replace(",", "") + ","
								+ two.replace(",", ""));
					}
					// 前一
					else if (playType == (play1 * 100 + 1)) {
						selectedNum.setShowLotteryNumber(one);
						selectedNum.setLotteryNumber(one);
					}
					// 直选三
					else if (playType == (play1 * 100 + 10)) {
						selectedNum.setShowLotteryNumber(one.replace(",", "")
								+ "," + two.replace(",", "") + ","
								+ three.replace(",", ""));
						selectedNum.setLotteryNumber(one.replace(",", "") + ","
								+ two.replace(",", "") + ","
								+ three.replace(",", ""));
					}
					// 如果为胆拖的话
					else if (playType > (play1 * 100 + 12)) {
						selectedNum.setShowLotteryNumber("("
								+ one.replace(",", "") + ")"
								+ two.replace(",", ""));
						selectedNum.setLotteryNumber(one.replace(",", "")
								+ " , " + two.replace(",", ""));
					} else {
						selectedNum.setShowLotteryNumber(one.replace(",", "")
								+ " " + two.replace(",", "") + " "
								+ three.replace(",", ""));
						selectedNum.setLotteryNumber(one.replace(",", "") + " "
								+ two.replace(",", "") + " "
								+ three.replace(",", ""));
					}
					selectedNum.setNumber(one.replace(",", "") + "-"
							+ two.replace(",", "") + "-"
							+ three.replace(",", ""));
					selectedNum.setLotteryId(play1 + "");
					selectedNum.setPlayType(playType);
					switch (play) {
					case 1:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 1);
						break;
					case 2:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 2);
						break;
					case 3:
					case 12:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 3);
						break;
					case 4:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 4);
						break;
					case 5:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 5);
						break;
					case 6:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 6);
						break;
					case 7:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 7);
						break;
					case 8:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 8);
						break;
					case 9:
						AppTools.totalCount = NumberTools.get11X5zuer(oneSet,
								twoSet);
						break;
					case 10:
						AppTools.totalCount = NumberTools.get11X5zusan(oneSet,
								twoSet, threeSet);
						break;
					case 13:
					case 15:
						if (oneSet.size() == 1)
							AppTools.totalCount = twoSet.size();
						else
							AppTools.totalCount = 0;
						break;
					case 11:
						AppTools.totalCount = NumberTools.get11X5Count(
								oneSet.size(), 2);
						break;
					// 组选前三胆拖
					case 14:
					case 16:
						AppTools.totalCount = NumberTools.get11X5Count_dan(
								oneSet.size(), twoSet.size(), 3);
						break;
					case 17:
						AppTools.totalCount = NumberTools.get11X5Count_dan(
								oneSet.size(), twoSet.size(), 4);
						break;
					case 18:
						AppTools.totalCount = NumberTools.get11X5Count_dan(
								oneSet.size(), twoSet.size(), 5);
						break;
					case 19:
						AppTools.totalCount = NumberTools.get11X5Count_dan(
								oneSet.size(), twoSet.size(), 6);
						break;
					case 20:
						AppTools.totalCount = NumberTools.get11X5Count_dan(
								oneSet.size(), twoSet.size(), 7);
						break;
					case 21:
						AppTools.totalCount = NumberTools.get11X5Count_dan(
								oneSet.size(), twoSet.size(), 8);
						break;

					// 乐选玩法
					case 22:
					case 23:
					case 24:
					case 25:
						AppTools.totalCount = NumberTools.get11X5Count_lexuan(
								oneSet.size(), twoSet.size(), threeSet.size(),
								play);
						break;

					}
					selectedNum.setCount(AppTools.totalCount);
					selectedNum.setMoney(AppTools.totalCount * 2);
					AppTools.list_numbers.add(selectedNum);
				}
				break;
			/**
			 * 江苏快3
			 */
			case 8301:
			case 8302:
			case 8303:
			case 8304:
			case 8305:
			case 8306:
			case 8307:
			case 8308:
				for (int i = 0; i < numbers.length; i++) {// 每一注
					SelectedNumbers selectedNum = new SelectedNumbers();
					String seleteNum = numbers[i];
					if (numbers.length != 1 && i != numbers.length
							&& i != numbers.length - 1) {// 去除每一个方案后面的空格
						seleteNum = seleteNum.substring(0,
								seleteNum.length() - 2);
					}
					ArrayList<String> list1 = new ArrayList<String>();
					ArrayList<String> list2 = new ArrayList<String>();
					ArrayList<String> list3 = new ArrayList<String>();// 第三排布局
					String one = "";
					String two = "";
					if (playType == 8301) {// 和值
						one = seleteNum;
						list1.add(seleteNum);
						selectedNum.setShowLotteryNumber(one.replace(",", ""));
						selectedNum.setLotteryNumber(one.replace(",", "\n"));
					} else if (playType == 8302) {
						list1.add(seleteNum);
						selectedNum.setShowLotteryNumber("三同号通选");
						selectedNum.setLotteryNumber("三同号通选");
					} else if (playType == 8308) {
						list1.add(seleteNum);
						selectedNum.setShowLotteryNumber("三连号通选");
						selectedNum.setLotteryNumber("三连号通选");
					} else if (playType == 8303) {// 三同号单选
						for (int j = 1; j <= 6; j++) {
							if (seleteNum.contains(j + "")) {
								one = one + j + "";
							}
						}
						list1.add(one);
						String[] array = one.replace(" ", "").split(",");
						String endnumber = null;
						for (String string : array) {
							if (endnumber == null)
								endnumber = string + string + string;
							else
								endnumber += string + string + string;
							if (array.length != 1)
								endnumber = endnumber + "\n";
						}
						selectedNum.setShowLotteryNumber(endnumber.replace(
								"\n", " "));
						selectedNum.setLotteryNumber(endnumber);
					} else if (playType == 8305 || 8309 == playType
							|| 8310 == playType) {// 二同号单选
						String str1 = seleteNum.substring(1,
								seleteNum.length() - 1);// 去除两端括号
						Log.i(TAG, str1);
						String[] strArray = str1.split("\\)");
						for (int j = 1; j <= 6; j++) {
							if (strArray[0].contains(j + "")) {
								one = one + j + "";
								list1.add(j + "");
							}
							if (strArray[1].contains(j + "")) {
								two = two + j + "";
								list2.add(j + "");
							}
						}
						String[] array = one.replace(" ", "").split(",");
						two = two.replace(",", "");
						String endnumber = null;
						for (String string : array) {
							if (8309 == playType || 8310 == playType) {
								selectedNum.setShowLotteryNumber(one.replace(
										",", "") + " , " + two);
								selectedNum.setLotteryNumber(one.replace(",",
										"") + " , " + two);
							} else {
								two = two.replace(" ", "");
								if (endnumber == null)
									endnumber = "(" + string + string + ")"
											+ "(" + two + ")";
								else
									endnumber += "(" + string + string + ")"
											+ "(" + two + ")";
								if (array.length != 1)
									endnumber = endnumber + "\n";
								selectedNum.setShowLotteryNumber(endnumber
										.replace("\n", " "));
								selectedNum.setLotteryNumber(endnumber);
							}
						}
					} else if (playType == 8306 || playType == 8307) {// 三不同号、二不同号
						String[] strArray1 = seleteNum.split("  ");
						StringBuilder sb = new StringBuilder();
						for (int j = 0; j < strArray1.length; j++) {
							if (j == 0)
								sb.append(strArray1[j]);
							else
								sb.append(", ").append(strArray1[j]);
							list1.add(strArray1[j]);
						}
						one = sb.toString();
						selectedNum.setShowLotteryNumber(one.replace(",", ""));
						selectedNum.setLotteryNumber(one.replace(",", " "));
					} else if (playType == 8304) {// 三同号复选
						if (seleteNum.contains(",")) {// 复式
							seleteNum = seleteNum.substring(0,
									seleteNum.length() - 1);
							String[] strArray2 = seleteNum.split(",");
							StringBuilder sb1 = new StringBuilder();
							for (int j = 0; j < strArray2.length; j++) {
								if (j == 0)
									for (int j2 = 1; j2 <= 6; j2++) {
										if (strArray2[j].contains(j2 + "")) {
											sb1.append(j2).append("");
										}
									}
								else
									for (int j2 = 1; j2 <= 6; j2++) {
										if (strArray2[j].contains(j2 + "")) {
											sb1.append(", ").append(j2)
													.append("");
										}
									}
								list1.add(strArray2[j]);
							}
							one = sb1.toString();
						} else {// 单式
							for (int j2 = 1; j2 <= 6; j2++) {
								if (seleteNum.contains(j2 + "")) {
									one = j2 + "";
									list1.add(one);
								}

							}
						}
						String[] array = one.replace(" ", "").split(",");
						String endnumber = null;
						for (String string : array) {
							if (endnumber == null)
								endnumber = string + string + "*";
							else
								endnumber += string + string + "*";
							if (array.length != 1)
								endnumber = endnumber + ",";
						}

						selectedNum.setShowLotteryNumber(endnumber.replace(",",
								" "));
						selectedNum.setLotteryNumber(endnumber);
					}
					// selectedNum.setNumber(one.replace(",",""));
					selectedNum.setPlayType(playType);
					if (playType == 8305 || 8309 == playType
							|| 8310 == playType)
						AppTools.totalCount = NumberTools.getCountFor_k3(
								playType, list1, list2, list3);
					else {
						AppTools.totalCount = NumberTools
								.getCountFor_k3(playType, list1,
										new ArrayList<String>(), list3);
					}
					selectedNum.setCount(AppTools.totalCount);
					selectedNum.setMoney(AppTools.totalCount * 2);
					AppTools.list_numbers.add(selectedNum);
				}
				break;
			}
		}
		// } catch (Exception e) {
		// Log.w("继续投注错误", e.toString());
		// return false;
		// }
		return true;

	}

	/**
	 * 得到大乐透的注数方法
	 * 
	 * @param RedCount
	 *            ：红球选球数
	 * @param RedCount_dan
	 *            ：红球胆码选球数
	 * @param blueCount
	 *            ：篮球选球数
	 * @param BlueCount_dan
	 *            ：篮球胆码选球数
	 * @param type
	 *            ：玩法id
	 * @return
	 */
	public static long getCountForDLT(int RedCount, int RedCount_dan,
			int blueCount, int BlueCount_dan, int type) {
		// 后区胆拖
		if (type == 1) {
			if (RedCount >= 5 && BlueCount_dan > 0 && blueCount == 1) {
				return getzuhe(RedCount, 5) * getzuhe(BlueCount_dan, 1);
			} else
				return 0;
		}
		// 双区胆拖
		else {
			if (RedCount >= 1 && RedCount <= 4 && RedCount_dan > 1
					&& BlueCount_dan > 1 && blueCount == 1) {
				return getzuhe(RedCount_dan, 5 - RedCount)
						* getzuhe(BlueCount_dan, 1);
			} else
				return 0;
		}
	}// 得到下标m 上标n 的 组合数C（m，n）

	/**
	 * 计算注数方法
	 * 
	 * @param m
	 *            ：选球数
	 * @param n
	 *            ：需要组合的数
	 * @return
	 */
	public static long getzuhe(int m, int n) {
		if (m < n) {
			return -1;
		}
		if (1 == n) {
			return m;
		}
		if (m == n) {
			return 1;
		}
		long result = 1;

		while (n > 0) {
			result *= (m + 1 - n);
			result /= n;
			n--;
		}

		return result;
	}

	/**
	 * 格式化双色球
	 * 
	 * @param red
	 *            ：红球胆码list
	 * @param redTuo
	 *            ：红球拖码list
	 * @param blue
	 *            ：蓝球胆码list
	 * @param type
	 *            ：玩法type
	 * @return
	 */
	public static String changeSSQ(List<String> red, List<String> redTuo,
			List<String> blue, int type) {
		String num = "";
		String tuoNum = "";
		String blueNum = "";

		for (String str : red) {
			num += str + " ";
		}

		if (redTuo != null) {
			for (String str : redTuo) {
				tuoNum += str + " ";
			}
		}

		for (String str : blue) {
			blueNum += str + " ";
		}

		if (type == 501) {
			num = num.trim() + "-" + blueNum;
		} else if (type == 502) {
			num = num + "," + " " + tuoNum.trim() + "-" + blueNum.trim();
		}

		return num;
	}

	/**
	 * 格式化大乐透
	 * 
	 * @param red
	 *            ：红球胆码list
	 * @param redTuo
	 *            ：红球拖码list
	 * @param blue
	 *            ：蓝球胆码list
	 * @param blueTuo
	 *            ：蓝球拖码list
	 * @param type
	 *            ：玩法type
	 * @return
	 */
	public static String changeDLT(List<String> red, List<String> redTuo,
			List<String> blue, List<String> blueTuo, int type) {
		String num = "";
		String tuoNum = "";
		String blueNum = "";
		String tuoblueNum = "";
		for (String str : red) {
			num += str + " ";
		}
		if (redTuo != null) {
			for (String str : redTuo) {
				tuoNum += str + " ";
			}
		}
		for (String str : blue) {
			blueNum += str + " ";
		}
		if (blueTuo != null) {
			for (String str : blueTuo) {
				tuoblueNum += str + " ";
			}
		}
		if (type == 3901) {
			num = num.trim() + "-" + blueNum;
		} else if (type == 3903) {
			num = num + "," + " " + tuoNum.trim() + "-" + blueNum.trim();
		} else if (type == 3906) {
			num = num + "-" + " " + blueNum.trim() + " , " + tuoblueNum.trim();
		} else if (type == 3907) {
			num = num + "," + " " + tuoNum.trim() + " - " + blueNum.trim()
					+ " , " + tuoblueNum.trim();
		}
		return num;
	}

	/**
	 * 格式化胜负彩选号格式
	 * 
	 * @param MapNum
	 *            ：选号map
	 * @return
	 */
	public static String ChangeSFC(Map<Integer, String> MapNum) {
		String StrNum = "";
		String StrKey = "";
		List<Entry<Integer, String>> entry = sort(MapNum);
		for (int i = 0; i < entry.size(); i++) {
			String value = entry.get(i).getValue();
			if (value.length() > 1) {
				StrNum += "(" + value + ")";
			} else {
				StrNum += value;
			}
		}
		System.out.println("号码num：" + StrNum);
		return StrNum;
	}

	/**
	 * 格式化任选9选号格式
	 * 
	 * @param MapNum
	 *            ：选号map
	 * @return
	 */
	public static String ChangeRX9(Map<Integer, String> MapNum) {
		String StrNum = "";
		List<Entry<Integer, String>> sort = sort(MapNum);
		int k = 0;
		for (int i = 0; i < 14; i++) {
			boolean b = false;
			for (int j = 0; j < sort.size(); j++) {
				int key = sort.get(j).getKey();
				if (i == key) {
					b = true;
				}
			}
			if (b) {
				String value = sort.get(k).getValue();
				k = k + 1;
				if (value.length() > 1) {
					StrNum += "(" + value + ")";
				} else {
					StrNum += value;
				}
			} else {
				StrNum += "-";
			}
		}
		System.out.println("号码value：" + StrNum);
		return StrNum;
	}

	/**
	 * 给map排序
	 * 
	 * @param map
	 *            ：map对象
	 */
	private static List<Entry<Integer, String>> sort(Map<Integer, String> map) {
		List<Map.Entry<Integer, String>> mEntryList = new ArrayList<Map.Entry<Integer, String>>(
				map.entrySet());
		Collections.sort(mEntryList,
				new Comparator<Map.Entry<Integer, String>>() {

					@Override
					public int compare(
							Map.Entry<Integer, String> firstMapEntry,
							Map.Entry<Integer, String> secondMapEntry) {
						return firstMapEntry.getKey().compareTo(
								secondMapEntry.getKey());
					}
				});
		return mEntryList;
	}

	/**
	 * 胆拖投注 根据红球 和篮球个数获取注数（ 双色球 (S) 6+1大乐透(D)是 5+2）
	 * 
	 * @param RedCount
	 *            所选的胆区红球个数
	 * @param BlueCount
	 *            所选的蓝球个数
	 * @return
	 */
	public static long getCountForSSQ_tuo(int RedCount, int RedCountTuo,
			int BlueCount, int redNum, int blueNum) {
		// 如果没有选满数字 则返回 0
		if ((RedCount < 1) || (BlueCount < blueNum)
				|| (RedCountTuo < redNum - RedCount)) {
			return 0;
		}
		// 红球的排列总数 变量
		int RedInvestNum = 1;

		for (int i = 1; i <= (redNum - RedCount); i++) {
			RedInvestNum *= RedCountTuo;
			RedCountTuo--;
		}

		int RedInvestNum2 = 1;
		for (int i = 2; i <= (redNum - RedCount); i++) {
			RedInvestNum2 *= i;
		}

		RedInvestNum = RedInvestNum / RedInvestNum2;

		int BlueInvestNum = 1;
		for (int i = 1; i < blueNum + 1; i++) {
			BlueInvestNum *= BlueCount;
			BlueCount--;
		}

		int BlueInvestNum2 = 1;
		for (int i = 1; i < blueNum + 1; i++) {
			BlueInvestNum2 *= i;
		}
		BlueInvestNum = BlueInvestNum / BlueInvestNum2;
		long countsum = RedInvestNum * BlueInvestNum;
		if (countsum < 2) {
			countsum = 0;
		}

		return countsum;
	}

	/**
	 * 计算3D注数
	 * 
	 * @param playType
	 *            玩法
	 * @param hundreds
	 *            百位
	 * @param decade
	 *            十位
	 * @param units
	 *            个位
	 * @return index 区分组三 组六单式
	 */
	public static int getCountForFC3D(int playType, int hundreds, int decade,
			int units, HashSet<String> hezhiSet, HashSet<String> zixuanhezhiSet) {
		if ((playType == 0)) {
			return 0;
		}
		int count = 0;
		switch (playType) {
		case 605:
		case 6305:
		case 606:
		case 6306: // 组选和值
			int sum1 = 0,
			sum2 = 0;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					for (int j2 = 0; j2 < 10; j2++) {
						if (playType == 6306 || playType == 606) {
							for (String k : hezhiSet) {
								int vlues = Integer.parseInt(k);
								if (i != j && j != j2 && i != j2) {
									if (i + j + j2 == vlues) {
										sum1 += 1;
									}
								} else if ((i == j && j != j2)
										|| (i == j2 && j != j2)
										|| (j2 == j && i != j2))
									if (i + j + j2 == vlues) {
										sum2 += 1;
									}
								count = sum1 / 6 + sum2 / 3;
							}
						} else {
							for (String string : zixuanhezhiSet) {
								if (i + j + j2 == Integer.parseInt(string)) {
									count += 1;
								}
							}
						}
					}
				}
			}
			break;
		case 601:
		case 6301:
			count = hundreds * decade * units;
			break;
		case 602:
		case 6302:
//			if (!Select_PL3_Activity.flag) {
//				if (units < 1) {
//					count = 0;
//				} else
//					count = hundreds * decade;
//			} else
//				count = hundreds * decade;
			break;
		case 604:
		case 6304:
			if (hundreds >= 2) {
				count = hundreds * (hundreds - 1);
			}
			break;
		case 603:
		case 6303:
			if (hundreds > 3) {
				count = hundreds * (hundreds - 1) * (hundreds - 2) / 6;
			}
			break;
		}
		return count;
	}

	/**
	 * 3D注数算法
	 * 
	 * @param playType
	 *            ：玩法ID
	 * @param hundreds
	 *            ：百
	 * @param decade
	 *            ：十
	 * @param units
	 *            ：个
	 * @param hezhiSet
	 *            ：和值
	 * @param zixuanhezhiSet
	 *            ：组选和值
	 * @param daxiao
	 *            ：大小
	 * @param jiou
	 *            ：奇偶
	 * @param isCaidx
	 *            ：猜大小
	 * @param isTuolaji
	 *            ：拖拉机
	 * @return
	 */
	public static int getCountFor3D(int playType, int hundreds, int decade,
			int units, HashSet<String> hezhiSet,
			HashSet<String> zixuanhezhiSet, int daxiao, int jiou,
			boolean isCaidx, boolean isTuolaji) {
		if ((playType == 0)) {
			return 0;
		}
		int count = 0;
		switch (playType) {
		case 605:// 猜1D
		case 607:// 猜2D二同
			count = hundreds;
			break;
		case 610: // 和数
			count = hezhiSet.size();
			break;
		case 601:// 直选
			count = hundreds * decade * units;
			break;
		case 602:// 组三
			if (hundreds >= 2) {
				count = hundreds * (hundreds - 1);
			}
			break;
		case 603:// 组六
			if (hundreds > 2) {
				count = C_m_n(hundreds, 3);
			}
			break;
		case 604:// 1D
			count = hundreds + decade + units;
			break;
		case 606:// 2D
			if (1 > hundreds) {
				count = decade * units;
			} else if (1 > decade) {
				count = hundreds * units;
			} else if (1 > units) {
				count = hundreds * decade;
			} else {
				count = decade * units + hundreds * units + hundreds * decade;
			}
			break;
		case 608:// 猜2D-二不同
			if (hundreds >= 2) {
				count = C_m_n(hundreds, 2);
			}
			break;
		case 609:// 通选
		case 612:// 包选6
			count = C_m_n(hundreds, 1) * C_m_n(decade, 1) * C_m_n(units, 1);
			break;
		case 611:// 包选3
			count = hundreds * decade;
			break;
		case 613:
		case 616:
			if (daxiao > 0 || jiou > 0)
				count = 1;
			break;
		case 614:
		case 615:
			if (isCaidx || isTuolaji)
				count = 1;
			break;
		}
		return count;
	}

	/**
	 * 
	 * @param playType
	 *            :玩法ID
	 * @param bai
	 *            ：百
	 * @param shi
	 *            ：十
	 * @param ge
	 *            ：个
	 * @return
	 */
	public static int getCountFor3D(int playType, HashSet<String> bai,
			HashSet<String> shi, HashSet<String> ge) {
		int count = 0;
		if (playType == 611) {
			if (bai.equals(shi)) {
				if (ge.size() > 0 && !ge.containsAll(shi)) {
					count = ge.size();
				}
			} else if (bai.equals(ge)) {
				if (shi.size() > 0 && !shi.containsAll(ge)) {
					count = shi.size();
				}
			} else if (shi.equals(ge)) {
				if (bai.size() > 0 && !bai.containsAll(ge)) {
					count = bai.size();
				}
			}
		}
		return count;
	}

	/**
	 * 计算排列m选n
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static int C_m_n(int m, int n) {
		int a = 1;
		for (int i = 0; i < n; i++) {
			a *= m - i;
		}
		int b = 1;
		for (int j = 0; j < n; j++) {
			b *= n - j;
		}
		return a / b;
	}

	/**
	 * 计算时时乐注数
	 * 
	 * @param playType
	 *            玩法
	 * @param hundreds
	 *            百位
	 * @param decade
	 *            十位
	 * @param units
	 *            个位
	 * @return index 区分组三 组六单式
	 */
	public static int getCountForSSL(int playType, int hundreds, int decade,
			int units) {
		if ((playType == 0)) {
			return 0;
		}
		int count = 0;
		switch (playType) {
		case 2902:
			count = hundreds * decade * units;
			break;
		case 2905:
			if (units < 3) {
				count = hundreds * decade;
				break;
			} else {
				if (units >= 3)
					count = (units - 2) * 6 + (units - 3) * (units - 4);
				break;
			}
		case 2904:
			if (hundreds >= 3) {
				count = hundreds * (hundreds - 1) * (hundreds - 2) / 6;
			}
			break;
		case 2908:
			count = hundreds * decade;
			break;
		case 2909:
			count = decade * units;
			break;
		case 2910:
			count = hundreds;
			break;
		case 2911:
			count = units;
			break;
		}
		return count;
	}

	/**
	 * 得到随机数
	 * 
	 * @param count
	 *            随机数个数
	 * @param max
	 *            随机数产生的最大数
	 * @param min
	 *            随机数产生的最小数
	 * @return
	 */
	public static ArrayList<String> getRandomNumk_3(int count, int min, int max) {
		ArrayList<String> list = new ArrayList<String>();
		String str = "";
		while (list.size() < count) {
			int a = (int) ((Math.random() * (max - min + 1)) + min);
			if (a == 0)
				continue;
			str = "" + a;
			if (list.contains(str)) {
				continue;
			}

			list.add(str);
		}
		return list;
	}

	/**
	 * 计算七星彩注数
	 * 
	 * @param playType
	 *            玩法
	 * @param yi
	 *            第一位
	 * @param er
	 *            第二位
	 * @param san
	 *            第三位
	 * @param si
	 *            第四位
	 * @param wu
	 *            第五位
	 * @param liu
	 *            第六位
	 * @param qi
	 *            第七位
	 * @return 注数
	 */
	public static long getCountForQXC(int playType, int yi, int er, int san,
			int si, int wu, int liu, int qi) {
		if (playType == 0 || yi < 1 || er < 1 || san < 1 || si < 1 || wu < 1
				|| liu < 1 || qi < 1) {
			return 0;
		}
		int count = 0;
		count = yi * er * san * si * wu * liu * qi;
		return count;
	}

	/**
	 * 四场进球的算法
	 * 
	 * @param playType
	 * @param oneH
	 * @param oneG
	 * @param twoH
	 * @param twoG
	 * @param threeH
	 * @param threeG
	 * @param fourH
	 * @param fourG
	 * @return
	 */
	public static long getCountForSCJQ(int playType, int oneH, int oneG,
			int twoH, int twoG, int threeH, int threeG, int fourH, int fourG) {
		// TODO Auto-generated method stub
		if (playType == 0 || oneH < 1 || oneG < 1 || twoH < 1 || twoG < 1
				|| threeH < 1 || threeG < 1 || fourH < 1 || fourG < 1) {
			return 0;
		}
		int count = 0;
		count = oneH * oneG * twoH * twoG * threeH * threeG * fourH * fourG;
		return count;
	}

	/**
	 * 胜负彩注数算法
	 * 
	 * @param btnMap
	 *            ：选球map
	 * @return
	 */
	public static int getCountForSFC(Map<Integer, String> btnMap) {
		int count = 0;

		if (btnMap.size() < 14) {
			count = 0;
		} else {
			count = btnMap.get(0).length() * btnMap.get(1).length()
					* btnMap.get(2).length() * btnMap.get(3).length()
					* btnMap.get(4).length() * btnMap.get(5).length()
					* btnMap.get(6).length() * btnMap.get(7).length()
					* btnMap.get(8).length() * btnMap.get(9).length()
					* btnMap.get(10).length() * btnMap.get(11).length()
					* btnMap.get(12).length() * btnMap.get(13).length();
		}

		return count;
	}

	/**
	 * 任选九的注数算法
	 * 
	 * @param btnMap
	 *            ：选球map
	 * @return
	 */
	public static int getCountForRX9(Map<Integer, String> btnMap) {
		int count = 0;
		List<String> list = new ArrayList<String>();
		Collection<String> values = btnMap.values();
		for (String s : values) {
			list.add(s);
		}
		count = getAll9G1Mixed(list);
		return count;
	}

	/**
	 * 计算排列五注数
	 * 
	 * @param playType
	 *            玩法
	 * @param yi
	 *            第一位
	 * @param er
	 *            第二位
	 * @param san
	 *            第三位
	 * @param si
	 *            第四位
	 * @param wu
	 *            第五位
	 * @return 注数
	 */
	public static long getCountForPL5(int playType, int yi, int er, int san,
			int si, int wu) {
		if (playType == 0 || yi < 1 || er < 1 || san < 1 || si < 1 || wu < 1) {
			return 0;
		}
		int count = 0;
		count = yi * er * san * si * wu;
		return count;
	}

	/**
	 * 得到随机数
	 * 
	 * @param count
	 *            随机数个数
	 * @param max
	 *            随机数产生的最大数
	 * @param type
	 *            是否加 0
	 * @return
	 */
	public static ArrayList<String> getRandomNum7(int count, int max,
			boolean type) {
		ArrayList<String> list = new ArrayList<String>();
		String str = "";
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));
			if (a == 0)
				continue;

			if (type) {
				if (a < 10)
					str = "0" + a;
				else
					str = "" + a;
			} else {
				str = "" + a;
			}

			if (list.contains(str)) {
				continue;
			}

			list.add(str);
		}
		return list;
	}

	/**
	 * 得到随机数
	 * 
	 * @param count
	 *            随机数个数
	 * @param max
	 *            随机数产生的最大数
	 */
	public static ArrayList<String> getRandomNum(int count, int max) {
		ArrayList<String> list = new ArrayList<String>();
		String str = "";
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));
			str = "" + a;
			if (list.contains(str)) {
				continue;
			}
			list.add(str);
		}
		return list;
	}

	/**
	 * 根据集合得到随机数集合
	 * 
	 * @param except
	 *            ：集合
	 * @param count
	 *            ：随机数个数
	 * @param max
	 *            ：随机数中最大可能值
	 * @return
	 */
	public static ArrayList<String> getRandomNum(List<String> except,
			int count, int max) {
		ArrayList<String> list = new ArrayList<String>();
		String str = "";
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));
			str = "" + a;
			if (list.contains(str) || except.contains(str)) {
				continue;
			}
			list.add(str);
		}
		return list;
	}

	/**
	 * 随机数集合
	 * 
	 * @param count
	 *            ：随机个数
	 * @param max
	 *            ：随机数最大值
	 * @return
	 */
	public static List<Integer> getRandomNumInt(int count, int max) {
		List<Integer> list = new ArrayList<Integer>();
		int i;
		while (list.size() < count) {
			i = (int) (Math.random() * (max + 1));
			if (list.contains(i)) {
				continue;
			}
			list.add(i);
		}
		return list;
	}

	/**
	 * 得到随机数数组
	 * 
	 * @param count
	 *            几注
	 * @param redNum
	 *            红球个数
	 * @param blueNum
	 *            蓝球个数
	 * @param max1
	 *            红球最大数
	 * @param max2
	 *            蓝球最大数
	 * @param type
	 *            是否加 0
	 * @return
	 */
	public static String[] getRandom(int count, int redNum, int blueNum,
			int max1, int max2, boolean type, boolean canZero) {
		String[] str = new String[count];
		for (int i = 0; i < count; i++) {
			List<String> red = new ArrayList<String>();
			List<String> blue = new ArrayList<String>();
			while (red.size() < redNum) {
				String s = "";
				int a = (int) (Math.random() * (max1 + 1));
				if (!canZero) {
					if (a == 0)
						continue;
				}

				if (type) {
					if (a < 10)
						s = s + " 0" + a;
					else
						s = s + " " + a;
				} else {
					s = s + " " + a;
				}

				if (red.contains(s)) {
					continue;
				}

				red.add(s);
			}

			while (blue.size() < blueNum) {
				String s = "";
				int a = (int) (Math.random() * (max2 + 1));
				if (a == 0)
					continue;

				if (type) {
					if (a < 10)
						s = s + " 0" + a;
					else
						s = s + " " + a;
				} else {
					s = s + " " + a;
				}

				if (blue.contains(s)) {
					continue;
				}

				blue.add(s);
			}

			Collections.sort(red);
			Collections.sort(blue);
			if (blueNum != 0) {
				str[i] = red.toString().trim().replace("[", "")
						.replace("]", "").replace(",", "")
						+ "-"
						+ blue.toString().trim().replace("[", "")
								.replace("]", "").replace(",", "");
			} else {
				str[i] = red.toString().trim().replace("[", "")
						.replace("]", "").replace(",", "");
			}
		}

		return str;
	}

	/**
	 * 得到随机数
	 * 
	 * @param count
	 *            随机数个数
	 * @param max
	 *            随机数产生的最大数
	 * @return
	 */
	public static HashSet<String> getRandomNum2(int count, int max) {
		HashSet<String> list = new HashSet<String>();
		String str = "";
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));
			if (a == 0)
				continue;
			if (a < 10)
				str = "0" + a;
			else
				str = "" + a;
			if (list.contains(str)) {
				continue;
			}
			list.add(str);
		}
		return list;
	}

	/**
	 * 得到随机数
	 * 
	 * @param count
	 *            随机数个数
	 * @param max
	 *            随机数产生的最大数(不包含)
	 * @return [0, max) 前面不加 0
	 */
	public static HashSet<String> getRandomNum3(int count, int max) {

		HashSet<String> list = new HashSet<String>();
		while (list.size() < count) {
			int a = (int) (Math.random() * (max));
			if (list.contains(a + "")) {

				continue;
			}
			list.add(a + "");

		}

		return list;
	}

	/**
	 * PL3 pl5 得到随机数
	 * 
	 * @return (0, max) 前面不加 0 //
	 */
	public static ArrayList<String> getRandomNum4(int count, int max) {
		ArrayList<String> list = new ArrayList<String>();

		while (list.size() < count) {
			int a = (int) (Math.random() * (max));
			if (list.contains(a + "")) {

				continue;
			}
			list.add(a + "");

		}
		return list;
	}

	/**
	 * wulin 得到随机数
	 * <p/>
	 * ======= /** 快赢481 / 幸运彩 得到随机数 不含0 不含01 02 数可重性
	 * 
	 * @return (0, max) 前面不加 0
	 */
	public static ArrayList<String> getRandomNum8(int count, int max,
			boolean iszero) {
		ArrayList<String> list = new ArrayList<String>();
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));

			if (iszero != true && a == 0)
				continue;

			list.add(a + "");
		}
		return list;
	}

	/**
	 * @return (0, max) 前面不加 0
	 */
	public static ArrayList<String> getRandomNum6(int count, int max) {
		ArrayList<String> list = new ArrayList<String>();
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));
			if (a == 0)
				continue;
			if (list.contains(a + "")) {
				continue;
			}

			list.add(a + "");
		}
		return list;
	}

	/**
	 * 得到随机数
	 * 
	 * @return (0, max) 前面加 0
	 */
	public static ArrayList<String> getRandomNum5(int count, int max) {
		ArrayList<String> list = new ArrayList<String>();
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));
			String b = "";
			if (a == 0)
				continue;
			if (a <= 9)
				b = "0" + a;
			else
				b = a + "";

			if (list.contains(b)) {
				continue;
			}
			Log.i("x", "随机数 ==== " + a);

			list.add(b);
		}
		return list;
	}

	/**
	 * 得到随机数集合
	 * 
	 * @param count
	 *            ：随机个数
	 * @param max
	 *            ：随机最大值
	 * @param set
	 *            ：已有集合
	 * @return
	 */
	public static ArrayList<String> getRandomNum(int count, int max,
			HashSet<String> set) {
		ArrayList<String> list = new ArrayList<String>();
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));
			if (a == 0)
				continue;

			String b = "";
			if (a == 0)
				continue;
			if (a <= 9)
				b = "0" + a;
			else
				b = a + "";

			if (list.contains(b)) {
				continue;
			}
			if (set.contains(b)) {
				continue;
			}
			list.add(b);
		}
		return list;
	}

	/**
	 * 得到随机数集合
	 * 
	 * @param count
	 *            ：随机个数
	 * @param max
	 *            ：随机最大值
	 * @param set
	 *            ：已有集合
	 * @return
	 */
	public static ArrayList<String> getRandomNum2(int count, int max,
			HashSet<String> set) {
		ArrayList<String> list = new ArrayList<String>();
		while (list.size() < count) {
			int a = (int) (Math.random() * (max + 1));
			if (a == 0)
				continue;

			if (a == 0)
				continue;

			if (list.contains(a + "")) {
				continue;
			}
			if (set.contains(a + "")) {
				continue;
			}

			Log.i("x", "摇一摇的数据=== " + a);
			list.add(a + "");
		}
		return list;
	}

	/**
	 * 得到随机数集合
	 * 
	 * @param count
	 *            ：随机个数
	 * @param max
	 *            ：随机最大值
	 * @param set
	 *            ：已有集合
	 * @param set2
	 *            ：已有集合2
	 * @return
	 */

	public static ArrayList<String> getRandomNum(int count, int max,
			HashSet<String> set, HashSet<String> set2) {
		ArrayList<String> list = new ArrayList<String>();
		while (list.size() < count) {
			int a = (int) (Math.random() * (max));
			String b = "";
			if (a == 0)
				continue;
			if (a <= 9)
				b = "0" + a;
			else
				b = a + "";
			if (list.contains(b)) {
				continue;
			}
			if (set.contains(b)) {
				continue;
			}
			if (set2.contains(b)) {
				continue;
			}
			list.add(b);
		}
		return list;
	}

	/**
	 * 得到随机数集合
	 * 
	 * @param count
	 *            ：随机个数
	 * @param max
	 *            ：随机最大值
	 * @param set
	 *            ：已有集合
	 * @param set2
	 *            ：已有集合2
	 * @return
	 */
	public static ArrayList<String> getRandomNum2(int count, int max,
			HashSet<String> set, HashSet<String> set2) {
		ArrayList<String> list = new ArrayList<String>();
		while (list.size() < count) {
			int a = (int) (Math.random() * (max));

			if (list.contains(a + "")) {
				continue;
			}
			if (set.contains(a + "")) {
				continue;
			}
			if (set2.contains(a + "")) {
				continue;
			}
			list.add(a + "");
		}
		return list;
	}

	public static int getRandomNum(int max) {
		return (int) (Math.random() * max);
	}

	/*
	 * 北京单场胆托算
	 */
	/**
	 * 2串3* 北京单场 2个单关＋1个2串1
	 */
	public static int getAll2G3Mixed_bjdc(List<String> list) {
		int count = 0;
		count += getAll2G1Mixed(list);
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = i + 1; j < list.size(); j++) {
				count += list.get(i).length() + list.get(j).length();
			}
		}
		return count;
	}

	/**
	 * 3串7* 北京单场 单关＋3个2串1 +1个3串1
	 */
	public static int getAll3G7Mixed_bjdc(List<String> list) {
		int count = 0;
		count += getAll3G4Mixed(list);
		for (int i = 0; i < list.size() - 2; i++) {
			for (int j = i + 1; j < list.size() - 1; j++) {
				for (int j2 = j + 1; j2 < list.size(); j2++) {
					count += list.get(i).length() + list.get(j).length()
							+ list.get(j2).length();
				}
			}
		}
		return count;
	}

	/**
	 * 4串15* 北京单场 3个单关＋3个2串1 +1个3串1
	 */
	public static int getAll4G15Mixed_bjdc(List<String> list) {
		int count = 0;
		count += getAll4G11Mixed(list);
		for (int i = 0; i < list.size() - 3; i++) {
			for (int j = i + 1; j < list.size() - 2; j++) {
				for (int j2 = j + 1; j2 < list.size() - 1; j2++) {
					for (int k = j2 + 1; k < list.size(); k++) {
						count += list.get(i).length() + list.get(j).length()
								+ list.get(j2).length() + list.get(k).length();
					}
				}
			}
		}
		return count;
	}

	/**
	 * 5串31* 北京单场 3个单关＋3个2串1 +1个3串1
	 */
	public static int getAll5G31Mixed_bjdc(List<String> list) {
		int count = 0;
		count += getAll5G26Mixed(list);
		for (int i = 0; i < list.size() - 4; i++) {
			for (int j = i + 1; j < list.size() - 3; j++) {
				for (int j2 = j + 1; j2 < list.size() - 2; j2++) {
					for (int k = j2 + 1; k < list.size() - 1; k++) {
						for (int k2 = k + 1; k2 < list.size(); k2++) {
							count += list.get(i).length()
									+ list.get(j).length()
									+ list.get(j2).length()
									+ list.get(k).length()
									+ list.get(k2).length();
						}
					}
				}
			}
		}
		return count;
	}

	/**
	 * 6串63* 北京单场 3个单关＋3个2串1 +1个3串1
	 */
	public static int getAll6G63Mixed_bjdc(List<String> list) {
		int count = 0;
		count += getAll6G57Mixed(list);
		for (int i = 0; i < list.size() - 5; i++) {
			for (int j = i + 1; j < list.size() - 4; j++) {
				for (int j2 = j + 1; j2 < list.size() - 3; j2++) {
					for (int k = j2 + 1; k < list.size() - 2; k++) {
						for (int k2 = k + 1; k2 < list.size() - 1; k2++) {
							for (int l = k2 + 1; l < list.size(); l++) {
								count += list.get(i).length()
										+ list.get(j).length()
										+ list.get(j2).length()
										+ list.get(k).length()
										+ list.get(k2).length()
										+ list.get(l).length();
							}

						}
					}
				}
			}
		}
		return count;
	}

	/*
	 * 2串3 胆托 北京单场
	 */
	public static int getAll2G3Mixed_dan_bjdc(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		if (list_dan.size() != 1)
			return 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i).length() * list_dan.get(0).length();
			sum += list.get(i).length() + list_dan.get(0).length();
		}
		return sum;
	}

	/*
	 * 3串7 胆托 北京单场
	 */
	public static int getAll3G7Mixed_dan_bjdc(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum += getAll3G4Mixed_dan(list_dan, list);
		switch (list_dan.size()) {
		case 1:
			for (int i = 0; i < list.size() - 1; i++) {
				for (int j = i + 1; j < list.size(); j++) {
					sum += list_dan.get(0).length() + list.get(i).length()
							+ list.get(j).length();
				}
			}
			break;
		case 2:
			for (int i = 0; i < list.size(); i++) {
				sum += list_dan.get(0).length() + list_dan.get(1).length()
						+ list.get(i).length();
			}
			break;
		default:
			break;
		}
		return sum;
	}

	/*
	 * 4串15 胆托 北京单场
	 */
	public static int getAll4G15Mixed_dan_bjdc(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum += NumberTools.getAll4G6_11Mixed_dan(list_dan, list, 11);
		switch (list_dan.size()) {
		case 1:
			for (int i = 0; i < list.size() - 2; i++) {
				for (int j = i + 1; j < list.size() - 1; j++) {
					for (int j2 = j + 1; j2 < list.size(); j2++) {
						sum += list_dan.get(0).length() + list.get(i).length()
								+ list.get(j).length() + list.get(j2).length();
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < list.size() - 1; i++) {
				for (int j = i + 1; j < list.size(); j++) {
					sum += list.get(i).length() + list.get(j).length()
							+ list_dan.get(0).length()
							+ list_dan.get(1).length();
				}
			}
			break;
		case 3:
			for (int i = 0; i < list.size(); i++) {
				sum += list.get(i).length() + list_dan.get(0).length()
						+ list_dan.get(1).length() + list_dan.get(2).length();
			}
			break;
		default:
			break;
		}
		return sum;
	}

	/*
	 * 5串31 胆托 北京单场
	 */
	public static int getAll6G63Mixed_dan_bjdc(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum += NumberTools.getAll6G57Mixed_dan(list_dan, list);
		switch (list_dan.size()) {
		case 1:
			for (int i = 0; i < list.size() - 4; i++) {
				for (int j = i + 1; j < list.size() - 3; j++) {
					for (int j2 = j + 1; j2 < list.size() - 2; j2++) {
						for (int k = j2 + 1; k < list.size() - 1; k++) {
							for (int k2 = k + 1; k2 < list.size(); k2++) {
								sum += list_dan.get(0).length()
										+ list.get(i).length()
										+ list.get(j).length()
										+ list.get(j2).length()
										+ list.get(k).length()
										+ list.get(k2).length();
							}
						}
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < list.size() - 3; i++) {
				for (int j = i + 1; j < list.size() - 2; j++) {
					for (int j2 = j + 1; j2 < list.size() - 1; j2++) {
						for (int k = j2 + 1; k < list.size(); k++) {
							sum += list_dan.get(0).length()
									+ list_dan.get(1).length()
									+ list.get(i).length()
									+ list.get(j).length()
									+ list.get(j2).length()
									+ list.get(k).length();
						}
					}
				}
			}
			break;
		case 3:
			for (int i = 0; i < list.size() - 2; i++) {
				for (int j = i + 1; j < list.size() - 1; j++) {
					for (int j2 = j + 1; j2 < list.size(); j2++) {
						sum += list_dan.get(0).length()
								+ list_dan.get(1).length()
								+ list_dan.get(2).length()
								+ list.get(i).length() + list.get(j).length()
								+ list.get(j2).length();
					}
				}
			}
			break;
		case 4:
			for (int i = 0; i < list.size() - 1; i++) {
				for (int j = i + 1; j < list.size(); j++) {
					sum += list_dan.get(0).length() + list_dan.get(1).length()
							+ list_dan.get(2).length()
							+ list_dan.get(3).length() + list.get(i).length()
							+ list.get(j).length();
				}
			}
			break;
		case 5:
			for (int i = 0; i < list.size(); i++) {
				sum += list_dan.get(0).length() + list_dan.get(1).length()
						+ list_dan.get(2).length() + list_dan.get(3).length()
						+ list_dan.get(4).length() + list.get(i).length();
			}
			break;
		default:
			break;
		}
		return sum;
	}

	/**
	 * 投注前号码格式转换
	 * 
	 * @param playType
	 *            玩法ID
	 * @param lotteryNumber
	 *            彩票号码
	 * @return 转换之后的彩票号码
	 */
	public static String lotteryNumberFormatConvert(int playType,
			String lotteryNumber) {
		if (lotteryNumber != "") {
			String[] tempLotterys = new String[] {};
			switch (playType) {
			case 601:
			case 6301:
				if (lotteryNumber.contains("(")) {
					break;
				}
				if (lotteryNumber.length() == 3) {
					break;
				}
				tempLotterys = lotteryNumber.split(" ");

				if (tempLotterys.length > 0) {
					lotteryNumber = "";
					for (String string : tempLotterys) {
						if (string.length() > 1) {
							lotteryNumber += "(" + string + ")";
						} else {
							lotteryNumber += string;
						}
					}

				}
				break;
			case 301:
				if (lotteryNumber.contains("(")) {
					break;
				}
				if (lotteryNumber.length() == 7) {
					break;
				}
				tempLotterys = lotteryNumber.split(" ");

				if (tempLotterys.length > 0) {
					lotteryNumber = "";
					for (String string : tempLotterys) {
						if (string.length() > 1) {
							lotteryNumber += "(" + string + ")";
						} else {
							lotteryNumber += string;
						}
					}
				}
				break;
			case 6401:
				if (lotteryNumber.contains("(")) {
					break;
				}
				if (lotteryNumber.length() == 5) {
					break;
				}
				tempLotterys = lotteryNumber.split(" ");

				if (tempLotterys.length > 0) {
					lotteryNumber = "";
					for (String string : tempLotterys) {
						if (string.length() > 1) {
							lotteryNumber += "(" + string + ")";
						} else {
							lotteryNumber += string;
						}
					}
				}
				break;
			}
		}
		return lotteryNumber;
	}

	/**
	 * 乐选玩法
	 * 
	 * @param number
	 * @param size
	 * @return
	 */
	public static int get11X5Count_lexuan(int number, int number2, int number3,
			int play) {
		int total = 0;

		switch (play) {
		case 22:
			if (number * number2 == 1) {
				total = 3;
			}
			break;
		case 23:
			if (number * number2 * number3 == 1) {
				total = 3;
			}
			break;

		case 24:
			if (number == 4) {
				total = 5;
			}
			break;

		case 25:
			if (number == 5) {
				total = 7;
			}
			break;

		default:
			break;
		}
		return total;
	}

	/**
	 * 11运夺金 任选（二三四五六七八）前（二三）组选 时时彩 一星复式，二星组选
	 */
	public static int get11X5Count(int number, int size) {
		int total = 1;

		if (size > 1 && size < 9) {
			total = C_m_n(number, size);
		} else {

			if (number == 0 || number < size)
				return 0;
			if (size == 1)
				return number;

			for (int i = 0; i < size; i++) {
				total *= number;
				number--;
			}
			int count = 1;
			for (int i = size; i > 0; i--) {
				count *= i;
			}

			total = total / count;
		}
		return total;
	}

	public static int get11X5zuer(HashSet<String> list1, HashSet<String> list2) {
		int d = 0; //
		for (String string1 : list1) {
			for (String string2 : list2) {
				if (!string1.equals(string2)) {
					d += 1;
				}
			}
		}
		return d;
	}

	public static int get11X5zusan(HashSet<String> list1,
			HashSet<String> list2, HashSet<String> list3) {
		int d = 0; //

		for (String string1 : list1) {
			for (String string2 : list2) {
				for (String string3 : list3) {
					if (!string1.equals(string2) && !string3.equals(string1)
							&& !string3.equals(string2)) {
						d += 1;
					}
				}
			}
		}
		return d;
	}

	public static int get11X5Count_dan(int number, int number_tuo, int size) {
		if (number == 0 || number >= size)
			return 0;
		if (number + number_tuo == size)
			return 1;
		if (size - number == 1)
			return number_tuo;

		int total = 1;

		for (int i = 0; i < size - number; i++) {
			total *= number_tuo;
			number_tuo--;
		}

		int count = 1;
		for (int i = size - number; i > 0; i--) {
			count *= i;
		}

		total = total / count;
		return total;
	}

	/**
	 * 重庆时时彩的注数算法 *
	 */
	public static int getSSC_count(HashSet<String> one, HashSet<String> two,
			HashSet<String> three, HashSet<String> four, HashSet<String> five,
			int type) {
		int total = 0;
		switch (type) {
		case 1:
			total = one.size();
			break;
		case 2:
			total = one.size() * two.size();
			break;
		case 3:
			total = one.size() * (one.size() - 1) / 2;
			break;
		case 4:
			total = one.size() * two.size() * three.size();
			break;
		case 5:
			total = one.size() * (one.size() - 1);
			break;
		case 6:
			total = one.size() * (one.size() - 1) * (one.size() - 2) / 6;
			break;
		case 7:
		case 8:
			total = one.size() * two.size() * three.size() * four.size()
					* five.size();
			break;
		case 9:
			total = 1;
			break;

		case 10:// 组三包胆
			int size1 = one.size();
			int size2 = two.size();
			if (0 != size1) {
				if (0 != size2) {
					total = 10;
				} else {
					total = 55;
				}
			}
			break;
		case 11:// 组三和值
			if (0 != one.size()) {
				for (String str : one) {
					int number = Integer.parseInt(str);
					switch (number) {
					case 0:
					case 1:
					case 26:
					case 27:
						total += 1;
						break;
					case 2:
					case 25:
						total += 2;
						break;
					case 3:
					case 24:
						total += 3;
						break;
					case 4:
					case 23:
						total += 4;
						break;
					case 5:
					case 22:
						total += 5;
						break;
					case 6:
					case 21:
						total += 7;
						break;
					case 7:
					case 20:
						total += 8;
						break;
					case 8:
					case 19:
						total += 10;
						break;
					case 9:
					case 18:
						total += 12;
						break;
					case 10:
					case 17:
						total += 13;
						break;
					case 11:
					case 16:
						total += 14;
						break;
					case 12:
					case 15:
					case 13:
					case 14:
						total += 15;
						break;
					}
				}
			}
			break;
		default:
			break;
		}
		return total;
	}

	/**
	 * 重庆时时彩的注数算法 *
	 */
	public static int getXJSSC_count(HashSet<String> one, HashSet<String> two,
			HashSet<String> three, HashSet<String> four, HashSet<String> five,
			int type) {
		int total = 0;
		switch (type) {
		case 1:
			total = one.size();
			break;
		case 2:
			total = one.size() * two.size();
			break;
		case 3:
			total = one.size() * (one.size() - 1) / 2;
			break;
		case 5:
			total = one.size() * two.size() * three.size();
			break;
		case 6:
			total = one.size() * (one.size() - 1);
			break;
		case 7:
			total = one.size() * (one.size() - 1) * (one.size() - 2) / 6;
			break;
		case 10:
		case 11:
			total = one.size() * two.size() * three.size() * four.size()
					* five.size();
			break;
		case 12:
			total = 1;
			break;

		case 9:// 四星直选
			total = one.size() * two.size() * three.size() * four.size();
			break;

		case 4:// 二星任选
			List<HashSet<String>> listren = new ArrayList<HashSet<String>>();
			int size = 0;
			if (0 != one.size()) {
				listren.add(one);
				size++;
			}
			if (0 != two.size()) {
				listren.add(two);
				size++;
			}
			if (0 != three.size()) {
				listren.add(three);
				size++;
			}
			if (0 != four.size()) {
				listren.add(four);
				size++;
			}
			if (0 != five.size()) {
				listren.add(five);
				size++;
			}
			if (size > 1) {
				for (int i = 0; i < listren.size(); i++) {
					for (int j = i + 1; j < listren.size(); j++) {
						total += listren.get(i).size() * listren.get(j).size();
					}
				}
			}
			break;

		case 8:// 三星任选
			List<HashSet<String>> list1 = new ArrayList<HashSet<String>>();
			int size1 = 0;
			if (0 != one.size()) {
				list1.add(one);
				size1++;
			}
			if (0 != two.size()) {
				list1.add(two);
				size1++;
			}
			if (0 != three.size()) {
				list1.add(three);
				size1++;
			}
			if (0 != four.size()) {
				list1.add(four);
				size1++;
			}
			if (0 != five.size()) {
				list1.add(five);
				size1++;
			}
			if (size1 > 2) {
				for (int i = 0; i < list1.size(); i++) {
					for (int j = i + 1; j < list1.size(); j++) {
						for (int k = j + 1; k < list1.size(); k++) {
							total += list1.get(i).size() * list1.get(j).size()
									* list1.get(k).size();
						}
					}
				}
			}
			break;
		default:
			break;
		}
		return total;
	}

	/**
	 * 江西时时彩的注数算法 * 在Select_JXSSCActivity 中删除了几种已被屏蔽的玩法，这里要改变位置。。
	 */
	public static int getJXSSC_count(HashSet<String> one, HashSet<String> two,
			HashSet<String> three, HashSet<String> four, HashSet<String> five,
			int type) {
		int total = 0;
		switch (type) {
		case 1:// 任一
			total = one.size() + two.size() + three.size() + four.size()
					+ five.size();
			break;
		case 2:// 任二
			List<HashSet<String>> list = new ArrayList<HashSet<String>>();
			int size = 0;
			if (0 != one.size()) {
				list.add(one);
				size++;
			}
			if (0 != two.size()) {
				list.add(two);
				size++;
			}
			if (0 != three.size()) {
				list.add(three);
				size++;
			}
			if (0 != four.size()) {
				list.add(four);
				size++;
			}
			if (0 != five.size()) {
				list.add(five);
				size++;
			}
			if (size > 1) {
				for (int i = 0; i < list.size(); i++) {
					for (int j = i + 1; j < list.size(); j++) {
						total += list.get(i).size() * list.get(j).size();
					}
				}
			}
			break;
		case 3:// 任三
			List<HashSet<String>> list1 = new ArrayList<HashSet<String>>();
			int size1 = 0;
			if (0 != one.size()) {
				list1.add(one);
				size1++;
			}
			if (0 != two.size()) {
				list1.add(two);
				size1++;
			}
			if (0 != three.size()) {
				list1.add(three);
				size1++;
			}
			if (0 != four.size()) {
				list1.add(four);
				size1++;
			}
			if (0 != five.size()) {
				list1.add(five);
				size1++;
			}
			if (size1 > 2) {
				for (int i = 0; i < list1.size(); i++) {
					for (int j = i + 1; j < list1.size(); j++) {
						for (int k = j + 1; k < list1.size(); k++) {
							total += list1.get(i).size() * list1.get(j).size()
									* list1.get(k).size();
						}
					}
				}
			}
			break;
		case 4:// 大小单双
			total = one.size() * two.size();
			break;
		case 5:// 一星
			total = one.size();
			break;
		case 6:// 二星直选
			total = one.size() * two.size();
			break;
		case 7:// 二星组选
			total = one.size() * (one.size() - 1) / 2;
			break;
		case 100:// 二星和值
			if (0 != one.size()) {
				for (String str : one) {
					int number = Integer.parseInt(str);
					switch (number) {
					case 0:
					case 1:
					case 17:
					case 18:
						total += 1;
						break;
					case 2:
					case 3:
					case 15:
					case 16:
						total += 2;
						break;
					case 4:
					case 5:
					case 13:
					case 14:
						total += 3;
						break;
					case 6:
					case 7:
					case 11:
					case 12:
						total += 4;
						break;
					case 8:
					case 9:
					case 10:
						total += 5;
						break;
					}
				}
			}
			break;
		case 8:// 趣味二星

			break;
		case 9:// 三星直选
			total = one.size() * two.size() * three.size();
			break;
		case 10:// 三星组三
			total = one.size() * (one.size() - 1);
			break;
		case 11:// 三星组六
			total = one.size() * (one.size() - 1) * (one.size() - 2) / 6;
			break;
		/*
		 * case 12://三组包胆 total=one.size()*55; break; case 14://三星直选和值
		 * if(0!=one.size()){ for (String str:one){ int
		 * number=Integer.parseInt(str); switch (number){ case 0: case 27:
		 * total+=1; break; case 1: case 26: total+=3; break; case 2: case 25:
		 * total+=6; break; case 3: case 24: total+=10; break; case 4: case 23:
		 * total+=15; break; case 5: case 22: total+=21; break; case 6: case 21:
		 * total+=28; break; case 7: case 20: total+=36; break; case 8: case 19:
		 * total+=45; break; case 9: case 18: total+=55; break; case 10: case
		 * 17: total+=63; break; case 11: case 16: total+=69; break; case 12:
		 * case 15: total+=73; break; case 13: case 14: total+=75; break; } } }
		 * break; case 15://三星组选和值 if(0!=one.size()){ for (String str:one){ int
		 * number=Integer.parseInt(str); switch (number){ case 1: case 26:
		 * total+=1; break; case 2: case 3: case 24: case 25: total+=2; break;
		 * case 4: case 23: total+=4; break; case 5: case 22: total+=5; break;
		 * case 6: case 21: total+=6; break; case 7: case 20: total+=8; break;
		 * case 8: case 19: total+=10; break; case 9: case 18: total+=11; break;
		 * case 10: case 17: total+=13; break; case 11: case 12: case 15: case
		 * 16: total+=14; break; case 13: case 14: total+=15; break; } } }
		 * break; case 16://三星组合 if(one.size()>2){ total=1; for(int
		 * i=0;i<3;i++){ total*=(one.size()-2+i); } } break;
		 */
		case 12:// 四星直选
			total = one.size() * two.size() * three.size() * four.size();
			break;
		case 13:// 四星组选4
		case 14:// 四星组选6
		case 19:// 五星组选5
		case 20:// 五星组选10
			if (0 != two.size()) {
				if (0 < one.size()) {
					Iterator it1 = two.iterator();
					while (it1.hasNext()) {
						String str = (String) it1.next();
						Iterator it2 = one.iterator();
						while (it2.hasNext()) {
							String str1 = (String) it2.next();
							if (!str.equals(str1)) {
								total++;
							}
						}
					}
				}
			}
			break;
		case 15:// 四星组选12
		case 21:// 五星组选20
			if (0 != two.size()) {
				if (1 < one.size()) {
					Iterator it1 = two.iterator();
					while (it1.hasNext()) {
						String str = (String) it1.next();
						Iterator it2 = one.iterator();
						while (it2.hasNext()) {
							String str1 = (String) it2.next();
							if (!str.equals(str1)) {
								Iterator it3 = one.iterator();
								while (it3.hasNext()) {
									String str2 = (String) it3.next();
									if (!str.equals(str2) && !str1.equals(str2)) {
										total++;
									}
								}
							}
						}
					}
				}
			}
			total /= 2;
			break;
		case 16:// 四星组选24
			if (one.size() > 3) {
				total = 1;
				if (4 != one.size()) {
					int level1 = 1;
					int level2 = 1;
					for (int i = 0; i < 4; i++) {
						level1 *= one.size() - 3 + i;
						level2 *= i + 1;
					}
					total *= level1 / level2;
				}
			}
			break;
		case 17:// 五星直选
		case 18:// 五星通选
			total = one.size() * two.size() * three.size() * four.size()
					* five.size();
			break;
		case 22:// 五星组选30
			if (0 != three.size()) {
				if (0 < two.size()) {
					if (0 < one.size()) {
						Iterator it1 = three.iterator();
						while (it1.hasNext()) {
							String str = (String) it1.next();
							Iterator it2 = two.iterator();
							while (it2.hasNext()) {
								String str1 = (String) it2.next();
								if (!str.equals(str1)) {
									Iterator it3 = one.iterator();
									while (it3.hasNext()) {
										String str2 = (String) it3.next();
										if (!str.equals(str2)
												&& !str1.equals(str2)) {
											total++;
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case 23:// 五星组选60
			if (0 != two.size()) {
				if (2 < one.size()) {
					Iterator it1 = two.iterator();
					while (it1.hasNext()) {
						String str = (String) it1.next();
						Iterator it2 = one.iterator();
						while (it2.hasNext()) {
							String str1 = (String) it2.next();
							if (!str.equals(str1)) {
								Iterator it3 = one.iterator();
								while (it3.hasNext()) {
									String str2 = (String) it3.next();
									if (!str.equals(str2) && !str1.equals(str2)) {
										Iterator it4 = one.iterator();
										while (it4.hasNext()) {
											String str3 = (String) it4.next();
											if (!str.equals(str3)
													&& !str1.equals(str3)
													&& !str2.equals(str3)) {
												total++;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			total /= 6;
			break;
		case 24:// 五星组选120
			if (one.size() > 4) {
				total = 1;
				if (5 != one.size()) {
					int level1 = 1;
					int level2 = 1;
					for (int i = 0; i < 5; i++) {
						level1 *= one.size() - 4 + i;
						level2 *= i + 1;
					}
					total *= level1 / level2;
				}
			}
			break;
		case 25:// 五星好事成双
		case 26:// 五星三星报喜
		case 27:// 五星四季发财
			total = one.size();
			break;
		default:
			break;
		}
		return total;
	}

	/**
	 * 2串1 *
	 */
	public static int getAll2G1Mixed(List<String> list) {
		int count = 0;
		for (Integer i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					if (j == list.size())
						continue;
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * 单关 *
	 */
	public static int getCountBySinglePass(List<String> list) {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			count += list.get(i).length();
		}
		return count;
	}

	/**
	 * 2串1 混合 *
	 */
	public static int getAll2G1Mixed_hunhe(List<String> list) {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).replace("|", ",").split(",").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					int length = 0;
					length = list.get(j).replace("|", ",").split(",").length;
					count += length;
				}
			}
		}
		return count;
	}

	/**
	 * 3串1 *
	 */
	public static int getAll3G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								sum++;
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 3串1 *
	 */
	public static int getAll3G1Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).replace("|", ",").split(",").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).replace("|", ",")
							.split(",").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							int length = 0;
							length = list.get(k).replace("|", ",").split(",").length;
							sum += length;
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 3串3 *
	 */
	public static int getAll3G3Mixed(List<String> list) {
		int sum = 0;
		for (int a1 = 0; a1 < list.size() - 2; a1++) {
			for (int a2 = a1 + 1; a2 < list.size() - 1; a2++) {
				for (int a3 = a2 + 1; a3 < list.size(); a3++) {
					sum += getAll2G1Mixed(new ArrayList<String>(Arrays.asList(
							list.get(a1), list.get(a2), list.get(a3))));
				}
			}
		}
		return sum;
	}

	/**
	 * 3串3混合 *
	 */
	public static int getAll3G3Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								String a = null;
								a = list.get(i).split("\\|")[i_0];
								String b = null;
								b = list.get(j).split("\\|")[j_0];
								String c = null;
								c = list.get(k).split("\\|")[k_0];
								ArrayList<String> subList = new ArrayList<String>(
										Arrays.asList(a, b, c));
								sum += getAll2G1Mixed_hunhe(subList);
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 3串4 = 3串3 加 3串1 *
	 */
	public static int getAll3G4Mixed(List<String> list) {
		int sum = 0;
		sum += getAll3G3Mixed(list);
		sum += getAll3G1Mixed(list);
		return sum;
	}

	/**
	 * 3串4 = 3串3 加 3串1混合 *
	 */
	public static int getAll3G4Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum += getAll3G3Mixed_hunhe(list);
		sum += getAll3G1Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 4串1 *
	 */
	public static int getAll4G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										sum++;
									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 4串1 混合 *
	 */
	public static int getAll4G1Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).replace("|", ",").split(",").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).replace("|", ",")
							.split(",").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k)
									.replace("|", ",").split(",").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									int length = 0;
									length = list.get(x).replace("|", ",")
											.split(",").length;
									sum += length;
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 4串4 *
	 */
	public static int getAll4G4Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				for (int k = j + 1; k < list.size(); k++) {
					for (int x = k + 1; x < list.size(); x++) {
						sum += getAll3G1Mixed(Arrays.asList(list.get(i),
								list.get(j), list.get(k), list.get(x)));
					}
				}
			}
		}

		return sum;
	}

	/**
	 * 4串4 混合 *
	 */
	public static int getAll4G4Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										String a = null;
										a = list.get(i).split("\\|")[i_0];
										String b = null;
										b = list.get(j).split("\\|")[j_0];
										String c = null;
										c = list.get(k).split("\\|")[k_0];
										String d = null;
										d = list.get(x).split("\\|")[x_0];
										ArrayList<String> subList = new ArrayList<String>(
												Arrays.asList(a, b, c, d));
										sum += getAll3G1Mixed_hunhe(subList);
									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 4串5 = 4串4 + 4串1 *
	 */
	public static int getAll4G5Mixed(List<String> list) {
		int sum = 0;
		sum = getAll4G4Mixed(list) + getAll4G1Mixed(list);
		return sum;
	}

	/**
	 * 4串5 = 4串4 + 4串1 混合 *
	 */
	public static int getAll4G5Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum = getAll4G4Mixed_hunhe(list) + getAll4G1Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 4串6 *
	 */
	public static int getAll4G6Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				for (int k = j + 1; k < list.size(); k++) {
					for (int x = k + 1; x < list.size(); x++) {
						sum += getAll2G1Mixed(Arrays.asList(list.get(i),
								list.get(j), list.get(k), list.get(x)));
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 4串6 混合 *
	 */
	public static int getAll4G6Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										String a = null;
										a = list.get(i).split("\\|")[i_0];
										String b = null;
										b = list.get(j).split("\\|")[j_0];
										String c = null;
										c = list.get(k).split("\\|")[k_0];
										String d = null;
										d = list.get(x).split("\\|")[x_0];
										ArrayList<String> subList = new ArrayList<String>(
												Arrays.asList(a, b, c, d));
										sum += getAll2G1Mixed_hunhe(subList);
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 4串11 *
	 */
	public static int getAll4G11Mixed(List<String> list) {
		int sum = 0;
		sum += getAll4G6Mixed(list);
		sum += getAll4G4Mixed(list);
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				for (int k = j + 1; k < list.size(); k++) {
					for (int x = k + 1; x < list.size(); x++) {
						sum += getAll4G1Mixed(Arrays.asList(list.get(i),
								list.get(j), list.get(k), list.get(x)));

					}
				}
			}
		}
		return sum;
	}

	/**
	 * 4串11 *
	 */
	public static int getAll4G11Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum += getAll4G6Mixed_hunhe(list);
		sum += getAll4G5Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 5串1 *
	 */
	public static int getAll5G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												sum++;
											}
										}

									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 5串1 混合 *
	 */
	public static int getAll5G1Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).replace("|", ",").split(",").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).replace("|", ",")
							.split(",").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k)
									.replace("|", ",").split(",").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.replace("|", ",").split(",").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											int length = 0;
											length = list.get(y)
													.replace("|", ",")
													.split(",").length;
											sum += length;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 5串5 *
	 */
	public static int getAll5G5Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				for (int k = j + 1; k < list.size(); k++) {
					for (int x = k + 1; x < list.size(); x++) {
						for (int y = x + 1; y < list.size(); y++) {
							sum += getAll4G1Mixed(Arrays.asList(list.get(i),
									list.get(j), list.get(k), list.get(x),
									list.get(y)));
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 5串5 *
	 */
	public static int getAll5G5Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												String a = null;
												a = list.get(i).split("\\|")[i_0];
												String b = null;
												b = list.get(j).split("\\|")[j_0];
												String c = null;
												c = list.get(k).split("\\|")[k_0];
												String d = null;
												d = list.get(x).split("\\|")[x_0];
												String e = null;
												e = list.get(y).split("\\|")[y_0];
												ArrayList<String> subList = new ArrayList<String>(
														Arrays.asList(a, b, c,
																d, e));

												for (int l = 0; l < subList
														.size(); l++) {
													System.out.print(subList
															.get(l) + "-");
												}

												sum += getAll4G1Mixed_hunhe(subList);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 5串6 *
	 */
	public static int getAll5G6Mixed(List<String> list) {
		int sum = 0;
		sum += getAll5G5Mixed(list);
		sum += getAll5G1Mixed(list);
		return sum;
	}

	/**
	 * 5串6 *
	 */
	public static int getAll5G6Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum += getAll5G5Mixed_hunhe(list);
		sum += getAll5G1Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 5串10 *
	 */
	public static int getAll5G10Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 4; i++) {
			for (int j = i + 1; j < list.size() - 3; j++) {
				for (int k = j + 1; k < list.size() - 2; k++) {
					for (int x = k + 1; x < list.size() - 1; x++) {
						for (int o = x + 1; o < list.size(); o++) {
							sum += getAll2G1Mixed(Arrays.asList(list.get(i),
									list.get(j), list.get(k), list.get(x),
									list.get(o)));
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 5串10 *
	 */
	public static int getAll5G10Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												String a = null;
												a = list.get(i).split("\\|")[i_0];
												String b = null;
												b = list.get(j).split("\\|")[j_0];
												String c = null;
												c = list.get(k).split("\\|")[k_0];
												String d = null;
												d = list.get(x).split("\\|")[x_0];
												String e = null;
												e = list.get(y).split("\\|")[y_0];
												ArrayList<String> subList = new ArrayList<String>(
														Arrays.asList(a, b, c,
																d, e));

												for (int l = 0; l < subList
														.size(); l++) {
													System.out.print(subList
															.get(l) + "-");
												}

												sum += getAll2G1Mixed_hunhe(subList);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 5串16 *
	 */
	public static int getAll5G16Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 4; i++) {
			for (int j = i + 1; j < list.size() - 3; j++) {
				for (int k = j + 1; k < list.size() - 2; k++) {
					for (int x = k + 1; x < list.size() - 1; x++) {
						for (int o = x + 1; o < list.size(); o++) {
							sum += getAll3G1Mixed(Arrays.asList(list.get(i),
									list.get(j), list.get(k), list.get(x),
									list.get(o)));
						}
					}
				}

			}
		}
		sum += getAll5G5Mixed(list);
		sum += getAll5G1Mixed(list);
		return sum;
	}

	/*
	 * 5串31 胆托 北京单场
	 */
	public static int getAll5G31Mixed_dan_bjdc(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum += NumberTools.getAll5G26Mixed_dan(list_dan, list);
		switch (list_dan.size()) {
		case 1:
			for (int i = 0; i < list.size() - 3; i++) {
				for (int j = i + 1; j < list.size() - 2; j++) {
					for (int j2 = j + 1; j2 < list.size() - 1; j2++) {
						for (int k = j2 + 1; k < list.size(); k++) {
							sum += list_dan.get(0).length()
									+ list.get(i).length()
									+ list.get(j).length()
									+ list.get(j2).length()
									+ list.get(k).length();
						}
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < list.size() - 2; i++) {
				for (int j = i + 1; j < list.size() - 1; j++) {
					for (int j2 = j + 1; j2 < list.size(); j2++) {
						sum += list_dan.get(0).length() + list.get(i).length()
								+ list.get(j).length() + list.get(j2).length()
								+ list_dan.get(1).length();
					}
				}
			}
			break;
		case 3:
			for (int i = 0; i < list.size() - 1; i++) {
				for (int j = i + 1; j < list.size(); j++) {
					sum += list_dan.get(0).length() + list.get(i).length()
							+ list.get(j).length() + list_dan.get(2).length()
							+ list_dan.get(1).length();
				}
			}
			break;
		case 4:
			for (int i = 0; i < list.size(); i++) {
				sum += list_dan.get(0).length() + list.get(i).length()
						+ list_dan.get(3).length() + list_dan.get(2).length()
						+ list_dan.get(1).length();
			}
			break;
		default:
			break;
		}
		return sum;
	}

	/**
	 * 5串16 *
	 */
	public static int getAll5G16Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												String a = null;
												a = list.get(i).split("\\|")[i_0];
												String b = null;
												b = list.get(j).split("\\|")[j_0];
												String c = null;
												c = list.get(k).split("\\|")[k_0];
												String d = null;
												d = list.get(x).split("\\|")[x_0];
												String e = null;
												e = list.get(y).split("\\|")[y_0];
												ArrayList<String> subList = new ArrayList<String>(
														Arrays.asList(a, b, c,
																d, e));

												for (int l = 0; l < subList
														.size(); l++) {
													System.out.print(subList
															.get(l) + "-");
												}

												sum += getAll3G1Mixed_hunhe(subList)
														+ getAll4G1Mixed_hunhe(subList)
														+ getAll5G1Mixed_hunhe(subList);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 5串20 *
	 */
	public static int getAll5G20Mixed(List<String> list) {
		int sum = 0;
		sum += getAll5G10Mixed(list);
		for (int i = 0; i < list.size() - 4; i++) {
			for (int j = i + 1; j < list.size() - 3; j++) {
				for (int k = j + 1; k < list.size() - 2; k++) {
					for (int x = k + 1; x < list.size() - 1; x++) {
						for (int o = x + 1; o < list.size(); o++) {
							sum += getAll3G1Mixed(Arrays.asList(list.get(i),
									list.get(j), list.get(k), list.get(x),
									list.get(o)));
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 5串20 *
	 */
	public static int getAll5G20Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												String a = null;
												a = list.get(i).split("\\|")[i_0];
												String b = null;
												b = list.get(j).split("\\|")[j_0];
												String c = null;
												c = list.get(k).split("\\|")[k_0];
												String d = null;
												d = list.get(x).split("\\|")[x_0];
												String e = null;
												e = list.get(y).split("\\|")[y_0];
												ArrayList<String> subList = new ArrayList<String>(
														Arrays.asList(a, b, c,
																d, e));

												for (int l = 0; l < subList
														.size(); l++) {
													System.out.print(subList
															.get(l) + "-");
												}

												sum += getAll2G1Mixed_hunhe(subList)
														+ getAll3G1Mixed_hunhe(subList);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 5串26 *
	 */
	public static int getAll5G26Mixed(List<String> list) {
		int sum = 0;
		sum += getAll5G10Mixed(list);
		sum += getAll5G16Mixed(list);
		return sum;
	}

	/**
	 * 5串26 *
	 */
	public static int getAll5G26Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum += getAll5G6Mixed_hunhe(list);
		sum += getAll5G20Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 6串1 *
	 */
	public static int getAll6G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														sum++;
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 6串1 混合 *
	 */
	public static int getAll6G1Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).replace("|", ",").split(",").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).replace("|", ",")
							.split(",").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k)
									.replace("|", ",").split(",").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.replace("|", ",").split(",").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.replace("|", ",")
													.split(",").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													int length = 0;
													length = list.get(z)
															.replace("|", ",")
															.split(",").length;
													sum += length;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 6串6 *
	 */
	public static int getAll6G6Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 5; i++) {
			for (int j = i + 1; j < list.size() - 4; j++) {
				for (int k = j + 1; k < list.size() - 3; k++) {
					for (int x = k + 1; x < list.size() - 2; x++) {
						for (int y = x + 1; y < list.size() - 1; y++) {
							for (int z = y + 1; z < list.size(); z++) {
								sum += getAll5G1Mixed(Arrays.asList(
										list.get(i), list.get(j), list.get(k),
										list.get(x), list.get(y), list.get(z)));
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 6串6 混合 *
	 */
	public static int getAll6G6Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														String a = null;
														a = list.get(i).split(
																"\\|")[i_0];
														String b = null;
														b = list.get(j).split(
																"\\|")[j_0];
														String c = null;
														c = list.get(k).split(
																"\\|")[k_0];
														String d = null;
														d = list.get(x).split(
																"\\|")[x_0];
														String e = null;
														e = list.get(y).split(
																"\\|")[y_0];
														String f = null;
														f = list.get(z).split(
																"\\|")[z_0];
														ArrayList<String> subList = new ArrayList<String>(
																Arrays.asList(
																		a, b,
																		c, d,
																		e, f));

														for (int l = 0; l < subList
																.size(); l++) {
															System.out
																	.print(subList
																			.get(l)
																			+ "-");
														}

														sum += getAll5G1Mixed_hunhe(subList);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 6串7 *
	 */
	public static int getAll6G7Mixed(List<String> list) {
		int sum = 0;
		sum = getAll6G6Mixed(list) + getAll6G1Mixed(list);
		return sum;
	}

	/**
	 * 6串7 混合 *
	 */
	public static int getAll6G7Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum = getAll6G6Mixed_hunhe(list) + getAll6G1Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 6串15 *
	 */
	public static int getAll6G15Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 5; i++) {
			for (int j = i + 1; j < list.size() - 4; j++) {
				for (int k = j + 1; k < list.size() - 3; k++) {
					for (int x = k + 1; x < list.size() - 2; x++) {
						for (int y = x + 1; y < list.size() - 1; y++) {
							for (int z = y + 1; z < list.size(); z++) {
								sum += getAll2G1Mixed(Arrays.asList(
										list.get(i), list.get(j), list.get(k),
										list.get(x), list.get(y), list.get(z)));
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 6串15 混合 *
	 */
	public static int getAll6G15Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														String a = null;
														a = list.get(i).split(
																"\\|")[i_0];
														String b = null;
														b = list.get(j).split(
																"\\|")[j_0];
														String c = null;
														c = list.get(k).split(
																"\\|")[k_0];
														String d = null;
														d = list.get(x).split(
																"\\|")[x_0];
														String e = null;
														e = list.get(y).split(
																"\\|")[y_0];
														String f = null;
														f = list.get(z).split(
																"\\|")[z_0];
														ArrayList<String> subList = new ArrayList<String>(
																Arrays.asList(
																		a, b,
																		c, d,
																		e, f));

														for (int l = 0; l < subList
																.size(); l++) {
															System.out
																	.print(subList
																			.get(l)
																			+ "-");
														}

														sum += getAll2G1Mixed_hunhe(subList);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 6串20 *
	 */
	public static int getAll6G20Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 5; i++) {
			for (int j = i + 1; j < list.size() - 4; j++) {
				for (int k = j + 1; k < list.size() - 3; k++) {
					for (int x = k + 1; x < list.size() - 2; x++) {
						for (int y = x + 1; y < list.size() - 1; y++) {
							for (int z = y + 1; z < list.size(); z++) {
								sum += getAll3G1Mixed(Arrays.asList(
										list.get(i), list.get(j), list.get(k),
										list.get(x), list.get(y), list.get(z)));
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 6串20 混合 *
	 */
	public static int getAll6G20Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														String a = null;
														a = list.get(i).split(
																"\\|")[i_0];
														String b = null;
														b = list.get(j).split(
																"\\|")[j_0];
														String c = null;
														c = list.get(k).split(
																"\\|")[k_0];
														String d = null;
														d = list.get(x).split(
																"\\|")[x_0];
														String e = null;
														e = list.get(y).split(
																"\\|")[y_0];
														String f = null;
														f = list.get(z).split(
																"\\|")[z_0];
														ArrayList<String> subList = new ArrayList<String>(
																Arrays.asList(
																		a, b,
																		c, d,
																		e, f));

														for (int l = 0; l < subList
																.size(); l++) {
															System.out
																	.print(subList
																			.get(l)
																			+ "-");
														}

														sum += getAll3G1Mixed_hunhe(subList);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 6串22 *
	 */
	public static int getAll6G22Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 5; i++) {
			for (int j = i + 1; j < list.size() - 4; j++) {
				for (int k = j + 1; k < list.size() - 3; k++) {
					for (int x = k + 1; x < list.size() - 2; x++) {
						for (int y = x + 1; y < list.size() - 1; y++) {
							for (int z = y + 1; z < list.size(); z++) {
								sum += getAll4G1Mixed(Arrays.asList(
										list.get(i), list.get(j), list.get(k),
										list.get(x), list.get(y), list.get(z)));
							}
						}
					}
				}

			}
		}
		sum += getAll6G6Mixed(list) + getAll6G1Mixed(list);
		return sum;
	}

	/**
	 * 6串22 混合 *
	 */
	public static int getAll6G22Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														String a = null;
														a = list.get(i).split(
																"\\|")[i_0];
														String b = null;
														b = list.get(j).split(
																"\\|")[j_0];
														String c = null;
														c = list.get(k).split(
																"\\|")[k_0];
														String d = null;
														d = list.get(x).split(
																"\\|")[x_0];
														String e = null;
														e = list.get(y).split(
																"\\|")[y_0];
														String f = null;
														f = list.get(z).split(
																"\\|")[z_0];
														ArrayList<String> subList = new ArrayList<String>(
																Arrays.asList(
																		a, b,
																		c, d,
																		e, f));

														for (int l = 0; l < subList
																.size(); l++) {
															System.out
																	.print(subList
																			.get(l)
																			+ "-");
														}

														sum += getAll4G1Mixed_hunhe(subList)
																+ getAll5G1Mixed_hunhe(subList)
																+ getAll6G1Mixed_hunhe(subList);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 6串35 *
	 */
	public static int getAll6G35Mixed(List<String> list) {
		int sum = 0;
		sum = getAll6G15Mixed(list) + getAll6G20Mixed(list);
		return sum;
	}

	/**
	 * 6串35 混合 *
	 */
	public static int getAll6G35Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum = getAll6G15Mixed_hunhe(list) + getAll6G20Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 6串42 *
	 */
	public static int getAll6G42Mixed(List<String> list) {
		int sum = 0;
		sum = getAll6G20Mixed(list) + getAll6G22Mixed(list);
		return sum;
	}

	/**
	 * 6串42 混合 *
	 */
	public static int getAll6G42Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum = getAll6G20Mixed_hunhe(list) + getAll6G22Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 6串50 *
	 */
	public static int getAll6G50Mixed(List<String> list) {
		int sum = 0;
		sum = getAll6G35Mixed(list);
		for (int i = 0; i < list.size() - 5; i++) {
			for (int j = i + 1; j < list.size() - 4; j++) {
				for (int k = j + 1; k < list.size() - 3; k++) {
					for (int x = k + 1; x < list.size() - 2; x++) {
						for (int y = x + 1; y < list.size() - 1; y++) {
							for (int z = y + 1; z < list.size(); z++) {
								sum += getAll4G1Mixed(Arrays.asList(
										list.get(i), list.get(j), list.get(k),
										list.get(x), list.get(y), list.get(z)));
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 6串50 混合 *
	 */
	public static int getAll6G50Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														String a = null;
														a = list.get(i).split(
																"\\|")[i_0];
														String b = null;
														b = list.get(j).split(
																"\\|")[j_0];
														String c = null;
														c = list.get(k).split(
																"\\|")[k_0];
														String d = null;
														d = list.get(x).split(
																"\\|")[x_0];
														String e = null;
														e = list.get(y).split(
																"\\|")[y_0];
														String f = null;
														f = list.get(z).split(
																"\\|")[z_0];
														ArrayList<String> subList = new ArrayList<String>(
																Arrays.asList(
																		a, b,
																		c, d,
																		e, f));

														for (int l = 0; l < subList
																.size(); l++) {
															System.out
																	.print(subList
																			.get(l)
																			+ "-");
														}

														sum += getAll2G1Mixed_hunhe(subList)
																+ getAll3G1Mixed_hunhe(subList)
																+ getAll4G1Mixed_hunhe(subList);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 6串57 *
	 */
	public static int getAll6G57Mixed(List<String> list) {
		int sum = 0;
		sum = getAll6G15Mixed(list) + getAll6G42Mixed(list);
		return sum;
	}

	/**
	 * 6串57混合 *
	 */
	public static int getAll6G57Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum = getAll6G50Mixed_hunhe(list) + getAll6G7Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 7串1 *
	 */
	public static int getAll7G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																sum++;
															}
														}

													}
												}

											}
										}

									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 7串1 混合 *
	 */
	public static int getAll7G1Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).replace("|", ",").split(",").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).replace("|", ",")
							.split(",").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k)
									.replace("|", ",").split(",").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.replace("|", ",").split(",").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.replace("|", ",")
													.split(",").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.replace("|", ",")
															.split(",").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															int length = 0;
															length = list
																	.get(a)
																	.replace(
																			"|",
																			",")
																	.split(",").length;
															sum += length;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 7串7 *
	 */
	public static int getAll7G7Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									sum += getAll6G1Mixed(Arrays.asList(
											list.get(i), list.get(j),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 7串7 混合 *
	 */
	public static int getAll7G7Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																String aa = null;
																aa = list
																		.get(i)
																		.split("\\|")[i_0];
																String b = null;
																b = list.get(j)
																		.split("\\|")[j_0];
																String c = null;
																c = list.get(k)
																		.split("\\|")[k_0];
																String d = null;
																d = list.get(x)
																		.split("\\|")[x_0];
																String e = null;
																e = list.get(y)
																		.split("\\|")[y_0];
																String f = null;
																f = list.get(z)
																		.split("\\|")[z_0];
																String g = null;
																g = list.get(a)
																		.split("\\|")[a_0];
																ArrayList<String> subList = new ArrayList<String>(
																		Arrays.asList(
																				aa,
																				b,
																				c,
																				d,
																				e,
																				f,
																				g));

																for (int l = 0; l < subList
																		.size(); l++) {
																	System.out
																			.print(subList
																					.get(l)
																					+ "-");
																}

																sum += getAll6G1Mixed_hunhe(subList);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 7串8 *
	 */
	public static int getAll7G8Mixed(List<String> list) {
		int sum = 0;
		sum = getAll7G7Mixed(list) + getAll7G1Mixed(list);
		return sum;
	}

	/**
	 * 7串8 混合 *
	 */
	public static int getAll7G8Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum = getAll7G7Mixed_hunhe(list) + getAll7G1Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 7串21 *
	 */
	public static int getAll7G21Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									sum += getAll5G1Mixed(Arrays.asList(
											list.get(i), list.get(j),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 7串21 混合 *
	 */
	public static int getAll7G21Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																String aa = null;
																aa = list
																		.get(i)
																		.split("\\|")[i_0];
																String b = null;
																b = list.get(j)
																		.split("\\|")[j_0];
																String c = null;
																c = list.get(k)
																		.split("\\|")[k_0];
																String d = null;
																d = list.get(x)
																		.split("\\|")[x_0];
																String e = null;
																e = list.get(y)
																		.split("\\|")[y_0];
																String f = null;
																f = list.get(z)
																		.split("\\|")[z_0];
																String g = null;
																g = list.get(a)
																		.split("\\|")[a_0];
																ArrayList<String> subList = new ArrayList<String>(
																		Arrays.asList(
																				aa,
																				b,
																				c,
																				d,
																				e,
																				f,
																				g));

																for (int l = 0; l < subList
																		.size(); l++) {
																	System.out
																			.print(subList
																					.get(l)
																					+ "-");
																}

																sum += getAll5G1Mixed_hunhe(subList);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 7串35 *
	 */
	public static int getAll7G35Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									sum += getAll4G1Mixed(Arrays.asList(
											list.get(i), list.get(j),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 7串35 混合 *
	 */
	public static int getAll7G35Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																String aa = null;
																aa = list
																		.get(i)
																		.split("\\|")[i_0];
																String b = null;
																b = list.get(j)
																		.split("\\|")[j_0];
																String c = null;
																c = list.get(k)
																		.split("\\|")[k_0];
																String d = null;
																d = list.get(x)
																		.split("\\|")[x_0];
																String e = null;
																e = list.get(y)
																		.split("\\|")[y_0];
																String f = null;
																f = list.get(z)
																		.split("\\|")[z_0];
																String g = null;
																g = list.get(a)
																		.split("\\|")[a_0];
																ArrayList<String> subList = new ArrayList<String>(
																		Arrays.asList(
																				aa,
																				b,
																				c,
																				d,
																				e,
																				f,
																				g));

																for (int l = 0; l < subList
																		.size(); l++) {
																	System.out
																			.print(subList
																					.get(l)
																					+ "-");
																}

																sum += getAll4G1Mixed_hunhe(subList);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 7串120 *
	 */
	public static int getAll7G120Mixed(List<String> list) {
		int sum = 0;
		// 过 5 和 4 和 6 和 7
		sum = getAll7G21Mixed(list) + getAll7G35Mixed(list)
				+ getAll7G7Mixed(list) + getAll7G1Mixed(list);
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									sum += getAll2G1Mixed(Arrays.asList(
											list.get(i), list.get(j),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
									sum += getAll3G1Mixed(Arrays.asList(
											list.get(i), list.get(j),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 7串120 混合 *
	 */
	public static int getAll7G120Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																String aa = null;
																aa = list
																		.get(i)
																		.split("\\|")[i_0];
																String b = null;
																b = list.get(j)
																		.split("\\|")[j_0];
																String c = null;
																c = list.get(k)
																		.split("\\|")[k_0];
																String d = null;
																d = list.get(x)
																		.split("\\|")[x_0];
																String e = null;
																e = list.get(y)
																		.split("\\|")[y_0];
																String f = null;
																f = list.get(z)
																		.split("\\|")[z_0];
																String g = null;
																g = list.get(a)
																		.split("\\|")[a_0];
																ArrayList<String> subList = new ArrayList<String>(
																		Arrays.asList(
																				aa,
																				b,
																				c,
																				d,
																				e,
																				f,
																				g));
																sum += getAll2G1Mixed_hunhe(subList)
																		+ getAll3G1Mixed_hunhe(subList)
																		+ getAll4G1Mixed_hunhe(subList)
																		+ getAll5G1Mixed_hunhe(subList)
																		+ getAll6G1Mixed_hunhe(subList)
																		+ getAll7G1Mixed_hunhe(subList);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 8串1 *
	 * 
	 */
	public static int getAll8G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.length(); b_0++) {
																		sum++;
																	}
																}

															}
														}

													}
												}

											}
										}

									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 8串1 混合 *
	 */
	public static int getAll8G1Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).replace("|", ",").split(",").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).replace("|", ",")
							.split(",").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k)
									.replace("|", ",").split(",").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.replace("|", ",").split(",").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.replace("|", ",")
													.split(",").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.replace("|", ",")
															.split(",").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.replace(
																			"|",
																			",")
																	.split(",").length; a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	int length = 0;
																	length = list
																			.get(b)
																			.replace(
																					"|",
																					",")
																			.split(",").length;
																	sum += length;
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 8串8 *
	 */
	public static int getAll8G8Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									for (int b = a + 1; b < list.size(); b++) {
										sum += getAll7G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
									}
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 8串8 混合 *
	 */
	public static int getAll8G8Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.split("\\|").length; b_0++) {
																		String aa = null;
																		aa = list
																				.get(i)
																				.split("\\|")[i_0];
																		String bb = null;
																		bb = list
																				.get(j)
																				.split("\\|")[j_0];
																		String c = null;
																		c = list.get(
																				k)
																				.split("\\|")[k_0];
																		String d = null;
																		d = list.get(
																				x)
																				.split("\\|")[x_0];
																		String e = null;
																		e = list.get(
																				y)
																				.split("\\|")[y_0];
																		String f = null;
																		f = list.get(
																				z)
																				.split("\\|")[z_0];
																		String g = null;
																		g = list.get(
																				a)
																				.split("\\|")[a_0];
																		String h = null;
																		h = list.get(
																				b)
																				.split("\\|")[b_0];
																		ArrayList<String> subList = new ArrayList<String>(
																				Arrays.asList(
																						aa,
																						bb,
																						c,
																						d,
																						e,
																						f,
																						g,
																						h));
																		sum += getAll7G1Mixed_hunhe(subList);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 8串9 *
	 */
	public static int getAll8G9Mixed(List<String> list) {
		int sum = 0;
		sum = getAll8G8Mixed(list) + getAll8G1Mixed(list);
		return sum;
	}

	/**
	 * 8串9 混合 *
	 */
	public static int getAll8G9Mixed_hunhe(List<String> list) {
		int sum = 0;
		sum = getAll8G8Mixed_hunhe(list) + getAll8G1Mixed_hunhe(list);
		return sum;
	}

	/**
	 * 8串28 *
	 */
	public static int getAll8G28Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									for (int b = a + 1; b < list.size(); b++) {
										sum += getAll6G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
									}
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 8串28 混合 *
	 */
	public static int getAll8G28Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.split("\\|").length; b_0++) {
																		String aa = null;
																		aa = list
																				.get(i)
																				.split("\\|")[i_0];
																		String bb = null;
																		bb = list
																				.get(j)
																				.split("\\|")[j_0];
																		String c = null;
																		c = list.get(
																				k)
																				.split("\\|")[k_0];
																		String d = null;
																		d = list.get(
																				x)
																				.split("\\|")[x_0];
																		String e = null;
																		e = list.get(
																				y)
																				.split("\\|")[y_0];
																		String f = null;
																		f = list.get(
																				z)
																				.split("\\|")[z_0];
																		String g = null;
																		g = list.get(
																				a)
																				.split("\\|")[a_0];
																		String h = null;
																		h = list.get(
																				b)
																				.split("\\|")[b_0];
																		ArrayList<String> subList = new ArrayList<String>(
																				Arrays.asList(
																						aa,
																						bb,
																						c,
																						d,
																						e,
																						f,
																						g,
																						h));

																		for (int l = 0; l < subList
																				.size(); l++) {
																			System.out
																					.print(subList
																							.get(l)
																							+ "-");
																		}

																		sum += getAll6G1Mixed_hunhe(subList);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 8串56 *
	 */
	public static int getAll8G56Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									for (int b = a + 1; b < list.size(); b++) {
										sum += getAll5G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
									}
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 8串56 混合 *
	 */
	public static int getAll8G56Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.split("\\|").length; b_0++) {
																		String aa = null;
																		aa = list
																				.get(i)
																				.split("\\|")[i_0];
																		String bb = null;
																		bb = list
																				.get(j)
																				.split("\\|")[j_0];
																		String c = null;
																		c = list.get(
																				k)
																				.split("\\|")[k_0];
																		String d = null;
																		d = list.get(
																				x)
																				.split("\\|")[x_0];
																		String e = null;
																		e = list.get(
																				y)
																				.split("\\|")[y_0];
																		String f = null;
																		f = list.get(
																				z)
																				.split("\\|")[z_0];
																		String g = null;
																		g = list.get(
																				a)
																				.split("\\|")[a_0];
																		String h = null;
																		h = list.get(
																				b)
																				.split("\\|")[b_0];
																		ArrayList<String> subList = new ArrayList<String>(
																				Arrays.asList(
																						aa,
																						bb,
																						c,
																						d,
																						e,
																						f,
																						g,
																						h));

																		for (int l = 0; l < subList
																				.size(); l++) {
																			System.out
																					.print(subList
																							.get(l)
																							+ "-");
																		}

																		sum += getAll5G1Mixed_hunhe(subList);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 8串70 *
	 */
	public static int getAll8G70Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									for (int b = a + 1; b < list.size(); b++) {
										sum += getAll4G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
									}
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 8串70 混合 *
	 */
	public static int getAll8G70Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.split("\\|").length; b_0++) {
																		String aa = null;
																		aa = list
																				.get(i)
																				.split("\\|")[i_0];
																		String bb = null;
																		bb = list
																				.get(j)
																				.split("\\|")[j_0];
																		String c = null;
																		c = list.get(
																				k)
																				.split("\\|")[k_0];
																		String d = null;
																		d = list.get(
																				x)
																				.split("\\|")[x_0];
																		String e = null;
																		e = list.get(
																				y)
																				.split("\\|")[y_0];
																		String f = null;
																		f = list.get(
																				z)
																				.split("\\|")[z_0];
																		String g = null;
																		g = list.get(
																				a)
																				.split("\\|")[a_0];
																		String h = null;
																		h = list.get(
																				b)
																				.split("\\|")[b_0];
																		ArrayList<String> subList = new ArrayList<String>(
																				Arrays.asList(
																						aa,
																						bb,
																						c,
																						d,
																						e,
																						f,
																						g,
																						h));

																		for (int l = 0; l < subList
																				.size(); l++) {
																			System.out
																					.print(subList
																							.get(l)
																							+ "-");
																		}

																		sum += getAll4G1Mixed_hunhe(subList);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 8串247 *
	 */
	public static int getAll8G247Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size() - 6; i++) {
			for (int j = i + 1; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									for (int b = a + 1; b < list.size(); b++) {
										sum += getAll2G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
										sum += getAll3G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
										sum += getAll4G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
										sum += getAll5G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
										sum += getAll6G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
										sum += getAll7G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
										sum += getAll8G1Mixed(Arrays.asList(
												list.get(i), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a), list.get(b)));
									}
								}
							}
						}
					}
				}

			}
		}
		return sum;
	}

	/**
	 * 8串247 混合 *
	 */
	public static int getAll8G247Mixed_hunhe(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).split("\\|").length; i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).split("\\|").length; j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).split("\\|").length; k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x).split(
											"\\|").length; x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.split("\\|").length; y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z)
															.split("\\|").length; z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.split("\\|").length; a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.split("\\|").length; b_0++) {
																		String aa = null;
																		aa = list
																				.get(i)
																				.split("\\|")[i_0];
																		String bb = null;
																		bb = list
																				.get(j)
																				.split("\\|")[j_0];
																		String c = null;
																		c = list.get(
																				k)
																				.split("\\|")[k_0];
																		String d = null;
																		d = list.get(
																				x)
																				.split("\\|")[x_0];
																		String e = null;
																		e = list.get(
																				y)
																				.split("\\|")[y_0];
																		String f = null;
																		f = list.get(
																				z)
																				.split("\\|")[z_0];
																		String g = null;
																		g = list.get(
																				a)
																				.split("\\|")[a_0];
																		String h = null;
																		h = list.get(
																				b)
																				.split("\\|")[b_0];
																		ArrayList<String> subList = new ArrayList<String>(
																				Arrays.asList(
																						aa,
																						bb,
																						c,
																						d,
																						e,
																						f,
																						g,
																						h));
																		sum += getAll2G1Mixed_hunhe(subList)
																				+ getAll3G1Mixed_hunhe(subList)
																				+ getAll4G1Mixed_hunhe(subList)
																				+ getAll5G1Mixed_hunhe(subList)
																				+ getAll6G1Mixed_hunhe(subList)
																				+ getAll7G1Mixed_hunhe(subList)
																				+ getAll8G1Mixed_hunhe(subList);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 2串1 胆 *
	 */
	public static int getAll2G1Mixed_dan(List<String> list_dan,
			List<String> list) {
		if (null == list_dan || list_dan.size() != 1)
			return 0;
		int count = 0;
		for (Integer i = 0; i < list.size(); i++) {
			count += list.get(i).length();
		}
		count = count * list_dan.get(0).length();
		return count;
	}

	/**
	 * n串1 胆 *
	 */
	public static int getAllnG1Mixed_dan(List<String> list_dan,
			List<String> list, int n) {
		if (null == list_dan || list_dan.size() == 0 || list_dan.size() >= n)
			return 0;
		int sum = 0;

		int danCount = 1;
		for (int i = 0; i < list_dan.size(); i++) {
			danCount = danCount * list_dan.get(i).length();
		}

		switch (n - list_dan.size()) {
		case 1:
			for (int j = 0; j < list.size(); j++) {
				sum += list.get(j).length();
			}
			break;
		case 2:
			sum = getAll2G1Mixed(list);
			break;
		case 3:
			sum = getAll3G1Mixed(list);
			break;
		case 4:
			sum = getAll4G1Mixed(list);
			break;
		case 5:
			sum = getAll5G1Mixed(list);
			break;
		case 6:
			sum = getAll6G1Mixed(list);
			break;
		case 7:
			sum = getAll7G1Mixed(list);
			break;
		default:
			break;
		}
		System.out.println("sum===count===" + sum);
		return danCount * sum;
	}

	/**
	 * 3串3 *
	 */
	public static int getAll3G3Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		switch (list_dan.size()) {
		case 1:
			for (int j = 0; j < list.size(); j++) {
				for (int k = j + 1; k < list.size(); k++) {
					sum += getAll2G1Mixed(new ArrayList<String>(Arrays.asList(
							list_dan.get(0), list.get(j), list.get(k))));
				}
			}
			break;
		case 2:
			for (int i = 0; i < 1; i++) {
				for (int k = 0; k < list.size(); k++) {
					sum += getAll2G1Mixed(new ArrayList<String>(Arrays.asList(
							list_dan.get(i), list_dan.get(i + 1), list.get(k))));
				}
			}
			break;
		default:
			break;
		}

		return sum;
	}

	/**
	 * 3串4 = 3串3 加 3串1 *
	 */
	public static int getAll3G4Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum += getAll3G3Mixed_dan(list_dan, list);
		sum += getAllnG1Mixed_dan(list_dan, list, 3);
		return sum;
	}

	/**
	 * 4串4 *
	 */
	public static int getAll4G4Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		switch (list_dan.size()) {
		case 1:
			for (int i = 0; i < list.size(); i++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int k = j + 1; k < list.size(); k++) {
						sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
								list.get(i), list.get(j), list.get(k)));
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < list.size(); i++) {
				for (int j = i + 1; j < list.size(); j++) {
					sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list.get(i), list.get(j)));
				}
			}
			break;
		case 3:
			for (int i = 0; i < list.size(); i++) {
				sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
						list_dan.get(1), list_dan.get(2), list.get(i)));
			}
			break;
		}

		return sum;
	}

	/**
	 * 4串5 = 4串4 + 4串1 *
	 */
	public static int getAll4G5Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum = getAll4G4Mixed_dan(list_dan, list)
				+ getAllnG1Mixed_dan(list_dan, list, 4);
		return sum;
	}

	/**
	 * 4串6 4串11 *
	 */
	public static int getAll4G6_11Mixed_dan(List<String> list_dan,
			List<String> list, int type) {
		int sum = 0;
		System.out.println("list_dan.size====" + list_dan.size());
		switch (list_dan.size()) {
		case 1:
			for (int j = 0; j < list.size(); j++) {
				for (int k = j + 1; k < list.size(); k++) {
					for (int x = k + 1; x < list.size(); x++) {
						if (type == 6) {
							sum += getAll2G1Mixed(Arrays.asList(
									list_dan.get(0), list.get(j), list.get(k),
									list.get(x)));
						} else {
							sum += getAll4G1Mixed(Arrays.asList(
									list_dan.get(0), list.get(j), list.get(k),
									list.get(x)));
						}
					}
				}
			}
			break;
		case 2:
			for (int j = 0; j < list.size(); j++) {
				for (int k = j + 1; k < list.size(); k++) {
					if (type == 6) {
						sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list.get(j), list.get(k)));
					} else {
						sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list.get(j), list.get(k)));
					}
				}
			}
			break;
		case 3:
			for (int k = 0; k < list.size(); k++) {
				if (type == 6) {
					sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list.get(k)));
				} else {
					sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list.get(k)));
				}
			}
			break;
		}

		if (type == 6)
			return sum;
		else {
			sum += getAll4G6_11Mixed_dan(list_dan, list, 6);
			sum += getAll4G4Mixed_dan(list_dan, list);
			return sum;
		}
	}

	/**
	 * 5串5 *
	 */
	public static int getAll5G5Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		switch (list_dan.size()) {
		case 1:
			for (int i = 0; i < list.size(); i++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int k = j + 1; k < list.size(); k++) {
						for (int g = k + 1; g < list.size(); g++) {
							sum += getAll4G1Mixed(Arrays.asList(
									list_dan.get(0), list.get(i), list.get(j),
									list.get(k), list.get(g)));
						}
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < list.size(); i++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int k = j + 1; k < list.size(); k++) {
						sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list.get(i), list.get(j),
								list.get(k)));
					}
				}
			}
			break;
		case 3:
			for (int i = 0; i < list.size(); i++) {
				for (int j = i + 1; j < list.size(); j++) {
					sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list.get(i),
							list.get(j)));
				}
			}
			break;
		case 4:
			for (int i = 0; i < list.size(); i++) {
				sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
						list_dan.get(1), list_dan.get(2), list_dan.get(3),
						list.get(i)));
			}
			break;
		}

		return sum;
	}

	/**
	 * 5串6 *
	 */
	public static int getAll5G6Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum += getAll5G5Mixed_dan(list_dan, list);
		sum += getAllnG1Mixed_dan(list_dan, list, 5);
		return sum;
	}

	/**
	 * 5串10 5串16 5串20 type ==10 为 5串10 *
	 */
	public static int getAll5G10_16_20Mixed_dan(List<String> list_dan,
			List<String> list, int type) {
		int sum = 0;
		switch (list_dan.size()) {
		case 1:
			for (int j = 0; j < list.size() - 3; j++) {
				for (int k = j + 1; k < list.size() - 2; k++) {
					for (int x = k + 1; x < list.size() - 1; x++) {
						for (int o = x + 1; o < list.size(); o++) {
							if (type == 10)
								sum += getAll2G1Mixed(Arrays.asList(
										list_dan.get(0), list.get(j),
										list.get(k), list.get(x), list.get(o)));
							else
								sum += getAll3G1Mixed(Arrays.asList(
										list_dan.get(0), list.get(j),
										list.get(k), list.get(x), list.get(o)));
						}
					}
				}
			}
			break;
		case 2:
			for (int k = 0; k < list.size() - 2; k++) {
				for (int x = k + 1; x < list.size() - 1; x++) {
					for (int o = x + 1; o < list.size(); o++) {
						if (type == 10)
							sum += getAll2G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list.get(k), list.get(x), list.get(o)));
						else
							sum += getAll3G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list.get(k), list.get(x), list.get(o)));
					}
				}
			}
			break;
		case 3:
			for (int x = 0; x < list.size() - 1; x++) {
				for (int o = x + 1; o < list.size(); o++) {
					if (type == 10)
						sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2), list.get(x),
								list.get(o)));
					else
						sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2), list.get(x),
								list.get(o)));
				}
			}
			break;
		case 4:
			for (int o = 0; o < list.size(); o++) {
				if (type == 10)
					sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list.get(o)));
				else
					sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list.get(o)));
			}
			break;
		}

		if (type == 16) {
			sum += getAll5G5Mixed_dan(list_dan, list);
			sum += getAllnG1Mixed_dan(list_dan, list, 5);
		} else if (type == 20) {
			sum += getAll5G10_16_20Mixed_dan(list_dan, list, 10);
		}
		return sum;
	}

	/**
	 * 5串26 *
	 */
	public static int getAll5G26Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum += getAll5G10_16_20Mixed_dan(list_dan, list, 10);
		sum += getAll5G10_16_20Mixed_dan(list_dan, list, 16);
		return sum;
	}

	/**
	 * 6串6 *
	 */
	public static int getAll6G6Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		switch (list_dan.size()) {
		case 1:
			for (int j = 0; j < list.size() - 4; j++) {
				for (int k = j + 1; k < list.size() - 3; k++) {
					for (int x = k + 1; x < list.size() - 2; x++) {
						for (int y = x + 1; y < list.size() - 1; y++) {
							for (int z = y + 1; z < list.size(); z++) {
								sum += getAll5G1Mixed(Arrays.asList(
										list_dan.get(0), list.get(j),
										list.get(k), list.get(x), list.get(y),
										list.get(z)));
							}
						}
					}
				}
			}
			break;
		case 2:
			for (int k = 0; k < list.size() - 3; k++) {
				for (int x = k + 1; x < list.size() - 2; x++) {
					for (int y = x + 1; y < list.size() - 1; y++) {
						for (int z = y + 1; z < list.size(); z++) {
							sum += getAll5G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list.get(k), list.get(x), list.get(y),
									list.get(z)));
						}
					}
				}
			}
			break;
		case 3:
			for (int x = 0; x < list.size() - 2; x++) {
				for (int y = x + 1; y < list.size() - 1; y++) {
					for (int z = y + 1; z < list.size(); z++) {
						sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2), list.get(x),
								list.get(y), list.get(z)));
					}
				}
			}
			break;
		case 4:
			for (int y = 0; y < list.size() - 1; y++) {
				for (int z = y + 1; z < list.size(); z++) {
					sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list.get(y), list.get(z)));
				}
			}
			break;
		case 5:
			for (int z = 0; z < list.size(); z++) {
				sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
						list_dan.get(1), list_dan.get(2), list_dan.get(3),
						list_dan.get(4), list.get(z)));
			}
			break;
		}

		return sum;
	}

	/**
	 * 6串7 *
	 */
	public static int getAll6G7Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum = getAll6G6Mixed_dan(list_dan, list)
				+ getAllnG1Mixed_dan(list_dan, list, 6);
		return sum;
	}

	/**
	 * 6串15 20 22 50 *
	 */
	public static int getAll6G15_20_22_50Mixed_dan(List<String> list_dan,
			List<String> list, int type) {
		int sum = 0;
		switch (list_dan.size()) {
		case 1:
			for (int j = 0; j < list.size() - 4; j++) {
				for (int k = j + 1; k < list.size() - 3; k++) {
					for (int x = k + 1; x < list.size() - 2; x++) {
						for (int y = x + 1; y < list.size() - 1; y++) {
							for (int z = y + 1; z < list.size(); z++) {
								if (type == 15)
									sum += getAll2G1Mixed(Arrays.asList(
											list_dan.get(0), list.get(j),
											list.get(k), list.get(x),
											list.get(y), list.get(z)));
								if (type == 20)
									sum += getAll3G1Mixed(Arrays.asList(
											list_dan.get(0), list.get(j),
											list.get(k), list.get(x),
											list.get(y), list.get(z)));
								if (type == 22 || type == 50)
									sum += getAll4G1Mixed(Arrays.asList(
											list_dan.get(0), list.get(j),
											list.get(k), list.get(x),
											list.get(y), list.get(z)));
							}
						}
					}
				}
			}
			break;
		case 2:
			for (int k = 0; k < list.size() - 3; k++) {
				for (int x = k + 1; x < list.size() - 2; x++) {
					for (int y = x + 1; y < list.size() - 1; y++) {
						for (int z = y + 1; z < list.size(); z++) {
							if (type == 15)
								sum += getAll2G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list.get(k), list.get(x), list.get(y),
										list.get(z)));
							if (type == 20)
								sum += getAll3G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list.get(k), list.get(x), list.get(y),
										list.get(z)));
							if (type == 22 || type == 50)
								sum += getAll4G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list.get(k), list.get(x), list.get(y),
										list.get(z)));
						}
					}
				}
			}
			break;
		case 3:
			for (int x = 0; x < list.size() - 2; x++) {
				for (int y = x + 1; y < list.size() - 1; y++) {
					for (int z = y + 1; z < list.size(); z++) {
						if (type == 15)
							sum += getAll2G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list.get(x), list.get(y),
									list.get(z)));
						if (type == 20)
							sum += getAll3G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list.get(x), list.get(y),
									list.get(z)));
						if (type == 22 || type == 50)
							sum += getAll4G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list.get(x), list.get(y),
									list.get(z)));
					}
				}
			}
			break;
		case 4:
			for (int y = 0; y < list.size() - 1; y++) {
				for (int z = y + 1; z < list.size(); z++) {
					if (type == 15)
						sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list.get(y), list.get(z)));
					if (type == 20)
						sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list.get(y), list.get(z)));
					if (type == 22 || type == 50)
						sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list.get(y), list.get(z)));
				}
			}
			break;
		case 5:
			for (int z = 0; z < list.size(); z++) {
				if (type == 15)
					sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list.get(z)));
				if (type == 20)
					sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list.get(z)));
				if (type == 22 || type == 50)
					sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list.get(z)));
			}
			break;
		}

		if (type == 22)
			sum += getAll6G6Mixed_dan(list_dan, list)
					+ getAllnG1Mixed_dan(list_dan, list, 6);
		else if (type == 50)
			sum += getAll6G35Mixed_dan(list_dan, list);
		return sum;
	}

	/**
	 * 6串35 *
	 */
	public static int getAll6G35Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum = getAll6G15_20_22_50Mixed_dan(list_dan, list, 15)
				+ getAll6G15_20_22_50Mixed_dan(list_dan, list, 20);
		return sum;
	}

	/**
	 * 6串42 *
	 */
	public static int getAll6G42Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum = getAll6G15_20_22_50Mixed_dan(list_dan, list, 20)
				+ getAll6G15_20_22_50Mixed_dan(list_dan, list, 22);
		return sum;
	}

	/**
	 * 6串57 *
	 */
	public static int getAll6G57Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum = getAll6G15_20_22_50Mixed_dan(list_dan, list, 15)
				+ getAll6G42Mixed_dan(list_dan, list);
		return sum;
	}

	/**
	 * 7串7 7串21 7串35 7串120 *
	 */
	public static int getAll7G7_21_35Mixed_dan(List<String> list_dan,
			List<String> list, int type) {
		int sum = 0;
		switch (list_dan.size()) {
		case 1:
			for (int j = 0; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									if (type == 7)
										sum += getAll6G1Mixed(Arrays.asList(
												list_dan.get(0), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a)));
									else if (type == 21)
										sum += getAll5G1Mixed(Arrays.asList(
												list_dan.get(0), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a)));
									else if (type == 35)
										sum += getAll4G1Mixed(Arrays.asList(
												list_dan.get(0), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a)));
									else if (type == 120) {
										sum += getAll2G1Mixed(Arrays.asList(
												list_dan.get(0), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a)));
										sum += getAll3G1Mixed(Arrays.asList(
												list_dan.get(0), list.get(j),
												list.get(k), list.get(x),
												list.get(y), list.get(z),
												list.get(a)));
									}
								}
							}
						}
					}
				}

			}
			break;
		case 2:
			for (int k = 0; k < list.size() - 4; k++) {
				for (int x = k + 1; x < list.size() - 3; x++) {
					for (int y = x + 1; y < list.size() - 2; y++) {
						for (int z = y + 1; z < list.size() - 1; z++) {
							for (int a = z + 1; a < list.size(); a++) {
								if (type == 7)
									sum += getAll6G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
								if (type == 21)
									sum += getAll5G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
								if (type == 35)
									sum += getAll4G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
								else if (type == 120) {
									sum += getAll2G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
									sum += getAll3G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list.get(k), list.get(x),
											list.get(y), list.get(z),
											list.get(a)));
								}
							}
						}
					}
				}

			}
		case 3:
			for (int x = 0; x < list.size() - 3; x++) {
				for (int y = x + 1; y < list.size() - 2; y++) {
					for (int z = y + 1; z < list.size() - 1; z++) {
						for (int a = z + 1; a < list.size(); a++) {
							if (type == 7)
								sum += getAll6G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list.get(x),
										list.get(y), list.get(z), list.get(a)));
							if (type == 21)
								sum += getAll5G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list.get(x),
										list.get(y), list.get(z), list.get(a)));
							if (type == 35)
								sum += getAll4G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list.get(x),
										list.get(y), list.get(z), list.get(a)));
							else if (type == 120) {
								sum += getAll2G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list.get(x),
										list.get(y), list.get(z), list.get(a)));
								sum += getAll3G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list.get(x),
										list.get(y), list.get(z), list.get(a)));
							}
						}
					}
				}

			}
			break;
		case 4:
			for (int y = 0; y < list.size() - 2; y++) {
				for (int z = y + 1; z < list.size() - 1; z++) {
					for (int a = z + 1; a < list.size(); a++) {
						if (type == 7)
							sum += getAll6G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list.get(y), list.get(z), list.get(a)));
						if (type == 21)
							sum += getAll5G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list.get(y), list.get(z), list.get(a)));
						if (type == 35)
							sum += getAll4G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list.get(y), list.get(z), list.get(a)));
						else if (type == 120) {
							sum += getAll2G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list.get(y), list.get(z), list.get(a)));
							sum += getAll3G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list.get(y), list.get(z), list.get(a)));
						}
					}
				}
			}
			break;
		case 5:
			for (int z = 0; z < list.size() - 1; z++) {
				for (int a = z + 1; a < list.size(); a++) {
					if (type == 7)
						sum += getAll6G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4), list.get(z),
								list.get(a)));
					if (type == 21)
						sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4), list.get(z),
								list.get(a)));
					if (type == 35)
						sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4), list.get(z),
								list.get(a)));
					else if (type == 120) {
						sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4), list.get(z),
								list.get(a)));
						sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4), list.get(z),
								list.get(a)));
					}
				}
			}
			break;
		case 6:
			for (int a = 0; a < list.size(); a++) {
				if (type == 7)
					sum += getAll6G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list.get(a)));
				if (type == 21)
					sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list.get(a)));
				if (type == 35)
					sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list.get(a)));
				else if (type == 120) {
					sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list.get(a)));
					sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list.get(a)));
				}
			}
			break;
		}
		if (type == 120) {
			sum += getAll7G7_21_35Mixed_dan(list_dan, list, 21)
					+ getAll7G7_21_35Mixed_dan(list_dan, list, 35)
					+ getAll7G7_21_35Mixed_dan(list_dan, list, 7)
					+ getAllnG1Mixed_dan(list_dan, list, 7);
		}
		return sum;
	}

	/**
	 * 7串8 *
	 */
	public static int getAll7G8Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum = getAll7G7_21_35Mixed_dan(list_dan, list, 7)
				+ getAllnG1Mixed_dan(list_dan, list, 7);
		return sum;
	}

	/**
	 * 8串8 *
	 */
	public static int getAll8G8_28_56_70_247Mixed_dan(List<String> list_dan,
			List<String> list, int type) {
		int sum = 0;
		switch (list_dan.size()) {
		case 1:
			for (int j = 0; j < list.size() - 5; j++) {
				for (int k = j + 1; k < list.size() - 4; k++) {
					for (int x = k + 1; x < list.size() - 3; x++) {
						for (int y = x + 1; y < list.size() - 2; y++) {
							for (int z = y + 1; z < list.size() - 1; z++) {
								for (int a = z + 1; a < list.size(); a++) {
									for (int b = a + 1; b < list.size(); b++) {
										if (type == 8)
											sum += getAll7G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
										else if (type == 28)
											sum += getAll6G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
										else if (type == 56)
											sum += getAll5G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
										else if (type == 70)
											sum += getAll4G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
										else if (type == 247) {
											sum += getAll2G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
											sum += getAll3G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
											sum += getAll4G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
											sum += getAll5G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
											sum += getAll6G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
											sum += getAll7G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
											sum += getAll8G1Mixed(Arrays
													.asList(list_dan.get(0),
															list.get(j),
															list.get(k),
															list.get(x),
															list.get(y),
															list.get(z),
															list.get(a),
															list.get(b)));
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case 2:
			for (int k = 0; k < list.size() - 4; k++) {
				for (int x = k + 1; x < list.size() - 3; x++) {
					for (int y = x + 1; y < list.size() - 2; y++) {
						for (int z = y + 1; z < list.size() - 1; z++) {
							for (int a = z + 1; a < list.size(); a++) {
								for (int b = a + 1; b < list.size(); b++) {
									if (type == 8)
										sum += getAll7G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
									else if (type == 28)
										sum += getAll6G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
									else if (type == 56)
										sum += getAll5G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
									else if (type == 70)
										sum += getAll4G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
									else if (type == 247) {
										sum += getAll2G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
										sum += getAll3G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
										sum += getAll4G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
										sum += getAll5G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
										sum += getAll6G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
										sum += getAll7G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
										sum += getAll8G1Mixed(Arrays.asList(
												list_dan.get(0),
												list_dan.get(1), list.get(k),
												list.get(x), list.get(y),
												list.get(z), list.get(a),
												list.get(b)));
									}
								}
							}
						}
					}
				}
			}
			break;
		case 3:
			for (int x = 0; x < list.size() - 3; x++) {
				for (int y = x + 1; y < list.size() - 2; y++) {
					for (int z = y + 1; z < list.size() - 1; z++) {
						for (int a = z + 1; a < list.size(); a++) {
							for (int b = a + 1; b < list.size(); b++) {
								if (type == 8)
									sum += getAll7G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
								else if (type == 28)
									sum += getAll6G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
								else if (type == 56)
									sum += getAll5G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
								else if (type == 70)
									sum += getAll4G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
								else if (type == 247) {
									sum += getAll2G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
									sum += getAll3G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
									sum += getAll4G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
									sum += getAll5G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
									sum += getAll6G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
									sum += getAll7G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
									sum += getAll8G1Mixed(Arrays.asList(
											list_dan.get(0), list_dan.get(1),
											list_dan.get(2), list.get(x),
											list.get(y), list.get(z),
											list.get(a), list.get(b)));
								}
							}
						}
					}
				}
			}
			break;
		case 4:
			for (int y = 0; y < list.size() - 2; y++) {
				for (int z = y + 1; z < list.size() - 1; z++) {
					for (int a = z + 1; a < list.size(); a++) {
						for (int b = a + 1; b < list.size(); b++) {
							if (type == 8)
								sum += getAll7G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
							else if (type == 28)
								sum += getAll6G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
							else if (type == 56)
								sum += getAll5G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
							else if (type == 70)
								sum += getAll4G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
							else if (type == 247) {
								sum += getAll2G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
								sum += getAll3G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
								sum += getAll4G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
								sum += getAll5G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
								sum += getAll6G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
								sum += getAll7G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
								sum += getAll8G1Mixed(Arrays.asList(
										list_dan.get(0), list_dan.get(1),
										list_dan.get(2), list_dan.get(3),
										list.get(y), list.get(z), list.get(a),
										list.get(b)));
							}
						}
					}
				}
			}
			break;
		case 5:
			for (int z = 0; z < list.size() - 1; z++) {
				for (int a = z + 1; a < list.size(); a++) {
					for (int b = a + 1; b < list.size(); b++) {
						if (type == 8)
							sum += getAll7G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
						else if (type == 28)
							sum += getAll6G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
						else if (type == 56)
							sum += getAll5G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
						else if (type == 70)
							sum += getAll4G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
						else if (type == 247) {
							sum += getAll2G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
							sum += getAll3G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
							sum += getAll4G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
							sum += getAll5G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
							sum += getAll6G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
							sum += getAll7G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
							sum += getAll8G1Mixed(Arrays.asList(
									list_dan.get(0), list_dan.get(1),
									list_dan.get(2), list_dan.get(3),
									list_dan.get(4), list.get(z), list.get(a),
									list.get(b)));
						}
					}
				}
			}
			break;
		case 6:
			for (int a = 0; a < list.size(); a++) {
				for (int b = a + 1; b < list.size(); b++) {
					if (type == 8)
						sum += getAll7G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
					else if (type == 28)
						sum += getAll6G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
					else if (type == 56)
						sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
					else if (type == 70)
						sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
					else if (type == 247) {
						sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
						sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
						sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
						sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
						sum += getAll6G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
						sum += getAll7G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
						sum += getAll8G1Mixed(Arrays.asList(list_dan.get(0),
								list_dan.get(1), list_dan.get(2),
								list_dan.get(3), list_dan.get(4),
								list_dan.get(5), list.get(a), list.get(b)));
					}
				}
			}
			break;
		case 7:
			for (int b = 0; b < list.size(); b++) {
				if (type == 8)
					sum += getAll7G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
				else if (type == 28)
					sum += getAll6G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
				else if (type == 56)
					sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
				else if (type == 70)
					sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
				else if (type == 247) {
					sum += getAll2G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
					sum += getAll3G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
					sum += getAll4G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
					sum += getAll5G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
					sum += getAll6G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
					sum += getAll7G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
					sum += getAll8G1Mixed(Arrays.asList(list_dan.get(0),
							list_dan.get(1), list_dan.get(2), list_dan.get(3),
							list_dan.get(4), list_dan.get(5), list_dan.get(6),
							list.get(b)));
				}
			}
			break;
		}

		if (type == 120) {
			sum += getAll7G7_21_35Mixed_dan(list_dan, list, 21)
					+ getAll7G7_21_35Mixed_dan(list_dan, list, 35)
					+ getAll7G7_21_35Mixed_dan(list_dan, list, 7)
					+ getAllnG1Mixed_dan(list_dan, list, 7);
		}
		return sum;
	}

	/**
	 * 8串9 *
	 */
	public static int getAll8G9Mixed_dan(List<String> list_dan,
			List<String> list) {
		int sum = 0;
		sum = getAll8G8_28_56_70_247Mixed_dan(list, list_dan, 8)
				+ getAllnG1Mixed_dan(list_dan, list, 8);
		return sum;
	}

	/**
	 * 9串1 *
	 */
	public static int getAll9G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.length(); b_0++) {
																		for (int c = b + 1; c < list
																				.size(); c++) {
																			for (int c_0 = 0; c_0 < list
																					.get(c)
																					.length(); c_0++) {
																				sum++;
																			}
																		}
																	}
																}
															}
														}

													}
												}

											}
										}

									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 10串1 *
	 */
	public static int getAll10G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.length(); b_0++) {
																		for (int c = b + 1; c < list
																				.size(); c++) {
																			for (int c_0 = 0; c_0 < list
																					.get(c)
																					.length(); c_0++) {
																				for (int d = c + 1; d < list
																						.size(); d++) {
																					for (int d_0 = 0; d_0 < list
																							.get(d)
																							.length(); d_0++) {
																						sum++;
																					}
																				}
																			}
																		}
																	}
																}
															}
														}

													}
												}

											}
										}

									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 11串1 *
	 */
	public static int getAll11G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.length(); b_0++) {
																		for (int c = b + 1; c < list
																				.size(); c++) {
																			for (int c_0 = 0; c_0 < list
																					.get(c)
																					.length(); c_0++) {
																				for (int d = c + 1; d < list
																						.size(); d++) {
																					for (int d_0 = 0; d_0 < list
																							.get(d)
																							.length(); d_0++) {
																						for (int e = d + 1; e < list
																								.size(); e++) {
																							for (int e_0 = 0; e_0 < list
																									.get(e)
																									.length(); e_0++) {
																								sum++;
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}

													}
												}

											}
										}

									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 12串1 *
	 */
	public static int getAll12G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.length(); b_0++) {
																		for (int c = b + 1; c < list
																				.size(); c++) {
																			for (int c_0 = 0; c_0 < list
																					.get(c)
																					.length(); c_0++) {
																				for (int d = c + 1; d < list
																						.size(); d++) {
																					for (int d_0 = 0; d_0 < list
																							.get(d)
																							.length(); d_0++) {
																						for (int e = d + 1; e < list
																								.size(); e++) {
																							for (int e_0 = 0; e_0 < list
																									.get(e)
																									.length(); e_0++) {
																								for (int f = e + 1; f < list
																										.size(); f++) {
																									for (int f_0 = 0; f_0 < list
																											.get(f)
																											.length(); f_0++) {
																										sum++;
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}

													}
												}

											}
										}

									}
								}

							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 13串1 *
	 */
	public static int getAll13G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.length(); b_0++) {
																		for (int c = b + 1; c < list
																				.size(); c++) {
																			for (int c_0 = 0; c_0 < list
																					.get(c)
																					.length(); c_0++) {
																				for (int d = c + 1; d < list
																						.size(); d++) {
																					for (int d_0 = 0; d_0 < list
																							.get(d)
																							.length(); d_0++) {
																						for (int e = d + 1; e < list
																								.size(); e++) {
																							for (int e_0 = 0; e_0 < list
																									.get(e)
																									.length(); e_0++) {
																								for (int f = e + 1; f < list
																										.size(); f++) {
																									for (int f_0 = 0; f_0 < list
																											.get(f)
																											.length(); f_0++) {
																										for (int g = f + 1; g < list
																												.size(); g++) {
																											for (int g_0 = 0; g_0 < list
																													.get(g)
																													.length(); g_0++) {
																												sum++;
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 14串1 *
	 */
	public static int getAll14G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.length(); b_0++) {
																		for (int c = b + 1; c < list
																				.size(); c++) {
																			for (int c_0 = 0; c_0 < list
																					.get(c)
																					.length(); c_0++) {
																				for (int d = c + 1; d < list
																						.size(); d++) {
																					for (int d_0 = 0; d_0 < list
																							.get(d)
																							.length(); d_0++) {
																						for (int e = d + 1; e < list
																								.size(); e++) {
																							for (int e_0 = 0; e_0 < list
																									.get(e)
																									.length(); e_0++) {
																								for (int f = e + 1; f < list
																										.size(); f++) {
																									for (int f_0 = 0; f_0 < list
																											.get(f)
																											.length(); f_0++) {
																										for (int g = f + 1; g < list
																												.size(); g++) {
																											for (int g_0 = 0; g_0 < list
																													.get(g)
																													.length(); g_0++) {
																												for (int h = g + 1; h < list
																														.size(); h++) {
																													for (int h_0 = 0; h_0 < list
																															.get(h)
																															.length(); h_0++) {
																														sum++;
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 15串1 *
	 */
	public static int getAll15G1Mixed(List<String> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int i_0 = 0; i_0 < list.get(i).length(); i_0++) {
				for (int j = i + 1; j < list.size(); j++) {
					for (int j_0 = 0; j_0 < list.get(j).length(); j_0++) {
						for (int k = j + 1; k < list.size(); k++) {
							for (int k_0 = 0; k_0 < list.get(k).length(); k_0++) {
								for (int x = k + 1; x < list.size(); x++) {
									for (int x_0 = 0; x_0 < list.get(x)
											.length(); x_0++) {
										for (int y = x + 1; y < list.size(); y++) {
											for (int y_0 = 0; y_0 < list.get(y)
													.length(); y_0++) {
												for (int z = y + 1; z < list
														.size(); z++) {
													for (int z_0 = 0; z_0 < list
															.get(z).length(); z_0++) {
														for (int a = z + 1; a < list
																.size(); a++) {
															for (int a_0 = 0; a_0 < list
																	.get(a)
																	.length(); a_0++) {
																for (int b = a + 1; b < list
																		.size(); b++) {
																	for (int b_0 = 0; b_0 < list
																			.get(b)
																			.length(); b_0++) {
																		for (int c = b + 1; c < list
																				.size(); c++) {
																			for (int c_0 = 0; c_0 < list
																					.get(c)
																					.length(); c_0++) {
																				for (int d = c + 1; d < list
																						.size(); d++) {
																					for (int d_0 = 0; d_0 < list
																							.get(d)
																							.length(); d_0++) {
																						for (int e = d + 1; e < list
																								.size(); e++) {
																							for (int e_0 = 0; e_0 < list
																									.get(e)
																									.length(); e_0++) {
																								for (int f = e + 1; f < list
																										.size(); f++) {
																									for (int f_0 = 0; f_0 < list
																											.get(f)
																											.length(); f_0++) {
																										for (int g = f + 1; g < list
																												.size(); g++) {
																											for (int g_0 = 0; g_0 < list
																													.get(g)
																													.length(); g_0++) {
																												for (int h = g + 1; h < list
																														.size(); h++) {
																													for (int h_0 = 0; h_0 < list
																															.get(h)
																															.length(); h_0++) {
																														for (int l = h + 1; l < list
																																.size(); l++) {
																															for (int l_0 = 0; l_0 < list
																																	.get(l)
																																	.length(); l_0++) {
																																sum++;
																															}
																														}
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 将包含括号的字符转换成list（没有括号的一个字符为一个元素，有括号内的字符串为一个元素）
	 * 
	 * @param seleteNum
	 * @return
	 */
	public static ArrayList<String> changeStringtoArray(String seleteNum) {
		ArrayList<String> resultList = new ArrayList<String>();
		if (seleteNum.contains("-")) {
			seleteNum = seleteNum.replace("-", "");
		}
		if (seleteNum.contains("(")) {// 含有括号
			String[] nums = seleteNum.split("");
			int index = 0;// 开始遍历的下标
			int startIndex = 0;
			int endIndex = 0;
			StringBuffer number = new StringBuffer();
			for (int i = 0; i < nums.length; i++) {
				if (0 != i) {// 第一个字符为空字符
					if (nums[i].contains("(")) {// 有右括号
						startIndex = i;
					} else if (nums[i].contains(")")) {
						endIndex = i;
					}
					if (0 == startIndex && 0 == endIndex) {// 没有左右括号
						resultList.add(nums[i]);
					} else if (0 != startIndex && 0 != endIndex) {// 到一整个括号段了
						resultList.add(number.toString());
						number = new StringBuffer();
						startIndex = 0;
						endIndex = 0;
					} else if (0 != startIndex && 0 == endIndex) {// 有一个括号但没到一整个括号
						if (nums[i].contains("(") || nums[i].contains(")")) {
							number.append("");
						} else
							number.append(nums[i]);
					}
				}
			}
		} else {// 不含括号
			for (int j = 0; j < seleteNum.split("").length; j++) {
				if (0 != j) {// 第一个字符为空字符
					resultList.add(seleteNum.split("")[j]);
				}
			}
		}
		return resultList;
	}

}
