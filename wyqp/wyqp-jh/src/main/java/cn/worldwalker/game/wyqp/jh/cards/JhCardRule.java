package cn.worldwalker.game.wyqp.jh.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;

import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.Card;
import cn.worldwalker.game.wyqp.common.domain.jh.JhPlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.jh.JhRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.PlayerStatusEnum;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;
import cn.worldwalker.game.wyqp.jh.enums.JhCardTypeEnum;
import cn.worldwalker.game.wyqp.jh.enums.JhPlayerStatusEnum;

public class JhCardRule {
	
	/**
	 * 计算牌型
	 * @param cardList
	 * @return
	 */
	public static Integer calculateCardType(List<Card> cardList){
		Card card0 = cardList.get(0);
		Card card1 = cardList.get(1);
		Card card2 = cardList.get(2);
		/**炸弹*/
		if (card0.getCardValue().equals(card1.getCardValue()) && card1.getCardValue().equals(card2.getCardValue())) {
			return JhCardTypeEnum.BOMB.cardType;
		}
		/**同花顺*/
		if ((card1.getCardValue().equals(card0.getCardValue() + 1) && card2.getCardValue().equals(card1.getCardValue() + 1)
			|| card0.getCardValue() == 14 && card1.getCardValue() == 2 && card2.getCardValue() == 3)
			&& card1.getCardSuit().equals(card0.getCardSuit()) && card2.getCardSuit().equals(card1.getCardSuit())) {
			return JhCardTypeEnum.FLUSH.cardType;
		}
		/**金花*/
		if (card1.getCardSuit().equals(card0.getCardSuit()) && card2.getCardSuit().equals(card1.getCardSuit())) {
			return JhCardTypeEnum.JINHUA.cardType;
		}
		/**普通顺子*/
		if (card1.getCardValue().equals(card0.getCardValue() + 1) && card2.getCardValue().equals(card1.getCardValue() + 1)
			|| card0.getCardValue() == 14 && card1.getCardValue() == 2 && card2.getCardValue() == 3) {
			return JhCardTypeEnum.STRAIGHT.cardType;
		}
		/**对子*/
		if (card0.getCardValue().equals(card1.getCardValue()) || card1.getCardValue().equals(card2.getCardValue()) || card0.getCardValue().equals(card2.getCardValue())) {
			return JhCardTypeEnum.PAIR.cardType;
		}
		/**单个*/
		return JhCardTypeEnum.SINGLE.cardType;
	}
	
	/**
	 * 对玩家的牌进行排序，从小到大
	 * @param playerCards
	 */
	public static void rankCards(List<List<Card>> playerCards){
		for(List<Card> cards : playerCards){
			int size = cards.size();
			for(int i = 0; i< size - 1; i++){
				for(int j = 0; j < size - 1 - i; j++){
					if (cards.get(j).getCardValue() > cards.get(j + 1).getCardValue()) {
						Card tempCard = cards.get(j);
						cards.set(j, cards.get(j + 1));
						cards.set(j + 1, tempCard);
					}else if(cards.get(j).getCardValue().equals(cards.get(j + 1).getCardValue())){
						if (cards.get(j).getCardSuit() >  cards.get(j + 1).getCardSuit()) {
							Card tempCard = cards.get(j);
							cards.set(j, cards.get(j + 1));
							cards.set(j + 1, tempCard);
						}
					}
				}
			}
		}
		/**处理顺子123这种特殊情况*/
		for(List<Card> cards : playerCards){
			Card card0 = cards.get(0);
			Card card1 = cards.get(1);
			Card card2 = cards.get(2);
			if (card0.getCardValue() == 2 && card1.getCardValue() == 3 && card2.getCardValue() == 14) {
				cards.set(2, card1);
				cards.set(1, card0);
				cards.set(0, card2);
			}
		}
	}
	
	/**
	 * 比较牌大小，返回赢家
	 * @param playerCards
	 */
	public static BasePlayerInfo comparePlayerCards(List<BasePlayerInfo> playerList){
		BasePlayerInfo maxPlayer = playerList.get(0);
		int playerNum = playerList.size();
		for(int k = 1; k < playerNum; k++){
			BasePlayerInfo curPlayer = playerList.get(k);
			if (compareTwoPlayerCards(maxPlayer, curPlayer) < 0) {
				maxPlayer = curPlayer;
			}
		}
		return maxPlayer;
	}
	
	public static void probabilityProcess(BaseRoomInfo roomInfo){
		List playerList = roomInfo.getPlayerList();
		int probabilityPlayerCount = 0;
		/**设置概率控制的玩家*/
		BasePlayerInfo probilityPlayer = null;
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo tempPlayer = (BasePlayerInfo)playerList.get(i);
			if (tempPlayer.getWinProbability() > 0) {
				probabilityPlayerCount++;
				probilityPlayer = tempPlayer;
			}
		}
		/**如果玩家都没有控制概率或者控制概率的玩家大于1家，则不处理*/
		if (probabilityPlayerCount != 1) {
			return;
		}
		/**如果概率控制的玩家是观察者，则不处理*/
		if (probilityPlayer.getStatus().equals(PlayerStatusEnum.observer.status)) {
			return;
		}
		
		/**当前第几局*/
		Integer curGame = roomInfo.getCurGame();
		/**如果玩家刚进入，则需要设置那几局需要概率控制*/
		if (probilityPlayer.getPlayedCount() == 0) {
			/**总局数*/
			Integer totalGames = roomInfo.getTotalGames();
			/**剩余局数*/
			Integer remainderGames = totalGames - curGame + 1;
			
			Integer remainder = remainderGames*probilityPlayer.getWinProbability()%100;
			/**需要概率控制的局数*/
			Integer needProbabilityGamesCount = remainderGames*probilityPlayer.getWinProbability()/100 + (remainder > 0 ? 1:0);
			
			probilityPlayer.setWinProbabilityGameIndexList(needProbabilityGameIndexList(curGame, totalGames + 1, needProbabilityGamesCount));
		}
		List<Integer> winProbabilityGameIndexList = probilityPlayer.getWinProbabilityGameIndexList();
		if (CollectionUtils.isEmpty(winProbabilityGameIndexList)) {
			return;
		}
		/**牌型最大的玩家*/
		BasePlayerInfo winPlayer = comparePlayerCards(playerList);
		/**如果牌型最大的玩家就是概率控制的玩家，则不处理*/
		if (winPlayer.getPlayerId().equals(probilityPlayer.getPlayerId())) {
			return;
		}
		/**如果列表中当前局数没有概率控制，则返回*/
		if (!winProbabilityGameIndexList.contains(curGame)) {
			return;
		}
		/**如果牌型最大的玩家不是概率控制的玩家，则将双方的牌互换，并将概率控制数量+1*/
		List<Card> winCardList = winPlayer.getCardList();
		Integer winCardType = winPlayer.getCardType();
		winPlayer.setCardList(probilityPlayer.getCardList());
		winPlayer.setCardType(probilityPlayer.getCardType());
		probilityPlayer.setCardList(winCardList);
		probilityPlayer.setCardType(winCardType);
	}
	public static void main(String[] args) {
		JhRoomInfo roomInfo = new JhRoomInfo();
		roomInfo.setCurGame(1);
		roomInfo.setTotalGames(8);
		JhPlayerInfo player1 = new JhPlayerInfo();
		player1.setPlayerId(111111);
		player1.setPlayedCount(0);
		player1.setWinProbability(100);
		player1.setStatus(JhPlayerStatusEnum.ready.status);
		player1.setCardType(3);
		JhPlayerInfo player2 = new JhPlayerInfo();
		player2.setPlayerId(222222);
		player2.setPlayedCount(0);
		player2.setWinProbability(0);
		player2.setStatus(JhPlayerStatusEnum.ready.status);
		player2.setCardType(1);
		roomInfo.getPlayerList().add(player1);
		roomInfo.getPlayerList().add(player2);
		probabilityProcess(roomInfo);
	}
    //从x-y中的数中随机找出num个不同的数，返回给integer的动态数组中  
    public static ArrayList<Integer> needProbabilityGameIndexList(int x, int y, int num)  
        {   
            //创建一个integer的动态数组  
            ArrayList<Integer> a = new ArrayList<Integer>();  
            if (y - x < num ) {
				return a;
			}
            int index = 0;  
            //往数组里面逐一加取到不重复的元素  
            while(index < num)  
            {  
            //产生x-y的随机数  
                Random r = new Random();  
                int temp = r.nextInt(y-x)+x ;  
            //设置是否重复的标记变量为false  
                boolean flag = false;  
                for(int i =0; i<index;i++)  
                {  
                    if(temp == a.get(i))  
                    {  
                        flag = true;  
                        break;  
                    }  
                }  
                if(flag==false)  
                {  
                    a.add(temp);  
                    index++;  
                }  
            }  
            return a;  
        }  
	/**
	 * 比较两个玩家牌的大小
	 * @param player1
	 * @param player2
	 * @return -1：player1 < player2 0：player1 == player2 1：player1 > player2
	 */
	public static int compareTwoPlayerCards(BasePlayerInfo player1, BasePlayerInfo player2){
		int result = 0;
		if (player1.getCardType() > player2.getCardType()) {
			result = 1;
		}else if(player1.getCardType().equals(player2.getCardType())){//如果牌型相同则比较每张牌的大小或花色
			JhCardTypeEnum cardTypeEnum = JhCardTypeEnum.getCardTypeEnum(player1.getCardType());
			switch (cardTypeEnum) {
				case BOMB:
					if (compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) > 0) {
						result = 1;
					}else{
						result = -1;
					}
					break;
				case FLUSH:
					if (compareTwoCardWithCardValueAndSuit(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
						result = 1;
					}else{
						result = -1;
					}
					break;
				case JINHUA:
					result = jinhuaCompare(player1, player2);
					break;
				case STRAIGHT:
					if (compareTwoCardWithCardValueAndSuit(player1.getCardList().get(2), player2.getCardList().get(2)) > 1) {
						result = 1;
					}else{
						result = -1;
					}
					break;
				case PAIR:
					result = pairCompare(player1, player2);
					break;
				case SINGLE:
					result = singleCompare(player1, player2);
					break;
	
				default:
					break;
			}
			
		}else{
			result =  -1;
		}
		
		return result;
	}
	
	/**
	 * 单个比较
	 * @param player1
	 * @param player2
	 * @return
	 */
	public static int singleCompare(BasePlayerInfo player1, BasePlayerInfo player2){
		int result = 0;
		/**如果三张牌的牌值相等，则志需要比较最大牌的花色*/
		if (compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) == 0 
			&& compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) == 0
			&& compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0) {
			if (compareTwoCardWithCardSuit(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**最小的两张牌值相等，则只需要比较最大的牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) == 0 
				&& compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**最大的两张牌值相等，则只需要比较最小的牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0 
				&& compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**最小的牌和最大的牌的牌值相等，则只需要比较中间牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) == 0 
				&& compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**最大的一张牌牌值相等，则只需要比较第二大的牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**其他情况，则只需要比较最大牌的牌值*/
		}else{
			if (compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
		}
		return result;
	}
	
	/**
	 * 金花比较
	 * @param player1
	 * @param player2
	 * @return
	 */
	public static int jinhuaCompare(BasePlayerInfo player1, BasePlayerInfo player2){
		int result = 0;
		/**如果三张牌的牌值相等，则志需要比较最大牌的花色*/
		if (compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) == 0 
			&& compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) == 0
			&& compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0) {
			if (compareTwoCardWithCardSuit(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**最小的两张牌值相等，则只需要比较最大的牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) == 0 
				&& compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**最大的两张牌值相等，则只需要比较最小的牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0 
				&& compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**最小的牌和最大的牌的牌值相等，则只需要比较中间牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) == 0 
				&& compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**最大的一张牌牌值相等，则只需要比较第二大的牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**其他情况，则只需要比较最大牌的牌值*/
		}else{
			if (compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
		}
		return result;
	}
	/**
	 * 对子比较
	 * @param player1
	 * @param player2
	 * @return
	 */
	public static int pairCompare(BasePlayerInfo player1, BasePlayerInfo player2){
		int result = 0;
		/**如果三张牌的牌值相等，则志需要比较最大牌的花色*/
		if (compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) == 0 
			&& compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) == 0
			&& compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) == 0) {
			
			if (compareTwoCardWithCardSuit(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**如果较小的两张牌牌值相等，则只需要比较最大的牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(0), player1.getCardList().get(1)) == 0 
				&& compareTwoCardWithCardValue(player2.getCardList().get(0), player2.getCardList().get(1)) == 0
				&& compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(2), player2.getCardList().get(2)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**如果较大的两张牌牌值相等，则只需要比较最小的牌的牌值*/
		}else if(compareTwoCardWithCardValue(player1.getCardList().get(1), player1.getCardList().get(2)) == 0 
				&& compareTwoCardWithCardValue(player2.getCardList().get(1), player2.getCardList().get(2)) == 0
				&& compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) == 0){
			if (compareTwoCardWithCardValue(player1.getCardList().get(0), player2.getCardList().get(0)) > 0) {
				result = 1;
			}else{
				result = -1;
			}
			/**其他情况直接比较对子大小,只需要比较牌列表中的第二个牌值就行*/
		}else{
			if (compareTwoCardWithCardValue(player1.getCardList().get(1), player2.getCardList().get(1)) > 0 ) {
				result = 1;
			}else{
				result = -1;
			}
		}
		return result;
	}
	
	
	/**
	 * 比较两张牌的大小(牌值和花色综合比较)
	 * @param card1
	 * @param card2
	 * @return
	 */
	public static int compareTwoCardWithCardValueAndSuit(Card card1, Card card2){
		if (card1.getCardValue() > card2.getCardValue()) {
			return 1;
		}else if(card1.getCardValue().equals(card2.getCardValue())){//如果牌值相等，则继续比较花色
			/**一副牌中牌值相同，花色肯定不同*/
			if (card1.getCardSuit() > card2.getCardSuit()) {
				return 1;
			}else{
				return -1;
			}
		}else{
			return -1;
		}
	}
	
	/**
	 * 比较两张牌的牌值大小
	 * @param card1
	 * @param card2
	 * @return
	 */
	public static int compareTwoCardWithCardValue(Card card1, Card card2){
		if (card1.getCardValue() > card2.getCardValue()) {
			return 1;
		}else if(card1.getCardValue().equals(card2.getCardValue())){//如果牌值相等，则继续比较花色
			return 0;
		}else{
			return -1;
		}
	}
	
	/**
	 * 比较两张牌的花色大小
	 * @param card1
	 * @param card2
	 * @return
	 */
	public static int compareTwoCardWithCardSuit(Card card1, Card card2){
		if (card1.getCardSuit() > card2.getCardSuit()) {
			return 1;
		}else{
			return -1;
		}
	}
	
}
