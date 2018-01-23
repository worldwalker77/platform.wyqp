package cn.worldwalker.game.wyqp.mj.enums;

public enum MjCardSuitEnum {
	
	TONG(4, "筒"),
	TIAO(3, "条"),
	WAN(2, "万"),
	FENG(1, "风");
	
	public Integer suit;
	
	public String name;
	
	private MjCardSuitEnum(Integer suit, String name){
		this.suit = suit;
		this.name = name;
	}
	
	public static MjCardSuitEnum getCardSuitBySuit(Integer suit){
		for(MjCardSuitEnum cardSuit : MjCardSuitEnum.values()){
			if (cardSuit.suit.equals(suit)) {
				return cardSuit;
			}
		}
		return null;
		
	}
}
