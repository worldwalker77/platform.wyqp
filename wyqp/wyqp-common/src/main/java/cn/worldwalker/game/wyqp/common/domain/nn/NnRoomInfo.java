package cn.worldwalker.game.wyqp.common.domain.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;

public class NnRoomInfo  extends BaseRoomInfo{
	
	private Integer roomBankerType;
	
	/**倍数限制 3倍5倍不限制*/
	private Integer multipleLimit;
	
	/**抢庄的时候选择的倍数*/
	private Integer robMultiple = 0;
	
	private List<NnPlayerInfo> playerList = new ArrayList<NnPlayerInfo>();
	
	/**底分类型 1:1/2/3/4 2:2/4/8/10 3:3/6/9/12*/
	private Integer buttomScoreType;
	/**随机压分局数索引列表*/
	private List<Integer> randomStakeScoreGameIndexList;
	/**随机压分玩家id与压分值映射*/
	private Map<Integer, Integer> randomPlayerIdStakeScoreMap;
	
	public Map<Integer, Integer> getRandomPlayerIdStakeScoreMap() {
		return randomPlayerIdStakeScoreMap;
	}
	public void setRandomPlayerIdStakeScoreMap(
			Map<Integer, Integer> randomPlayerIdStakeScoreMap) {
		this.randomPlayerIdStakeScoreMap = randomPlayerIdStakeScoreMap;
	}
	public List<Integer> getRandomStakeScoreGameIndexList() {
		return randomStakeScoreGameIndexList;
	}
	public void setRandomStakeScoreGameIndexList(
			List<Integer> randomStakeScoreGameIndexList) {
		this.randomStakeScoreGameIndexList = randomStakeScoreGameIndexList;
	}
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
	public Integer getMultipleLimit() {
		return multipleLimit;
	}
	public void setMultipleLimit(Integer multipleLimit) {
		this.multipleLimit = multipleLimit;
	}
	public List<NnPlayerInfo> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(List<NnPlayerInfo> playerList) {
		this.playerList = playerList;
	}
	
	
}
