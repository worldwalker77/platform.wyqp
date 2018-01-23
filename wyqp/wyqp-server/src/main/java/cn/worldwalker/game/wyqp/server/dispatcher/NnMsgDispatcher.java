package cn.worldwalker.game.wyqp.server.dispatcher;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.nn.service.NnGameService;
@Service(value="nnMsgDispatcher")
public class NnMsgDispatcher extends BaseMsgDisPatcher {
	@Autowired
	private NnGameService nnGameService;
	@Override
	public void requestDispatcher(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Integer msgType = request.getMsgType();
		MsgTypeEnum msgTypeEnum= MsgTypeEnum.getMsgTypeEnumByType(msgType);
		switch (msgTypeEnum) {
			case createRoom:
				nnGameService.createRoom(ctx, request, userInfo);
				break;
			case entryRoom:
				nnGameService.entryRoom(ctx, request, userInfo);
				break;
			case ready:
				nnGameService.ready(ctx, request, userInfo);
				break;
			case robBanker:
				nnGameService.robBanker(ctx, request, userInfo);
				break;
			case stakeScore:
				nnGameService.stakeScore(ctx, request, userInfo);
				break;
			case showCard:
				nnGameService.showCard(ctx, request, userInfo);
				break;
			case dissolveRoom:
				nnGameService.dissolveRoom(ctx, request, userInfo);
				break;
			case agreeDissolveRoom:
				nnGameService.agreeDissolveRoom(ctx, request, userInfo);
				break;
			case disagreeDissolveRoom:
				nnGameService.disagreeDissolveRoom(ctx, request, userInfo);
				break;
			case delRoomConfirmBeforeReturnHall:
				nnGameService.delRoomConfirmBeforeReturnHall(ctx, request, userInfo);
				break;
			case queryPlayerInfo:
				nnGameService.queryPlayerInfo(ctx, request, userInfo);
				break;
			case chatMsg:
				nnGameService.chatMsg(ctx, request, userInfo);
				break;
			case userRecord:
				nnGameService.userRecord(ctx, request, userInfo);
				break;
			/**********以下为茶楼相关************/
			case createTeaHouse:
				nnGameService.createTeaHouse(ctx, request, userInfo);
				break;
			default:
				break;
		}
	}
	
}
