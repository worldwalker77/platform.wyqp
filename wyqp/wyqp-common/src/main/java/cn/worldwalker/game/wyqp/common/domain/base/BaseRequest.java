package cn.worldwalker.game.wyqp.common.domain.base;

public class BaseRequest {
	
	private Integer msgType;
	
	private String token;
	
	private Integer gameType;
	
	private BaseMsg msg;
	
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getGameType() {
		return gameType;
	}
	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}
	public BaseMsg getMsg() {
		return msg;
	}
	public void setMsg(BaseMsg msg) {
		this.msg = msg;
	}
	
}
