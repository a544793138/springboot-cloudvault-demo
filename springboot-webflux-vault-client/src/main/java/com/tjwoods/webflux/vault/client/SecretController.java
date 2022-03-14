package com.tjwoods.webflux.vault.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretController {

    @Autowired
    private TestBean testBean;

    @Autowired
    private VaultTemplate vaultTemplate;

    @GetMapping("/pwd")
    public String getPassword() {
        return testBean.getPassword();
    }
}
