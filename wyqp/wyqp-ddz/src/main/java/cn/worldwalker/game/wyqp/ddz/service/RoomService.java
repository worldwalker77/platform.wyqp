package cn.worldwalker.game.wyqp.ddz.service;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.common.DdzPlayerInfo;
import cn.worldwalker.game.wyqp.ddz.common.DdzRoomInfo;
import cn.worldwalker.game.wyqp.ddz.common.GameStatusEnum;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RoomService {

    private static RoomService instance = new RoomService();

    public static RoomService getInstance(){
        return instance;
    }

    private RoomService(){}

    private final PlayerService playerService = PlayerService.getInstance();

    public void dealCard(DdzRoomInfo ddzRoomInfo){
        if (GameStatusEnum.WAIT.equals(ddzRoomInfo.getGameStatusEnum())){
            List playerList = ddzRoomInfo.getPlayerInfoList();
            if (playerList.size() != 3){
                throw new IllegalArgumentException("num of players is" + playerList.size());
            }
            //一副牌
            List<DdzCard> allDdzCards = new LinkedList<>();
            for (int i=0; i<4; i++){
                for (int j=0;j<13;j++){
                    allDdzCards.add(new DdzCard(j+2));
                }
            }
            //洗牌
            Collections.shuffle(allDdzCards);
            //发牌，三家
            for (int i = 0; i< allDdzCards.size(); i++){
                playerService.addCard((DdzPlayerInfo) playerList.get(i%3),allDdzCards.get(i));
            }

        } else {
            throw new IllegalArgumentException("已经开始");
        }
        ddzRoomInfo.setGameStatusEnum(GameStatusEnum.RUN);
    }

    public void dealRestCard(){

    }

    public void start(){

    }


}
