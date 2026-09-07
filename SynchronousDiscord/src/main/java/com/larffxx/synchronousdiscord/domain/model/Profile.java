package com.larffxx.synchronousdiscord.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Profile class.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {
    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The name.
     */
    private String name;

    /**
     * The servers connect.
     */
    @ManyToOne
    @JoinColumn(name = "fk_connect_id")
    private ServersConnect serversConnect;

    /**
     * The users connect.
     */
    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private UsersConnect usersConnect;
}
