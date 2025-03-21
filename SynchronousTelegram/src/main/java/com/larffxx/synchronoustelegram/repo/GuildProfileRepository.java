package com.larffxx.synchronoustelegram.repo;

import com.larffxx.synchronoustelegram.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildProfileRepository extends JpaRepository<Profile, Long> {
    Profile getByName(String name);
}
