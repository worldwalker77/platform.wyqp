package cn.worldwalker.game.wyqp.server.dispatcher;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.stereotype.Service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
@Service(value="mjMsgDisPatcher")
public class MjMsgDispatcher extends BaseMsgDisPatcher {

	@Override
	public void requestDispatcher(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Integer msgType = request.getMsgType();
		MsgTypeEnum msgTypeEnum= MsgTypeEnum.getMsgTypeEnumByType(msgType);
		switch (msgTypeEnum) {
			case createRoom:
				break;
			case entryRoom:
				break;
			case ready:
				break;
			case dealCards:
				break;
			case dissolveRoom:
				break;
			case agreeDissolveRoom:
				break;
			case disagreeDissolveRoom:
				break;
				
			case successDissolveRoom://服务端主动推送的消息
				break;
				
			case delRoomConfirmBeforeReturnHall:
				break;
				
			case refreshRoom:
				break;
			case queryPlayerInfo:
				break;
			case chatMsg:
				break;
			case heartBeat:
				break;
			case userRecord:
				break;
			case userFeedback:
				break;
			case sendEmoticon:
				break;
			case syncPlayerLocation:
				break;
				
			default:
				break;
		}
	}
	
}
