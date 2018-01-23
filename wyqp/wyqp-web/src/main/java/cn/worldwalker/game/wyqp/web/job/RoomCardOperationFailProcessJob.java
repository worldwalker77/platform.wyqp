package cn.worldwalker.game.wyqp.web.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.worldwalker.game.wyqp.common.channel.ChannelContainer;
import cn.worldwalker.game.wyqp.common.domain.base.RoomCardOperationFailInfo;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.RoomCardOperationEnum;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.manager.CommonManager;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;

public class RoomCardOperationFailProcessJob /**extends SingleServerJobByRedis*/ {
	
	private final static Log log = LogFactory.getLog(RoomCardOperationFailProcessJob.class);
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private RedisOperationService redisOperationService;
	@Autowired
	private ChannelContainer channelContainer;
	
//	@Override
	public void doTask() {
		Result result = new Result();
		result.setMsgType(MsgTypeEnum.roomCardNumUpdate.msgType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		boolean flag = true;
		RoomCardOperationFailInfo failInfo = null;
		do {
			failInfo = redisOperationService.rpopRoomCardOperationFailInfo();
			if (failInfo == null) {
				break;
			}
			if (RoomCardOperationEnum.consumeCard.type.equals(failInfo.getRoomCardOperationType())) {
				try {
					Integer curRoomCardNum = commonManager.doDeductRoomCard(failInfo.getGameType(), failInfo.getPayType(), failInfo.getTotalGames(), RoomCardOperationEnum.jobCompensateConsumeCard, failInfo.getPlayerId());
					data.put("playerId", failInfo.getPlayerId());
					data.put("roomCardNum", curRoomCardNum);
					/**房卡更新通知消息*/
					channelContainer.sendTextMsgByPlayerIds(result, failInfo.getPlayerId());
				} catch (BusinessException e) {
					log.error(e.getMessage() + ", failInfo:" + JsonUtil.toJson(failInfo), e);
					redisOperationService.lpushRoomCardOperationFailInfo(failInfo.getPlayerId(), failInfo.getGameType(), failInfo.getPayType(), failInfo.getTotalGames(), RoomCardOperationEnum.consumeCard);
					break;
				}catch (Exception e) {
					log.error(ExceptionEnum.SYSTEM_ERROR.description + ", failInfo:" + JsonUtil.toJson(failInfo), e);
					redisOperationService.lpushRoomCardOperationFailInfo(failInfo.getPlayerId(), failInfo.getGameType(), failInfo.getPayType(), failInfo.getTotalGames(), RoomCardOperationEnum.consumeCard);
					break;
				}
			}
		} while (flag);
	}

}
