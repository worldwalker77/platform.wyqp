package cn.worldwalker.game.wyqp.ddz.card.union;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;

import java.util.ArrayList;
import java.util.List;

public class FeijiCardUnion implements CardUnion {

    private ShunCardUnion shunCardUnion;
    private BaseCardUnion baseCardUnion;
    private int withTypeCount;

    public FeijiCardUnion(ShunCardUnion shunCardUnion,
                          BaseCardUnion baseCardUnion, int withTypeCount) {
        if (shunCardUnion.getBaseType().getCount() == 3 &&
                shunCardUnion.getSize() == 2 &&
                (withTypeCount <=2 )) {
            this.shunCardUnion = shunCardUnion;
            this.baseCardUnion = baseCardUnion;
            this.withTypeCount = withTypeCount;
        } else {
            throw new IllegalArgumentException(shunCardUnion + "with" + withTypeCount);
        }
    }

    @Override
    public int getValue() {
        return shunCardUnion.getValue();
    }

    @Override
    public String getType(){
        return shunCardUnion.getType() + "_" + baseCardUnion.getType() + "_" +  withTypeCount;
    }

    @Override
    public List<DdzCard> generateCardList() {
        List<DdzCard> cardList = new ArrayList<>(20);
        cardList.addAll(shunCardUnion.generateCardList());
        for (int i=0; i<withTypeCount; i++){
            cardList.addAll(baseCardUnion.generateCardList());
        }
        return null;
    }

    @Override
    public String toString() {
        return shunCardUnion +
                "__" + baseCardUnion +
                "x" + withTypeCount;
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
