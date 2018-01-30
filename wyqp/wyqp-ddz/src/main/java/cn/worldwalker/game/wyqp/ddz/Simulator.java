package cn.worldwalker.game.wyqp.ddz;

import cn.worldwalker.game.wyqp.ddz.common.DdzPlayerInfo;
import cn.worldwalker.game.wyqp.ddz.common.DdzRoomInfo;
import cn.worldwalker.game.wyqp.ddz.service.RoomService;

public class Simulator {


    public static void main(String[] strings){

        DdzRoomInfo ddzRoomInfo = new DdzRoomInfo();
        DdzPlayerInfo ddzPlayerInfo1 = new DdzPlayerInfo();
        ddzPlayerInfo1.setPlayerId(10001);
        DdzPlayerInfo ddzPlayerInfo2 = new DdzPlayerInfo();
        ddzPlayerInfo2.setPlayerId(10002);
        DdzPlayerInfo ddzPlayerInfo3 = new DdzPlayerInfo();
        ddzPlayerInfo3.setPlayerId(10003);
        ddzRoomInfo.getPlayerList().add(ddzPlayerInfo1);
        ddzRoomInfo.getPlayerList().add(ddzPlayerInfo2);
        ddzRoomInfo.getPlayerList().add(ddzPlayerInfo3);

        RoomService.getInstance().dealCard(ddzRoomInfo);
        ddzRoomInfo.setLandlord(0);

        PlayerSimulator playerSimulator1 = new PlayerSimulator(ddzPlayerInfo1,ddzRoomInfo,ddzPlayerInfo2.getPlayerId());
        PlayerSimulator playerSimulator2 = new PlayerSimulator(ddzPlayerInfo2,ddzRoomInfo,ddzPlayerInfo3.getPlayerId());
        PlayerSimulator playerSimulator3 = new PlayerSimulator(ddzPlayerInfo3,ddzRoomInfo,ddzPlayerInfo1.getPlayerId());
        ddzRoomInfo.setCurPlayerId(ddzPlayerInfo1.getPlayerId());

//        new Thread(playerSimulator1).start();
//        new Thread(playerSimulator2).start();
//        new Thread(playerSimulator3).start();
//
    }
}
