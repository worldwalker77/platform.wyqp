package cn.worldwalker.game.wyqp.common.domain.base;

import java.util.Date;

public class OrderModel {
	
	private Long orderId;
	
	private Integer playerId;
	
	private Integer productId;
	
	private Integer roomCardNum;
	
	private Integer price;
	
	private Integer wxPayPrice;
	
	private Integer payStatus;
	
	private String transactionId;
	
	private Date createtime;
	
	private Date updateTime;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getRoomCardNum() {
		return roomCardNum;
	}

	public void setRoomCardNum(Integer roomCardNum) {
		this.roomCardNum = roomCardNum;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getWxPayPrice() {
		return wxPayPrice;
	}

	public void setWxPayPrice(Integer wxPayPrice) {
		this.wxPayPrice = wxPayPrice;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

}
