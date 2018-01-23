package cn.worldwalker.game.wyqp.nn.enums;

public enum NnCardTypeEnum {
	
	NO_NIU(0, 1, "无牛"),
	NIU_1(1, 1, "牛一"),
	NIU_2(2, 1, "牛二"),
	NIU_3(3, 1, "牛三"),
	NIU_4(4, 1, "牛四"),
	NIU_5(5, 1, "牛五"),
	NIU_6(6, 1, "牛六"),
	NIU_7(7, 1, "牛七"),
	NIU_8(8, 2, "牛八"),
	NIU_9(9, 3, "牛九"),
	NIU_NIU(10, 4, "牛牛"),
	GOLD_NIU(11, 5, "金牛"),
	FIVE_SMALL_NIU(12, 6, "五小牛"),
	BOMB_NIU(13, 7, "炸弹牛");
	
	public Integer cardType;
	public Integer multiple;
	public String desc;
	
	private NnCardTypeEnum(Integer cardType, Integer multiple, String desc){
		this.cardType = cardType;
		this.multiple = multiple;
		this.desc = desc;
	}
	
	public static NnCardTypeEnum getNnCardTypeEnum(Integer cardType){
		for(NnCardTypeEnum cardTypeEnum : NnCardTypeEnum.values()){
			if (cardTypeEnum.cardType.equals(cardType)) {
				return cardTypeEnum;
			}
		}
		return null;
	}
}
