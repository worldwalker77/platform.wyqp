package cn.worldwalker.game.wyqp.common.domain.base;


public class RedisResponse{
	String des = "success";
	Object value;
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
