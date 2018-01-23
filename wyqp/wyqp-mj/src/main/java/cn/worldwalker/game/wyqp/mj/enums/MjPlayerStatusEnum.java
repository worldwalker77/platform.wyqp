package cn.worldwalker.game.wyqp.mj.enums;

public enum MjPlayerStatusEnum {
	
	notReady(1, "未准备"),
	ready(2, "已准备"),
	notWatch(3, "未看牌"),
	watch(4, "已看牌"),
	autoDiscard(5, "主动弃牌"),
	compareDisCard(6, "比牌被动弃牌"),
	leave(7, "离开");
	
	public Integer status;
	public String desc;
	
	private MjPlayerStatusEnum(Integer status, String desc){
		this.status = status;
		this.desc = desc;
	}
}
