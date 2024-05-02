package com.larffxx.synchronoustelegram.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UsersConnect {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "users_connect_id"
    )
    private Long id;
    private String discordName;
    private String telegramName;

    @ManyToOne
    @JoinColumn(name = "fk_servers_connect_id", referencedColumnName = "servers_connect_id")
    private ServersConnect serversConnect;
    public UsersConnect(String discordName, String telegramName) {
        this.discordName = discordName;
        this.telegramName = telegramName;
    }

    public UsersConnect(String discordName) {
        this.discordName = discordName;
    }
}