package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.card.union.BaseCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;

import java.util.List;

public class BasicCardsHandler extends CardsHandlerChain {
    @Override
    public CardUnion getCardsType(List<DdzCard> ddzCardList) {
        if (ddzCardList != null && ddzCardList.size() > 0){
            int value = ddzCardList.get(0).getValue();
            for (DdzCard ddzCard : ddzCardList){
                if (ddzCard.getValue() != value){
                    return IllegalCardUnion.getInstance();
                }
            }
            return new BaseCardUnion(value, ddzCardList.size());
        }
        return IllegalCardUnion.getInstance();
    }
}
