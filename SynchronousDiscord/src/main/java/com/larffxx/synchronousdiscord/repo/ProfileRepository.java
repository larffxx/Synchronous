package com.larffxx.synchronousdiscord.repo;

import com.larffxx.synchronousdiscord.model.Profile;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUsersConnect(UsersConnect usersConnect);
    boolean existsByUsersConnect(UsersConnect usersConnect);

    @Transactional
    @Modifying
    @Query("update Profile u set u.description = ?1, u.photoUrl = ?2, u.socialUrl = ?3 where u.usersConnect = ?4")
    void updateAllByProfileData(String description, String url, String socialUrl, UsersConnect usersConnect);
}
