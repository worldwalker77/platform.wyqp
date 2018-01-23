package cn.worldwalker.game.wyqp.ddz.card.union;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;

import java.util.ArrayList;
import java.util.List;

public class ShunCardUnion implements CardUnion {

    private BaseCardUnion baseType;
    private int size;

    public ShunCardUnion(BaseCardUnion baseType, int size) {
        if ( (baseType.getCount() == 1 && size >= 5) ||
                (baseType.getCount() == 2 && size >= 3 ) ||
                (baseType.getCount() == 3 && size >= 2 )){
            this.baseType = baseType;
            this.size = size;
        }else {
            throw new IllegalArgumentException(baseType.getType() + "->" + size);
        }
    }

    @Override
    public int getValue() {
        return baseType.getValue();
    }

    @Override
    public String getType() {
        return baseType.getType() + "->" + size ;
    }

    @Override
    public List<DdzCard> generateCardList() {
        List<DdzCard> cardList = new ArrayList<>(20);
        for (int i=0; i<size; i++){
            cardList.addAll( new BaseCardUnion(baseType.getValue()+i,
                    baseType.getCount()).generateCardList());
        }
        return cardList;
    }

    @Override
    public String toString() {
        return "" + baseType + "->" + size;
    }

    public BaseCardUnion getBaseType() {
        return baseType;
    }

    public int getSize() {
        return size;
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
