package cn.worldwalker.game.wyqp.server.service;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.RedisRelaModel;
import cn.worldwalker.game.wyqp.common.domain.base.TeaHouseModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.domain.jh.JhMsg;
import cn.worldwalker.game.wyqp.common.domain.jh.JhRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnMsg;
import cn.worldwalker.game.wyqp.common.domain.nn.NnRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.manager.CommonManager;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.jh.service.JhGameService;
import cn.worldwalker.game.wyqp.mj.service.MjGameService;
import cn.worldwalker.game.wyqp.nn.service.NnGameService;

@Service(value="commonGameService")
public class CommonGameService extends BaseGameService{
	
	@Autowired
	private NnGameService nnGameService;
	
	@Autowired
	private MjGameService mjGameService;
	
	@Autowired
	private JhGameService jhGameService;
	
	@Autowired
	private CommonManager commonManager;
	
	@Autowired
	private RedisOperationService redisOperationService;
	
	/**
	 * 由于加入房间的时候，只知道房间号，不知道游戏类型，所以这里需要先获取是哪种类型的房间
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void commonEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Integer roomId = request.getMsg().getRoomId();
		RedisRelaModel rrm = redisOperationService.getGameTypeUpdateTimeByRoomId(roomId);
		Integer realGameType = rrm.getGameType();
		/**设置真是的gameType*/
		request.setGameType(realGameType);
		GameTypeEnum gameTypeEnum = GameTypeEnum.getGameTypeEnumByType(realGameType);
		switch (gameTypeEnum) {
			case nn:
				nnGameService.entryRoom(ctx, request, userInfo);
				break;
			case mj:
				mjGameService.entryRoom(ctx, request, userInfo);
				break;
			case jh:
				jhGameService.entryRoom(ctx, request, userInfo);
				break;
			default:
				break;
			}
	}
	
	
	public void commonRefreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Integer roomId = userInfo.getRoomId();
		if (roomId == null) {
			channelContainer.sendTextMsgByPlayerIds(new Result(0, MsgTypeEnum.entryHall.msgType), userInfo.getPlayerId());
			return;
		}
		RedisRelaModel rrm = redisOperationService.getGameTypeUpdateTimeByRoomId(roomId);
		/**如果为null，则说明可能是解散房间后，玩家的userInfo里面的roomId没有清空，需要清空掉*/
		if (rrm == null) {
			userInfo.setRoomId(null);
			redisOperationService.setUserInfo(request.getToken(), userInfo);
			channelContainer.sendTextMsgByPlayerIds(new Result(0, MsgTypeEnum.entryHall.msgType), userInfo.getPlayerId());
			return;
		}
		Integer realGameType = rrm.getGameType();
		/**设置真是的gameType*/
		request.setGameType(realGameType);
		GameTypeEnum gameTypeEnum = GameTypeEnum.getGameTypeEnumByType(realGameType);
		switch (gameTypeEnum) {
			case nn:
				nnGameService.refreshRoom(ctx, request, userInfo);
				break;
			case mj:
				nnGameService.refreshRoom(ctx, request, userInfo);
				break;
			case jh:
				jhGameService.refreshRoom(ctx, request, userInfo);
				break;
			default:
				break;
			}
	}
	/**
	 * 加入茶楼牌桌，需要先通过茶楼号和牌桌号获取是否已经创建了房间，没有创建房间则创建房间，如果已经创建房间，则加入
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void entryTeaHouseTable(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		BaseMsg msg = request.getMsg();
		Integer teaHouseNum = msg.getTeaHouseNum();
		Integer tableNum = msg.getTableNum();
		Integer roomId = redisOperationService.getRoomIdByTeaHouseNumTableNum(teaHouseNum, tableNum);
		if (roomId != null) {
			msg.setRoomId(roomId);
			commonEntryRoom(ctx, request, userInfo);
		}else{
			TeaHouseModel resModel = commonManager.getTeaHouseTypeByTeaHouseNum(teaHouseNum,msg.getPlayerId());
			if (resModel == null) {
				throw new BusinessException(ExceptionEnum.TEA_HOUSE_NUM_ERROR);
			}
			Integer gameType = resModel.getGameType();
			/**这里将gameTyp转换为实际的游戏类型*/
			request.setGameType(gameType);
			GameTypeEnum gameTypeEnum = GameTypeEnum.getGameTypeEnumByType(gameType);
			switch (gameTypeEnum) {
				case nn:
					NnMsg nnMsg = new NnMsg();
					nnMsg.setPlayerId(msg.getPlayerId());
					nnMsg.setTeaHouseNum(teaHouseNum);
					nnMsg.setTableNum(tableNum);
					nnMsg.setTotalGames(resModel.getTotalGame());
					nnMsg.setRoomBankerType(resModel.getRoomBankerType());
					nnMsg.setMultipleLimit(resModel.getMultipleLimit());
					nnMsg.setPayType(resModel.getPayType());
					nnMsg.setButtomScoreType(resModel.getButtomScoreType());
					request.setMsg(nnMsg);
					nnGameService.createRoom(ctx, request, userInfo);
					break;
				case mj:
					mjGameService.createRoom(ctx, request, userInfo);
					break;
				case jh:
					JhMsg jhMsg = new JhMsg();
					jhMsg.setPlayerId(msg.getPlayerId());
					jhMsg.setTeaHouseNum(teaHouseNum);
					jhMsg.setTableNum(tableNum);
					jhMsg.setTotalGames(resModel.getTotalGame());
					jhMsg.setPayType(resModel.getPayType());
					request.setMsg(jhMsg);
					jhGameService.createRoom(ctx, request, userInfo);
					break;
				default:
					break;
				}
			
		}
		
	}
	
	
	@Override
	public BaseRoomInfo doCreateRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		return null;
	}

	@Override
	public BaseRoomInfo doEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		return null;
	}

	@Override
	public List<BaseRoomInfo> doRefreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		return null;
	}


	@Override
	public BaseRoomInfo getRoomInfo(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Integer roomId = userInfo.getRoomId();
		RedisRelaModel model = redisOperationService.getGameTypeUpdateTimeByRoomId(roomId);
		BaseRoomInfo roomInfo = null;
		if (model.getGameType().equals(GameTypeEnum.nn.gameType)) {
			roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
		}else if(model.getGameType().equals(GameTypeEnum.jh.gameType)){
			roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		}
		return roomInfo;
	}


}
