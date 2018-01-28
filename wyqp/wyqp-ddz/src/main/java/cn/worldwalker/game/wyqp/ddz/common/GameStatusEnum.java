package cn.worldwalker.game.wyqp.ddz.common;

public enum GameStatusEnum {
    WAIT("等待阶段"),
    CALL("叫地主阶段"),
    PLAY("出牌阶段"),
    OVER("结束了");

    private GameStatusEnum(String desc) {
        this.desc = desc;
    }

    private String desc;
}
