package com.fcang.spider.hotel.core;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CommonUtil {
	CommonUtil(){}
	public static Element getElementsByClassFirst(Element elementLi,String className) {
		Elements elementsByClass = elementLi.getElementsByClass(className);
		if(elementsByClass.isEmpty()) {
			return null;
		}
		return elementsByClass.get(0);
	}
	
	private static ThreadLocal<Map<String,Object>> threadLocalMap = new ThreadLocal<>();
	
	
	public static Object get(String key) {
		Map<String, Object> map = threadLocalMap.get();
		if(map==null) {
			return null;
		}
		return map.get(key);
	}
	public static void put(String key,Object value) {
		Map<String, Object> map = threadLocalMap.get();
		if(map==null) {
			threadLocalMap.set(new HashMap<>());
			map = threadLocalMap.get();
		}
		map.put(key, value);
	}
	
	
}
