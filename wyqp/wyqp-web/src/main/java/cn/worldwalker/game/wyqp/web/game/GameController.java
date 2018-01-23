package cn.worldwalker.game.wyqp.web.game;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.manager.CommonManager;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.common.utils.JsonUtil;
import cn.worldwalker.game.wyqp.nn.service.NnGameService;

@Controller
@RequestMapping("game/")
public class GameController {
	
	public final static Log log = LogFactory.getLog(GameController.class);
	
	@Autowired
	private RedisOperationService redisOperationService;
	@Resource(name="commonGameService")
	private BaseGameService commonGameService;
	
	@Resource(name="nnGameService")
	private NnGameService nnGameService;
	@Autowired
	private CommonManager commonManager;
	
	@RequestMapping("login")
	@ResponseBody
	public Result login(String code,String deviceType,HttpServletResponse response,HttpServletRequest request){
		log.info("请求 ,登录: code=" + code);
		response.addHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		try {
			if (redisOperationService.isLoginFuseOpen()) {
				result = commonGameService.login(code, deviceType, request);
			}else{
				result = commonGameService.login1(code, deviceType, request);
			}
			
		} catch (Exception e) {
			log.error("code:" + code, e);
			result.setCode(1);
			result.setDesc("系统异常");
		}
		log.info("返回 ,登录: " + JsonUtil.toJson(result));
		return result;
	}
	/**
	 * 游戏列表页，进入的时候如果没有code，则需要302重定向到微信进行授权后，再重定向回来进行微信信息获取
	 * @param code
	 * @param deviceType
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView gameMallIndex(String code, String deviceType, HttpServletResponse response, HttpServletRequest request){
		if (StringUtils.isBlank(code)) {
			
			try {
				response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APPID + "&redirect_uri=http://" + Constant.domain + "/game/index&response_type=code&scope=snsapi_userinfo&state=3fcdcfe0a5675569b6f223fe11d67300&connect_redirect=1#wechat_redirect");
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("game/index");
		UserInfo userInfo = null;
		try {
			userInfo = (UserInfo)commonGameService.login(code, deviceType, request).getData();
			mv.addObject("playerId", userInfo.getPlayerId());
			mv.addObject("nickName", userInfo.getNickName());
			mv.addObject("headImgUrl", userInfo.getHeadImgUrl());
			mv.addObject("token", userInfo.getToken());
			mv.addObject("roomCardNum", userInfo.getRoomCardNum());
		} catch (Exception e) {
			log.error("code:" + code, e);
		}
		return mv;
	}
	
	
	/**
	 * 游戏列表页，进入的时候如果没有code，则需要302重定向到微信进行授权后，再重定向回来进行微信信息获取
	 * @param code
	 * @param deviceType
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("zjh")
	public ModelAndView zjh(String code, String roomId, String token, String playerNumLimit, String deviceType, HttpServletResponse response, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(playerNumLimit)) {
			if (StringUtils.isNotBlank(roomId)) {
				String[] temp = roomId.split("_");
				if (temp.length == 2) {
					roomId = temp[0];
					playerNumLimit = temp[1];
				}
			}
		}
		
		if (StringUtils.isNotBlank(token)) {
			if (StringUtils.isNotBlank(roomId)) {
				if (StringUtils.isNotBlank(code)) {
					mv.setViewName("game/zjh");
					mv.addObject("token", token);
					mv.addObject("roomId", roomId);
					mv.addObject("playerNumLimit", StringUtils.isBlank(playerNumLimit)?"":playerNumLimit);
					return mv;
				}else{
					mv.setViewName("game/zjh");
					mv.addObject("token", token);
					mv.addObject("roomId", roomId);
					mv.addObject("playerNumLimit", StringUtils.isBlank(playerNumLimit)?"":playerNumLimit);
					return mv;
				}
			}else{
				if (StringUtils.isNotBlank(code)) {
					mv.setViewName("game/zjh");
					mv.addObject("token", token);
					mv.addObject("roomId", "");
					mv.addObject("playerNumLimit", StringUtils.isBlank(playerNumLimit)?"":playerNumLimit);
					return mv;
				}else{
					mv.setViewName("game/zjh");
					mv.addObject("token", token);
					mv.addObject("roomId", "");
					mv.addObject("playerNumLimit", StringUtils.isBlank(playerNumLimit)?"":playerNumLimit);
					return mv;
				}
			}
		}else{
			if (StringUtils.isNotBlank(roomId)) {
				if (StringUtils.isNotBlank(code)) {
					UserInfo userInfo = (UserInfo)commonGameService.login(code, deviceType, request).getData();
					try {
						if (StringUtils.isNotBlank(playerNumLimit)) {
							response.sendRedirect("http://" + Constant.domain + "/game/zjh?"
									+ "roomId=" + roomId + "_" + playerNumLimit + "&token="+ userInfo.getToken());
						}else{
							response.sendRedirect("http://" + Constant.domain + "/game/zjh?"
									+ "roomId=" + roomId + "&token="+ userInfo.getToken());
						}
						return null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					try {
						if (StringUtils.isNotBlank(playerNumLimit)) {
							response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APPID + "&"
									+ "redirect_uri=http://" + Constant.domain + "/game/zjh?roomId=" + roomId + "_" + playerNumLimit + "&response_type=code&scope=snsapi_userinfo&state=3fcdcfe0a5675569b6f223fe11d67300&connect_redirect=1#wechat_redirect");

						}else{
							response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APPID + "&"
									+ "redirect_uri=http://" + Constant.domain + "/game/zjh?roomId=" + roomId + "&response_type=code&scope=snsapi_userinfo&state=3fcdcfe0a5675569b6f223fe11d67300&connect_redirect=1#wechat_redirect");

						}
						return null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				if (StringUtils.isNotBlank(code)) {
					UserInfo userInfo = (UserInfo)commonGameService.login(code, deviceType, request).getData();
					try {
						if (StringUtils.isNotBlank(playerNumLimit)) {
							response.sendRedirect("http://" + Constant.domain + "/game/zjh?"
									+ "token="+ userInfo.getToken() + "&playerNumLimit=" + playerNumLimit);
						}else{
							response.sendRedirect("http://" + Constant.domain + "/game/zjh?"
									+ "token="+ userInfo.getToken());
						}
						
						return null;
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else{
					try {
						if (StringUtils.isNotBlank(playerNumLimit)) {
							response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APPID + "&"
									+ "redirect_uri=http://" + Constant.domain + "/game/zjh?playerNumLimit=" + playerNumLimit + "&response_type=code&scope=snsapi_userinfo&state=3fcdcfe0a5675569b6f223fe11d67300&connect_redirect=1#wechat_redirect");
					
						}else{
							response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APPID + "&"
									+ "redirect_uri=http://" + Constant.domain + "/game/zjh?&response_type=code&scope=snsapi_userinfo&state=3fcdcfe0a5675569b6f223fe11d67300&connect_redirect=1#wechat_redirect");
					
						}
						return null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			response.sendRedirect("http://" + Constant.domain + "/game/index");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping("h5Login")
	@ResponseBody
	public Result h5Login(String token,HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		try {
			UserInfo userInfo = redisOperationService.getUserInfo(token);
			result.setData(userInfo);
		} catch (Exception e) {
			log.error("token:" + token, e);
			result.setCode(1);
			result.setDesc("系统异常");
		}
		return result;
	}
	
	
}
