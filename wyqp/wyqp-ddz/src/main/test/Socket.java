import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Socket extends WebSocketClient implements Runnable{

    private Client client;

    public Socket(URI serverUri, Client client) {
        super(serverUri);
        this.client = client;
    }

    public void sendMsg(String msg){
        System.out.println(client.getToken() + "--发送消息: " + msg);
        send(msg);
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
                cardList.add(((JSONObject)o).getInteger("value"));
            }
            client.setCardList(cardList);
            System.out.println("allCardList:" + cardList);
        } else if (msgType == 406){
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("cueCardList");
            List<Integer> cardList = new ArrayList<>(jsonArray.size());
            for (Object o: jsonArray) {
                cardList.add(((JSONObject)o).getInteger("value"));
            }
            client.setCueCardList(cardList);
            System.out.println("curCardList:" + cardList);
        }
        System.out.println(client.getToken() + "--收到消息: " + arg0);
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
