package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.union.*;
import cn.worldwalker.game.wyqp.ddz.service.CardService;

import java.util.List;

public class FeijiCardsHandler extends CardsHandlerChain {
    private CardService cardService = CardService.getInstance();
    @Override
    public CardUnion getCardsType(List<Integer> cardList) {
        List<BaseCardUnion> baseCardUnionList = cardService.getBaseList(cardList);
        List<ShunCardUnion> shunCardUnionList = cardService.getShunList2(baseCardUnionList);
        List<FeijiCardUnion> feijiCardUnionList = cardService.getFeiJiList(shunCardUnionList, baseCardUnionList);

        FeijiCardUnion feijiCardUnion = null;
        if (shunCardUnionList.size() > 0 ) {
            feijiCardUnion = feijiCardUnionList.get(feijiCardUnionList.size()-1);
        }

        if (feijiCardUnion!= null && feijiCardUnion.getCardList().size() == cardList.size()){
            return feijiCardUnion;
        }else {
            return IllegalCardUnion.getInstance();
        }

    }
}
