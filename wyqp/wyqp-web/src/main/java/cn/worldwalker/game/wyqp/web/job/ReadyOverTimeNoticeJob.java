package cn.worldwalker.game.wyqp.web.job;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.worldwalker.game.wyqp.common.channel.ChannelContainer;
import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.domain.base.RedisRelaModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.domain.jh.JhPlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.jh.JhRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnPlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.jh.enums.JhPlayerStatusEnum;
import cn.worldwalker.game.wyqp.jh.enums.JhRoomStatusEnum;
import cn.worldwalker.game.wyqp.jh.service.JhGameService;
import cn.worldwalker.game.wyqp.nn.enums.NnPlayerStatusEnum;
import cn.worldwalker.game.wyqp.nn.enums.NnRoomStatusEnum;
import cn.worldwalker.game.wyqp.nn.service.NnGameService;

@Component(value="readyOverTimeNoticeJob")
public class ReadyOverTimeNoticeJob {
	
	private final static Log log = LogFactory.getLog(ReadyOverTimeNoticeJob.class);
	
	@Autowired
	public RedisOperationService redisOperationService;
	@Autowired
	private ChannelContainer channelContainer;
	@Autowired
	private JhGameService jhGameService;
	@Autowired
	private NnGameService nnGameService;
	
	public void doTask(){
		String ip = Constant.localIp;
		if (StringUtils.isBlank(ip)) {
			return;
		}
		Map<String, String> map = redisOperationService.getAllNotReadyIpRoomIdTime();
		Set<Entry<String, String>> set = map.entrySet();
		for(Entry<String, String> entry : set){
			try {
				Integer roomId = Integer.valueOf(entry.getKey());
				Long time = Long.valueOf(entry.getValue());
				RedisRelaModel rrm = redisOperationService.getGameTypeUpdateTimeByRoomId(roomId);
				if (rrm == null) {
					redisOperationService.delNotReadyIpRoomIdTime(roomId);
					continue;
				}
				Integer gameType = rrm.getGameType();
				if (GameTypeEnum.jh.gameType.equals(gameType)) {
					processJinhua(roomId, time);
				}else if(GameTypeEnum.nn.gameType.equals(gameType)){
					processNn(roomId, time);
				}else if(GameTypeEnum.mj.gameType.equals(gameType)){
					
				}else{
					
				}
				
				
			} catch (Exception e) {
				log.error("roomId:" + entry.getKey(), e);
			}
		}
		
	}
	
	private void processJinhua(Integer roomId, Long time){
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		if (roomInfo == null) {
			redisOperationService.delNotReadyIpRoomIdTime(roomId);
			return;
		}
		/**如果房间已经在游戏中了,将此房间抢庄标记从redis中删掉*/
		if (roomInfo.getStatus().equals(JhRoomStatusEnum.inGame.status)) {
			redisOperationService.delNotReadyIpRoomIdTime(roomId);
			return;
		}
		if (System.currentTimeMillis() - time < 10000) {
			return;
		}
		/**将没有准备的人设置为观察者，并且发牌给其他的人，同时通过刷新房间接口返回个所有玩家*/
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		for(JhPlayerInfo player : playerList){
			/**玩家的状态为没有准备，则自动准备*/
			if (!player.getStatus().equals(JhPlayerStatusEnum.ready.status)) {
				UserInfo userInfo = new UserInfo();
				userInfo.setPlayerId(player.getPlayerId());
				userInfo.setRoomId(roomId);
				jhGameService.ready(null, null, userInfo);
			}
		}
		redisOperationService.delNotReadyIpRoomIdTime(roomId);
	}
	
	private void processNn(Integer roomId, Long time){
		NnRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
		if (roomInfo == null) {
			redisOperationService.delNotReadyIpRoomIdTime(roomId);
			return;
		}
		/**如果是抢庄阶段或者压分阶段（对应抢庄类型和非抢庄类型）*/
		if (roomInfo.getStatus().equals(NnRoomStatusEnum.inRob.status) 
			|| roomInfo.getStatus().equals(NnRoomStatusEnum.inStakeScore.status)) {
			redisOperationService.delNotReadyIpRoomIdTime(roomId);
			return;
		}
		if (System.currentTimeMillis() - time < 10000) {
			return;
		}
		
		/**将没有准备的人自动调用准备接口*/
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		for(NnPlayerInfo player : playerList){
			/**除观察者外，没有准备的自动准备*/
			if (!player.getStatus().equals(NnPlayerStatusEnum.ready.status) && player.getStatus() > NnPlayerStatusEnum.observer.status) {
				UserInfo userInfo = new UserInfo();
				userInfo.setPlayerId(player.getPlayerId());
				userInfo.setRoomId(roomId);
				nnGameService.ready(null, null, userInfo);
			}
		}
		/**删除未准备10秒计时器*/
		redisOperationService.delNotReadyIpRoomIdTime(roomId);
		
		
		
		
	}
}
