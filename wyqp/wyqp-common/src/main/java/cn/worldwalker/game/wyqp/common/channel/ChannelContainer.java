package cn.worldwalker.game.wyqp.common.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.RedisRelaModel;
import cn.worldwalker.game.wyqp.common.domain.jh.JhRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.OnlineStatusEnum;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.common.utils.GameUtil;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;

@Component
public class ChannelContainer {
	
	private static final Log log = LogFactory.getLog(ChannelContainer.class);
	
	@Autowired
	public RedisOperationService redisOperationService;
	
	private static Map<Integer, Channel> sessionMap = new ConcurrentHashMap<Integer, Channel>();
	
	public void addChannel(ChannelHandlerContext ctx, Integer playerId){
		sessionMap.put(playerId, ctx.channel());
	}
	
	public Channel getChannel(Integer playerId){
		return sessionMap.get(playerId);
	}
	
	public void sendTextMsgByPlayerIds(Result result, Integer... playerIds){
		if (redisOperationService.isLogFuseOpen() && result.getMsgType() != MsgTypeEnum.heartBeat.msgType) {
			log.info("返回 ," + MsgTypeEnum.getMsgTypeEnumByType(result.getMsgType()).desc + ": " + JsonUtil.toJson(result));
		}
		for(Integer playerId : playerIds){
			Channel channel = getChannel(playerId);
			if (null != channel) {
				try {
					channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
				} catch (Exception e) {
					log.error("sendTextMsgByPlayerId error, playerId: " + playerId + ", result : " + JsonUtil.toJson(result), e);
				}
			}
		}
	}
	
	public void sendTextMsgToAllPlayer(Result result){
		if (redisOperationService.isLogFuseOpen()) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		Set<Entry<Integer, Channel>> set = sessionMap.entrySet();
		for(Entry<Integer, Channel> entry : set){
			Integer playerId = entry.getKey();
			Channel channel = entry.getValue();
			if (null != channel) {
				try {
					channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
				} catch (Exception e) {
					log.error("sendTextMsgByPlayerIdList error, playerId: " + playerId + ", result : " + JsonUtil.toJson(result), e);
				}
			}
		}
	}
	
	public Result sendErrorMsg(ChannelHandlerContext ctx, ExceptionEnum exceptionEnum, BaseRequest request){
		Result result = new Result(exceptionEnum.index, exceptionEnum.description, request.getMsgType(), request.getGameType());
		if (redisOperationService.isLogFuseOpen()) {
			log.info("返回 ：" + JsonUtil.toJson(result));
		}
		try {
			ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(result)));
		} catch (Exception e) {
			log.error("sendErrorMsg error, result : " + JsonUtil.toJson(result), e);
		}
		return result;
	}
	

	public Map<Integer, Channel> getSessionMap() {
		return sessionMap;
	}
	
	
	public void removeSession(ChannelHandlerContext ctx){
		if (sessionMap.isEmpty()) {
			return;
		}
		Integer playerId = null;
		Set<Entry<Integer, Channel>> entrySet = sessionMap.entrySet();
		for(Entry<Integer, Channel> entry : entrySet){
			if (entry.getValue().equals(ctx.channel())) {
				playerId = entry.getKey();
				break;
			}
		}
		if (playerId != null) {
			sessionMap.remove(playerId);
			RedisRelaModel redisRelaModel = redisOperationService.getRoomIdGameTypeByPlayerId(playerId);
			System.out.println("====>>>removeSession,玩家断线，redisRelaModel:" + JsonUtil.toJson(redisRelaModel));
			/**如果此掉线的playerId有房间信息*/
			if (redisRelaModel != null) {
				Integer roomId = redisRelaModel.getRoomId();
				Integer gameType = redisRelaModel.getGameType();
				/**设置离线playerId与roomId的映射关系*/
				redisOperationService.setOfflinePlayerIdRoomIdGameTypeTime(playerId, roomId, gameType, new Date());
				GameTypeEnum gameTypeEnum = GameTypeEnum.getGameTypeEnumByType(gameType);
				/**设置当前玩家为离线状态并通知其他玩家此玩家离线*/
				BaseRoomInfo roomInfo = null;
				if (GameTypeEnum.nn.gameType == gameType) {
					roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
				}else if(GameTypeEnum.mj.gameType == gameType){
					
				}else if(GameTypeEnum.jh.gameType == gameType){
					roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
				}
				List playerList = roomInfo.getPlayerList();
				List<Integer> playerIdList = new ArrayList<Integer>();
				for(Object object : playerList){
					BasePlayerInfo basePlayerInfo = (BasePlayerInfo)object;
					if (playerId.equals(basePlayerInfo.getPlayerId())) {
						basePlayerInfo.setOnlineStatus(OnlineStatusEnum.offline.status);
					}
				}
				redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
				
				Result result = new Result();
				result.setMsgType(MsgTypeEnum.offlineNotice.msgType);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("playerId", playerId);
				result.setData(data);
				result.setGameType(gameType);
				sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArrWithOutSelf(playerList, playerId));
			}
		}
	}
	
}
