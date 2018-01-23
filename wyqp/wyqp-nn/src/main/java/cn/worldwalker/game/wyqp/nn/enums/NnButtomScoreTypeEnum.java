package cn.worldwalker.game.wyqp.nn.enums;

public enum NnButtomScoreTypeEnum {
	
	buttom_1_2_3_4(1, 6, "1_2_3_4"),
	buttom_2_4_6_8(2, 12, "2_4_6_8"),
	buttom_3_6_9_12(3, 20, "3_6_9_12");
	
	public Integer buttomType;
	public Integer randomScore;
	public String value;
	
	private NnButtomScoreTypeEnum(Integer buttomType, Integer randomScore, String value){
		this.buttomType = buttomType;
		this.randomScore = randomScore;
		this.value = value;
	}
	
	public static NnButtomScoreTypeEnum getNnButtomScoreTypeEnum(Integer buttomType){
		for(NnButtomScoreTypeEnum nnButtomScoreTypeEnum : NnButtomScoreTypeEnum.values()){
			if (nnButtomScoreTypeEnum.buttomType.equals(buttomType)) {
				return nnButtomScoreTypeEnum;
			}
		}
		return null;
	}
}
