package cn.worldwalker.game.wyqp.common.dao;

import cn.worldwalker.game.wyqp.common.domain.base.OrderModel;

public interface OrderDao {
	 
	 public Integer insertOrder(OrderModel orderModel);
	 
	 public Integer updateOrder(OrderModel orderModel);
	 
	 public OrderModel getOderByOrderId(Long orderId);
	 
}
