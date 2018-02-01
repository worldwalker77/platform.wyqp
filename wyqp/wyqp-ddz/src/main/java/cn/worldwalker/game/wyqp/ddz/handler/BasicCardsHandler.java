package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.union.BaseCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;

import java.util.List;

public class BasicCardsHandler extends CardsHandlerChain {
    @Override
    public CardUnion getCardsType(List<Integer> cardList) {
        CardUnion cardUnion = BaseCardUnion.valueOf(cardList);
        if (cardUnion == null){
           cardUnion = IllegalCardUnion.getInstance();
        }
        return cardUnion;
    }
}
