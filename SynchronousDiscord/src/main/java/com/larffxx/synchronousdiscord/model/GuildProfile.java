package com.larffxx.synchronousdiscord.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GuildProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_connect_id")
    private ServersConnect serversConnect;

    @ManyToOne
    @JoinColumn(name ="fk_profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private UsersConnect usersConnect;

    public GuildProfile(String name, UsersConnect usersConnect, ServersConnect serversConnect, Profile profile){
        this.name = name;
        this.usersConnect = usersConnect;
        this.serversConnect = serversConnect;
        this.profile = profile;
    }
}
