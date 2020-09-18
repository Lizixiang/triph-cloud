package tripleh.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import tripleh.gateway.com.filter.AccessLogGlobalFilter;
import tripleh.gateway.com.filter.SecurityGlobalFilter;
import tripleh.gateway.com.filter.WebLogFilter;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public WebLogFilter webLogFilter() {
        return new WebLogFilter();
    }

    @Bean
    public SecurityGlobalFilter securityGlobalFilter() {
        return new SecurityGlobalFilter();
    }

    @Bean
    public AccessLogGlobalFilter accessLogGlobalFilter() {
        return new AccessLogGlobalFilter();
    }

}
