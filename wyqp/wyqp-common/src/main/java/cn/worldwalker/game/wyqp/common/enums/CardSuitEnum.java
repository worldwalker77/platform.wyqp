package cn.worldwalker.game.wyqp.common.enums;

public enum CardSuitEnum {
	
	HEART(4, "黑桃"),
	SPADE(3, "红桃"),
	CLUB(2, "梅花"),
	DIAMOND(1, "方块");
	
	public Integer suit;
	
	public String name;
	
	private CardSuitEnum(Integer suit, String name){
		this.suit = suit;
		this.name = name;
	}
	
	public static CardSuitEnum getCardSuitBySuit(Integer suit){
		for(CardSuitEnum cardSuit : CardSuitEnum.values()){
			if (cardSuit.suit.equals(suit)) {
				return cardSuit;
			}
		}
		return null;
		
	}
}
