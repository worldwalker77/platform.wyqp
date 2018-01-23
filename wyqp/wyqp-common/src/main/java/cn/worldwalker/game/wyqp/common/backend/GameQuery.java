package cn.worldwalker.game.wyqp.common.backend;

public class GameQuery {
	private Integer proxyId;
	private Integer playerId;
	private String startDate;
	private String endDate;
	private long offset;
	private int limit = 10;
	
	private String mobilePhone;
	private String password;
	private Integer changeRoomCardNum;
	private Integer roomCardNum;
	private String nickName;
	private Integer winProbability;
	private String wechatNum;
	private String oldPassword;
	private String newPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getWechatNum() {
		return wechatNum;
	}
	public void setWechatNum(String wechatNum) {
		this.wechatNum = wechatNum;
	}
	public Integer getWinProbability() {
		return winProbability;
	}
	public void setWinProbability(Integer winProbability) {
		this.winProbability = winProbability;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Integer getProxyId() {
		return proxyId;
	}
	public void setProxyId(Integer proxyId) {
		this.proxyId = proxyId;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public Integer getChangeRoomCardNum() {
		return changeRoomCardNum;
	}
	public void setChangeRoomCardNum(Integer changeRoomCardNum) {
		this.changeRoomCardNum = changeRoomCardNum;
	}
	public Integer getRoomCardNum() {
		return roomCardNum;
	}
	public void setRoomCardNum(Integer roomCardNum) {
		this.roomCardNum = roomCardNum;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
