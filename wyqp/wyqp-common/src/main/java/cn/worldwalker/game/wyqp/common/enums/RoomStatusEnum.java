package cn.worldwalker.game.wyqp.common.enums;

public enum RoomStatusEnum {
	
	justBegin(1, "刚开始准备阶段"),
	inGame(2, "小局中"),
	curGameOver(3, "小局结束"),
	totalGameOver(4, "一圈结束");
	
	public Integer status;
	public String desc;
	
	private RoomStatusEnum(Integer status, String desc){
		this.status = status;
		this.desc = desc;
	}
	
	public static RoomStatusEnum getRoomStatusEnum(Integer status){
		for(RoomStatusEnum statusEnum : RoomStatusEnum.values()){
			if (statusEnum.status.equals(status)) {
				return statusEnum;
			}
		}
		return null;
	}
}
