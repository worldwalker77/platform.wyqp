package cn.worldwalker.game.wyqp.common.domain.mj;

import java.util.List;

import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;

public class MjPlayerInfo extends BasePlayerInfo{
	/**玩家手上的牌*/
	private List<Integer> handCardList;
	/**已经吃的牌列表*/
	private List<Integer> chiCardList;
	/**已经碰的牌列表*/
	private List<Integer> pengCardList;
	/**已经杠的牌列表*/
	private List<Integer> gangCardList;
	/**已经打出的牌列表*/
	private List<Integer> discardCardList;
	public List<Integer> getHandCardList() {
		return handCardList;
	}
	public void setHandCardList(List<Integer> handCardList) {
		this.handCardList = handCardList;
	}
	public List<Integer> getChiCardList() {
		return chiCardList;
	}
	public void setChiCardList(List<Integer> chiCardList) {
		this.chiCardList = chiCardList;
	}
	public List<Integer> getPengCardList() {
		return pengCardList;
	}
	public void setPengCardList(List<Integer> pengCardList) {
		this.pengCardList = pengCardList;
	}
	public List<Integer> getGangCardList() {
		return gangCardList;
	}
	public void setGangCardList(List<Integer> gangCardList) {
		this.gangCardList = gangCardList;
	}
	public List<Integer> getDiscardCardList() {
		return discardCardList;
	}
	public void setDiscardCardList(List<Integer> discardCardList) {
		this.discardCardList = discardCardList;
	}
	
}
