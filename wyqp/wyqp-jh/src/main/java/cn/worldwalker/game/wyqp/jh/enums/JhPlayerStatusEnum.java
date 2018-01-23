package cn.worldwalker.game.wyqp.jh.enums;

public enum JhPlayerStatusEnum {
	
	observer(0, "观察者"),
	notReady(1, "未准备"),
	ready(2, "已准备"),
	notWatch(3, "未看牌"),
	watch(4, "已看牌"),
	autoDiscard(5, "主动弃牌"),
	compareDisCard(6, "比牌被动弃牌");
	
	public Integer status;
	public String desc;
	
	private JhPlayerStatusEnum(Integer status, String desc){
		this.status = status;
		this.desc = desc;
	}
}
