import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Socket extends WebSocketClient implements Runnable{

    private Client client;

    public Socket(URI serverUri, Client client) {
        super(serverUri);
        this.client = client;
    }

    public void sendMsg(String msg){
//        System.out.println(client.getToken().substring(0,4) + "--发送消息: " + msg);
        send(msg);
    }

    public List<Integer> cardValue(List<Integer> cardList){
        List<Integer> valueList = new ArrayList<>(cardList.size());
        for (Integer index : cardList){
            valueList.add(index>>2);
        }
        return valueList;
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        System.out.println("打开链接");
    }

    @Override
    public void onMessage(String arg0) {
        JSONObject jsonObject = JSON.parseObject(arg0);
        Integer msgType = jsonObject.getInteger("msgType");
        if (msgType == 5) {
            Integer roomId = jsonObject.getJSONObject("data").getInteger("roomId");
            client.setRoomId(roomId);
        } else if (msgType == 8){
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("cardList");
            List<Integer> cardList = new ArrayList<>(jsonArray.size());
            for (Object o: jsonArray) {
                cardList.add((Integer) o );
            }
            client.setCardList(cardList);
            System.out.println(client.getPlayerId() + ":allCardList:" + cardValue(cardList));
        } else if (msgType == 403){
            Integer  landlordId = jsonObject.getJSONObject("data").getInteger("landlord");
            if (landlordId.equals(client.getPlayerId())){
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("restCardList");
                List<Integer> cardList = client.getCardList();
                for (Object o: jsonArray) {
                    cardList.add((Integer) o);
                }
                System.out.println(client.getPlayerId() + ":allCardList:" + cardValue(cardList));
            }

        } else if (msgType == 406){
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("cueCardList");
            if (jsonArray == null || jsonArray.isEmpty()){
                System.out.println(client.getPlayerId() + ":pass:");
                client.setCueCardList(Collections.<Integer>emptyList());
            } else {
                List<Integer> cardList = new ArrayList<>(jsonArray.size());
                for (Object o: jsonArray) {
                    cardList.add((Integer) o );
                }
                client.setCueCardList(cardList);
                System.out.println(client.getPlayerId() + ":cueCardList:" + cardValue(cardList)
                + "  index:" + cardList);
            }
        } else if (msgType == 24){
            client.setGameOver(true);
            System.out.println(jsonObject);
        }
//        System.out.println(client.getPlayerId() + "--收到消息: " + arg0);
    }

    @Override
    public void onError(Exception arg0) {
        arg0.printStackTrace();
        System.out.println("发生错误已关闭");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        System.out.println("链接已关闭");
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            System.out.println(new String(bytes.array(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
