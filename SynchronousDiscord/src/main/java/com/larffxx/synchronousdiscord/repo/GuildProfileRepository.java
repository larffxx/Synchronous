package com.larffxx.synchronousdiscord.repo;

import com.larffxx.synchronousdiscord.model.GuildProfile;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildProfileRepository extends JpaRepository<GuildProfile, Long> {

    boolean existsByUsersConnect(UsersConnect usersConnect);
    GuildProfile getGuildProfileByUsersConnect(UsersConnect usersConnect);

    GuildProfile getByName(String name);

    @Transactional
    @Modifying
    @Query("update GuildProfile u set u.name = ?1 where u.usersConnect = ?2")
    void updateByUsersConnect(String name, UsersConnect usersConnect);
}
