package cn.worldwalker.game.wyqp.common.rpc;

import cn.worldwalker.game.wyqp.common.domain.base.WeiXinUserInfo;

public interface WeiXinRpc {
	public WeiXinUserInfo getWeiXinUserInfo(String code);
	
	public String getAccessToken();
	
	public String getTicket();
}
