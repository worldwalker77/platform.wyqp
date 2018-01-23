package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.card.union.BaseCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.DaiCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;

import java.util.Collections;
import java.util.List;

public class WithCardsHandler extends CardsHandlerChain {
    @Override
    public CardUnion getCardsType(List<DdzCard> ddzCardList) {
        List<BaseCardUnion> baseCardUnionList = cardService.getBaseList(ddzCardList);
        int size = baseCardUnionList.size();
        Collections.sort(baseCardUnionList);
        BaseCardUnion maxBasicCards = baseCardUnionList.get(baseCardUnionList.size()-1);
        BaseCardUnion firstBasicCards = baseCardUnionList.get(0);
        BaseCardUnion secondBasicCards = baseCardUnionList.get(1);

        if ( maxBasicCards.getCount() == 3 && size == 2
                && firstBasicCards.getCount() == 1 ){
            return new DaiCardUnion(maxBasicCards,firstBasicCards,1);
        } else if (size == 3){
            if (maxBasicCards.getCount() == 4 && firstBasicCards.getCount() == 1
                    && secondBasicCards.getCount() == 1){
                return new DaiCardUnion(maxBasicCards,firstBasicCards,2);
            }
        }
        return IllegalCardUnion.getInstance();

    }
}
