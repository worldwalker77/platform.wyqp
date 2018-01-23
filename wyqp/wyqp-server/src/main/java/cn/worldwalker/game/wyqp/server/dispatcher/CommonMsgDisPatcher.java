package cn.worldwalker.game.wyqp.server.dispatcher;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.server.service.CommonGameService;

@Service(value="commonMsgDispatcher")
public class CommonMsgDisPatcher extends BaseMsgDisPatcher{
	
	@Autowired
	private CommonGameService commonGameService;
	
	@Override
	public void requestDispatcher(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) throws Exception {
		BaseMsg msg = request.getMsg();
		Integer msgType = request.getMsgType();
		MsgTypeEnum msgTypeEnum= MsgTypeEnum.getMsgTypeEnumByType(msgType);
		switch (msgTypeEnum) {
			case entryHall:
				commonGameService.entryHall(ctx, request, userInfo);
				break;
			case syncPlayerLocation:
				commonGameService.syncPlayerLocation(ctx, request, userInfo);
				break;
			case entryRoom:
				commonGameService.commonEntryRoom(ctx, request, userInfo);
				break;
			case userFeedback:
				commonGameService.userFeedback(ctx, request, userInfo);
				break;
			case heartBeat:
				channelContainer.sendTextMsgByPlayerIds(new Result(GameTypeEnum.common.gameType, MsgTypeEnum.heartBeat.msgType), userInfo.getPlayerId());
				break;
			case refreshRoom:
				channelContainer.addChannel(ctx, userInfo.getPlayerId());
				commonGameService.commonRefreshRoom(ctx, request, userInfo);
				break;
			case productList:
				commonGameService.productList(ctx, request, userInfo);
				break;
			case bindProxy:
				commonGameService.bindProxy(ctx, request, userInfo);
				break;
			case checkBindProxy:
				commonGameService.checkBindProxy(ctx, request, userInfo);
				break;
			case unifiedOrder:
				commonGameService.unifiedOrder(ctx, request, userInfo);
				break;
			case notice:
				commonGameService.notice(ctx, request, userInfo);
				break;
				
			/**********以下为茶楼相关************/
			case queryPlayerTeaHouseList:
				commonGameService.queryPlayerTeaHouseList(ctx, request, userInfo);
				break;
			case delTeaHouse:
				commonGameService.delTeaHouse(ctx, request, userInfo);
				break;
			case auditEntryTeaHouse:
				commonGameService.auditEntryTeaHouse(ctx, request, userInfo);
				break;
			case queryTeaHousePlayerList:
				commonGameService.queryTeaHousePlayerList(ctx, request, userInfo);
				break;
			case delTeaHouseUser:
				commonGameService.delTeaHouseUser(ctx, request, userInfo);
				break;
			case entryTeaHouse:
				commonGameService.entryTeaHouse(ctx, request, userInfo);
				break;
				/**进入茶楼茶桌*/
			case entryTeaHouseTable:
				commonGameService.entryTeaHouseTable(ctx, request, userInfo);
				break;
			case joinTeaHouse:
				commonGameService.joinTeaHouse(ctx, request, userInfo);
				break;
			case queryTeaHouseTablePlayerList:
				commonGameService.queryTeaHouseTablePlayerList(ctx, request, userInfo);
				break;
			case queryTableDetail:
				commonGameService.queryTableDetail(ctx, request, userInfo);
				break;
			case delFromTeaHouse:
				commonGameService.delFromTeaHouse(ctx, request, userInfo);
				break;
				
			case queryNeedAuditPlayerList:
				commonGameService.queryNeedAuditPlayerList(ctx, request, userInfo);
				break;
			case teaHouseRecord:
				commonGameService.teaHouseRecord(ctx, request, userInfo);
				break;
			case myTeaHouseRecord:
				commonGameService.myTeaHouseRecord(ctx, request, userInfo);
				break;
			case queryPlayerJoinedTeaHouseList:
				commonGameService.queryPlayerJoinedTeaHouseList(ctx, request, userInfo);
				break;
			case exitTeaHouse:
				commonGameService.exitTeaHouse(ctx, request, userInfo);
				break;
				
			case teaHouseConfig:
				commonGameService.teaHouseConfig(ctx, request, userInfo);
				break;
			case setDianXiaoer:
				commonGameService.setDianXiaoer(ctx, request, userInfo);
				break;
			case teaHouseBigWinner:
				commonGameService.teaHouseBigWinner(ctx, request, userInfo);
				break;
			case tuhaoBoard:
				commonGameService.tuhaoBoard(ctx, request, userInfo);
				break;
			case paishenBoard:
				commonGameService.paishenBoard(ctx, request, userInfo);
				break;
			case openRoomList:
				commonGameService.openRoomList(ctx, request, userInfo);
				break;
			case queryAllPlayerRelatedTeaHouse:
				commonGameService.queryAllPlayerRelatedTeaHouse(ctx, request, userInfo);
				break;
			default:
				break;
			}
		
	
	}

}
