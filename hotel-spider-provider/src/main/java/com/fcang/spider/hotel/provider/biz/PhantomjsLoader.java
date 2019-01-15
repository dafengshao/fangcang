package com.fcang.spider.hotel.provider.biz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;

/**
 * Hello world!
 *
 */
public class PhantomjsLoader 
{  
	static final  Logger LOGGER = LoggerFactory.getLogger(PhantomjsLoader.class);
	static final String SPACE=" ";
	
	public static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");
	
	/***/
    public String loadCtripHTMLContent(String cmdCommand,JSONObject json,String chartset) throws IOException,InterruptedException {  
        String processExec = cmdCommand;//phantomjs script.js   指令+js参数+json参数
        StringBuilder sb = new StringBuilder(processExec);
        sb.append(SPACE);
        if(isWindows) {
        	sb.append(json.toJSONString().replaceAll("\"", "\\\\\""));
        }else {
        	sb.append("'").append(json.toJSONString()).append("'");
        }
        String exec = sb.toString();
        return loadJs(exec,chartset);
    }

    public static String loadJs(String exec,String chartset)
			throws IOException, InterruptedException {
    	if(chartset==null) {
    		chartset = "utf-8";
    	}
        StringBuilder sbf = new StringBuilder();  
		InputStream is = null;
		BufferedReader br = null;
		String[] exes = new String[]{"/bin/sh","-c",exec};
        LOGGER.info(exec);
        try{
        	Runtime rt = Runtime.getRuntime();  
        	Process p = null; 
        	if(isWindows){
        		p = rt.exec(exec.split(SPACE)); 
        	}else{
        		p = rt.exec(exes); 
        	}
	        is = p.getInputStream();  
	        br = new BufferedReader(new InputStreamReader(is,chartset));  
	        String tmp = "";  
	        while((tmp=br.readLine())!=null) {  
	        	sbf.append(tmp).append("\n");
	        	LOGGER.info("phantomjs ,Process:{},info:{}",p.hashCode(),tmp);
	        }
        }finally{
        	if(br!=null)br.close();
        	if(is!=null)is.close();
        }
        return sbf.toString();
	}
   
    
public static void main(String[] args) throws IOException {
	String exec = "phantomjs C:/Users/47246/github/hotel-spider/phantomjs-2.1.1-windows/bin/download_as_file.js \"{\\\"checkin\\\":\\\"2018-12-30\\\",\\\"checkout\\\":\\\"2018-12-31\\\",\\\"filePath\\\":\\\"C:/Users/47246/github/hotel-spider/phantomjs-2.1.1-windows/bin/html/2018-12-30/\\\",\\\"list\\\":[{\\\"id\\\":1963429},{\\\"id\\\":6866628},{\\\"id\\\":1700578},{\\\"id\\\":9239148},{\\\"id\\\":2012420},{\\\"id\\\":2012203},{\\\"id\\\":1315955},{\\\"id\\\":1315811},{\\\"id\\\":26106267},{\\\"id\\\":806269}]}\\\"";
	Runtime rt = Runtime.getRuntime();  
	 Process process = rt.exec(exec); 
	 InputStream is = null;
	BufferedReader br = null;
	 is = process.getInputStream();  
     br = new BufferedReader(new InputStreamReader(is,"gb2312")); 
     String tmp = "";  
     while((tmp=br.readLine())!=null) {
    	 System.out.println(tmp);
     }
     
 	if(br!=null)br.close();
 	if(is!=null)is.close();
}
}
