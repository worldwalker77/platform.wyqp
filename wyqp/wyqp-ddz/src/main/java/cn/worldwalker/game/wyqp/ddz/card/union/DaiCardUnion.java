package cn.worldwalker.game.wyqp.ddz.card.union;

import java.util.ArrayList;
import java.util.List;

public class DaiCardUnion implements CardUnion {

    private BaseCardUnion mainType;
    private List<BaseCardUnion> withTypeList;

    public DaiCardUnion(BaseCardUnion mainType, List<BaseCardUnion> withTypeList){
            this.mainType = mainType;
            this.withTypeList = withTypeList;
    }

    @Override
    public Integer getValue() {
        return mainType.getValue();
    }

    @Override
    public String getType(){
        return mainType.getType() + " 带" + withTypeList.size() + "个" + withTypeList.get(0).getType();
    }

    @Override
    public List<Integer> getCardList() {
        List<Integer> cardList = new ArrayList<>(20);
        cardList.addAll(mainType.getCardList());
        for (BaseCardUnion baseCardUnion : withTypeList){
            cardList.addAll(baseCardUnion.getCardList());
        }
        return cardList;
    }


    @Override
    public String toString() {
        return mainType.toString() + " 带" + withTypeList.toString();
    }

    public enum DaiEnum {
        SAN_DAI_YI(3,1,1),
        SAN_DAI_DUI(3,2,1),
        SI_DAI_YI(4,1,2),
        SI_DAI_DUI(4,2,2);

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
