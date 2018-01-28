import com.alibaba.fastjson.JSONObject;

public class Request {
    private Integer msgType;
    private String token;
    private Integer gameType = 4;
    private String ddzMsg;
    private JSONObject msg;

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public String getDdzMsg() {
        return ddzMsg;
    }

    public void setDdzMsg(String ddzMsg) {
        this.ddzMsg = ddzMsg;
    }

    public JSONObject getMsg() {
        return msg;
    }

    public void setMsg(JSONObject msg) {
        this.msg = msg;
    }
}
