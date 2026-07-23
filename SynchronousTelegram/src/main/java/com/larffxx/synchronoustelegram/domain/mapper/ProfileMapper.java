package com.larffxx.synchronoustelegram.domain.mapper;

import com.larffxx.synchronoustelegram.domain.dto.ProfileDTO;
import com.larffxx.synchronoustelegram.domain.model.Profile;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;

public class ProfileMapper implements Mapper<Profile, ProfileDTO> {
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
