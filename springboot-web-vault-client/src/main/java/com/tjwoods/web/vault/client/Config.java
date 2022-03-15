package com.tjwoods.web.vault.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.vault.annotation.VaultPropertySource;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

@Configuration
public class Config {

    @Value("${password}")
    private String password;

    @Bean
    public TestBean testBean() {
        final TestBean testBean = new TestBean();
        System.out.println(password);
        testBean.setPassword(password);
        return testBean;
    }
}
