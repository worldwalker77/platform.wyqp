package cn.worldwalker.game.wyqp.common.domain.nn;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;

public class NnRequest extends BaseRequest{
	
	private NnMsg msg = new NnMsg();

	public NnMsg getMsg() {
		return msg;
	}

	public void setMsg(NnMsg msg) {
		this.msg = msg;
	}
	
	
}
