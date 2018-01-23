package cn.worldwalker.game.wyqp.common.enums;

public enum DissolveStatusEnum {
	
	agree(1, "同意解散房间"),
	disagree(2, "不同意解散房间");
	
	public Integer status;
	public String desc;
	
	private DissolveStatusEnum(int status, String desc){
		this.status = status;
		this.desc = desc;
	}
	
	public static DissolveStatusEnum getDissolveEnumByType(Integer status){
		for(DissolveStatusEnum dissolveEnum : DissolveStatusEnum.values()){
			if (status == dissolveEnum.status) {
				return dissolveEnum;
			}
		}
		return null;
	}
}
