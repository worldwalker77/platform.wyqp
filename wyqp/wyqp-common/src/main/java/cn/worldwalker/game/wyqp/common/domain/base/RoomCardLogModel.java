package cn.worldwalker.game.wyqp.common.domain.base;

import java.util.Date;

public class RoomCardLogModel {
	
	private Integer id;
	
	private Integer playerId;
	
	private Integer preRoomCardNum;
	
	private Integer curRoomCardNum;
	
	private Integer diffRoomCardNum;
	
	private Integer operatorId;
	
	private Integer operatorType;
	
	private Date createTime;

	private Integer gameType;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public Integer getPreRoomCardNum() {
		return preRoomCardNum;
	}

	public void setPreRoomCardNum(Integer preRoomCardNum) {
		this.preRoomCardNum = preRoomCardNum;
	}

	public Integer getCurRoomCardNum() {
		return curRoomCardNum;
	}

	public void setCurRoomCardNum(Integer curRoomCardNum) {
		this.curRoomCardNum = curRoomCardNum;
	}

	public Integer getDiffRoomCardNum() {
		return diffRoomCardNum;
	}

	public void setDiffRoomCardNum(Integer diffRoomCardNum) {
		this.diffRoomCardNum = diffRoomCardNum;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public Integer getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(Integer operatorType) {
		this.operatorType = operatorType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	
}
