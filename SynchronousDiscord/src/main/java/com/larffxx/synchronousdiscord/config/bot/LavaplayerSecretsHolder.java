package com.larffxx.synchronousdiscord.config.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class LavaplayerSecretsHolder {
    @Value("${clientId}")
    private String spotifyClientId;
    @Value("${clientSecret}")
    private String spotifyClientSecret;
    @Value("${yandexToken}")
    private String yandexAccessToken;
}
