package com.team.backend.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * <p>
 *  TODO
 * </p>
 *
 * @author Colin
 * @since 2023/12/10
 */
@Configuration
@Data
@PropertySources({@PropertySource(value = "classpath:application.properties",encoding = "UTF-8")})
public class RemoteConfig {
    @Value("${remote.ip}")
    private String ip;
    @Value("${remote.username}")
    private String username;
    @Value("${remote.password}")
    private String password;
    @Value("${remote.backupdir}")
    private String backupDir;
    @Value("${remote.appenddir}")
    private String appendDir;
    @Value("${remote.port}")
    private String port;
}
