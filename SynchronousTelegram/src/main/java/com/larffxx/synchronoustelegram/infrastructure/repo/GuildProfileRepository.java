package com.larffxx.synchronoustelegram.infrastructure.repo;

import com.larffxx.synchronoustelegram.domain.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for guild profile entities.
 * Provides lookup of registered profiles by Discord name.
 */
@Repository
public interface GuildProfileRepository extends JpaRepository<Profile, Long> {
    /**
     * Finds a profile by Discord name.
     * @param name the Discord name to search for
     * @return the matching profile
     */
    Profile getByName(String name);
}
