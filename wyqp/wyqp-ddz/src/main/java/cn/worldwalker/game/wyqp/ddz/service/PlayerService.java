package cn.worldwalker.game.wyqp.ddz.service;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.common.DdzPlayerInfo;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PlayerService {
    private static PlayerService instance = new PlayerService();

    public static PlayerService getInstance(){
        return instance;
    }

    private PlayerService(){}

    public void addCard(DdzPlayerInfo ddzPlayerInfo , DdzCard ddzCard){
        ddzPlayerInfo.getDdzCardList().add(ddzCard);
        Collections.sort(ddzPlayerInfo.getDdzCardList());
    }

    public void playCard(DdzPlayerInfo ddzPlayerInfo, List<DdzCard> toPlayCardList){
        for (DdzCard ddzCard : toPlayCardList){
            Iterator<DdzCard> it = ddzPlayerInfo.getDdzCardList().iterator();
            while (it.hasNext()) {
                DdzCard ddzCard1 = it.next();
                if (ddzCard.getValue() == ddzCard1.getValue()) {
                    it.remove();
                }
            }
        }

    }
}
