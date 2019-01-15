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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


public class JoupUtil {
	private JoupUtil() {}
	
	static final Logger LOGGER
	= LoggerFactory.getLogger(JoupUtil.class);
	
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
		connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		connect.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
		connect.cookie("Cookie", "ASP.NET_SessionId=ftgwhqoygl4lxz1xridqqypp; _abtest_userid=1ee16158-a288-4541-8ad9-eededecd017c; OID_ForOnlineHotel=15470143643052onlwm1547014365155102002; _ga=GA1.2.2113510859.1547014367; _gid=GA1.2.537436028.1547014367; _RF1=222.212.140.66; _RSG=gcK7ZYWloBCIWsPgwaMCaA; _RDG=28adb20bdd57d8238f3970baf9b380a635; _RGUID=c2f2be08-a31d-49aa-a90c-5a4eaa676812; MKT_Pagesource=PC; appFloatCnt=4; manualclose=1; _bfa=1.1547014364305.2onlwm.1.1547014364305.1547017769043.2.10; _bfs=1.9; MKT_OrderClick=ASID=&CT=1547018402718&CURL=http%3A%2F%2Fhotels.ctrip.com%2Fhotel%2Fwuhan477%2Fp204&VAL={\"pc_vid\":\"1547014364305.2onlwm\"}; _jzqco=%7C%7C%7C%7C1547014367806%7C1.2024051462.1547014367481.1547017814946.1547018402758.1547017814946.1547018402758.undefined.0.0.6.6; __zpspc=9.2.1547017771.1547018402.5%234%7C%7C%7C%7C%7C%23; _gat=1; _bfi=p1%3D102002%26p2%3D102002%26v1%3D10%26v2%3D9");
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
			LOGGER.error("",e);
			return BaseFullResponse.failed(e.getMessage());
		}catch (IOException e) {
			LOGGER.error("",e);
			return BaseFullResponse.failed("IOException",e.getMessage());
		}
		
	}
	
	
	public static BaseFullResponse<Document> buildByFile(String fileName,String charsetName) {
		File in = new File(fileName);
		if(!in.exists()) {
			return BaseFullResponse.failed("FILE_NOT_EXIST",fileName+"文件不存在。");
		}
		return buildByFile(in,charsetName);
	}
	
	public static BaseFullResponse<Document> buildByFile(File htmlFile,String charsetName) {
		try {
			Document parse = Jsoup.parse(htmlFile, charsetName);
			return BaseFullResponse.success(parse);
		} catch (IOException e) {
			LOGGER.error("",e);
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
