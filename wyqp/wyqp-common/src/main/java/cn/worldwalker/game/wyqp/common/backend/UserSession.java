package cn.worldwalker.game.wyqp.common.backend;

import java.io.Serializable;

public class UserSession implements Serializable{
	private static final long serialVersionUID = 7478917352637885504L;
	private Integer proxyId;
	private Integer playerId;
	private String nickName;
	private String mobilePhone;
	private String wechatNum;
	private String realName;
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
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getWechatNum() {
		return wechatNum;
	}
	public void setWechatNum(String wechatNum) {
		this.wechatNum = wechatNum;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
}
