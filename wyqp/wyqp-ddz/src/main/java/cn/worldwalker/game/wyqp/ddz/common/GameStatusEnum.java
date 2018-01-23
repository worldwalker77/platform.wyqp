package cn.worldwalker.game.wyqp.ddz.common;

public enum GameStatusEnum {
    WAIT("等待"),
    RUN("进行"),
    OVER("结束");

    private GameStatusEnum(String desc) {
        this.desc = desc;
    }

    private String desc;
}
