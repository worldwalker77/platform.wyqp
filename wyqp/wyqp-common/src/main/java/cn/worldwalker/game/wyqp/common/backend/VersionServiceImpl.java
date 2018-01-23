package cn.worldwalker.game.wyqp.common.backend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.dao.VersionDao;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.utils.RarUtil;

@Service
public class VersionServiceImpl implements VersionService{
	
	@Autowired
	private VersionDao versionDao;

	@Override
	public Result uploadClientFile(MultipartFile multipartFile, String newFeature, String clientVersion) {
		Result result = new Result();
		String fileName = multipartFile.getOriginalFilename();
        try {
        	
        	/**删除持久化目录下文件*/
        	RarUtil.deleteDir(new File(Constant.clientFileUploadPath));
        	/**重新创建目录*/
        	RarUtil.mkDir(Constant.clientFileUploadPath);
        	/**将文件存储到持久化目录下**/
			SaveFileToVirtualDir(multipartFile.getInputStream(), Constant.clientFileUploadPath, fileName);
			
			/**将持久化目录中的文件解压到file-manager的classespath 下的clientversion目录中*/
			String sourceRarPath = Constant.clientFileUploadPath + fileName;
			File sourceRar = new File(sourceRarPath);
			
			/**删除解压目录*/
        	RarUtil.deleteDir(new File(Constant.clientFileUnrarPath));
        	/**重新创建解压目录*/
        	RarUtil.mkDir(Constant.clientFileUnrarPath);
        	
			File destDir = new File(Constant.clientFileUnrarPath);
			RarUtil.unrar(sourceRar, destDir);
			VersionModel vmq = versionDao.selectVersion(new VersionModel());
			/**将下载链接持久化到数据库中*/
			String version = fileName.substring(0, fileName.length() - 4);
			String updateUrl = Constant.UPDATE_RUL;
			String codeUrl = Constant.CODE_URL;
			updateUrl = updateUrl.replace("VERSION", version);
			codeUrl = codeUrl.replace("VERSION", version);
			VersionModel versionModel = new VersionModel();
			versionModel.setCodeUrl(codeUrl);
			versionModel.setUpdateUrl(updateUrl);
			versionModel.setNewFeature(newFeature);
			versionModel.setClientVersion(clientVersion);
			if (vmq == null) {
				versionDao.insertVersion(versionModel);
			}else{
				versionModel.setId(vmq.getId());
				versionDao.updateVersion(versionModel);
			}
			
		} catch (Exception e) {
			result.setCode(ExceptionEnum.SYSTEM_ERROR.index);
			result.setDesc(ExceptionEnum.SYSTEM_ERROR.description);
			e.printStackTrace();
		}
		return result;
	}
	
	public void SaveFileToVirtualDir(InputStream stream,String path,String fileName) throws Exception{ 
		  
		  FileOutputStream fs=new FileOutputStream( path + fileName);
		  byte[] buffer =new byte[1024*1024];
		  int bytesum = 0;
		  int byteread = 0;
		  while ((byteread=stream.read(buffer))!=-1){
		     bytesum+=byteread;
		     fs.write(buffer,0,byteread);
		     fs.flush();
		  }
		  fs.close();
		  stream.close();     
	}
	
	public static void main(String[] args) {
		String fileName = "1111.rar";
		String version = fileName.substring(0, fileName.length() - 4);
	}

	@Override
	public Result getVersionList(VersionModel versionModel) {
		List<VersionModel> list = versionDao.selectVersionList(versionModel);
		Long total = versionDao.selectVersionListCount(versionModel);
		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", total);
		result.setData(map);
		return result;
	}

	@Override
	public VersionModel getVersion() {
		return versionDao.selectVersion(new VersionModel());
	}
	
}
