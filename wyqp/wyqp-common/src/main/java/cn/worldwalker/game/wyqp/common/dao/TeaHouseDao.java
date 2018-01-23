package cn.worldwalker.game.wyqp.common.dao;

import java.util.List;

import cn.worldwalker.game.wyqp.common.domain.base.TeaHouseModel;

public interface TeaHouseDao {
	
	public Integer insertTeaHouseUser(TeaHouseModel teaHouseModel);
	public Integer insertTeaHouse(TeaHouseModel teaHouseModel);
	public Integer insertTeaHouseType(TeaHouseModel teaHouseModel);
	public Integer auditTeaHouseUser(TeaHouseModel teaHouseModel);
	public void deleteTeaHouseUserByCondition(TeaHouseModel teaHouseModel);
	public void deleteTeaHouseByCondition(TeaHouseModel teaHouseModel);
	public List<TeaHouseModel> getTeaHousePlayerList(TeaHouseModel teaHouseModel);
	public List<TeaHouseModel> getPlayerTeaHouseList(TeaHouseModel teaHouseModel);
	public TeaHouseModel getTeaHouseTypeByTeaHouseNum(TeaHouseModel teaHouseModel);
	public TeaHouseModel getTeaHouseTypeByCondition(TeaHouseModel teaHouseModel);
	public TeaHouseModel getTeaHouseUserByCondition(TeaHouseModel teaHouseModel);
	public TeaHouseModel getTeaHouseByCondition(TeaHouseModel teaHouseModel);
	public List<TeaHouseModel> getPlayerJoinedTeaHouseList(TeaHouseModel teaHouseModel);
	public Integer updateTeaHouseByCondition(TeaHouseModel teaHouseModel);
	public List<TeaHouseModel> getNeedAuditPlayerList(TeaHouseModel teaHouseModel);
	public TeaHouseModel getTeaHouseByTeaHouseNum(TeaHouseModel teaHouseModel);
	public Integer updateTeaHouseUserByCondition(TeaHouseModel teaHouseModel);
	
}
