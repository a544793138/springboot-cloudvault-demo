package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.vault.annotation.VaultPropertySource;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.util.*;

@Configuration
public class Config {

    @Value("${password:local}")
    private String password;

    private VaultKeyValueOperations vaultKvOps;
    private String secretPath;

    @Bean
    public TestBean testBean(VaultTemplate vaultTemplate, Environment environment) {
        final TestBean testBean = new TestBean();
//        System.out.println(password);
        testBean.setPassword(password);
        testVaultKvOps(vaultTemplate, environment);
        return testBean;
    }

    private void testVaultKvOps(VaultTemplate vaultTemplate, Environment environment) {

        final List<String> vaultPath = environment.getProperty("spring.config.import", List.class);
        System.out.printf("Get property `spring.config.import` => %s\n", vaultPath);
        assert vaultPath != null;
        vaultPath.stream().filter(s -> s.startsWith("vault://")).findFirst()
                .map(s -> s.replace("vault://", ""))
                .ifPresent(pathAll -> {
                    final String[] paths = pathAll.split("/");
                    vaultKvOps = vaultTemplate.opsForKeyValue(paths[0], VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);
                    secretPath = paths[1];
                });

        vaultKvOps.patch(secretPath,Collections.singletonMap("email.pwd.test", "test-write-secret"));
        System.out.println("Test write to vault => key: email.pwd.test, value: test-write-secret.");
        vaultKvOps.patch(secretPath,Collections.singletonMap("email.pwd.test", "test-update-secret"));
        System.out.println("Test update to vault => key: email.pwd.test, value: test-update-secret.");

        final String testGet = String.valueOf(vaultKvOps.get(secretPath).getData().get("email.pwd.test"));
        System.out.printf("Test get from vault => key: email.pwd.test, value: %s\n", testGet);
    }
}
