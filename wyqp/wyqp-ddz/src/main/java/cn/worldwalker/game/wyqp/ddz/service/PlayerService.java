package cn.worldwalker.game.wyqp.ddz.service;

import cn.worldwalker.game.wyqp.ddz.common.DdzPlayerInfo;

import java.util.Collections;
import java.util.List;

public class PlayerService {
    private static PlayerService instance = new PlayerService();

    private CardService cardService = CardService.getInstance();

    public static PlayerService getInstance(){
        return instance;
    }

    private PlayerService(){}

    public void addCard(DdzPlayerInfo ddzPlayerInfo , Integer card ){
        ddzPlayerInfo.getDdzCardList().add(card);
    }

    public void sortCard(DdzPlayerInfo ddzPlayerInfo){
        Collections.sort(ddzPlayerInfo.getDdzCardList());
//        , new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//
//                return cardService.cardValue(o1) - cardService.cardValue(o2);
//            }
//        });

    }

    public void playCard(DdzPlayerInfo ddzPlayerInfo, List<Integer> toPlayCardList){
        ddzPlayerInfo.getDdzCardList().removeAll(toPlayCardList);
    }
}
