package cn.worldwalker.game.wyqp.ddz.card.union;

import cn.worldwalker.game.wyqp.ddz.service.CardService;

import java.util.List;

public class BaseCardUnion implements CardUnion,Comparable<BaseCardUnion>{

    private List<Integer> cardList;

    public BaseCardUnion(List<Integer> cardList){
        this.cardList = cardList;
    }

    @Override
    public Integer getValue() {
        return CardService.getInstance().cardValue(cardList.get(0));
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
    public int compareTo(BaseCardUnion o) {
        return this.cardList.size() == o.cardList.size()
                ? (this.getValue() - o.getValue())
                : (this.cardList.size() - o.cardList.size());
    }

    @Override
    public String toString() {
        return  cardList.size()+ "个"  + getValue();
    }

}
