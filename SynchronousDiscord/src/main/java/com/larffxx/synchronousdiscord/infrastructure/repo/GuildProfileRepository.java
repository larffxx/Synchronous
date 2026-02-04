package com.larffxx.synchronousdiscord.infrastructure.repo;

import com.larffxx.synchronousdiscord.domain.model.Profile;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByUsersConnect(UsersConnect usersConnect);

    @Transactional
    @Modifying
    @Query("update Profile u set u.name = ?1 where u.usersConnect = ?2")
    void updateByUsersConnect(String name, UsersConnect usersConnect);
}
