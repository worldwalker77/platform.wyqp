package cn.worldwalker.game.wyqp.common.domain.mj;

import cn.worldwalker.game.wyqp.common.domain.base.BaseMsg;

public class MjMsg extends BaseMsg{
	/**麻将类型 1湖口麻将 2通山麻将*/
	private Integer mjType;
	/**牌索引*/
	private Integer cardIndex;
	/**吃牌类型，1吃、2碰、3杠*/
	private Integer chiPaiType;
	
}
