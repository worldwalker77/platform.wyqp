package cn.worldwalker.game.wyqp.ddz.card.union;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;

import java.util.ArrayList;
import java.util.List;

public class BaseCardUnion implements CardUnion,Comparable<BaseCardUnion>{
    private static int MAX = 15;
    private static int MIN = 2;

    private int value;
    private int count;

    public BaseCardUnion(int value, int count) {
        if (value >= BaseCardUnion.MIN && value<= BaseCardUnion.MAX){
            this.value = value;
            this.count = count;
        } else {
            throw new IllegalArgumentException(value + "");
        }
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getType() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<count; i++){
            stringBuilder.append("A");
        }
        return stringBuilder.toString();
    }

    @Override
    public List<DdzCard> generateCardList() {
        List<DdzCard> ddzCardList = new ArrayList<>(4);
        for (int i = 0; i<count; i++){
            ddzCardList.add(new DdzCard(value));
        }
        return ddzCardList;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(BaseCardUnion o) {
        return this.count == o.count ? (this.value - o.value) : (this.count - o.count);
    }

    @Override
    public String toString() {
        return value + "*"  + count;
    }
}
