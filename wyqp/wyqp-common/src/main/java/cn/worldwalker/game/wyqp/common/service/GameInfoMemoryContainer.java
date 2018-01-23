package cn.worldwalker.game.wyqp.common.service;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class GameInfoMemoryContainer {
	
	public static String logFuse = "1";
	public static String loginFuse = "0";
	public static String createRoomFuse = "1";
	public static Map<String, String> tokenUserInfoMap = new ConcurrentHashMap<String, String>();
	public static Map<String, Long> tokenTimeMap = new ConcurrentHashMap<String, Long>();
	public static Map<String, String> roomIdGameTypeUpdateTimeMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> roomIdRoomInfoMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> playerIdRoomIdGameTypeMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> offlinePlayerIdRoomIdGameTypeTimeMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> nnRobIpRoomIdTimeMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> nnShowCardIpRoomIdTimeMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> notReadyIpRoomIdTimeMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> nnNotStakeScoreIpRoomIdTimeMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> jhNoOperationIpPlayerIdRoomIdTimeMap = new ConcurrentHashMap<String, String>();
	public static Map<String, Integer> ipConnectCountMap = new ConcurrentHashMap<String, Integer>();
	public static Stack<String> roomCardOperationFailList = new Stack<String>();
	public static Map<String, String> teaHouseNumTableNumRoomIdMap = new ConcurrentHashMap<String, String>();
	public static Map<String, String> playerIdTeaHouseNumMap = new ConcurrentHashMap<String, String>();
	
}
