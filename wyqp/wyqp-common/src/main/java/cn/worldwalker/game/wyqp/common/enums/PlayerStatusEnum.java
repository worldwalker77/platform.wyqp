package cn.worldwalker.game.wyqp.common.enums;

public enum PlayerStatusEnum {
	
	observer(0, "观察者"),
	notReady(1, "未准备"),
	ready(2, "已准备");
	
	public Integer status;
	public String desc;
	
	private PlayerStatusEnum(Integer status, String desc){
		this.status = status;
		this.desc = desc;
	}
}
