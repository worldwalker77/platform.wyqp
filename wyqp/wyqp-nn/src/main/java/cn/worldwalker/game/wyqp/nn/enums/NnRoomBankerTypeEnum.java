package cn.worldwalker.game.wyqp.nn.enums;

public enum NnRoomBankerTypeEnum {
	
	inTurnBanker(1, "轮流坐庄"),
	overLordBanker(2, "霸王庄"),
	robBanker(3, "抢庄"),
	winnerBanker(4, "赢家坐庄");
	
	public Integer type;
	public String desc;
	
	private NnRoomBankerTypeEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
	
	public static  NnRoomBankerTypeEnum getNnRoomBankerTypeEnum(Integer type){
		for(NnRoomBankerTypeEnum typeEnum : NnRoomBankerTypeEnum.values()){
			if (typeEnum.type.equals(type)) {
				return typeEnum;
			}
		}
		return null;
	}
}
