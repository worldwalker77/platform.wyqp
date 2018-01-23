package cn.worldwalker.game.wyqp.common.dao;

import cn.worldwalker.game.wyqp.common.domain.base.ProxyModel;

public interface ProxyDao {
	
	 public Integer insertProxyUser(ProxyModel proxyModel);
	 
	 public Integer getProxyCountByProxyId(Integer proxyId);
	 
	 public Integer getProxyUserCountByPlayerId(Integer playerId);
	 
	 public Integer getProxyIdByPlayerId(Integer playerId);
	 
	 public Long getProxyTotalIncome(Integer proxyId);
	 
	 public Integer updateProxyTotalIncome(ProxyModel proxyModel);
	 
	 
	 public ProxyModel getProxyInfo(Integer proxyId);
	 
	 public Integer updateProxyInfo(ProxyModel proxyModel);
	 
	 
}
