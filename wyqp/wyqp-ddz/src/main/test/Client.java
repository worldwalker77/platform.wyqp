import cn.worldwalker.game.wyqp.common.utils.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.java_websocket.WebSocket;

import java.net.URI;
import java.util.List;

public class Client {
    private Socket socket;
    private String token;
    private Integer roomId;
    private Integer playerId;
    private List<Integer> cardList;
    private List<Integer> cueCardList;

    private Boolean gameOver;

    public void init() throws Exception {
        socket = new Socket(new URI("ws://localhost:10007/websocket"),this);
        socket.connect();
        int i=0;
        while (!socket.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            System.out.println("等待连接:" + i++);
            Thread.sleep(1000);
        }
        System.out.println("连接成功");
        gameOver = false;
        generateToken();
    }

    public void entryHall(){
        Request request = new Request();
        request.setGameType(0);
        request.setMsgType(1);
        request.setToken(token);
        socket.sendMsg(JSON.toJSONString(request));
    }

    public void createRoom(){
        Request request = new Request();
        request.setMsgType(5);
        request.setToken(token);
        JSONObject jsonMsg = new JSONObject(3);
        jsonMsg.put("payType",1);
        jsonMsg.put("totalGames",10);
        jsonMsg.put("gameType",4);
        request.setMsg(jsonMsg);
        socket.sendMsg(JSON.toJSONString(request));
    }

    public void playerIn(int roomId ){
        Request request = new Request();
        request.setMsgType(6);
        request.setToken(token);
        JSONObject jsonMsg = new JSONObject(3);
        jsonMsg.put("roomId",roomId);
        request.setMsg(jsonMsg);
        socket.sendMsg(JSON.toJSONString(request));
    }

    public void playerReady(){
        Request request = new Request();
        request.setMsgType(7);
        request.setToken(token);
        socket.sendMsg(JSON.toJSONString(request));
    }

    public void callLandlord(int score){
        Request request = new Request();
        request.setMsgType(401);
        request.setToken(token);
        JSONObject jsonDdzMsg = new JSONObject(1);
        jsonDdzMsg.put("score",score);
        request.setDdzMsg(JSON.toJSONString(jsonDdzMsg));
        socket.sendMsg(JSON.toJSONString(request));
    }

    public void cue(){
        Request request = new Request();
        request.setMsgType(406);
        request.setToken(token);
        socket.sendMsg(JSON.toJSONString(request));
    }

    public void playerCard(){
        Request request = new Request();
        request.setMsgType(404);
        request.setToken(token);
        JSONObject jsonDdzMsg = new JSONObject(3);
        jsonDdzMsg.put("playCards",cueCardList);
        request.setDdzMsg(JSON.toJSONString(jsonDdzMsg));
        socket.sendMsg(JSON.toJSONString(request));
    }

    private void generateToken() throws Exception {
        String ret = HttpClientUtils.get("http://localhost:8080/game/login");
        JSONObject jsonObject = JSON.parseObject(ret);
        token = jsonObject.getJSONObject("data").getString("token");
        playerId = jsonObject.getJSONObject("data").getInteger("playerId");
    }

    public String getToken() {
        return token;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public List<Integer> getCardList() {
        return cardList;
    }

    public void setCardList(List<Integer> cardList) {
        this.cardList = cardList;
    }

    public List<Integer> getCueCardList() {
        return cueCardList;
    }

    public void setCueCardList(List<Integer> cueCardList) {
        this.cueCardList = cueCardList;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }
}
