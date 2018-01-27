package cn.worldwalker.game.wyqp.nn.service;

import cn.worldwalker.game.wyqp.common.domain.base.*;
import cn.worldwalker.game.wyqp.common.domain.nn.NnMsg;
import cn.worldwalker.game.wyqp.common.domain.nn.NnPlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.nn.NnRoomInfo;
import cn.worldwalker.game.wyqp.common.enums.*;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import cn.worldwalker.game.wyqp.common.utils.GameUtil;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;
import cn.worldwalker.game.wyqp.nn.cards.NnCardResource;
import cn.worldwalker.game.wyqp.nn.cards.NnCardRule;
import cn.worldwalker.game.wyqp.nn.enums.*;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.*;
@Service(value="nnGameService")
public class NnGameService extends BaseGameService{
	
	@Override
	public BaseRoomInfo doCreateRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		NnMsg msg = (NnMsg)request.getMsg();
		if (msg.getRoomBankerType() == null) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		NnRoomInfo roomInfo = new NnRoomInfo();
		roomInfo.setGameType(GameTypeEnum.nn.gameType);
		roomInfo.setRoomBankerType(msg.getRoomBankerType());
		/**如果不是抢庄类型，则创建房间的时候直接设置房主为庄家*/
		if (!NnRoomBankerTypeEnum.robBanker.type.equals(msg.getRoomBankerType())) {
			roomInfo.setRoomBankerId(msg.getPlayerId());
		}
		roomInfo.setMultipleLimit(NnMultipleLimitEnum.getNnMultipleLimitEnum(msg.getMultipleLimit()).multiple);
		roomInfo.setButtomScoreType(msg.getButtomScoreType());
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		NnPlayerInfo player = new NnPlayerInfo();
		playerList.add(player);
		return roomInfo;
	}

	@Override
	public BaseRoomInfo doEntryRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		BaseMsg msg = request.getMsg();
		NnRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(msg.getRoomId(), NnRoomInfo.class);
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		NnPlayerInfo player = new NnPlayerInfo();
		playerList.add(player);
		return roomInfo;
	}

	public void ready(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		Result result = new Result();
		result.setGameType(GameTypeEnum.nn.gameType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		Integer playerId = userInfo.getPlayerId();
		final Integer roomId = userInfo.getRoomId();
		NnRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
		/**如果玩家点击准备的时候房间已经在游戏中状态，说明可能是点击关闭结算窗口操作，并且刚好10秒倒计时已经发牌了，产生了并发，避免玩家状态乱了，这里需要做控制*/
		if (roomInfo.getRoomBankerType().equals(NnRoomBankerTypeEnum.robBanker.type)) {
			if (roomInfo.getStatus().equals(NnRoomStatusEnum.inRob.status)) {
				result.setMsgType(MsgTypeEnum.ready.msgType);
				data.put("playerId", userInfo.getPlayerId());
				channelContainer.sendTextMsgByPlayerIds(result, playerId);
				return;
			}
		}else{
			if (roomInfo.getStatus().equals(NnRoomStatusEnum.inStakeScore.status)) {
				result.setMsgType(MsgTypeEnum.ready.msgType);
				data.put("playerId", userInfo.getPlayerId());
				channelContainer.sendTextMsgByPlayerIds(result, playerId);
				return;
			}
		}
		
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		/**玩家已经准备计数*/
		int readyCount = 0;
		int observerCount = 0;
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			NnPlayerInfo player = playerList.get(i);
			if (player.getPlayerId().equals(playerId)) {
				/**设置状态为已准备*/
				player.setStatus(NnPlayerStatusEnum.ready.status);
			}
			if (NnPlayerStatusEnum.ready.status.equals(player.getStatus())) {
				readyCount++;
			}
			if (NnPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				observerCount++;
			}
		}
		
		/**如果已经准备的人数据大于1并且等于房间内所有玩家的数目，抢庄类型，发4张牌，非抢庄则通知压分*/
		if (readyCount > 1 && readyCount == (size - observerCount)) {
			/**开始发牌时将房间内当前局数+1*/
			roomInfo.setCurGame(roomInfo.getCurGame() + 1);
			/**发牌*/
			List<List<Card>> playerCards = NnCardResource.dealCardsWithOutRank(size);
			/**为每个玩家设置牌及牌型*/
			for(int i = 0; i < size; i++ ){
				NnPlayerInfo player = playerList.get(i);
				List<Card> cardList = playerCards.get(i);
				List<Card> nnCardList = new ArrayList<Card>();
				List<Card> robFourCardList = new ArrayList<Card>();
				Card fifthCard = new Card();
				player.setCardType(NnCardRule.calculateCardType(player, cardList, nnCardList, robFourCardList, fifthCard));
				player.setCardList(cardList);
				player.setNnCardList(nnCardList);
				player.setRobFourCardList(robFourCardList);
				player.setFifthCard(fifthCard);
				/**设置每个玩家的解散房间状态为不同意解散，后面大结算返回大厅的时候回根据此状态判断是否解散房间*/
				player.setDissolveStatus(DissolveStatusEnum.disagree.status);
			}
			/**如果是抢庄类型，则设置房间状态为抢庄阶段*/
			if (NnRoomBankerTypeEnum.robBanker.type.equals(roomInfo.getRoomBankerType())) {
				roomInfo.setStatus(NnRoomStatusEnum.inRob.status);
			}else{
				roomInfo.setStatus(NnRoomStatusEnum.inStakeScore.status);
			}
			
			roomInfo.setUpdateTime(new Date());
			
			/**删除未准备10秒计时器*/
			redisOperationService.delNotReadyIpRoomIdTime(roomId);
			
			data.put("roomId", roomInfo.getRoomId());
			data.put("roomOwnerId", roomInfo.getRoomOwnerId());
			data.put("totalGames", roomInfo.getTotalGames());
			data.put("curGame", roomInfo.getCurGame());
			/**如果是抢庄类型，则给每个玩家返回四张牌，并通知准备抢庄.同时开启后台定时任务计数*/
			if (NnRoomBankerTypeEnum.robBanker.type.equals(roomInfo.getRoomBankerType())) {
				redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
				/**开启自动抢庄定时任务*/
				redisOperationService.setNnRobIpRoomIdTime(roomId);
				result.setMsgType(MsgTypeEnum.readyRobBanker.msgType);
				for(int i = 0; i < size; i++ ){
					NnPlayerInfo player = playerList.get(i);
					List<Card> cardList = player.getRobFourCardList();
					data.put("cardList", cardList);
					channelContainer.sendTextMsgByPlayerIds(result, player.getPlayerId());
				}
			}else{/**如果是非抢庄类型，则通知玩家谁是庄家并准备压分*/
				result.setMsgType(MsgTypeEnum.readyStake.msgType);
				data.put("roomBankerId", roomInfo.getRoomBankerId());
				Integer[] playerArr = GameUtil.getPlayerIdArr(playerList);
				String[] scoreArr = NnButtomScoreTypeEnum.getNnButtomScoreTypeEnum(roomInfo.getButtomScoreType()).value.split("_");
				List<Integer> stakeScoreList = new ArrayList<Integer>();
				for(String socre : scoreArr){
					stakeScoreList.add(Integer.valueOf(socre));
				}
				Map<Integer, Integer> map = NnCardRule.getRandomPlayerIdStakeScoreMap(roomInfo);
				redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
				if (map.size() > 0) {
					for(Integer tempPlayerId : playerArr){
						if (map.containsKey(tempPlayerId)) {
							List<Integer> randomStakeScoreList = new ArrayList<Integer>();
							randomStakeScoreList.addAll(stakeScoreList);
							randomStakeScoreList.add(map.get(tempPlayerId));
							data.put("stakeScoreList", randomStakeScoreList);
						}else{
							data.put("stakeScoreList", stakeScoreList);
						}
						channelContainer.sendTextMsgByPlayerIds(result, tempPlayerId);
					}
				}else{
					data.put("stakeScoreList", stakeScoreList);
					channelContainer.sendTextMsgByPlayerIds(result, playerArr);
				}
				/**设置压分倒计时标记*/
				redisOperationService.setNnNotStakeScoreIpRoomIdTime(roomId);
			}
			return;
		}
		roomInfo.setUpdateTime(new Date());
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		result.setMsgType(MsgTypeEnum.ready.msgType);
		data.put("playerId", playerId);
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
		
		/**如果准备的玩家数为2，则启动计时器，并返回消息告诉前端开始10秒计时*/
		if (readyCount == 1 && (size - observerCount) > 1) {
			redisOperationService.setNotReadyIpRoomIdTime(roomId);
			result.setMsgType(MsgTypeEnum.notReadyTimer.msgType);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			return;
		}
	}
	
	/**
	 * 抢庄
	 * 
	 * */
	public void robBanker(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		
		Result result = new Result();
		result.setGameType(GameTypeEnum.nn.gameType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		NnMsg msg = (NnMsg)request.getMsg();
		Integer playerId = userInfo.getPlayerId();
		Integer roomId = userInfo.getRoomId();
		NnRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		/**玩家已经抢庄计数*/
		int robCount = 0;
		/**开始玩游戏后才进来的玩家计数  addby liujinfengnew*/
		int observerCount = 0;
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			NnPlayerInfo player = playerList.get(i);
			if (player.getPlayerId().equals(playerId)) {
				/**设置状态为已抢庄*/
				player.setStatus(msg.getIsRobBanker());
				player.setRobBankerTime(System.currentTimeMillis());
				player.setRobMultiple(msg.getRobMultiple());
			}
			if (NnPlayerStatusEnum.notRob.status.equals(player.getStatus()) || NnPlayerStatusEnum.rob.status.equals(player.getStatus())) {
				robCount++;
			}
			if (NnPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				observerCount++;
			}
		}
		
		
		/**如果都抢完庄(玩家总数减去观察者玩家数),则通知玩家庄家并开始压分*/
		if (robCount > 1 && robCount == (size - observerCount)) {
			/**如果都抢完庄，则删除自动抢庄定时任务标记*/
			redisOperationService.delNnRobIpRoomIdTime(roomId);
			/**计算是谁抢到庄**/
			NnPlayerInfo bankerPlayer = playerList.get(0);
			for(int i = 1; i < size; i++){
				NnPlayerInfo player = playerList.get(i);
				if (NnPlayerStatusEnum.rob.status.equals(player.getStatus())) {
					if (!NnPlayerStatusEnum.rob.status.equals(bankerPlayer.getStatus())) {
						bankerPlayer = player;
					}else{/**两个玩家都抢庄了，则比较抢庄的时间，先抢的先当庄*/
						if (bankerPlayer.getRobBankerTime() > player.getRobBankerTime()) {
							bankerPlayer = player;
						}
					}
					
				}
			}
			/**如果都没抢庄的话，默认是上个赢家的庄，如果是第一局，则是房主的庄*/
			if (!NnPlayerStatusEnum.rob.status.equals(bankerPlayer.getStatus())) {
				if (roomInfo.getCurWinnerId() != null) {
					roomInfo.setRoomBankerId(roomInfo.getCurWinnerId());
				}else{
					roomInfo.setRoomBankerId(roomInfo.getRoomOwnerId());
				}
				/**如果都没有抢庄，则设置庄家的倍数为1*/
				roomInfo.setRobMultiple(1);
			}else{/**如果有人抢庄*/
				roomInfo.setRoomBankerId(bankerPlayer.getPlayerId());
				roomInfo.setRobMultiple(bankerPlayer.getRobMultiple());
			}
			result.setMsgType(MsgTypeEnum.readyStake.msgType);
			data.put("roomBankerId", roomInfo.getRoomBankerId());
			data.put("curGame", roomInfo.getCurGame());
			data.put("playerId", msg.getPlayerId());
			data.put("robMultiple", msg.getRobMultiple());
			Integer[] playerArr = GameUtil.getPlayerIdArr(playerList);
			String[] scoreArr = NnButtomScoreTypeEnum.getNnButtomScoreTypeEnum(roomInfo.getButtomScoreType()).value.split("_");
			List<Integer> stakeScoreList = new ArrayList<Integer>();
			for(String socre : scoreArr){
				stakeScoreList.add(Integer.valueOf(socre));
			}
			Map<Integer, Integer> map = NnCardRule.getRandomPlayerIdStakeScoreMap(roomInfo);
			roomInfo.setStatus(NnRoomStatusEnum.inStakeScore.status);
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			if (map.size() > 0) {
				for(Integer tempPlayerId : playerArr){
					if (map.containsKey(tempPlayerId)) {
						List<Integer> randomStakeScoreList = new ArrayList<Integer>();
						randomStakeScoreList.addAll(stakeScoreList);
						randomStakeScoreList.add(map.get(tempPlayerId));
						data.put("stakeScoreList", randomStakeScoreList);
					}else{
						data.put("stakeScoreList", stakeScoreList);
					}
					channelContainer.sendTextMsgByPlayerIds(result, tempPlayerId);
				}
			}else{
				data.put("stakeScoreList", stakeScoreList);
				channelContainer.sendTextMsgByPlayerIds(result, playerArr);
			}
			/**设置压分倒计时标记*/
			redisOperationService.setNnNotStakeScoreIpRoomIdTime(roomId);
			return ;
		}
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		result.setMsgType(MsgTypeEnum.robBanker.msgType);
		data.put("playerId", playerId);
		data.put("isRobBanker", msg.getIsRobBanker());
		data.put("robMultiple", msg.getRobMultiple());
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}

	public void stakeScore(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(GameTypeEnum.nn.gameType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		NnMsg msg = (NnMsg)request.getMsg();
		Integer playerId = userInfo.getPlayerId();
		Integer roomId = userInfo.getRoomId();
		NnRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
		if (roomInfo.getRoomBankerId().equals(playerId)) {
			throw new BusinessException(ExceptionEnum.ROOM_BANKER_CAN_NOT_STAKE_SCORE);
		}
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		/**玩家已经压分计数*/
		int stakeScoreCount = 0;
		/**开始玩游戏后才进来的玩家计数  addby liujinfengnew*/
		int observerCount = 0;
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			NnPlayerInfo player = playerList.get(i);
			if (player.getPlayerId().equals(playerId)) {
				/**设置所压分数*/
				player.setStakeScore(msg.getStakeScore());
				player.setStatus(NnPlayerStatusEnum.stakeScore.status);
			}
			if (NnPlayerStatusEnum.stakeScore.status.equals(player.getStatus())) {
				stakeScoreCount++;
			}
			if (NnPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				observerCount++;
			}
		}
		
		/**如果都压完分(除去庄家和观察者的数量),则发牌*/
		if (stakeScoreCount == (size - observerCount - 1)) {
			/**删除自动压分标记*/
			redisOperationService.delNnNotStakeScoreIpRoomIdTime(roomId);
			roomInfo.setStatus(NnRoomStatusEnum.inGame.status);
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			/**都压完分，先给所有玩家返回最后一个压分信息，延迟一会再发牌*/
			result.setMsgType(MsgTypeEnum.stakeScore.msgType);
			data.put("playerId", playerId);
			data.put("stakeScore", msg.getStakeScore());
			data.put("isLastStakeScore", 1);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			result.setMsgType(MsgTypeEnum.dealCards.msgType);
			for(NnPlayerInfo player : playerList){
				/**如果当前玩家是观察者，则不反回信息 add by liujinfengnew*/
				if (NnPlayerStatusEnum.observer.status.equals(player.getStatus())) {
					continue;
				}
				/**如果是看牌抢庄类型，则拿到第五张牌，再和前四张牌一起返回*/
				if (NnRoomBankerTypeEnum.robBanker.type.equals(roomInfo.getRoomBankerType())) {
					List<Card> list = player.getRobFourCardList();
					list.add(player.getFifthCard());
					data.put("cardList", list);
				}else{
					data.put("cardList", player.getCardList());
				}
				data.put("cardType", player.getCardType());
				data.put("playerId", player.getPlayerId());
				/**给每个玩家返回自己的牌及牌型*/
				channelContainer.sendTextMsgByPlayerIds(result, player.getPlayerId());
			}
			/**设置明牌倒计时标记*/
			redisOperationService.setNnShowCardIpRoomIdTime(roomId);
			return ;
		}
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		result.setMsgType(MsgTypeEnum.stakeScore.msgType);
		data.put("playerId", playerId);
		data.put("stakeScore", msg.getStakeScore());
		data.put("isLastStakeScore", 0);
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
	}
	
	public void showCard(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo){
		Result result = new Result();
		result.setGameType(GameTypeEnum.nn.gameType);
		Map<String, Object> data = new HashMap<String, Object>();
		result.setData(data);
		Integer playerId = userInfo.getPlayerId();
		Integer roomId = userInfo.getRoomId();
		NnRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		List<Card> cardList = null;
		List<Card> nnCardList = null;
		Integer cardType = null;
		int showCardNum = 0;
		/**开始玩游戏后才进来的玩家计数  addby liujinfengnew*/
		int observerCount = 0;
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			NnPlayerInfo player = playerList.get(i);
			if (player.getPlayerId().equals(playerId)) {
				player.setStatus(NnPlayerStatusEnum.showCard.status);
				if (NnRoomBankerTypeEnum.robBanker.type.equals(roomInfo.getRoomBankerType())) {
					List<Card> list = player.getRobFourCardList();
					list.add(player.getFifthCard());
					cardList = list;
				}else{
					cardList = player.getCardList();
				}
				
				cardType = player.getCardType();
				nnCardList = player.getNnCardList();
			}
			if (NnPlayerStatusEnum.showCard.status.equals(player.getStatus())) {
				showCardNum++;
			}
			if (NnPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				observerCount++;
			}
		}
		/**如果所有的玩家都已经翻牌（除去观察者数量），则计算得分**/
		if (showCardNum == (size - observerCount)) {
			/**删除翻牌计数*/
			redisOperationService.delNnShowCardIpRoomIdTime(roomId);
			try {
				calculateScoreAndRoomBanker(roomInfo);
			} catch (Exception e) {
				log.error("roomInfo:" + JsonUtil.toJson(roomInfo), e);
				throw e;
			}
			redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
			NnRoomInfo newRoomInfo = new NnRoomInfo();
			newRoomInfo.setTotalWinnerId(roomInfo.getTotalWinnerId());
			newRoomInfo.setRoomId(roomId);
			newRoomInfo.setRoomOwnerId(roomInfo.getRoomOwnerId());
			newRoomInfo.setRoomBankerId(roomInfo.getRoomBankerId());
			newRoomInfo.setUpdateTime(roomInfo.getUpdateTime());
			for(NnPlayerInfo player : playerList){
				NnPlayerInfo newPlayer = new NnPlayerInfo();
				newPlayer.setPlayerId(player.getPlayerId());
				newPlayer.setCurScore(player.getCurScore());
				newPlayer.setTotalScore(player.getTotalScore());
				newPlayer.setCardType(player.getCardType());
				newPlayer.setMaxCardType(player.getMaxCardType());
				newPlayer.setWinTimes(player.getWinTimes());
				newPlayer.setLoseTimes(player.getLoseTimes());
				
				newPlayer.setBankerCount(player.getBankerCount());
				newPlayer.setBombNiuCount(player.getBombNiuCount());
				newPlayer.setFiveSmallNiuCount(player.getFiveSmallNiuCount());
				newPlayer.setGoldNiuCount(player.getGoldNiuCount());
				newPlayer.setNiuNiuCount(player.getNiuNiuCount());
				newPlayer.setYouNiuCount(player.getYouNiuCount());
				newPlayer.setWuNiuCount(player.getWuNiuCount());
				newRoomInfo.getPlayerList().add(newPlayer);
			}
			
			result.setMsgType(MsgTypeEnum.showCard.msgType);
			data.put("playerId", playerId);
			data.put("cardList", cardList);
			data.put("cardType", cardType);
			data.put("nnCardList", nnCardList);
			data.put("isLastShowCard", 1);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			if (NnRoomStatusEnum.totalGameOver.status.equals(roomInfo.getStatus())) {
				result.setMsgType(MsgTypeEnum.totalSettlement.msgType);
			}else{
				result.setMsgType(MsgTypeEnum.curSettlement.msgType);
			}
			result.setData(newRoomInfo);
			channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
			return;
		}
		redisOperationService.setRoomIdRoomInfo(roomId, roomInfo);
		result.setMsgType(MsgTypeEnum.showCard.msgType);
		data.put("playerId", playerId);
		data.put("cardList", cardList);
		data.put("cardType", cardType);
		data.put("nnCardList", nnCardList);
		data.put("isLastShowCard", 0);
		channelContainer.sendTextMsgByPlayerIds(result, GameUtil.getPlayerIdArr(playerList));
		
	}
	
	private void calculateScoreAndRoomBanker(NnRoomInfo roomInfo){
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		/**找出庄家*/
		NnPlayerInfo roomBankerPlayer = null;
		for(NnPlayerInfo player : playerList){
			if (roomInfo.getRoomBankerId().equals(player.getPlayerId())) {
				roomBankerPlayer = player;
				break;
			}
		}
		/**设置庄家次数*/
		roomBankerPlayer.setBankerCount(roomBankerPlayer.getBankerCount() + 1);
		/**将其他玩家的牌依次与庄家进行比较，计算各自得当前局分及总得分，最大牌型，并计算下一次庄家是谁*/
		for(NnPlayerInfo player : playerList){
			/**观察者的不算 add by liujinfengnew */
			if (NnPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				continue;
			}
			if (!roomInfo.getRoomBankerId().equals(player.getPlayerId())) {
				if (NnCardRule.cardTypeCompare(player, roomBankerPlayer) > 0) {
					
					Integer cardTypeMultiple = NnCardTypeEnum.getNnCardTypeEnum(player.getCardType()).multiple;
					/**如果是抢庄类型，则需要计算抢庄时候的倍数*/
					if (roomInfo.getRoomBankerType().equals(NnRoomBankerTypeEnum.robBanker.type)) {
						cardTypeMultiple = cardTypeMultiple*roomInfo.getRobMultiple();
					}
					Integer settedMultiple = roomInfo.getMultipleLimit();
					Integer multiple = cardTypeMultiple;
					if (cardTypeMultiple > settedMultiple) {
						multiple = settedMultiple;
					}else{
						multiple = cardTypeMultiple;
					}
					Integer winScore = player.getStakeScore()*multiple;
					player.setCurScore(winScore);
					player.setTotalScore(player.getTotalScore() + player.getCurScore());
					player.setWinTimes(player.getWinTimes() + 1);
					roomBankerPlayer.setCurScore(0 - winScore);
					roomBankerPlayer.setTotalScore(roomBankerPlayer.getTotalScore() + roomBankerPlayer.getCurScore());
					roomBankerPlayer.setLoseTimes(roomBankerPlayer.getLoseTimes() + 1);
				}else{
					Integer cardTypeMultiple = NnCardTypeEnum.getNnCardTypeEnum(roomBankerPlayer.getCardType()).multiple;
					Integer settedMultiple = roomInfo.getMultipleLimit();
					Integer multiple = cardTypeMultiple;
					if (cardTypeMultiple > settedMultiple) {
						multiple = settedMultiple;
					}else{
						multiple = cardTypeMultiple;
					}
					Integer winScore = player.getStakeScore()*multiple;
					player.setCurScore(0 - winScore);
					player.setTotalScore(player.getTotalScore() + player.getCurScore());
					player.setLoseTimes(player.getLoseTimes() + 1);
					roomBankerPlayer.setCurScore(winScore);
					roomBankerPlayer.setTotalScore(roomBankerPlayer.getTotalScore() + roomBankerPlayer.getCurScore());
					roomBankerPlayer.setWinTimes(roomBankerPlayer.getWinTimes() + 1);
				}
			}
			/**计算各自的最大牌型*/
			if (player.getCardType() > player.getMaxCardType()) {
				player.setMaxCardType(player.getCardType());
			}
		}
		
		/**设置房间的总赢家及当前赢家*/
		Integer totalWinnerId = playerList.get(0).getPlayerId();
		Integer curWinnerId = playerList.get(0).getPlayerId();
		Integer maxTotalScore = playerList.get(0).getTotalScore();
		Integer maxCurScore = playerList.get(0).getCurScore();
		for(NnPlayerInfo player : playerList){
			/**观察者的不算 add by liujinfengnew */
			if (NnPlayerStatusEnum.observer.status.equals(player.getStatus())) {
				continue;
			}
			player.setPlayedCount(player.getPlayedCount() + 1);
			Integer tempTotalScore = player.getTotalScore();
			Integer tempCurScore = player.getCurScore();
			if (tempTotalScore > maxTotalScore) {
				maxTotalScore = tempTotalScore;
				totalWinnerId = player.getPlayerId();
			}
			if (tempCurScore > maxCurScore) {
				maxCurScore = tempCurScore;
				curWinnerId = player.getPlayerId();
			}
		}
		roomInfo.setTotalWinnerId(totalWinnerId);
		roomInfo.setCurWinnerId(curWinnerId);
		/**如果当前局数小于总局数，则设置为当前局结束*/
		if (roomInfo.getCurGame() < roomInfo.getTotalGames()) {
			/**设置下一局的庄家id（抢庄的不设置）*/
			setRoomBankerId(roomInfo);
			roomInfo.setStatus(NnRoomStatusEnum.curGameOver.status);
		}else{/**如果当前局数等于总局数，则设置为一圈结束*/
			roomInfo.setStatus(NnRoomStatusEnum.totalGameOver.status);
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
		}else{/**如果不是第一局，则只扣除观察者的房卡 add by liujinfengnew*/
			if (redisOperationService.isLoginFuseOpen()) {
				log.info("扣除房卡开始===============");
				try {
					List<Integer> palyerIdList = commonManager.deductRoomCardForObserver(roomInfo, RoomCardOperationEnum.consumeCard);
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
		
		/**将各种定时标记删除*/
		redisOperationService.delNotReadyIpRoomIdTime(roomInfo.getRoomId());
		redisOperationService.delNnRobIpRoomIdTime(roomInfo.getRoomId());
		redisOperationService.delNnNotStakeScoreIpRoomIdTime(roomInfo.getRoomId());
		redisOperationService.delNnShowCardIpRoomIdTime(roomInfo.getRoomId());
		
	}
	
	public void test(){
		NnRoomInfo roomInfo = new NnRoomInfo();
		roomInfo.setRoomId(195886);
		roomInfo.setRoomBankerId(876917);
		roomInfo.setRoomBankerType(1);
		roomInfo.setCurGame(2);
		roomInfo.setGameType(1);
		roomInfo.setTotalGames(10);
		roomInfo.setPayType(2);
		roomInfo.setMultipleLimit(1);
		
		NnPlayerInfo player = new NnPlayerInfo();
		player.setPlayerId(20000);
		player.setCardType(13);
		NnPlayerInfo player1 = new NnPlayerInfo();
		player1.setPlayerId(20001);
		player1.setCardType(10);
		player1.setStakeScore(2);
		roomInfo.getPlayerList().add(player);
		roomInfo.getPlayerList().add(player1);
		
		if (roomInfo.getCurGame() == 1) {
			if (redisOperationService.isLoginFuseOpen()) {
				log.info("扣除房卡开始===============");
				try {
					List<Integer> palyerIdList = commonManager.deductRoomCard(roomInfo, RoomCardOperationEnum.consumeCard);
					log.info("palyerIdList:" + JsonUtil.toJson(palyerIdList));
					for(Integer playerId : palyerIdList){
						UserModel userM = commonManager.getUserById(playerId);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("扣除房卡结束===============");
			}
		}
		
	}
	
	public static void main(String[] args) {
		NnRoomInfo roomInfo = new NnRoomInfo();
		roomInfo.setRoomId(195886);
		roomInfo.setRoomBankerId(20000);
		roomInfo.setRoomBankerType(1);
		roomInfo.setCurGame(2);
		roomInfo.setGameType(1);
		roomInfo.setTotalGames(10);
		roomInfo.setPayType(2);
		roomInfo.setMultipleLimit(3);
		
		NnPlayerInfo player = new NnPlayerInfo();
		player.setPlayerId(20000);
		player.setCardType(10);
		NnPlayerInfo player1 = new NnPlayerInfo();
		player1.setPlayerId(20001);
		player1.setCardType(1);
		player1.setStakeScore(1);
		roomInfo.getPlayerList().add(player);
		roomInfo.getPlayerList().add(player1);
//		calculateScoreAndRoomBanker(roomInfo);
	}
	
	
	private static void setRoomBankerId(NnRoomInfo roomInfo){
		
		NnRoomBankerTypeEnum typeEnum = NnRoomBankerTypeEnum.getNnRoomBankerTypeEnum(roomInfo.getRoomBankerType());
		
		switch (typeEnum) {
			case inTurnBanker:
				/**如果房间中庄家id为null则说明房间是刚创建的，直接设置房主为庄家*/
				if (roomInfo.getRoomBankerId() == null) {
					roomInfo.setRoomBankerId(roomInfo.getRoomOwnerId());
				}else{
					List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
					int size = playerList.size();
					for(int i = 0; i < size; i++){
						NnPlayerInfo player = playerList.get(i);
						if (player.getPlayerId().equals(roomInfo.getRoomBankerId())) {
							if (i == size - 1) {
								roomInfo.setRoomBankerId((playerList.get(0)).getPlayerId());
								break;
							}else{
								roomInfo.setRoomBankerId((playerList.get(i + 1)).getPlayerId());
								break;
							}
						}
					}
				}
				break;
			case overLordBanker:
				roomInfo.setRoomBankerId(roomInfo.getRoomOwnerId());		
				break;
			case winnerBanker:
				if (roomInfo.getCurWinnerId() == null) {
					roomInfo.setRoomBankerId(roomInfo.getRoomOwnerId());
				}else{
					roomInfo.setRoomBankerId(roomInfo.getCurWinnerId());
				}
				break;
			case robBanker:
				roomInfo.setRoomBankerId(null);
				break;
			default:
				break;
		}
	}

	@Override
	public List<BaseRoomInfo> doRefreshRoom(ChannelHandlerContext ctx, BaseRequest request, UserInfo userInfo) {
		List<BaseRoomInfo> roomInfoList = new ArrayList<BaseRoomInfo>();
		Integer playerId = userInfo.getPlayerId();
		Integer roomId = userInfo.getRoomId();
		NnRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
		NnRoomInfo newRoomInfo = new NnRoomInfo();
		roomInfoList.add(roomInfo);
		roomInfoList.add(newRoomInfo);
		List<NnPlayerInfo> playerList = roomInfo.getPlayerList();
		
		
		/**根据不同的房间及玩家状态设置返回房间信息*/
		NnRoomStatusEnum roomStatusEnum = NnRoomStatusEnum.getRoomStatusEnum(roomInfo.getStatus());
		newRoomInfo.setGameType(roomInfo.getGameType());
		newRoomInfo.setStatus(roomStatusEnum.status);
		newRoomInfo.setRoomId(roomId);
		newRoomInfo.setRoomOwnerId(roomInfo.getRoomOwnerId());
		newRoomInfo.setRoomBankerId(roomInfo.getRoomBankerId());
		newRoomInfo.setRoomBankerType(roomInfo.getRoomBankerType());
		newRoomInfo.setTotalGames(roomInfo.getTotalGames());
		newRoomInfo.setCurGame(roomInfo.getCurGame());
		newRoomInfo.setPayType(roomInfo.getPayType());
		newRoomInfo.setMultipleLimit(roomInfo.getMultipleLimit());
		
		for(NnPlayerInfo player : playerList){
			NnPlayerInfo newPlayer = new NnPlayerInfo();
			newPlayer.setPlayerId(player.getPlayerId());
			newPlayer.setNickName(player.getNickName());
			newPlayer.setHeadImgUrl(player.getHeadImgUrl());
			newPlayer.setOrder(player.getOrder());
			newPlayer.setRoomCardNum(player.getRoomCardNum());
			newPlayer.setLevel(player.getLevel());
			newPlayer.setStatus(player.getStatus());
			newPlayer.setTotalScore(player.getTotalScore());
			newRoomInfo.getPlayerList().add(newPlayer);
			if (playerId.equals(player.getPlayerId())) {
				newPlayer.setOnlineStatus(OnlineStatusEnum.online.status);
			}else{
				newPlayer.setOnlineStatus(player.getOnlineStatus());
			}
			switch (roomStatusEnum) {
				case justBegin:
					break;
				case inRob:
					/**如果是自己，则设置四张牌返回*/
					if (playerId.equals(player.getPlayerId())) {
						newPlayer.setCardList(player.getRobFourCardList());
					}
					break;
				case inStakeScore:
					newPlayer.setStakeScore(player.getStakeScore());
					/**如果是刷新玩家本人*/
					if (playerId.equals(player.getPlayerId())) {
						if (NnRoomBankerTypeEnum.robBanker.type.equals(roomInfo.getRoomBankerType())) {
							String[] scoreArr = NnButtomScoreTypeEnum.getNnButtomScoreTypeEnum(roomInfo.getButtomScoreType()).value.split("_");
							List<Integer> stakeScoreList = new ArrayList<Integer>();
								for(String socre : scoreArr){
								stakeScoreList.add(Integer.valueOf(socre));
							}
							newPlayer.setCardList(player.getRobFourCardList());
							newPlayer.setStakeScoreList(stakeScoreList);
						}else{/**非抢庄类型*/
							String[] scoreArr = NnButtomScoreTypeEnum.getNnButtomScoreTypeEnum(roomInfo.getButtomScoreType()).value.split("_");
							List<Integer> stakeScoreList = new ArrayList<Integer>();
							for(String socre : scoreArr){
								stakeScoreList.add(Integer.valueOf(socre));
							}
							newPlayer.setStakeScoreList(stakeScoreList);
						}
					}
					break;
				case inGame:
					newPlayer.setStakeScore(player.getStakeScore());
					/**如果玩家是自己或者已经亮牌，则返回牌信息*/
					if (playerId.equals(player.getPlayerId()) || NnPlayerStatusEnum.showCard.status.equals(player.getStatus())) {
						if (NnRoomBankerTypeEnum.robBanker.type.equals(roomInfo.getRoomBankerType())) {
							List<Card> list = player.getRobFourCardList();
							list.add(player.getFifthCard());
							newPlayer.setCardList(list);
						}else{
							newPlayer.setCardList(player.getCardList());
						}
						newPlayer.setCardType(player.getCardType());
					}
					break;
				case curGameOver:
					newPlayer.setCurScore(player.getCurScore());
					if (NnRoomBankerTypeEnum.robBanker.type.equals(roomInfo.getRoomBankerType())) {
						List<Card> list = player.getRobFourCardList();
						list.add(player.getFifthCard());
						newPlayer.setCardList(list);
					}else{
						newPlayer.setCardList(player.getCardList());
					}
					newPlayer.setCardType(player.getCardType());
					break;
				case totalGameOver:
					newPlayer.setMaxCardType(player.getMaxCardType());
					newPlayer.setWinTimes(player.getWinTimes());
					newPlayer.setLoseTimes(player.getLoseTimes());
					
					newPlayer.setBankerCount(player.getBankerCount());
					newPlayer.setBombNiuCount(player.getBombNiuCount());
					newPlayer.setFiveSmallNiuCount(player.getFiveSmallNiuCount());
					newPlayer.setGoldNiuCount(player.getGoldNiuCount());
					newPlayer.setNiuNiuCount(player.getNiuNiuCount());
					newPlayer.setYouNiuCount(player.getYouNiuCount());
					newPlayer.setWuNiuCount(player.getWuNiuCount());
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
		NnRoomInfo roomInfo = redisOperationService.getRoomInfoByRoomId(roomId, NnRoomInfo.class);
		return roomInfo;
	}

}
