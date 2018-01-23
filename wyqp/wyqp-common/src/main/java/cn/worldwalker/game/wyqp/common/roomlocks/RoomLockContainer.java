package cn.worldwalker.game.wyqp.common.roomlocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * 每个房间都会分配一把锁，控制此房间请求排队
 * @author jinfeng.liu
 *
 */
public class RoomLockContainer {
	private static final Map<Integer, Lock> lockMap = new ConcurrentHashMap<Integer, Lock>();
	
	public static Lock getLockByRoomId(Integer roomId){
		return lockMap.get(roomId);
	}
	
	public static void setLockByRoomId(Integer roomId, Lock lock){
		lockMap.put(roomId, lock);
	}
	
	public static void delLockByRoomId(Integer roomId){
		lockMap.remove(roomId);
	}
}
