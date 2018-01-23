package cn.worldwalker.game.wyqp.common.domain.nn;

import java.util.List;

import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.base.Card;

public class NnPlayerInfo extends BasePlayerInfo{
	/**押注分数 1,2,3,4,5*/
	private Integer stakeScore;
	/**玩家抢庄时间*/
	private Long robBankerTime;
	/**有牛的情况下，返回的三张和为10的倍数的三张牌*/
	private List<Card> nnCardList;
	/**抢庄的时候先给的四张牌*/
	private List<Card> robFourCardList;
	/**抢庄的时候第五张牌*/
	private Card fifthCard;
	/**抢庄的时候选择的倍数*/
	private Integer robMultiple = 0;
	/**庄次数*/
	private Integer bankerCount = 0;
	/**五小牛次数*/
	private Integer fiveSmallNiuCount = 0;
	/**炸弹牛次数*/
	private Integer bombNiuCount = 0;
	/**金牛次数*/
	private Integer goldNiuCount = 0;
	/**牛牛次数*/
	private Integer niuNiuCount = 0;
	/**有牛次数*/
	private Integer youNiuCount = 0;
	/**没牛次数*/
	private Integer wuNiuCount = 0;
	/**压分列表*/
	private List<Integer> stakeScoreList;
	
	public List<Integer> getStakeScoreList() {
		return stakeScoreList;
	}

	public void setStakeScoreList(List<Integer> stakeScoreList) {
		this.stakeScoreList = stakeScoreList;
	}

	public Integer getBankerCount() {
		return bankerCount;
	}

	public void setBankerCount(Integer bankerCount) {
		this.bankerCount = bankerCount;
	}

	public Integer getFiveSmallNiuCount() {
		return fiveSmallNiuCount;
	}

	public void setFiveSmallNiuCount(Integer fiveSmallNiuCount) {
		this.fiveSmallNiuCount = fiveSmallNiuCount;
	}

	public Integer getBombNiuCount() {
		return bombNiuCount;
	}

	public void setBombNiuCount(Integer bombNiuCount) {
		this.bombNiuCount = bombNiuCount;
	}

	public Integer getGoldNiuCount() {
		return goldNiuCount;
	}

	public void setGoldNiuCount(Integer goldNiuCount) {
		this.goldNiuCount = goldNiuCount;
	}

	public Integer getNiuNiuCount() {
		return niuNiuCount;
	}

	public void setNiuNiuCount(Integer niuNiuCount) {
		this.niuNiuCount = niuNiuCount;
	}

	public Integer getYouNiuCount() {
		return youNiuCount;
	}

	public void setYouNiuCount(Integer youNiuCount) {
		this.youNiuCount = youNiuCount;
	}

	public Integer getWuNiuCount() {
		return wuNiuCount;
	}

	public void setWuNiuCount(Integer wuNiuCount) {
		this.wuNiuCount = wuNiuCount;
	}

	public Integer getRobMultiple() {
		return robMultiple;
	}

	public void setRobMultiple(Integer robMultiple) {
		this.robMultiple = robMultiple;
	}

	public Integer getStakeScore() {
		return stakeScore;
	}

	public void setStakeScore(Integer stakeScore) {
		this.stakeScore = stakeScore;
	}

	public Long getRobBankerTime() {
		return robBankerTime;
	}

	public void setRobBankerTime(Long robBankerTime) {
		this.robBankerTime = robBankerTime;
	}

	public List<Card> getNnCardList() {
		return nnCardList;
	}

	public void setNnCardList(List<Card> nnCardList) {
		this.nnCardList = nnCardList;
	}

	public List<Card> getRobFourCardList() {
		return robFourCardList;
	}

	public void setRobFourCardList(List<Card> robFourCardList) {
		this.robFourCardList = robFourCardList;
	}

	public Card getFifthCard() {
		return fifthCard;
	}

	public void setFifthCard(Card fifthCard) {
		this.fifthCard = fifthCard;
	}

}
