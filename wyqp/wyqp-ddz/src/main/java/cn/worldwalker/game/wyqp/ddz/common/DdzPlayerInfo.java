package cn.worldwalker.game.wyqp.ddz.common;

import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;
import cn.worldwalker.game.wyqp.ddz.card.DdzCard;

import java.util.ArrayList;
import java.util.List;

public class DdzPlayerInfo extends BasePlayerInfo{

    private Integer role;
    private List<DdzCard> ddzCardList = new ArrayList<>(20);

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public List<DdzCard> getDdzCardList() {
        return ddzCardList;
    }

    public void setDdzCardList(List<DdzCard> ddzCardList) {
        this.ddzCardList = ddzCardList;
    }

    @Override
    public String toString() {
        return "" + getPlayerId() +
                ", " + ddzCardList ;
    }
}

