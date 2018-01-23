package cn.worldwalker.game.wyqp.common.enums;

public enum PayTypeEnum {
	
	roomOwnerPay(1, "房主支付"),
	AAPay(2, "AA支付"),
	winnerPay(3, "赢家支付");
	
	public Integer type;
	public String desc;
	
	private PayTypeEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
	
	public static PayTypeEnum getPayTypeEnum(Integer type){
		for(PayTypeEnum payTypeEnum : PayTypeEnum.values()){
			if (payTypeEnum.type.equals(type)) {
				return payTypeEnum;
			}
		}
		return null;
	}
}
