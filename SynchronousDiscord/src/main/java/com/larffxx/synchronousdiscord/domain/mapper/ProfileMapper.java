package com.larffxx.synchronousdiscord.domain.mapper;

import com.larffxx.synchronousdiscord.domain.model.Profile;
import com.larffxx.synchronousdiscord.domain.dto.ProfileDTO;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper implements Mapper<Profile,ProfileDTO> {
    public ProfileDTO toDTO(Profile profile) {
        ProfileDTO dto = new ProfileDTO();

        dto.setUsername(profile.getName());
        dto.setDiscordGuild(profile.getServersConnect().getDiscordGuild());
        dto.setDiscordId(profile.getUsersConnect().getDiscordId());

        return dto;
    }
    public Profile toEntity(ProfileDTO dto) {
        Profile profile = new Profile();
        profile.setName(dto.getUsername());

        return profile;
    }
}
