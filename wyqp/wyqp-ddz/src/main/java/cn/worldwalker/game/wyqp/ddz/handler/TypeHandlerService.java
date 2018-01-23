package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;

import java.util.List;

/**
 * Created by zhangmin on 2018/1/12.
 */
public class TypeHandlerService {

    private CardsHandlerChain typeHandlerChain;
    private static TypeHandlerService ourInstance = new TypeHandlerService();

    public static TypeHandlerService getInstance() {
        return ourInstance;
    }

    private TypeHandlerService() {
        typeHandlerChain = new BasicCardsHandler();
        CardsHandlerChain sequenceCardsHandler = new SequenceCardsHandler();
        CardsHandlerChain withCardsHandler = new WithCardsHandler();
        sequenceCardsHandler.setNextChain(withCardsHandler);
        typeHandlerChain.setNextChain(sequenceCardsHandler);
    }

    public CardUnion getCardType(List<DdzCard> ddzCardList){
        return typeHandlerChain.getCardsType(ddzCardList);
    }

}
