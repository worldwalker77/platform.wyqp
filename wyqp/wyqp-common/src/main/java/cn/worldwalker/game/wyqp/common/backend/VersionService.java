package cn.worldwalker.game.wyqp.common.backend;

import org.springframework.web.multipart.MultipartFile;

import cn.worldwalker.game.wyqp.common.result.Result;

public interface VersionService {
	
	public Result uploadClientFile(MultipartFile multipartFile, String newFeature, String clientVersion);
	
	public Result getVersionList(VersionModel versionModel);
	
	public VersionModel getVersion();
}
