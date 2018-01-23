package cn.worldwalker.game.wyqp.common.enums;

public enum ChatTypeEnum {
	
	text(1, "文本"),
	emotion(2, "表情"),
	sound(3, "语音"),
	img(4, "图片"),
	specialEmotion(5, "给特定玩家发的特殊表情"),
	voiceChat(6, "语音聊天");
	
	public int type;
	public String desc;
	
	private ChatTypeEnum(int type, String desc){
		this.type = type;
		this.desc = desc;
	}
	
	public static ChatTypeEnum getGameTypeEnumByType(int type){
		for(ChatTypeEnum chatTypeEnum : ChatTypeEnum.values()){
			if (type == chatTypeEnum.type) {
				return chatTypeEnum;
			}
		}
		return null;
	}
	
}
