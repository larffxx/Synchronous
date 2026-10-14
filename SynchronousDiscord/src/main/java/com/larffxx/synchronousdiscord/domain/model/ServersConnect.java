package com.larffxx.synchronousdiscord.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Servers Connect class.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class ServersConnect {
    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "servers_connect_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String discordGuild;

    @Column(nullable = false, unique = true)
    private String telegramChannel;

    /**
     * Creates a new ServersConnect.
     * @param id the id.
     */
    public ServersConnect(Long id){
        this.id = id;
    }
}
