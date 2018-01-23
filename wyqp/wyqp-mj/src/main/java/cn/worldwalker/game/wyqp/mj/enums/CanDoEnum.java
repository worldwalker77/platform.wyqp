package cn.worldwalker.game.wyqp.mj.enums;

public enum CanDoEnum {
	
	CHI(1, "吃"),
	PENG(2, "碰"),
	GANG(3, "杠"),
	HU(4, "胡");
	
	public Integer type;
	
	public String desc;
	
	private CanDoEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
}
