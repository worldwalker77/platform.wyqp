package cn.worldwalker.game.wyqp.common.enums;

public enum MjMsgTypeEnum {
	
	entryHall(1, "进入大厅"),
	createRoom(2, "创建房间"),
	entryRoom(3, "进入房间"),
	ready(4, "点击准备"),
	dealCards(5, "发牌消息"),
	stake(6, "跟注"),
	watchCards(7, "看牌"),
	manualCardsCompare(8, "局中发起比牌"),
	discardCards(9, "弃牌"),
	curSettlement(10, "大结算"),
	totalSettlement(11, "大结算"),
	autoCardsCompare(12, "到达跟注数量上限的自动比牌"),
	dissolveRoom(13, "解散房间"),
	agreeDissolveRoom(14, "同意解散房间"),
	disagreeDissolveRoom(15, "不同意解散房间"),
	successDissolveRoom(16, "成功解散房间通知"),
	refreshRoom(17, "刷新房间信息"),
	offlineNotice(18, "通知玩家离线"),
	onlineNotice(19, "通知玩家上线"),
	delRoomConfirmBeforeReturnHall(20, "客户端返回大厅时的通知消息"),
	queryPlayerInfo(21, "查看玩家信息"),
	dissolveRoomCausedByOffline(22, "玩家离线超过20分钟，解散房间"),
	chatMsg(23, "聊天消息"),
	heartBeat(24, "心跳检测"),
	userRecord(25, "玩家战绩"),
	userFeedback(26, "用户反馈"),
	updatePlayerInfo(27, "玩家信息更新"),
	notice(28, "公告通知"),
	roomCardNumUpdate(29, "房卡数更新"),
	sendEmoticon(30, "给某个玩家发送特殊的表情符号");
	
	public int msgType;
	public String desc;
	
	private MjMsgTypeEnum(int msgType, String desc){
		this.msgType = msgType;
		this.desc = desc;
	}
	
	public static MjMsgTypeEnum getMsgTypeEnumByType(int msgType){
		for(MjMsgTypeEnum msgTypeEnum : MjMsgTypeEnum.values()){
			if (msgType == msgTypeEnum.msgType) {
				return msgTypeEnum;
			}
		}
		return null;
	}
	
}
