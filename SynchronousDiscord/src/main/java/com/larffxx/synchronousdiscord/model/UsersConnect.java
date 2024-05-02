package com.larffxx.synchronousdiscord.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UsersConnect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "users_connect_id")
    private Long id;

    private String discordName, telegramName, discordId;
    @ManyToOne
    @JoinColumn(name = "fk_servers_connect_id", referencedColumnName = "servers_connect_id")
    private ServersConnect serversConnect;

    public UsersConnect(String discordName, String telegramName,String discordId, ServersConnect serversConnect) {
        this.discordName = discordName;
        this.telegramName = telegramName;
        this.discordId = discordId;
        this.serversConnect = serversConnect;
    }
}
