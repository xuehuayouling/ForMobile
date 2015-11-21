package com.ysq.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ValidVerifyUtil {

	private static Map<String, Long> sessionMap = new HashMap<String, Long>();
	
	public synchronized static void addSession(String key, long sessionID) {
		sessionMap.put(key, sessionID);
	}
	
	public static boolean isValid(long sessionID) {
		if (sessionMap.containsValue(sessionID)) {
			return true;
		}
		return false;
	}
	
	public static long getSessionID(String userName) {
		long sessionID = -1;
		if (sessionMap.containsKey(userName)) {
			sessionID = sessionMap.get(userName);
		} else {
			Random rdm = new Random(System.currentTimeMillis());
			synchronized (sessionMap) {
				sessionID = Math.abs(rdm.nextLong());
				while (sessionMap.containsValue(sessionID)) {
					sessionID = rdm.nextLong();
				}
				addSession(userName, sessionID);
			}
		}
		return sessionID;
	}
}
