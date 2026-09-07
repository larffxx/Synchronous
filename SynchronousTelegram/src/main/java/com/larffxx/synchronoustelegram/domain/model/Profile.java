package com.larffxx.synchronoustelegram.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * JPA entity that links a registered user to a connected server.
 * Represents one guild profile row in the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {
    /**
     * Database identifier of the profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Display name of the profile.
     */
    private String name;

    /**
     * Server link this profile belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "fk_connect_id")
    private ServersConnect serversConnect;

    /**
     * User link this profile refers to.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private UsersConnect usersConnect;
}
