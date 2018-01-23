package cn.worldwalker.game.wyqp.ddz.handler;


import cn.worldwalker.game.wyqp.ddz.service.CardService;
import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;

import java.util.List;

/**
 * Created by zhangmin on 2018/1/12.
 */
public abstract class CardsHandlerChain {

    CardService cardService = CardService.getInstance();

    private CardsHandlerChain nextChain;

    public abstract CardUnion getCardsType(List<DdzCard> ddzCardList);

    public CardUnion handler(List<DdzCard> ddzCardList) {
        CardUnion cardsInfo = getCardsType(ddzCardList);
        if ( cardsInfo !=  null && !cardsInfo.equals(IllegalCardUnion.getInstance())){
            return cardsInfo;
        }else {
            return nextChain.handler(ddzCardList);
        }
    }

    public void setNextChain(CardsHandlerChain nextChain) {
        this.nextChain = nextChain;
    }
}
