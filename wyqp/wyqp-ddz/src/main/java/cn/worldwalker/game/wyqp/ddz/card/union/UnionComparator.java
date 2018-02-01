package cn.worldwalker.game.wyqp.ddz.card.union;

import java.util.Comparator;

public class UnionComparator {

    /**
     * 按照牌数逆序排；主要用于出牌提示
     */
    public final static Comparator<CardUnion> BY_SIZE = new Comparator<CardUnion>() {
        @Override
        public int compare(CardUnion o1, CardUnion o2) {
            return o2.getCardList().size() - o1.getCardList().size();
        }
    };

    /**
     * 按值大小顺序排; 主要用于大过对方的出牌提示
     */
    public final static Comparator<CardUnion> BY_VALUE = new Comparator<CardUnion>() {
        @Override
        public int compare(CardUnion o1, CardUnion o2) {
            return o1.getValue() - o2.getValue();
        }
    };





}

