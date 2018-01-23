package cn.worldwalker.game.wyqp.common.domain.base;

import java.util.List;

public class BaseMsg {
	
	private Integer roomId;
	private Integer playerId;
	private Integer otherPlayerId;//另外一方的id
	private Integer payType;//1:房主付费 2:AA付费
	private Integer totalGames;//10局，20局，30局
	private Integer refreshType;//刷新类型 1，断线重连刷新  2 消息id不连续刷新
	private Integer chatType;//聊天消息类型 1 文本 2 表情 3语音 4图片
	private String chatMsg;//消息
	private String feedBack;
	private String mobilePhone;
	private Integer feedBackType;
	private Integer noticeType;
	private String noticeContent;
	private List<Integer> noticePlayerList;
	private String address;
	private String x;
	private String y;
	private Integer proxyId;
	private Integer productId;
	private Integer playerNumLimit;
	/**以下为茶楼相关*/
	private Integer teaHouseNum;
	private Integer tableNum;
	private Integer status;
	private String teaHouseOwnerWord;
	private Integer isNeedAudit;
	private Integer isDianXiaoer;
	
	public Integer getIsDianXiaoer() {
		return isDianXiaoer;
	}
	public void setIsDianXiaoer(Integer isDianXiaoer) {
		this.isDianXiaoer = isDianXiaoer;
	}
	public Integer getIsNeedAudit() {
		return isNeedAudit;
	}
	public void setIsNeedAudit(Integer isNeedAudit) {
		this.isNeedAudit = isNeedAudit;
	}
	public String getTeaHouseOwnerWord() {
		return teaHouseOwnerWord;
	}
	public void setTeaHouseOwnerWord(String teaHouseOwnerWord) {
		this.teaHouseOwnerWord = teaHouseOwnerWord;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPlayerNumLimit() {
		return playerNumLimit;
	}
	public void setPlayerNumLimit(Integer playerNumLimit) {
		this.playerNumLimit = playerNumLimit;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Integer getTotalGames() {
		return totalGames;
	}
	public void setTotalGames(Integer totalGames) {
		this.totalGames = totalGames;
	}
	public Integer getRefreshType() {
		return refreshType;
	}
	public void setRefreshType(Integer refreshType) {
		this.refreshType = refreshType;
	}
	public Integer getChatType() {
		return chatType;
	}
	public void setChatType(Integer chatType) {
		this.chatType = chatType;
	}
	public String getChatMsg() {
		return chatMsg;
	}
	public void setChatMsg(String chatMsg) {
		this.chatMsg = chatMsg;
	}
	public String getFeedBack() {
		return feedBack;
	}
	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public Integer getFeedBackType() {
		return feedBackType;
	}
	public void setFeedBackType(Integer feedBackType) {
		this.feedBackType = feedBackType;
	}
	public Integer getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
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
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public Integer getOtherPlayerId() {
		return otherPlayerId;
	}
	public void setOtherPlayerId(Integer otherPlayerId) {
		this.otherPlayerId = otherPlayerId;
	}
	public List<Integer> getNoticePlayerList() {
		return noticePlayerList;
	}
	public void setNoticePlayerList(List<Integer> noticePlayerList) {
		this.noticePlayerList = noticePlayerList;
	}
	public Integer getProxyId() {
		return proxyId;
	}
	public void setProxyId(Integer proxyId) {
		this.proxyId = proxyId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getTeaHouseNum() {
		return teaHouseNum;
	}
	public void setTeaHouseNum(Integer teaHouseNum) {
		this.teaHouseNum = teaHouseNum;
	}
	public Integer getTableNum() {
		return tableNum;
	}
	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}
	
}
