package cn.worldwalker.game.wyqp.ddz.common;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;

public class DdzRequest  extends BaseRequest{

    private DdzMsg msg = new DdzMsg();

    public DdzMsg getDdzMsg() {
        return msg;
    }

    public void setDdzMsg(DdzMsg msg) {
        this.msg = msg;
    }
}

