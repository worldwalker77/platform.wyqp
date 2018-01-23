package cn.worldwalker.game.wyqp.common.domain.base;

public class ProxyModel {
	
	private Integer proxyId;
	private Integer playerId;
	private String nickName;
	private Long extractAmount;
	private Long remainderAmount;
	private Long totalIncome;
	private Integer curIncome;
	private Integer curRemainder;
	
	public Integer getProxyId() {
		return proxyId;
	}
	public void setProxyId(Integer proxyId) {
		this.proxyId = proxyId;
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
	public Long getExtractAmount() {
		return extractAmount;
	}
	public void setExtractAmount(Long extractAmount) {
		this.extractAmount = extractAmount;
	}
	public Long getRemainderAmount() {
		return remainderAmount;
	}
	public void setRemainderAmount(Long remainderAmount) {
		this.remainderAmount = remainderAmount;
	}
	public Long getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(Long totalIncome) {
		this.totalIncome = totalIncome;
	}
	public Integer getCurIncome() {
		return curIncome;
	}
	public void setCurIncome(Integer curIncome) {
		this.curIncome = curIncome;
	}
	public Integer getCurRemainder() {
		return curRemainder;
	}
	public void setCurRemainder(Integer curRemainder) {
		this.curRemainder = curRemainder;
	}
	
}
