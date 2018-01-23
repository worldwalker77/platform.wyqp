package cn.worldwalker.game.wyqp.mj.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.worldwalker.game.wyqp.common.domain.base.Card;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;
import cn.worldwalker.game.wyqp.mj.enums.MjCardSuitEnum;

public class CardResource {
	
	/**总共136张牌*/
	private static final Integer CARD_MAX_VALUE = 136;
	/**每个玩家的牌数*/
	private static final int CARD_NUM_PER_PLAYER = 13;
	/**牌循环数9*/
	private static final int CARD_LOOP_NINE = 9;
	/**牌循环数7*/
	private static final int CARD_LOOP_SEVEN = 7;
	
	/**生成一副麻将
	 * 0-8  筒 1,2,3,4,5....9
	 * 9-17 筒 1,2,3,4,5....9
	 * 18-26 筒 1,2,3,4,5....9
	 * 27-35筒 1,2,3,4,5....9
	 * 
	 * 36-44条 1,2,3,4,5....9
	 * 45-53条 1,2,3,4,5....9
	 * 54-62条 1,2,3,4,5....9
	 * 63-71条 1,2,3,4,5....9
	 * 
	 * 72-80万 1,2,3,4,5....9
	 * 81-89万 1,2,3,4,5....9
	 * 90-98万 1,2,3,4,5....9
	 * 99-107万 1,2,3,4,5....9
	 * 
	 * 108-114 东南西北中发白
	 * 115-121东南西北中发白
	 * 122-128东南西北中发白
	 * 129-135东南西北中发白
	 * 
	 * */
	public static List<Card> genCardResource(){
		List<Card> cardList = new ArrayList<Card>();
		for(int i = 0; i < CARD_MAX_VALUE; i++){
			Card card = new Card();
			card.setCardIndex(i);
			if (i <= 35) {
				card.setCardSuit(MjCardSuitEnum.TONG.suit);
				card.setCardSuitName(MjCardSuitEnum.TONG.name);
				card.setCardValue(i%CARD_LOOP_NINE + 1);
			}else if(i >= 36 && i <= 71){
				card.setCardSuit(MjCardSuitEnum.TIAO.suit);
				card.setCardSuitName(MjCardSuitEnum.TIAO.name);
				card.setCardValue(i%CARD_LOOP_NINE + 1);
			}else if(i >= 72 && i <= 107){
				card.setCardSuit(MjCardSuitEnum.WAN.suit);
				card.setCardSuitName(MjCardSuitEnum.WAN.name);
				card.setCardValue(i%CARD_LOOP_NINE + 1);
			}else{
				card.setCardSuit(MjCardSuitEnum.FENG.suit);
				card.setCardSuitName(MjCardSuitEnum.FENG.name);
				card.setCardValue((i - 108)%CARD_LOOP_SEVEN + 1);
			}
			
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
	 * 模拟发牌，每个玩家发13张，一张一张的发牌
	 * @param playerNum 玩家数
	 * @return
	 */
	public static List<List<Card>> dealCards(int playerNum){
		
		List<List<Card>> playerCards = new ArrayList<List<Card>>();
		for(int j = 0; j < playerNum; j++){
			playerCards.add(new ArrayList<Card>());
		}
		List<Card> cardResource = genCardResource();
		for(int i = 0; i < CARD_NUM_PER_PLAYER; i++){
			for(int j = 0; j < playerNum; j++){
				Card card = genCard(cardResource, 0, cardResource.size());
				playerCards.get(j).add(card);
			}
		}
		CardRule.rankCards(playerCards);
		return playerCards;
	}
	
	
	public static void main(String[] args) {
	//	genCardResource();
	}
}
