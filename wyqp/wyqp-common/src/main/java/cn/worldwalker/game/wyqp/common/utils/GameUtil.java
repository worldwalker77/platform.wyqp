package cn.worldwalker.game.wyqp.common.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import cn.worldwalker.game.wyqp.common.domain.base.BasePlayerInfo;
import cn.worldwalker.game.wyqp.common.enums.DissolveStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.OnlineStatusEnum;
import cn.worldwalker.game.wyqp.common.enums.PlayerStatusEnum;

public class GameUtil {
	
	public static int genRoomId(){
		int max=999999;
        int min=100000;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;	
		return s;
	}
	public static int genTeaHouseNum(){
		int max=999999;
        int min=100000;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;	
		return s;
	}
	
	public static Integer genPlayerId(){
		int max=999999;
		int min=100000;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}
	public static String genToken(Integer playerId){
		String temp = playerId + System.currentTimeMillis() + Thread.currentThread().getName();
		return MD5Util1.encryptByMD5(temp);
	}
	
	public static Integer[] getPlayerIdArr(List playerList){
		int size = playerList.size();
		Integer[] arr = new Integer[size];
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			arr[i] = playerInfo.getPlayerId();
		}
		return arr;
	}
	
	public static String[] getPlayerIdStrArr(List playerList){
		int size = playerList.size();
		String[] arr = new String[size];
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			arr[i] = String.valueOf(playerInfo.getPlayerId());
		}
		return arr;
	}
	
	public static Integer[] getPlayerIdArrWithOutSelf(List playerList, Integer playerId){
		int size = playerList.size();
		List<Integer> playerIdList = new ArrayList<Integer>();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (!playerId.equals(playerInfo.getPlayerId())) {
				playerIdList.add(playerInfo.getPlayerId());
			}
		}
		Integer[] arr = new Integer[playerIdList.size()];
		playerIdList.toArray(arr);
		return arr;
	}
	
	public static Integer[] getPlayerIdArrWithOutRoomBanker(List playerList, Integer roomBankerId){
		int size = playerList.size();
		List<Integer> playerIdList = new ArrayList<Integer>();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (!roomBankerId.equals(playerInfo.getPlayerId())) {
				playerIdList.add(playerInfo.getPlayerId());
			}
		}
		Integer[] arr = new Integer[playerIdList.size()];
		playerIdList.toArray(arr);
		return arr;
	}
	
	public static boolean isExistPlayerInRoom(Integer playerId, List playerList){
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (playerInfo.getPlayerId().equals(playerId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 设置玩家解散状态
	 * @param playerList
	 * @param playerId
	 * @param statusEnum
	 */
	public static void setDissolveStatus(List playerList, Integer playerId, DissolveStatusEnum statusEnum){
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (playerInfo.getPlayerId().equals(playerId)) {
				playerInfo.setDissolveStatus(statusEnum.status);
			}
		}
	}
	
	/**
	 * 设置玩家在线状态
	 * @param playerList
	 * @param playerId
	 * @param statusEnum
	 */
	public static void setOnlineStatus(List playerList, Integer playerId, OnlineStatusEnum onlineStatusEnum){
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (playerInfo.getPlayerId().equals(playerId)) {
				playerInfo.setOnlineStatus(onlineStatusEnum.status);
			}
		}
	}
	/**
	 * 设置玩家状态
	 * @param playerList
	 * @param playerId
	 * @param status
	 */
	public static void setPlayerStatus(List playerList, Integer playerId,  Integer status){
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (playerInfo.getPlayerId().equals(playerId)) {
				playerInfo.setStatus(status);
			}
		}
	}
	
	public static Integer getPlayerStatus(List playerList, Integer playerId){
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (playerInfo.getPlayerId().equals(playerId)) {
				return playerInfo.getStatus();
			}
		}
		return null;
	}
	
	public static Integer getPlayedCountByPlayerId(List playerList, Integer playerId){
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (playerInfo.getPlayerId().equals(playerId)) {
				return playerInfo.getPlayedCount();
			}
		}
		return null;
	}
	
	public static void removePlayer(List playerList, Integer playerId){
		int size = playerList.size();
		int needRemoveIndex = 0;
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (playerInfo.getPlayerId().equals(playerId)) {
				needRemoveIndex = i;
				break;
			}
		}
		playerList.remove(needRemoveIndex);
	}
	/**
	   * 计算两点之间距离
	   * @param start
	   * @param end
	   * @return String  多少m ,  多少km
	   */
	public static String getLatLngDistance(BasePlayerInfo curPlayer, BasePlayerInfo otherPlayer){
	   try {
		String startXStr = curPlayer.getX();
		   String startYStr = curPlayer.getY();
		   String endXStr = otherPlayer.getX();
		   String endYStr = otherPlayer.getY();
		   if (null == curPlayer || null == otherPlayer 
			   || StringUtils.isBlank(curPlayer.getX()) || StringUtils.isBlank(curPlayer.getX())
			   || StringUtils.isBlank(otherPlayer.getX()) || StringUtils.isBlank(otherPlayer.getX())) {
			   return null;
		   }
		   if ("0.0".equals(startXStr) && "0.0".equals(startYStr) || "0.0".equals(endXStr) && "0.0".equals(endYStr)) {
			   return null;
		   }
		   double lat1 = (Math.PI/180)*Double.valueOf(startXStr);
		   double lat2 = (Math.PI/180)*Double.valueOf(endXStr);
		   
		   double lon1 = (Math.PI/180)*Double.valueOf(startYStr);
		   double lon2 = (Math.PI/180)*Double.valueOf(endYStr);
		   
		   //地球半径
		   double R = 6371.004;
		   
		   //两点间距离 m，如果想要米的话，结果*1000就可以了
		   double dis =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;
		   NumberFormat nFormat = NumberFormat.getNumberInstance();  //数字格式化对象
		   if(dis < 1){               //当小于1千米的时候用,用米做单位保留一位小数
		    
		    nFormat.setMaximumFractionDigits(1);    //已可以设置为0，这样跟百度地图APP中计算的一样 
		    dis *= 1000;
		    
		    return nFormat.format(dis)+"m";
		   }else{
		    nFormat.setMaximumFractionDigits(2);
		    return nFormat.format(dis)+"km";
		   }
	} catch (NumberFormatException e) {
		e.printStackTrace();
		return null;
	}

	 }
	
	
	public static Integer getNextOperatePlayerIdByRoomBankerId(List playerList, Integer roomBankerId){
		
		int size = playerList.size();
		for(int i = 0; i < size; i++ ){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			if (player.getPlayerId().equals(roomBankerId)) {
				if (i == size - 1) {
					BasePlayerInfo tempPlayer = (BasePlayerInfo)playerList.get(0);
					
					if (tempPlayer.getStatus() < PlayerStatusEnum.ready.status) {
						return getNextOperatePlayerIdByRoomBankerId(playerList, tempPlayer.getPlayerId());
					}else{
						return tempPlayer.getPlayerId();
					}
				}else{
					BasePlayerInfo tempPlayer = (BasePlayerInfo)playerList.get(i + 1);
					if (tempPlayer.getStatus() < PlayerStatusEnum.ready.status) {
						return getNextOperatePlayerIdByRoomBankerId(playerList, tempPlayer.getPlayerId());
					}else{
						return tempPlayer.getPlayerId();
					}
				}
			}
		}
		return null;
	}
	
	public static Integer getNextOperatePlayerId(List playerList, Integer curPlayerId){
		
		List alivePlayerList = getAlivePlayerList(playerList);
		int size = alivePlayerList.size();
		Integer nextOperatePlayerId = null;
		for(int i = 0; i < size; i++ ){
			BasePlayerInfo player = (BasePlayerInfo)alivePlayerList.get(i);
			if (player.getPlayerId().equals(curPlayerId)) {
				if (i == size - 1) {
					nextOperatePlayerId = ((BasePlayerInfo)alivePlayerList.get(0)).getPlayerId();
					break;
				}else{
					nextOperatePlayerId = ((BasePlayerInfo)alivePlayerList.get(i + 1)).getPlayerId();
					break;
				}
			}
		}
		return nextOperatePlayerId;
	}
	
	public static List getAlivePlayerList(List playerList){
		List alivePlayerList = new ArrayList<>();
		int size = playerList.size();
		for(int  i = 0; i < size; i++){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			/**金花特有，3：未看牌，4：已看牌*/
			if (player.getStatus().equals(3) || player.getStatus().equals(4)) {
				alivePlayerList.add(player);
			}
		}
		return alivePlayerList;
	}
	
	public static Integer getNextPlayerId(List playerList, Integer curPlayerId){
		
		int size = playerList.size();
		Integer nextOperatePlayerId = null;
		for(int i = 0; i < size; i++ ){
			BasePlayerInfo player = (BasePlayerInfo)playerList.get(i);
			if (player.getPlayerId().equals(curPlayerId)) {
				if (i == size - 1) {
					nextOperatePlayerId = ((BasePlayerInfo)playerList.get(0)).getPlayerId();
					break;
				}else{
					nextOperatePlayerId = ((BasePlayerInfo)playerList.get(i + 1)).getPlayerId();
					break;
				}
			}
		}
		return nextOperatePlayerId;
	}
	public static BasePlayerInfo getPlayerByPlayerId(List playerList, Integer playerId){
		int size = playerList.size();
		for(int i = 0; i < size; i++){
			BasePlayerInfo playerInfo = (BasePlayerInfo)playerList.get(i);
			if (playerInfo.getPlayerId().equals(playerId)) {
				return playerInfo;
			}
		}
		return null;
	}
	
	public static String emojiFilter (String str) {  
        
        if(str.trim().isEmpty()){  
            return str;  
        }  
        String pattern="[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";  
        String reStr="";  
        Pattern emoji=Pattern.compile(pattern);  
        Matcher  emojiMatcher=emoji.matcher(str);  
        str=emojiMatcher.replaceAll(reStr);  
        return str;  
    }  
}
