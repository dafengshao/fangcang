package com.startdt.memberweb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;

/**   
* @Description: TODO(用一句话描述该类做什么) 
* @author hewenfeng   
* @date 2018年1月8日 下午7:51:54 
* @version V1.0   
*/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MemerWebApplication.class)
public class BaseTest {
	 public static byte[] serialize(Object obj) throws IOException {
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        Hessian2Output ho = new Hessian2Output(os);
	        byte[] cc = null;
	        try {
	            if(obj==null) {throw new NullPointerException();}
	            ho.writeObject(obj);
	            ho.flushBuffer();
	            ho.flush();
	            cc=os.toByteArray();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	            ho.close();
	        }
	        return cc;

	    }

	    public static Object deserialize(byte[] by) throws IOException{
	        try {
	            if(by==null) {throw new NullPointerException();}
	            ByteArrayInputStream is = new ByteArrayInputStream(by);
	            Hessian2Input hi = new Hessian2Input(is);
	            return hi.readObject();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;

	    }

}
