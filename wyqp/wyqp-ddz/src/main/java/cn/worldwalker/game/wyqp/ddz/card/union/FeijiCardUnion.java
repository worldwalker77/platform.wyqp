package cn.worldwalker.game.wyqp.ddz.card.union;

import java.util.ArrayList;
import java.util.List;

public class FeijiCardUnion implements CardUnion {

    private ShunCardUnion shunCardUnion;
//    private List<BaseCardUnion> baseCardUnionList;
    private BaseCardUnion baseCardUnion1;
    private BaseCardUnion baseCardUnion2;

    public FeijiCardUnion(ShunCardUnion shunCardUnion, List<BaseCardUnion> baseCardUnionList){
//        if (shunCardUnion.getBaseType().getCount() == 3 &&
//                shunCardUnion.getSize() == 2 &&
//                (withTypeCount <=2 )) {
        this.shunCardUnion = shunCardUnion;
        this.baseCardUnion1 = baseCardUnionList.get(0);
        this.baseCardUnion2= baseCardUnionList.get(1);
//        } else {
//            throw new IllegalArgumentException("" + shunCardUnion + baseCardUnion1 + baseCardUnion2);
//        }
    }

    @Override
    public Integer getValue() {
        return shunCardUnion.getValue();
    }

    @Override
    public String getType(){
        return shunCardUnion.getType() + "带" + baseCardUnion1.getType() + "带" +  baseCardUnion2;
    }

    @Override
    public List<Integer> getCardList() {
        List<Integer> cardList = new ArrayList<>(20);
        cardList.addAll(shunCardUnion.getCardList());
        cardList.addAll(baseCardUnion1.getCardList());
        cardList.addAll(baseCardUnion2.getCardList());
        return cardList;
    }

    @Override
    public String toString() {
        return shunCardUnion + " 带" + baseCardUnion1 + " 带" +  baseCardUnion2;
    }

    public enum FeijiEnum {
        DAN(3,2,1,2),
        DUI(3,2,2,2);

        FeijiEnum(int mainTypeCount, int mainTypeLen , int withTypeCount, int withTypeCnt) {
            this.mainTypeCount = mainTypeCount;
            this.mainTypeLen = mainTypeLen;
            this.withTypeCount = withTypeCount;
            this.withTypeCnt = withTypeCnt;
        }

        private int mainTypeCount;
        private int mainTypeLen;
        private int withTypeCount;
        private int withTypeCnt;

        public int getMainTypeCount() {
            return mainTypeCount;
        }

        public int getMainTypeLen() {
            return mainTypeLen;
        }

        public int getWithTypeCount() {
            return withTypeCount;
        }

        public int getWithTypeCnt() {
            return withTypeCnt;
        }
    }
}
