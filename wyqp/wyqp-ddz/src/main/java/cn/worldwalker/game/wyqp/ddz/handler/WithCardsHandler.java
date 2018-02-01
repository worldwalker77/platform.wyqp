package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.union.BaseCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.DaiCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;

import java.util.List;

public class WithCardsHandler extends CardsHandlerChain {
    @Override
    public CardUnion getCardsType(List<Integer> cardList) {
        List<BaseCardUnion> baseCardUnionList = cardService.getBaseList(cardList);
        List<DaiCardUnion> daiCardUnionList = cardService.getDaiList(baseCardUnionList);

        DaiCardUnion daiCardUnion = null;
        if (daiCardUnionList.size() > 0 ) {
            daiCardUnion = daiCardUnionList.get(daiCardUnionList.size()-1);
        }

        if (daiCardUnion != null && daiCardUnion.getCardList().size() == cardList.size()){
            return daiCardUnion;
        }else {
            return IllegalCardUnion.getInstance();
        }
       /*
        int size = baseCardUnionList.size();
        Collections.sort(baseCardUnionList);
        BaseCardUnion maxBasicCards = baseCardUnionList.get(baseCardUnionList.size()-1);
        BaseCardUnion firstBasicCards = baseCardUnionList.get(0);
        BaseCardUnion secondBasicCards = baseCardUnionList.get(1);

        if ( maxBasicCards.getCardList().size() == 3 && size == 2
                && firstBasicCards.getCardList().size() == 1 ){
            return new DaiCardUnion(maxBasicCards,baseCardUnionList.subList(0,2));
        } else if (size == 3){
            if (maxBasicCards.getCardList().size() == 4 && firstBasicCards.getCardList().size() == 1
                    && secondBasicCards.getCardList().size() == 1){
                return new DaiCardUnion(maxBasicCards,baseCardUnionList.subList(0,1));
            }
        }
        return IllegalCardUnion.getInstance();
*/
    }
}
