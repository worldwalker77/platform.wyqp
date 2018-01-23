package cn.worldwalker.game.wyqp.common.dao;

import java.util.List;

import cn.worldwalker.game.wyqp.common.domain.base.UserRecordModel;

public interface UserRecordDao {
	
	public long insertRecord(UserRecordModel model);
	
	public long batchInsertRecord(List<UserRecordModel> modelList);
	
	public List<UserRecordModel> getUserRecord(UserRecordModel model);
	
	public List<UserRecordModel> getTeaHouseRecord(UserRecordModel model);
	
	public List<UserRecordModel> getMyTeaHouseRecord(UserRecordModel model);
	
	public List<UserRecordModel> getTeaHouseBigWinner(UserRecordModel model);
	
	public List<UserRecordModel> getPaishenBoard(UserRecordModel model);
	
}
