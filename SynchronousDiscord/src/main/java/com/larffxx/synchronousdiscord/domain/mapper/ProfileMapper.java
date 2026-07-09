package com.larffxx.synchronousdiscord.domain.mapper;

import com.larffxx.synchronousdiscord.domain.model.Profile;
import com.larffxx.synchronousdiscord.domain.dto.ProfileDTO;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper implements Mapper<Profile,ProfileDTO> {
    public ProfileDTO toDTO(Profile profile) {
        ProfileDTO dto = new ProfileDTO();

        dto.setUsername(profile.getName());
        dto.setServersConnectId(profile.getServersConnect().getId());
        dto.setUsersConnectId(profile.getUsersConnect().getId());

        return dto;
    }
    public Profile toEntity(ProfileDTO dto) {
        Profile profile = new Profile();

        profile.setName(dto.getUsername());
        profile.setServersConnect(new ServersConnect(dto.getServersConnectId()));
        profile.setUsersConnect(new UsersConnect(dto.getUsersConnectId()));

        return profile;
    }
}
