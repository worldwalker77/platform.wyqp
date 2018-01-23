package cn.worldwalker.game.wyqp.common.manager;

import java.util.List;
import java.util.Map;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.common.domain.base.OrderModel;
import cn.worldwalker.game.wyqp.common.domain.base.ProductModel;
import cn.worldwalker.game.wyqp.common.domain.base.TeaHouseModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserFeedbackModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserModel;
import cn.worldwalker.game.wyqp.common.domain.base.UserRecordModel;
import cn.worldwalker.game.wyqp.common.enums.RoomCardOperationEnum;

public interface CommonManager {
	
	public UserModel getUserByWxOpenId(String openId);
	
	public void insertUser(UserModel userModel);
	
	public void insertFeedback(UserFeedbackModel model);
	
	public List<UserRecordModel> getUserRecord(UserRecordModel model);
	
	public List<Integer> deductRoomCard(BaseRoomInfo roomInfo, RoomCardOperationEnum operationEnum);
	
	public List<Integer> deductRoomCardForObserver(BaseRoomInfo roomInfo, RoomCardOperationEnum operationEnum);
	
	public Integer doDeductRoomCard(Integer gameType, Integer payType, Integer totalGames, RoomCardOperationEnum operationEnum, Integer playerId);
	
	public void addUserRecord(BaseRoomInfo roomInfo);
	
	public void roomCardCheck(Integer playerId, Integer gameType, Integer payType, Integer totalGames);
	
	public Long insertOrder(Integer playerId, Integer productId, Integer roomCardNum, Integer price);
	 
	public Integer updateOrderAndUser(Integer playerId, Integer addRoomCardNum, Long orderId, String transactionId, Integer wxPayPrice);
	
	public ProductModel getProductById(Integer productId);
	 
	public List<ProductModel> getProductList();
	
	public void insertProxyUser(Integer proxyId, Integer playerId, String nickName);
	
	public Integer getProxyCountByProxyId(Integer proxyId);
	
	public OrderModel getOderByOrderId(Long orderId);
	
	public UserModel getUserById(Integer playerId);
	
	public Integer getProxyUserCountByPlayerId(Integer playerId);
	
	public Integer getProxyIdByPlayerId(Integer playerId);
	
	public Integer addRoomCard(Map<String, Object> map);
	
	/**以下为茶楼相关*/
	public void createTeaHouse(TeaHouseModel teaHouseModel);
	public List<TeaHouseModel> queryPlayerTeaHouseList(Integer playerId);
	public void delTeaHouse(Integer teaHouseNum, Integer playerId);
	public void joinTeaHouse(Integer teaHouseNum, Integer playerId,String nickName, Integer status);
	public void auditEntryTeaHouse(Integer teaHouseNum, Integer playerId, Integer otherPlayerId, Integer status);
	public List<TeaHouseModel> queryTeaHousePlayerList(Integer teaHouseNum);
	public TeaHouseModel getTeaHouseTypeByTeaHouseNum(Integer teaHouseNum, Integer playerId);
	public void delTeaHouseUser(Integer teaHouseNum, Integer otherPlayerId, Integer playerId);
	public void delFromTeaHouse(Integer teaHouseNum, Integer playerId);
	public List<TeaHouseModel> queryPlayerJoinedTeaHouseList(Integer teaHouseNum);
	public List<UserRecordModel> getTeaHouseRecord(Integer teaHouseNum,Integer playerId);
	public List<UserRecordModel> getMyTeaHouseRecord(Integer teaHouseNum, Integer playerId);
	public boolean isPlayerInTeaHouse(Integer teaHouseNum, Integer playerId);
	public boolean isTeaHouseExist(Integer teaHouseNum);
	public void teaHouseConfig(Integer teaHouseNum, Integer playerId, String teaHouseOwnerWord, Integer isNeedAudit);
	
	public void setDianXiaoer(Integer teaHouseNum, Integer playerId, Integer otherPlayerId, Integer isDianXiaoer);
	
	public List<UserRecordModel> getTeaHouseBigWinner(Integer teaHouseNum);
	public List<UserRecordModel> getPaishenBoard();
	
	public List<TeaHouseModel> queryNeedAuditPlayerList(Integer teaHouseNum, Integer playerId);
	
	public TeaHouseModel getTeaHouseByTeaHouseNum(Integer teaHouseNum);
	
}
