package cn.worldwalker.game.wyqp.jh.service;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.Card;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.domain.base.UserModel;
import cn.worldwalker.game.wyqp.common.domain.jh.JhMsg;
import cn.worldwalker.game.wyqp.common.domain.jh.JhPlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.jh.JhRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.DissolveStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.MsgTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.OnlineStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.RoomCardOperationEnum;
import cn.worldwalker.game.wyqp.common.enums.RoomStatusEnum;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import cn.worldwalker.game.wyqp.common.utils.GameUtil;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;
import cn.worldwalker.game.wyqp.jh.cards.JhCardResource;
import cn.worldwalker.game.wyqp.jh.cards.JhCardRule;
import cn.worldwalker.game.wyqp.jh.enums.JhPlayerStatusEnum;
import cn.worldwalker.game.wyqp.jh.enums.JhRoomStatusEnum;
@Service(value="jhGameService")
public class JhGameService extends BaseGameService{
	
	@Override
	public BaseRoomInfo doCreateRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		JhMsg msg = (JhMsg)request.getMsg();
		JhRoomInfo roomInfo = new JhRoomInfo();
		roomInfo.setGameType(GameTypeEnum.jh.gameType);
		roomInfo.setRoomBankerId(msg.getPlayerId());
		roomInfo.setStakeButtom(Constant.stakeButtom);
		roomInfo.setStakeLimit(Constant.stakeLimit);
		roomInfo.setStakeTimesLimit(Constant.stakeTimesLimit);
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		JhPlayerInfo player = new JhPlayerInfo();
		playerList.add(player);
		return roomInfo;
	}

	@Override
	public BaseRoomInfo doEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		BaseMsg msg = request.getMsg();
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(msg.getRoomId(), JhRoomInfo.class);
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		JhPlayerInfo player = new JhPlayerInfo();
		playerList.add(player);
		return roomInfo;
	}

	public void ready(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		result.setGameType(GameTypeEnum.jh.gameType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		Integer playerId = userInfo.getPlayerId();
		final Integer roomId = userInfo.getRoomId();
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		/**如果玩家点击准备的时候房间已经在游戏中状态，说明可能是点击关闭结算窗口操作，并且刚好10秒倒计时已经发牌了，产生了并发，避免玩家状态乱了，这里需要做控制*/
		if (roomInfo.getStatus().equals(JhRoomStatusEnum.inGame.status)) {
			result.setMsgType(MsgTypeEnum.ready.msgType);
			data.put("playerId", userInfo.getPlayerId());
			channelContainer.sendTextMsgByPlayerIds(result, playerId);
			return;
		}
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		/**玩家已经准备计数*/
		int readyCount = 0;
		/**观察者玩家计数  addby liujinfengnew*/
		int observerCount = 0;
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			JhPlayerInfo player = playerList.get(i);
			if (player.getPlayerId().equals(playerId)) {
				/**设置状态为已准备*/
				player.setStatus(JhPlayerStatusEnum.ready.status);
			}
			if (JhPlayerStatusEnum.ready.status.equals(player.getStatus())) {
				readyCount++;
			}
			if (JhPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				observerCount++;
			}
		}
		
		if (readyCount > 1 && readyCount == (size - observerCount)) {
			
			/**开始发牌时将房间内当前局数+1*/
			roomInfo.setCurGame(roomInfo.getCurGame() + 1);
			/**发牌*/
			List<List<Card>> playerCards = JhCardResource.dealCards(size);
			/**为每个玩家设置牌及牌型*/
			for(int i = 0; i < size; i++ ){
				JhPlayerInfo player = playerList.get(i);
				player.setCardList(playerCards.get(i));
				player.setCardType(JhCardRule.calculateCardType(playerCards.get(i)));
				player.setStatus(JhPlayerStatusEnum.notWatch.status);
				player.setStakeTimes(0);
				player.setCurTotalStakeScore(0);
				player.setCurScore(0);
				player.setCurStakeScore(0);
				/**设置每个玩家的解散房间状态为不同意解散，后面大结算返回大厅的时候回根据此状态判断是否解散房间*/
				player.setDissolveStatus(DissolveStatusEnum.disagree.status);
			}
			/**概率控制*/
			JhCardRule.probabilityProcess(roomInfo);
			/**根据庄家id获取最开始出牌玩家的id*/
			Integer nextOperatePlayerId = GameUtil.getNextOperatePlayerIdByRoomBankerId(playerList, roomInfo.getRoomBankerId());
			roomInfo.setCurPlayerId(nextOperatePlayerId);
			roomInfo.setStatus(JhRoomStatusEnum.inGame.status);
			roomInfo.setUpdateTime(new Date());
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			/**删除未准备10秒计时器*/
			redisOperationService.delNotReadyIpRoomIdTime(roomId);
			/**发牌返回信息*/
			result.setMsgType(MsgTypeEnum.dealCards.msgType);
			Map<String, Object> roomInfoMap = new HashMap<String, Object>();
			roomInfoMap.put("roomId", roomInfo.getRoomId());
			roomInfoMap.put("roomOwnerId", roomInfo.getRoomOwnerId());
			roomInfoMap.put("roomBankerId", roomInfo.getRoomBankerId());
			/**庄家的下家第一个说话*/
			roomInfoMap.put("curPlayerId", nextOperatePlayerId);
			roomInfoMap.put("totalGames", roomInfo.getTotalGames());
			roomInfoMap.put("curGame", roomInfo.getCurGame());
			result.setData(roomInfoMap);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			return;
		}
		
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		result.setMsgType(MsgTypeEnum.ready.msgType);
		data.put("playerId", userInfo.getPlayerId());
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
		/**如果准备的玩家数为2，则启动计时器，并返回消息告诉前端开始10秒计时*/
		if (readyCount == 1 && (size - observerCount) > 1) {
			redisOperationService.setNotReadyIpRoomIdTime(roomId);
			result.setMsgType(MsgTypeEnum.notReadyTimer.msgType);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			return;
		}
	}
	
	
	public void stake(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(GameTypeEnum.jh.gameType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		
		JhMsg msg = (JhMsg)request.getMsg();
		Integer playerId = userInfo.getPlayerId();
		Integer roomId = userInfo.getRoomId();
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		if (null == roomInfo) {
			throw new BusinessException(ExceptionEnum.ROOM_ID_NOT_EXIST);
		}
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(msg.getPlayerId(), playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		/**如果跟注人不是当前说话人的id，则直接返回提示*/
		if (!msg.getPlayerId().equals(roomInfo.getCurPlayerId())) {
			throw new BusinessException(ExceptionEnum.IS_NOT_YOUR_TURN);
		}
		/**删除当前玩家120s未操作标记*/
		redisOperationService.delJhNoOperationIpPlayerIdRoomIdTime(playerId);
		
		Integer prePlayerStatus = roomInfo.getPrePlayerStatus();
		Integer prePlayerStakeScore = roomInfo.getPrePlayerStakeScore();
		if (prePlayerStakeScore != null) {
			Integer curPlayerStatus = GameUtil.getPlayerStatus(playerList, playerId);
			/**前一个玩家未看牌*/
			if (JhPlayerStatusEnum.notWatch.status.equals(prePlayerStatus)) {
				/**当前玩家未看牌*/
				if (JhPlayerStatusEnum.notWatch.status.equals(curPlayerStatus)) {
					/**如果当前玩家的跟注分数小于前一个玩家，则提示错误信息*/
					if (msg.getCurStakeScore() < prePlayerStakeScore) {
						throw new BusinessException(ExceptionEnum.STAKE_SCORE_ERROR_1);
					}
					/**当前玩家已看牌*/
				}else if (JhPlayerStatusEnum.watch.status.equals(curPlayerStatus)){
					/**当前玩家的跟注分数如果小于前一个玩家的跟注分数的2倍，则提示错误信息*/
					if (msg.getCurStakeScore() < prePlayerStakeScore*2) {
						throw new BusinessException(ExceptionEnum.STAKE_SCORE_ERROR_2);
					}
				}else{
					throw new BusinessException(ExceptionEnum.PLAYER_STATUS_ERROR_1);
				}
				/**前一个玩家已看牌*/
			}else{
				/**当前玩家未看牌*/
				if (JhPlayerStatusEnum.notWatch.status.equals(curPlayerStatus)) {
					/**如果当前玩家的跟注分数的2倍小于前一个玩家，则提示错误信息*/
					if (2*msg.getCurStakeScore() < prePlayerStakeScore) {
						throw new BusinessException(ExceptionEnum.STAKE_SCORE_ERROR_3);
					}
					/**当前玩家已看牌*/
				}else if (JhPlayerStatusEnum.watch.status.equals(curPlayerStatus)){
					/**当前玩家的跟注分数如果小于前一个玩家的跟注分数，则提示错误信息*/
					if (msg.getCurStakeScore() < prePlayerStakeScore) {
						throw new BusinessException(ExceptionEnum.STAKE_SCORE_ERROR_1);
					}
				}else{
					throw new BusinessException(ExceptionEnum.PLAYER_STATUS_ERROR_1);
				}
			}
		}
		/**跟注次数到指定次数上限的玩家计数*/
		int stakeTimesReachCount = 0;
		/**当前玩家的跟注次数*/
		int curPlayerStakeTimes = 0;
		for(JhPlayerInfo player : playerList){
			if (player.getPlayerId().equals(msg.getPlayerId())) {
				player.getStakeScoreList().add(msg.getCurStakeScore());
				player.setStakeTimes(player.getStakeTimes() + 1);
				player.setCurStakeScore(msg.getCurStakeScore());
				player.setCurTotalStakeScore(player.getCurTotalStakeScore() + msg.getCurStakeScore());
				curPlayerStakeTimes = player.getStakeTimes();
			}
			if (JhPlayerStatusEnum.notWatch.status.equals(player.getStatus()) || JhPlayerStatusEnum.watch.status.equals(player.getStatus())) {
				if (player.getStakeTimes().equals(roomInfo.getStakeTimesLimit())) {
					stakeTimesReachCount++;
				}
			}
		}
		/**如果活着的玩家的跟注次数都已经到了指定跟注次数上限，则自动明牌，注意明牌的时候主动弃牌的不用明*/
		if (stakeTimesReachCount == getAlivePlayerCount(playerList)) {
			calScoresAndWinner(roomInfo);
			roomInfo.setUpdateTime(new Date());
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			/**此处new一个新对象，是返回给客户端需要返回的数据，不需要返回的数据则隐藏掉*/
			JhRoomInfo newRoomInfo = new JhRoomInfo();
			newRoomInfo.setCurWinnerId(roomInfo.getCurWinnerId());
			newRoomInfo.setTotalWinnerId(roomInfo.getTotalWinnerId());
			newRoomInfo.setStatus(roomInfo.getStatus());
			newRoomInfo.setRoomId(roomId);
			newRoomInfo.setRoomOwnerId(roomInfo.getRoomOwnerId());
			newRoomInfo.setTotalStakeTimes(roomInfo.getStakeTimesLimit());
			for(JhPlayerInfo player : playerList){
				JhPlayerInfo newPlayer = new JhPlayerInfo();
				newPlayer.setPlayerId(player.getPlayerId());
				newPlayer.setCurScore(player.getCurScore());
				newPlayer.setTotalScore(player.getTotalScore());
				newPlayer.setStatus(player.getStatus());
				newPlayer.setMaxCardType(player.getMaxCardType());
				newPlayer.setWinTimes(player.getWinTimes());
				newPlayer.setLoseTimes(player.getLoseTimes());
				newPlayer.setHeadImgUrl(player.getHeadImgUrl());
				if (!JhPlayerStatusEnum.autoDiscard.status.equals(player.getStatus())) {
					newPlayer.setCardType(player.getCardType());
					newPlayer.setCardList(player.getCardList());
				}
				newRoomInfo.getPlayerList().add(newPlayer);
			}
			result.setMsgType(MsgTypeEnum.autoCardsCompare.msgType);
			result.setData(newRoomInfo);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			return ;
		}
		Integer curPlayerId = GameUtil.getNextOperatePlayerId(playerList, msg.getPlayerId());
		roomInfo.setCurPlayerId(curPlayerId);
		roomInfo.setPrePlayerId(msg.getPlayerId());
		roomInfo.setPrePlayerStatus(GameUtil.getPlayerStatus(playerList, msg.getPlayerId()));
		roomInfo.setPrePlayerStakeScore(msg.getCurStakeScore());
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		/**设置下一个玩家120s没操作定时任务标记*/
		JhPlayerInfo nextPlayer = (JhPlayerInfo)GameUtil.getPlayerByPlayerId(playerList, curPlayerId);
		redisOperationService.setJhNoOperationIpPlayerIdRoomIdTime(curPlayerId, roomId, roomInfo.getCurGame(), nextPlayer.getStakeTimes());
	
		result.setMsgType(MsgTypeEnum.stake.msgType);
		data.put("playerId", msg.getPlayerId());
		data.put("stakeScore", msg.getCurStakeScore());
		data.put("stakeTimes", curPlayerStakeTimes);
		data.put("totalStakeTimes", getTotalStakeTimes(playerList));
		data.put("curPlayerId", GameUtil.getNextOperatePlayerId(playerList, msg.getPlayerId()));
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}
	
	
	
	public void watchCards(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		JhMsg msg = (JhMsg)request.getMsg();
		Integer roomId = userInfo.getRoomId();
		Integer playerId = userInfo.getPlayerId();
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(msg.getPlayerId(), playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		/**如果当前房间的状态不是在游戏中，则不处理此请求*/
		if (!RoomStatusEnum.inGame.status.equals(roomInfo.getStatus())) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		
		List<Card> cardList = null;
		for(JhPlayerInfo player : playerList){
			if (player.getPlayerId().equals(msg.getPlayerId())) {
				player.setStatus(JhPlayerStatusEnum.watch.status);
				cardList = player.getCardList();
			}
		}
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		result.setMsgType(MsgTypeEnum.watchCards.msgType);
		data.put("playerId", msg.getPlayerId());
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArrWithOutSelf(playerList, playerId));
		data.put("cardList", cardList);
		channelContainer.sendTextMsgByPlayerIds(result, msg.getPlayerId());
	}
	
	/**
	 * 局中玩家发起的比牌
	 */
	public void manualCardsCompare(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		
		JhMsg msg = (JhMsg)request.getMsg();
		Integer roomId = userInfo.getRoomId();
		Integer playerId = userInfo.getPlayerId();
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(msg.getPlayerId(), playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		/**如果跟注人不是当前说话人的id，则直接返回提示*/
		if (!msg.getPlayerId().equals(roomInfo.getCurPlayerId())) {
			throw new BusinessException(ExceptionEnum.IS_NOT_YOUR_TURN);
		}
		/**删除当前玩家120s未操作标记*/
		redisOperationService.delJhNoOperationIpPlayerIdRoomIdTime(playerId);
		
		Integer curStakeScore = null;
		Integer prePlayerStatus = roomInfo.getPrePlayerStatus();
		Integer prePlayerStakeScore = roomInfo.getPrePlayerStakeScore();
		if (prePlayerStakeScore != null) {
			Integer curPlayerStatus = GameUtil.getPlayerStatus(playerList, msg.getPlayerId());
			/**前一个玩家未看牌*/
			if (JhPlayerStatusEnum.notWatch.status.equals(prePlayerStatus)) {
				/**当前玩家未看牌*/
				if (JhPlayerStatusEnum.notWatch.status.equals(curPlayerStatus)) {
					/**当前玩家自动投注分数为前一个玩家的一半*/
					if (null != prePlayerStakeScore) {
						curStakeScore = prePlayerStakeScore;
					}
					/**当前玩家已看牌*/
				}else if (JhPlayerStatusEnum.watch.status.equals(curPlayerStatus)){
					/**当前玩家自动投注分数为前一个玩家的一半*/
					if (null != prePlayerStakeScore) {
						curStakeScore = 2*prePlayerStakeScore;
					}
				}else{
					throw new BusinessException(ExceptionEnum.PLAYER_STATUS_ERROR_1);
				}
				/**前一个玩家已看牌*/
			}else{
				/**当前玩家未看牌*/
				if (JhPlayerStatusEnum.notWatch.status.equals(curPlayerStatus)) {
					/**当前玩家自动投注分数为前一个玩家的一半*/
					if (null != prePlayerStakeScore) {
						curStakeScore = prePlayerStakeScore/2;
					}
					/**当前玩家已看牌*/
				}else if (JhPlayerStatusEnum.watch.status.equals(curPlayerStatus)){
					/**当前玩家自动投注分数为前一个玩家的一半*/
					if (null != prePlayerStakeScore) {
						curStakeScore = prePlayerStakeScore;
					}
				}else{
					throw new BusinessException(ExceptionEnum.PLAYER_STATUS_ERROR_1);
				}
			}
		}else{/**前一个玩家没有投注分，则说明此玩家是第一个说话的，直接设置1分*/
			curStakeScore = 1;
		}
		
		JhPlayerInfo selfPlayer = null;
		JhPlayerInfo otherPlayer = null;
		int alivePlayerCount = 0;
		for(JhPlayerInfo player : playerList){
			if (player.getPlayerId().equals(msg.getPlayerId())) {
				selfPlayer = player;
				/**设置当前跟注分数*/
				player.setCurStakeScore(curStakeScore);
				/**设置当前总跟注分数*/
				player.setCurTotalStakeScore((player.getCurTotalStakeScore()==null?0:player.getCurTotalStakeScore()) + curStakeScore);
			}else if(player.getPlayerId().equals(msg.getOtherPlayerId())){
				otherPlayer = player;
			}
			if (player.getStatus().equals(JhPlayerStatusEnum.notWatch.status) || player.getStatus().equals(JhPlayerStatusEnum.watch.status)) {
				alivePlayerCount++;
			}
		}
		/**如果最后只剩下两家，则需要自动进行明牌，结束本局*/
		if (alivePlayerCount == 2) {
			calScoresAndWinner(roomInfo);
			roomInfo.setUpdateTime(new Date());
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			/**此处new一个新对象，是返回给客户端需要返回的数据，不需要返回的数据则隐藏掉*/
			JhRoomInfo newRoomInfo = new JhRoomInfo();
			newRoomInfo.setCurWinnerId(roomInfo.getCurWinnerId());
			newRoomInfo.setTotalWinnerId(roomInfo.getTotalWinnerId());
			newRoomInfo.setStatus(roomInfo.getStatus());
			newRoomInfo.setRoomId(roomId);
			newRoomInfo.setRoomOwnerId(roomInfo.getRoomOwnerId());
			for(JhPlayerInfo player : playerList){
				JhPlayerInfo newPlayer = new JhPlayerInfo();
				newPlayer.setPlayerId(player.getPlayerId());
				newPlayer.setCurScore(player.getCurScore());
				newPlayer.setTotalScore(player.getTotalScore());
				newPlayer.setStatus(player.getStatus());
				newPlayer.setMaxCardType(player.getMaxCardType());
				newPlayer.setWinTimes(player.getWinTimes());
				newPlayer.setLoseTimes(player.getLoseTimes());
				newPlayer.setHeadImgUrl(player.getHeadImgUrl());
				if (!JhPlayerStatusEnum.autoDiscard.status.equals(player.getStatus())) {
					newPlayer.setCardType(player.getCardType());
					newPlayer.setCardList(player.getCardList());
				}
				newRoomInfo.getPlayerList().add(newPlayer);
			}
			result.setMsgType(MsgTypeEnum.autoCardsCompare.msgType);
			result.setData(newRoomInfo);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			return;
		}
		
		/**如果活着的玩家大于2家，则比牌的两个玩家都必须是看牌*/
		if (selfPlayer.getStatus().equals(JhPlayerStatusEnum.notWatch.status) 
			|| otherPlayer.getStatus().equals(JhPlayerStatusEnum.notWatch.status)) {
			throw new BusinessException(ExceptionEnum.MUST_WATCH_CARD);
		}
		/**如果活着的玩家大于2家，则只需要将此两个玩比牌*/
		Integer curPlayerId = null;
		int re = JhCardRule.compareTwoPlayerCards(selfPlayer, otherPlayer);
		if (re > 0) {
			otherPlayer.setStatus(JhPlayerStatusEnum.compareDisCard.status);
			data.put("winnerId", selfPlayer.getPlayerId());
			data.put("loserId", otherPlayer.getPlayerId());
			/**获取下一个操作者id需要在另外一个玩家设置弃牌之后*/
			curPlayerId = GameUtil.getNextOperatePlayerId(playerList, msg.getPlayerId());
			data.put("curPlayerId", curPlayerId);
		}else{
			data.put("winnerId", otherPlayer.getPlayerId());
			data.put("loserId", selfPlayer.getPlayerId());
			/**获取下一个操作者id需要在本玩家设置弃牌之前*/
			curPlayerId = GameUtil.getNextOperatePlayerId(playerList, msg.getPlayerId());
			data.put("curPlayerId", curPlayerId);
			selfPlayer.setStatus(JhPlayerStatusEnum.compareDisCard.status);
		}
		roomInfo.setCurPlayerId(curPlayerId);
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		
		/**设置下一个玩家120s没操作定时任务标记*/
		JhPlayerInfo nextPlayer = (JhPlayerInfo)GameUtil.getPlayerByPlayerId(playerList, curPlayerId);
		redisOperationService.setJhNoOperationIpPlayerIdRoomIdTime(curPlayerId, roomId, roomInfo.getCurGame(), nextPlayer.getStakeTimes());
	
		
		result.setMsgType(MsgTypeEnum.manualCardsCompare.msgType);
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}
	
	
	/**
	 * 玩家弃牌
	 * 如果其他的都弃牌，只剩下最后一个玩家，则进行自动明牌
	 */
	public void discardCards(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		result.setMsgType(MsgTypeEnum.discardCards.msgType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		
		Integer roomId = userInfo.getRoomId();
		Integer playerId = userInfo.getPlayerId();
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		if (!GameUtil.isExistPlayerInRoom(playerId, playerList)) {
			throw new BusinessException(ExceptionEnum.PLAYER_NOT_IN_ROOM);
		}
		/**删除当前玩家120s未操作标记*/
		redisOperationService.delJhNoOperationIpPlayerIdRoomIdTime(playerId);
		
		Integer nextOperatePlayerId = GameUtil.getNextOperatePlayerId(playerList, playerId);
		roomInfo.setCurPlayerId(nextOperatePlayerId);
		/**设置当前玩家状态为主动弃牌*/
		GameUtil.setPlayerStatus(playerList, playerId, JhPlayerStatusEnum.autoDiscard.status);
		
		int alivePlayerCount = getAlivePlayerCount(playerList);
		/**如果剩余或者的玩家数为1，自动明牌*/
		if (alivePlayerCount == 1) {
			calScoresAndWinner(roomInfo);
			roomInfo.setUpdateTime(new Date());
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			/**此处new一个新对象，是返回给客户端需要返回的数据，不需要返回的数据则隐藏掉*/
			JhRoomInfo newRoomInfo = new JhRoomInfo();
			newRoomInfo.setCurWinnerId(roomInfo.getCurWinnerId());
			newRoomInfo.setRoomBankerId(roomInfo.getRoomBankerId());
			newRoomInfo.setTotalWinnerId(roomInfo.getTotalWinnerId());
			newRoomInfo.setStatus(roomInfo.getStatus());
			newRoomInfo.setRoomId(roomId);
			newRoomInfo.setRoomOwnerId(roomInfo.getRoomOwnerId());
			for(JhPlayerInfo player : playerList){
				JhPlayerInfo newPlayer = new JhPlayerInfo();
				newPlayer.setPlayerId(player.getPlayerId());
				newPlayer.setCurScore(player.getCurScore());
				newPlayer.setTotalScore(player.getTotalScore());
				newPlayer.setStatus(player.getStatus());
				newPlayer.setMaxCardType(player.getMaxCardType());
				newPlayer.setWinTimes(player.getWinTimes());
				newPlayer.setLoseTimes(player.getLoseTimes());
				newPlayer.setHeadImgUrl(player.getHeadImgUrl());
				if (!JhPlayerStatusEnum.autoDiscard.status.equals(player.getStatus())) {
					newPlayer.setCardType(player.getCardType());
					newPlayer.setCardList(player.getCardList());
				}
				newRoomInfo.getPlayerList().add(newPlayer);
			}
			result.setMsgType(MsgTypeEnum.autoCardsCompare.msgType);
			result.setData(newRoomInfo);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			return;
		}
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		/**设置下一个玩家120s没操作定时任务标记*/
		JhPlayerInfo nextPlayer = (JhPlayerInfo)GameUtil.getPlayerByPlayerId(playerList, nextOperatePlayerId);
		redisOperationService.setJhNoOperationIpPlayerIdRoomIdTime(nextOperatePlayerId, roomId, roomInfo.getCurGame(), nextPlayer.getStakeTimes());
		
		/**如果剩余或者的玩家数大于1,则给所有的玩家广播通知此玩家弃牌*/
		data.put("playerId", playerId);
		data.put("curPlayerId", nextOperatePlayerId);
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}
	
	
	@Override
	public List<BaseRoomInfo> doRefreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		
		List<BaseRoomInfo> roomInfoList = new ArrayList<BaseRoomInfo>();
		Integer playerId = userInfo.getPlayerId();
		Integer roomId = userInfo.getRoomId();
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		JhRoomInfo newRoomInfo = new JhRoomInfo();
		roomInfoList.add(roomInfo);
		roomInfoList.add(newRoomInfo);
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		/**房间公共字段*/
		JhRoomStatusEnum roomStatusEnum = JhRoomStatusEnum.getRoomStatusEnum(roomInfo.getStatus());
		newRoomInfo.setGameType(roomInfo.getGameType());
		newRoomInfo.setStatus(roomStatusEnum.status);
		newRoomInfo.setRoomId(roomId);
		newRoomInfo.setRoomOwnerId(roomInfo.getRoomOwnerId());
		newRoomInfo.setRoomBankerId(roomInfo.getRoomBankerId());
		newRoomInfo.setTotalGames(roomInfo.getTotalGames());
		newRoomInfo.setCurGame(roomInfo.getCurGame());
		newRoomInfo.setPayType(roomInfo.getPayType());
		/**金花房间特有字段信息*/
		newRoomInfo.setStakeButtom(roomInfo.getStakeButtom());
		newRoomInfo.setStakeLimit(roomInfo.getStakeLimit());
		newRoomInfo.setStakeTimesLimit(roomInfo.getStakeTimesLimit());
		newRoomInfo.setCurPlayerId(roomInfo.getCurPlayerId());
		newRoomInfo.setPrePlayerId(roomInfo.getPrePlayerId());
		newRoomInfo.setPrePlayerStakeScore(roomInfo.getPrePlayerStakeScore());
		newRoomInfo.setPrePlayerStatus(roomInfo.getPrePlayerStatus());
		newRoomInfo.setTotalStakeTimes(roomInfo.getTotalStakeTimes());
		
		for(JhPlayerInfo player : playerList){
			JhPlayerInfo newPlayer = new JhPlayerInfo();
			/**玩家信息公共字段*/
			newPlayer.setPlayerId(player.getPlayerId());
			newPlayer.setNickName(player.getNickName());
			newPlayer.setHeadImgUrl(player.getHeadImgUrl());
			newPlayer.setOrder(player.getOrder());
			newPlayer.setRoomCardNum(player.getRoomCardNum());
			newPlayer.setLevel(player.getLevel());
			newPlayer.setStatus(player.getStatus());
			newPlayer.setCurScore(player.getCurScore());
			newPlayer.setTotalScore(player.getTotalScore());
			newPlayer.setMaxCardType(player.getMaxCardType());
			newPlayer.setWinTimes(player.getWinTimes());
			newPlayer.setLoseTimes(player.getLoseTimes());
			/**金花玩家特有字段信息*/
			newPlayer.setCurStakeScore(player.getCurStakeScore());
			newPlayer.setCurTotalStakeScore(player.getCurTotalStakeScore());
			newPlayer.setStakeTimes(player.getStakeTimes());
			newPlayer.setStakeScoreList(player.getStakeScoreList());
			newPlayer.setPlayedCount(player.getPlayedCount());
			newRoomInfo.getPlayerList().add(newPlayer);
			if (playerId.equals(player.getPlayerId())) {
				newPlayer.setOnlineStatus(OnlineStatusEnum.online.status);
			}else{
				newPlayer.setOnlineStatus(player.getOnlineStatus());
			}
			switch (roomStatusEnum) {
				case justBegin:
					break;
				case inGame:
					/**如果是已看牌，并且是当前请求刷新的用户，则要给此玩家返回牌型*/
					if (JhPlayerStatusEnum.watch.status.equals(player.getStatus()) && player.getPlayerId().equals(playerId)) {
						newPlayer.setCardList(player.getCardList());
						newPlayer.setCardType(player.getCardType());
					}
					break;
				case curGameOver:
					/**小局结束的试试，主动弃牌的牌不展示，其他的都需要展示**/
					if (!JhPlayerStatusEnum.autoDiscard.status.equals(player.getStatus())) {
						newPlayer.setCardList(player.getCardList());
						newPlayer.setCardType(player.getCardType());
					}
					break;
				case totalGameOver:
					
					break;
				default:
					break;
			}
		}
		
		return roomInfoList;
	}

	@Override
	public BaseRoomInfo getRoomInfo(ChannelHandlerContext ctx,
			BaseRequest request, UserInfo userInfo) {
		Integer roomId = userInfo.getRoomId();
		JhRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, JhRoomInfo.class);
		return roomInfo;
	}
	
	public void calScoresAndWinner(JhRoomInfo roomInfo){
		List<JhPlayerInfo> playerList = roomInfo.getPlayerList();
		/**在活着的玩家里面找出赢家*/
		BasePlayerInfo curWinnerPlayer = JhCardRule.comparePlayerCards(getAlivePlayerList(playerList));
		roomInfo.setCurWinnerId(curWinnerPlayer.getPlayerId());
		/**设置下一小局的庄家*/
		roomInfo.setRoomBankerId(curWinnerPlayer.getPlayerId());
		/**设置前一个玩家跟注分数为null*/
		roomInfo.setPrePlayerStakeScore(null);
		/**计算每个玩家当前局得分*/
		for(JhPlayerInfo player : playerList){
			/**liujinfengnew*/
			if (JhPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				continue;
			}
			/**玩家跟注次数置0*/
			player.setStakeTimes(0);
			if (!player.getPlayerId().equals(curWinnerPlayer.getPlayerId())) {
				player.setCurScore(player.getCurScore() - player.getCurTotalStakeScore() - 1);
				curWinnerPlayer.setCurScore(curWinnerPlayer.getCurScore() + player.getCurTotalStakeScore() + 1);
				player.setLoseTimes((player.getLoseTimes()==null?0:player.getLoseTimes()) + 1);
				if (player.getCardType() > player.getMaxCardType()) {
					player.setMaxCardType(player.getCardType());
				}
			}else{
				curWinnerPlayer.setWinTimes((curWinnerPlayer.getWinTimes()==null?0:curWinnerPlayer.getWinTimes()) + 1);
				if (curWinnerPlayer.getCardType() > curWinnerPlayer.getMaxCardType()) {
					curWinnerPlayer.setMaxCardType(curWinnerPlayer.getCardType());
				}
			}
		}
		/**计算每个玩家总得分*/
		for(JhPlayerInfo player : playerList){
			/**liujinfengnew*/
			if (JhPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				continue;
			}
			player.setPlayedCount(player.getPlayedCount() + 1);
			player.setTotalScore((player.getTotalScore()==null?0:player.getTotalScore()) + player.getCurScore());
		}
		
		/**设置房间的总赢家*/
		Integer totalWinnerId = playerList.get(0).getPlayerId();
		Integer maxTotalScore = playerList.get(0).getTotalScore()==null?0:playerList.get(0).getTotalScore();
		for(JhPlayerInfo player : playerList){
			/**liujinfengnew*/
			if (JhPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				continue;
			}
			Integer tempTotalScore = player.getTotalScore()==null?0:player.getTotalScore();
			if (tempTotalScore > maxTotalScore) {
				maxTotalScore = tempTotalScore;
				totalWinnerId = player.getPlayerId();
			}
			/**清空押注list*/
			player.getStakeScoreList().clear();
		}
		roomInfo.setTotalWinnerId(totalWinnerId);
		/**如果当前局数小于总局数，则设置为当前局结束*/
		if (roomInfo.getCurGame() < roomInfo.getTotalGames()) {
			roomInfo.setStatus(JhRoomStatusEnum.curGameOver.status);
		}else{/**如果当前局数等于总局数，则设置为一圈结束*/
			roomInfo.setStatus(JhRoomStatusEnum.totalGameOver.status);
			try {
				commonManager.addUserRecord(roomInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**如果是第一局结束，则扣除房卡;扣除房卡异常不影响游戏进行，会将异常数据放入redis中，由定时任务进行补偿扣除*/
		if (roomInfo.getCurGame() == 1) {
			if (redisOperationService.isLoginFuseOpen()) {
				log.info("扣除房卡开始===============");
				try {
					List<Integer> palyerIdList = commonManager.deductRoomCard(roomInfo, RoomCardOperationEnum.consumeCard);
					log.info("palyerIdList:" + JsonUtil.toJson(palyerIdList));
					for(Integer playerId : palyerIdList){
						UserModel userM = commonManager.getUserById(playerId);
						roomCardNumUpdate(userM.getRoomCardNum(), playerId);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("扣除房卡结束===============");
			}
		}
		/**删除各种定时标记*/
		redisOperationService.delNotReadyIpRoomIdTime(roomInfo.getRoomId());
		for(JhPlayerInfo player : playerList){
			redisOperationService.delJhNoOperationIpPlayerIdRoomIdTime(player.getPlayerId());
		}
		
	}
	public Integer getTotalStakeTimes(List<JhPlayerInfo> playerList){
		Integer totalStakeTimes = playerList.get(0).getStakeTimes();
		for(JhPlayerInfo player : playerList){
			if (totalStakeTimes < player.getStakeTimes()) {
				totalStakeTimes = player.getStakeTimes();
			}
		}
		return totalStakeTimes;
	}
	public int getAlivePlayerCount(List<JhPlayerInfo> playerList){
		int alivePlayerCount = 0;
		for(JhPlayerInfo player : playerList){
			if (player.getStatus().equals(JhPlayerStatusEnum.notWatch.status) || player.getStatus().equals(JhPlayerStatusEnum.watch.status)) {
				alivePlayerCount++;
			}
		}
		return alivePlayerCount;
	}
	
	public static List getAlivePlayerList(List<JhPlayerInfo> playerList){
		List<JhPlayerInfo> alivePlayerList = new ArrayList<>();
		int size = playerList.size();
		for(JhPlayerInfo player : playerList){
			/**金花特有，3：未看牌，4：已看牌*/
			if (player.getStatus().equals(JhPlayerStatusEnum.notWatch.status) || player.getStatus().equals(JhPlayerStatusEnum.watch.status)) {
				alivePlayerList.add(player);
			}
		}
		return alivePlayerList;
	}

}
