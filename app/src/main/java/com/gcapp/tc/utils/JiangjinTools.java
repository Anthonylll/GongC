package com.gcapp.tc.utils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 预测奖金工具类
 * 
 * @author echo
 */
public class JiangjinTools {
	public static final String TAG = "JiangjinTools";

	/**
	 * 快乐十分的预测奖金
	 * 
	 * @param mark
	 *            :最大值还是最小值参数
	 * @param number
	 *            ：选号长度1
	 * @param number2
	 *            ：选号长度2
	 * @param play
	 *            ：玩法标识
	 * @return
	 */
	public static int getKLSF_Jiangjin(String mark, int number, int number2,
			int play) {
		double money = 0;
		int costcount = (int) AppTools.totalCount;
		if (mark.equals("max")) {// 获取最大奖金
			switch (play) {
			case 1:// 直选好运特：7601
				if (costcount > 0) {
					money = 20;
				}
				break;
			case 2:// 直选好运一：7602
				if (costcount > 0) {
					money = 4;
				}
				break;
			case 3:// 直选好运二：7603
			case 13:// 直选好运二胆拖：7613
				if (costcount > 0) {
					money = 20;
				}
				break;
			case 4:// 直选好运三：7604
			case 14:// 直选好运三胆拖：7614
				if (costcount > 0) {
					money = 120;
				}
				break;
			case 7:// 直选好运四：7607
			case 15:// 直选好运四胆拖：7615
				if (costcount > 0) {
					money = 1120;
				}
				break;
			case 10:// 直选好运五：7610
			case 16:// 直选好运五胆拖：7616
				if (costcount > 0) {
					money = 20000;
				}
				break;
			case 5:// 通选好运三：7605
			case 17:// 通选好运三胆拖：7617
				if (costcount > 0) {
					money = 50;
				}
				break;
			case 8:// 通选好运四：7608
			case 18:// 通选好运四胆拖：7618
				if (costcount > 0) {
					money = 500;
				}
				break;
			case 11:// 通选好运五：7611
			case 19:// 通选好运五胆拖：7619
				if (costcount > 0) {
					money = 5000;
				}
				break;
			default:
				break;
			}

		} else {// 最小奖金
			switch (play) {
			case 1:// 直选好运特：7601
				if (costcount > 0) {
					money = 20;
				}
				break;
			case 2:// 直选好运一：7602
				if (costcount > 0) {
					money = 4;
				}
				break;
			case 3:// 直选好运二：7603
			case 13:// 直选好运二胆拖：7613
				if (costcount > 0) {
					money = 20;
				}
				break;
			case 4:// 直选好运三：7604
			case 14:// 直选好运三胆拖：7614
				if (costcount > 0) {
					money = 120;
				}
				break;
			case 7:// 直选好运四：7607
			case 15:// 直选好运四胆拖：7615
				if (costcount > 0) {
					money = 1120;
				}
				break;
			case 10:// 直选好运五：7610
			case 16:// 直选好运五胆拖：7616
				if (costcount > 0) {
					money = 20000;
				}
				break;
			case 5:// 通选好运三：7605
			case 17:// 通选好运三胆拖：7617
				if (costcount > 0) {
					money = 5;
				}
				break;
			case 8:// 通选好运四：7608
			case 18:// 通选好运四胆拖：7618
				if (costcount > 0) {
					money = 2;
				}
				break;
			case 11:// 通选好运五：7611
			case 19:// 通选好运五胆拖：7619
				if (costcount > 0) {
					money = 5;
				}
				break;
			}
		}
		return (int) money;
	}

	/**
	 * 新疆时时彩奖金预测
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param one
	 *            ：第一排选号
	 * @param two
	 *            ：第二排选号
	 * @param three
	 *            ：第三排选号
	 * @param four
	 *            ：第四排选号
	 * @param five
	 *            ：第五排选号
	 * @param index
	 *            ：玩法标识
	 * @return
	 */
	public static int getXJSSC_Jiangjin(String mark, HashSet<String> one,
			HashSet<String> two, HashSet<String> three, HashSet<String> four,
			HashSet<String> five, int index) {

		int money = 0;
		int costcount = (int) AppTools.totalCount;
		if (mark.equals("max")) {// 获取最大奖金
			switch (index) {
			case 1:
				if (costcount > 0)
					money = 10;
				break;
			case 2:
				if (costcount > 0)
					money = 100;
				break;
			case 3:
				if (costcount > 0)
					money = 50;
				break;
			case 4:
				if (costcount > 0)
					money = 1000;
				break;
			case 5:
				if (costcount > 0)
					money = 320;
				break;
			case 6:
				if (costcount > 0)
					money = 160;
				break;
			case 7:
				if (costcount > 0) {
					money = 100000;
				}
				break;
			case 8:
				if (costcount > 0) {
					if (costcount == 1) {
						money = 20440;
					} else
						money = 20440
								+ 220
								* (one.size() * two.size() + four.size()
										* five.size() - 2)
								+ 20
								* (three.size() - 1)
								* (one.size() * two.size() + four.size()
										* five.size());
				}
				break;
			case 10:
				if (costcount > 0) {
					money = 9999;
				}
				break;
			case 11:
				if (costcount > 0) {
					money = 80;
				}
				break;

			case 12:
				if (costcount > 0) {
					money = 600;
				}
				break;
			default:
				break;
			}

		} else { // 最小奖金
			switch (index) {
			case 1:
				if (costcount > 0)
					money = 10;
				break;
			case 2:
				if (costcount > 0)
					money = 100;
				break;
			case 3:
				if (costcount > 0)
					money = 50;
				break;
			case 4:
				if (costcount > 0)
					money = 1000;
				break;
			case 5:
				if (costcount > 0)
					money = 320;
				break;
			case 6:
				if (costcount > 0)
					money = 160;
				break;
			case 7:
				if (costcount > 0)
					money = 100000;
				break;
			case 8:
				if (costcount > 0) {
					if (five.size() * four.size() < one.size() * two.size()) {
						money = five.size() * four.size() * three.size() * 20;
					} else if (five.size() * four.size() > one.size()
							* two.size()) {
						money = one.size() * two.size() * three.size() * 20;
					} else {
						money = one.size() * two.size() * three.size() * 20;
					}
				}
				break;
			case 10:
				if (costcount > 0) {
					money = 75;
				}
				break;
			case 11:
				if (costcount > 0) {
					money = 9;
				}
				break;
			case 12:
				if (costcount > 0) {
					money = 20;
				}
				break;
			default:
				break;
			}
		}
		return money;
	}

	/**
	 * 重庆时时彩奖金预测
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param one
	 *            ：第一排选号
	 * @param two
	 *            ：第二排选号
	 * @param three
	 *            ：第三排选号
	 * @param four
	 *            ：第四排选号
	 * @param five
	 *            ：第五排选号
	 * @param index
	 *            ：玩法标识
	 * @return
	 */
	public static int getSSC_Jiangjin(String mark, HashSet<String> one,
			HashSet<String> two, HashSet<String> three, HashSet<String> four,
			HashSet<String> five, int index) {

		int money = 0;
		int costcount = (int) AppTools.totalCount;
		if (mark.equals("max")) {// 获取最大奖金
			switch (index) {
			case 1:
				if (costcount > 0)
					money = 10;
				break;
			case 2:
				if (costcount > 0)
					money = 100;
				break;
			case 3:
				if (costcount > 0)
					money = 50;
				break;
			case 4:
				if (costcount > 0)
					money = 1000;
				break;
			case 5:
				if (costcount > 0)
					money = 320;
				break;
			case 6:
				if (costcount > 0)
					money = 160;
				break;
			case 7:
				if (costcount > 0) {
					money = 100000;
				}
				break;
			case 8:
				if (costcount > 0) {
					if (costcount == 1) {
						money = 20440;
					} else
						money = 20440
								+ 220
								* (one.size() * two.size() + four.size()
										* five.size() - 2)
								+ 20
								* (three.size() - 1)
								* (one.size() * two.size() + four.size()
										* five.size());
				}
				break;
			case 10:
				if (costcount > 0) {
					money = 1000;
				}
				break;
			case 11:
				if (costcount > 0) {
					money = 1000;
					Object[] str = one.toArray();
					if (one.size() == 1) {
						for (int j = 0; j < str.length; j++) {
							if (str[j].equals("1") || str[j].equals("26")) {
								money = 320;
							}

						}
					} else if (one.size() == 2) {
						int uu = 0;
						for (int j = 0; j < str.length; j++) {
							if (str[j].equals("1") || str[j].equals("26")) {
								uu += 1;
							}
						}
						if (uu == 2) {
							money = 320;
						}
					}
				}
				break;
			default:
				break;
			}

		} else { // 最小奖金
			switch (index) {
			case 1:
				if (costcount > 0)
					money = 10;
				break;
			case 2:
				if (costcount > 0)
					money = 100;
				break;
			case 3:
				if (costcount > 0)
					money = 50;
				break;
			case 4:
				if (costcount > 0)
					money = 1000;
				break;
			case 5:
				if (costcount > 0)
					money = 320;
				break;
			case 6:
				if (costcount > 0)
					money = 160;
				break;
			case 7:
				if (costcount > 0)
					money = 100000;
				break;
			case 8:
				if (costcount > 0) {
					if (five.size() * four.size() < one.size() * two.size()) {
						money = five.size() * four.size() * three.size() * 20;
					} else if (five.size() * four.size() > one.size()
							* two.size()) {
						money = one.size() * two.size() * three.size() * 20;
					} else {
						money = one.size() * two.size() * three.size() * 20;
					}
				}
				break;
			case 10:
				if (costcount > 0) {
					money = 160;
				}
				break;

			case 11:
				if (costcount > 0) {
					money = 160;
					Object[] str = one.toArray();
					if (one.size() == 1) {
						for (int j = 0; j < str.length; j++) {
							if (str[j].equals("0") || str[j].equals("27")) {
								money = 1000;
							} else if (str[j].equals("1")
									|| str[j].equals("26")) {
								money = 320;
							}
						}
					} else if (one.size() == 2) {
						int uu = 0;
						int uu2 = 0;
						for (int j = 0; j < str.length; j++) {
							if (str[j].equals("0") || str[j].equals("27")) {
								uu += 1;
							} else if (str[j].equals("1")
									|| str[j].equals("26")) {
								uu2 += 1;
							}
						}
						if (uu == 2) {
							money = 1000;
						} else if (uu2 == 2) {
							money = 320;
						}
					}
				}
				break;
			default:
				break;
			}
		}
		return money;
	}

	/**
	 * 11选5奖金预测
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param number
	 *            ：选号长度1
	 * @param number2
	 *            ：选号长度2
	 * @param play
	 *            ：玩法下标
	 * @return
	 */
	public static int get11X5_Jiangjin(String mark, int number, int number2,
			int play) {
		int money = 0;
		int costcount = (int) AppTools.totalCount;
		if (mark.equals("max")) {// 获取最大奖金
			switch (play) {
			case 1:// 前一
				if (number > 0) {
					money = 13;
				}
				break;
			case 2:// 任选二
				if (number < 6) {
					money = costcount * 6;
				} else if (number > 5) {
					money = 60;
				}
				break;
			case 3:
				if (number < 6) {
					money = costcount * 19;
				} else if (number > 5) {
					money = 190;
				}
				break;
			case 4:
				if (number < 6) {
					money = costcount * 78;
				} else if (number > 5) {
					money = 390;
				}
				break;
			case 5:
				if (number > 4) {
					money = 540;
				}
				break;
			case 6:
				if (number > 5) {
					money = 90 * (number - 5);
				}
				break;
			case 7:
				if (number > 6) {
					int n = number - 6;
					money = 26 * (n * (n + 1) / 2);
				}
				break;
			case 8:
				if (number > 7) {
					int n = number - 7;
					money = 9 * (n * (n + 1) * (n + 2) / 6);
				}
				break;

			case 9:// 前二直选
				if (costcount > 0) {
					money = 130;
				}
				break;
			case 10:// 前三直选
				if (costcount > 0) {
					money = 1170;
				}
				break;
			case 11:// 前二组选
				if (costcount > 0) {
					money = 65;
				}
				break;
			case 12:// 前三组选
				if (costcount > 0) {
					money = 195;
				}
				break;
			case 15:// 任二胆拖
				if (costcount > 0) {
					if (number2 < 5) {
						money = 6 * number2;
					} else {
						money = 24;
					}
				}
				break;
			case 16:// 任3胆拖
				if (costcount > 0) {
					if (number == 1) {
						if (number2 <= 4) {
							money = 19 * costcount;
						} else {
							money = 114;
						}
					} else {
						if (number2 <= 3) {
							money = 19 * costcount;
						} else {
							money = 57;
						}
					}
				}
				break;
			case 17:// 任4胆拖
				if (costcount > 0) {
					switch (number) {
					case 1:
						if (number2 <= 4) {
							money = 78 * costcount;
						} else {
							money = 312;
						}
						break;
					case 2:
						if (number2 <= 3) {
							money = 78 * costcount;
						} else {
							money = 234;
						}
						break;
					case 3:
						if (number2 <= 2) {
							money = 78 * costcount;
						} else {
							money = 156;
						}
						break;
					default:
						break;
					}
				}
				break;
			case 18:// 任5胆拖
				if (costcount > 0) {
					money = 540;
				}
				break;
			case 19:// 任6胆拖
				if (costcount > 0) {
					switch (number) {
					case 1:
						money = (number2 - 4) * 90;
						break;
					case 2:
						money = (number2 - 3) * 90;
						break;
					case 3:
						money = (number2 - 2) * 90;
						break;
					case 4:
						money = (number2 - 1) * 90;
						break;
					case 5:
						money = number2 * 90;
						break;
					}
				}
				break;
			case 20:// 任七胆拖
				if (costcount > 0) {
					int n = 0;
					switch (number) {
					case 1:
						n = number2 - 5;
						money = n * (n + 1) / 2 * 26;
						break;
					case 2:
						n = number2 - 4;
						money = n * (n + 1) / 2 * 26;
						break;
					case 3:
						n = number2 - 3;
						money = n * (n + 1) / 2 * 26;
						break;
					case 4:
						n = number2 - 2;
						money = n * (n + 1) / 2 * 26;
						break;
					case 5:
						n = number2 - 1;
						money = n * (n + 1) / 2 * 26;
						break;
					case 6:
						money = n * 26;
						break;
					}
				}
				break;
			case 21:// 任八胆拖
				if (costcount > 0) {
					int n = 0;
					switch (number) {
					case 1:
						int result = NumberTools.C_m_n(number2 - 4, 3);
						money = result * 9;
						break;
					case 2:
						int result2 = NumberTools.C_m_n(number2 - 3, 3);
						money = result2 * 9;
						break;
					case 3:
						int result3 = NumberTools.C_m_n(number2 - 2, 3);
						money = result3 * 9;
						break;
					case 4:
						int result4 = NumberTools.C_m_n(number2 - 1, 3);
						money = result4 * 9;
						break;
					case 5:
						int result5 = NumberTools.C_m_n(number2, 3);
						money = result5 * 9;
						break;
					case 6:
						int result6 = NumberTools.C_m_n(number2, 2);
						money = result6 * 9;
						break;
					case 7:
						money = number2 * 9;
						break;
					}
				}
				break;

			case 13:// 前二组选胆拖
				if (costcount > 0) {
					money = 65;
				}
				break;
			case 14:// 前三组选胆拖
				if (costcount > 0) {
					money = 195;
				}
				break;

			case 22:// 乐选2
				if (costcount > 0) {
					money = 201;
				}
				break;
			case 23:// 乐选3
				if (costcount > 0) {
					money = 1384;
				}
				break;
			case 24:// 乐选4
				if (costcount > 0) {
					money = 154;
				}
				break;
			case 25:// 乐选5
				if (costcount > 0) {
					money = 1080;
				}
				break;
			default:
				break;
			}

		} else {// 最小奖金

			switch (play) {
			case 1:// 前一
				if (number > 0) {
					money = 13;
				}
				break;
			case 2:// 任选二
				if (number > 1 && number < 9) {
					money = 6;
				} else if (number == 9) {
					money = 18;
				} else if (number == 10) {
					money = 36;
				} else if (number == 11) {
					money = 60;
				}
				break;
			case 3:
				if (number > 2 && number < 10) {
					money = 19;
				} else if (number == 10) {
					money = 76;
				} else if (number == 11) {
					money = 190;
				}

				break;
			case 4:
				if (number > 3 && number < 11) {
					money = 78;
				} else if (number == 11) {
					money = 390;
				}
				break;
			case 5:
				if (number > 4) {
					money = 540;
				}
				break;
			case 6:
				if (number > 5) {
					money = 90 * (number - 5);
				}
				break;
			case 7:
				if (number > 6) {
					int n = number - 6;
					money = 26 * (n * (n + 1) / 2);
				}
				break;
			case 8:
				if (number > 7) {
					int n = number - 7;
					money = 9 * (n * (n + 1) * (n + 2) / 6);
				}
				break;
			case 9:// 前二直选
				if (costcount > 0) {
					money = 130;
				}
				break;
			case 10:// 前三直选
				if (costcount > 0) {
					money = 1170;
				}
				break;
			case 11:// 前二组选
				if (costcount > 0) {
					money = 65;
				}
				break;
			case 12:// 前三组选
				if (costcount > 0) {
					money = 195;
				}
				break;
			case 15:// 任二胆拖
				if (costcount > 0) {
					if (number2 < 8) {
						money = 6;
					} else {
						money = 6 * (number2 - 6);
					}
				}
				break;
			case 16:// 任3胆拖
				if (costcount > 0) {
					if (number == 1) {
						if (number2 <= 8) {
							money = 19;
						} else if (number2 == 9) {
							money = 57;
						} else if (number2 == 10) {
							money = 114;
						}

					} else {
						if (number2 <= 7) {
							money = 19;
						} else if (number2 == 8) {
							money = 38;
						} else if (number2 == 9) {
							money = 57;
						}
					}
				}
				break;
			case 17:// 任4胆拖
				if (costcount > 0) {
					switch (number) {
					case 1:
						if (number2 <= 9) {
							money = 78;
						} else {
							money = 312;
						}
						break;
					case 2:
						if (number2 <= 8) {
							money = 78;
						} else {
							money = 234;
						}
						break;
					case 3:
						if (number2 <= 7) {
							money = 78;
						} else {
							money = 156;
						}
						break;
					default:
						break;
					}
				}
				break;
			case 18:// 任5胆拖
				if (costcount > 0) {
					money = 540;
				}
				break;
			case 19:// 任6胆拖
				if (costcount > 0) {
					money = 90;
				}
				break;
			case 20:// 任七胆拖
				if (costcount > 0) {
					switch (number) {
					case 1:
						int n = number2 - 5;
						money = n * 26;
						break;
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						money = 26;
						break;
					}
				}
				break;
			case 21:// 任八胆拖
				if (costcount > 0) {
					switch (number) {
					case 1:
						int result = NumberTools.C_m_n(number2 - 5, 2);
						money = result * 9;
						break;
					case 2:
						int result2 = number2 - 5;
						money = result2 * 9;
						break;
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						money = 9;

						break;
					}
				}
				break;

			case 13:// 前二组选胆拖
				if (costcount > 0) {
					money = 65;
				}
				break;
			case 14:// 前三组选胆拖
				if (costcount > 0) {
					money = 195;
				}
				break;

			case 22:// 乐选2
				if (costcount > 0) {
					money = 6;
				}
				break;
			case 23:// 乐选3
				if (costcount > 0) {
					money = 19;
				}
				break;
			case 24:// 乐选4
				if (costcount > 0) {
					money = 19;
				}
				break;
			case 25:// 乐选5
				if (costcount > 0) {
					money = 90;
				}
				break;
			}
		}
		return money;
	}

	/**
	 * 安徽快三的奖金预测方法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param playType
	 *            ：玩法ID
	 * @param list1
	 *            ：选号集合1
	 * @param list2
	 *            :选号集合2
	 * @param list3
	 *            :选号集合3
	 * @return
	 */
	public static int getJiangjinFor_Anhuik3(String mark, int playType,
			List<String> list1, List<String> list2, List<String> list3) {
		int money = 0;
		if (mark.equals("max")) {// 获取最大奖金
			switch (playType) {
			case 8401:// 和值
				if (list1.contains("10") || list1.contains("11")) {
					money = 9;
				}
				if (list1.contains("9") || list1.contains("12")) {
					money = 10;
				}
				if (list1.contains("8") || list1.contains("13")) {
					money = 12;
				}
				if (list1.contains("7") || list1.contains("14")) {
					money = 16;
				}
				if (list1.contains("6") || list1.contains("15")) {
					money = 25;
				}
				if (list1.contains("5") || list1.contains("16")) {
					money = 40;
				}
				if (list1.contains("4") || list1.contains("17")) {
					money = 80;
				}
				if (list1.contains("3") || list1.contains("18")) {
					money = 240;
				}
				break;
			case 8403: // 三同号
				if (list1.size() == 0 && list3.size() > 0) {
					money = 40;
				} else if (list1.size() > 0 && list3.size() == 0) {
					money = 240;
				} else if (list1.size() > 0 && list3.size() > 0) {
					money = 280;
				}
				break;
			case 8405:// 二同号

				if (list1.size() * list2.size() == 0 && list3.size() > 0) {
					money = 15;
				}
				if (list1.size() * list2.size() > 0 && list3.size() == 0) {
					money = 80;
				}
				if (list1.size() * list2.size() > 0 && list3.size() > 0) {
					money = 80;
					for (int i = 0; i < list1.size(); i++) {
						if (list3.contains(list1.get(i))) {
							money = 95;
							break;
						}
					}
				}
				break;
			case 8406:// 三不同号 1 2 3 4 5 6
				if (list3.size() > 0 && list1.size() < 3) {
					money = 10;
				}
				if (list3.size() == 0 && list1.size() > 2) {
					money = 40;
				}
				if (list3.size() > 0 && list1.size() > 2) {
					money = 50;
				}
				break;

			case 8407:// 二不同号1 2 3 4 5 6
				if (list1.size() > 1) {
					money = 8;
				}
				if (list1.size() > 2) {
					money = 24;
				}
				break;
			default:
				break;
			}
		} else {
			switch (playType) {
			case 8401:// 和值
				if (list1.contains("3") || list1.contains("18")) {
					money = 240;
				}
				if (list1.contains("4") || list1.contains("17")) {
					money = 80;
				}
				if (list1.contains("5") || list1.contains("16")) {
					money = 40;
				}
				if (list1.contains("6") || list1.contains("15")) {
					money = 25;
				}
				if (list1.contains("7") || list1.contains("14")) {
					money = 16;
				}
				if (list1.contains("8") || list1.contains("13")) {
					money = 12;
				}
				if (list1.contains("9") || list1.contains("12")) {
					money = 10;
				}
				if (list1.contains("10") || list1.contains("11")) {
					money = 9;
				}
				break;

			case 8403: // 三同号
				if (list1.size() == 6 && list3.size() > 0) {
					money = 280;
				} else if (list3.size() == 0 && list1.size() > 0) {
					money = 240;
				} else if (list3.size() > 0 && list1.size() != 6) {
					money = 40;
				}
				break;

			case 8405:// 二同号
				if (list1.size() * list2.size() > 0 && list3.size() < 1) {
					money = 80;
				} else if (list3.size() > 0) {
					money = 15;
					if (list1.size() == 1 && list2.size() == 5) {
						if (list3.contains(list1.get(0))) {
							money = 95;
						}
					}
				}
				break;
			case 8406:// 三不同号 1 2 3 4 5 6
				if ((list3.size() == 0 && list1.size() > 2)
						|| (list3.size() > 0 && list1.size() == 6)) {
					money = 40;
				}
				if (list3.size() > 0 && list1.size() != 6) {
					money = 10;
				}
				break;
			case 8407:// 二不同号1 2 3 4 5 6
				if (list1.size() > 1) {
					money = 8;
				}
				break;
			default:
				break;
			}
		}
		return money;
	}

	/**
	 * 江苏快三的奖金预测方法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param playType
	 *            ：玩法ID
	 * @param list1
	 *            ：选号集合1
	 * @param list2
	 *            :选号集合2
	 * @param list3
	 *            :选号集合3
	 * @return
	 */
	public static int getJiangjinFor_k3(String mark, int playType,
			List<String> list1, List<String> list2, List<String> list3) {
		int money = 0;
		if (mark.equals("max")) {// 获取最大奖金
			switch (playType) {
			case 8301:// 和值
				if (list1.contains("10") || list1.contains("11")) {
					money = 9;
				}
				if (list1.contains("9") || list1.contains("12")) {
					money = 10;
				}
				if (list1.contains("8") || list1.contains("13")) {
					money = 12;
				}
				if (list1.contains("7") || list1.contains("14")) {
					money = 16;
				}
				if (list1.contains("6") || list1.contains("15")) {
					money = 25;
				}
				if (list1.contains("5") || list1.contains("16")) {
					money = 40;
				}
				if (list1.contains("4") || list1.contains("17")) {
					money = 80;
				}
				if (list1.contains("3") || list1.contains("18")) {
					money = 240;
				}
				break;
			case 8303: // 三同号
				if (list1.size() == 0 && list3.size() > 0) {
					money = 40;
				} else if (list1.size() > 0 && list3.size() == 0) {
					money = 240;
				} else if (list1.size() > 0 && list3.size() > 0) {
					money = 280;
				}
				break;
			case 8305:// 二同号

				if (list1.size() * list2.size() == 0 && list3.size() > 0) {
					money = 15;
				}
				if (list1.size() * list2.size() > 0 && list3.size() == 0) {
					money = 80;
				}
				if (list1.size() * list2.size() > 0 && list3.size() > 0) {
					money = 80;
					for (int i = 0; i < list1.size(); i++) {
						if (list3.contains(list1.get(i))) {
							money = 95;
							break;
						}
					}
				}
				break;
			case 8306:// 三不同号 1 2 3 4 5 6
				if (list3.size() > 0 && list1.size() < 3) {
					money = 10;
				}
				if (list3.size() == 0 && list1.size() > 2) {
					money = 40;
				}
				if (list3.size() > 0 && list1.size() > 2) {
					money = 50;
				}
				break;

			case 8307:// 二不同号1 2 3 4 5 6
				if (list1.size() > 1) {
					money = 8;
				}
				if (list1.size() > 2) {
					money = 24;
				}
				break;
			default:
				break;
			}
		} else {
			switch (playType) {
			case 8301:// 和值
				if (list1.contains("3") || list1.contains("18")) {
					money = 240;
				}
				if (list1.contains("4") || list1.contains("17")) {
					money = 80;
				}
				if (list1.contains("5") || list1.contains("16")) {
					money = 40;
				}
				if (list1.contains("6") || list1.contains("15")) {
					money = 25;
				}
				if (list1.contains("7") || list1.contains("14")) {
					money = 16;
				}
				if (list1.contains("8") || list1.contains("13")) {
					money = 12;
				}
				if (list1.contains("9") || list1.contains("12")) {
					money = 10;
				}
				if (list1.contains("10") || list1.contains("11")) {
					money = 9;
				}
				break;

			case 8303: // 三同号
				if (list1.size() == 6 && list3.size() > 0) {
					money = 280;
				} else if (list3.size() == 0 && list1.size() > 0) {
					money = 240;
				} else if (list3.size() > 0 && list1.size() != 6) {
					money = 40;
				}
				break;

			case 8305:// 二同号
				if (list1.size() * list2.size() > 0 && list3.size() < 1) {
					money = 80;
				} else if (list3.size() > 0) {
					money = 15;
					if (list1.size() == 1 && list2.size() == 5) {
						if (list3.contains(list1.get(0))) {
							money = 95;
						}
					}
				}
				break;
			case 8306:// 三不同号 1 2 3 4 5 6
				if ((list3.size() == 0 && list1.size() > 2)
						|| (list3.size() > 0 && list1.size() == 6)) {
					money = 40;
				}
				if (list3.size() > 0 && list1.size() != 6) {
					money = 10;
				}
				break;
			case 8307:// 二不同号1 2 3 4 5 6
				if (list1.size() > 1) {
					money = 8;
				}
				break;
			default:
				break;
			}
		}
		return money;
	}

	/**
	 * 北京单场的奖金预测方法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param guoguan
	 *            ：过关方式
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_jiangjin(String mark, String guoguan,
			List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			if (guoguan.contains("单关")) {
				money += getbjdc_danguan("max", list_jiangjin);
			}
			if (guoguan.contains("2串1")) {
				money += getbjdc_2x1("max", list_jiangjin);
			}
			if (guoguan.contains("2串3")) {// 2个1关+1个两关
				double result1 = getbjdc_jiangjin("max", "单关", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "2串1", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 1, 1);
				money += result1 * beishu + result2;
			}
			if (guoguan.contains("3串1")) {
				money += getbjdc_3x1("max", list_jiangjin);
			}
			if (guoguan.contains("4串1")) {
				money += getbjdc_4x1("max", list_jiangjin);
			}
			if (guoguan.contains("5串1")) {
				money += getbjdc_5x1("max", list_jiangjin);
			}
			if (guoguan.contains("6串1")) {
				money += getbjdc_6x1("max", list_jiangjin);
			}
			if (guoguan.contains("7串1")) {
				money += getbjdc_7x1("max", list_jiangjin);
			}
			if (guoguan.contains("8串1")) {
				money += getbjdc_8x1("max", list_jiangjin);
			}
			if (guoguan.contains("9串1")) {
				money += getbjdc_9x1("max", list_jiangjin);
			}
			if (guoguan.contains("10串1")) {
				money += getbjdc_10x1("max", list_jiangjin);
			}
			if (guoguan.contains("11串1")) {
				money += getbjdc_11x1("max", list_jiangjin);
			}
			if (guoguan.contains("12串1")) {
				money += getbjdc_12x1("max", list_jiangjin);
			}
			if (guoguan.contains("13串1")) {
				money += getbjdc_13x1("max", list_jiangjin);
			}
			if (guoguan.contains("14串1")) {
				money += getbjdc_14x1("max", list_jiangjin);
			}
			if (guoguan.contains("15串1")) {
				money += getbjdc_15x1("max", list_jiangjin);
			}

			if (guoguan.contains("3串3")) {
				double result = getbjdc_2x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 2);
			}

			if (guoguan.contains("3串4")) {
				double result = getbjdc_2x1("max", list_jiangjin);
				double result2 = getbjdc_3x1("max", list_jiangjin);
				double resultAll = result * (list_jiangjin.size() - 2);
				money += resultAll + result2;
			}

			if (guoguan.contains("3串7")) {// 3个一关+3个三关+1个三关
				double result1 = getbjdc_jiangjin("max", "单关", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "3串4", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 1, 2);
				money += result1 * beishu + result2;
			}
			if (guoguan.contains("4串4")) {// 4个三关
				double result = getbjdc_3x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 3);
			}
			if (guoguan.contains("4串5")) {// 4个三关+1个四关
				double result = getbjdc_3x1("max", list_jiangjin);
				double result2 = getbjdc_4x1("max", list_jiangjin);
				double resultAll = result * (list_jiangjin.size() - 3);
				money += resultAll + result2;
			}

			if (guoguan.contains("4串6")) {// 6个两关
				double result = getbjdc_2x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 3;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			}

			if (guoguan.contains("4串11")) {// 6个两关+4个三关+1个四关(4串11也包含4串1,所以需要减去一个4串1)
				double result4x1 = getbjdc_4x1("max", list_jiangjin);
				double result1 = getbjdc_jiangjin("max", "4串6", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "4串5", list_jiangjin);
				money += result1 + result2 - result4x1;
			}

			if (guoguan.contains("4串15")) {// 4个1关+6个两关+4个三关+1个四关(4串15也包含4串1,所以需要减去一个4串1)
				double result4x1 = getbjdc_4x1("max", list_jiangjin);
				double result1 = getbjdc_jiangjin("max", "单关", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "4串11", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 1, 3);
				money += result1 * beishu + result2 - result4x1;
			}

			if (guoguan.contains("5串5")) {// 5个四关
				double result = getbjdc_4x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 4);
			}

			if (guoguan.contains("5串6")) {// 5个四关+1个5关
				double result = getbjdc_4x1("max", list_jiangjin);
				double result2 = getbjdc_5x1("max", list_jiangjin);
				double resultAll = result * (list_jiangjin.size() - 4);
				money += resultAll + result2;
			}
			if (guoguan.contains("5串10")) {// 10个两关(5串10也包含5串1,所以需要减去一个5串1)
				double result5x1 = getbjdc_5x1("max", list_jiangjin);
				double result = getbjdc_2x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) * (n + 2) / 6;
				money += result * beishu - result5x1;
			}
			if (guoguan.contains("5串16")) {// 10个三关+5个四关+1个5关(5串16也包含5串1,所以需要减去一个5串1)
				double result5x1 = getbjdc_5x1("max", list_jiangjin);
				double result1 = getbjdc_jiangjin("max", "5串6", list_jiangjin);
				double re_3x1 = getbjdc_3x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) / 2;
				double result2 = beishu * re_3x1;
				money += result1 + result2 - result5x1;
			}
			if (guoguan.contains("5串20")) {// 10个两关+10个三关
				double result1 = getbjdc_jiangjin("max", "5串10", list_jiangjin);
				double re_3x1 = getbjdc_3x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) / 2;
				double result2 = beishu * re_3x1;
				money += result1 + result2;
			}

			if (guoguan.contains("5串26")) {// 10个两关+10个三关+5个四关+1个5关
				double result1 = getbjdc_jiangjin("max", "5串6", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "5串20", list_jiangjin);
				money += result1 + result2;
			}

			if (guoguan.contains("5串31")) {// 5个单关+10个两关+10个三关+5个四关+1个5关
				double result1 = getbjdc_jiangjin("max", "单关", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "5串26", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 1, 4);
				money += result1 * beishu + result2;
			}

			if (guoguan.contains("6串6")) {// 6个六关
				double result = getbjdc_5x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 5);
			}

			if (guoguan.contains("6串7")) {// 6个六关+1个七关
				double result1 = getbjdc_jiangjin("max", "6串6", list_jiangjin);
				double result2 = getbjdc_6x1("max", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("6串15")) {// 15个2关(6串15也包含5串1,所以需要减去一个6串1)
				double result6x1 = getbjdc_6x1("max", list_jiangjin);
				double result = getbjdc_2x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 2, 4);
				money += result * beishu - result6x1;
			}
			if (guoguan.contains("6串20")) {// 20个三关
				double result = getbjdc_3x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 3, 3);
				money += result * beishu;
			}
			if (guoguan.contains("6串22")) {// 15个四关+6个五关+1个六关
				double result1 = getbjdc_4x1("max", list_jiangjin);
				double result2 = getbjdc_5x1("max", list_jiangjin);
				double result3 = getbjdc_6x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 5;
				int beishu_4x1 = n * (n + 1) / 2;
				int beishu_5x1 = list_jiangjin.size() - 5;
				money += result1 * beishu_4x1 + result2 * beishu_5x1 + result3;
			}
			if (guoguan.contains("6串35")) {// 15个两关+20个三关
				double result1 = getbjdc_jiangjin("max", "6串15", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "6串20", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("6串42")) {// 20个三关+15个四关+6个五关+1个六关
				double result1 = getbjdc_jiangjin("max", "6串20", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "6串22", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("6串50")) {// 15个两关+20个三关+15个四关
				double result1 = getbjdc_jiangjin("max", "6串35", list_jiangjin);
				double result = getbjdc_4x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 5;
				int beishu_4x1 = n * (n + 1) / 2;
				double result2 = result * beishu_4x1;
				money += result1 + result2;
			}
			if (guoguan.contains("6串57")) {// 15个两关+20个三关+15个四关+6个五关+1个六关
				double result1 = getbjdc_jiangjin("max", "6串15", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "6串42", list_jiangjin);
				money += result1 + result2;
			}

			if (guoguan.contains("6串63")) {// 6个单关+15个两关+20个三关+15个四关+6个五关+1个六关(6串63也包含6串6,所以需要减去一个6串6)
				double result6x6 = getbjdc_jiangjin("max", "6串6", list_jiangjin);
				double result1 = getbjdc_jiangjin("max", "单关", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "6串57", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 1, 5);
				money += result1 * beishu + result2 - result6x6;
			}
			if (guoguan.contains("7串7")) {// 7个六关
				double result = getbjdc_6x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 6);
			}
			if (guoguan.contains("7串8")) {// 7个六关+1个七关
				double result1 = getbjdc_jiangjin("max", "7串7", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "7串1", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("7串21")) {// 21个五关
				double result = getbjdc_5x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 6;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			}
			if (guoguan.contains("7串35")) {// 35个四关
				double result = getbjdc_4x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 4, 3);
				money += result * beishu;
			}
			if (guoguan.contains("7串120")) {// 21个两关+35个三关+35个四关+21个五关+7个六关+1个七关(7串120也包含7串1,所以需减去一个7串1)
				double result7x1 = getbjdc_7x1("max", list_jiangjin);
				double result1 = getbjdc_jiangjin("max", "7串8", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "7串21", list_jiangjin);
				double result3 = getbjdc_jiangjin("max", "7串35", list_jiangjin);
				double result_2x1 = getbjdc_2x1("max", list_jiangjin);
				double result_3x1 = getbjdc_3x1("max", list_jiangjin);
				int beishu_2x1 = NumberTools.C_m_n(list_jiangjin.size() - 2, 5);
				int beishu_3x1 = NumberTools.C_m_n(list_jiangjin.size() - 3, 4);
				money += result1 + result2 + result3 + result_2x1 * beishu_2x1
						+ result_3x1 * beishu_3x1 - result7x1;
			}
			if (guoguan.contains("8串8")) {// 8个七关
				double result = getbjdc_7x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 7);
			}
			if (guoguan.contains("8串9")) {// 8个七关+1个8关
				double result1 = getbjdc_jiangjin("max", "8串8", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "8串1", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("8串28")) {// 28个六关
				double result = getbjdc_6x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 6, 2);
				money += result * beishu;
			}
			if (guoguan.contains("8串56")) {// 56个五关
				double result = getbjdc_5x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 5, 3);
				money += result * beishu;
			}
			if (guoguan.contains("8串70")) {// 70个四关
				double result = getbjdc_4x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 4, 4);
				money += result * beishu;
			}
			if (guoguan.contains("8串247")) {// 28个两关+56个三关+70个四关+56个五关+28个六关+8个七关+1个八关
				double result1 = getbjdc_jiangjin("max", "8串9", list_jiangjin);
				double result2 = getbjdc_jiangjin("max", "8串28", list_jiangjin);
				double result3 = getbjdc_jiangjin("max", "8串56", list_jiangjin);
				double result4 = getbjdc_jiangjin("max", "8串70", list_jiangjin);

				double result_2x1 = getbjdc_2x1("max", list_jiangjin);
				double result_3x1 = getbjdc_3x1("max", list_jiangjin);

				int beishu_2x1 = NumberTools.C_m_n(list_jiangjin.size() - 2, 6);
				int beishu_3x1 = NumberTools.C_m_n(list_jiangjin.size() - 3, 5);
				money += result1 + result2 + result3 + result4 + result_2x1
						* beishu_2x1 + result_3x1 * beishu_3x1;
			}

		} else if (mark.equals("min")) {
			if (guoguan.contains("单关")) {
				money += getbjdc_danguan("min", list_jiangjin);
			} else if (guoguan.contains("2串1")) {
				money += getbjdc_2x1("min", list_jiangjin);
			} else if (guoguan.contains("2串3")) {// 2个1关+1个两关
				money += getbjdc_danguan("min", list_jiangjin);
			} else if (guoguan.contains("3串1")) {
				money += getbjdc_3x1("min", list_jiangjin);
			} else if (guoguan.contains("4串1") && !guoguan.contains("4串11")
					&& !guoguan.contains("4串11")) {
				money += getbjdc_4x1("min", list_jiangjin);
			} else if (guoguan.contains("5串1") && !guoguan.contains("5串10")
					&& !guoguan.contains("5串16")) {
				money += getbjdc_5x1("min", list_jiangjin);
			} else if (guoguan.contains("6串1") && !guoguan.contains("6串15")) {
				money += getbjdc_6x1("min", list_jiangjin);
			} else if (guoguan.contains("7串1") && !guoguan.contains("7串120")) {
				money += getbjdc_7x1("min", list_jiangjin);
			} else if (guoguan.contains("8串1")) {
				money += getbjdc_8x1("min", list_jiangjin);
			} else if (guoguan.contains("9串1")) {
				money += getbjdc_9x1("min", list_jiangjin);
			} else if (guoguan.contains("10串1")) {
				money += getbjdc_10x1("min", list_jiangjin);
			} else if (guoguan.contains("11串1")) {
				money += getbjdc_11x1("min", list_jiangjin);
			} else if (guoguan.contains("12串1")) {
				money += getbjdc_12x1("min", list_jiangjin);
			} else if (guoguan.contains("13串1")) {
				money += getbjdc_13x1("min", list_jiangjin);
			} else if (guoguan.contains("14串1")) {
				money += getbjdc_14x1("min", list_jiangjin);
			} else if (guoguan.contains("15串1")) {
				money += getbjdc_15x1("min", list_jiangjin);
			} else if (guoguan.contains("3串3")) {
				double result = getbjdc_2x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 2);
			} else if (guoguan.contains("3串4")) {
				double result = getbjdc_2x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 2);

			} else if (guoguan.contains("3串7")) {// 3个一关+3个三关+1个三关
				money += getbjdc_danguan("min", list_jiangjin);
			} else if (guoguan.contains("4串4")) {
				double result = getbjdc_3x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 3);

			} else if (guoguan.contains("4串5")) {
				double result = getbjdc_3x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 3);

			} else if (guoguan.contains("4串6")) {
				double result = getbjdc_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 3;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			} else if (guoguan.contains("4串11")) {
				double result = getbjdc_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 3;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			} else if (guoguan.contains("4串15")) {// 4个1关+6个两关+4个三关+1个四关
				money += getbjdc_danguan("min", list_jiangjin);
			} else if (guoguan.contains("5串5")) {
				double result = getbjdc_4x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 4);

			} else if (guoguan.contains("5串6")) {
				double result = getbjdc_4x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 4);
			} else if (guoguan.contains("5串10")) {
				double result = getbjdc_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) * (n + 2) / 6;
				money += result * beishu;
			} else if (guoguan.contains("5串16")) {
				double result = getbjdc_3x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			} else if (guoguan.contains("5串20")) {// 等于5串10
				double result = getbjdc_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) * (n + 2) / 6;
				money += result * beishu;
			} else if (guoguan.contains("5串26")) {// 等于5串10
				double result = getbjdc_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) * (n + 2) / 6;
				money += result * beishu;
			} else if (guoguan.contains("5串31")) {// 5个单关+10个两关+10个三关+5个四关+1个5关
				money += getbjdc_danguan("min", list_jiangjin);
			} else if (guoguan.contains("6串6") && !guoguan.contains("6串63")) {// 6个六关
				double result = getbjdc_5x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 5);
			} else if (guoguan.contains("6串7")) {
				double result = getbjdc_5x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 5);
			} else if (guoguan.contains("6串15")) {// 15个2关
				double result = getbjdc_2x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 2, 4);
				money += result * beishu;
			} else if (guoguan.contains("6串20")) {// 20个三关
				double result = getbjdc_3x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 3, 3);
				money += result * beishu;
			} else if (guoguan.contains("6串22")) {// 15个四关+6个五关+1个六关
				double result = getbjdc_4x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 5;
				int beishu_4x1 = n * (n + 1) / 2;
				money += result * beishu_4x1;
			} else if (guoguan.contains("6串35")) {// 15个两关+20个三关
				money += getbjdc_jiangjin("min", "6串15", list_jiangjin);
			} else if (guoguan.contains("6串42")) {// 20个三关+15个四关+6个五关+1个六关
				money += getbjdc_jiangjin("min", "6串20", list_jiangjin);
			} else if (guoguan.contains("6串50")) {// 15个两关+20个三关+15个四关
				money += getbjdc_jiangjin("min", "6串35", list_jiangjin);
			} else if (guoguan.contains("6串57")) {// 15个两关+20个三关+15个四关+6个五关+1个六关
				money += getbjdc_jiangjin("min", "6串15", list_jiangjin);
			} else if (guoguan.contains("6串63")) {// 6个单关+15个两关+20个三关+15个四关+6个五关+1个六关
				money += getbjdc_danguan("min", list_jiangjin);
			}

			else if (guoguan.contains("7串7")) {// 7个六关
				double result = getbjdc_6x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 6);
			} else if (guoguan.contains("7串8")) {// 7个六关+1个七关
				money += getbjdc_jiangjin("min", "7串7", list_jiangjin);
			} else if (guoguan.contains("7串21")) {// 21个五关
				double result = getbjdc_5x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 6;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			} else if (guoguan.contains("7串35")) {// 35个四关
				double result = getbjdc_4x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 4, 3);
				money += result * beishu;
			} else if (guoguan.contains("7串120")) {// 21个两关+35个三关+35个四关+21个五关+7个六关+1个七关
				double result = getbjdc_2x1("min", list_jiangjin);
				int beishu_2x1 = NumberTools.C_m_n(list_jiangjin.size() - 2, 5);
				money += result * beishu_2x1;
			} else if (guoguan.contains("8串8")) {// 8个七关
				double result = getbjdc_7x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 7);
			} else if (guoguan.contains("8串9")) {// 8个七关+1个8关
				money += getbjdc_jiangjin("min", "8串8", list_jiangjin);
			} else if (guoguan.contains("8串28")) {// 28个六关
				double result = getbjdc_6x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 6, 2);
				money += result * beishu;
			} else if (guoguan.contains("8串56")) {// 56个五关
				double result = getbjdc_5x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 5, 3);
				money += result * beishu;
			} else if (guoguan.contains("8串70")) {// 70个四关
				double result = getbjdc_4x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 4, 4);
				money += result * beishu;
			} else if (guoguan.contains("8串247")) {// 28个两关+56个三关+70个四关+56个五关+28个六关+8个七关+1个八关
				double result = getbjdc_2x1("min", list_jiangjin);
				int beishu_2x1 = NumberTools.C_m_n(list_jiangjin.size() - 2, 6);
				money += result * beishu_2x1;
			}
		}
		DecimalFormat df = new DecimalFormat("########0.00");
		String result = df.format(money);
		double jiangjin = Double.parseDouble(result);
		return jiangjin;
	}

	/**
	 * 竞彩足球/篮球的奖金预测方法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param guoguan
	 *            ：过关方式
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_jiangjin(String mark, String guoguan,
			List<Double> list_jiangjin) {
		System.out.println("=========guoguan=====" + guoguan);
		double money = 0;
		if (mark.equals("max")) {
			if (guoguan.contains("单关")) {
				money += getjczq_danguan("max", list_jiangjin);
			}
			if (guoguan.contains("2串1")) {
				money += getjczq_2x1("max", list_jiangjin);
			}
			if (guoguan.contains("3串1")) {
				money += getjczq_3x1("max", list_jiangjin);
			}
			if (guoguan.contains("4串1")) {
				money += getjczq_4x1("max", list_jiangjin);
			}
			if (guoguan.contains("5串1")) {
				money += getjczq_5x1("max", list_jiangjin);
			}
			if (guoguan.contains("6串1")) {
				money += getjczq_6x1("max", list_jiangjin);
			}
			if (guoguan.contains("7串1")) {
				money += getjczq_7x1("max", list_jiangjin);
			}
			if (guoguan.contains("8串1")) {
				money += getjczq_8x1("max", list_jiangjin);
			}
			if (guoguan.contains("9串1")) {
				money += getjczq_9x1("max", list_jiangjin);
			}
			if (guoguan.contains("10串1")) {
				money += getjczq_10x1("max", list_jiangjin);
			}
			if (guoguan.contains("11串1")) {
				money += getjczq_11x1("max", list_jiangjin);
			}
			if (guoguan.contains("12串1")) {
				money += getjczq_12x1("max", list_jiangjin);
			}
			if (guoguan.contains("13串1")) {
				money += getjczq_13x1("max", list_jiangjin);
			}
			if (guoguan.contains("14串1")) {
				money += getjczq_14x1("max", list_jiangjin);
			}
			if (guoguan.contains("15串1")) {
				money += getjczq_15x1("max", list_jiangjin);
			}

			if (guoguan.contains("3串3")) {
				double result = getjczq_2x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 2);
			}

			if (guoguan.contains("3串4")) {
				double result = getjczq_2x1("max", list_jiangjin);
				double result2 = getjczq_3x1("max", list_jiangjin);
				double resultAll = result * (list_jiangjin.size() - 2);
				money += resultAll + result2;
			}
			if (guoguan.contains("4串4")) {// 4个三关
				double result = getjczq_3x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 3);
			}
			if (guoguan.contains("4串5")) {// 4个三关+1个四关
				double result = getjczq_3x1("max", list_jiangjin);
				double result2 = getjczq_4x1("max", list_jiangjin);
				double resultAll = result * (list_jiangjin.size() - 3);
				money += resultAll + result2;
			}

			if (guoguan.contains("4串6")) {// 6个两关
				double result = getjczq_2x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 3;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			}

			if (guoguan.contains("4串11")) { // 6个两关+4个三关+1个四关(4串11也包含4串1,所有也会执行4串1的方法)
				double result3 = getjczq_4x1("max", list_jiangjin);
				double result1 = getjczq_jiangjin("max", "4串6", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "4串5", list_jiangjin);
				money += result1 + result2 - result3;
			}

			if (guoguan.contains("5串5")) {// 5个四关
				double result = getjczq_4x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 4);
			}

			if (guoguan.contains("5串6")) {// 5个四关+1个5关
				double result = getjczq_4x1("max", list_jiangjin);
				double result2 = getjczq_5x1("max", list_jiangjin);
				double resultAll = result * (list_jiangjin.size() - 4);
				money += resultAll + result2;
			}
			if (guoguan.contains("5串10")) {// 10个两关(5串10也包含5串1，所以要减一个5串1)
				double result3 = getjczq_5x1("max", list_jiangjin);
				double result = getjczq_2x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) * (n + 2) / 6;
				money += result * beishu - result3;
			}
			if (guoguan.contains("5串16")) {// 10个三关+5个四关+1个5关(5串16也包含5串1，所以要减一个5串1)
				double result3 = getjczq_5x1("max", list_jiangjin);
				double result1 = getjczq_jiangjin("max", "5串6", list_jiangjin);
				double re_3x1 = getjczq_3x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) / 2;
				double result2 = beishu * re_3x1;
				money += result1 + result2 - result3;
			}
			if (guoguan.contains("5串20")) {// 10个两关+10个三关
				double result1 = getjczq_jiangjin("max", "5串10", list_jiangjin);
				double re_3x1 = getjczq_3x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) / 2;
				double result2 = beishu * re_3x1;
				money += result1 + result2;
			}
			if (guoguan.contains("5串26")) {// 10个两关+10个三关+5个四关+1个5关
				double result1 = getjczq_jiangjin("max", "5串6", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "5串20", list_jiangjin);
				money += result1 + result2;
			}

			if (guoguan.contains("6串6")) {// 6个六关
				double result = getjczq_5x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 5);
			}

			if (guoguan.contains("6串7")) {// 6个六关+1个七关
				double result1 = getjczq_jiangjin("max", "6串6", list_jiangjin);
				double result2 = getjczq_6x1("max", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("6串15")) {// 15个2关(6串15也包含6串1，所以要减一个6串1)
				double result3 = getjczq_6x1("max", list_jiangjin);
				double result = getjczq_2x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 2, 4);
				money += result * beishu - result3;
			}
			if (guoguan.contains("6串20")) {// 20个三关
				double result = getjczq_3x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 3, 3);
				money += result * beishu;
			}
			if (guoguan.contains("6串22")) {// 15个四关+6个五关+1个六关
				double result1 = getjczq_4x1("max", list_jiangjin);
				double result2 = getjczq_5x1("max", list_jiangjin);
				double result3 = getjczq_6x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 5;
				int beishu_4x1 = n * (n + 1) / 2;
				int beishu_5x1 = list_jiangjin.size() - 5;
				money += result1 * beishu_4x1 + result2 * beishu_5x1 + result3;
			}
			if (guoguan.contains("6串35")) {// 15个两关+20个三关
				double result1 = getjczq_jiangjin("max", "6串15", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "6串20", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("6串42")) {// 20个三关+15个四关+6个五关+1个六关
				double result1 = getjczq_jiangjin("max", "6串20", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "6串22", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("6串50")) {// 15个两关+20个三关+15个四关
				double result1 = getjczq_jiangjin("max", "6串35", list_jiangjin);
				double result = getjczq_4x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 5;
				int beishu_4x1 = n * (n + 1) / 2;
				double result2 = result * beishu_4x1;
				money += result1 + result2;
			}
			if (guoguan.contains("6串57")) {// 15个两关+20个三关+15个四关+6个五关+1个六关
				double result1 = getjczq_jiangjin("max", "6串15", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "6串42", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("7串7")) {// 7个六关
				double result = getjczq_6x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 6);
			}
			if (guoguan.contains("7串8")) {// 7个六关+1个七关
				double result1 = getjczq_jiangjin("max", "7串7", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "7串1", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("7串21")) {// 21个五关
				double result = getjczq_5x1("max", list_jiangjin);
				int n = list_jiangjin.size() - 6;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			}
			if (guoguan.contains("7串35")) {// 35个四关
				double result = getjczq_4x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 4, 3);
				money += result * beishu;
			}
			if (guoguan.contains("7串120")) {// 21个两关+35个三关+35个四关+21个五关+7个六关+1个七关(7串120也包含7串1，所以要减一个7串1)
				double result_7x1 = getjczq_7x1("max", list_jiangjin);
				double result1 = getjczq_jiangjin("max", "7串8", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "7串21", list_jiangjin);
				double result3 = getjczq_jiangjin("max", "7串35", list_jiangjin);
				double result_2x1 = getjczq_2x1("max", list_jiangjin);
				double result_3x1 = getjczq_3x1("max", list_jiangjin);
				int beishu_2x1 = NumberTools.C_m_n(list_jiangjin.size() - 2, 5);
				int beishu_3x1 = NumberTools.C_m_n(list_jiangjin.size() - 3, 4);
				money += result1 + result2 + result3 + result_2x1 * beishu_2x1
						+ result_3x1 * beishu_3x1 - result_7x1;
			}
			if (guoguan.contains("8串8")) {// 8个七关
				double result = getjczq_7x1("max", list_jiangjin);
				money += result * (list_jiangjin.size() - 7);
			}
			if (guoguan.contains("8串9")) {// 8个七关+1个8关
				double result1 = getjczq_jiangjin("max", "8串8", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "8串1", list_jiangjin);
				money += result1 + result2;
			}
			if (guoguan.contains("8串28")) {// 28个六关
				double result = getjczq_6x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 6, 2);
				money += result * beishu;
			}
			if (guoguan.contains("8串56")) {// 56个五关
				double result = getjczq_5x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 5, 3);
				money += result * beishu;
			}
			if (guoguan.contains("8串70")) {// 70个四关
				double result = getjczq_4x1("max", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 4, 4);
				money += result * beishu;
			}
			if (guoguan.contains("8串247")) {// 28个两关+56个三关+70个四关+56个五关+28个六关+8个七关+1个八关
				double result1 = getjczq_jiangjin("max", "8串9", list_jiangjin);
				double result2 = getjczq_jiangjin("max", "8串28", list_jiangjin);
				double result3 = getjczq_jiangjin("max", "8串56", list_jiangjin);
				double result4 = getjczq_jiangjin("max", "8串70", list_jiangjin);

				double result_2x1 = getjczq_2x1("max", list_jiangjin);
				double result_3x1 = getjczq_3x1("max", list_jiangjin);

				int beishu_2x1 = NumberTools.C_m_n(list_jiangjin.size() - 2, 6);
				int beishu_3x1 = NumberTools.C_m_n(list_jiangjin.size() - 3, 5);
				money += result1 + result2 + result3 + result4 + result_2x1
						* beishu_2x1 + result_3x1 * beishu_3x1;
			}

		} else if (mark.equals("min")) {
			if (guoguan.contains("单关")) {
				money += getjczq_danguan("min", list_jiangjin);
			} else if (guoguan.contains("2串1")) {
				money += getjczq_2x1("min", list_jiangjin);
			} else if (guoguan.contains("3串1")) {
				money += getjczq_3x1("min", list_jiangjin);
			} else if (guoguan.contains("4串1") && !guoguan.contains("4串11")) {
				money += getjczq_4x1("min", list_jiangjin);
			} else if (guoguan.contains("5串1") && !guoguan.contains("5串10")
					&& !guoguan.contains("5串16")) {
				money += getjczq_5x1("min", list_jiangjin);
			} else if (guoguan.contains("6串1") && !guoguan.contains("6串15")) {
				money += getjczq_6x1("min", list_jiangjin);
			} else if (guoguan.contains("7串1") && !guoguan.contains("7串120")) {
				money += getjczq_7x1("min", list_jiangjin);
			} else if (guoguan.contains("8串1")) {
				money += getjczq_8x1("min", list_jiangjin);
			} else if (guoguan.contains("9串1")) {
				money += getjczq_9x1("min", list_jiangjin);
			} else if (guoguan.contains("10串1")) {
				money += getjczq_10x1("min", list_jiangjin);
			} else if (guoguan.contains("11串1")) {
				money += getjczq_11x1("min", list_jiangjin);
			} else if (guoguan.contains("12串1")) {
				money += getjczq_12x1("min", list_jiangjin);
			} else if (guoguan.contains("13串1")) {
				money += getjczq_13x1("min", list_jiangjin);
			} else if (guoguan.contains("14串1")) {
				money += getjczq_14x1("min", list_jiangjin);
			} else if (guoguan.contains("15串1")) {
				money += getjczq_15x1("min", list_jiangjin);
			} else if (guoguan.contains("3串3")) {
				double result = getjczq_2x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 2);
			} else if (guoguan.contains("3串4")) {
				double result = getjczq_2x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 2);

			} else if (guoguan.contains("4串4")) {
				double result = getjczq_3x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 3);

			} else if (guoguan.contains("4串5")) {
				double result = getjczq_3x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 3);

			} else if (guoguan.contains("4串6")) {
				double result = getjczq_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 3;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			} else if (guoguan.contains("4串11")) {
				double result = getjczq_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 3;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			} else if (guoguan.contains("5串5")) {
				double result = getjczq_4x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 4);

			} else if (guoguan.contains("5串6")) {
				double result = getjczq_4x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 4);
			} else if (guoguan.contains("5串10")) {
				double result = getjczq_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) * (n + 2) / 6;
				money += result * beishu;
			} else if (guoguan.contains("5串16")) {
				double result = getjczq_3x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			} else if (guoguan.contains("5串20")) {// 等于5串10
				double result = getjczq_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) * (n + 2) / 6;
				money += result * beishu;
			} else if (guoguan.contains("5串26")) {// 等于5串10
				double result = getjczq_2x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 4;
				int beishu = n * (n + 1) * (n + 2) / 6;
				money += result * beishu;
			} else if (guoguan.contains("6串6")) {// 6个六关
				double result = getjczq_5x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 5);
			} else if (guoguan.contains("6串7")) {
				double result = getjczq_5x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 5);
			} else if (guoguan.contains("6串15")) {// 15个2关
				double result = getjczq_2x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 2, 4);
				money += result * beishu;
			} else if (guoguan.contains("6串20")) {// 20个三关
				double result = getjczq_3x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 3, 3);
				money += result * beishu;
			} else if (guoguan.contains("6串22")) {// 15个四关+6个五关+1个六关
				double result = getjczq_4x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 5;
				int beishu_4x1 = n * (n + 1) / 2;
				money += result * beishu_4x1;
			} else if (guoguan.contains("6串35")) {// 15个两关+20个三关
				money += getjczq_jiangjin("min", "6串15", list_jiangjin);
			} else if (guoguan.contains("6串42")) {// 20个三关+15个四关+6个五关+1个六关
				money += getjczq_jiangjin("min", "6串20", list_jiangjin);
			} else if (guoguan.contains("6串50")) {// 15个两关+20个三关+15个四关
				money += getjczq_jiangjin("min", "6串35", list_jiangjin);
			} else if (guoguan.contains("6串57")) {// 15个两关+20个三关+15个四关+6个五关+1个六关
				money += getjczq_jiangjin("min", "6串15", list_jiangjin);
			} else if (guoguan.contains("7串7")) {// 7个六关
				double result = getjczq_6x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 6);
			} else if (guoguan.contains("7串8")) {// 7个六关+1个七关
				money += getjczq_jiangjin("min", "7串7", list_jiangjin);
			} else if (guoguan.contains("7串21")) {// 21个五关
				double result = getjczq_5x1("min", list_jiangjin);
				int n = list_jiangjin.size() - 6;
				int beishu = n * (n + 1) / 2;
				money += result * beishu;
			} else if (guoguan.contains("7串35")) {// 35个四关
				double result = getjczq_4x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 4, 3);
				money += result * beishu;
			} else if (guoguan.contains("7串120")) {// 21个两关+35个三关+35个四关+21个五关+7个六关+1个七关
				double result = getjczq_2x1("min", list_jiangjin);
				int beishu_2x1 = NumberTools.C_m_n(list_jiangjin.size() - 2, 5);
				money += result * beishu_2x1;
			} else if (guoguan.contains("8串8")) {// 8个七关
				double result = getjczq_7x1("min", list_jiangjin);
				money += result * (list_jiangjin.size() - 7);
			} else if (guoguan.contains("8串9")) {// 8个七关+1个8关
				money += getjczq_jiangjin("min", "8串8", list_jiangjin);
			} else if (guoguan.contains("8串28")) {// 28个六关
				double result = getjczq_6x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 6, 2);
				money += result * beishu;
			} else if (guoguan.contains("8串56")) {// 56个五关
				double result = getjczq_5x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 5, 3);
				money += result * beishu;
			} else if (guoguan.contains("8串70")) {// 70个四关
				double result = getjczq_4x1("min", list_jiangjin);
				int beishu = NumberTools.C_m_n(list_jiangjin.size() - 4, 4);
				money += result * beishu;
			} else if (guoguan.contains("8串247")) {// 28个两关+56个三关+70个四关+56个五关+28个六关+8个七关+1个八关
				double result = getjczq_2x1("min", list_jiangjin);
				int beishu_2x1 = NumberTools.C_m_n(list_jiangjin.size() - 2, 6);
				money += result * beishu_2x1;
			}
		}
		DecimalFormat df = new DecimalFormat("########0.00");
		String result = df.format(money);
		double jiangjin = Double.parseDouble(result);
		return jiangjin;
	}

	/**
	 * 竞彩足球/篮球的单关奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_danguan(String mark, List<Double> list_jiangjin) {
		double money_danguanjc = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size(); i++) {
				money_danguanjc += list_jiangjin.get(i);
			}
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			if (list_jiangjin.size() > 0)
				money_danguanjc = list_jiangjin.get(0);
		}
		money_danguanjc = money_danguanjc * 2;
		return money_danguanjc;
	}

	/**
	 * 北单的单关奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_danguan(String mark, List<Double> list_jiangjin) {
		double moneybd = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size(); i++) {
				moneybd += list_jiangjin.get(i);
			}
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			if (list_jiangjin.size() > 0)
				moneybd = list_jiangjin.get(0);
		}
		moneybd = moneybd * 2 * 0.65;
		return moneybd;
	}

	/**
	 * 竞彩足球/篮球的2串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_2x1(String mark, List<Double> list_jiangjin) {
		double money2x1 = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 1; i++) {
				for (int j = i + 1; j < list_jiangjin.size(); j++) {
					money2x1 += (list_jiangjin.get(j)) * (list_jiangjin.get(i));
				}
			}
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			if (list_jiangjin.size() > 1)
				money2x1 = list_jiangjin.get(0) * list_jiangjin.get(1);
		}
		money2x1 = money2x1 * 2;
		return money2x1;
	}

	/**
	 * 北单的2串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_2x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 1; i++) {
				for (int j = i + 1; j < list_jiangjin.size(); j++) {
					money += (list_jiangjin.get(j)) * (list_jiangjin.get(i));
				}
			}
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			if (list_jiangjin.size() > 1)
				money = list_jiangjin.get(0) * list_jiangjin.get(1);
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的3串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_3x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 2; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 1; j++) {
					for (int k = j + 1; k < list_jiangjin.size(); k++) {
						money += list_jiangjin.get(j) * list_jiangjin.get(i)
								* list_jiangjin.get(k);
					}
				}
			}
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 3) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的3串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_3x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 2; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 1; j++) {
					for (int k = j + 1; k < list_jiangjin.size(); k++) {
						money += list_jiangjin.get(j) * list_jiangjin.get(i)
								* list_jiangjin.get(k);
					}
				}
			}
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 3) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的4串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_4x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 3; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 2; j++) {
					for (int k = j + 1; k < list_jiangjin.size() - 1; k++) {
						for (int a = k + 1; a < list_jiangjin.size(); a++) {
							money += list_jiangjin.get(j)
									* list_jiangjin.get(i)
									* list_jiangjin.get(k)
									* list_jiangjin.get(a);
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 4) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的4串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_4x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 3; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 2; j++) {
					for (int k = j + 1; k < list_jiangjin.size() - 1; k++) {
						for (int a = k + 1; a < list_jiangjin.size(); a++) {
							money += list_jiangjin.get(j)
									* list_jiangjin.get(i)
									* list_jiangjin.get(k)
									* list_jiangjin.get(a);
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 4) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的5串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_5x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 4; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 3; j++) {
					for (int k = j + 1; k < list_jiangjin.size() - 2; k++) {
						for (int a = k + 1; a < list_jiangjin.size() - 1; a++) {
							for (int b = a + 1; b < list_jiangjin.size(); b++) {
								money += list_jiangjin.get(j)
										* list_jiangjin.get(i)
										* list_jiangjin.get(k)
										* list_jiangjin.get(a)
										* list_jiangjin.get(b);
							}
						}
					}
				}
			}
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);

			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 5) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的5串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_5x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 4; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 3; j++) {
					for (int k = j + 1; k < list_jiangjin.size() - 2; k++) {
						for (int a = k + 1; a < list_jiangjin.size() - 1; a++) {
							for (int b = a + 1; b < list_jiangjin.size(); b++) {
								money += list_jiangjin.get(j)
										* list_jiangjin.get(i)
										* list_jiangjin.get(k)
										* list_jiangjin.get(a)
										* list_jiangjin.get(b);
							}
						}
					}
				}
			}
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 5) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的6串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_6x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 5; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 4; j++) {
					for (int k = j + 1; k < list_jiangjin.size() - 3; k++) {
						for (int a = k + 1; a < list_jiangjin.size() - 2; a++) {
							for (int b = a + 1; b < list_jiangjin.size() - 1; b++) {
								for (int c = b + 1; c < list_jiangjin.size(); c++) {
									money += list_jiangjin.get(j)
											* list_jiangjin.get(i)
											* list_jiangjin.get(k)
											* list_jiangjin.get(a)
											* list_jiangjin.get(b)
											* list_jiangjin.get(c);
									;
								}
							}
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 6) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;

	}

	/**
	 * 北单的6串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_6x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 5; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 4; j++) {
					for (int k = j + 1; k < list_jiangjin.size() - 3; k++) {
						for (int a = k + 1; a < list_jiangjin.size() - 2; a++) {
							for (int b = a + 1; b < list_jiangjin.size() - 1; b++) {
								for (int c = b + 1; c < list_jiangjin.size(); c++) {
									money += list_jiangjin.get(j)
											* list_jiangjin.get(i)
											* list_jiangjin.get(k)
											* list_jiangjin.get(a)
											* list_jiangjin.get(b)
											* list_jiangjin.get(c);
									;
								}
							}
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);

			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 6) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;

	}

	/**
	 * 竞彩足球/篮球的7串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_7x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 6; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 5; j++) {
					for (int k = j + 1; k < list_jiangjin.size() - 4; k++) {
						for (int a = k + 1; a < list_jiangjin.size() - 3; a++) {
							for (int b = a + 1; b < list_jiangjin.size() - 2; b++) {
								for (int c = b + 1; c < list_jiangjin.size() - 1; c++) {
									for (int d = c + 1; d < list_jiangjin
											.size(); d++) {

										money += list_jiangjin.get(j)
												* list_jiangjin.get(i)
												* list_jiangjin.get(k)
												* list_jiangjin.get(a)
												* list_jiangjin.get(b)
												* list_jiangjin.get(c)
												* list_jiangjin.get(d);
									}
								}
							}
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 7) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的7串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_7x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int i = 0; i < list_jiangjin.size() - 6; i++) {
				for (int j = i + 1; j < list_jiangjin.size() - 5; j++) {
					for (int k = j + 1; k < list_jiangjin.size() - 4; k++) {
						for (int a = k + 1; a < list_jiangjin.size() - 3; a++) {
							for (int b = a + 1; b < list_jiangjin.size() - 2; b++) {
								for (int c = b + 1; c < list_jiangjin.size() - 1; c++) {
									for (int d = c + 1; d < list_jiangjin
											.size(); d++) {

										money += list_jiangjin.get(j)
												* list_jiangjin.get(i)
												* list_jiangjin.get(k)
												* list_jiangjin.get(a)
												* list_jiangjin.get(b)
												* list_jiangjin.get(c)
												* list_jiangjin.get(d);
									}
								}
							}
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 7) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;

	}

	/**
	 * 竞彩足球/篮球的8串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_8x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x = 0; x < list_jiangjin.size() - 7; x++) {
				for (int i = x + 1; i < list_jiangjin.size() - 6; i++) {
					for (int j = i + 1; j < list_jiangjin.size() - 5; j++) {
						for (int k = j + 1; k < list_jiangjin.size() - 4; k++) {
							for (int a = k + 1; a < list_jiangjin.size() - 3; a++) {
								for (int b = a + 1; b < list_jiangjin.size() - 2; b++) {
									for (int c = b + 1; c < list_jiangjin
											.size() - 1; c++) {
										for (int d = c + 1; d < list_jiangjin
												.size(); d++) {

											money += list_jiangjin.get(j)
													* list_jiangjin.get(i)
													* list_jiangjin.get(k)
													* list_jiangjin.get(a)
													* list_jiangjin.get(b)
													* list_jiangjin.get(c)
													* list_jiangjin.get(d)
													* list_jiangjin.get(x);
										}
									}
								}
							}
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 8) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的8串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_8x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x = 0; x < list_jiangjin.size() - 7; x++) {
				for (int i = x + 1; i < list_jiangjin.size() - 6; i++) {
					for (int j = i + 1; j < list_jiangjin.size() - 5; j++) {
						for (int k = j + 1; k < list_jiangjin.size() - 4; k++) {
							for (int a = k + 1; a < list_jiangjin.size() - 3; a++) {
								for (int b = a + 1; b < list_jiangjin.size() - 2; b++) {
									for (int c = b + 1; c < list_jiangjin
											.size() - 1; c++) {
										for (int d = c + 1; d < list_jiangjin
												.size(); d++) {

											money += list_jiangjin.get(j)
													* list_jiangjin.get(i)
													* list_jiangjin.get(k)
													* list_jiangjin.get(a)
													* list_jiangjin.get(b)
													* list_jiangjin.get(c)
													* list_jiangjin.get(d)
													* list_jiangjin.get(x);
										}
									}
								}
							}
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 8) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的9串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_9x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x = 0; x < list_jiangjin.size() - 8; x++) {
				for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
					for (int j = i + 1; j < list_jiangjin.size() - 6; j++) {
						for (int k = j + 1; k < list_jiangjin.size() - 5; k++) {
							for (int a = k + 1; a < list_jiangjin.size() - 4; a++) {
								for (int b = a + 1; b < list_jiangjin.size() - 3; b++) {
									for (int c = b + 1; c < list_jiangjin
											.size() - 2; c++) {
										for (int d = c + 1; d < list_jiangjin
												.size() - 1; d++) {
											for (int e = d + 1; e < list_jiangjin
													.size(); e++) {

												money += list_jiangjin.get(j)
														* list_jiangjin.get(i)
														* list_jiangjin.get(k)
														* list_jiangjin.get(a)
														* list_jiangjin.get(b)
														* list_jiangjin.get(c)
														* list_jiangjin.get(d)
														* list_jiangjin.get(x)
														* list_jiangjin.get(e);
											}
										}
									}
								}
							}
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 9) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的9串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_9x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x = 0; x < list_jiangjin.size() - 8; x++) {
				for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
					for (int j = i + 1; j < list_jiangjin.size() - 6; j++) {
						for (int k = j + 1; k < list_jiangjin.size() - 5; k++) {
							for (int a = k + 1; a < list_jiangjin.size() - 4; a++) {
								for (int b = a + 1; b < list_jiangjin.size() - 3; b++) {
									for (int c = b + 1; c < list_jiangjin
											.size() - 2; c++) {
										for (int d = c + 1; d < list_jiangjin
												.size() - 1; d++) {
											for (int e = d + 1; e < list_jiangjin
													.size(); e++) {

												money += list_jiangjin.get(j)
														* list_jiangjin.get(i)
														* list_jiangjin.get(k)
														* list_jiangjin.get(a)
														* list_jiangjin.get(b)
														* list_jiangjin.get(c)
														* list_jiangjin.get(d)
														* list_jiangjin.get(x)
														* list_jiangjin.get(e);
											}
										}
									}
								}
							}
						}
					}
				}
			}

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 9) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的10串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_10x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x1 = 0; x1 < list_jiangjin.size() - 9; x1++) {
				for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
					for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
						for (int j = i + 1; j < list_jiangjin.size() - 6; j++) {
							for (int k = j + 1; k < list_jiangjin.size() - 5; k++) {
								for (int a = k + 1; a < list_jiangjin.size() - 4; a++) {
									for (int b = a + 1; b < list_jiangjin
											.size() - 3; b++) {
										for (int c = b + 1; c < list_jiangjin
												.size() - 2; c++) {
											for (int d = c + 1; d < list_jiangjin
													.size() - 1; d++) {
												for (int e = d + 1; e < list_jiangjin
														.size(); e++) {

													money += list_jiangjin
															.get(j)
															* list_jiangjin
																	.get(i)
															* list_jiangjin
																	.get(k)
															* list_jiangjin
																	.get(a)
															* list_jiangjin
																	.get(b)
															* list_jiangjin
																	.get(c)
															* list_jiangjin
																	.get(d)
															* list_jiangjin
																	.get(x)
															* list_jiangjin
																	.get(e)
															* list_jiangjin
																	.get(x1);

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

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 10) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的10串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_10x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x1 = 0; x1 < list_jiangjin.size() - 9; x1++) {
				for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
					for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
						for (int j = i + 1; j < list_jiangjin.size() - 6; j++) {
							for (int k = j + 1; k < list_jiangjin.size() - 5; k++) {
								for (int a = k + 1; a < list_jiangjin.size() - 4; a++) {
									for (int b = a + 1; b < list_jiangjin
											.size() - 3; b++) {
										for (int c = b + 1; c < list_jiangjin
												.size() - 2; c++) {
											for (int d = c + 1; d < list_jiangjin
													.size() - 1; d++) {
												for (int e = d + 1; e < list_jiangjin
														.size(); e++) {

													money += list_jiangjin
															.get(j)
															* list_jiangjin
																	.get(i)
															* list_jiangjin
																	.get(k)
															* list_jiangjin
																	.get(a)
															* list_jiangjin
																	.get(b)
															* list_jiangjin
																	.get(c)
															* list_jiangjin
																	.get(d)
															* list_jiangjin
																	.get(x)
															* list_jiangjin
																	.get(e)
															* list_jiangjin
																	.get(x1);

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

		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 10) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的11串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_11x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x2 = 0; x2 < list_jiangjin.size() - 10; x2++) {
				for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
					for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
						for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
							for (int j = i + 1; j < list_jiangjin.size() - 6; j++) {
								for (int k = j + 1; k < list_jiangjin.size() - 5; k++) {
									for (int a = k + 1; a < list_jiangjin
											.size() - 4; a++) {
										for (int b = a + 1; b < list_jiangjin
												.size() - 3; b++) {
											for (int c = b + 1; c < list_jiangjin
													.size() - 2; c++) {
												for (int d = c + 1; d < list_jiangjin
														.size() - 1; d++) {
													for (int e = d + 1; e < list_jiangjin
															.size(); e++) {

														money += list_jiangjin
																.get(j)
																* list_jiangjin
																		.get(i)
																* list_jiangjin
																		.get(k)
																* list_jiangjin
																		.get(a)
																* list_jiangjin
																		.get(b)
																* list_jiangjin
																		.get(c)
																* list_jiangjin
																		.get(d)
																* list_jiangjin
																		.get(x)
																* list_jiangjin
																		.get(e)
																* list_jiangjin
																		.get(x1)
																* list_jiangjin
																		.get(x2);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 11) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的11串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_11x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x2 = 0; x2 < list_jiangjin.size() - 10; x2++) {
				for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
					for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
						for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
							for (int j = i + 1; j < list_jiangjin.size() - 6; j++) {
								for (int k = j + 1; k < list_jiangjin.size() - 5; k++) {
									for (int a = k + 1; a < list_jiangjin
											.size() - 4; a++) {
										for (int b = a + 1; b < list_jiangjin
												.size() - 3; b++) {
											for (int c = b + 1; c < list_jiangjin
													.size() - 2; c++) {
												for (int d = c + 1; d < list_jiangjin
														.size() - 1; d++) {
													for (int e = d + 1; e < list_jiangjin
															.size(); e++) {

														money += list_jiangjin
																.get(j)
																* list_jiangjin
																		.get(i)
																* list_jiangjin
																		.get(k)
																* list_jiangjin
																		.get(a)
																* list_jiangjin
																		.get(b)
																* list_jiangjin
																		.get(c)
																* list_jiangjin
																		.get(d)
																* list_jiangjin
																		.get(x)
																* list_jiangjin
																		.get(e)
																* list_jiangjin
																		.get(x1)
																* list_jiangjin
																		.get(x2);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 11) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的12串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_12x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x3 = 0; x3 < list_jiangjin.size() - 11; x3++) {
				for (int x2 = x3 + 1; x2 < list_jiangjin.size() - 10; x2++) {
					for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
						for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
							for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
								for (int j = i + 1; j < list_jiangjin.size() - 6; j++) {
									for (int k = j + 1; k < list_jiangjin
											.size() - 5; k++) {
										for (int a = k + 1; a < list_jiangjin
												.size() - 4; a++) {
											for (int b = a + 1; b < list_jiangjin
													.size() - 3; b++) {
												for (int c = b + 1; c < list_jiangjin
														.size() - 2; c++) {
													for (int d = c + 1; d < list_jiangjin
															.size() - 1; d++) {
														for (int e = d + 1; e < list_jiangjin
																.size(); e++) {

															money += list_jiangjin
																	.get(j)
																	* list_jiangjin
																			.get(i)
																	* list_jiangjin
																			.get(k)
																	* list_jiangjin
																			.get(a)
																	* list_jiangjin
																			.get(b)
																	* list_jiangjin
																			.get(c)
																	* list_jiangjin
																			.get(d)
																	* list_jiangjin
																			.get(x)
																	* list_jiangjin
																			.get(e)
																	* list_jiangjin
																			.get(x1)
																	* list_jiangjin
																			.get(x2)
																	* list_jiangjin
																			.get(x3);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 12) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的12串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_12x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x3 = 0; x3 < list_jiangjin.size() - 11; x3++) {
				for (int x2 = x3 + 1; x2 < list_jiangjin.size() - 10; x2++) {
					for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
						for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
							for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
								for (int j = i + 1; j < list_jiangjin.size() - 6; j++) {
									for (int k = j + 1; k < list_jiangjin
											.size() - 5; k++) {
										for (int a = k + 1; a < list_jiangjin
												.size() - 4; a++) {
											for (int b = a + 1; b < list_jiangjin
													.size() - 3; b++) {
												for (int c = b + 1; c < list_jiangjin
														.size() - 2; c++) {
													for (int d = c + 1; d < list_jiangjin
															.size() - 1; d++) {
														for (int e = d + 1; e < list_jiangjin
																.size(); e++) {

															money += list_jiangjin
																	.get(j)
																	* list_jiangjin
																			.get(i)
																	* list_jiangjin
																			.get(k)
																	* list_jiangjin
																			.get(a)
																	* list_jiangjin
																			.get(b)
																	* list_jiangjin
																			.get(c)
																	* list_jiangjin
																			.get(d)
																	* list_jiangjin
																			.get(x)
																	* list_jiangjin
																			.get(e)
																	* list_jiangjin
																			.get(x1)
																	* list_jiangjin
																			.get(x2)
																	* list_jiangjin
																			.get(x3);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 12) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的13串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_13x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x4 = 0; x4 < list_jiangjin.size() - 12; x4++) {
				for (int x3 = x4 + 1; x3 < list_jiangjin.size() - 11; x3++) {
					for (int x2 = x3 + 1; x2 < list_jiangjin.size() - 10; x2++) {
						for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
							for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
								for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
									for (int j = i + 1; j < list_jiangjin
											.size() - 6; j++) {
										for (int k = j + 1; k < list_jiangjin
												.size() - 5; k++) {
											for (int a = k + 1; a < list_jiangjin
													.size() - 4; a++) {
												for (int b = a + 1; b < list_jiangjin
														.size() - 3; b++) {
													for (int c = b + 1; c < list_jiangjin
															.size() - 2; c++) {
														for (int d = c + 1; d < list_jiangjin
																.size() - 1; d++) {
															for (int e = d + 1; e < list_jiangjin
																	.size(); e++) {

																money += list_jiangjin
																		.get(j)
																		* list_jiangjin
																				.get(i)
																		* list_jiangjin
																				.get(k)
																		* list_jiangjin
																				.get(a)
																		* list_jiangjin
																				.get(b)
																		* list_jiangjin
																				.get(c)
																		* list_jiangjin
																				.get(d)
																		* list_jiangjin
																				.get(x)
																		* list_jiangjin
																				.get(e)
																		* list_jiangjin
																				.get(x1)
																		* list_jiangjin
																				.get(x2)
																		* list_jiangjin
																				.get(x3)
																		* list_jiangjin
																				.get(x4);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 13) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;

		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的13串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_13x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x4 = 0; x4 < list_jiangjin.size() - 12; x4++) {
				for (int x3 = x4 + 1; x3 < list_jiangjin.size() - 11; x3++) {
					for (int x2 = x3 + 1; x2 < list_jiangjin.size() - 10; x2++) {
						for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
							for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
								for (int i = x + 1; i < list_jiangjin.size() - 7; i++) {
									for (int j = i + 1; j < list_jiangjin
											.size() - 6; j++) {
										for (int k = j + 1; k < list_jiangjin
												.size() - 5; k++) {
											for (int a = k + 1; a < list_jiangjin
													.size() - 4; a++) {
												for (int b = a + 1; b < list_jiangjin
														.size() - 3; b++) {
													for (int c = b + 1; c < list_jiangjin
															.size() - 2; c++) {
														for (int d = c + 1; d < list_jiangjin
																.size() - 1; d++) {
															for (int e = d + 1; e < list_jiangjin
																	.size(); e++) {

																money += list_jiangjin
																		.get(j)
																		* list_jiangjin
																				.get(i)
																		* list_jiangjin
																				.get(k)
																		* list_jiangjin
																				.get(a)
																		* list_jiangjin
																				.get(b)
																		* list_jiangjin
																				.get(c)
																		* list_jiangjin
																				.get(d)
																		* list_jiangjin
																				.get(x)
																		* list_jiangjin
																				.get(e)
																		* list_jiangjin
																				.get(x1)
																		* list_jiangjin
																				.get(x2)
																		* list_jiangjin
																				.get(x3)
																		* list_jiangjin
																				.get(x4);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 13) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的14串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_14x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x5 = 0; x5 < list_jiangjin.size() - 13; x5++) {
				for (int x4 = x5 + 1; x4 < list_jiangjin.size() - 12; x4++) {
					for (int x3 = x4 + 1; x3 < list_jiangjin.size() - 11; x3++) {
						for (int x2 = x3 + 1; x2 < list_jiangjin.size() - 10; x2++) {
							for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
								for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
									for (int i = x + 1; i < list_jiangjin
											.size() - 7; i++) {
										for (int j = i + 1; j < list_jiangjin
												.size() - 6; j++) {
											for (int k = j + 1; k < list_jiangjin
													.size() - 5; k++) {
												for (int a = k + 1; a < list_jiangjin
														.size() - 4; a++) {
													for (int b = a + 1; b < list_jiangjin
															.size() - 3; b++) {
														for (int c = b + 1; c < list_jiangjin
																.size() - 2; c++) {
															for (int d = c + 1; d < list_jiangjin
																	.size() - 1; d++) {
																for (int e = d + 1; e < list_jiangjin
																		.size(); e++) {

																	money += list_jiangjin
																			.get(j)
																			* list_jiangjin
																					.get(i)
																			* list_jiangjin
																					.get(k)
																			* list_jiangjin
																					.get(a)
																			* list_jiangjin
																					.get(b)
																			* list_jiangjin
																					.get(c)
																			* list_jiangjin
																					.get(d)
																			* list_jiangjin
																					.get(x)
																			* list_jiangjin
																					.get(e)
																			* list_jiangjin
																					.get(x1)
																			* list_jiangjin
																					.get(x2)
																			* list_jiangjin
																					.get(x3)
																			* list_jiangjin
																					.get(x4)
																			* list_jiangjin
																					.get(x5);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 14) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;

		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的14串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_14x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x5 = 0; x5 < list_jiangjin.size() - 13; x5++) {
				for (int x4 = x5 + 1; x4 < list_jiangjin.size() - 12; x4++) {
					for (int x3 = x4 + 1; x3 < list_jiangjin.size() - 11; x3++) {
						for (int x2 = x3 + 1; x2 < list_jiangjin.size() - 10; x2++) {
							for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
								for (int x = x1 + 1; x < list_jiangjin.size() - 8; x++) {
									for (int i = x + 1; i < list_jiangjin
											.size() - 7; i++) {
										for (int j = i + 1; j < list_jiangjin
												.size() - 6; j++) {
											for (int k = j + 1; k < list_jiangjin
													.size() - 5; k++) {
												for (int a = k + 1; a < list_jiangjin
														.size() - 4; a++) {
													for (int b = a + 1; b < list_jiangjin
															.size() - 3; b++) {
														for (int c = b + 1; c < list_jiangjin
																.size() - 2; c++) {
															for (int d = c + 1; d < list_jiangjin
																	.size() - 1; d++) {
																for (int e = d + 1; e < list_jiangjin
																		.size(); e++) {

																	money += list_jiangjin
																			.get(j)
																			* list_jiangjin
																					.get(i)
																			* list_jiangjin
																					.get(k)
																			* list_jiangjin
																					.get(a)
																			* list_jiangjin
																					.get(b)
																			* list_jiangjin
																					.get(c)
																			* list_jiangjin
																					.get(d)
																			* list_jiangjin
																					.get(x)
																			* list_jiangjin
																					.get(e)
																			* list_jiangjin
																					.get(x1)
																			* list_jiangjin
																					.get(x2)
																			* list_jiangjin
																					.get(x3)
																			* list_jiangjin
																					.get(x4)
																			* list_jiangjin
																					.get(x5);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 14) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;

		}
		money = money * 2 * 0.65;
		return money;
	}

	/**
	 * 竞彩足球/篮球的15串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getjczq_15x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x6 = 0; x6 < list_jiangjin.size() - 14; x6++) {
				for (int x5 = x6 + 1; x5 < list_jiangjin.size() - 13; x5++) {
					for (int x4 = x5 + 1; x4 < list_jiangjin.size() - 12; x4++) {
						for (int x3 = x4 + 1; x3 < list_jiangjin.size() - 11; x3++) {
							for (int x2 = x3 + 1; x2 < list_jiangjin.size() - 10; x2++) {
								for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
									for (int x = x1 + 1; x < list_jiangjin
											.size() - 8; x++) {
										for (int i = x + 1; i < list_jiangjin
												.size() - 7; i++) {
											for (int j = i + 1; j < list_jiangjin
													.size() - 6; j++) {
												for (int k = j + 1; k < list_jiangjin
														.size() - 5; k++) {
													for (int a = k + 1; a < list_jiangjin
															.size() - 4; a++) {
														for (int b = a + 1; b < list_jiangjin
																.size() - 3; b++) {
															for (int c = b + 1; c < list_jiangjin
																	.size() - 2; c++) {
																for (int d = c + 1; d < list_jiangjin
																		.size() - 1; d++) {
																	for (int e = d + 1; e < list_jiangjin
																			.size(); e++) {

																		money += list_jiangjin
																				.get(j)
																				* list_jiangjin
																						.get(i)
																				* list_jiangjin
																						.get(k)
																				* list_jiangjin
																						.get(a)
																				* list_jiangjin
																						.get(b)
																				* list_jiangjin
																						.get(c)
																				* list_jiangjin
																						.get(d)
																				* list_jiangjin
																						.get(x)
																				* list_jiangjin
																						.get(e)
																				* list_jiangjin
																						.get(x1)
																				* list_jiangjin
																						.get(x2)
																				* list_jiangjin
																						.get(x3)
																				* list_jiangjin
																						.get(x4)
																				* list_jiangjin
																						.get(x5)
																				* list_jiangjin
																						.get(x6);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 15) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2;
		return money;
	}

	/**
	 * 北单的15串1的奖金算法
	 * 
	 * @param mark
	 *            ：最大值还是最小值参数
	 * @param list_jiangjin
	 *            ：赔率集合
	 * @return
	 */
	public static double getbjdc_15x1(String mark, List<Double> list_jiangjin) {
		double money = 0;
		if (mark.equals("max")) {
			for (int x6 = 0; x6 < list_jiangjin.size() - 14; x6++) {
				for (int x5 = x6 + 1; x5 < list_jiangjin.size() - 13; x5++) {
					for (int x4 = x5 + 1; x4 < list_jiangjin.size() - 12; x4++) {
						for (int x3 = x4 + 1; x3 < list_jiangjin.size() - 11; x3++) {
							for (int x2 = x3 + 1; x2 < list_jiangjin.size() - 10; x2++) {
								for (int x1 = x2 + 1; x1 < list_jiangjin.size() - 9; x1++) {
									for (int x = x1 + 1; x < list_jiangjin
											.size() - 8; x++) {
										for (int i = x + 1; i < list_jiangjin
												.size() - 7; i++) {
											for (int j = i + 1; j < list_jiangjin
													.size() - 6; j++) {
												for (int k = j + 1; k < list_jiangjin
														.size() - 5; k++) {
													for (int a = k + 1; a < list_jiangjin
															.size() - 4; a++) {
														for (int b = a + 1; b < list_jiangjin
																.size() - 3; b++) {
															for (int c = b + 1; c < list_jiangjin
																	.size() - 2; c++) {
																for (int d = c + 1; d < list_jiangjin
																		.size() - 1; d++) {
																	for (int e = d + 1; e < list_jiangjin
																			.size(); e++) {

																		money += list_jiangjin
																				.get(j)
																				* list_jiangjin
																						.get(i)
																				* list_jiangjin
																						.get(k)
																				* list_jiangjin
																						.get(a)
																				* list_jiangjin
																						.get(b)
																				* list_jiangjin
																						.get(c)
																				* list_jiangjin
																						.get(d)
																				* list_jiangjin
																						.get(x)
																				* list_jiangjin
																						.get(e)
																				* list_jiangjin
																						.get(x1)
																				* list_jiangjin
																						.get(x2)
																				* list_jiangjin
																						.get(x3)
																				* list_jiangjin
																						.get(x4)
																				* list_jiangjin
																						.get(x5)
																				* list_jiangjin
																						.get(x6);
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
		} else if (mark.equals("min")) {
			Collections.sort(list_jiangjin);
			double moneymin = 1;
			for (int i = 0; i < list_jiangjin.size(); i++) {
				if (i < 15) {
					moneymin *= list_jiangjin.get(i);
				}
			}
			money = moneymin;
		}
		money = money * 2 * 0.65;
		return money;
	}
}
