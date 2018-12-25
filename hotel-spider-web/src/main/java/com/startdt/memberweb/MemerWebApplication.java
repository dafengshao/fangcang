package com.startdt.memberweb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {"com.startdt.memberweb"})
public class MemerWebApplication extends SpringBootServletInitializer {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
//                    log.info("JVM停服");
                } catch (Exception e) {
//                    log.error("JVM 钩子关闭异常!");
                }
            }
        });
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(MemerWebApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MemerWebApplication.class, args);
    }

}