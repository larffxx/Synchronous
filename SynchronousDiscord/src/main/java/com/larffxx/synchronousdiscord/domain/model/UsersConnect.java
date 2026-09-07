package com.larffxx.synchronousdiscord.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Users Connect class.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class UsersConnect {
    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "users_connect_id")
    private Long id;

    /**
     * The discord user id.
     */
    private String discordName, telegramName, discordUserId;
    /**
     * The servers connect.
     */
    @ManyToOne
    @JoinColumn(name = "fk_servers_connect_id", referencedColumnName = "servers_connect_id")
    private ServersConnect serversConnect;

    /**
     * Creates a new UsersConnect.
     * @param id the id.
     */
    public UsersConnect(Long id){
        this.id = id;
    }
}
