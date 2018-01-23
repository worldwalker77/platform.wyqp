package cn.worldwalker.game.wyqp.ddz.service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.ddz.common.DdzMsg;
import cn.worldwalker.game.wyqp.ddz.common.DdzPlayerInfo;
import cn.worldwalker.game.wyqp.ddz.common.DdzRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ddzGameService")
public class DdzGameService extends BaseGameService {
    @Override
    public BaseRoomInfo doCreateRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        DdzMsg msg = (DdzMsg) request.getMsg();
        DdzRoomInfo ddzRoomInfo = new DdzRoomInfo();
        ddzRoomInfo.setGameType(GameTypeEnum.ddz.gameType);
        ddzRoomInfo.setRoomBankerId(msg.getPlayerId());
        List playerList = ddzRoomInfo.getPlayerList();
        DdzPlayerInfo ddzPlayerInfo = new DdzPlayerInfo();
        playerList.add(ddzPlayerInfo);
        return ddzRoomInfo;
    }

    @Override
    public BaseRoomInfo doEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        BaseMsg msg = request.getMsg();
        DdzRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(msg.getRoomId(), DdzRoomInfo.class);
        List playerList = roomInfo.getPlayerList();
        DdzPlayerInfo player = new DdzPlayerInfo();
        playerList.add(player);
        return roomInfo;
    }

    @Override
    public BaseRoomInfo getRoomInfo(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        Integer roomId = userInfo.getRoomId();
        return redisOperationService.getRoomInfoByRoomId(roomId, DdzRoomInfo.class);
    }

    @Override
    public List<BaseRoomInfo> doRefreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
        return null;
    }
}
