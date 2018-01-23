package cn.worldwalker.game.wyqp.common.service;

import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.worldwalker.game.wyqp.common.channel.ChannelContainer;
import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.OrderModel;
import cn.worldwalker.game.wyqp.common.domain.base.ProductModel;
import cn.worldwalker.game.wyqp.common.domain.base.RecordModel;
import cn.worldwalker.game.wyqp.common.domain.base.RedisRelaModel;
import cn.worldwalker.game.wyqp.common.domain.base.TeaHouseModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserFeedbackModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.domain.base.UserModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserRecordModel;
import cn.worldwalker.game.wyqp.common.domain.base.WeiXinUserInfo;
import cn.worldwalker.game.wyqp.common.domain.jh.JhMsg;
import cn.worldwalker.game.wyqp.common.domain.nn.NnMsg;
import cn.worldwalker.game.wyqp.common.enums.ChatTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.DissolveStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.OnlineStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.PayStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.PayTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.PlayerStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.RoomStatusEnum;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.manager.CommonManager;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.roomlocks.RoomLockContainer;
import cn.worldwalker.game.wyqp.common.rpc.WeiXinRpc;
import cn.worldwalker.game.wyqp.common.utils.GameUtil;
import cn.worldwalker.game.wyqp.common.utils.IPUtil;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;
import cn.worldwalker.game.wyqp.common.utils.wxpay.DateUtils;
import cn.worldwalker.game.wyqp.common.utils.wxpay.HttpUtil;
import cn.worldwalker.game.wyqp.common.utils.wxpay.MapUtils;
import cn.worldwalker.game.wyqp.common.utils.wxpay.PayCommonUtil;
import cn.worldwalker.game.wyqp.common.utils.wxpay.WeixinConstant;
import cn.worldwalker.game.wyqp.common.utils.wxpay.XMLUtil;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;

public abstract class BaseGameService {
	
	public final static Log log = LogFactory.getLog(BaseGameService.class);
	
	@Autowired
	public RedisOperationService redisOperationService;
	@Autowired
	public ChannelContainer channelContainer;
	@Autowired
	public CommonManager commonManager;
	
	@Autowired
	public WeiXinRpc weiXinRpc;
	
	public Result login(String code, String deviceType, HttpServletRequest request) {
		Result result = new Result();
		if (StringUtils.isBlank(code)) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		WeiXinUserInfo weixinUserInfo = weiXinRpc.getWeiXinUserInfo(code);
		if (null == weixinUserInfo) {
			throw new BusinessException(ExceptionEnum.QUERY_WEIXIN_USER_INFO_FAIL);
		}
		UserModel userModel = commonManager.getUserByWxOpenId(weixinUserInfo.getOpneid());
		if (null == userModel) {
			userModel = new UserModel();
			userModel.setNickName(GameUtil.emojiFilter(weixinUserInfo.getName()));
			userModel.setHeadImgUrl(weixinUserInfo.getHeadImgUrl());
			userModel.setWxOpenId(weixinUserInfo.getOpneid());
			userModel.setRoomCardNum(10);
			commonManager.insertUser(userModel);
		}
		/**从redis查看此用户是否有roomId*/
		Integer roomId = null;
		RedisRelaModel redisRelaModel = redisOperationService.getRoomIdGameTypeByPlayerId(userModel.getPlayerId());
		if (redisRelaModel != null) {
			roomId = redisRelaModel.getRoomId();
		}
		Integer teaHouseNum = redisOperationService.getTeaHouseNumByPlayerId(userModel.getPlayerId());
		
		UserInfo userInfo = new UserInfo();
		userInfo.setPlayerId(userModel.getPlayerId());
		userInfo.setRoomId(roomId);
		userInfo.setTeaHouseNum(teaHouseNum);
		userInfo.setNickName(weixinUserInfo.getName());
		userInfo.setLevel(userModel.getUserLevel() == null ? 1 : userModel.getUserLevel());
		userInfo.setServerIp(Constant.localIp);
		userInfo.setPort(String.valueOf(Constant.websocketPort));
		userInfo.setRemoteIp(IPUtil.getRemoteIp(request));
		String loginToken = GameUtil.genToken(userModel.getPlayerId());
//		userInfo.setHeadImgUrl(UrlImgDownLoadUtil.getLocalImgUrl(weixinUserInfo.getHeadImgUrl(), userModel.getPlayerId()));
		userInfo.setHeadImgUrl(weixinUserInfo.getHeadImgUrl());
		userInfo.setToken(loginToken);
		/**设置赢牌概率*/
		userInfo.setWinProbability(userModel.getWinProbability());
		userInfo.setRoomCardNum(userModel.getRoomCardNum());
		redisOperationService.setUserInfo(loginToken, userInfo);
		/****-------*/
//		redisOperationService.hdelOfflinePlayerIdRoomIdGameTypeTime(userModel.getPlayerId());
		result.setData(userInfo);
		return result;
	}
	
	public Result login1(String code, String deviceType,HttpServletRequest request) {
		Result result = new Result();
		Integer roomId = null;
		Integer playerId = GameUtil.genPlayerId();
		UserInfo userInfo = new UserInfo();
		userInfo.setPlayerId(playerId);
		userInfo.setRoomId(roomId);
		userInfo.setNickName(String.valueOf(playerId));
		userInfo.setLevel(1);
		userInfo.setServerIp(Constant.localIp);
		userInfo.setPort(String.valueOf(Constant.websocketPort));
		userInfo.setRemoteIp(IPUtil.getRemoteIp(request));
		String loginToken =GameUtil.genToken(playerId);
		userInfo.setToken(loginToken);
		userInfo.setHeadImgUrl("http://wx.qlogo.cn/mmopen/wibbRT31wkCR4W9XNicL2h2pgaLepmrmEsXbWKbV0v9ugtdibibDgR1ybONiaWFtVeVtYWGWhObRiaiaicMgw8zat8Y5p6YzQbjdstE2/0");
		redisOperationService.setUserInfo(loginToken, userInfo);
		
		userInfo.setRoomCardNum(10);
		result.setData(userInfo);
		return result;
	}
	
	public void entryHall(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = null;
		result = new Result();
		result.setMsgType(MsgTypeEnum.entryHall.msgType);
		Integer playerId = request.getMsg().getPlayerId();
		/**将channel与playerId进行映射*/
		channelContainer.addChannel(ctx, playerId);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
		notice(ctx, request, userInfo);
	}
	
	public void createRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setMsgType(MsgTypeEnum.createRoom.msgType);
		result.setGameType(request.getGameType());
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		/**校验创建房间的开关是否打开，如果没打开，就提示用户；此时可能是需要升级，暂停服务*/
		if (!redisOperationService.isCreateRoomFuseOpen()) {
			throw new BusinessException(ExceptionEnum.SYSTEM_UPGRADE);
		}
		RedisRelaModel redisRelaModel = redisOperationService.getRoomIdGameTypeByPlayerId(msg.getPlayerId());
		if (redisRelaModel != null) {
			throw new BusinessException(ExceptionEnum.ALREADY_IN_ROOM.index, ExceptionEnum.ALREADY_IN_ROOM.description + ",房间号：" + redisRelaModel.getRoomId());
		}
		/**校验房卡数量是否足够*/
		//TODO
		if (redisOperationService.isLoginFuseOpen()) {
			Integer roomCardCheckPlayerId = playerId;
			Integer teaHouseNum = redisOperationService.getTeaHouseNumByPlayerId(playerId);
			/**如果玩家是从茶楼牌桌创建房间的，则只需要扣老板房卡*/
			if (teaHouseNum != null) {
				TeaHouseModel teaHouseModel = commonManager.getTeaHouseByTeaHouseNum(teaHouseNum);
				roomCardCheckPlayerId = teaHouseModel.getPlayerId();
			}
			commonManager.roomCardCheck(roomCardCheckPlayerId, request.getGameType(), msg.getPayType(), msg.getTotalGames());
		}
		
		Integer roomId = GameUtil.genRoomId();
		int i = 0;
		while(i < 3){
			/**如果不存在则跳出循环，此房间号可以使用*/
			if (!redisOperationService.isRoomIdExist(roomId)) {
				break;
			}
			/**如果此房间号存在则重新生成*/
			roomId = GameUtil.genRoomId();
			i++;
			if (i >= 3) {
				throw new BusinessException(ExceptionEnum.GEN_ROOM_ID_FAIL);
			}
		}
		/**将当前房间号设置到userInfo中*/
		userInfo.setRoomId(roomId);
		redisOperationService.setUserInfo(request.getToken(), userInfo);
		
		/**doCreateRoom抽象方法由具体实现类去实现*/
		BaseRoomInfo roomInfo = doCreateRoom(ctx, request, userInfo);
		
		/**组装房间对象*/
		roomInfo.setRoomId(roomId);
		roomInfo.setRoomOwnerId(msg.getPlayerId());
		roomInfo.setPayType(msg.getPayType());
		roomInfo.setTotalGames(msg.getTotalGames());
		roomInfo.setPlayerNumLimit(msg.getPlayerNumLimit()==null?12:msg.getPlayerNumLimit());
		roomInfo.setCurGame(0);
		roomInfo.setStatus(RoomStatusEnum.justBegin.status);
		roomInfo.setServerIp(Constant.localIp);
		Date date = new Date();
		roomInfo.setCreateTime(date);
		roomInfo.setUpdateTime(date);
		/**茶楼相关参数**/
		roomInfo.setTeaHouseNum(msg.getTeaHouseNum());
		roomInfo.setTableNum(msg.getTableNum());
		
		List playerList = roomInfo.getPlayerList();
		BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(0);
		playerInfo.setPlayerId(msg.getPlayerId());
		playerInfo.setLevel(1);
		playerInfo.setOrder(1);
		playerInfo.setStatus(PlayerStatusEnum.notReady.status);
		playerInfo.setOnlineStatus(OnlineStatusEnum.online.status);
		playerInfo.setRoomCardNum(10);
		playerInfo.setWinTimes(0);
		playerInfo.setLoseTimes(0);
		/**设置地理位置信息*/
		playerInfo.setAddress(userInfo.getAddress());
		playerInfo.setX(userInfo.getX());
		playerInfo.setY(userInfo.getY());
		/**设置当前用户ip*/
		playerInfo.setIp(userInfo.getRemoteIp());
		playerInfo.setNickName(userInfo.getNickName());
		playerInfo.setHeadImgUrl(userInfo.getHeadImgUrl());
		/**概率控制*/
		playerInfo.setWinProbability(userInfo.getWinProbability());
		
		redisOperationService.setRoomIdGameTypeUpdateTime(roomId, request.getGameType(), new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		redisOperationService.setPlayerIdRoomIdGameType(playerId, roomId, request.getGameType());
		redisOperationService.setTeaHouseNumTableNumRoomId(roomInfo.getTeaHouseNum(), roomInfo.getTableNum(), roomId);
		/**设置返回信息*/
		result.setData(roomInfo);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
		/**设置房间锁，此房间的请求排队进入*/
		RoomLockContainer.setLockByRoomId(roomId, new ReentrantLock());
	}
	
	public abstract BaseRoomInfo doCreateRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo);
	
	public void entryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = null;
		BaseMsg msg = request.getMsg();
		Integer playerId = userInfo.getPlayerId();
		/**进入房间的时候，房间号参数是前台传过来的，所以不能从userInfo里面取得*/
		Integer roomId = msg.getRoomId();
		/**参数为空*/
		if (roomId == null) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		if (!redisOperationService.isRoomIdExist(roomId)) {
			throw new BusinessException(ExceptionEnum.ROOM_ID_NOT_EXIST);
		}
		
		
		BaseRoomInfo roomInfo = doEntryRoom(ctx, request, userInfo);
		
		/**如果是麻将（金花和牛牛除外），那么游戏已经开始，则不允许再加入 add by liujinfengnew*/
		if (roomInfo.getGameType().equals(GameTypeEnum.mj.gameType)) {
			if (roomInfo.getStatus() > RoomStatusEnum.justBegin.status) {
				throw new BusinessException(ExceptionEnum.NOT_IN_READY_STATUS);
			}
		}
		if (redisOperationService.isLoginFuseOpen()) {
			/**如果是aa支付，则校验房卡数量是否足够*/
			if (PayTypeEnum.AAPay.type.equals(roomInfo.getPayType())) {
				commonManager.roomCardCheck(userInfo.getPlayerId(), request.getGameType(), roomInfo.getPayType(), roomInfo.getTotalGames());
			}
		}
		List playerList = roomInfo.getPlayerList();
		int size = playerList.size();
		/**最多12个玩家*/
		if (size >= roomInfo.getPlayerNumLimit()) {
			throw new BusinessException(ExceptionEnum.EXCEED_MAX_PLAYER_NUM);
		}
		userInfo.setRoomId(roomId);
		redisOperationService.setUserInfo(request.getToken(), userInfo);
		boolean isExist = false;
		for(int i = 0; i < playerList.size(); i++ ){
			BasePlayerInfo tempPlayerInfo = (BasePlayerInfo)playerList.get(i);
			/**如果加入的玩家id已经存在*/
			if (playerId.equals(tempPlayerInfo.getPlayerId())) {
				isExist = true;
				break;
			}
		}
		/**如果申请加入房间的玩家已经存在房间中，则只需要走刷新接口*/
		if (isExist) {
			playerList.remove(playerList.size() - 1);
			refreshRoom(ctx, request, userInfo);
			return;
		}
		
		/**取list最后一个，即为本次加入的玩家，设置公共信息*/
		BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(playerList.size() - 1);
		playerInfo.setPlayerId(userInfo.getPlayerId());
		playerInfo.setNickName(userInfo.getNickName());
		playerInfo.setHeadImgUrl(userInfo.getHeadImgUrl());
		playerInfo.setLevel(1);
		/***/
		BasePlayerInfo lastPlayerInfo = (BasePlayerInfo)playerList.get(playerList.size() - 2);
		playerInfo.setOrder(lastPlayerInfo.getOrder() + 1);
		if (roomInfo.getGameType().equals(GameTypeEnum.nn.gameType)) {
			/**加入房间的时候，需要判断当前房间的状态，确定新加入的玩家应该是什么状态*/
			if (roomInfo.getStatus().equals(RoomStatusEnum.justBegin.status) ) {//刚开始准备
				playerInfo.setStatus(PlayerStatusEnum.notReady.status);
			}else if(roomInfo.getStatus().equals(5) ){//小局结束nn是5
				playerInfo.setStatus(PlayerStatusEnum.notReady.status);
			}else if(roomInfo.getStatus().equals(6) ){//一圈结束nn是6
				throw new BusinessException(ExceptionEnum.TOTAL_GAME_OVER);
			}else{//其他状态
				playerInfo.setStatus(PlayerStatusEnum.observer.status);
			}
		}else if(roomInfo.getGameType().equals(GameTypeEnum.jh.gameType)){
			/**加入房间的时候，需要判断当前房间的状态，确定新加入的玩家应该是什么状态*/
			if (roomInfo.getStatus().equals(RoomStatusEnum.justBegin.status) ) {//刚开始准备
				playerInfo.setStatus(PlayerStatusEnum.notReady.status);
			}else if(roomInfo.getStatus().equals(RoomStatusEnum.curGameOver.status) ){//小局结束
				playerInfo.setStatus(PlayerStatusEnum.notReady.status);
			}else if(roomInfo.getStatus().equals(RoomStatusEnum.totalGameOver.status) ){//一圈结束
				throw new BusinessException(ExceptionEnum.TOTAL_GAME_OVER);
			}else{//其他状态
				playerInfo.setStatus(PlayerStatusEnum.observer.status);
			}
		}else{
			playerInfo.setStatus(PlayerStatusEnum.notReady.status);
		}
		
		
		playerInfo.setOnlineStatus(OnlineStatusEnum.online.status);
		playerInfo.setRoomCardNum(10);
		playerInfo.setWinTimes(0);
		playerInfo.setLoseTimes(0);
		playerInfo.setIp(userInfo.getRemoteIp());
		/**设置地理位置信息*/
		playerInfo.setAddress(userInfo.getAddress());
		playerInfo.setX(userInfo.getX());
		playerInfo.setY(userInfo.getY());
		/**概率控制*/
		playerInfo.setWinProbability(userInfo.getWinProbability());
		roomInfo.setUpdateTime(new Date());
		
		redisOperationService.setRoomIdGameTypeUpdateTime(roomId, request.getGameType(), new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		redisOperationService.setPlayerIdRoomIdGameType(userInfo.getPlayerId(), roomId, request.getGameType());
		
		result = new Result();
		/**如果是麻将，则走正常逻辑返回房间信息*/
		if (roomInfo.getGameType().equals(GameTypeEnum.mj.gameType)) {
			result.setGameType(request.getGameType());
			result.setMsgType(MsgTypeEnum.entryRoom.msgType);
			result.setData(roomInfo);
			/**给此房间中的所有玩家发送消息*/
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
		}else{/**如果是炸金花或者斗牛，由于需要玩家随时可以加入，所以需要通过刷新接口给每个玩家返回房间信息*/
			refreshRoomForAllPlayer(roomInfo);
		}
		
	}
	
	public void refreshRoomForAllPlayer(BaseRoomInfo roomInfo){
		Result result = new Result();
		result.setGameType(roomInfo.getGameType());
		result.setMsgType(MsgTypeEnum.refreshRoom.msgType);
		List playerList = roomInfo.getPlayerList();
		int size = playerList.size();
		UserInfo userInfo = new UserInfo();
		BasePlayerInfo playerInfo = null;
		for(int i = 0; i < size; i++){
			playerInfo = (BasePlayerInfo)playerList.get(i);
			userInfo.setRoomId(roomInfo.getRoomId());
			userInfo.setPlayerId(playerInfo.getPlayerId());
			/**复用刷新接口*/
			List<BaseRoomInfo> roomInfoList = doRefreshRoom(null, null, userInfo);
			BaseRoomInfo returnRoomInfo = roomInfoList.get(1);
			result.setData(returnRoomInfo);
			/**返回给当前玩家刷新信息*/
			channelContainer.sendTextMsgByPlayerIds(result, playerInfo.getPlayerId());
		}
	}
	
	public abstract BaseRoomInfo doEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo);
	
	public void dissolveRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		Integer playerId = userInfo.getPlayerId();
		Integer roomId = userInfo.getRoomId();
		BaseRoomInfo roomInfo = getRoomInfo(ctx, request, userInfo);
		
		List playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(playerId, playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		GameUtil.setDissolveStatus(playerList, playerId, DissolveStatusEnum.agree);
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		redisOperationService.setRoomIdGameTypeUpdateTime(roomId, new Date());
		if (playerList.size() == 1) {
			/**解散房间*/
			redisOperationService.cleanPlayerAndRoomInfo(roomId, GameUtil.getPlayerIdStrArr(playerList));
			/**茶楼相关*/
			redisOperationService.delRoomIdByTeaHouseNumTableNum(roomInfo.getTeaHouseNum(), roomInfo.getTableNum());
			/**将用户缓存信息里面的roomId设置为null*/
			userInfo.setRoomId(null);
			redisOperationService.setUserInfo(request.getToken(), userInfo);
			result.setMsgType(MsgTypeEnum.successDissolveRoom.msgType);
			data.put("roomId", roomId);
			channelContainer.sendTextMsgByPlayerIds(result, playerId);
			return;
		}
		
		Integer playerStatus = GameUtil.getPlayerStatus(playerList, playerId);
		/**如果玩家的状态是观察者,并且一局都没有玩过，则随时可以退出*/
		if (playerStatus.equals(PlayerStatusEnum.observer.status) && GameUtil.getPlayedCountByPlayerId(playerList, playerId) < 1) {
			/**删除玩家*/
			GameUtil.removePlayer(playerList, playerId);
			redisOperationService.cleanPlayerAndRoomInfoForSignout(roomId, String.valueOf(playerId));
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			/**将用户缓存信息里面的roomId设置为null*/
			userInfo.setRoomId(null);
			redisOperationService.setUserInfo(request.getToken(), userInfo);
			result.setMsgType(MsgTypeEnum.entryHall.msgType);
			/**当前玩家返回大厅*/
			channelContainer.sendTextMsgByPlayerIds(result, playerId);
			/**其他玩家通知刷新*/
			refreshRoomForAllPlayer(roomInfo);
			return;
		}
		/**如果玩家的状态是未准备,并且房间状态是最开始准备阶段，则玩家可以退出*/
		if (playerStatus.equals(PlayerStatusEnum.notReady.status) && roomInfo.getStatus().equals(RoomStatusEnum.justBegin.status)) {
			
			/**如果在游戏最开始准备阶段退出的是庄家（即房主），则需要设置一个默认的庄家（房主）,当前退出庄家的下家*/
			if (roomInfo.getRoomBankerId().equals(playerId)) {
				Integer tempId = GameUtil.getNextPlayerId(playerList, playerId);
				roomInfo.setRoomOwnerId(tempId);
				if (request.getGameType().equals(GameTypeEnum.jh.gameType)) {
					roomInfo.setRoomBankerId(tempId);
				}
			}
			
			/**删除玩家*/
			GameUtil.removePlayer(playerList, playerId);
			redisOperationService.cleanPlayerAndRoomInfoForSignout(roomId, String.valueOf(playerId));
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			/**将用户缓存信息里面的roomId设置为null*/
			userInfo.setRoomId(null);
			redisOperationService.setUserInfo(request.getToken(), userInfo);
			result.setMsgType(MsgTypeEnum.entryHall.msgType);
			/**当前玩家返回大厅*/
			channelContainer.sendTextMsgByPlayerIds(result, playerId);
			/**其他玩家通知刷新*/
			refreshRoomForAllPlayer(roomInfo);
			return;
		}
		result.setMsgType(MsgTypeEnum.dissolveRoom.msgType);
		data.put("roomId", roomId);
		data.put("playerId", playerId);
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}
	
	
	public void agreeDissolveRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		BaseMsg msg = request.getMsg();
		Integer roomId = msg.getRoomId();
		BaseRoomInfo roomInfo = getRoomInfo(ctx, request, userInfo);
		List playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(msg.getPlayerId(), playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		int size = playerList.size();
		int agreeDissolveCount = 0;
		for(int i = 0; i < size; i++){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			if (player.getPlayerId().equals(msg.getPlayerId())) {
				player.setDissolveStatus(DissolveStatusEnum.agree.status);
			}
			if (DissolveStatusEnum.agree.status.equals(player.getDissolveStatus())) {
				agreeDissolveCount++;
			}
		}
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		/**如果大部分人同意，则推送解散消息并解散房间*/
		if (agreeDissolveCount >= (playerList.size()/2 + 1)) {
			/**解散房间*/
			redisOperationService.cleanPlayerAndRoomInfo(roomId, GameUtil.getPlayerIdStrArr(playerList));
			/**茶楼相关*/
			redisOperationService.delRoomIdByTeaHouseNumTableNum(roomInfo.getTeaHouseNum(), roomInfo.getTableNum());
			/**解散后需要进行结算*/
			data.put("roomInfo", roomInfo);
			result.setMsgType(MsgTypeEnum.successDissolveRoom.msgType);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			return ;
		}
		result.setMsgType(MsgTypeEnum.agreeDissolveRoom.msgType);
		data.put("roomId", roomId);
		data.put("playerId", msg.getPlayerId());
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}
	
	public void disagreeDissolveRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		BaseMsg msg = request.getMsg();
		Integer roomId = msg.getRoomId();
		BaseRoomInfo roomInfo = getRoomInfo(ctx, request, userInfo);
		if (null == roomInfo) {
			throw new BusinessException(ExceptionEnum.ROOM_ID_NOT_EXIST);
		}
		List playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(msg.getPlayerId(), playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			if (player.getPlayerId().equals(msg.getPlayerId())) {
				player.setDissolveStatus(DissolveStatusEnum.disagree.status);
			}
		}
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		result.setMsgType(MsgTypeEnum.disagreeDissolveRoom.msgType);
		data.put("roomId", roomId);
		data.put("playerId", msg.getPlayerId());
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}
	
	public void delRoomConfirmBeforeReturnHall(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		BaseMsg msg = request.getMsg();
		Integer roomId = msg.getRoomId();
		BaseRoomInfo roomInfo = getRoomInfo(ctx, request, userInfo);
		if (null == roomInfo) {
			throw new BusinessException(ExceptionEnum.ROOM_ID_NOT_EXIST);
		}
		List playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(msg.getPlayerId(), playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		
		int agreeDissolveCount = 0;
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			if (player.getPlayerId().equals(msg.getPlayerId())) {
				player.setDissolveStatus(DissolveStatusEnum.agree.status);
			}
			if (player.getDissolveStatus().equals(DissolveStatusEnum.agree.status)) {
				agreeDissolveCount++;
			}
		}
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		/**如果所有人都有确认消息，则解散房间*/
		if (agreeDissolveCount >= playerList.size()) {
			/**解散房间*/
			redisOperationService.cleanPlayerAndRoomInfo(roomId, GameUtil.getPlayerIdStrArr(playerList));
			/**茶楼相关*/
			redisOperationService.delRoomIdByTeaHouseNumTableNum(roomInfo.getTeaHouseNum(), roomInfo.getTableNum());
		}else{/**如果只有部分人确认，则只删除当前玩家的标记*/
			redisOperationService.hdelOfflinePlayerIdRoomIdGameTypeTime(msg.getPlayerId());
			redisOperationService.hdelPlayerIdRoomIdGameType(msg.getPlayerId());
		}
		/**通知玩家返回大厅*/
		result.setMsgType(MsgTypeEnum.delRoomConfirmBeforeReturnHall.msgType);
		channelContainer.sendTextMsgByPlayerIds(result, msg.getPlayerId());
		/**将roomId从用户信息中去除*/
		userInfo.setRoomId(null);
		redisOperationService.setUserInfo(request.getToken(), userInfo);
	}
	
	public void chatMsg(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		BaseMsg msg = request.getMsg();
		Integer roomId = msg.getRoomId();
		BaseRoomInfo roomInfo = getRoomInfo(ctx, request, userInfo);
		if (null == roomInfo) {
			throw new BusinessException(ExceptionEnum.ROOM_ID_NOT_EXIST);
		}
		List playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(msg.getPlayerId(), playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		result.setMsgType(MsgTypeEnum.chatMsg.msgType);
		if (ChatTypeEnum.specialEmotion.type == msg.getChatType()) {
			data.put("playerId", msg.getPlayerId());
			data.put("otherPlayerId", msg.getOtherPlayerId());
			data.put("chatMsg", msg.getChatMsg());
			data.put("chatType", msg.getChatType());
			List<Integer> playerIdList = new ArrayList<Integer>();
			Integer[] playerIdArr = new Integer[2];
			playerIdArr[0] = msg.getPlayerId();
			playerIdArr[1] = msg.getOtherPlayerId();
			channelContainer.sendTextMsgByPlayerIds(result, playerIdArr);
			return;
		}else if(ChatTypeEnum.voiceChat.type == msg.getChatType()){
			data.put("playerId", msg.getPlayerId());
			data.put("chatMsg", msg.getChatMsg());
			data.put("chatType", msg.getChatType());
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArrWithOutSelf(playerList, msg.getPlayerId()));
			return;
		}
		
		data.put("playerId", msg.getPlayerId());
		data.put("chatMsg", msg.getChatMsg());
		data.put("chatType", msg.getChatType());
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}
	
	public void syncPlayerLocation(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.syncPlayerLocation.msgType);
		BaseMsg msg = request.getMsg();
		userInfo.setAddress(msg.getAddress());
		userInfo.setX(msg.getX());
		userInfo.setY(msg.getY());
		redisOperationService.setUserInfo(request.getToken(), userInfo);
	}
	
	public void queryPlayerInfo(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		BaseMsg msg = request.getMsg();
		Integer roomId = msg.getRoomId();
		BaseRoomInfo roomInfo = getRoomInfo(ctx, request, userInfo);
		if (null == roomInfo) {
			throw new BusinessException(ExceptionEnum.ROOM_ID_NOT_EXIST);
		}
		List playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(msg.getPlayerId(), playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		Integer otherPlayerId = msg.getOtherPlayerId();
		Integer playerId = msg.getPlayerId();
		BasePlayerInfo otherPlayer = null;
		BasePlayerInfo curPlayer = null;
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			if (player.getPlayerId().equals(otherPlayerId)) {
				otherPlayer = player;
			}else if(player.getPlayerId().equals(playerId)){
				curPlayer = player;
			}
		}
		if (otherPlayer != null) {
			data.put("playerId", otherPlayer.getPlayerId());
			data.put("nickName", otherPlayer.getNickName());
			data.put("headImgUrl", otherPlayer.getHeadImgUrl());
			data.put("address", otherPlayer.getAddress());
			String distance = GameUtil.getLatLngDistance(curPlayer, otherPlayer);
			data.put("distance", distance);
		}else{
			data.put("playerId", curPlayer.getPlayerId());
			data.put("nickName", curPlayer.getNickName());
			data.put("headImgUrl", curPlayer.getHeadImgUrl());
			data.put("address", curPlayer.getAddress());
		}
		result.setMsgType(MsgTypeEnum.queryPlayerInfo.msgType);
		channelContainer.sendTextMsgByPlayerIds(result, msg.getPlayerId());
	}
	
	public void userRecord(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		BaseMsg msg = request.getMsg();
		UserRecordModel qmodel = new UserRecordModel();
		qmodel.setGameType(request.getGameType());
		qmodel.setPlayerId(userInfo.getPlayerId());
		List<UserRecordModel> list = commonManager.getUserRecord(qmodel);
		result.setMsgType(MsgTypeEnum.userRecord.msgType);
		result.setData(list);
		channelContainer.sendTextMsgByPlayerIds(result, msg.getPlayerId());
	}

	public void userFeedback(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		BaseMsg msg = request.getMsg();
		UserFeedbackModel model = new UserFeedbackModel();
		model.setPlayerId(msg.getPlayerId());
		model.setMobilePhone(msg.getMobilePhone());
		model.setFeedBack(msg.getFeedBack());
		model.setType(msg.getFeedBackType());
		commonManager.insertFeedback(model);
		result.setMsgType(MsgTypeEnum.userFeedback.msgType);
		channelContainer.sendTextMsgByPlayerIds(result, msg.getPlayerId());
	}
	
	public abstract BaseRoomInfo getRoomInfo(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo);
	
	public void ready(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){}
	
	public void refreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		
		BaseMsg msg = request.getMsg();
		Integer roomId = msg.getRoomId();
		Integer playerId = msg.getPlayerId();
		List<BaseRoomInfo> roomInfoList = doRefreshRoom(ctx, request, userInfo);
		BaseRoomInfo roomInfo = roomInfoList.get(0);
		BaseRoomInfo returnRoomInfo = roomInfoList.get(1);
		if (null == roomInfo) {
			/**房间不存在，则需要删除离线用户与房间的关系标记，防止循环刷新，但是房间不存在*/
			redisOperationService.hdelOfflinePlayerIdRoomIdGameTypeTime(playerId);
			channelContainer.sendTextMsgByPlayerIds(new Result(0, MsgTypeEnum.entryHall.msgType), playerId);
			return;
		}
		List playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(playerId, playerList)) {
			channelContainer.sendTextMsgByPlayerIds(new Result(0, MsgTypeEnum.entryHall.msgType), playerId);
			return;
		}
		/**茶楼相关*/
		returnRoomInfo.setTeaHouseNum(roomInfo.getTeaHouseNum());
		returnRoomInfo.setTableNum(roomInfo.getTableNum());
		
		result.setGameType(roomInfo.getGameType());
		result.setMsgType(MsgTypeEnum.refreshRoom.msgType);
		result.setData(returnRoomInfo);
		/**返回给当前玩家刷新信息*/
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
		
		
		/**设置当前玩家缓存中为在线状态*/
		GameUtil.setOnlineStatus(playerList, playerId, OnlineStatusEnum.online);
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		/**给其他的玩家发送当前玩家上线通知*/
		Result result1 = new Result();
		Map<String, Object> data1 = new HashMap<String, Object>();
		result1.setData(data1);
		data1.put("playerId", msg.getPlayerId());
		result1.setGameType(roomInfo.getGameType());
		result1.setMsgType(MsgTypeEnum.onlineNotice.msgType);
		channelContainer.sendTextMsgByPlayerIds(result1, GameUtil.getPlayerIdArrWithOutSelf(playerList, playerId));
		/**删除此玩家的离线标记*/
		redisOperationService.hdelOfflinePlayerIdRoomIdGameTypeTime(playerId);
	}
	
	public abstract List<BaseRoomInfo> doRefreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo);
	
	public void productList(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(GameTypeEnum.common.gameType);
		result.setMsgType(MsgTypeEnum.productList.msgType);
		commonManager.getProductList();
		result.setData(commonManager.getProductList());
		/**返回给当前玩家刷新信息*/
		channelContainer.sendTextMsgByPlayerIds(result, userInfo.getPlayerId());
	}
	
	public void bindProxy(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Integer proxyId = request.getMsg().getProxyId();
		if (proxyId == null) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		Integer proxyCount = commonManager.getProxyCountByProxyId(proxyId);
		if (proxyCount < 1) {
			throw new BusinessException(ExceptionEnum.PROXY_NOT_EXIST);
		}
		Integer proxyUserCount = commonManager.getProxyUserCountByPlayerId(userInfo.getPlayerId());
		if (proxyUserCount > 0) {
			throw new BusinessException(ExceptionEnum.HAS_BIND_PROXY);
		}
		commonManager.insertProxyUser(proxyId, userInfo.getPlayerId(), userInfo.getNickName());
		/**绑定代理后送15张房卡*/
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("addNum", 15);
		param.put("playerId", userInfo.getPlayerId());
		commonManager.addRoomCard(param);
		/**更新后再查询*/
		UserModel userModel = commonManager.getUserById(userInfo.getPlayerId());
		/**推送房卡更新消息*/
		roomCardNumUpdate(userModel.getRoomCardNum(), userInfo.getPlayerId());
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(GameTypeEnum.common.gameType);
		result.setMsgType(MsgTypeEnum.bindProxy.msgType);
		channelContainer.sendTextMsgByPlayerIds(result, userInfo.getPlayerId());
	}
	
	public void checkBindProxy(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		if (!"sjsj".equals(Constant.curCompany)) {
			Integer proxyId = commonManager.getProxyIdByPlayerId(userInfo.getPlayerId());
			if (proxyId == null) {
				throw new BusinessException(ExceptionEnum.NEED_BIND_PROXY);
			}
		}
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(GameTypeEnum.common.gameType);
		result.setMsgType(MsgTypeEnum.checkBindProxy.msgType);
		channelContainer.sendTextMsgByPlayerIds(result, userInfo.getPlayerId());
	}
	
	public void notice(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
        Result result = new Result();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("noticeContent", Constant.noticeMsg);
        result.setData(data);
        result.setGameType(GameTypeEnum.common.gameType);
        result.setMsgType(MsgTypeEnum.notice.msgType);
        channelContainer.sendTextMsgByPlayerIds(result, userInfo.getPlayerId());
    }
	
	/**
	 *  微信预支付 统一下单入口(websocket协议)
	 * @param productId
	 * @param playerId
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public void unifiedOrder(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) throws Exception{
		BaseMsg msg = request.getMsg();
		Integer productId = msg.getProductId();
		Integer playerId = userInfo.getPlayerId();
		String ip = userInfo.getRemoteIp();
		
		ProductModel productModel = commonManager.getProductById(productId);
		if (productModel == null) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		Long orderId = commonManager.insertOrder(playerId, productId, productModel.getRoomCardNum(), productModel.getPrice());
		SortedMap<String, Object> parameters = prepareOrder(ip, String.valueOf(orderId), productModel.getPrice(), productModel.getRemark());
		/**生成签名*/
		parameters.put("sign", PayCommonUtil.createSign(Charsets.UTF_8.toString(), parameters));
		/**生成xml格式字符串*/
		String requestXML = PayCommonUtil.getRequestXml(parameters);
		String responseStr = HttpUtil.httpsRequest(Constant.UNIFIED_ORDER_URL, "POST", requestXML);
		/**检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改*/
		if (!PayCommonUtil.checkIsSignValidFromResponseString(responseStr)) {
			log.error("微信统一下单失败,签名可能被篡改 "+responseStr);
			throw new BusinessException(ExceptionEnum.UNIFIED_ORDER_FAIL);
		}
		/**解析结果 resultStr*/
		SortedMap<String, Object> resutlMap = XMLUtil.doXMLParse(responseStr);
		if (resutlMap != null && WeixinConstant.FAIL.equals(resutlMap.get("return_code"))) {
			log.error("微信统一下单失败,订单编号: " + orderId + " 失败原因:"+ resutlMap.get("return_msg"));
			throw new BusinessException(ExceptionEnum.UNIFIED_ORDER_FAIL);
		}
		/**获取到 prepayid*/
		/**商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再在APP里面调起支付。*/
		SortedMap<String, Object> map = buildClientJson(resutlMap);
		map.put("outTradeNo", orderId);
		log.info("统一下定单成功 "+map.toString());
		
		Result result = new Result();
		result.setGameType(GameTypeEnum.common.gameType);
		result.setMsgType(MsgTypeEnum.unifiedOrder.msgType);
		result.setData(map);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	/**
	 *  微信预支付 统一下单入口(http协议)
	 * @param productId
	 * @param playerId
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public Result unifiedOrder(Integer productId, Integer playerId, String ip) throws Exception{
		Result result = new Result();
		ProductModel productModel = commonManager.getProductById(productId);
		if (productModel == null) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		Long orderId = commonManager.insertOrder(playerId, productId, productModel.getRoomCardNum(), productModel.getPrice());
		SortedMap<String, Object> parameters = prepareOrder(ip, String.valueOf(orderId), productModel.getPrice(), productModel.getRemark());
		/**生成签名*/
		parameters.put("sign", PayCommonUtil.createSign(Charsets.UTF_8.toString(), parameters));
		/**生成xml格式字符串*/
		String requestXML = PayCommonUtil.getRequestXml(parameters);
		String responseStr = HttpUtil.httpsRequest(Constant.UNIFIED_ORDER_URL, "POST", requestXML);
		/**检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改*/
		if (!PayCommonUtil.checkIsSignValidFromResponseString(responseStr)) {
			log.error("微信统一下单失败,签名可能被篡改 "+responseStr);
			throw new BusinessException(ExceptionEnum.UNIFIED_ORDER_FAIL);
		}
		/**解析结果 resultStr*/
		SortedMap<String, Object> resutlMap = XMLUtil.doXMLParse(responseStr);
		if (resutlMap != null && WeixinConstant.FAIL.equals(resutlMap.get("return_code"))) {
			log.error("微信统一下单失败,订单编号: " + orderId + " 失败原因:"+ resutlMap.get("return_msg"));
			throw new BusinessException(ExceptionEnum.UNIFIED_ORDER_FAIL);
		}
		/**获取到 prepayid*/
		/**商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再在APP里面调起支付。*/
		SortedMap<String, Object> map = buildClientJson(resutlMap);
		map.put("outTradeNo", orderId);
		log.info("统一下定单成功 "+map.toString());
		result.setData(map);
		return result;
	}
	
	
	/**
	 * 微信回调告诉微信支付结果 注意：同样的通知可能会多次发送给此接口，注意处理重复的通知。
	 * 对于支付结果通知的内容做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
	 * 
	 * @param params
	 * @return
	 */
	public String callback(String responseStr) {
		try {
			Map<String, Object> map = XMLUtil.doXMLParse(responseStr);
			/**校验签名 防止数据泄漏导致出现“假通知”，造成资金损失*/
			if (!PayCommonUtil.checkIsSignValidFromResponseString(responseStr)) {
				log.error("微信回调失败,签名可能被篡改 " + responseStr);
				return PayCommonUtil.setXML(WeixinConstant.FAIL, "invalid sign");
			}
			if (WeixinConstant.FAIL.equalsIgnoreCase(map.get("result_code").toString())) {
				log.error("微信回调失败的原因："+responseStr);
				return PayCommonUtil.setXML(WeixinConstant.FAIL, "weixin pay fail");
			}
			if (WeixinConstant.SUCCESS.equalsIgnoreCase(map.get("result_code")
					.toString())) {
				/**对数据库的操作,更新订单状态为已付款*/
				String outTradeNo = (String) map.get("out_trade_no");
				String transactionId = (String) map.get("transaction_id");
				String totlaFee = (String) map.get("total_fee");
				Integer totalPrice = Integer.valueOf(totlaFee);
				/**根据订单号查询订单信息*/
				OrderModel order = commonManager.getOderByOrderId(Long.valueOf(outTradeNo));
				Integer payStatus = order.getPayStatus();
				
				/**如果支付状态为已经支付，则说明此次回调为重复回调，直接返回成功*/
				if (PayStatusEnum.pay.type.equals(payStatus)) {
					return PayCommonUtil.setXML(WeixinConstant.SUCCESS, "OK");
				}
				Integer playerId = order.getPlayerId();
				Integer roomCardNum = commonManager.updateOrderAndUser(playerId, order.getRoomCardNum(), Long.valueOf(outTradeNo), transactionId, totalPrice);
				
				/**推送房卡更新消息*/
				roomCardNumUpdate(roomCardNum, playerId);
				
				/**告诉微信服务器，我收到信息了，不要在调用回调action了*/
				log.info("回调成功："+responseStr);
				return PayCommonUtil.setXML(WeixinConstant.SUCCESS, "OK");
			}
		} catch (Exception e) {
			log.error("回调异常" + e.getMessage());
			return PayCommonUtil.setXML(WeixinConstant.FAIL,"weixin pay server exception");
		}
		return PayCommonUtil.setXML(WeixinConstant.FAIL, "weixin pay fail");
	}
	
	public void roomCardNumUpdate( Integer roomCardNum, Integer playerId){
		/**推送房卡更新消息*/
		Result result = new Result();
		result.setMsgType(MsgTypeEnum.roomCardNumUpdate.msgType);
		result.setGameType(0);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerId", playerId);
		data.put("roomCardNum", roomCardNum);
		result.setData(data);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	/**
	 * 生成订单信息
	 * 
	 * @param ip
	 * @param orderId
	 * @return
	 */
	private SortedMap<String, Object> prepareOrder(String ip, String orderId, int price, String productBody) {
		Map<String, Object> oparams = ImmutableMap.<String, Object> builder()
				.put("appid", Constant.APPID)// 服务号的应用号
				.put("body", productBody)// 商品描述
				.put("mch_id", Constant.MCH_ID)// 商户号 ？
				.put("nonce_str", PayCommonUtil.CreateNoncestr())// 16随机字符串(大小写字母加数字)
				.put("out_trade_no", orderId)// 商户订单号
				.put("total_fee", price)// 支付金额 单位分 注意:前端负责传入分
				.put("spbill_create_ip", ip)// IP地址
				.put("notify_url", Constant.WEIXIN_PAY_CALL_BACK_URL) // 微信回调地址
				.put("trade_type", Constant.TRADE_TYPE)// 支付类型 app
				.build();
		return MapUtils.sortMap(oparams);
	}
	
	/**
	 * 生成预付快订单完成，返回给android,ios唤起微信所需要的参数。
	 * 
	 * @param resutlMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private SortedMap<String, Object> buildClientJson(
			Map<String, Object> resutlMap) throws UnsupportedEncodingException {
		// 获取微信返回的签名
		Map<String, Object> params = ImmutableMap.<String, Object> builder()
				.put("appid", Constant.APPID)
				.put("noncestr", PayCommonUtil.CreateNoncestr())
				.put("package", "Sign=WXPay")
				.put("partnerid", Constant.MCH_ID)
				.put("prepayid", resutlMap.get("prepay_id"))
				.put("timestamp", DateUtils.getTimeStamp()) // 10 位时间戳
				.build();
		// key ASCII排序 // 这里用treemap也是可以的 可以用treemap // TODO
		SortedMap<String, Object> sortMap = MapUtils.sortMap(params);
		sortMap.put("package", "Sign=WXPay");
		// paySign的生成规则和Sign的生成规则同理
		String paySign = PayCommonUtil.createSign(Charsets.UTF_8.toString(), sortMap);
		sortMap.put("sign", paySign);
		return sortMap;
	}
	
	/*************以下为茶楼相关*********************/
	
	public void createTeaHouse(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.createTeaHouse.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setPlayerId(playerId);
		teaHouseModel.setNickName(userInfo.getNickName());
		Integer teaHouseNum = GameUtil.genTeaHouseNum();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setTeaHouseName(String.valueOf(teaHouseNum));
		Integer gameType = request.getGameType();
		teaHouseModel.setGameType(gameType);
		
		teaHouseModel.setTotalGame(msg.getTotalGames());
		teaHouseModel.setPayType(msg.getPayType());
		GameTypeEnum gameTypeEnum = GameTypeEnum.getGameTypeEnumByType(gameType);
		
		switch (gameTypeEnum) {
			case nn:
				NnMsg nnMsg = (NnMsg)msg;
				teaHouseModel.setRoomBankerType(nnMsg.getRoomBankerType());
				teaHouseModel.setMultipleLimit(nnMsg.getMultipleLimit());
				teaHouseModel.setButtomScoreType(nnMsg.getButtomScoreType());
				data.put("roomBankerType", nnMsg.getRoomBankerType());
				data.put("multipleLimit", nnMsg.getMultipleLimit());
				data.put("buttomScoreType", nnMsg.getButtomScoreType());
				break;
			case mj:
				break;
			case jh:
				JhMsg jhMsg = (JhMsg)msg;
				break;
			default:
				break;
			}
		commonManager.createTeaHouse(teaHouseModel);
		data.put("teaHouseNum", teaHouseNum);
		data.put("totalGame", msg.getTotalGames());
		data.put("payType", msg.getPayType());
		data.put("playerId", playerId);
		/**设置playerId与茶楼号的关系，记忆下次进入*/
		redisOperationService.setPlayerIdTeaHouseNum(playerId, teaHouseNum);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public void queryPlayerTeaHouseList(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.queryPlayerTeaHouseList.msgType);
		Integer playerId = request.getMsg().getPlayerId();
		result.setData(commonManager.queryPlayerTeaHouseList(playerId));
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	public void queryPlayerJoinedTeaHouseList(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.queryPlayerJoinedTeaHouseList.msgType);
		Integer playerId = request.getMsg().getPlayerId();
		result.setData(commonManager.queryPlayerJoinedTeaHouseList(playerId));
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public void queryAllPlayerRelatedTeaHouse(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.queryAllPlayerRelatedTeaHouse.msgType);
		Integer playerId = request.getMsg().getPlayerId();
		List<TeaHouseModel> playerTeaHouseList = commonManager.queryPlayerTeaHouseList(playerId);
		List<TeaHouseModel> playerJoinedTeaHouseList = commonManager.queryPlayerJoinedTeaHouseList(playerId);
		Map<Integer, TeaHouseModel> map = new HashMap<Integer, TeaHouseModel>();
		for(TeaHouseModel teaHouseModel : playerTeaHouseList){
			map.put(teaHouseModel.getTeaHouseNum(), teaHouseModel);
		}
		for(TeaHouseModel teaHouseModel : playerJoinedTeaHouseList){
			map.put(teaHouseModel.getTeaHouseNum(), teaHouseModel);
		}
		List<TeaHouseModel> resList = new ArrayList<TeaHouseModel>();
		Set<Entry<Integer, TeaHouseModel>> set = map.entrySet();
		for(Entry<Integer, TeaHouseModel> en : set){
			resList.add(en.getValue());
		}
		result.setData(resList);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public static void main(String[] args) {
		TeaHouseModel model = new TeaHouseModel();
		model.setTeaHouseNum(111);
		model.setPlayerId(222);
		model.setNickName("test");
		List<TeaHouseModel> playerTeaHouseList = new ArrayList<TeaHouseModel>();
		playerTeaHouseList.add(model);
		List<TeaHouseModel> playerJoinedTeaHouseList = new ArrayList<TeaHouseModel>();
		playerJoinedTeaHouseList.add(model);
		int size = playerJoinedTeaHouseList.size();
		List<Integer> needRemoveList = new ArrayList<Integer>();
		for(TeaHouseModel teaHouseModel : playerTeaHouseList){
			for(int i = 0; i < size; i++){
				TeaHouseModel joinedTeaHouseModel = playerJoinedTeaHouseList.get(i);
				if (teaHouseModel.getTeaHouseNum().equals(joinedTeaHouseModel.getTeaHouseNum())) {
					needRemoveList.add(i);
				}
			}
		}
		size = needRemoveList.size();
		for(int i = 0; i < size; i++){
			playerJoinedTeaHouseList.remove(i);
		}
		playerTeaHouseList.addAll(playerJoinedTeaHouseList);
		System.out.println(JsonUtil.toJson(playerTeaHouseList));
	}
	
	/**
	 * 从茶楼列表进入茶楼
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void entryTeaHouse(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.entryTeaHouse.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		Integer teaHouseNum = msg.getTeaHouseNum();
		/**设置茶楼类型信息*/
		result.setData(commonManager.getTeaHouseTypeByTeaHouseNum(teaHouseNum,playerId));
		/**设置玩家id与茶楼号的关系，记忆下次直接进入茶楼*/
		redisOperationService.setPlayerIdTeaHouseNum(playerId, teaHouseNum);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	/**
	 * 从大厅输入茶楼号加入茶楼
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void joinTeaHouse(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.joinTeaHouse.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		TeaHouseModel model = commonManager.getTeaHouseByTeaHouseNum(msg.getTeaHouseNum());
		if (model == null) {
			throw new BusinessException(ExceptionEnum.TEA_HOUSE_NOT_EXIST);
		}
		boolean isIn = commonManager.isPlayerInTeaHouse(msg.getTeaHouseNum(), playerId);
		/**如果当前玩家已经在茶楼中,则直接进入茶楼*/
		if (isIn) {
			entryTeaHouse(ctx, request, userInfo);
			return;
		}
		/**如果茶楼需要审核*/
		if (model.getIsNeedAudit() > 0) {
			commonManager.joinTeaHouse(msg.getTeaHouseNum(), playerId, userInfo.getNickName(), 0);
			channelContainer.sendTextMsgByPlayerIds(result, playerId);
		}else{/**如果不需要审核*/
			commonManager.joinTeaHouse(msg.getTeaHouseNum(), playerId, userInfo.getNickName(), 1);
			entryTeaHouse(ctx, request, userInfo);
		}
	}
	
	public void queryTeaHousePlayerList(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.queryTeaHousePlayerList.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		result.setData(commonManager.queryTeaHousePlayerList(msg.getTeaHouseNum()));
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public void queryTeaHouseTablePlayerList(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.queryTeaHouseTablePlayerList.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		Integer teaHouseNum = msg.getTeaHouseNum();
		List<Integer> tablePlayerNumList = new ArrayList<Integer>();
		for(int tableNum = 1; tableNum < 9; tableNum++){
			Integer roomId = redisOperationService.getRoomIdByTeaHouseNumTableNum(teaHouseNum, tableNum);
			if (roomId != null) {
				userInfo.setRoomId(roomId);
				BaseRoomInfo roomInfo = getRoomInfo(ctx, request, userInfo);
				tablePlayerNumList.add(roomInfo.getPlayerList().size());
			}else{
				tablePlayerNumList.add(0);
			}
		}
		result.setData(tablePlayerNumList);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public void queryTableDetail(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.queryTableDetail.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		Integer teaHouseNum = msg.getTeaHouseNum();
		Integer tableNum = msg.getTableNum();
		Integer roomId = redisOperationService.getRoomIdByTeaHouseNumTableNum(teaHouseNum, tableNum);
		List<RecordModel> returnList = new ArrayList<RecordModel>(); 
		if (roomId != null) {
			userInfo.setRoomId(roomId);
			BaseRoomInfo roomInfo = getRoomInfo(ctx, request, userInfo);
			List playerList = roomInfo.getPlayerList();
			int size = playerList.size();
			for(int i = 0; i < size; i++){
				RecordModel modle = new RecordModel();
				BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
				modle.setPlayerId(playerId);
				modle.setNickName(playerInfo.getNickName());
				modle.setIp(playerInfo.getIp());
				returnList.add(modle);
			}
		}
		result.setData(returnList);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	
	public void myTeaHouseRecord(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.myTeaHouseRecord.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		List<UserRecordModel> list = commonManager.getMyTeaHouseRecord(msg.getTeaHouseNum(),playerId);
		result.setData(list);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public void teaHouseBigWinner(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.teaHouseBigWinner.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		result.setData(commonManager.getTeaHouseBigWinner(msg.getTeaHouseNum()));
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public void tuhaoBoard(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.tuhaoBoard.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public void paishenBoard(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.paishenBoard.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		result.setData(commonManager.getPaishenBoard());
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	public void openRoomList(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.openRoomList.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	/**
	 * 查询需要审核的玩家列表（楼主和店小二才有权限）
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void queryNeedAuditPlayerList(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.queryNeedAuditPlayerList.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		result.setData(commonManager.queryNeedAuditPlayerList(msg.getTeaHouseNum(), playerId));
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	/**
	 * 审核同意/拒绝（楼主和店小二才有权限）
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void auditEntryTeaHouse(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.auditEntryTeaHouse.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		commonManager.auditEntryTeaHouse(msg.getTeaHouseNum(),msg.getPlayerId(), msg.getOtherPlayerId(), msg.getStatus());
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	/**
	 * 楼主将玩家移除茶楼（楼主和店小二才有权限，但是不能移除自己）
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void delTeaHouseUser(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.delTeaHouseUser.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		commonManager.delTeaHouseUser(msg.getTeaHouseNum(), msg.getOtherPlayerId(), playerId);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	/**
	 * 删除茶楼（楼主才有权限）
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void delTeaHouse(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		commonManager.delTeaHouse(msg.getTeaHouseNum(),playerId);
		
		/**如果删除的茶楼是当前玩家进入的茶楼，则删除茶楼后需要退出到大厅*/
		Integer hasEntryTeaHouseNum = redisOperationService.getTeaHouseNumByPlayerId(playerId);
		if (msg.getTeaHouseNum().equals(hasEntryTeaHouseNum)) {
			exitTeaHouse(ctx, request, userInfo);
		}else{/**如果删除的茶楼不是当前玩家进入的茶楼*/
			Result result = new Result();
			result.setGameType(request.getGameType());
			result.setMsgType(MsgTypeEnum.delTeaHouse.msgType);
			channelContainer.sendTextMsgByPlayerIds(result, playerId);
		}
		
	}
	/**
	 * 退出已经加入的茶楼
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void delFromTeaHouse(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		commonManager.delFromTeaHouse(msg.getTeaHouseNum(), playerId);
		/**如果退出的茶楼是当前玩家进入的茶楼，则退出茶楼后需要退出到大厅*/
		Integer hasEntryTeaHouseNum = redisOperationService.getTeaHouseNumByPlayerId(playerId);
		if (msg.getTeaHouseNum().equals(hasEntryTeaHouseNum)) {
			exitTeaHouse(ctx, request, userInfo);
		}else{/**如果退出的茶楼不是当前玩家进入的茶楼*/
			Result result = new Result();
			result.setGameType(request.getGameType());
			result.setMsgType(MsgTypeEnum.delFromTeaHouse.msgType);
			channelContainer.sendTextMsgByPlayerIds(result, playerId);
		}
		
		
	}
	/**
	 * 茶楼设置（楼主和店小二才有权限）
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void teaHouseConfig(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.teaHouseConfig.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		commonManager.teaHouseConfig(msg.getTeaHouseNum(), playerId, msg.getTeaHouseOwnerWord(),msg.getIsNeedAudit());
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	/**
	 * 设置/取消店小二（楼主才有权限）
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void setDianXiaoer(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.setDianXiaoer.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		Integer otherPlayerId = msg.getOtherPlayerId();
		commonManager.setDianXiaoer(msg.getTeaHouseNum(), playerId, otherPlayerId, msg.getIsDianXiaoer());
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	/**
	 * 退出茶楼到大厅
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void exitTeaHouse(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.exitTeaHouse.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		/**退出茶楼需要删除playerId与茶楼号的对应关系，去掉记忆*/
		redisOperationService.hdelPlayerIdTeaHouseNum(playerId);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
	
	/**
	 * 茶楼战绩（楼主和店小二才有权限）
	 * @param ctx
	 * @param request
	 * @param userInfo
	 */
	public void teaHouseRecord(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		result.setGameType(request.getGameType());
		result.setMsgType(MsgTypeEnum.teaHouseRecord.msgType);
		BaseMsg msg = request.getMsg();
		Integer playerId = msg.getPlayerId();
		Integer teaHouseNum = redisOperationService.getTeaHouseNumByPlayerId(playerId);
		List<UserRecordModel> list = commonManager.getTeaHouseRecord(teaHouseNum, playerId);
		result.setData(list);
		channelContainer.sendTextMsgByPlayerIds(result, playerId);
	}
}
