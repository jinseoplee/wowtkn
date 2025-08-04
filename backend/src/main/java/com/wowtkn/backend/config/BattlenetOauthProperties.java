package com.wowtkn.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "battlenet.oauth")
public class BattlenetOauthProperties {

    private String clientId;
    private String clientSecret;
    private String tokenUrl;
}
