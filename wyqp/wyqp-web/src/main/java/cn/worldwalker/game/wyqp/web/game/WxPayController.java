package cn.worldwalker.game.wyqp.web.game;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.worldwalker.game.wyqp.common.domain.base.UserInfo;
import cn.worldwalker.game.wyqp.common.exception.BusinessException;
import cn.worldwalker.game.wyqp.common.exception.ExceptionEnum;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.common.utils.IPUtil;

import com.google.common.base.Charsets;

@Controller
@RequestMapping("game/")
public class WxPayController {
	
	private final static Log log = LogFactory.getLog(WxPayController.class);
	@Autowired
	private RedisOperationService redisOperationService;
	
	@Resource(name="commonGameService")
	private BaseGameService commonGameService;
	
	@RequestMapping("unifiedOrder")
	@ResponseBody
	public Result unifiedOrder(String token, Integer productId, HttpServletRequest request, HttpServletResponse response){
		Result result = new Result();
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtils.isEmpty(token) || productId == null) {
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
		String ip = IPUtil.getRemoteIp(request);
		try {
			result = commonGameService.unifiedOrder(productId, userInfo.getPlayerId(), ip);
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
			result.setCode(e.getBussinessCode());
			result.setDesc(e.getMessage());
			return result;
		} catch (Exception e) {
			log.error("系统异常", e);
			result.setCode(ExceptionEnum.SYSTEM_ERROR.index);
			result.setDesc(ExceptionEnum.SYSTEM_ERROR.description);
			return result;
		}
		return result;
	}
	
	@RequestMapping("wxPayCallBack")
	@ResponseBody
	public String wxPayCallBack(HttpServletRequest request){
		String xmlStr = parseWeixinCallback(request);
		return commonGameService.callback(xmlStr);
		
	}
	
	/**
	 * 解析微信回调参数
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private String parseWeixinCallback(HttpServletRequest request){
		// 获取微信调用我们notify_url的返回信息
		String result = "";
		InputStream inStream = null;
		ByteArrayOutputStream outSteam = null;
		try {
			inStream = request.getInputStream();
			outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			result = new String(outSteam.toByteArray(), Charsets.UTF_8.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(outSteam != null){
					outSteam.close();
					outSteam = null; // help GC
				}
				if(inStream != null){
					inStream.close();
					inStream = null;// help GC
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@RequestMapping("aaaaaaa")
	@ResponseBody
	public Result queryOrder(String outTradeNo, String transactionId){
		Result result = new Result();
		
		return result;
	}
	
}
