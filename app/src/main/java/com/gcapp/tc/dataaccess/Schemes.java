package com.gcapp.tc.dataaccess;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 方案实体类
 * 
 * @author lenovo
 * 
 */
public class Schemes implements Serializable {

	/** 基本数据 */
	private String id; // 方案ID
	private String schemeNumber; // 订单号
	private double money; // 方案总金额
	private String lotteryID; // 彩种ＩＤ
	private String lotteryName; // 彩种名称
	private int playTypeID; // 玩法ID
	private String playTypeName; // 玩法名称
	private String countNotes;// 玩法注数

	private String lotteryNumber; // 投注内容
	private List<LotteryContent> buyContent; // 投注玩法内容
	private String dateTime; // 方案发起时间
	private int multiple; // 倍数
	private String isuseID; // 奖期ID
	private String winNumber; // 开奖号码
	private String isuseName; // 奖期名称
	private int SerialNumber; // 数据在全部数据中的位置
	private int RecordCount; // 总记录数
	private int FromClient; // 来自哪？ 1表示来自网页，2表示来自手机应用

	private String isOpened;
	private String endTime;//方案截止时间

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIsOpened() {
		return isOpened;
	}

	public void setIsOpened(String isOpened) {
		this.isOpened = isOpened;
	}

	// 合买大厅的方案详情界面新加的字段
	private double totalmoney;// 总金额
	private double selfmoney;// 自购金额
	private double greatmanOdds;// 胜率
	private String playtypeName;// 过关方式
	private int rengou_peoplesum;
	private int rengou_money;
	private String follow_state;////和schemeStatus代表相同的意义

	public double getTotalmoney() {
		return totalmoney;
	}

	public void setSelfmoney(double selfmoney) {
		this.selfmoney = selfmoney;
	}
	
	public double getSelfmoney() {
		return selfmoney;
	}

	public double getGreatmanOdds() {
		return greatmanOdds;
	}

	public void setGreatmanOdds(double greatmanOdds) {
		this.greatmanOdds = greatmanOdds;
	}

	public void setTotalmoney(double totalmoney) {
		this.totalmoney = totalmoney;
	}

	public String getPlaytypeName() {
		return playtypeName;
	}

	public void setPlaytypeName(String playtypeName) {
		this.playtypeName = playtypeName;
	}

	public int getRengou_peoplesum() {
		return rengou_peoplesum;
	}

	public void setRengou_peoplesum(int rengou_peoplesum) {
		this.rengou_peoplesum = rengou_peoplesum;
	}

	public int getRengou_money() {
		return rengou_money;
	}

	public void setRengou_money(int rengou_money) {
		this.rengou_money = rengou_money;
	}

	public String getFollow_state() {
		return follow_state;
	}

	public void setFollow_state(String follow_state) {
		this.follow_state = follow_state;
	}

	/** 合买数据 */
	private int shareMoney; // 每份金额
	private int share; // 每份数
	private int surplusShare; // 剩余份数
	private String initiateUserID; // 发起人ID
	private String initiateName; // 发起人名称
	private String initiateImg; // 发起人头像
	private int level; // 发起人历史战绩
	private double assureMoney; // 保底金额

	private double winMoney;

	public double getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(double winMoney) {
		this.winMoney = winMoney;
	}
	
	public String getInitiateImg() {
		return initiateImg;
	}

	public void setInitiateImg(String initiateImg) {
		this.initiateImg = initiateImg;
	}

	private int assureShare; // 保底份数
	private String title; // 方案标题
	private double schemeBonusScale; // 佣金比
	private double schemeCommission; // 佣金
	private int secrecyLevel; // 保密设置 ( 0 不保密 1 到截止 2 到开奖 3 永远)
	private int schedule; // 进度
	private String description; // 方案描述
	private String myBuyMoney; // 认购金额
	private int myBuyShare; // 认购份数

	/** 状态 **/
	private int quashStatus; // 撒单状态 ( 0未撤单 )
	private double winMoneyNoWithTax; // 中奖金额
	private double rewardMoney; // 加奖
	private String schemeIsOpened; // 是否开奖 (False 表示未开奖，True表示已开奖)
	private String isPurchasing; // 是否代购 (True 代购 False 合买)
	private String buyed; // False 未出票 true 已出票
	private int isChase; // 是否是追号
	private double stopWhenWinMoney; // 中奖金额为多少时 停止追号
	//和follow_state代表相同的意义
	private String schemeStatus;

	/** 追号数据 */
	private int sumChaseCount; // 总共追多少期
	private double SumSchemeMoney; // 追号方案金额
	private boolean isExecuted; // 方案是否执行
	private int chaseTaskID; // 追号任务ID
	private String chaseTaskTime; // 执行追号任务时间
	private int sumCompletedCount; // 执行期数
	private int sumUnCompletedCount; // 未执行期数

	private double detailMoney;// 金额
	private int couponMoney;// 优惠券
	private double handselMoney;// 彩金
	private double shareWinMoney;// 跟单个人奖金

	// 新增九个字段
	// private int id; // 传值id //重复

	private String orderTime; // 日期

	private String orderTitle; // 标题

	private int type; // 类型 1：投注 2：中奖 3：充值 4：提款

	private int schemeId;// 方案表ID

	// 新增字段
	private String oType; // 用户订单类型

	private boolean isPreBet;

	private int isHide;// 方案是否保密

	private String secretMsg;// 保密方案的描述

	public int getIsHide() {
		return isHide;
	}

	public void setIsHide(int isHide) {
		this.isHide = isHide;
	}

	public String getSecretMsg() {
		return secretMsg;
	}

	public void setSecretMsg(String secretMsg) {
		this.secretMsg = secretMsg;
	}

	public String getoType() {
		return oType;
	}

	public void setoType(String oType) {
		this.oType = oType;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	// public int getOrderType() {
	// return orderType;
	// }
	//
	// public void setOrderType(int orderType) {
	// this.orderType = orderType;
	// }

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(int schemeId) {
		this.schemeId = schemeId;
	}

	public String getDetailMoney() {

		DecimalFormat format = new DecimalFormat("#####0.00");
		return format.format(detailMoney);
	}

	public void setDetailMoney(double detailMoney) {
		this.detailMoney = detailMoney;
	}

	public int getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(int couponMoney) {
		this.couponMoney = couponMoney;
	}

	public String getHandselMoney() {

		DecimalFormat format = new DecimalFormat("#####0.00");

		return format.format(handselMoney);
	}

	public void setHandselMoney(double handselMoney) {
		this.handselMoney = handselMoney;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchemeNumber() {
		return schemeNumber;
	}

	public void setSchemeNumber(String schemeNumber) {
		this.schemeNumber = schemeNumber;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getShareMoney() {
		return shareMoney;
	}

	public void setShareMoney(int shareMoney) {
		this.shareMoney = shareMoney;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getSurplusShare() {
		return surplusShare;
	}

	public void setSurplusShare(int surplusShare) {
		this.surplusShare = surplusShare;
	}

	public String getInitiateUserID() {
		return initiateUserID;
	}

	public String getCountNotes() {
		return countNotes;
	}

	public void setCountNotes(String countNotes) {
		this.countNotes = countNotes;
	}

	public void setInitiateUserID(String initiateUserID) {
		this.initiateUserID = initiateUserID;
	}

	public String getInitiateName() {
		return initiateName;
	}

	public void setInitiateName(String initiateName) {
		this.initiateName = initiateName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getAssureMoney() {
		return assureMoney;
	}

	public void setAssureMoney(double assureMoney) {
		this.assureMoney = assureMoney;
	}

	public int getAssureShare() {
		return assureShare;
	}

	public void setAssureShare(int assureShare) {
		this.assureShare = assureShare;
	}

	public int getQuashStatus() {
		return quashStatus;
	}

	public void setQuashStatus(int quashStatus) {
		this.quashStatus = quashStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getShareWinMoney() {
		return shareWinMoney;
	}

	public void setShareWinMoney(double shareWinMoney) {
		this.shareWinMoney = shareWinMoney;
	}
	
	public double getWinMoneyNoWithTax() {
		return winMoneyNoWithTax;
	}

	public void setWinMoneyNoWithTax(double winMoneyNoWithTax) {
		this.winMoneyNoWithTax = winMoneyNoWithTax;
	}
	
	public double getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(double rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	public double getSchemeBonusScale() {
		return schemeBonusScale;
	}

	public void setSchemeBonusScale(double schemeBonusScale) {
		this.schemeBonusScale = schemeBonusScale;
	}
	
	public double getSchemeCommission() {
		return schemeCommission;
	}

	public void setSchemeCommission(double schemeCommission) {
		this.schemeCommission = schemeCommission;
	}

	public int getSecrecyLevel() {
		return secrecyLevel;
	}

	public void setSecrecyLevel(int secrecyLevel) {
		this.secrecyLevel = secrecyLevel;
	}

	public int getSchedule() {
		return schedule;
	}

	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}

	public String getSchemeIsOpened() {
		return schemeIsOpened;
	}

	public void setSchemeIsOpened(String schemeIsOpened) {
		this.schemeIsOpened = schemeIsOpened;
	}

	public String getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(String lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}
	
	public String getIsPurchasing() {
		return isPurchasing;
	}

	public void setIsPurchasing(String isPurchasing) {
		this.isPurchasing = isPurchasing;
	}

	public String getLotteryID() {
		return lotteryID;
	}

	public void setLotteryID(String lotteryID) {
		this.lotteryID = lotteryID;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public int getPlayTypeID() {
		return playTypeID;
	}

	public void setPlayTypeID(int playTypeID) {
		this.playTypeID = playTypeID;
	}

	public String getPlayTypeName() {
		return playTypeName;
	}

	public void setPlayTypeName(String playTypeName) {
		this.playTypeName = playTypeName;
	}

	public String getIsuseID() {
		return isuseID;
	}

	public void setIsuseID(String isuseID) {
		this.isuseID = isuseID;
	}

	public String getIsuseName() {
		return isuseName;
	}

	public void setIsuseName(String isuseName) {
		this.isuseName = isuseName;
	}

	public int getSerialNumber() {
		return SerialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		SerialNumber = serialNumber;
	}

	public int getRecordCount() {
		return RecordCount;
	}

	public void setRecordCount(int recordCount) {
		RecordCount = recordCount;
	}

	public String getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public String getBuyed() {
		return buyed;
	}

	public void setBuyed(String buyed) {
		this.buyed = buyed;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMultiple() {// 倍数
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getIsChase() {
		return isChase;
	}

	public void setIsChase(int isChase) {
		this.isChase = isChase;
	}

	public String getMyBuyMoney() {
		return myBuyMoney;
	}

	public void setMyBuyMoney(String myBuyMoney) {
		this.myBuyMoney = myBuyMoney;
	}

	public int getMyBuyShare() {
		return myBuyShare;
	}

	public void setMyBuyShare(int myBuyShare) {
		this.myBuyShare = myBuyShare;
	}

	public int getFromClient() {
		return FromClient;
	}

	public void setFromClient(int fromClient) {
		FromClient = fromClient;
	}

	public int getSumChaseCount() {
		return sumChaseCount;
	}

	public void setSumChaseCount(int sumChaseCount) {
		this.sumChaseCount = sumChaseCount;
	}

	public double getSumSchemeMoney() {
		return SumSchemeMoney;
	}

	public void setSumSchemeMoney(double sumSchemeMoney) {
		SumSchemeMoney = sumSchemeMoney;
	}

	public boolean isExecuted() {
		return isExecuted;
	}

	public void setExecuted(boolean isExecuted) {
		this.isExecuted = isExecuted;
	}

	public int getChaseTaskID() {
		return chaseTaskID;
	}

	public void setChaseTaskID(int chaseTaskID) {
		this.chaseTaskID = chaseTaskID;
	}

	public String getChaseTaskTime() {
		return chaseTaskTime;
	}

	public void setChaseTaskTime(String chaseTaskTime) {
		this.chaseTaskTime = chaseTaskTime;
	}

	public double getStopWhenWinMoney() {
		return stopWhenWinMoney;
	}

	public void setStopWhenWinMoney(double stopWhenWinMoney) {
		this.stopWhenWinMoney = stopWhenWinMoney;
	}

	public int getSumCompletedCount() {
		return sumCompletedCount;
	}

	public void setSumCompletedCount(int sumCompletedCount) {
		this.sumCompletedCount = sumCompletedCount;
	}

	public int getSumUnCompletedCount() {
		return sumUnCompletedCount;
	}

	public void setSumUnCompletedCount(int sumUnCompletedCount) {
		this.sumUnCompletedCount = sumUnCompletedCount;
	}

	public List<LotteryContent> getBuyContent() {
		return buyContent;
	}

	public void setBuyContent(List<LotteryContent> buyContent) {
		this.buyContent = buyContent;
	}

	public String getSchemeStatus() {
		return schemeStatus;
	}

	public void setSchemeStatus(String schemeStatus) {
		this.schemeStatus = schemeStatus;
	}

	public boolean getIsPreBet() {
		return isPreBet;
	}

	public void setIsPreBet(boolean isPreBet) {
		this.isPreBet = isPreBet;
	}
}
