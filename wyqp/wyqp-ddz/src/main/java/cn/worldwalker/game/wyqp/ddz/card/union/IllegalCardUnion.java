package cn.worldwalker.game.wyqp.ddz.card.union;

import java.util.List;

public class IllegalCardUnion implements CardUnion {
    private static IllegalCardUnion ourInstance = new IllegalCardUnion();

    public static IllegalCardUnion getInstance() {
        return ourInstance;
    }

    private IllegalCardUnion() {
    }

    @Override
    public Integer getValue() {
        return -1;
    }

    @Override
    public String getType() {
        return "~!@#$%^&*()";
    }

    @Override
    public List<Integer> getCardList() {
        return null;
    }

    @Override
    public boolean isBomb() {
        return false;
    }
}
