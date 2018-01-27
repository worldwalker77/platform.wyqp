package cn.worldwalker.game.wyqp.ddz.common;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;
import cn.worldwalker.game.wyqp.ddz.card.DdzCard;

import java.util.ArrayList;
import java.util.List;

public class DdzRoomInfo extends BaseRoomInfo{

    private List<DdzPlayerInfo> playerList = new ArrayList<>(3);
    private GameStatusEnum gameStatusEnum = GameStatusEnum.WAIT;
    private Integer curLord = 0;
    private Integer curPlayer = 0;
    private List<DdzCard> curCards = new ArrayList<>(20);
    private Integer curCardsOwner = 0;


    @Override
    public List<DdzPlayerInfo> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<DdzPlayerInfo> playerList) {
        this.playerList = playerList;
    }

    public GameStatusEnum getGameStatusEnum() {
        return gameStatusEnum;
    }

    public void setGameStatusEnum(GameStatusEnum gameStatusEnum) {
        this.gameStatusEnum = gameStatusEnum;
    }

    public Integer getCurLord() {
        return curLord;
    }

    public void setCurLord(Integer curLord) {
        this.curLord = curLord;
    }

    public Integer getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(Integer curPlayer) {
        this.curPlayer = curPlayer;
    }

    public List<DdzCard> getCurCards() {
        return curCards;
    }

    public void setCurCards(List<DdzCard> curCards) {
        this.curCards = curCards;
    }

    public Integer getCurCardsOwner() {
        return curCardsOwner;
    }

    public void setCurCardsOwner(Integer curCardsOwner) {
        this.curCardsOwner = curCardsOwner;
    }


    @Override
    public String toString() {
        return "" +
                ", " + gameStatusEnum +
                ", lord=" + curLord +
                ", curPlayer=" + curPlayer +
                ", curCards=" + curCards +
                ", curCardOwner=" + curCardsOwner ;
    }
}