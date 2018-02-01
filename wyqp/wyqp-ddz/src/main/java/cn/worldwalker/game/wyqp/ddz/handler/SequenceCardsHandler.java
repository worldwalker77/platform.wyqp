package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.union.BaseCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.ShunCardUnion;

import java.util.List;

public class SequenceCardsHandler extends CardsHandlerChain {

    @Override
    public CardUnion getCardsType(List<Integer> ddzCardList) {
        List<BaseCardUnion> baseCardUnionList = cardService.getBaseList(ddzCardList);
        List<ShunCardUnion> shunCardUnionList = cardService.getShunList(baseCardUnionList);

        ShunCardUnion shunCardUnion = null;
        if (shunCardUnionList.size() > 0 ) {
            shunCardUnion = shunCardUnionList.get(shunCardUnionList.size()-1);
        }

        if (shunCardUnion != null && shunCardUnion.getCardList().size() == ddzCardList.size()){
           return shunCardUnion;
        }else {
            return IllegalCardUnion.getInstance();
        }

    }

}
