package cn.worldwalker.game.wyqp.common.enums;

public enum ProductEnum {
	
	tenCards(1, 10, 1000),
	twentyCards(2, 20, 2000),
	thirtyCards(3, 40, 4000),
	fourtyCards(4, 60, 6000);
	
	/**商品id**/
	public Integer productId;
	/**房卡数量**/
	public Integer roomCardNum;
	/**价格，单位分**/
	public Integer price;
	
	private ProductEnum(Integer productId, Integer roomCardNum, Integer price){
		this.productId = productId;
		this.roomCardNum = roomCardNum;
		this.price = price;
	}
	
	public static ProductEnum getProductEnum(Integer productId){
		for(ProductEnum productEnum : ProductEnum.values()){
			if (productEnum.productId.equals(productId)) {
				return productEnum;
			}
		}
		return null;
	}
}
