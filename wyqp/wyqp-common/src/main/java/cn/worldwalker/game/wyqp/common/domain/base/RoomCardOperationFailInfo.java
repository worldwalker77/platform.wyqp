package cn.worldwalker.game.wyqp.common.domain.base;

public class RoomCardOperationFailInfo {
	
	private Integer playerId;
	private Integer gameType;
	private Integer payType;
	private Integer totalGames;
	private Integer roomCardOperationType;
	
	public RoomCardOperationFailInfo(Integer playerId, Integer gameType, Integer payType, Integer totalGames, Integer roomCardOperationType){
		this.playerId = playerId;
		this.gameType = gameType;
		this.payType = payType;
		this.totalGames = totalGames;
		this.roomCardOperationType = roomCardOperationType;
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
	public Integer getRoomCardOperationType() {
		return roomCardOperationType;
	}
	public void setRoomCardOperationType(Integer roomCardOperationType) {
		this.roomCardOperationType = roomCardOperationType;
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
	
}
