package cn.worldwalker.game.wyqp.server.dispatcher;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.ddz.service.DdzGameService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service( value = "ddzMsgDispatcher")
public class DdzMsgDispatcher extends BaseMsgDisPatcher{

    @Autowired
    private DdzGameService ddzGameService;

    @Override
    public void requestDispatcher(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) throws Exception {
        Integer msgType = request.getMsgType();
        MsgTypeEnum msgTypeEnum= MsgTypeEnum.getMsgTypeEnumByType(msgType);
        switch (msgTypeEnum) {
            case createRoom:
               ddzGameService.createRoom(ctx,request,userInfo);
               break;
            case entryRoom:
                ddzGameService.entryRoom(ctx,request,userInfo);
                break;
            case ready:
                ddzGameService.ready(ctx, request, userInfo);
                break;
            case ddzCue:
                ddzGameService.cue(ctx, request, userInfo);
                break;
            case ddzPlay:
                ddzGameService.play(ctx, request, userInfo);
                break;
            default:
                break;

        }
    }
}
