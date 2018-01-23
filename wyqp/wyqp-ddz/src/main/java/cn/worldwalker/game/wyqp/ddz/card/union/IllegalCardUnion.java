package cn.worldwalker.game.wyqp.ddz.card.union;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;

import java.util.List;

public class IllegalCardUnion implements CardUnion {
    private static IllegalCardUnion ourInstance = new IllegalCardUnion();

    public static IllegalCardUnion getInstance() {
        return ourInstance;
    }

    private IllegalCardUnion() {
    }

    @Override
    public int getValue() {
        return -1;
    }

    @Override
    public String getType() {
        return "~!@#$%^&*()";
    }

    @Override
    public List<DdzCard> generateCardList() {
        return null;
    }
}
