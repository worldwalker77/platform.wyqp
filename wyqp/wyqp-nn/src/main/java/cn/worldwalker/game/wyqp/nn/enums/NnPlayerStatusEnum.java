package cn.worldwalker.game.wyqp.nn.enums;

public enum NnPlayerStatusEnum {
	
	observer(0, "观察者"),
	notReady(1, "未准备"),
	ready(2, "已准备"),
	notRob(3, "不抢庄"),
	rob(4, "抢庄"),
	stakeScore(5, "已压分"),
	showCard(6, "已亮牌");
	
	public Integer status;
	public String desc;
	
	private NnPlayerStatusEnum(Integer status, String desc){
		this.status = status;
		this.desc = desc;
	}
}
