package cn.worldwalker.game.wyqp.common.enums;

public enum RoomCardOperationEnum {
	
	buyCard(1, "购买房卡"),
	consumeCard(2, "游戏消费房卡"),
	jobCompensateConsumeCard(3, "job补偿消费房卡"),
	other(4, "其他");
	
	public Integer type;
	public String desc;
	
	private RoomCardOperationEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
	
	public static RoomCardOperationEnum getRoomOperationEnum(Integer type){
		for(RoomCardOperationEnum operationEnum : RoomCardOperationEnum.values()){
			if (operationEnum.type.equals(type)) {
				return operationEnum;
			}
		}
		return null;
	}
}
