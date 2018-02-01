package cn.worldwalker.game.wyqp.ddz.service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.PlayerStatusEnum;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import cn.worldwalker.game.wyqp.common.utils.GameUtil;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.IllegalCardUnion;
import cn.worldwalker.game.wyqp.ddz.card.union.UnionComparator;
import cn.worldwalker.game.wyqp.ddz.common.DdzPlayerInfo;
import cn.worldwalker.game.wyqp.ddz.common.DdzRequest;
import cn.worldwalker.game.wyqp.ddz.common.DdzRoomInfo;
import cn.worldwalker.game.wyqp.ddz.common.GameStatusEnum;
import cn.worldwalker.game.wyqp.ddz.handler.TypeHandlerService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("ddzGameService")
public class DdzGameService extends BaseGameService {

    private RoomService roomService = RoomService.getInstance();
    private PlayerService playerService = PlayerService.getInstance();
    private CardService cardService = CardService.getInstance();
    private TypeHandlerService typeHandlerService = TypeHandlerService.getInstance();

    private final static Log logger = LogFactory.getLog(DdzGameService.class);

    @Override
    public BaseRoomInfo doCreateRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        DdzRoomInfo ddzRoomInfo = new DdzRoomInfo();
        ddzRoomInfo.setGameType(GameTypeEnum.ddz.gameType);
        ddzRoomInfo.setRoomBankerId(userInfo.getPlayerId());
        ddzRoomInfo.getPlayerList().add(new DdzPlayerInfo());
        return ddzRoomInfo;
    }

    @Override
    public BaseRoomInfo doEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        BaseMsg msg = request.getMsg();
        DdzRoomInfo ddzRoomInfo = redisOperationService.getRoomInfoByRoomId(msg.getRoomId(), DdzRoomInfo.class);
        ddzRoomInfo.getPlayerList().add(new DdzPlayerInfo());
        return ddzRoomInfo;
    }

    @Override
    public BaseRoomInfo getRoomInfo(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        return redisOperationService.getRoomInfoByRoomId(userInfo.getRoomId(), DdzRoomInfo.class);
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
        if (ddzRoomInfo.getPlayerList().size() == 3
                && roomService.isAllPlayerDone(ddzRoomInfo, PlayerStatusEnum.ready)) {
            roomService.dealCard(ddzRoomInfo);
            //通知前端
            for (DdzPlayerInfo ddzPlayerInfo1 : ddzRoomInfo.getPlayerList()) {
                Result result1 = new Result(request.getGameType(), MsgTypeEnum.dealCards.msgType);
                JSONObject jsonData = new JSONObject(2);
                jsonData.put("cardList", ddzPlayerInfo1.getDdzCardList());
                result1.setData(jsonData);
                channelContainer.sendTextMsgByPlayerIds(result1, ddzPlayerInfo1.getPlayerId());
            }
            ddzRoomInfo.setGameStatusEnum(GameStatusEnum.CALL);
            //todo 指定该谁叫地主
            ddzRoomInfo.setCurPlayerId(userInfo.getPlayerId());
        }
        //每次有修改数据，难道都要保存一下吗？
        redisOperationService.setRoomIdRoomInfo(ddzRoomInfo.getRoomId(), ddzRoomInfo);

    }

    public void callLandlord(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {

        DdzRoomInfo ddzRoomInfo = redisOperationService.getRoomInfoByRoomId(userInfo.getRoomId(), DdzRoomInfo.class);
        if (!userInfo.getPlayerId().equals(ddzRoomInfo.getCurPlayerId())) {
            throw new BusinessException(ExceptionEnum.IS_NOT_YOUR_TURN);
        }

        JSONObject jsonDdzMsg = JSON.parseObject(((DdzRequest) request).getDdzMsg());
        int score = jsonDdzMsg.getInteger("score");
        if ((score < ddzRoomInfo.getLastCallScore() && score != 0) || score > 3) {
            //合法性校验
            throw new BusinessException(ExceptionEnum.STAKE_SCORE_ERROR_1);
        }

        DdzPlayerInfo ddzPlayerInfo = roomService.getPlayerInfo(ddzRoomInfo, userInfo.getPlayerId());
        ddzPlayerInfo.setStatus(PlayerStatusEnum.called.status);
        Result result = new Result(request);
        //叫地主结束
        if (score == 3 || roomService.isAllPlayerDone(ddzRoomInfo, PlayerStatusEnum.called)) {
            ddzRoomInfo.setLandlord(userInfo.getPlayerId());
            ddzRoomInfo.setScore(score);
            ddzRoomInfo.setGameStatusEnum(GameStatusEnum.PLAY);
            ddzRoomInfo.setCurPlayerId(userInfo.getPlayerId());

            for (Integer restCard : ddzRoomInfo.getRestCardList()) {
                ddzPlayerInfo.getDdzCardList().add(restCard);
            }
            playerService.sortCard(ddzPlayerInfo);

            result.setMsgType(403);
            JSONObject jsonData = new JSONObject(5);
            jsonData.put("landlord", userInfo.getPlayerId());
            jsonData.put("score", score);
            //todo 这个逻辑要加上
            jsonData.put("restCardList", ddzRoomInfo.getRestCardList());
            result.setData(jsonData);
        } else {
            //继续叫地主
            ddzRoomInfo.setLastCaller(userInfo.getPlayerId());
            ddzRoomInfo.setLastCallScore(score);
            Integer nextPlayerId = GameUtil.getNextPlayerId(ddzRoomInfo.getPlayerList(), userInfo.getPlayerId());
            ddzRoomInfo.setCurPlayerId(nextPlayerId);

            JSONObject jsonData = new JSONObject(5);
            jsonData.put("lastCaller", ddzRoomInfo.getLastCaller());
            jsonData.put("lastCallScore", ddzRoomInfo.getLastCallScore());
            jsonData.put("curCaller", ddzRoomInfo.getCurPlayerId());
            result.setData(jsonData);
        }

        redisOperationService.setRoomIdRoomInfo(ddzRoomInfo.getRoomId(), ddzRoomInfo);

        for (DdzPlayerInfo ddzPlayerInfo1 : ddzRoomInfo.getPlayerList()) {
            channelContainer.sendTextMsgByPlayerIds(result, ddzPlayerInfo1.getPlayerId());
        }

    }

    public void cue(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        DdzRoomInfo ddzRoomInfo = redisOperationService.getRoomInfoByRoomId(userInfo.getRoomId(), DdzRoomInfo.class);
        DdzPlayerInfo ddzPlayerInfo = roomService.getPlayerInfo(ddzRoomInfo, userInfo.getPlayerId());

        List<CardUnion> cardUnions = cardService.getUnionList(ddzPlayerInfo.getDdzCardList());
        List<CardUnion> cardUnionBomb = new ArrayList<>(cardUnions);
        List<CardUnion> cardUnionNormal = new ArrayList<>(cardUnions);
        for (CardUnion cardUnion : cardUnions) {
            if (cardUnion.isBomb()) {
                cardUnionBomb.add(cardUnion);
            } else {
                cardUnionNormal.add(cardUnion);
            }
        }

        //随便出，普通牌选择数量最多的，炸弹选择大小最小的
        List<Integer> playCardList = null;
        if (ddzRoomInfo.getLastCards().isEmpty() ||
                ddzRoomInfo.getLastCardsOwner().equals(ddzPlayerInfo.getPlayerId())) {
            if (!cardUnionNormal.isEmpty()) {
                Collections.sort(cardUnionNormal, UnionComparator.BY_SIZE);
                playCardList = cardUnionNormal.get(0).getCardList();
            } else if (!cardUnionBomb.isEmpty()) {
                Collections.sort(cardUnionBomb, UnionComparator.BY_VALUE);
                playCardList = cardUnionNormal.get(0).getCardList();
            }
        } else {
            //压桌面上的牌，选择第一个比桌面上的牌大的
            CardUnion curCardUnion = typeHandlerService.getCardType(ddzRoomInfo.getLastCards());
            if (curCardUnion.isBomb()) {
                if (!cardUnionBomb.isEmpty()) {
                    Collections.sort(cardUnionBomb, UnionComparator.BY_VALUE);
                    for (CardUnion cardUnion : cardUnionBomb) {
                        if (cardUnion.isBomb() && cardUnion.getValue() > curCardUnion.getValue()) {
                            playCardList = cardUnion.getCardList();
                            break;
                        }
                    }

                }
            } else {
                if (!cardUnionNormal.isEmpty()) {
                    Collections.sort(cardUnionNormal, UnionComparator.BY_VALUE);
                    for (CardUnion cardUnion : cardUnionNormal) {
                        if (cardUnion.getType().equals(curCardUnion.getType())
                                && cardUnion.getValue() > curCardUnion.getValue()) {
                            playCardList = cardUnion.getCardList();
                            break;
                        }
                    }
                } else if (!cardUnionBomb.isEmpty()) {
                    Collections.sort(cardUnionBomb, UnionComparator.BY_VALUE);
                    playCardList = cardUnionBomb.get(0).getCardList();
                }
            }
        }
        Result result = new Result(request.getGameType(), request.getMsgType());
        JSONObject jsonObject = new JSONObject(2);
        jsonObject.put("cueCardList", playCardList);
        result.setData(jsonObject);
        channelContainer.sendTextMsgByPlayerIds(result, userInfo.getPlayerId());

    }

    public void play(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {

        DdzRoomInfo ddzRoomInfo = redisOperationService.getRoomInfoByRoomId(userInfo.getRoomId(), DdzRoomInfo.class);
        logger.info("lastCards:" + ddzRoomInfo.getLastCards() + ", lastPlayer" + ddzRoomInfo.getLastCardsOwner()
                + ", curCard" + JSON.toJSONString(request) + ",playerId:" + userInfo.getPlayerId());

        if (!userInfo.getPlayerId().equals(ddzRoomInfo.getCurPlayerId())) {
            logger.error("not your turn:" + userInfo.getPlayerId() + "," + ddzRoomInfo.getCurPlayerId());
            throw new BusinessException(ExceptionEnum.DDZ_NOT_YOUR_TURN);
        }

        JSONObject jsonDdzMsg = JSON.parseObject(((DdzRequest) request).getDdzMsg());
        String playCards = jsonDdzMsg.getString("playCards");
        JSONArray jsonArray = (JSONArray) JSONObject.parse(playCards);
        List<Integer> cardList = new ArrayList<>(jsonArray.size());
        DdzPlayerInfo ddzPlayerInfo = roomService.getPlayerInfo(ddzRoomInfo, userInfo.getPlayerId());
        boolean isPlayCard = false, isNextPlay = false;
        CardUnion curCardUnion = null;
        if (!jsonArray.isEmpty()) {
            for (Object o : jsonArray) {
                cardList.add((Integer) o);
            }
            if (cardList.isEmpty()) {
                isNextPlay = true;
            } else {
                curCardUnion = typeHandlerService.getCardType(cardList);
                if (!(curCardUnion instanceof IllegalCardUnion)) {
                    if (ddzRoomInfo.getLastCards().isEmpty()) {
                        isPlayCard = true;
                        isNextPlay = true;
                    } else {
                        CardUnion lastCardUnion = typeHandlerService.getCardType(ddzRoomInfo.getLastCards());
                        if (userInfo.getPlayerId().equals(ddzRoomInfo.getLastCardsOwner())
                                || cardService.isBigger(curCardUnion, lastCardUnion)) {
                            isPlayCard = true;
                            isNextPlay = true;
                        } else {
                            channelContainer.sendErrorMsg(ctx, ExceptionEnum.DDZ_ILLEGAL_PLAY, request);
                        }
                    }
                }
            }

        } else {
            isNextPlay = true;
        }

        //是否出牌
        if (isPlayCard) {
            playerService.playCard(ddzPlayerInfo, cardList);
            if (curCardUnion.isBomb()) {
                ddzRoomInfo.setBombCnt(ddzRoomInfo.getBombCnt() + 1);
            }
            ddzRoomInfo.setLastCards(cardList);
            ddzRoomInfo.setLastCardsOwner(userInfo.getPlayerId());
        }
        //切换下个玩家
        if (isNextPlay) {
            Result result = new Result(request);
            JSONObject jsonObject = new JSONObject(2);
            jsonObject.put("playCardList", ddzRoomInfo.getLastCards());
            jsonObject.put("playerId", ddzRoomInfo.getLastCardsOwner());
            result.setData(jsonObject);
            for (DdzPlayerInfo ddzPlayerInfo1 : ddzRoomInfo.getPlayerList()) {
                channelContainer.sendTextMsgByPlayerIds(result, ddzPlayerInfo1.getPlayerId());
            }
            Integer nextPlayerId = GameUtil.getNextPlayerId(ddzRoomInfo.getPlayerList(), userInfo.getPlayerId());
            ddzRoomInfo.setCurPlayerId(nextPlayerId);
            logger.info("cur:" + userInfo.getPlayerId() + ",next" + nextPlayerId);
        }

        if (ddzPlayerInfo.getDdzCardList().isEmpty()) {
            Result result = new Result(request);
            result.setMsgType(MsgTypeEnum.curSettlement.msgType);
            int score = ddzRoomInfo.getScore() << ddzRoomInfo.getBombCnt() ;
            boolean isLandWin =  ddzRoomInfo.getLandlord().equals(userInfo.getPlayerId());
            for (DdzPlayerInfo ddzPlayerInfo1 : ddzRoomInfo.getPlayerList()) {
                JSONObject jsonObject = new JSONObject(1);
                if (isLandWin){
                    if (ddzPlayerInfo1.getPlayerId().equals(ddzRoomInfo.getLandlord())){
                        jsonObject.put("score", score*2);
                    }else {
                        jsonObject.put("score", 0-score);
                    }
                }else {
                    if (ddzPlayerInfo1.getPlayerId().equals(ddzRoomInfo.getLandlord())){
                        jsonObject.put("score", 0-score*2);
                    }else {
                        jsonObject.put("score", score);
                    }

                }
                jsonObject.put("playerId",ddzPlayerInfo1.getPlayerId());
                result.setData(jsonObject);
                channelContainer.sendTextMsgByPlayerIds(result, ddzPlayerInfo1.getPlayerId());
            }
        }

        //每次都要更新房间信息啊
        redisOperationService.setRoomIdRoomInfo(ddzRoomInfo.getRoomId(), ddzRoomInfo);
    }


}

