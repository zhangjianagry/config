package restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    /**
     * 实例化RestTemplate
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate rest() {
        return new RestTemplate();
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
