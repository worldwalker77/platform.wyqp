package cn.worldwalker.game.wyqp.common.domain.base;

import java.util.List;

public class BasePlayerInfo {
	/**玩家id*/
	private Integer playerId;
	/**用户图像url*/
	private String headImgUrl;
	/**玩家昵称*/
	private String nickName;
	/**玩家顺序*/
	private Integer order;
	/**用户级别*/
	private Integer level;
	/**玩家状态  */
	private Integer status;
	/**牌型*/
	private Integer cardType = 0;
	/**牌*/
	private List<Card> cardList;
	/**当前局得分*/
	private Integer curScore = 0;
	/**一圈总得分*/
	private Integer totalScore = 0;
	/**赢的局数*/
	private Integer winTimes = 0;
	/**输的局数*/
	private Integer loseTimes = 0;
	/**1 同意解散 2不同意解散*/
	private Integer dissolveStatus;
	/**房卡数*/
	private Integer roomCardNum = 0;
	/**最大牌型*/
	private Integer maxCardType = 0;
	/**玩家ip*/
	private String ip;
	/**在线状态1在线 0离线*/
	private Integer onlineStatus;
	
	private String address;
	private String x;
	private String y;
	/**玩过的局数*/
	private Integer playedCount = 0;
	
	private Integer winProbability = 0;
	
	private List<Integer> winProbabilityGameIndexList = null;
	
	public List<Integer> getWinProbabilityGameIndexList() {
		return winProbabilityGameIndexList;
	}
	public void setWinProbabilityGameIndexList(
			List<Integer> winProbabilityGameIndexList) {
		this.winProbabilityGameIndexList = winProbabilityGameIndexList;
	}
	public Integer getWinProbability() {
		return winProbability;
	}
	public void setWinProbability(Integer winProbability) {
		this.winProbability = winProbability;
	}
	public Integer getPlayedCount() {
		return playedCount;
	}
	public void setPlayedCount(Integer playedCount) {
		this.playedCount = playedCount;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public List<Card> getCardList() {
		return cardList;
	}
	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}
	public Integer getCurScore() {
		return curScore;
	}
	public void setCurScore(Integer curScore) {
		this.curScore = curScore;
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getWinTimes() {
		return winTimes;
	}
	public void setWinTimes(Integer winTimes) {
		this.winTimes = winTimes;
	}
	public Integer getLoseTimes() {
		return loseTimes;
	}
	public void setLoseTimes(Integer loseTimes) {
		this.loseTimes = loseTimes;
	}
	public Integer getDissolveStatus() {
		return dissolveStatus;
	}
	public void setDissolveStatus(Integer dissolveStatus) {
		this.dissolveStatus = dissolveStatus;
	}
	public Integer getRoomCardNum() {
		return roomCardNum;
	}
	public void setRoomCardNum(Integer roomCardNum) {
		this.roomCardNum = roomCardNum;
	}
	public Integer getMaxCardType() {
		return maxCardType;
	}
	public void setMaxCardType(Integer maxCardType) {
		this.maxCardType = maxCardType;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	
}
