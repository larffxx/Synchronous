package com.larffxx.synchronousdiscord.domain.model;

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

    private String discordName, telegramName, discordUserId;
    @ManyToOne
    @JoinColumn(name = "fk_servers_connect_id", referencedColumnName = "servers_connect_id")
    private ServersConnect serversConnect;

    public UsersConnect(Long id){
        this.id = id;
    }
}
