package tripleh.triphauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import tripleh.triphauth.com.interceptor.SecurityGlobalInterceptor;

@SpringBootApplication
@MapperScan("tripleh.*.com.mapper")
@EnableDiscoveryClient
public class TriphAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TriphAuthApplication.class, args);
    }

}
