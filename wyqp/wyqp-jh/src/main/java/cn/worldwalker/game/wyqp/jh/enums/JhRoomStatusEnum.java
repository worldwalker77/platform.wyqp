package cn.worldwalker.game.wyqp.jh.enums;


public enum JhRoomStatusEnum {
	
	justBegin(1, "刚开始准备阶段"),
	inGame(2, "小局中"),
	curGameOver(3, "小局结束"),
	totalGameOver(4, "一圈结束");
	
	public Integer status;
	public String desc;
	
	private JhRoomStatusEnum(Integer status, String desc){
		this.status = status;
		this.desc = desc;
	}
	
	public static JhRoomStatusEnum getRoomStatusEnum(Integer status){
		for(JhRoomStatusEnum statusEnum : JhRoomStatusEnum.values()){
			if (statusEnum.status.equals(status)) {
				return statusEnum;
			}
		}
		return null;
	}
}
