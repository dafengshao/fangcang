package com.startdt.memberweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class PhantomjsLoader 
{  
	static final  Logger LOGGER = LoggerFactory.getLogger(PhantomjsLoader.class);
	static final String SPACE=" ";
	
	static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");
	
	/**
	 * 
	 * @param p[0] cmdCommand 指令如：phantomjs --proxy=117.143.109.136:80 
	 * @param p[1] js文件路径 如：download_as_file.js
	 * @param 其他参数 p[2] url;p[3] chartset;p[4] cookie;...
	 * @return
	 */
	public String loadContent(String... p){
		
		return null;
	}
	
	
	/***/
    public String loadCtripHTMLContent(String cmdCommand,String url,String chartset) throws IOException {  
        String processExec = cmdCommand;//phantomjs script.js   指令+参数
        //processExec = String.format(processExec, "");
        StringBuilder sb = new StringBuilder(processExec);
        sb.append(SPACE);
        sb.append("\"").append(url).append("\"");
        String exec = sb.toString();
        return loadJs(exec,chartset);
    }

    public static String loadJs(String exec,String chartset)
			throws IOException {
        StringBuilder sbf = new StringBuilder();  
		InputStream is = null;
		BufferedReader br = null;
		String[] exes = new String[]{"/bin/sh","-c",exec};
        LOGGER.info(exec);
        try{
        	Runtime rt = Runtime.getRuntime();  
        	Process p = null; 
        	if(isWindows){
        		p = rt.exec(exec); 
        	}else{
        		p = rt.exec(exes); 
        	}
	        
	        is = p.getInputStream();  
	        br = new BufferedReader(new InputStreamReader(is,chartset));  
	        String tmp = "";  
	        while((tmp=br.readLine())!=null) {  
	        	if(tmp.startsWith("phantomjs")){
	        		if(tmp.contains("exception")||tmp.contains("timeout")||tmp.contains("error")){
	        			LOGGER.error(tmp);
		        	}else{
		        		LOGGER.info(tmp);
		        	}
	        		continue;
	        	}
	        	sbf.append(tmp).append("\n");
	        }
        }finally{
        	if(br!=null)br.close();
        	if(is!=null)is.close();
        }
        return sbf.toString();
	}
   
    public void loadHTMLAsFile(String cmdCommand,String url,String fileName) throws Exception {  
        String processExec = cmdCommand;//phantomjs script.js   指令+参数
        //processExec = String.format(processExec, "");
        StringBuilder sb = new StringBuilder(processExec);
        sb.append(SPACE);
        sb.append("\"").append(url).append("\"");
        sb.append(SPACE);
        sb.append(fileName);
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder sbf = new StringBuilder(); 
        String exec = sb.toString();
        String[] exes = new String[]{"/bin/sh","-c",exec};
        LOGGER.info(exec);
        try{
        	Runtime rt = Runtime.getRuntime();  
        	Process p = null; 
        	if(isWindows){
        		p = rt.exec(exec); 
        	}else{
        		p = rt.exec(exes); 
        	}
        	is = p.getInputStream();  
	        br = new BufferedReader(new InputStreamReader(is));  
	        String tmp = "";  
	        while((tmp=br.readLine())!=null) {  
	        	if(tmp.startsWith("phantomjs")){
	        		LOGGER.info(tmp);
	        		sbf.append(tmp).append("\n");
	        		if(tmp.contains("exception")){
	        			sbf.append(cmdCommand).append("\n");
	        			throw new Exception(sbf.toString());
	        		}else
	        		if(tmp.contains("timeout")){
	        			sbf.append(cmdCommand).append("\n");
	        			throw new TimeoutException(sbf.toString());
	        		}
	        	}
	        	continue;
	        }
        }finally{
        	sbf=null;
        	if(br!=null)br.close();
        	if(is!=null)is.close();
        }
       
    }
    

}
