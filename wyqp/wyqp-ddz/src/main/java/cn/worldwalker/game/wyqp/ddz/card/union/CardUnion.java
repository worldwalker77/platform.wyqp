package cn.worldwalker.game.wyqp.ddz.card.union;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;

import java.util.List;

/**
 * 组合牌，斗地主出牌，都是组合牌形式，比如顺子，三带一
 */
public interface CardUnion {
    int getValue();
    String getType();
    List<DdzCard> generateCardList();
}