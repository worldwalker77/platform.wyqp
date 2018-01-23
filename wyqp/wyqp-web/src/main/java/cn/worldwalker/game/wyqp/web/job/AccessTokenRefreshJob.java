package cn.worldwalker.game.wyqp.web.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.worldwalker.game.wyqp.common.rpc.WeiXinRpc;
import cn.worldwalker.game.wyqp.common.rpc.WeiXinRpcImpl;

@Component(value="accessTokenRefreshJob")
public class AccessTokenRefreshJob{
	public final static Log log = LogFactory.getLog(AccessTokenRefreshJob.class);
	@Autowired
	private WeiXinRpc weiXinRpc;
	public void doTask() {
		try {
			WeiXinRpcImpl.accessToken = weiXinRpc.getAccessToken();
		} catch (Exception e) {
			log.error("定时任务刷新access_token异常", e);
		}
	}
	
}
