package cn.worldwalker.game.wyqp.ddz.service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.PlayerStatusEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.common.DdzMsg;
import cn.worldwalker.game.wyqp.ddz.common.DdzPlayerInfo;
import cn.worldwalker.game.wyqp.ddz.common.DdzRoomInfo;
import cn.worldwalker.game.wyqp.ddz.common.GameStatusEnum;
import cn.worldwalker.game.wyqp.ddz.handler.TypeHandlerService;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("ddzGameService")
public class DdzGameService extends BaseGameService {

    private RoomService roomService = RoomService.getInstance();

    private CardService cardService = CardService.getInstance();

    private TypeHandlerService typeHandlerService = TypeHandlerService.getInstance();

    private Random random = new Random();

    @Override
    public BaseRoomInfo doCreateRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
//        DdzMsg msg = (DdzMsg) request.getMsg();
        DdzRoomInfo ddzRoomInfo = new DdzRoomInfo();
        ddzRoomInfo.setGameType(GameTypeEnum.ddz.gameType);
        ddzRoomInfo.setRoomBankerId(userInfo.getPlayerId());
        List playerList = ddzRoomInfo.getPlayerList();

        DdzPlayerInfo ddzPlayerInfo = new DdzPlayerInfo();
//        ddzPlayerInfo.setPlayerId(userInfo.getPlayerId());
        playerList.add(ddzPlayerInfo);

        return ddzRoomInfo;
    }

    @Override
    public BaseRoomInfo doEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        BaseMsg msg = request.getMsg();
        DdzRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(msg.getRoomId(), DdzRoomInfo.class);
        List playerList = roomInfo.getPlayerList();

        DdzPlayerInfo ddzPlayerInfo = new DdzPlayerInfo();
        playerList.add(ddzPlayerInfo);
        return roomInfo;
    }

    @Override
    public BaseRoomInfo getRoomInfo(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        Integer roomId = userInfo.getRoomId();
        return redisOperationService.getRoomInfoByRoomId(roomId, DdzRoomInfo.class);
    }

    @Override
    public List<BaseRoomInfo> doRefreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        List<BaseRoomInfo> roomInfoList = new ArrayList<>();
        Integer roomId = userInfo.getRoomId();
        DdzRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, DdzRoomInfo.class);
//        DdzRoomInfo ddzRoomInfo = new DdzRoomInfo();
        roomInfoList.add(roomInfo);
        roomInfoList.add(roomInfo);

        return roomInfoList;
    }

    public void ready(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        DdzRoomInfo ddzRoomInfo = redisOperationService.getRoomInfoByRoomId(userInfo.getRoomId(), DdzRoomInfo.class);
        DdzPlayerInfo ddzPlayerInfo = roomService.getPlayerInfo(ddzRoomInfo, userInfo.getPlayerId());
        ddzPlayerInfo.setStatus(PlayerStatusEnum.ready.status);
        Result result = new Result(request);
        channelContainer.sendTextMsgByPlayerIds(result, ddzPlayerInfo.getPlayerId());

        //如果都准备了，就发牌
        if (ddzRoomInfo.getPlayerList().size() == 3 && roomService.isAllPlayerReady(ddzRoomInfo)){
            roomService.dealCard(ddzRoomInfo);
            //通知前端
            for (DdzPlayerInfo ddzPlayerInfo1 : ddzRoomInfo.getPlayerList()){
                Result result1 = new Result(request.getGameType(), MsgTypeEnum.dealCards.msgType);
                JSONObject jsonData = new JSONObject(2);
                jsonData.put("cardList",ddzPlayerInfo1.getDdzCardList());
                result1.setData(jsonData);
                channelContainer.sendTextMsgByPlayerIds(result1, ddzPlayerInfo1.getPlayerId());
            }
            ddzRoomInfo.setGameStatusEnum(GameStatusEnum.RUN);
            ddzRoomInfo.setCurPlayer(userInfo.getPlayerId());
        }
        //每次有修改数据，难道都要保存一下吗？
        redisOperationService.setRoomIdRoomInfo(ddzRoomInfo.getRoomId(), ddzRoomInfo);

    }

    public void cue(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
        DdzRoomInfo ddzRoomInfo = redisOperationService.getRoomInfoByRoomId(userInfo.getRoomId(), DdzRoomInfo.class);

        DdzPlayerInfo ddzPlayerInfo = roomService.getPlayerInfo(ddzRoomInfo, userInfo.getPlayerId());
        List<CardUnion> cardUnions = cardService.getUionList(ddzPlayerInfo.getDdzCardList());
        List<DdzCard> playCardList = null ;
        if (ddzRoomInfo.getCurCards().isEmpty() ||
                ddzRoomInfo.getCurCardsOwner() == ddzPlayerInfo.getPlayerId()){
            playCardList = cardUnions.get(random.nextInt(cardUnions.size())).generateCardList();
        }else {
            CardUnion curCardUnion = typeHandlerService.getCardType(ddzRoomInfo.getCurCards());
            for (CardUnion cardUnion: cardUnions){
                if (cardUnion.getType().equals(curCardUnion.getType())
                        && cardUnion.getValue() > curCardUnion.getValue()){
                    playCardList = cardUnion.generateCardList();
                    break;
                }
            }
        }
        Result result = new Result(request.getGameType(), request.getMsgType());
        JSONObject jsonObject = new JSONObject(2);
        jsonObject.put("cueCardList",playCardList);
        result.setData(jsonObject);
        channelContainer.sendTextMsgByPlayerIds(result,userInfo.getPlayerId());

    }

    public void play(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
        DdzMsg ddzMsg = (DdzMsg) request.getMsg();
        System.out.println(ddzMsg.getPlayCards());
//        DdzRoomInfo ddzRoomInfo = redisOperationService.getRoomInfoByRoomId(userInfo.getRoomId(), DdzRoomInfo.class);
//        DdzPlayerInfo ddzPlayerInfo = roomService.getPlayerInfo(ddzRoomInfo, userInfo.getPlayerId());


    }


}

