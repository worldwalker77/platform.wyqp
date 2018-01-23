package cn.worldwalker.game.wyqp.common.domain.base;

public class TeaHouseModel{
	private Integer id;
	private Integer teaHouseId;
	private String teaHouseName;
	private Integer teaHouseTypeId;
	private Integer teaHouseNum;
	private Integer playerId;
	private String nickName;
	private Integer status;
	private Integer gameType;
	private Integer totalGame;
	private Integer roomBankerType;
	private Integer multipleLimit;
	private Integer payType;
	private String teaHouseOwnerWord;
	/**底分类型 1:1/2/3/4 2:2/4/8/10 3:3/6/9/12*/
	private Integer buttomScoreType;
	
	private Integer isNeedAudit;
	
	private Integer isDianXiaoer = 0;
	
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
	public Integer getButtomScoreType() {
		return buttomScoreType;
	}
	public void setButtomScoreType(Integer buttomScoreType) {
		this.buttomScoreType = buttomScoreType;
	}
	public String getTeaHouseOwnerWord() {
		return teaHouseOwnerWord;
	}
	public void setTeaHouseOwnerWord(String teaHouseOwnerWord) {
		this.teaHouseOwnerWord = teaHouseOwnerWord;
	}
	public Integer getTeaHouseId() {
		return teaHouseId;
	}
	public void setTeaHouseId(Integer teaHouseId) {
		this.teaHouseId = teaHouseId;
	}
	public Integer getTeaHouseTypeId() {
		return teaHouseTypeId;
	}
	public void setTeaHouseTypeId(Integer teaHouseTypeId) {
		this.teaHouseTypeId = teaHouseTypeId;
	}
	public Integer getTeaHouseNum() {
		return teaHouseNum;
	}
	public void setTeaHouseNum(Integer teaHouseNum) {
		this.teaHouseNum = teaHouseNum;
	}
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getGameType() {
		return gameType;
	}
	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}
	public Integer getTotalGame() {
		return totalGame;
	}
	public void setTotalGame(Integer totalGame) {
		this.totalGame = totalGame;
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
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTeaHouseName() {
		return teaHouseName;
	}
	public void setTeaHouseName(String teaHouseName) {
		this.teaHouseName = teaHouseName;
	}
	
}
