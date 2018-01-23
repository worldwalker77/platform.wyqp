package cn.worldwalker.game.wyqp.common.domain.mj;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;

public class MjRequest extends BaseRequest{
	
	private MjMsg msg = new MjMsg();

	public MjMsg getMsg() {
		return msg;
	}

	public void setMsg(MjMsg msg) {
		this.msg = msg;
	}
	
	
}
