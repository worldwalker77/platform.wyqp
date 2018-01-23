package cn.worldwalker.game.wyqp.web.game;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.manager.CommonManager;
import cn.worldwalker.game.wyqp.common.result.Result;
import cn.worldwalker.game.wyqp.common.rpc.WeiXinRpc;
import cn.worldwalker.game.wyqp.common.service.BaseGameService;
import cn.worldwalker.game.wyqp.common.service.RedisOperationService;
import cn.worldwalker.game.wyqp.nn.service.NnGameService;

@Controller
@RequestMapping("weixin/")
public class WxController {
	
	public final static Log log = LogFactory.getLog(WxController.class);
	
	@Autowired
	private RedisOperationService redisOperationService;
	@Resource(name="commonGameService")
	private BaseGameService commonGameService;
	
	@Resource(name="nnGameService")
	private NnGameService nnGameService;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	private WeiXinRpc weiXinRpc;
	
	@RequestMapping("getWxConfig")
	@ResponseBody
	public Result getWxConfig(String url, HttpServletRequest request ,HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		String jsapi_ticket = weiXinRpc.getTicket();
		if (StringUtils.isBlank(jsapi_ticket)) {
			result.setCode(1);
			result.setDesc("获取jsapi_ticket异常");
			return result;
		}
        Map<String, Object> ret = new HashMap<String, Object>();
        String appId = Constant.APPID; // 必填，公众号的唯一标识
//        String requestUrl = request.getRequestURL().toString();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
        String nonceStr = UUID.randomUUID().toString(); // 必填，生成签名的随机串
        
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        String sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr+ "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sign.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ret.put("appId", appId);
        ret.put("timestamp", timestamp);
        ret.put("nonceStr", nonceStr);
        ret.put("signature", signature);
        result.setData(ret);
        return result;
    }

	    
	    /**
	    * 方法名：byteToHex</br>
	    * 详述：字符串加密辅助方法 </br>
	    * 开发人员：souvc  </br>
	    * 创建时间：2016-1-5  </br>
	    * @param hash
	    * @return 说明返回值含义
	    * @throws 说明发生此异常的条件
	     */
	    private static String byteToHex(final byte[] hash) {
	        Formatter formatter = new Formatter();
	        for (byte b : hash) {
	            formatter.format("%02x", b);
	        }
	        String result = formatter.toString();
	        formatter.close();
	        return result;

	    }
}
