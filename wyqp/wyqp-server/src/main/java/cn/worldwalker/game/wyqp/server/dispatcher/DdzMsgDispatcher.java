package cn.worldwalker.game.wyqp.server.dispatcher;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service( value = "ddzMsgDispatcher")
public class DdzMsgDispatcher extends BaseMsgDisPatcher{

    @Override
    public void requestDispatcher(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) throws Exception {

    }
}
