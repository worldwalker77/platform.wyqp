package cn.worldwalker.game.wyqp.common.result;

public enum ResultCode {
	
	/**公用**/
	SUCCESS(0, "success"),
	NEED_LOGIN(1, "需要登录"),
	ROOM_NOT_EXIST(2, "房间不存在或已解散"),
	PARAM_ERROR(3, "参数错误"),
	SYSTEM_ERROR(4, "系统异常"),
	NOT_ALLOW_ENTRY_ROOM(5, "不允许加入此房间"),
	PLAYER_NOT_IN_ROOM(6, "玩家不在此房间中"),
	IS_NOT_YOUR_TURN(7, "抱歉，还没轮到你说话"),
	
	/**金花*/
	STAKE_SCORE_ERROR_1(8, "你的跟注分数必须大于或等于前一个玩家"),
	STAKE_SCORE_ERROR_2(9, "你的跟注分数必须大于或等于前一个玩家跟注分数的两倍"),
	STAKE_SCORE_ERROR_3(10, "你的跟注分数必须大于或等于前一个玩家的跟注分数一半"),
	PLAYER_STATUS_ERROR_1(11, "当前玩家状态错误，必须是未看牌或者已看牌"),
	ROOM_CARD_NOT_ENOUGH(12, "房卡数量不足"),
	ROOM_CARD_DEDUCT_THREE_TIMES_FAIL(13, "房卡扣除重试三次都失败"),
	ROOM_CARD_DEDUCT_EXCEPTION(14, "扣除房卡异常");
	
	public int code;
	public String desc;
	
	private ResultCode(int code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
}
