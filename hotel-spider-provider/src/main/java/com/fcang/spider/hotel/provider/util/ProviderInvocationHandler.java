package com.fcang.spider.hotel.provider.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**   
* @Description: jdk动态代理
* @author hewenfeng   
* @date 2017年11月30日 下午5:28:39 
* @version V1.0   
*/
public interface ProviderInvocationHandler extends InvocationHandler {
	
    @SuppressWarnings("unchecked")
    default <T> T getProxy(T t){
        ClassLoader loader = t.getClass().getClassLoader();  
        Class<?>[] interfaces = t.getClass().getInterfaces();  
        return  (T)Proxy.newProxyInstance(loader, interfaces, this); 
    }
    
}
