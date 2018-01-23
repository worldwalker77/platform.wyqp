package cn.worldwalker.game.wyqp.common.dao;

import java.util.List;

import cn.worldwalker.game.wyqp.common.backend.GameModel;
import cn.worldwalker.game.wyqp.common.backend.GameQuery;

public interface GameDao {
	
	public List<GameModel> getMyMembers(GameQuery gameQuery);
	public Long getMyMembersCount(GameQuery gameQuery);
	
	public List<GameModel> getBillingDetails(GameQuery gameQuery);
	public Long getBillingDetailsCount(GameQuery gameQuery);
	
	public List<GameModel> getWithDrawalRecords(GameQuery gameQuery);
	public Long getWithDrawalRecordsCount(GameQuery gameQuery);
	
	public GameModel getProxyInfo(GameQuery gameQuery);
	
	public GameModel getProxyByPhoneAndPassword(GameQuery gameQuery);
	
	public List<GameModel> getUserByCondition(GameQuery gameQuery);
	public Long getUserByConditionCount(GameQuery gameQuery);
	
	public Integer updateRoomCardNumByPlayerId(GameQuery gameQuery);
	
	public Integer updateProbabilityByPlayerId(GameQuery gameQuery);
	
	public List<GameModel> getProxys(GameQuery gameQuery);
	public Long getProxysCount(GameQuery gameQuery);
	
	public Integer insertProxy(GameQuery gameQuery);
	
	public Integer updateProxy(GameQuery gameQuery);
	
	
}
