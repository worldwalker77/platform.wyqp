package cn.worldwalker.game.wyqp.common.domain.base;

import java.util.Date;
import java.util.List;

public class UserRecordModel {
	
	private Long id;
	
	private Integer gameType;
	
	private Integer playerId;
	
	private Integer roomId;
	
	private Integer payType;
	
	private Integer totalGames;
	
	private Integer score;
	
	private String recordInfo;
	
	private String remark; 
	
	private Date createTime;
	
	private List<RecordModel> recordList;
	
	private Integer teaHouseNum;
	
	private Integer tableNum;
	
	private String nickName;
	
	private String headImgUrl;
	
	private Integer totalScore;
	
	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRecordInfo() {
		return recordInfo;
	}

	public void setRecordInfo(String recordInfo) {
		this.recordInfo = recordInfo;
	}

	public List<RecordModel> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<RecordModel> recordList) {
		this.recordList = recordList;
	}


}
