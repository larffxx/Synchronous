package com.larffxx.synchronoustelegram.repo;

import com.larffxx.synchronoustelegram.models.GuildProfile;
import com.larffxx.synchronoustelegram.models.UsersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildProfileRepository extends JpaRepository<GuildProfile, Long> {
    GuildProfile getByName(String name);
}
