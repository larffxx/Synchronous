package com.larffxx.synchronoustelegram.infrastructure.repo;

import com.larffxx.synchronoustelegram.domain.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildProfileRepository extends JpaRepository<Profile, Long> {
    Profile getByName(String name);
}
