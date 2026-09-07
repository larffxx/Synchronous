package com.larffxx.synchronoustelegram.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity that links a Discord guild to a Telegram channel.
 * Represents one server connection row in the database.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class ServersConnect {
    /**
     * Database identifier of the server link.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "servers_connect_id")
    private Long id;

    /**
     * Identifiers of the linked Discord guild and Telegram channel.
     */
    private String discordGuild,telegramChannel;

    /**
     * Creates a server link reference with the given identifier.
     * @param id the database identifier
     */
    public ServersConnect(Long id){
        this.id = id;
    }
}
