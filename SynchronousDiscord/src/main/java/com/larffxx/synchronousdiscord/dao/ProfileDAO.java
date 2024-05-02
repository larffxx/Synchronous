package com.larffxx.synchronousdiscord.dao;

import com.larffxx.synchronousdiscord.model.Profile;
import com.larffxx.synchronousdiscord.model.UsersConnect;
import com.larffxx.synchronousdiscord.repo.ProfileRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@Setter
public class ProfileDAO {

    private final ProfileRepository profileRepository;
    private final UsersConnectDAO usersConnectDAO;

    public ProfileDAO(ProfileRepository profileRepository, UsersConnectDAO usersConnectDAO) {
        this.profileRepository = profileRepository;
        this.usersConnectDAO = usersConnectDAO;
    }

    public Profile getProfile(String userName){
        return profileRepository.findByUsersConnect(usersConnectDAO.getByDiscordName(userName));
    }

    public boolean existsByUsersConnect(UsersConnect usersConnect){
        return profileRepository.existsByUsersConnect(usersConnect);
    }

    public void updateProfile(String description, String photoUrl, String socialUrl, UsersConnect usersConnect){
        profileRepository.updateAllByProfileData(description, photoUrl, socialUrl, usersConnect);
    }
    public void saveModel(Profile profile){
        profileRepository.save(profile);
    }
}
