package cn.worldwalker.game.wyqp.web.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.worldwalker.game.wyqp.common.channel.ChannelContainer;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.RedisRelaModel;
import cn.worldwalker.game.wyqp.common.domain.jh.JhRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.mj.MjRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.common.utils.GameUtil;


public class OfflinePlayerCleanJob /**extends SingleServerJobByRedis*/{
	 
	@Autowired
	private RedisOperationService redisOperationService;
	@Autowired
	private ChannelContainer channelContainer;
	
	private static long roomClearTime = 4*60*1000L;
	/**
	 * 玩家离线超过4分钟，并且房间没有更新则清除房间信息
	 */
//	@Override
	public void doTask() {
		try {
			List<RedisRelaModel> list = redisOperationService.getAllOfflinePlayerIdRoomIdGameTypeTime();
			for(RedisRelaModel model : list){
				if (System.currentTimeMillis() - model.getUpdateTime() > roomClearTime) {
					Result result = new Result();
					BaseRoomInfo roomInfo = null;
					if (GameTypeEnum.nn.gameType.equals(model.getGameType()) ) {
						roomInfo = redisOperationService.getRoomInfoByRoomId(model.getRoomId(), NnRoomInfo.class);
						result.setGameType(GameTypeEnum.nn.gameType);
					}else if(GameTypeEnum.mj.gameType.equals(model.getGameType()) ){
						roomInfo = redisOperationService.getRoomInfoByRoomId(model.getRoomId(), MjRoomInfo.class);
						result.setGameType(GameTypeEnum.mj.gameType);
					}else if(GameTypeEnum.jh.gameType.equals(model.getGameType()) ){
						roomInfo = redisOperationService.getRoomInfoByRoomId(model.getRoomId(), JhRoomInfo.class);
						result.setGameType(GameTypeEnum.jh.gameType);
					}
					if (roomInfo == null) {
						/**如果无房间信息，则说明可能其他离线玩家已经将房间删除，不需要再推送消息给其他玩家*/
						redisOperationService.cleanPlayerAndRoomInfo(model.getRoomId(), String.valueOf(model.getPlayerId()));
						return;
					}
					/**如果房间没有更新小于4分钟*/
					if (System.currentTimeMillis() - roomInfo.getUpdateTime().getTime() < roomClearTime) {
						continue;
					}
					List playerList = roomInfo.getPlayerList();
					result.setMsgType(MsgTypeEnum.dissolveRoomCausedByOffline.msgType);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("playerId", model.getPlayerId());
					result.setData(data);
					channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArrWithOutSelf(playerList, model.getPlayerId()));
					redisOperationService.cleanPlayerAndRoomInfo(model.getRoomId(), GameUtil.getPlayerIdStrArr(playerList));
					/**茶楼相关*/
					redisOperationService.delRoomIdByTeaHouseNumTableNum(roomInfo.getTeaHouseNum(), roomInfo.getTableNum());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
