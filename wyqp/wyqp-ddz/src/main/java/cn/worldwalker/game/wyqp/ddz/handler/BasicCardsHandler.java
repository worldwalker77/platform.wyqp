package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.union.BaseCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;
import cn.worldwalker.game.wyqp.ddz.service.CardService;

import java.util.List;

public class BasicCardsHandler extends CardsHandlerChain {
    private CardService cardService = CardService.getInstance();
    @Override
    public CardUnion getCardsType(List<Integer> cardList) {
        if (cardList != null && cardList.size() > 0){
            int value0 = cardService.cardValue(cardList.get(0));
            for (Integer index : cardList){
               int value = cardService.cardValue(index);
                if (value != value0){
                    return IllegalCardUnion.getInstance();
                }
            }
            return new BaseCardUnion(cardList);
        }
        return IllegalCardUnion.getInstance();
    }
}
