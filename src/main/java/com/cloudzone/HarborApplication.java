package com.cloudzone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Harbor Server Application Start Main
 *
 * @author gaoyanlei
 * @since 2018/3/9
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class HarborApplication {
    public static void main(String[] args) {
        SpringApplication.run(HarborApplication.class);
    }
}
