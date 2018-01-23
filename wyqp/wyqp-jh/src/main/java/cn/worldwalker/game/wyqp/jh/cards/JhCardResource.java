package cn.worldwalker.game.wyqp.jh.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.worldwalker.game.wyqp.common.domain.base.Card;
import cn.worldwalker.game.wyqp.common.enums.CardSuitEnum;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;

public class JhCardResource {
	
	/**总共52张牌*/
	private static final Integer CARD_MAX_VALUE = 52;
	
	/**生成一副扑克牌
	 * 0-12   方块2,3,4,5....10,J,Q,K,A
	 * 13-25 梅花2,3,4,5....10,J,Q,K,A
	 * 26-38 红桃2,3,4,5....10,J,Q,K,A
	 * 39-51 黑桃2,3,4,5....10,J,Q,K,A
	 * */
	public static List<Card> genCardResource(){
		List<Card> cardList = new ArrayList<Card>();
		for(int i = 0; i < CARD_MAX_VALUE; i++){
			Card card = new Card();
			card.setCardIndex(i);
			CardSuitEnum suitEnum = CardSuitEnum.getCardSuitBySuit(i/13 + 1);
			card.setCardSuit(suitEnum.suit);
			card.setCardSuitName(suitEnum.name);
			card.setCardValue(i%13 + 2);
			cardList.add(card);
		}
		return cardList;
	}
	
	/**
	 * 随机生成一张牌，并且将此牌从扑克牌中去除
	 * @param cardResource 一副52张的扑克牌
	 * @param min 牌索引最小值
	 * @param max 牌索引最大值
	 * @return
	 */
	public static Card genCard(List<Card> cardResource, int min, int max){
        Random random = new Random();
        /**随机生成min - max之间的一个数*/
        int s = random.nextInt(max)%(max-min+1) + min;
        Card tempCard = cardResource.get(s);
        Card card = tempCard.copy();
        cardResource.remove(s);
        return card;
	}
	
	/**
	 * 模拟发牌，每个玩家发3张，一张一张的发牌
	 * @param playerNum 玩家数
	 * @return
	 */
	public static List<List<Card>> dealCards(int playerNum){
		
		List<List<Card>> playerCards = new ArrayList<List<Card>>();
		for(int j = 0; j < playerNum; j++){
			playerCards.add(new ArrayList<Card>());
		}
		List<Card> cardResource = genCardResource();
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < playerNum; j++){
				Card card = genCard(cardResource, 0, cardResource.size());
				playerCards.get(j).add(card);
			}
		}
		JhCardRule.rankCards(playerCards);
		return playerCards;
	}
	
	
	public static void main(String[] args) {
	//	genCardResource();
		dealCards(5);
	}
}
