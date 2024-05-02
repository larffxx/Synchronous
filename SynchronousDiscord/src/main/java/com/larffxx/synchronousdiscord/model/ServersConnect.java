package com.larffxx.synchronousdiscord.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ServersConnect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "servers_connect_id")
    private Long id;

    private String discordGuild,telegramChannel;

    public ServersConnect(String discordName, String telegramName){
        this.discordGuild = discordName;
        this.telegramChannel = telegramName;
    }
}
