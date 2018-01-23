package cn.worldwalker.game.wyqp.common.enums;

public enum CardTypeEnum {
	
	C235(1, "235"),
	SINGLE(2, "单个"),
	PAIR(3, "对子"),
	STRAIGHT(4, "顺子"),
	JINHUA(5, "金花"),
	FLUSH(6, "同花顺"),
	BOMB(7, "炸弹");
	
	public Integer cardType;
	public String desc;
	
	private CardTypeEnum(Integer cardType, String desc){
		this.cardType = cardType;
		this.desc = desc;
	}
	
	public static CardTypeEnum getCardTypeEnum(Integer cardType){
		for(CardTypeEnum cardTypeEnum : CardTypeEnum.values()){
			if (cardTypeEnum.cardType.equals(cardType)) {
				return cardTypeEnum;
			}
		}
		return null;
	}
}
