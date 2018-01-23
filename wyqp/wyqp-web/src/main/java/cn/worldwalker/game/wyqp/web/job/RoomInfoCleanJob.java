package cn.worldwalker.game.wyqp.web.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.worldwalker.game.wyqp.common.channel.ChannelContainer;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.RedisRelaModel;
import cn.worldwalker.game.wyqp.common.domain.jh.JhRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.mj.MjRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.common.utils.GameUtil;

/**
 * 房间如果10分钟没有更新，则删除房间信息
 * @author lenovo
 *
 */
public class RoomInfoCleanJob /**extends SingleServerJobByRedis*/ {
	@Autowired
	private RedisOperationService redisOperationService;
	@Autowired
	private ChannelContainer channelContainer;
//	@Override
	public void doTask() {
		
		List<RedisRelaModel> list = redisOperationService.getAllRoomIdGameTypeUpdateTime();
		for(RedisRelaModel model : list){
			if (System.currentTimeMillis() - model.getUpdateTime() > 10*60*1000) {
				BaseRoomInfo roomInfo = null;
				if (GameTypeEnum.nn.gameType.equals(model.getGameType()) ) {
					roomInfo = redisOperationService.getRoomInfoByRoomId(model.getRoomId(), NnRoomInfo.class);
				}else if(GameTypeEnum.mj.gameType.equals(model.getGameType()) ){
					roomInfo = redisOperationService.getRoomInfoByRoomId(model.getRoomId(), MjRoomInfo.class);
				}else if(GameTypeEnum.jh.gameType.equals(model.getGameType()) ){
					roomInfo = redisOperationService.getRoomInfoByRoomId(model.getRoomId(), JhRoomInfo.class);
				}
				if (roomInfo == null) {
					redisOperationService.delGameTypeUpdateTimeByRoomId(model.getRoomId());
					return;
				}
				List playerList = roomInfo.getPlayerList();
				redisOperationService.cleanPlayerAndRoomInfo(model.getRoomId(), GameUtil.getPlayerIdStrArr(playerList));
				/**茶楼相关*/
				redisOperationService.delRoomIdByTeaHouseNumTableNum(roomInfo.getTeaHouseNum(), roomInfo.getTableNum());
			}
		}
	}
	
}
