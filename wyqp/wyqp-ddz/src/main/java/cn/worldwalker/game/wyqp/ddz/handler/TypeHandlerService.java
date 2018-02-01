package cn.worldwalker.game.wyqp.ddz.handler;

import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.service.CardService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmin on 2018/1/12.
 */
public class TypeHandlerService {

    private CardsHandlerChain baseCardHandler;
    private static TypeHandlerService ourInstance = new TypeHandlerService();

    public static TypeHandlerService getInstance() {
        return ourInstance;
    }

    private TypeHandlerService() {
        baseCardHandler = new BasicCardsHandler();
        CardsHandlerChain sequenceCardsHandler = new SequenceCardsHandler();
        CardsHandlerChain withCardsHandler = new WithCardsHandler();
        CardsHandlerChain feiJiCardHandler = new FeijiCardsHandler();

        baseCardHandler.setNextChain(withCardsHandler);
        withCardsHandler.setNextChain(sequenceCardsHandler);
        sequenceCardsHandler.setNextChain(feiJiCardHandler);
    }

    public CardUnion getCardType(List<Integer> cardList){
        return baseCardHandler.handler(cardList);
    }


    public static void main(String[] args){
        TypeHandlerService instance = getInstance();
        CardService cardService = CardService.getInstance();

        List<Integer> list = new ArrayList<>(54);
        for (int i =0; i<54; i++){
            list.add(i);
        }

        List<CardUnion> cardUnionList = cardService.getUnionList(list);
        for (CardUnion cardUnion : cardUnionList){
            CardUnion cardUnion1 = instance.getCardType(cardUnion.getCardList());
            System.out.println("[A] : " + cardUnion.getClass() + "," + cardUnion.toString());
            System.out.println("[B] : " + cardUnion1.getClass() + "," + cardUnion1.toString());
        }
    }

}
