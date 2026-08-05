package com.larffxx.synchronousdiscord.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_connect_id")
    private ServersConnect serversConnect;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private UsersConnect usersConnect;
}
