package cn.worldwalker.game.wyqp.ddz.common;

import cn.worldwalker.game.wyqp.common.domain.base.BaseRoomInfo;

import java.util.ArrayList;
import java.util.List;

public class DdzRoomInfo extends BaseRoomInfo{

    private List<DdzPlayerInfo> playerList = new ArrayList<>(3);

    private GameStatusEnum gameStatusEnum = GameStatusEnum.WAIT;

    //地主相关
    private Integer landlord = 0;
    private Integer score = 0;
    private Integer lastCaller = 0;
    private Integer lastCallScore = 0;
    private Integer bombCnt = 0;

    //底牌
    private List<Integer> restCardList = new ArrayList<>(3);


    //出牌相关
    private List<Integer> lastCards = new ArrayList<>(20);
    private Integer lastCardsOwner = 0;


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

    public Integer getLandlord() {
        return landlord;
    }

    public void setLandlord(Integer landlord) {
        this.landlord = landlord;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getLastCaller() {
        return lastCaller;
    }

    public void setLastCaller(Integer lastCaller) {
        this.lastCaller = lastCaller;
    }

    public Integer getLastCallScore() {
        return lastCallScore;
    }

    public void setLastCallScore(Integer lastCallScore) {
        this.lastCallScore = lastCallScore;
    }

    public List<Integer> getLastCards() {
        return lastCards;
    }

    public void setLastCards(List<Integer> lastCards) {
        this.lastCards = lastCards;
    }

    public Integer getLastCardsOwner() {
        return lastCardsOwner;
    }

    public void setLastCardsOwner(Integer lastCardsOwner) {
        this.lastCardsOwner = lastCardsOwner;
    }

    public List<Integer> getRestCardList() {
        return restCardList;
    }

    public void setRestCardList(List<Integer> restCardList) {
        this.restCardList = restCardList;
    }

    public Integer getBombCnt() {
        return bombCnt;
    }

    public void setBombCnt(Integer bombCnt) {
        this.bombCnt = bombCnt;
    }
}