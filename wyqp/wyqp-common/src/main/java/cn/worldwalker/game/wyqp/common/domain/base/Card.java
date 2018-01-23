package cn.worldwalker.game.wyqp.common.domain.base;

public class Card {
	
	/**花色*/
	private Integer cardSuit;
	/**花色名称*/
	private String cardSuitName;
	/**真实牌值*/
	private Integer cardValue;
	/**索引值*/
	private Integer cardIndex;
	
	public Card copy(){
		Card card = new Card();
		card.setCardIndex(cardIndex);
		card.setCardSuit(cardSuit);
		card.setCardSuitName(cardSuitName);
		card.setCardValue(cardValue);
		return card;
	}
	public Integer getCardSuit() {
		return cardSuit;
	}
	public void setCardSuit(Integer cardSuit) {
		this.cardSuit = cardSuit;
	}
	public Integer getCardValue() {
		return cardValue;
	}
	public void setCardValue(Integer cardValue) {
		this.cardValue = cardValue;
	}
	public Integer getCardIndex() {
		return cardIndex;
	}
	public void setCardIndex(Integer cardIndex) {
		this.cardIndex = cardIndex;
	}
	public String getCardSuitName() {
		return cardSuitName;
	}
	public void setCardSuitName(String cardSuitName) {
		this.cardSuitName = cardSuitName;
	}
	
}
