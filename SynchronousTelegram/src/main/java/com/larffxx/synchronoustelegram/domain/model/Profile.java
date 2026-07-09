package com.larffxx.synchronoustelegram.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//TODO: DTO LAYER
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

    public Profile(String name, UsersConnect usersConnect, ServersConnect serversConnect){
        this.name = name;
        this.usersConnect = usersConnect;
        this.serversConnect = serversConnect;
    }
}
