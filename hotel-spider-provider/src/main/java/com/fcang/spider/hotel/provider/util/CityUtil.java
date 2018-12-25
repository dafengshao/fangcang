package com.fcang.spider.hotel.provider.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

public class CityUtil {
	
	
	public static JSONObject getCtripCityJSON(String fileName) throws IOException {
		File file = new File(fileName);
		String readLine = null;
		try(BufferedReader in = new BufferedReader(new FileReader(file), 1024)) {
			readLine = in.readLine();
			return (JSONObject)JSONObject.parse(readLine);
		}
	}
}
