package cn.worldwalker.game.wyqp.common.rpc;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.domain.base.WeiXinAccess;
import cn.worldwalker.game.wyqp.common.domain.base.WeiXinUserInfo;
import cn.worldwalker.game.wyqp.common.utils.HttpClientUtil;
import cn.worldwalker.game.wyqp.common.utils.HttpClientUtils;

@Service
public class WeiXinRpcImpl implements WeiXinRpc{
	public final static Log log = LogFactory.getLog(WeiXinRpcImpl.class);
	public static String accessToken = null;
	@Override
	public WeiXinUserInfo getWeiXinUserInfo(String code) {
		WeiXinAccess weiXinAccess = getWeiXinAccessByCode(code);
		if (weiXinAccess!=null){
			String openid = weiXinAccess.getOpenid();
			String access_token = weiXinAccess.getAccess_token();
			WeiXinUserInfo weixinUserInfo = getWeixinUserInfo(access_token,openid);
			return weixinUserInfo;
		}else{
			return null;
		}
	}
	
	private WeiXinUserInfo getWeixinUserInfo(String access_token,String openid){
		String url = Constant.getWXUserInfoUrl;
		url = url.replace("ACCESS_TOKEN", access_token);
		url = url.replace("OPENID", openid);
		JSONObject obj = HttpClientUtil.httpRequest(url, "POST", null);
		if (obj != null && obj.containsKey("errcode")){
			log.error("获取微信信息失败，access_token:" + access_token + "openid:" + openid + ", 返回结果：" + obj.toString());
			return null;
		}
			
		WeiXinUserInfo weixinUserInfo = new WeiXinUserInfo();
		weixinUserInfo.setCity(obj.getString("city"));
		weixinUserInfo.setCountry(obj.getString("country"));
		weixinUserInfo.setHeadImgUrl(obj.getString("headimgurl"));
		weixinUserInfo.setName(obj.getString("nickname"));
		weixinUserInfo.setOpneid(obj.getString("openid"));
		weixinUserInfo.setProvince(obj.getString("province"));
		weixinUserInfo.setSex(obj.getInt("sex"));
//		weixinUserInfo.setPrivilege(obj.getJSONArray("privilege"));
		return weixinUserInfo;
	}
	
	private WeiXinAccess getWeiXinAccessByCode(String code){
		String url = Constant.getOpenidAndAccessCode;
		url = url.replace("CODE", code);
		JSONObject obj = HttpClientUtil.httpRequest(url, "POST", null);
		if (obj != null && obj.containsKey("errcode")){
			log.error("获取微信accessToken失败，code:" + code + ", 返回结果：" + obj.toString());
			return null;
		}
		else{
			WeiXinAccess weiXinAccess = new WeiXinAccess();
			String openid = obj.getString("openid");
			String access_token = obj.getString("access_token");
			String refresh_token = obj.getString("refresh_token");//用户刷新access_token
			Long expires_in = obj.getLong("expires_in");//access_token接口调用凭证超时时间，单位（秒）
			String scope = obj.getString("scope");//用户授权的作用域，使用逗号（,）分隔
//			String unionid = obj.getString("unionid");// 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
			weiXinAccess.setOpenid(openid);
			weiXinAccess.setAccess_token(access_token);
			weiXinAccess.setRefresh_token(refresh_token);
			weiXinAccess.setExpires_in(expires_in);
			weiXinAccess.setScope(scope);
//			weiXinAccess.setUnionid(unionid);
			return weiXinAccess;
		}
	}

	@Override
	public String getAccessToken() {
		String accessTokenUrl = Constant.getAccessTokenUrl;
		try {
			JSONObject ob = JSONObject.fromObject(HttpClientUtils.get(accessTokenUrl));
			return (String)ob.get("access_token");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getTicket() {
		try {
			String url = Constant.getTicketUrl;
			if (StringUtils.isBlank(accessToken)) {
				accessToken = getAccessToken();
			}
			String requestUrl = url.replace("ACCESS_TOKEN", accessToken);
			String resStr = HttpClientUtils.get(requestUrl);
			JSONObject ob = JSONObject.fromObject(resStr);
			return (String)ob.get("ticket");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
