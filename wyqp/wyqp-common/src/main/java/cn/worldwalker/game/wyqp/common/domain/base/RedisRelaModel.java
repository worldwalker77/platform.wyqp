package cn.worldwalker.game.wyqp.common.domain.base;

public class RedisRelaModel {
	private Integer roomId;
	private Integer gameType;
	private Long updateTime;
	private Integer playerId;
	
	public RedisRelaModel(Integer playerId, Integer roomId, Integer gameType, Long updateTime){
		this.playerId = playerId;
		this.roomId = roomId;
		this.gameType = gameType;
		this.updateTime = updateTime;
	}
	
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
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

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	
}
