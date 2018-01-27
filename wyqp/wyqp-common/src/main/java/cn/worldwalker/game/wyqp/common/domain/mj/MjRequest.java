package cn.worldwalker.game.wyqp.common.domain.mj;

import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;
import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;

public class MjRequest extends BaseRequest{
	
	private BaseMsg msg = new BaseMsg() {
        /**麻将类型 1湖口麻将 2通山麻将*/
        private Integer mjType;
        /**牌索引*/
        private Integer cardIndex;
        /**吃牌类型，1吃、2碰、3杠*/
        private Integer chiPaiType;
    };

	public BaseMsg getMsg() {
		return msg;
	}

	public void setMsg(BaseMsg msg) {
		this.msg = msg;
	}
	
	
}
