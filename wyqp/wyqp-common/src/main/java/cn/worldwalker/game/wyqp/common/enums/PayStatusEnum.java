package cn.worldwalker.game.wyqp.common.enums;

public enum PayStatusEnum {
	
	unPay(0, "未支付"),
	pay(1, "已支付");
	
	public Integer type;
	public String desc;
	
	private PayStatusEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
	
	public static PayStatusEnum getPayTypeEnum(Integer type){
		for(PayStatusEnum payTypeEnum : PayStatusEnum.values()){
			if (payTypeEnum.type.equals(type)) {
				return payTypeEnum;
			}
		}
		return null;
	}
}
