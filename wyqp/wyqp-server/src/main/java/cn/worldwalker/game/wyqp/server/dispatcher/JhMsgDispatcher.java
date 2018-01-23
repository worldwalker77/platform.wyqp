package cn.worldwalker.game.wyqp.server.dispatcher;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.jh.service.JhGameService;
@Service(value="jhMsgDispatcher")
public class JhMsgDispatcher extends BaseMsgDisPatcher {
	@Autowired
	private JhGameService jhGameService;
	@Override
	public void requestDispatcher(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Integer msgType = request.getMsgType();
		MsgTypeEnum msgTypeEnum= MsgTypeEnum.getMsgTypeEnumByType(msgType);
		switch (msgTypeEnum) {
			case createRoom:
				jhGameService.createRoom(ctx, request, userInfo);
				break;
			case entryRoom:
				jhGameService.entryRoom(ctx, request, userInfo);
				break;
			case ready:
				jhGameService.ready(ctx, request, userInfo);
				break;
			case stake:
				jhGameService.stake(ctx, request, userInfo);
				break;
			case manualCardsCompare:
				jhGameService.manualCardsCompare(ctx, request, userInfo);
				break;
			case watchCards:
				jhGameService.watchCards(ctx, request, userInfo);
				break;
			case discardCards:
				jhGameService.discardCards(ctx, request, userInfo);
				break;
			case dissolveRoom:
				jhGameService.dissolveRoom(ctx, request, userInfo);
				break;
			case agreeDissolveRoom:
				jhGameService.agreeDissolveRoom(ctx, request, userInfo);
				break;
			case disagreeDissolveRoom:
				jhGameService.disagreeDissolveRoom(ctx, request, userInfo);
				break;
			case delRoomConfirmBeforeReturnHall:
				jhGameService.delRoomConfirmBeforeReturnHall(ctx, request, userInfo);
				break;
			case queryPlayerInfo:
				jhGameService.queryPlayerInfo(ctx, request, userInfo);
				break;
			case chatMsg:
				jhGameService.chatMsg(ctx, request, userInfo);
				break;
			case userRecord:
				jhGameService.userRecord(ctx, request, userInfo);
				break;
			/**********以下为茶楼相关************/
			case createTeaHouse:
				jhGameService.createTeaHouse(ctx, request, userInfo);
				break;
			default:
				break;
		}
	}
	
}
