package com.larffxx.synchronoustelegram.domain.mapper;

import com.larffxx.synchronoustelegram.domain.dto.ProfileDTO;
import com.larffxx.synchronoustelegram.domain.model.Profile;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;

/**
 * Converts between profile entities and profile transfer objects.
 * Resolves server and user links by identifier when building entities.
 */
public class ProfileMapper implements Mapper<Profile, ProfileDTO> {
    /**
     * Converts a profile entity to its transfer object.
     * @param profile the profile entity to convert
     * @return the converted transfer object
     */
    public ProfileDTO toDTO(Profile profile) {
        ProfileDTO dto = new ProfileDTO();

        dto.setUsername(profile.getName());
        dto.setServersConnectId(profile.getServersConnect().getId());
        dto.setUsersConnectId(profile.getUsersConnect().getId());

        return dto;
    }
    /**
     * Converts a profile transfer object to its entity.
     * @param dto the transfer object to convert
     * @return the converted profile entity
     */
    public Profile toEntity(ProfileDTO dto) {
        Profile profile = new Profile();

        profile.setName(dto.getUsername());
        profile.setServersConnect(new ServersConnect(dto.getServersConnectId()));
        profile.setUsersConnect(new UsersConnect(dto.getUsersConnectId()));

        return profile;
    }
}
