package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.union.BaseCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.ShunCardUnion;

import java.util.Collections;
import java.util.List;

public class SequenceCardsHandler extends CardsHandlerChain {

    @Override
    public CardUnion getCardsType(List<Integer> ddzCardList) {
        List<BaseCardUnion> baseCardUnionList = cardService.getBaseList(ddzCardList);
        Collections.sort(baseCardUnionList);
        BaseCardUnion firstUnion = baseCardUnionList.get(0);
        int lastValue = firstUnion.getValue(), count = firstUnion.getCardList().size();
        for (int i=1; i<baseCardUnionList.size(); i++){
            BaseCardUnion baseCardUnion = baseCardUnionList.get(i);
            if ( baseCardUnion.getValue() == lastValue + 1 &&
                    baseCardUnion.getCardList().size() == count){
                lastValue = baseCardUnion.getValue();
            } else {
                return IllegalCardUnion.getInstance();
            }
        }
        return new ShunCardUnion(baseCardUnionList);

    }

}
