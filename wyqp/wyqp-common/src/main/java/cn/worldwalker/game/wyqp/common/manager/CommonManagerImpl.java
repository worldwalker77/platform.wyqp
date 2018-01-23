package cn.worldwalker.game.wyqp.common.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.worldwalker.game.wyqp.common.dao.OrderDao;
import cn.worldwalker.game.wyqp.common.dao.ProductDao;
import cn.worldwalker.game.wyqp.common.dao.ProxyDao;
import cn.worldwalker.game.wyqp.common.dao.RoomCardLogDao;
import cn.worldwalker.game.wyqp.common.dao.TeaHouseDao;
import cn.worldwalker.game.wyqp.common.dao.UserDao;
import cn.worldwalker.game.wyqp.common.dao.UserFeedbackDao;
import cn.worldwalker.game.wyqp.common.dao.UserRecordDao;
import cn.worldwalker.game.wyqp.common.dao.VersionDao;
import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.OrderModel;
import cn.worldwalker.game.wyqp.common.domain.base.ProductModel;
import cn.worldwalker.game.wyqp.common.domain.base.ProxyModel;
import cn.worldwalker.game.wyqp.common.domain.base.RecordModel;
import cn.worldwalker.game.wyqp.common.domain.base.RoomCardLogModel;
import cn.worldwalker.game.wyqp.common.domain.base.TeaHouseModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserFeedbackModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserRecordModel;
import cn.worldwalker.game.wyqp.common.enums.GameTypeEnum;
import cn.worldwalker.game.wyqp.common.enums.PlayerStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.RoomCardConsumeEnum;
import cn.worldwalker.game.wyqp.common.enums.RoomCardOperationEnum;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;
@Component
public class CommonManagerImpl implements CommonManager{
	
	private static final Log log = LogFactory.getLog(CommonManagerImpl.class);
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoomCardLogDao roomCardLogDao;
	@Autowired
	private UserFeedbackDao userFeedbackDao;
	@Autowired
	private UserRecordDao userRecordDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProxyDao proxyDao;
	@Autowired
	private VersionDao versionDao;
	@Autowired
	private TeaHouseDao teaHouseDao;
	
	@Override
	public UserModel getUserByWxOpenId(String openId){
		return userDao.getUserByWxOpenId(openId);
	}
	@Override
	public void insertUser(UserModel userModel){
		try {
			userDao.insertUser(userModel);
		} catch (Exception e) {
			log.error("插入用户信息异常", e);
			userModel.setNickName(String.valueOf(userModel.getPlayerId()));
			userDao.insertUser(userModel);
			
		}
	}
	
	@Transactional
	@Override
	public List<Integer> deductRoomCard(BaseRoomInfo roomInfo, RoomCardOperationEnum operationEnum){
		List<Integer> playerIList = new ArrayList<Integer>();
		if (roomInfo.getPayType() == 1) {/**房主付费*/
			playerIList.add(roomInfo.getRoomOwnerId());
		}else{/**AA付费*/
			List playerList = roomInfo.getPlayerList();
			int size = playerList.size();
			for(int i = 0; i < size; i++){
				BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
				playerIList.add(player.getPlayerId());
			}
		}
		for(Integer playerId : playerIList){
			doDeductRoomCard(roomInfo.getGameType(), roomInfo.getPayType(), roomInfo.getTotalGames(), operationEnum, playerId);
		}
		return playerIList;
	}
	
	@Override
	public List<Integer> deductRoomCardForObserver(BaseRoomInfo roomInfo, RoomCardOperationEnum operationEnum) {
		List<Integer> playerIList = new ArrayList<Integer>();
		if (roomInfo.getPayType() == 1) {/**房主付费,则新进来的观察者不需要扣房卡*/
			return playerIList;
		}else{/**AA付费*/
			List playerList = roomInfo.getPlayerList();
			int size = playerList.size();
			for(int i = 0; i < size; i++){
				BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
				if (!PlayerStatusEnum.observer.status.equals(player.getStatus())) {
					continue;
				}
				playerIList.add(player.getPlayerId());
			}
		}
		for(Integer playerId : playerIList){
			doDeductRoomCard(roomInfo.getGameType(), roomInfo.getPayType(), roomInfo.getTotalGames(), operationEnum, playerId);
		}
		return playerIList;
	}
	
	@Override
	public Integer doDeductRoomCard(Integer gameType, Integer payType, Integer totalGames, RoomCardOperationEnum operationEnum, Integer playerId){
		RoomCardConsumeEnum consumeEnum = RoomCardConsumeEnum.getRoomCardConsumeEnum(gameType, payType, totalGames);
		Map<String, Object> map = new HashMap<String, Object>();
		int re = 0;
		int reTryCount = 1;
		UserModel userModel = null;
		do {
			userModel = userDao.getUserById(playerId);
			map.put("playerId", playerId);
			map.put("deductNum", consumeEnum.needRoomCardNum);
			map.put("roomCardNum", userModel.getRoomCardNum());
			map.put("updateTime", userModel.getUpdateTime());
			log.info("doDeductRoomCard, map:" + JsonUtil.toJson(map));
			re = userDao.deductRoomCard(map);
			if (re == 1) {
				break;
			}
			reTryCount++;
			log.info("扣除房卡重试第" + reTryCount + "次");
		} while (reTryCount < 4);/**扣除房卡重试三次*/
		if (reTryCount == 4) {
			throw new BusinessException(ExceptionEnum.ROOM_CARD_DEDUCT_THREE_TIMES_FAIL);
		}
		RoomCardLogModel roomCardLogModel = new RoomCardLogModel();
		roomCardLogModel.setGameType(gameType);
		roomCardLogModel.setPlayerId(playerId);
		roomCardLogModel.setDiffRoomCardNum(consumeEnum.needRoomCardNum);
		roomCardLogModel.setPreRoomCardNum(userModel.getRoomCardNum());
		Integer curRoomCardNum = userModel.getRoomCardNum() - consumeEnum.needRoomCardNum;
		roomCardLogModel.setCurRoomCardNum(curRoomCardNum);
		roomCardLogModel.setOperatorId(playerId);
		roomCardLogModel.setOperatorType(operationEnum.type);
		roomCardLogModel.setCreateTime(new Date());
		roomCardLogDao.insertRoomCardLog(roomCardLogModel);
		return curRoomCardNum;
	}
	@Override
	public void insertFeedback(UserFeedbackModel model) {
		userFeedbackDao.insertFeedback(model);
	}
	@Override
	public List<UserRecordModel> getUserRecord(UserRecordModel model) {
		List<UserRecordModel> list = userRecordDao.getUserRecord(model);
		for(UserRecordModel userRecordModel : list){
			userRecordModel.setRecordList(JsonUtil.json2list(userRecordModel.getRecordInfo(), RecordModel.class));
			userRecordModel.setRecordInfo(null);
		}
		return list;
	}
	
	
	@Override
	public void addUserRecord(BaseRoomInfo roomInfo) {
		List playerList = roomInfo.getPlayerList();
		if (CollectionUtils.isEmpty(playerList)) {
			return;
		}
		int size = playerList.size();
		List<RecordModel> recordModelList = new ArrayList<RecordModel>();
		for(int i = 0; i < size; i++){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			RecordModel recordModel = new RecordModel();
			recordModel.setHeadImgUrl(player.getHeadImgUrl());
			recordModel.setNickName(player.getNickName());
			recordModel.setPlayerId(player.getPlayerId());
			recordModel.setScore(player.getTotalScore());
			recordModelList.add(recordModel);
		}
		String recordInfo = JsonUtil.toJson(recordModelList);
		List<UserRecordModel> modelList = new ArrayList<UserRecordModel>();
		Date createTime = new Date();
		for(int i = 0; i < size; i++){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			UserRecordModel model = new UserRecordModel();
			model.setGameType(roomInfo.getGameType());
			model.setPlayerId(player.getPlayerId());
			model.setNickName(player.getNickName());
			model.setHeadImgUrl(player.getHeadImgUrl());
			model.setRoomId(roomInfo.getRoomId());
			model.setPayType(roomInfo.getPayType());
			model.setTotalGames(roomInfo.getTotalGames());
			model.setScore(player.getTotalScore());
			model.setRecordInfo(recordInfo);
			model.setRemark(RoomCardConsumeEnum.getRoomCardConsumeEnum(roomInfo.getGameType(), roomInfo.getPayType(), roomInfo.getTotalGames()).desc);
			model.setCreateTime(createTime);
			model.setTeaHouseNum(roomInfo.getTeaHouseNum()==null?0:roomInfo.getTeaHouseNum());
			model.setTableNum(roomInfo.getTableNum()==null?0:roomInfo.getTableNum());
			modelList.add(model);
		}
		userRecordDao.batchInsertRecord(modelList);
	}
	@Override
	public void roomCardCheck(Integer playerId, Integer gameType, Integer payType, Integer totalGames) {
		RoomCardConsumeEnum consumeEnum = RoomCardConsumeEnum.getRoomCardConsumeEnum(gameType,payType, totalGames);
		if (consumeEnum == null) {
			throw new BusinessException(ExceptionEnum.PARAMS_ERROR);
		}
		UserModel userModel = userDao.getUserById(playerId);
		Integer roomCardNum = userModel.getRoomCardNum();
		if (roomCardNum < consumeEnum.needRoomCardNum) {
			throw new BusinessException(ExceptionEnum.ROOM_CARD_NOT_ENOUGH);
		}
	}
	@Override
	public Long insertOrder(Integer playerId, Integer productId,
			Integer roomCardNum, Integer price) {
		OrderModel orderModel = new OrderModel();
		orderModel.setPlayerId(playerId);
		orderModel.setProductId(productId);
		orderModel.setRoomCardNum(roomCardNum);
		orderModel.setPrice(price);
		Integer res = orderDao.insertOrder(orderModel);
		if (res <= 0) {
			throw new BusinessException(ExceptionEnum.INSERT_ORDER_FAIL);
		}
		return orderModel.getOrderId();
	}
	@Transactional
	@Override
	public Integer updateOrderAndUser(Integer playerId, Integer addRoomCardNum, Long orderId, String transactionId, Integer wxPayPrice) {
		
		OrderModel orderModel = new OrderModel();
		orderModel.setOrderId(orderId);
		orderModel.setTransactionId(transactionId);
		orderModel.setWxPayPrice(wxPayPrice);
		/**更新订单的最终支付状态*/
		Integer res = orderDao.updateOrder(orderModel);
		if (res <= 0) {
			throw new BusinessException(ExceptionEnum.UPDATE_ORDER_FAIL);
		}
		/**更新用户的房卡数*/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("playerId", playerId);
		map.put("addNum", addRoomCardNum);
		res = userDao.addRoomCard(map);
		if (res <= 0) {
			throw new BusinessException(ExceptionEnum.UPDATE_USER_ROOM_CARD_FAIL);
		}
		
		/**根据playerId去t_proxy_user表里面查proxy_id*/
		Integer proxyId = proxyDao.getProxyIdByPlayerId(playerId);
		
		/*************如果没有绑定代理则不走代理提成流程****************************************/
		if (proxyId == null) {
			/**查询用户当前房卡数并返回*/
			UserModel user = userDao.getUserById(playerId);
			return user.getRoomCardNum();
		}
		/**根据proxy_id查询当前代理的总收益及当前账户余额，主要是为更新做准备，防止更新覆盖*/
		ProxyModel resModel = proxyDao.getProxyInfo(proxyId);
		/**更新代理总收益,用户充值的一半分给代理*/
		ProxyModel proxyModel = new ProxyModel();
		Integer temp = wxPayPrice/2;
		/**总收益，防止覆盖更新*/
		proxyModel.setTotalIncome(resModel.getTotalIncome());
		proxyModel.setCurIncome(temp);
		/**更新代理账户余额,用户充值的一半分给代理*/
		/**账户余额，防止覆盖更新*/
		proxyModel.setRemainderAmount(resModel.getRemainderAmount());
		proxyModel.setCurRemainder(temp);
		/**提现金额，防止覆盖更新*/
		proxyModel.setExtractAmount(resModel.getExtractAmount());
		proxyModel.setProxyId(proxyId);
		/**以总收益、账户余额、已提现金额当做版本号更新代理总收益及账户余额*/
		res = proxyDao.updateProxyInfo(proxyModel);
		if (res <= 0) {
			throw new BusinessException(ExceptionEnum.UPDATE_PROXY_INCOME_FAIL);
		}
		/**查询用户当前房卡数并返回*/
		UserModel user = userDao.getUserById(playerId);
		return user.getRoomCardNum();
	}
	@Override
	public ProductModel getProductById(Integer productId) {
		return productDao.getProductById(productId);
	}
	@Override
	public List<ProductModel> getProductList() {
		return productDao.getProductList();
	}
	@Override
	public OrderModel getOderByOrderId(Long orderId) {
		return orderDao.getOderByOrderId(orderId);
	}
	@Override
	public UserModel getUserById(Integer playerId) {
		return userDao.getUserById(playerId);
	}
	
	@Override
	public void insertProxyUser(Integer proxyId, Integer playerId, String nickName) {
		ProxyModel model = new ProxyModel();
		model.setProxyId(proxyId);
		model.setPlayerId(playerId);
		model.setNickName(nickName);
		Integer res = proxyDao.insertProxyUser(model);
		if (res <= 0) {
			throw new BusinessException(ExceptionEnum.BIND_PROXY_FAIL);
		}
	}
	@Override
	public Integer getProxyCountByProxyId(Integer proxyId) {
		return proxyDao.getProxyCountByProxyId(proxyId);
	}
	@Override
	public Integer getProxyUserCountByPlayerId(Integer playerId) {
		return proxyDao.getProxyUserCountByPlayerId(playerId);
	}
	@Override
	public Integer getProxyIdByPlayerId(Integer playerId) {
		return proxyDao.getProxyIdByPlayerId(playerId);
	}
	@Override
	public Integer addRoomCard(Map<String, Object> map) {
		return userDao.addRoomCard(map);
	}
	
	/************以下为茶楼相关**************/
	@Override
	public void createTeaHouse(TeaHouseModel model) {
		/**查询茶楼号是否存在，如果存在则提示重新创建*/
		TeaHouseModel teaHouseNumModel = new TeaHouseModel();
		teaHouseNumModel.setTeaHouseNum(model.getTeaHouseNum());
		teaHouseNumModel = teaHouseDao.getTeaHouseTypeByTeaHouseNum(teaHouseNumModel);
		if (teaHouseNumModel != null) {
			throw new BusinessException(ExceptionEnum.TEA_HOUSE_NUM_EXIST);
		}
		
		GameTypeEnum gameTypeEnum = GameTypeEnum.getGameTypeEnumByType(model.getGameType());
		TeaHouseModel typeModel = new TeaHouseModel();
		switch (gameTypeEnum) {
			case nn:
				typeModel.setGameType(model.getGameType());
				typeModel.setTotalGame(model.getTotalGame());
				typeModel.setRoomBankerType(model.getRoomBankerType());
				typeModel.setMultipleLimit(model.getMultipleLimit());
				typeModel.setPayType(model.getPayType());
				typeModel.setButtomScoreType(model.getButtomScoreType());
				break;
			case mj:
				break;
			case jh:
				typeModel.setGameType(model.getGameType());
				typeModel.setTotalGame(model.getTotalGame());
				typeModel.setPayType(model.getPayType());
				break;
			default:
				break;
			}
		/**查询此种类型茶楼id*/
		TeaHouseModel resModel = teaHouseDao.getTeaHouseTypeByCondition(typeModel);
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseName(model.getTeaHouseName());
		teaHouseModel.setTeaHouseNum(model.getTeaHouseNum());
		teaHouseModel.setPlayerId(model.getPlayerId());
		teaHouseModel.setNickName(model.getNickName());
		/**此种类型茶楼id存在*/
		if (resModel != null) {
			teaHouseModel.setTeaHouseTypeId(resModel.getTeaHouseTypeId());
			teaHouseDao.insertTeaHouse(teaHouseModel);
			
		}else{/**此种类型茶楼不存在，则先要创建此种类型，然后获取类型id后，创建茶楼*/
			teaHouseDao.insertTeaHouseType(typeModel);
			teaHouseModel.setTeaHouseTypeId(typeModel.getTeaHouseTypeId());
			teaHouseDao.insertTeaHouse(teaHouseModel);
		}
		/**将当前玩家加入茶楼*/
		teaHouseModel.setStatus(1);
		teaHouseDao.insertTeaHouseUser(teaHouseModel);
	}
	
	@Override
	public List<TeaHouseModel> queryPlayerTeaHouseList(Integer playerId) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setPlayerId(playerId);
		return teaHouseDao.getPlayerTeaHouseList(teaHouseModel);
	}
	
	@Override
	public void delTeaHouse(Integer teaHouseNum, Integer playerId) {
		if (!isTeaHouseOwner(teaHouseNum, playerId)) {
			throw new BusinessException(ExceptionEnum.NO_PERMISSION);
		}
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseDao.deleteTeaHouseUserByCondition(teaHouseModel);
		teaHouseModel.setPlayerId(playerId);
		teaHouseDao.deleteTeaHouseByCondition(teaHouseModel);
		
	}
	
	@Override
	public void joinTeaHouse(Integer teaHouseNum, Integer playerId,String nickName, Integer status) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setPlayerId(playerId);
		teaHouseModel.setNickName(nickName);
		teaHouseModel.setStatus(status);
		teaHouseDao.insertTeaHouseUser(teaHouseModel);
	}
	
	@Override
	public void auditEntryTeaHouse(Integer teaHouseNum, Integer playerId, Integer otherPlayerId, Integer status) {
		if (!hasPermission(teaHouseNum, playerId)) {
			throw new BusinessException(ExceptionEnum.NO_PERMISSION);
		}
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setPlayerId(otherPlayerId);
		/**同意*/
		if (status == 1) {
			teaHouseModel.setStatus(status);
			teaHouseDao.auditTeaHouseUser(teaHouseModel);
		}else if(status == 0){/**拒绝的话就删除记录*/
			teaHouseDao.deleteTeaHouseUserByCondition(teaHouseModel);
		}
		
	}
	
	@Override
	public List<TeaHouseModel> queryTeaHousePlayerList(Integer teaHouseNum) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		return teaHouseDao.getTeaHousePlayerList(teaHouseModel);
	}
	
	@Override
	public void delTeaHouseUser(Integer teaHouseNum, Integer otherPlayerId, Integer playerId) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setPlayerId(playerId);
		if (!hasPermission(teaHouseNum, playerId)) {
			throw new BusinessException(ExceptionEnum.NO_PERMISSION);
		}
		/**不能将自己移除茶楼*/
		if (playerId.equals(otherPlayerId)) {
			throw new BusinessException(ExceptionEnum.CAN_NOT_REMOVE_SELF);
		}
		teaHouseModel.setPlayerId(otherPlayerId);
		teaHouseDao.deleteTeaHouseUserByCondition(teaHouseModel);
	}
	private boolean hasPermission(Integer teaHouseNum, Integer playerId){
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setPlayerId(playerId);
		if (teaHouseDao.getTeaHouseByCondition(teaHouseModel) != null) {
			return true;
		}
		TeaHouseModel res = teaHouseDao.getTeaHouseUserByCondition(teaHouseModel);
		if (res.getIsDianXiaoer() > 0) {
			return true;
		}
		return false;
	}
	
	private boolean isDianXiaoer(Integer teaHouseNum, Integer playerId){
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setPlayerId(playerId);
		TeaHouseModel res = teaHouseDao.getTeaHouseUserByCondition(teaHouseModel);
		if (res.getIsDianXiaoer() > 0) {
			return true;
		}
		return false;
	}
	
	private boolean isTeaHouseOwner(Integer teaHouseNum, Integer playerId){
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setPlayerId(playerId);
		teaHouseModel = teaHouseDao.getTeaHouseByCondition(teaHouseModel);
		if (teaHouseModel != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public TeaHouseModel getTeaHouseTypeByTeaHouseNum(Integer teaHouseNum, Integer playerId) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		TeaHouseModel res = teaHouseDao.getTeaHouseTypeByTeaHouseNum(teaHouseModel);
		teaHouseModel.setPlayerId(playerId);
		teaHouseModel = teaHouseDao.getTeaHouseUserByCondition(teaHouseModel);
		res.setIsDianXiaoer(teaHouseModel.getIsDianXiaoer());
		return res;
	}
	@Override
	public void delFromTeaHouse(Integer teaHouseNum, Integer playerId) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setPlayerId(playerId);
		teaHouseDao.deleteTeaHouseUserByCondition(teaHouseModel);
	}
	
	@Override
	public List<TeaHouseModel> queryPlayerJoinedTeaHouseList(Integer playerId) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setPlayerId(playerId);
		return teaHouseDao.getPlayerJoinedTeaHouseList(teaHouseModel);
	}
	@Override
	public List<UserRecordModel> getTeaHouseRecord(Integer teaHouseNum,Integer playerId) {
		if (!hasPermission(teaHouseNum, playerId)) {
			throw new BusinessException(ExceptionEnum.NO_PERMISSION);
		}
		UserRecordModel model = new UserRecordModel();
		model.setTeaHouseNum(teaHouseNum);
		List<UserRecordModel> list = userRecordDao.getTeaHouseRecord(model);
		for(UserRecordModel userRecordModel : list){
			userRecordModel.setRecordList(JsonUtil.json2list(userRecordModel.getRecordInfo(), RecordModel.class));
			userRecordModel.setRecordInfo(null);
		}
		return list;
	}
	@Override
	public List<UserRecordModel> getMyTeaHouseRecord(Integer teaHouseNum, Integer playerId) {
		UserRecordModel model = new UserRecordModel();
		model.setTeaHouseNum(teaHouseNum);
		model.setPlayerId(playerId);
		List<UserRecordModel> list = userRecordDao.getMyTeaHouseRecord(model);
		for(UserRecordModel userRecordModel : list){
			userRecordModel.setRecordList(JsonUtil.json2list(userRecordModel.getRecordInfo(), RecordModel.class));
			userRecordModel.setRecordInfo(null);
		}
		return list;
	}
	@Override
	public boolean isPlayerInTeaHouse(Integer teaHouseNum, Integer playerId) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setPlayerId(playerId);
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		if (teaHouseDao.getTeaHouseUserByCondition(teaHouseModel) == null) {
			return false;
		}
		return true;
	}
	@Override
	public void teaHouseConfig(Integer teaHouseNum, Integer playerId, String teaHouseOwnerWord, Integer isNeedAudit) {
		if (!hasPermission(teaHouseNum, playerId)) {
			throw new BusinessException(ExceptionEnum.NO_PERMISSION);
		}
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setTeaHouseOwnerWord(teaHouseOwnerWord);
		teaHouseModel.setIsNeedAudit(isNeedAudit);
		teaHouseDao.updateTeaHouseByCondition(teaHouseModel);
	}
	@Override
	public List<UserRecordModel> getTeaHouseBigWinner(Integer teaHouseNum) {
		UserRecordModel model = new UserRecordModel();
		model.setTeaHouseNum(teaHouseNum);
		return userRecordDao.getTeaHouseBigWinner(model);
	}
	@Override
	public List<UserRecordModel> getPaishenBoard() {
		return userRecordDao.getPaishenBoard(new UserRecordModel());
	}
	@Override
	public boolean isTeaHouseExist(Integer teaHouseNum) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		if (teaHouseDao.getTeaHouseByCondition(teaHouseModel) == null) {
			return false;
		}
		return true;
	}
	@Override
	public List<TeaHouseModel> queryNeedAuditPlayerList(Integer teaHouseNum, Integer playerId) {
		if (!hasPermission(teaHouseNum, playerId)) {
			throw new BusinessException(ExceptionEnum.NO_PERMISSION);
		}
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		return teaHouseDao.getNeedAuditPlayerList(teaHouseModel);
	}
	@Override
	public TeaHouseModel getTeaHouseByTeaHouseNum(Integer teaHouseNum) {
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		return teaHouseDao.getTeaHouseByTeaHouseNum(teaHouseModel);
	}
	@Override
	public void setDianXiaoer(Integer teaHouseNum, Integer playerId,
			Integer otherPlayerId, Integer isDianXiaoer) {
		if (!isTeaHouseOwner(teaHouseNum, otherPlayerId)) {
			throw new BusinessException(ExceptionEnum.NO_PERMISSION);
		}
		TeaHouseModel teaHouseModel = new TeaHouseModel();
		teaHouseModel.setTeaHouseNum(teaHouseNum);
		teaHouseModel.setPlayerId(otherPlayerId);
		teaHouseModel.setIsDianXiaoer(isDianXiaoer);
		teaHouseDao.updateTeaHouseUserByCondition(teaHouseModel);
		
	}
	
}
