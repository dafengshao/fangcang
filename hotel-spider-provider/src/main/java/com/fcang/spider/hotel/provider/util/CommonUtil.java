package com.fcang.spider.hotel.provider.util;

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
}
