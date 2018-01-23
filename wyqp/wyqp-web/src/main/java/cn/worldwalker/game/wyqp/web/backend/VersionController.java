package cn.worldwalker.game.wyqp.web.backend;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.worldwalker.game.wyqp.common.backend.VersionModel;
import cn.worldwalker.game.wyqp.common.backend.VersionService;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.common.utils.CustomizedPropertyConfigurer;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;

@Controller
@RequestMapping("backend/")
public class VersionController {
	private static final Logger log = Logger.getLogger(VersionController.class);
	@Autowired
	private VersionService versionService;
	@Autowired
	private RedisOperationService redisOperationService;
	
	@RequestMapping(value="/versioncontroll")
	public ModelAndView versionControll(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("backend/version/versioncontroll");
//		String a = null;
//		a.split("a");
		return mv;
	}
	
	/**
	 * 上传文件到tomcat指定目录
	 * @param multipartFiles
	 * @param clientVersion 版本号
	 * @param changeLog 新特性
	 * @param req
	 * @return
	 */
	@RequestMapping("/uploadClientfile")  
	@ResponseBody  
	public Result uploadFile(@RequestParam("multipartFiles") MultipartFile[] multipartFiles, String clientVersion, String changeLog, HttpServletRequest request, HttpServletResponse response) { 
		Result result = new Result();
		if ( multipartFiles == null || multipartFiles.length == 0) {
			result.setCode(1);
			result.setDesc("异常");
			return result;
		}
	    try{  
//	    	String unrarPath = request.getSession().getServletContext().getRealPath(System.getProperty("file.separator") + "clientversion" +  System.getProperty("file.separator"));
	    	versionService.uploadClientFile(multipartFiles[0], changeLog, clientVersion);
	    } catch (Exception e) {  
	    	log.error("uploadfile error", e);
	    	result.setCode(1);
			result.setDesc("异常");
	    } 
	    return result;
	} 
	
	@RequestMapping(value="/getVersionList")
	@ResponseBody
	public Result getVersionList(VersionModel model){
		return versionService.getVersionList(model);
	}
	
	@RequestMapping(value="/getVersion")
	@ResponseBody
	public Map<String, Object> getVersion(HttpServletResponse response){
		VersionModel versionModel = versionService.getVersion();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code_url", versionModel.getCodeUrl());
		map.put("update_url", versionModel.getUpdateUrl());
		response.addHeader("Access-Control-Allow-Origin", "*");
		return map;
	}
	
	@RequestMapping(value = "/uploadVoice",method = RequestMethod.POST)
	@ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file, String token, Model model,HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (file == null) {
			log.error("请求, 上传语音文件， file:"+ ", token:" + token);
		}else{
			log.error("请求, 上传语音文件， file:"+ file.toString() + ", token:" + token);
		}
		if (StringUtils.isEmpty(token)) {
			token = request.getHeader("token");
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
        if (file == null || StringUtils.isEmpty(token)) {
        	result.setCode(ExceptionEnum.PARAMS_ERROR.index);
        	result.setDesc(ExceptionEnum.PARAMS_ERROR.description);
        	return result;
		}
        UserInfo userInfo = redisOperationService.getUserInfo(token);
        if (userInfo == null) {
        	result.setCode(ExceptionEnum.NEED_LOGIN.index);
        	result.setDesc(ExceptionEnum.NEED_LOGIN.description);
        	return result;
		}
        Integer roomId = userInfo.getRoomId();
        if (roomId == null) {
        	result.setCode(ExceptionEnum.ROOM_ID_NOT_EXIST.index);
        	result.setDesc(ExceptionEnum.ROOM_ID_NOT_EXIST.description);
        	return result;
		}
		try {
			File dir=new File(request.getSession().getServletContext().getRealPath("/voicefiles/" + roomId));
			if(!dir.exists()){
			    dir.mkdirs();
			}
			String fileName = file.getOriginalFilename();
			String path = dir.getAbsolutePath() + "/" + fileName;
			file.transferTo(new File(path));
			result.setData("http://" + CustomizedPropertyConfigurer.getContextProperty("cur.domain") + "/voicefiles/" + roomId + "/" + fileName);
		} catch (Exception e) {
			result.setCode(1);
			result.setDesc("系统异常");
			e.printStackTrace();
		}
		log.error("返回, 上传语音文件， result:" + JsonUtil.toJson(result));
        return result;
    }
}
