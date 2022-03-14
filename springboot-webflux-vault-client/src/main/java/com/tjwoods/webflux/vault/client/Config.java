package com.tjwoods.webflux.vault.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class Config {

    @Autowired
    private Environment environment;

    @Value("${password}")
    private String password;

    @Value("${key-store-password}")
    private String keyStorePassword;

    @Bean
    public TestBean testBean() {
        final TestBean testBean = new TestBean();
        testBean.setPassword(environment.getProperty("password"));
        System.out.println(environment.getProperty("key-store-password"));
        System.out.println(password);
        System.out.println(keyStorePassword);
        return testBean;
    }
}
