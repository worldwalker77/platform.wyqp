package cn.worldwalker.game.wyqp.ddz.common;

import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;

import java.util.ArrayList;
import java.util.List;

public class DdzPlayerInfo extends BasePlayerInfo{

    private Integer role;
    private List<Integer> ddzCardList= new ArrayList<>(20);

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public List<Integer> getDdzCardList() {
        return ddzCardList;
    }

    public void setDdzCardList(List<Integer> ddzCardList) {
        this.ddzCardList = ddzCardList;
    }

    @Override
    public String toString() {
        return "" + getPlayerId() +
                ", " + ddzCardList ;
    }
}

