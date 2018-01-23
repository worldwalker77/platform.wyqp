package cn.worldwalker.game.wyqp.common.dao;

import java.util.Map;

import cn.worldwalker.game.wyqp.common.domain.base.UserModel;

public interface UserDao {
	
	 public UserModel getUserByWxOpenId(String wxOpenId);
	 
	 public UserModel getUserById(Integer id);
	 
	 public Integer insertUser(UserModel userModel);
	 
	 public Integer deductRoomCard(Map<String, Object> map);
	 
	 public Integer addRoomCard(Map<String, Object> map);
	 
}
