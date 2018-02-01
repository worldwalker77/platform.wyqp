package cn.worldwalker.game.wyqp.ddz.card.union;

import java.util.ArrayList;
import java.util.List;

public class ShunCardUnion implements CardUnion {

    private List<BaseCardUnion> baseCardUnionList;

    public ShunCardUnion(List<BaseCardUnion> baseCardUnionList) {
        this.baseCardUnionList = baseCardUnionList;
    }

    public List<BaseCardUnion> getBaseCardUnionList() {
        return baseCardUnionList;
    }

    @Override
    public Integer getValue() {
        return baseCardUnionList.get(0).getValue();
    }

    @Override
    public String getType() {
        StringBuilder sb = new StringBuilder(baseCardUnionList.size());
        for (BaseCardUnion baseCardUnion :baseCardUnionList){
            sb.append(baseCardUnion.getType());
        }
        return sb.toString();
    }

    @Override
    public List<Integer> getCardList() {
        List<Integer> cardList = new ArrayList<>(20);
        for (BaseCardUnion baseCardUnion :baseCardUnionList){
            cardList.addAll(baseCardUnion.getCardList());
        }
        return cardList;
    }

    @Override
    public boolean isBomb() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(baseCardUnionList.size());
        for (BaseCardUnion baseCardUnion :baseCardUnionList){
            sb.append(baseCardUnion).append(" ");
        }
        return sb.toString();
    }

    public enum ShunEnum {
        DAN_SHUN(1,5),
        DUI_SHUN(2,3),
        SAN_SHUN(3,2);
        ShunEnum(int basicCardCount, int minLen) {
            this.basicCardCount = basicCardCount;
            this.minLen = minLen;
        }
        int basicCardCount;
        int minLen;

        public int getBasicCardCount() {
            return basicCardCount;
        }

        public int getMinLen() {
            return minLen;
        }
    }
}
