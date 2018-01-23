package cn.worldwalker.game.wyqp.common.enums;

public enum FeedbackTypeEnum {
	
	complaintSuggestion(1, "投诉建议"),
	problemFeedback(2, "问题反馈");
	
	public Integer type;
	public String desc;
	
	private FeedbackTypeEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
	
	public static FeedbackTypeEnum getRoomStatusEnum(Integer type){
		for(FeedbackTypeEnum feedbackTypeEnum : FeedbackTypeEnum.values()){
			if (feedbackTypeEnum.type.equals(type)) {
				return feedbackTypeEnum;
			}
		}
		return null;
	}
}
