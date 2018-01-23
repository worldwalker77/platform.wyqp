package cn.worldwalker.game.wyqp.common.domain.base;

import java.util.Date;
import java.util.List;

public class BaseRoomInfo {
	/**房间id*/
	private Integer roomId;
	/**房主id*/
	private Integer roomOwnerId;
	/**当前局赢家*/
	private Integer curWinnerId;
	/**总赢家*/
	private Integer totalWinnerId;
	/**当前可操作人id*/
	private Integer curPlayerId;
	/**庄家id*/
	private Integer roomBankerId;
	/**总局数*/
	private Integer totalGames;
	/**当前局数*/
	private Integer curGame = 0;
	/**支付方式 1：房主付费 2：AA付费*/
	private Integer payType;
	/**此房间所在服务器ip*/
	private String serverIp;
	/**房间的创建时间*/
	private Date createTime;
	/**房间的更新时间*/
	private Date updateTime;
	/**当前房间状态*/
	private Integer status;
	
	private List playerList;
	
	private Integer gameType;
	
	private Integer playerNumLimit;
	
	/**以下为茶楼相关*/
	private Integer teaHouseNum;
	private Integer tableNum;

	public Integer getPlayerNumLimit() {
		return playerNumLimit;
	}

	public void setPlayerNumLimit(Integer playerNumLimit) {
		this.playerNumLimit = playerNumLimit;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getRoomOwnerId() {
		return roomOwnerId;
	}

	public void setRoomOwnerId(Integer roomOwnerId) {
		this.roomOwnerId = roomOwnerId;
	}

	public Integer getCurWinnerId() {
		return curWinnerId;
	}

	public void setCurWinnerId(Integer curWinnerId) {
		this.curWinnerId = curWinnerId;
	}

	public Integer getTotalWinnerId() {
		return totalWinnerId;
	}

	public void setTotalWinnerId(Integer totalWinnerId) {
		this.totalWinnerId = totalWinnerId;
	}

	public Integer getCurPlayerId() {
		return curPlayerId;
	}

	public void setCurPlayerId(Integer curPlayerId) {
		this.curPlayerId = curPlayerId;
	}

	public Integer getRoomBankerId() {
		return roomBankerId;
	}

	public void setRoomBankerId(Integer roomBankerId) {
		this.roomBankerId = roomBankerId;
	}

	public Integer getTotalGames() {
		return totalGames;
	}

	public void setTotalGames(Integer totalGames) {
		this.totalGames = totalGames;
	}

	public Integer getCurGame() {
		return curGame;
	}

	public void setCurGame(Integer curGame) {
		this.curGame = curGame;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List getPlayerList() {
		return playerList;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
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
