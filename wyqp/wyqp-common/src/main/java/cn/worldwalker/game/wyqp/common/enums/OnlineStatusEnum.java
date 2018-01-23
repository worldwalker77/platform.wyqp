package cn.worldwalker.game.wyqp.common.enums;

public enum OnlineStatusEnum {
	online(1, "在线"),
	offline(0, "离线");
	
	public Integer status;
	public String desc;
	
	private OnlineStatusEnum(Integer status, String desc){
		this.status = status;
		this.desc = desc;
	}
	
	public static OnlineStatusEnum getGameTypeEnumByType(Integer status){
		for(OnlineStatusEnum onlineStatusEnum : OnlineStatusEnum.values()){
			if (onlineStatusEnum.status.equals(status)) {
				return onlineStatusEnum;
			}
		}
		return null;
	}
}
