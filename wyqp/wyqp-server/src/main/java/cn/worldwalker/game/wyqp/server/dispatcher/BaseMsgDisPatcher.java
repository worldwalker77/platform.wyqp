package cn.worldwalker.game.wyqp.server.dispatcher;

import cn.worldwalker.game.wyqp.common.channel.ChannelContainer;
import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.roomlocks.RoomLockContainer;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseMsgDisPatcher {
	
	private static final Logger log = Logger.getLogger(BaseMsgDisPatcher.class);
	@Autowired
	public RedisOperationService redisOperationService;
	@Autowired
	public ChannelContainer channelContainer;
	
	public void textMsgProcess(ChannelHandlerContext ctx, BaseRequest request){
		
		/**参数校验*/
		if (null == request || request.getGameType() == null || request.getMsgType() == null || StringUtils.isBlank(request.getToken())) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		/**
		 * token登录检验
		 */
		String token = request.getToken();
		UserInfo userInfo = redisOperationService.getUserInfo(token);
		if (userInfo == null) {
			throw new BusinessException(ExceptionEnum.NEED_LOGIN);
		}
		redisOperationService.expireUserInfo(token);
		/**自动设置playerId和roomId*/
		BaseMsg msg = request.getMsg();
		if (msg == null) {
			msg = new BaseMsg();
			request.setMsg(msg);
		}
		msg.setPlayerId(userInfo.getPlayerId());
		
		MsgTypeEnum msgTypeEnum = MsgTypeEnum.getMsgTypeEnumByType(request.getMsgType());
		if (!MsgTypeEnum.entryRoom.equals(msgTypeEnum)) {
			msg.setRoomId(userInfo.getRoomId());
		}
		if (!MsgTypeEnum.heartBeat.equals(msgTypeEnum) ) {
			log.info("请求," + MsgTypeEnum.getMsgTypeEnumByType(request.getMsgType()).desc + ": " + JsonUtil.toJson(request));
		}
		Lock lock = null;
		try {
			if (!notNeedLockMsgTypeMap.containsKey(request.getMsgType())) {
				/**如果不存在房间号*/
				if (!redisOperationService.isRoomIdExist(msg.getRoomId())) {
					/**如果是非刷新房间的请求，则报错，提示房间号不存在*/
					if (MsgTypeEnum.refreshRoom.msgType != request.getMsgType()) {
						throw new BusinessException(ExceptionEnum.ROOM_ID_NOT_EXIST);
					}
				}else{/**如果房间号存在*/
					lock = RoomLockContainer.getLockByRoomId(msg.getRoomId());
					if (lock == null) {
						synchronized (BaseMsgDisPatcher.class) {
							if (lock == null) {
								lock = new ReentrantLock();
								RoomLockContainer.setLockByRoomId(msg.getRoomId(), lock);
							}
						}
					}
					lock.lock();
				}
			}
			requestDispatcher(ctx, request, userInfo);
		} catch (BusinessException e) {
			log.error(e.getBussinessCode() + ":" + e.getMessage() + ", request:" + JsonUtil.toJson(request), e);
			channelContainer.sendErrorMsg(ctx, ExceptionEnum.getExceptionEnum(e.getBussinessCode()), request);
			
		} catch (Exception e1) {
			log.error("系统异常, request:" + JsonUtil.toJson(request), e1);
			channelContainer.sendErrorMsg(ctx, ExceptionEnum.SYSTEM_ERROR, request);
		} finally{
			if (lock != null) {
				lock.unlock();
			}
			
		}
		
		
	}
	
	public abstract void requestDispatcher(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) throws Exception;
	
	private static Map<Integer, MsgTypeEnum> notNeedLockMsgTypeMap = new HashMap<Integer, MsgTypeEnum>();
	static{
		notNeedLockMsgTypeMap.put(MsgTypeEnum.entryHall.msgType, MsgTypeEnum.entryHall);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.createRoom.msgType, MsgTypeEnum.createRoom);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.heartBeat.msgType, MsgTypeEnum.heartBeat);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.userFeedback.msgType, MsgTypeEnum.userFeedback);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.userRecord.msgType, MsgTypeEnum.userRecord);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.queryPlayerInfo.msgType, MsgTypeEnum.queryPlayerInfo);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.syncPlayerLocation.msgType, MsgTypeEnum.syncPlayerLocation);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.productList.msgType, MsgTypeEnum.productList);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.bindProxy.msgType, MsgTypeEnum.bindProxy);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.checkBindProxy.msgType, MsgTypeEnum.checkBindProxy);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.unifiedOrder.msgType, MsgTypeEnum.unifiedOrder);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.notice.msgType, MsgTypeEnum.notice);
		
		/**茶楼相关*/
		notNeedLockMsgTypeMap.put(MsgTypeEnum.createTeaHouse.msgType, MsgTypeEnum.createTeaHouse);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.queryPlayerTeaHouseList.msgType, MsgTypeEnum.queryPlayerTeaHouseList);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.delTeaHouse.msgType, MsgTypeEnum.delTeaHouse);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.auditEntryTeaHouse.msgType, MsgTypeEnum.auditEntryTeaHouse);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.queryTeaHousePlayerList.msgType, MsgTypeEnum.queryTeaHousePlayerList);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.delTeaHouseUser.msgType, MsgTypeEnum.delTeaHouseUser);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.entryTeaHouseTable.msgType, MsgTypeEnum.entryTeaHouseTable);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.joinTeaHouse.msgType, MsgTypeEnum.joinTeaHouse);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.entryTeaHouse.msgType, MsgTypeEnum.entryTeaHouse);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.queryTeaHouseTablePlayerList.msgType, MsgTypeEnum.queryTeaHouseTablePlayerList);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.queryTableDetail.msgType, MsgTypeEnum.queryTableDetail);
		
		notNeedLockMsgTypeMap.put(MsgTypeEnum.delFromTeaHouse.msgType, MsgTypeEnum.delFromTeaHouse);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.queryNeedAuditPlayerList.msgType, MsgTypeEnum.queryNeedAuditPlayerList);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.teaHouseRecord.msgType, MsgTypeEnum.teaHouseRecord);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.myTeaHouseRecord.msgType, MsgTypeEnum.myTeaHouseRecord);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.queryPlayerJoinedTeaHouseList.msgType, MsgTypeEnum.queryPlayerJoinedTeaHouseList);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.exitTeaHouse.msgType, MsgTypeEnum.exitTeaHouse);
		
		notNeedLockMsgTypeMap.put(MsgTypeEnum.teaHouseConfig.msgType, MsgTypeEnum.teaHouseConfig);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.setDianXiaoer.msgType, MsgTypeEnum.setDianXiaoer);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.teaHouseBigWinner.msgType, MsgTypeEnum.teaHouseBigWinner);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.tuhaoBoard.msgType, MsgTypeEnum.tuhaoBoard);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.paishenBoard.msgType, MsgTypeEnum.paishenBoard);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.openRoomList.msgType, MsgTypeEnum.openRoomList);
		notNeedLockMsgTypeMap.put(MsgTypeEnum.queryAllPlayerRelatedTeaHouse.msgType, MsgTypeEnum.queryAllPlayerRelatedTeaHouse);
		
	}
}
