package cn.worldwalker.game.wyqp.common.domain.jh;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;

public class JhRequest extends BaseRequest{
	
	private JhMsg msg = new JhMsg();

	public JhMsg getMsg() {
		return msg;
	}

	public void setMsg(JhMsg msg) {
		this.msg = msg;
	}
	
	
}
