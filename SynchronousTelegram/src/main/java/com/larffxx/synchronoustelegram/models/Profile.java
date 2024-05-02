package com.larffxx.synchronoustelegram.models;

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

    private String description, photoUrl, socialUrl;

    @OneToOne
    @JoinColumn(name = "fk_users_connect_id", referencedColumnName = "users_connect_id", unique=true)
    private UsersConnect usersConnect;

    public Profile(String description, String photoUrl,String socialUrl, UsersConnect usersConnect){
        this.description = description;
        this.photoUrl = photoUrl;
        this.usersConnect = usersConnect;
        this.socialUrl = socialUrl;
    }
}
