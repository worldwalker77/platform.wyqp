package cn.worldwalker.game.wyqp.ddz.common;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRequest;

public class DdzRequest  extends BaseRequest{
    private DdzMsg ddzMsg;


    public DdzMsg getDdzMsg() {
        return ddzMsg;
    }

    public void setDdzMsg(DdzMsg ddzMsg) {
        this.ddzMsg = ddzMsg;
    }
}
