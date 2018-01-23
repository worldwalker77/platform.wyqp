package cn.worldwalker.game.wyqp.common.domain.base;

public class UserInfo {
	
	private Integer playerId;
	
	private Integer roomId;
	
	private String nickName;
	
	private Integer level;
	
	private String serverIp;
	
	private String port;
	
	private String token;
	
	private String headImgUrl;
	
	private String remoteIp;
	
	private Integer roomCardNum = 0;
	
	private String address;
	
	private String x;
	
	private String y;
	
	private Integer winProbability = 0;
	
	private Integer teaHouseNum;
	
	
	public Integer getTeaHouseNum() {
		return teaHouseNum;
	}

	public void setTeaHouseNum(Integer teaHouseNum) {
		this.teaHouseNum = teaHouseNum;
	}

	public Integer getWinProbability() {
		return winProbability;
	}

	public void setWinProbability(Integer winProbability) {
		this.winProbability = winProbability;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public Integer getRoomCardNum() {
		return roomCardNum;
	}

	public void setRoomCardNum(Integer roomCardNum) {
		this.roomCardNum = roomCardNum;
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
	
}
