package cn.worldwalker.game.wyqp.ddz.card.union;

import cn.worldwalker.game.wyqp.ddz.constant.DdzConstant;
import cn.worldwalker.game.wyqp.ddz.service.CardService;

import java.util.*;

public class BaseCardUnion implements CardUnion, Comparable<BaseCardUnion> {

    private List<Integer> cardList;
    private Integer value;

    private BaseCardUnion(List<Integer> cardList) {
        this.cardList = cardList;
        value = CardService.getInstance().cardValue(cardList.get(0));
    }

    public static BaseCardUnion valueOf(List<Integer> cardList){
        return BaseUnionCache.cardUnionMap.get(cardList.toString());
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getType() {
        return cardList.size() + "个";
    }

    @Override
    public List<Integer> getCardList() {
        return cardList;
    }

    @Override
    public boolean isBomb() {
        return cardList.size() == 4 || (cardList.size() == 2 && value == DdzConstant.KING_VALUE);
    }

    @Override
    public int compareTo(BaseCardUnion o) {
        return this.cardList.size() == o.cardList.size()
                ? (this.getValue() - o.getValue())
                : (this.cardList.size() - o.cardList.size());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < cardList.size(); i++) {
            sb.append(this.value).append("-");
        }
        return sb.toString();
    }


    private static class BaseUnionCache {
        private final static Map<String, BaseCardUnion> cardUnionMap = new LinkedHashMap<>(256);

        static {
            for (int i=0; i<14; i++) {
                List<Integer> list = new ArrayList<>(4);
                for (int j = 0; j<4; j++) {
                    int index = (i << 2) + j;
                    if (index < 54)
                        list.add(index);
                }
                List<List<Integer>> unionList = generateComb(list);
                for (List<Integer> union : unionList) {
                    cardUnionMap.put(union.toString(), new BaseCardUnion(union));
                }
            }
        }

        private static List<List<Integer>> generateComb(List<Integer> list) {
            int len = list.size();
            int nBits = 1 << len;
            List<List<Integer>> retList = new ArrayList<>(256);
            for (int i = 0; i < nBits; ++i) {
                int t;
                List<Integer> list1 = new ArrayList<>(4);
                for (int j = 0; j < len; j++) {
                    t = 1 << j;
                    if ((t & i) != 0) { // 与运算，同为1时才会是1
                        list1.add(list.get(j));
                    }
                }
                if (list1.size() > 0) {
                    retList.add(list1);
                }
            }
            return retList;
        }

    }

//    public static void main(String[] args){
//        System.out.println(BaseUnionCache.cardUnionMap.size());
//        for (Map.Entry<String, BaseCardUnion> entry : BaseUnionCache.cardUnionMap.entrySet()){
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//
//        }
//    }
}
