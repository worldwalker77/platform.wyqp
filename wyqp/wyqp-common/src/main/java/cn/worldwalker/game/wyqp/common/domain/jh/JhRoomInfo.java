package cn.worldwalker.game.wyqp.common.domain.jh;

import java.util.ArrayList;
import java.util.List;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;

public class JhRoomInfo  extends BaseRoomInfo{
	
	/**底注*/
	private Integer stakeButtom;
	/**押注上限*/
	private Integer stakeLimit = 10;
	/**押注次数上限*/
	private Integer stakeTimesLimit = 6;
	/**前一个说话玩家的id*/
	private Integer prePlayerId;
	/**前一个说话玩家跟注时的状态,是看牌还是未看牌，主要是为了校验当前玩家的跟注是否符合要求*/
	private Integer prePlayerStatus;
	/**前一个说话玩家的跟注分数*/
	private Integer prePlayerStakeScore;
	/**当前局跟注轮数*/
	private Integer totalStakeTimes;
	
	private List<JhPlayerInfo> playerList = new ArrayList<JhPlayerInfo>();

	public Integer getStakeButtom() {
		return stakeButtom;
	}

	public void setStakeButtom(Integer stakeButtom) {
		this.stakeButtom = stakeButtom;
	}

	public Integer getStakeLimit() {
		return stakeLimit;
	}

	public void setStakeLimit(Integer stakeLimit) {
		this.stakeLimit = stakeLimit;
	}

	public Integer getStakeTimesLimit() {
		return stakeTimesLimit;
	}

	public void setStakeTimesLimit(Integer stakeTimesLimit) {
		this.stakeTimesLimit = stakeTimesLimit;
	}


	public Integer getPrePlayerId() {
		return prePlayerId;
	}

	public void setPrePlayerId(Integer prePlayerId) {
		this.prePlayerId = prePlayerId;
	}

	public Integer getPrePlayerStatus() {
		return prePlayerStatus;
	}

	public void setPrePlayerStatus(Integer prePlayerStatus) {
		this.prePlayerStatus = prePlayerStatus;
	}

	public Integer getPrePlayerStakeScore() {
		return prePlayerStakeScore;
	}

	public void setPrePlayerStakeScore(Integer prePlayerStakeScore) {
		this.prePlayerStakeScore = prePlayerStakeScore;
	}

	public Integer getTotalStakeTimes() {
		return totalStakeTimes;
	}

	public void setTotalStakeTimes(Integer totalStakeTimes) {
		this.totalStakeTimes = totalStakeTimes;
	}

	public List<JhPlayerInfo> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<JhPlayerInfo> playerList) {
		this.playerList = playerList;
	}
	
}
