package cn.worldwalker.game.wyqp.common.dao;

import java.util.List;

import cn.worldwalker.game.wyqp.common.domain.base.ProductModel;

public interface ProductDao {
	
	 public ProductModel getProductById(Integer productId);
	 
	 public List<ProductModel> getProductList();
	 
}
