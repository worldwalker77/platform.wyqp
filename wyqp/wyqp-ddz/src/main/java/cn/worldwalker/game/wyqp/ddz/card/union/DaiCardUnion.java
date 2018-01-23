package cn.worldwalker.game.wyqp.ddz.card.union;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;

import java.util.ArrayList;
import java.util.List;

public class DaiCardUnion implements CardUnion {

    private BaseCardUnion mainType;
    private BaseCardUnion withType;
    private int withTypeCnt;

    public DaiCardUnion(BaseCardUnion mainType, BaseCardUnion withType,
                        int withTypeCnt) {
        //三带一 和 四带二
        if ((mainType.getCount() == 4 && withTypeCnt == 2) ||
                (mainType.getCount() == 3 && withTypeCnt == 1)){
            this.mainType = mainType;
            this.withType = withType;
            this.withTypeCnt = withTypeCnt;
        } else {
            throw new IllegalArgumentException(mainType.getType() + "_" + withTypeCnt);
        }
    }

    @Override
    public int getValue() {
        return mainType.getValue();
    }

    @Override
    public String getType(){
        StringBuilder stringBuffer = new StringBuilder(mainType.getType());
        for (int i=0;i<withTypeCnt;i++){
//            stringBuffer.append("_").append(withType.getType());
            stringBuffer.append("_").append("B");
        }
        return stringBuffer.toString();
    }

    @Override
    public List<DdzCard> generateCardList() {
        List<DdzCard> cardList = new ArrayList<>(20);
        cardList.addAll(mainType.generateCardList());
        for (int i=0; i<withTypeCnt; i++){
            cardList.addAll(withType.generateCardList());
        }
        return cardList;
    }


    @Override
    public String toString() {
        return "" + mainType +
                "_" + withType +
                "_" + withTypeCnt;
    }

    public enum DaiEnum {
        SAN_DAI_YI(3,1,1),
        SAN_DAI_DUI(3,2,1),
        SI_DAI_YI(4,1,2);

        DaiEnum(int mainTypeCount, int withTypeCount, int withTypeCnt) {
            this.mainTypeCount = mainTypeCount;
            this.withTypeCount = withTypeCount;
            this.withTypeCnt = withTypeCnt;
        }

        private int mainTypeCount;
        private int withTypeCount;
        private int withTypeCnt;

        public int getMainTypeCount() {
            return mainTypeCount;
        }

        public int getWithTypeCount() {
            return withTypeCount;
        }

        public int getWithTypeCnt() {
            return withTypeCnt;
        }
    }
}
