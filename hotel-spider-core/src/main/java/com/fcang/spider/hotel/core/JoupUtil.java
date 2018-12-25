package com.fcang.spider.hotel.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.util.JSONPObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;


public class JoupUtil {
	private JoupUtil() {}
	
	private static final ThreadLocal<JSONObject> PROXYINFOTHREADLOCAL = new ThreadLocal() ;
	
	private static final String[] USER_AGENT_S = {"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1"
	,"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0"		
	,"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50"		
	,"Opera/9.80 (Windows NT 6.1; U; zh-cn) Presto/2.9.168 Version/11.50"		
	,"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; Tablet PC 2.0; .NET4.0E)"		
	,"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3)"		
	,"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; GTB7.0)"		
	,"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"		
	,"Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36"		
			
			
	};
	@SuppressWarnings("deprecation")
	public static Connection connect(String aurl,Map<String,String> parm,Map<String, String> headers ) {
		Connection connect = Jsoup.connect(aurl);
		if(headers!=null&&!headers.isEmpty()) {
			connect.headers(headers );
		}
		if(parm!=null&&!parm.isEmpty()) {
			connect.data(parm);
		}
		connect.validateTLSCertificates(false);
		connect.ignoreContentType(true);
		connect.timeout(20000);
		//connect.proxy(host, port);
		return connect;
	}
	public static BaseFullResponse<Document> buildByUrl(String url,Map<String,String> parm,Map<String,String> herders,ProxyInfo info) {
		Connection connect = JoupUtil.connect(url,parm,herders);
		if(info!=null) {
			connect.proxy(info.getHost(), info.getPort());
		}
		Response execute;
		try {
			execute = connect.execute();
			Document parse = Jsoup.parse(execute.body());
			return BaseFullResponse.success(parse);
		} catch (HttpStatusException e) {
			if(e.getStatusCode()==404) {
				return BaseFullResponse.emptyData();
			}
			return BaseFullResponse.failed(e.getMessage());
		}catch (IOException e) {
			return BaseFullResponse.failed("IOException",e.getMessage());
		}
		
	}
	
	
	public static BaseFullResponse<Document> buildByFile(String fileName,String charsetName) {
		File in = new File(fileName);
		if(!in.exists()) {
			return BaseFullResponse.failed("FILE_NOT_EXIST",fileName+"文件不存在。");
		}
		try {
			Document parse = Jsoup.parse(in, charsetName);
			return BaseFullResponse.success(parse);
		} catch (IOException e) {
			return BaseFullResponse.failed("IOException");
		}
	}
	
	public static void setProxy(ProxyInfo info) {
		JSONObject jsonObject = PROXYINFOTHREADLOCAL.get();
		if(jsonObject==null) {
			jsonObject = new JSONObject();
			PROXYINFOTHREADLOCAL.set(jsonObject);
		}
		jsonObject.put("proxy", info);
	}
	
	public static void setRandomUa() {
		String agent = USER_AGENT_S[NumberUtil.random(USER_AGENT_S.length)];
		setUserAgent(agent);
	}
	
	public static void setUserAgent(String agent) {
		
		JSONObject jsonObject = PROXYINFOTHREADLOCAL.get();
		if(jsonObject==null) {
			jsonObject = new JSONObject();
			PROXYINFOTHREADLOCAL.set(jsonObject);
		}
		jsonObject.put("userAgent", agent);
	}
	
	public static ProxyInfo getProxy() {
		JSONObject jsonObject = PROXYINFOTHREADLOCAL.get();
		if(jsonObject==null) {
			return null;
		}
		return (ProxyInfo) jsonObject.get("proxy");
	}
	
	public static String getUserAgent() {
		JSONObject jsonObject = PROXYINFOTHREADLOCAL.get();
		if(jsonObject==null) {
			return null;
		}
		return jsonObject.getString("userAgent");
	}
	
	
	public static void setKV(String key,String value) {
		JSONObject jsonObject = PROXYINFOTHREADLOCAL.get();
		if(jsonObject==null) {
			jsonObject = new JSONObject();
			PROXYINFOTHREADLOCAL.set(jsonObject);
		}
		jsonObject.put(key, value);
	}
	public  static <T> T getValue(String key) {
		JSONObject jsonObject = PROXYINFOTHREADLOCAL.get();
		if(jsonObject==null) {
			return null;
		}
		return (T)jsonObject.get(key);
	}
	
	public static void setBatchInfo(String batchCode) {
		JSONObject jsonObject = PROXYINFOTHREADLOCAL.get();
		if(jsonObject==null) {
			jsonObject = new JSONObject();
			PROXYINFOTHREADLOCAL.set(jsonObject);
		}
		jsonObject.put("taskBatch", batchCode);
	}
	
	public static String getBatchInfo() {
		JSONObject jsonObject = PROXYINFOTHREADLOCAL.get();
		if(jsonObject==null) {
			return null;
		}
		return jsonObject.getString("taskBatch");
	}
	
	public static void remove() {
		PROXYINFOTHREADLOCAL.remove();
	}
}
