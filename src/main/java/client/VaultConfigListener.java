package client;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessorApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class VaultConfigListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {


    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        try {
            final ClassLoader loader = getClass().getClassLoader();
            final InputStream inputStream = loader.getResourceAsStream("application.properties");
            final Properties properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
            final String keyStorePassword = properties.getProperty("spring.cloud.vault.ssl.key-store-password");
            final String trustStorePassword = properties.getProperty("spring.cloud.vault.ssl.trust-store-password");
            final String realKeyStorePwd = getRealPwd(keyStorePassword);
            final String realTrustStorePwd = getRealPwd(trustStorePassword);
            final Map<String, Object> map = Map.of(
                    "spring.cloud.vault.ssl.key-store-password", realKeyStorePwd,
                    "spring.cloud.vault.ssl.trust-store-password", realTrustStorePwd
            );
            event.getEnvironment().getPropertySources().addFirst(new MapPropertySource("defaultConfigurationPropertySource", map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getRealPwd(String maybePath) throws Exception {
        if (!maybePath.startsWith("file:")) {
            return maybePath;
        }
        final File file1 = ResourceUtils.getFile(maybePath);
        final FileInputStream fileInputStream1 = new FileInputStream(file1);
        final byte[] bytes = new byte[fileInputStream1.available()];
        fileInputStream1.read(bytes);
        fileInputStream1.close();
        return new String(bytes);
    }

    /**
     * 该顺序固定，不要修改。否则无法在 spring 完成 vault 配置前将配置文件中 password 的配置从文件中读出并回填回去。
     * 该顺序的值来自于 {@link  EnvironmentPostProcessorApplicationListener#DEFAULT_ORDER}
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 9;
    }
}
