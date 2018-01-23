package cn.worldwalker.game.wyqp.common.dao;

import java.util.List;

import cn.worldwalker.game.wyqp.common.backend.VersionModel;

public interface VersionDao {
	
	public Integer updateVersion(VersionModel versionModel);
	
	public List<VersionModel> selectVersionList(VersionModel versionModel);
	
	public Long selectVersionListCount(VersionModel versionModel);
	
	public VersionModel selectVersion(VersionModel versionModel);
	
	public void insertVersion(VersionModel versionModel);
	
}
