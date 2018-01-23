package cn.worldwalker.game.wyqp.common.domain.nn;

import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;

public class NnMsg extends BaseMsg{
	
	private Integer roomBankerType;
	/**是否抢庄 3不抢 4 抢*/
	private Integer isRobBanker;
	
	private Integer stakeScore;
	
	private Integer multipleLimit;
	
	/**底分类型 1:1/2/3/4 2:2/4/8/10 3:3/6/9/12*/
	private Integer buttomScoreType;
	
	/**抢庄的时候选择的倍数*/
	private Integer robMultiple;
	
	public Integer getRobMultiple() {
		return robMultiple;
	}

	public void setRobMultiple(Integer robMultiple) {
		this.robMultiple = robMultiple;
	}

	public Integer getButtomScoreType() {
		return buttomScoreType;
	}

	public void setButtomScoreType(Integer buttomScoreType) {
		this.buttomScoreType = buttomScoreType;
	}

	public Integer getRoomBankerType() {
		return roomBankerType;
	}

	public void setRoomBankerType(Integer roomBankerType) {
		this.roomBankerType = roomBankerType;
	}

	public Integer getIsRobBanker() {
		return isRobBanker;
	}

	public void setIsRobBanker(Integer isRobBanker) {
		this.isRobBanker = isRobBanker;
	}

	public Integer getStakeScore() {
		return stakeScore;
	}

	public void setStakeScore(Integer stakeScore) {
		this.stakeScore = stakeScore;
	}

	public Integer getMultipleLimit() {
		return multipleLimit;
	}

	public void setMultipleLimit(Integer multipleLimit) {
		this.multipleLimit = multipleLimit;
	}
	
}
