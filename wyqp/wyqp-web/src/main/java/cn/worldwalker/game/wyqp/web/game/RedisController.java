package cn.worldwalker.game.wyqp.web.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.common.domain.base.RedisRequest;
import cn.worldwalker.game.wyqp.common.domain.base.RedisResponse;
import cn.worldwalker.game.wyqp.common.service.GameInfoMemoryContainer;
import cn.worldwalker.game.wyqp.common.utils.redis.JedisTemplate;

@Controller
@RequestMapping("redis/")
public class RedisController {
	
//	@Autowired
	private JedisTemplate jedisTemplate;
	
	@RequestMapping(value = "do", method = RequestMethod.POST)
    @ResponseBody
    public RedisResponse redisOperation(@RequestBody RedisRequest request) {
		RedisResponse response = new RedisResponse();
		String operation = request.getOperation();
		try {
			switch (operation) {
				case "get":
					if (Constant.gameInfoStorageType == 0 ) {
						response.setValue(jedisTemplate.get(request.getKey()));
					}else{
						if ("logFuse".equals(request.getKey())) {
							response.setValue(GameInfoMemoryContainer.logFuse);
						}else if("loginFuse".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.loginFuse);
						}else if("createRoomFuse".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.createRoomFuse);
						}else{
							response.setDes("no such operation");
						}
					}
					break;
				case "set":
					if (Constant.gameInfoStorageType == 0 ) {
						jedisTemplate.set(request.getKey(), String.valueOf(request.getValue()));	
						response.setValue(jedisTemplate.get(request.getKey()));
					}else{
						if ("logFuse".equals(request.getKey())) {
							GameInfoMemoryContainer.logFuse = String.valueOf(request.getValue());
							response.setValue(GameInfoMemoryContainer.logFuse);
						}else if("loginFuse".equals(request.getKey())){
							GameInfoMemoryContainer.loginFuse = String.valueOf(request.getValue());
							response.setValue(GameInfoMemoryContainer.loginFuse);
						}else if("createRoomFuse".equals(request.getKey())){
							GameInfoMemoryContainer.createRoomFuse = String.valueOf(request.getValue());
							response.setValue(GameInfoMemoryContainer.createRoomFuse);
						}else{
							response.setDes("no such operation");
						}
					}
					break;
				case "del":
					if (Constant.gameInfoStorageType == 0 ) {
						jedisTemplate.del(request.getKey());
					}else{
						if ("logFuse".equals(request.getKey())) {
							GameInfoMemoryContainer.logFuse = null;
						}else if("loginFuse".equals(request.getKey())){
							GameInfoMemoryContainer.loginFuse = null;
						}else if("tokenUserInfoMap".equals(request.getKey())){
							GameInfoMemoryContainer.tokenUserInfoMap.clear();
						}else if("tokenTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.tokenTimeMap.clear();
						}else if("roomIdGameTypeUpdateTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.roomIdGameTypeUpdateTimeMap.clear();
						}else if("roomIdRoomInfoMap".equals(request.getKey())){
							GameInfoMemoryContainer.roomIdRoomInfoMap.clear();
						}else if("playerIdRoomIdGameTypeMap".equals(request.getKey())){
							GameInfoMemoryContainer.playerIdRoomIdGameTypeMap.clear();
						}else if("offlinePlayerIdRoomIdGameTypeTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.offlinePlayerIdRoomIdGameTypeTimeMap.clear();
						}else if("nnRobIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.nnRobIpRoomIdTimeMap.clear();
						}else if("nnShowCardIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.nnShowCardIpRoomIdTimeMap.clear();
						}else if("notReadyIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.notReadyIpRoomIdTimeMap.clear();
						}else if("jhNoOperationIpPlayerIdRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.jhNoOperationIpPlayerIdRoomIdTimeMap.clear();
						}else if("ipConnectCountMap".equals(request.getKey())){
							GameInfoMemoryContainer.ipConnectCountMap.clear();
						}else{
							response.setDes("no such operation");
						}
					}
					break;
				case "hgetAll":
					
					if (Constant.gameInfoStorageType == 0 ) {
						response.setValue(jedisTemplate.hgetAll(request.getKey()));
					}else{
						if("tokenUserInfoMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.tokenUserInfoMap);
						}else if("tokenTimeMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.tokenTimeMap);
						}else if("roomIdGameTypeUpdateTimeMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.roomIdGameTypeUpdateTimeMap);
						}else if("roomIdRoomInfoMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.roomIdRoomInfoMap);
						}else if("playerIdRoomIdGameTypeMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.playerIdRoomIdGameTypeMap);
						}else if("offlinePlayerIdRoomIdGameTypeTimeMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.offlinePlayerIdRoomIdGameTypeTimeMap);
						}else if("nnRobIpRoomIdTimeMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.nnRobIpRoomIdTimeMap);
						}else if("nnShowCardIpRoomIdTimeMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.nnShowCardIpRoomIdTimeMap);
						}else if("notReadyIpRoomIdTimeMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.notReadyIpRoomIdTimeMap);
						}else if("jhNoOperationIpPlayerIdRoomIdTimeMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.jhNoOperationIpPlayerIdRoomIdTimeMap);
						}else if("ipConnectCountMap".equals(request.getKey())){
							response.setValue(GameInfoMemoryContainer.ipConnectCountMap);
						}else{
							response.setDes("no such operation");
						}
					}
					break;
				case "hset":
					if (Constant.gameInfoStorageType == 0 ) {
						jedisTemplate.hset(request.getKey(),request.getField() , String.valueOf(request.getValue()));
						response.setValue(jedisTemplate.hget(request.getKey(),request.getField()));
					}else{
						if("tokenUserInfoMap".equals(request.getKey())){
							GameInfoMemoryContainer.tokenUserInfoMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.tokenUserInfoMap.get(request.getField()));
						}else if("tokenTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.tokenTimeMap.put(request.getField() , Long.valueOf(String.valueOf(request.getValue())));
							response.setValue(GameInfoMemoryContainer.tokenTimeMap.get(request.getField()));
						}else if("roomIdGameTypeUpdateTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.roomIdGameTypeUpdateTimeMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.roomIdGameTypeUpdateTimeMap.get(request.getField()));
						}else if("roomIdRoomInfoMap".equals(request.getKey())){
							GameInfoMemoryContainer.roomIdRoomInfoMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.roomIdRoomInfoMap.get(request.getField()));
						}else if("playerIdRoomIdGameTypeMap".equals(request.getKey())){
							GameInfoMemoryContainer.playerIdRoomIdGameTypeMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.playerIdRoomIdGameTypeMap.get(request.getField()));
						}else if("offlinePlayerIdRoomIdGameTypeTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.offlinePlayerIdRoomIdGameTypeTimeMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.offlinePlayerIdRoomIdGameTypeTimeMap.get(request.getField()));
						}else if("nnRobIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.nnRobIpRoomIdTimeMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.nnRobIpRoomIdTimeMap.get(request.getField()));
						}else if("nnShowCardIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.nnShowCardIpRoomIdTimeMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.nnShowCardIpRoomIdTimeMap.get(request.getField()));
						}else if("notReadyIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.notReadyIpRoomIdTimeMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.notReadyIpRoomIdTimeMap.get(request.getField()));
						}else if("jhNoOperationIpPlayerIdRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.jhNoOperationIpPlayerIdRoomIdTimeMap.put(request.getField() , String.valueOf(request.getValue()));
							response.setValue(GameInfoMemoryContainer.jhNoOperationIpPlayerIdRoomIdTimeMap.get(request.getField()));
						}else if("ipConnectCountMap".equals(request.getKey())){
							GameInfoMemoryContainer.ipConnectCountMap.put(request.getField() , Integer.valueOf(String.valueOf(request.getValue())));
							response.setValue(GameInfoMemoryContainer.ipConnectCountMap.get(request.getField()));
						}else{
							response.setDes("no such operation");
						}
					}
					break;
				case "hdel":
					if (Constant.gameInfoStorageType == 0 ) {
						jedisTemplate.hdel(request.getKey(),request.getField());
					}else{
						if("tokenUserInfoMap".equals(request.getKey())){
							GameInfoMemoryContainer.tokenUserInfoMap.remove(request.getField());
						}else if("tokenTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.tokenTimeMap.remove(request.getField());
						}else if("roomIdGameTypeUpdateTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.roomIdGameTypeUpdateTimeMap.remove(request.getField());
						}else if("roomIdRoomInfoMap".equals(request.getKey())){
							GameInfoMemoryContainer.roomIdRoomInfoMap.remove(request.getField());
						}else if("playerIdRoomIdGameTypeMap".equals(request.getKey())){
							GameInfoMemoryContainer.playerIdRoomIdGameTypeMap.remove(request.getField());
						}else if("offlinePlayerIdRoomIdGameTypeTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.offlinePlayerIdRoomIdGameTypeTimeMap.remove(request.getField());
						}else if("nnRobIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.nnRobIpRoomIdTimeMap.remove(request.getField());
						}else if("nnShowCardIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.nnShowCardIpRoomIdTimeMap.remove(request.getField());
						}else if("notReadyIpRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.notReadyIpRoomIdTimeMap.remove(request.getField());
						}else if("jhNoOperationIpPlayerIdRoomIdTimeMap".equals(request.getKey())){
							GameInfoMemoryContainer.jhNoOperationIpPlayerIdRoomIdTimeMap.remove(request.getField());
						}else if("ipConnectCountMap".equals(request.getKey())){
							GameInfoMemoryContainer.ipConnectCountMap.remove(request.getField());
						}else{
							response.setDes("no such operation");
						}
					}
					break;
				case "lpush":
					if (Constant.gameInfoStorageType == 0 ) {
						jedisTemplate.lpush(request.getKey(),String.valueOf(request.getValue()));
					}else{
						
					}
					break;
				case "lrange":
					if (Constant.gameInfoStorageType == 0 ) {
						jedisTemplate.lrange(request.getKey(),request.getStart(),request.getEnd());
					}else{
						
					}
					break;
				default:
					response.setDes("no such operation");
					break;
			}
		} catch (Exception e) {
			response.setDes("sys exception");
		}
        return response;
    }
}
