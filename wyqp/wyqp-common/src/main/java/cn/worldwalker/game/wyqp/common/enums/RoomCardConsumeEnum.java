package cn.worldwalker.game.wyqp.common.enums;

public enum RoomCardConsumeEnum {
	
	nn_roomOwnerPayTenGames(1, 1, 10, 4, "房主付费,10局4张房卡"),
	nn_roomOwnerPayTwentyGames(1, 1, 20, 7, "房主付费,20局7张房卡"),
	nn_AAPayTenGames(1, 2, 10, 1, "AA制付费,10局每人1张房卡"),
	nn_AAPayTwentyGames(1, 2, 20, 2, "AA制付费,20局每人2张房卡"),
	
	jh_roomOwnerPayTenGames(3, 1, 8, 4, "房主付费8局需4张房卡"),
	jh_roomOwnerPayTwentyGames(3, 1, 16, 7, "房主付费16局需7张房卡"),
	jh_AAPayTenGames(3, 2, 8, 1, "AA制付费10局每人需1张房卡"),
	jh_AAPaySixtyGames(3, 2, 16, 2, "AA制付费16局每人需2张房卡");
	
	public Integer gameType;
	public Integer payType;
	public Integer totalGames;
	public Integer needRoomCardNum;
	public String desc;
	
	private RoomCardConsumeEnum(Integer gameType, Integer payType, Integer totalGames, Integer needRoomCardNum, String desc){
		this.gameType = gameType;
		this.payType = payType;
		this.totalGames = totalGames;
		this.needRoomCardNum = needRoomCardNum;
		this.desc = desc;
	}
	
	public static RoomCardConsumeEnum getRoomCardConsumeEnum(Integer gameType, Integer payType, Integer totalGames){
		for(RoomCardConsumeEnum consumeEnum : RoomCardConsumeEnum.values()){
			if (consumeEnum.gameType.equals(gameType) && consumeEnum.payType.equals(payType) && consumeEnum.totalGames.equals(totalGames)) {
				return consumeEnum;
			}
		}
		return null;
	}
}
