package cn.worldwalker.game.wyqp.jh.enums;


public enum JhCardTypeEnum {
	
	C235(1, "235"),
	SINGLE(2, "单个"),
	PAIR(3, "对子"),
	STRAIGHT(4, "顺子"),
	JINHUA(5, "金花"),
	FLUSH(6, "同花顺"),
	BOMB(7, "炸弹");
	
	public Integer cardType;
	public String desc;
	
	private JhCardTypeEnum(Integer cardType, String desc){
		this.cardType = cardType;
		this.desc = desc;
	}
	
	public static JhCardTypeEnum getCardTypeEnum(Integer cardType){
		for(JhCardTypeEnum cardTypeEnum : JhCardTypeEnum.values()){
			if (cardTypeEnum.cardType.equals(cardType)) {
				return cardTypeEnum;
			}
		}
		return null;
	}
}
