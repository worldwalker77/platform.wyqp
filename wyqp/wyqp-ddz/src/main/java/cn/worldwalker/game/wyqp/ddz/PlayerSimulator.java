package cn.worldwalker.game.wyqp.ddz;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.card.union.CardUnion;
import cn.worldwalker.game.wyqp.ddz.common.DdzPlayerInfo;
import cn.worldwalker.game.wyqp.ddz.common.DdzRoomInfo;
import cn.worldwalker.game.wyqp.ddz.common.GameStatusEnum;
import cn.worldwalker.game.wyqp.ddz.handler.TypeHandlerService;
import cn.worldwalker.game.wyqp.ddz.service.CardService;
import cn.worldwalker.game.wyqp.ddz.service.PlayerService;

import java.util.List;
import java.util.Random;

public class PlayerSimulator implements Runnable{
    private PlayerService playerService = PlayerService.getInstance();
    private CardService cardService = CardService.getInstance();
    private TypeHandlerService typeHandlerService = TypeHandlerService.getInstance();
    private DdzPlayerInfo playerInfo;
    private DdzRoomInfo roomInfo;
    private Integer nextPlayerId;
    private Random random = new Random();

    public PlayerSimulator(DdzPlayerInfo playerInfo, DdzRoomInfo roomInfo,
                           Integer nextPlayerId ) {
        this.playerInfo = playerInfo;
        this.roomInfo = roomInfo;
        this.nextPlayerId = nextPlayerId;
    }

    @Override
    public void run() {
        while (GameStatusEnum.PLAY.equals(roomInfo.getGameStatusEnum())){
            if (playerInfo.getPlayerId().equals(roomInfo.getCurPlayerId())) {
                System.out.println("");
                System.out.println(playerInfo.getPlayerId() + "'s turn");

                List<CardUnion> cardUnions = cardService.getUionList(playerInfo.getDdzCardList());
                List<DdzCard> playCardList = null ;
                if (roomInfo.getLastCards().isEmpty() ||
                        roomInfo.getLastCardsOwner().equals(playerInfo.getPlayerId())){
                    playCardList = cardUnions.get(random.nextInt(cardUnions.size())).generateCardList();
                }else {
                    int x =0;
                    CardUnion curCardUnion = typeHandlerService.getCardType(roomInfo.getLastCards());
                    for (CardUnion cardUnion: cardUnions){
                        if (cardUnion.getType().equals(curCardUnion.getType())
                                && cardUnion.getValue() > curCardUnion.getValue()){
                            playCardList = cardUnion.generateCardList();
                            break;
                        }
                    }
                }

                if (playCardList != null){
                    playerService.playCard(playerInfo, playCardList);
                    roomInfo.setLastCards(playCardList);
                    roomInfo.setLastCardsOwner(playerInfo.getPlayerId());
                    System.out.println("play：" + playCardList + ", rest:"
                            +  playerInfo.getDdzCardList());
                    if (playerInfo.getDdzCardList().isEmpty()){
                        System.out.println("Over, winner is " + playerInfo.getPlayerId());
                        roomInfo.setGameStatusEnum(GameStatusEnum.OVER);
                    }
                } else {
                    System.out.println("player " + playerInfo.getPlayerId() + " pass");
                }
                roomInfo.setCurPlayerId(nextPlayerId);
            } else {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String toString() {
                return "" + playerInfo +
//                ", " + roomInfo +
                ", " + nextPlayerId ;
    }
}



