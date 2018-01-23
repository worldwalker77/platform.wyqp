package cn.worldwalker.game.wyqp.common.domain.jh;

import java.util.ArrayList;
import java.util.List;

import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;

public class JhPlayerInfo extends BasePlayerInfo{
	private Integer stakeTimes = 0;
	/**当前跟注分数*/
	private Integer curStakeScore = 0;
	/**当前局总的跟注分数*/
	private Integer curTotalStakeScore = 0;
	/**当前玩家已经押注分数列表，断线重连时需要根据这个展示桌面上的压分*/
	private List<Integer> stakeScoreList = new ArrayList<Integer>();
	
	public Integer getCurStakeScore() {
		return curStakeScore;
	}
	public void setCurStakeScore(Integer curStakeScore) {
		this.curStakeScore = curStakeScore;
	}
	public Integer getCurTotalStakeScore() {
		return curTotalStakeScore;
	}
	public void setCurTotalStakeScore(Integer curTotalStakeScore) {
		this.curTotalStakeScore = curTotalStakeScore;
	}
	public List<Integer> getStakeScoreList() {
		return stakeScoreList;
	}
	public void setStakeScoreList(List<Integer> stakeScoreList) {
		this.stakeScoreList = stakeScoreList;
	}
	public Integer getStakeTimes() {
		return stakeTimes;
	}
	public void setStakeTimes(Integer stakeTimes) {
		this.stakeTimes = stakeTimes;
	}

}
