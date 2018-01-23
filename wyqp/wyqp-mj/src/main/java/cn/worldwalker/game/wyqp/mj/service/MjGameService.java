package cn.worldwalker.game.wyqp.mj.service;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.domain.mj.MjPlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.mj.MjRoomInfo;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
@Service(value="mjGameService")
public class MjGameService extends BaseGameService{

	@Override
	public BaseRoomInfo doCreateRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		
		if (null == request) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		return null;
	}

	@Override
	public BaseRoomInfo doEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		
		MjRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(userInfo.getRoomId(), MjRoomInfo.class);
		List<MjPlayerInfo> playerList = roomInfo.getPlayerList();
		/**校验房间中是否已经有玩家信息，如果有的话可能是异常情况下产生的（缓存设置成功，但是客户端没有收到成功加入房间消息，导致客户端再次操作），需要去掉*/
		for(int i = 0; i < playerList.size(); i++){
			Integer tempPalyerId = playerList.get(i).getPlayerId();
			if (tempPalyerId.equals(userInfo.getPlayerId())) {
				playerList.remove(i);
			}
		}
		MjPlayerInfo playerInfo = new MjPlayerInfo();
		playerList.add(playerInfo);
		return roomInfo;
	}


	@Override
	public List<BaseRoomInfo> doRefreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		return null;
	}

	@Override
	public BaseRoomInfo getRoomInfo(ChannelHandlerContext ctx,
			BaseRequest request, UserInfo userInfo) {
		return null;
	}


}
