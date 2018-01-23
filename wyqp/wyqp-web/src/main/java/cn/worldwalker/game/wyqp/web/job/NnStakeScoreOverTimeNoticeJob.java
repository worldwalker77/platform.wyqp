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
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnMsg;
import cn.worldwalker.game.wyqp.common.domain.nn.NnPlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnRequest;
import cn.worldwalker.game.wyqp.common.domain.nn.NnRoomInfo;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.nn.enums.NnButtomScoreTypeEnum;
import cn.worldwalker.game.wyqp.nn.enums.NnPlayerStatusEnum;
import cn.worldwalker.game.wyqp.nn.enums.NnRoomBankerTypeEnum;
import cn.worldwalker.game.wyqp.nn.enums.NnRoomStatusEnum;
import cn.worldwalker.game.wyqp.nn.service.NnGameService;

@Component(value="nnStakeScoreOverTimeNoticeJob")
public class NnStakeScoreOverTimeNoticeJob {
	
	private final static Log log = LogFactory.getLog(NnStakeScoreOverTimeNoticeJob.class);
	
	@Autowired
	public RedisOperationService redisOperationService;
	@Autowired
	private ChannelContainer channelContainer;
	@Autowired
	private NnGameService nnGameService;
	
	public void doTask(){
		String ip = Constant.localIp;
		if (StringUtils.isBlank(ip)) {
			return;
		}
		Map<String, String> map = redisOperationService.getAllNnNotStakeScoreIpRoomIdTime();
		Set<Entry<String, String>> set = map.entrySet();
		for(Entry<String, String> entry : set){
			try {
				Integer roomId = Integer.valueOf(entry.getKey());
				Long time = Long.valueOf(entry.getValue());
			
				NnRoomInfo nnRoomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
				if (nnRoomInfo == null) {
					redisOperationService.delNnNotStakeScoreIpRoomIdTime(roomId);
					continue;
				}
				/**如果状态不为在压分中,将此标记从redis中删掉*/
				if (!nnRoomInfo.getStatus().equals(NnRoomStatusEnum.inStakeScore.status)) {
					redisOperationService.delNnNotStakeScoreIpRoomIdTime(roomId);
					continue;
				}
				if (System.currentTimeMillis() - time < 10000) {
					continue;
				}
				List<NnPlayerInfo> playerList = nnRoomInfo.getPlayerList();
				for(NnPlayerInfo player : playerList){
					if (nnRoomInfo.getRoomBankerType().equals(NnRoomBankerTypeEnum.robBanker.type)) {
						/**玩家状态小于已准备，则说明是观察者*/
						if (player.getStatus() < NnPlayerStatusEnum.notRob.status) {
							continue;
						}
					}else{
						/**玩家状态小于已准备，则说明是观察者*/
						if (player.getStatus() < NnPlayerStatusEnum.ready.status) {
							continue;
						}
					}
					if (player.getStatus() < NnPlayerStatusEnum.stakeScore.status) {
						if (!player.getPlayerId().equals(nnRoomInfo.getRoomBankerId())) {
							UserInfo userInfo = new UserInfo();
							userInfo.setPlayerId(player.getPlayerId());
							userInfo.setRoomId(roomId);
							NnRequest request = new NnRequest();
							NnMsg msg = new NnMsg();
							NnButtomScoreTypeEnum nnButtomScoreTypeEnum = NnButtomScoreTypeEnum.getNnButtomScoreTypeEnum(nnRoomInfo.getButtomScoreType());
							/**自动压分压最低分*/
							msg.setStakeScore(Integer.valueOf(nnButtomScoreTypeEnum.value.split("_")[0]));
							request.setMsg(msg);
							nnGameService.stakeScore(null, request, userInfo);
						}
					}
				}
				redisOperationService.delNnNotStakeScoreIpRoomIdTime(roomId);
			} catch (Exception e) {
				log.error("roomId:" + entry.getKey(), e);
			}
		}
		
	}
	
}
