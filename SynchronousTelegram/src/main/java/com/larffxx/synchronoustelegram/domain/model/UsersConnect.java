package com.larffxx.synchronoustelegram.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity that links a Discord user to a Telegram user within one server.
 * Represents one user connection row in the database.
 */
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
    /**
     * Database identifier of the user link.
     */
    private Long id;
    /**
     * Discord name, Telegram name, and Discord user id of the linked user.
     */
    private String discordName, telegramName, discordUserId;

    /**
     * Coolness rating of the user, from 0 to 100.
     */
    private int coolness;

    /**
     * Server link this user link belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "fk_servers_connect_id", referencedColumnName = "servers_connect_id")
    private ServersConnect serversConnect;

    /**
     * Creates a user link reference with the given identifier.
     * @param id the database identifier
     */
    public UsersConnect(Long id){
        this.id = id;
    }
}